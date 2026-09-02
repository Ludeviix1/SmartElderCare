<script setup>
  import {computed, onMounted, ref} from 'vue'
  import {showConfirmDialog, showLoadingToast, showToast} from 'vant'
  import {useRouter} from 'vue-router'
  import appointmentApi from '@/api/appointment.js'
  import {useAppointmentStore} from '@/store/appointment.js'
  const router = useRouter()
  const appointmentStore = useAppointmentStore();

  //加载状态
  const loading = ref(true)

  //拉取后端预约列表
  const loadList = () => {
    loading.value = true
    appointmentApi.list().then(result => {
      if (result.code == 1) {
        appointmentStore.setAppointmentList(result.data)
      }
    }).finally(() => {
      loading.value = false
    })
  }

  onMounted(() => {
    //列表数据变化过（提交/取消预约）或第一次进入时拉取
    if (appointmentStore.dirty) {
      loadList()
    } else {
      loading.value = false
    }
  })

  //状态筛选Tab：全部/待体检/已完成/已取消
  const activeTab = ref(0)
  const statusTabs = [
    {title: '全部'},
    {title: '待体检', status: 0},
    {title: '已完成', status: 2},
    {title: '已取消', status: 3}
  ]

  const filteredList = computed(() => {
    const status = statusTabs[activeTab.value].status
    if (status === undefined) {
      return appointmentStore.appointmentList
    }
    return appointmentStore.appointmentList.filter(item => item.status === status)
  })

  //状态（与后端一致：0待体检 1体检中 2已完成 3已取消 4已过期）
  const statusOptions = [
    {value: 0, label: '待体检', color: '#1989fa'},
    {value: 1, label: '体检中', color: '#ff976a'},
    {value: 2, label: '已完成', color: '#07c160'},
    {value: 3, label: '已取消', color: '#969799'},
    {value: 4, label: '已过期', color: '#969799'}
  ]

  const statusText = (status) => {
    const statusObj = statusOptions.find(item => item.value === status)
    return statusObj ? statusObj.label : '未知'
  }

  const statusColor = (status) => {
    const statusObj = statusOptions.find(item => item.value === status)
    return statusObj ? statusObj.color : '#969799'
  }

  //取消预约
  const cancelAppointment = (appointment) => {
    showConfirmDialog({
      title: '取消预约',
      message: `确定要取消【${appointment.packageName}】的预约吗？`
    }).then(() => {
      const toast = showLoadingToast({message: '取消中...', forbidClick: true, duration: 0})
      appointmentApi.cancel(appointment.id).then(result => {
        if (result.code == 1) {
          showToast('已取消')
          //标记数据已变化，重新拉取列表
          appointmentStore.markDirty()
          loadList()
        } else {
          showToast(result.msg)
        }
      }).finally(() => {
        toast.close()
      })
    }).catch(() => {})
  }
</script>

<template>
  <div class="appointment-page">
    <van-nav-bar title="我的预约" fixed placeholder/>

    <van-tabs v-model:active="activeTab" sticky offset-top="46px">
      <van-tab v-for="tab in statusTabs" :key="tab.title" :title="tab.title"/>
    </van-tabs>

    <van-loading v-if="loading" class="page-loading" size="24" vertical>加载中...</van-loading>

    <van-pull-refresh v-else :model-value="false" @refresh="loadList">
    <!--预约列表-->
    <div v-if="filteredList.length">
      <div v-for="appointment in filteredList" :key="appointment.id" class="appointment-card">
        <div class="card-top" @click="router.push('/package/' + appointment.packageId)">
          <div class="package-name">{{ appointment.packageName }}</div>
          <div class="status" :style="{color: statusColor(appointment.status)}">{{ statusText(appointment.status) }}</div>
        </div>
        <div class="card-info">
          <div class="info-row">
            <van-icon name="friends-o"/>
            <span>体检人：{{ appointment.elderName }}</span>
          </div>
          <div class="info-row">
            <van-icon name="calendar-o"/>
            <span>{{ appointment.appointmentDate }} {{ appointment.appointmentTime }}</span>
          </div>
          <div class="info-row">
            <van-icon name="apps-o"/>
            <span>共{{ appointment.examItemCount }}项检查</span>
          </div>
        </div>
        <div class="card-footer">
          <span class="price">¥{{ appointment.price }}</span>
          <van-button v-if="appointment.status === 0" size="small" plain round type="danger" @click="cancelAppointment(appointment)">
            取消预约
          </van-button>
          <van-button v-else-if="appointment.status === 2" size="small" plain round type="primary">
            查看报告
          </van-button>
        </div>
      </div>
    </div>

    <!--空状态-->
    <van-empty v-else description="暂无预约，去挑选一个体检套餐吧">
      <van-button round type="primary" size="small" style="width: 120px" @click="router.push('/package')">
        去预约
      </van-button>
    </van-empty>
    </van-pull-refresh>
  </div>
</template>

<style scoped lang="scss">
  .appointment-page {
    padding-bottom: 20px;
  }

  .page-loading {
    padding: 60px 0;
  }

  .appointment-card {
    margin: 12px 16px 0;
    padding: 12px 14px;
    border-radius: 12px;
    background-color: #fff;

    .card-top {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .package-name {
        font-size: 15px;
        font-weight: bold;
        color: #323233;
      }

      .status {
        font-size: 12px;
        font-weight: bold;
      }
    }

    .card-info {
      margin-top: 10px;
      display: flex;
      flex-direction: column;
      gap: 6px;

      .info-row {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 12px;
        color: #646566;

        .van-icon {
          color: #969799;
        }
      }
    }

    .card-footer {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-top: 10px;
      padding-top: 10px;
      border-top: 1px solid #f2f3f5;

      .price {
        font-size: 16px;
        font-weight: bold;
        color: #ee0a24;
      }
    }
  }
</style>
