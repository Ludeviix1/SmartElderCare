<script setup>
  import homeApi from '@/api/home.js'
  import examPackageApi from '@/api/examPackage.js'
  import {onMounted, ref, computed} from 'vue'
  import {useElderInfoStore} from '@/store/elderInfo.js'
  import {useAppointmentStore} from '@/store/appointment.js'
  import {useRouter} from 'vue-router'
  const router = useRouter()
  const elderInfoStore = useElderInfoStore();
  const appointmentStore = useAppointmentStore();

  const greeting = computed(() => '你好，' + elderInfoStore.elder.name)
  const healthData = ref({})
  const notices = ref([])
  const noticeShow = ref(false)
  //热门套餐（后端无gradient字段，前端按下标取色板）
  const gradients = [
    'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  ]
  const hotPackages = ref([])

  onMounted(() => {
    homeApi.healthData().then(result => {
      healthData.value = result.data
    })
    homeApi.notices().then(result => {
      notices.value = result.data
    })
    //取前两个上架套餐作为热门推荐
    examPackageApi.list().then(result => {
      if (result.code == 1) {
        hotPackages.value = result.data.slice(0, 2).map((examPackage, index) => ({
          ...examPackage,
          gradient: gradients[index % gradients.length]
        }))
      }
    })
  })

  //快捷入口
  const shortcuts = [
    {name: '体检预约', icon: 'calendar-o', color: '#1989fa', to: '/package'},
    {name: '我的预约', icon: 'clock-o', color: '#07c160', to: '/appointment'},
    {name: '健康档案', icon: 'records', color: '#ff976a', to: '/profile'},
    {name: '在线咨询', icon: 'chat-o', color: '#ee0a24', to: '/profile'}
  ]
</script>

<template>
  <div class="home">
    <!--头部问候-->
    <div class="home-header">
      <div class="greeting">{{ greeting }}</div>
      <div class="date">{{ elderInfoStore.elder.address }} · 今天也要注意身体哦</div>
    </div>

    <!--健康数据卡片-->
    <div class="health-card">
      <div class="health-title">今日健康数据</div>
      <div class="health-grid">
        <div class="health-item">
          <div class="value">{{ healthData.bloodPressure }}</div>
          <div class="label">血压(mmHg)</div>
        </div>
        <div class="health-item">
          <div class="value">{{ healthData.heartRate }}</div>
          <div class="label">心率(次/分)</div>
        </div>
        <div class="health-item">
          <div class="value">{{ healthData.bloodSugar }}</div>
          <div class="label">血糖(mmol/L)</div>
        </div>
      </div>
    </div>

    <!--快捷入口-->
    <div class="shortcuts">
      <div v-for="item in shortcuts" :key="item.name" class="shortcut-item" @click="item.to && router.push(item.to)">
        <van-icon :name="item.icon" :color="item.color" size="26"/>
        <span>{{ item.name }}</span>
      </div>
    </div>

    <!--公告-->
    <van-cell-group inset class="notice-cell">
      <van-notice-bar left-icon="volume-o" :text="notices[0] ? notices[0].title : '暂无公告'" @click="noticeShow = true" color="#1989fa" background="#f0f7ff"/>
    </van-cell-group>

    <!--推荐套餐-->
    <div class="section">
      <div class="section-title">
        <span class="bar"></span>
        热门体检套餐
      </div>
      <div v-for="examPackage in hotPackages" :key="examPackage.id" class="package-row" @click="router.push('/package/' + examPackage.id)">
        <div class="package-thumb" :style="{background: examPackage.gradient}">{{ examPackage.name.slice(0, 2) }}</div>
        <div class="package-info">
          <div class="name">{{ examPackage.name }}</div>
          <div class="desc">{{ examPackage.description }}</div>
        </div>
        <div class="price">¥{{ examPackage.price }}</div>
      </div>
    </div>

    <!--待体检提醒-->
    <van-cell-group inset v-if="appointmentStore.pendingList.length">
      <van-cell title="您有预约待体检" :value="appointmentStore.pendingList.length + '个'" is-link @click="router.push('/appointment')"/>
    </van-cell-group>

    <!--公告弹层-->
    <van-popup v-model:show="noticeShow" position="bottom" round>
      <div class="notice-popup">
        <div class="notice-popup-title">社区公告</div>
        <div v-for="notice in notices" :key="notice.id" class="notice-item">
          <div class="notice-content">{{ notice.title }}</div>
          <div class="notice-date">{{ notice.date }}</div>
        </div>
      </div>
    </van-popup>
  </div>
</template>

<style scoped lang="scss">
  .home {
    padding-bottom: 20px;
  }

  .home-header {
    padding: 20px 16px 40px;
    background: linear-gradient(180deg, #1989fa 0%, #5cadff 60%, #f5f6f8 100%);

    .greeting {
      font-size: 20px;
      font-weight: bold;
      color: #fff;
    }

    .date {
      margin-top: 6px;
      font-size: 12px;
      color: rgba(255, 255, 255, 0.9);
    }
  }

  .health-card {
    margin: -24px 16px 0;
    padding: 16px;
    border-radius: 12px;
    background-color: #fff;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);

    .health-title {
      font-size: 15px;
      font-weight: bold;
      color: #323233;
      margin-bottom: 12px;
    }

    .health-grid {
      display: flex;

      .health-item {
        flex: 1;
        text-align: center;

        .value {
          font-size: 20px;
          font-weight: bold;
          color: #1989fa;
        }

        .label {
          margin-top: 4px;
          font-size: 11px;
          color: #969799;
        }
      }
    }
  }

  .shortcuts {
    display: flex;
    margin: 16px 16px 0;
    padding: 16px 0;
    border-radius: 12px;
    background-color: #fff;

    .shortcut-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px;
      font-size: 12px;
      color: #323233;
    }
  }

  .notice-cell {
    margin-top: 16px;
  }

  .section {
    margin: 16px 16px 0;

    .section-title {
      display: flex;
      align-items: center;
      font-size: 15px;
      font-weight: bold;
      color: #323233;
      margin-bottom: 12px;

      .bar {
        width: 4px;
        height: 16px;
        margin-right: 8px;
        border-radius: 2px;
        background-color: #1989fa;
      }
    }
  }

  .package-row {
    display: flex;
    align-items: center;
    padding: 12px;
    margin-bottom: 10px;
    border-radius: 12px;
    background-color: #fff;

    .package-thumb {
      width: 52px;
      height: 52px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 14px;
      font-weight: bold;
    }

    .package-info {
      flex: 1;
      margin: 0 12px;
      overflow: hidden;

      .name {
        font-size: 15px;
        font-weight: bold;
        color: #323233;
      }

      .desc {
        margin-top: 4px;
        font-size: 11px;
        color: #969799;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }

    .price {
      font-size: 16px;
      font-weight: bold;
      color: #ee0a24;
    }
  }

  .notice-popup {
    padding: 20px 16px 30px;

    .notice-popup-title {
      text-align: center;
      font-size: 16px;
      font-weight: bold;
      color: #323233;
      margin-bottom: 16px;
    }

    .notice-item {
      padding: 12px 0;
      border-bottom: 1px solid #f2f3f5;

      .notice-content {
        font-size: 14px;
        color: #323233;
        line-height: 1.5;
      }

      .notice-date {
        margin-top: 6px;
        font-size: 11px;
        color: #969799;
      }
    }
  }
</style>
