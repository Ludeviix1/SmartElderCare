<script setup>
  import {onMounted, ref} from 'vue'
  import {useRouter} from 'vue-router'
  import {showConfirmDialog, showToast} from 'vant'
  import {useTokenStore} from '@/store/token.js'
  import {useElderInfoStore} from '@/store/elderInfo.js'
  import {useAppointmentStore} from '@/store/appointment.js'
  import examPackageApi from '@/api/examPackage.js'
  const router = useRouter()
  const tokenStore = useTokenStore();
  const elderInfoStore = useElderInfoStore();
  const appointmentStore = useAppointmentStore();

  //上架的可用套餐数量
  const packageCount = ref(0)
  onMounted(() => {
    examPackageApi.list().then(result => {
      if (result.code == 1) {
        packageCount.value = result.data.length
      }
    })
  })

  //退出登录
  const logout = () => {
    showConfirmDialog({
      title: '退出登录',
      message: '确定要退出登录吗？'
    }).then(() => {
      tokenStore.removeToken()
      elderInfoStore.removeElderInfo()
      //清掉这个账号的预约数据，避免换账号登录后串数据
      appointmentStore.reset()
      showToast('已退出')
      router.push('/login')
    }).catch(() => {})
  }
</script>

<template>
  <div class="profile-page">
    <van-nav-bar title="我的" fixed placeholder/>

    <!--用户信息卡片-->
    <div class="elder-card">
      <div class="avatar">
        <van-icon name="elder-circle-o" size="56" color="#fff"/>
      </div>
      <div class="elder-info">
        <div class="name">{{ elderInfoStore.elder.name }}</div>
        <div class="room">{{ elderInfoStore.elder.address }} · {{ elderInfoStore.elder.age }}岁</div>
      </div>
    </div>

    <!--预约统计-->
    <div class="stat-card">
      <div class="stat-item" @click="router.push('/appointment')">
        <div class="stat-value">{{ appointmentStore.appointmentList.length }}</div>
        <div class="stat-label">全部预约</div>
      </div>
      <div class="stat-item" @click="router.push('/appointment')">
        <div class="stat-value" style="color: #1989fa">{{ appointmentStore.pendingList.length }}</div>
        <div class="stat-label">待体检</div>
      </div>
      <div class="stat-item" @click="router.push('/package')">
        <div class="stat-value" style="color: #07c160">{{ packageCount }}</div>
        <div class="stat-label">可用套餐</div>
      </div>
    </div>

    <!--菜单-->
    <van-cell-group inset class="menu-card">
      <van-cell title="个人资料" icon="elder-o" is-link/>
      <van-cell title="健康档案" icon="records" is-link/>
      <van-cell title="我的预约" icon="clock-o" is-link @click="router.push('/appointment')"/>
      <van-cell title="体检报告" icon="notes-o" is-link/>
      <van-cell title="联系客服" icon="service-o" is-link/>
      <van-cell title="关于我们" icon="info-o" is-link/>
    </van-cell-group>

    <!--退出登录-->
    <div class="logout-btn">
      <van-button block round type="danger" plain @click="logout">退出登录</van-button>
    </div>
  </div>
</template>

<style scoped lang="scss">
  .profile-page {
    padding-bottom: 30px;
  }

  .elder-card {
    display: flex;
    align-items: center;
    margin: 12px 16px 0;
    padding: 20px 16px;
    border-radius: 12px;
    background: linear-gradient(135deg, #1989fa 0%, #5cadff 100%);

    .avatar {
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .elder-info {
      flex: 1;
      margin-left: 14px;

      .name {
        font-size: 20px;
        font-weight: bold;
        color: #fff;
      }

      .room {
        margin-top: 6px;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.9);
      }
    }
  }

  .stat-card {
    display: flex;
    margin: 12px 16px 0;
    padding: 16px 0;
    border-radius: 12px;
    background-color: #fff;

    .stat-item {
      flex: 1;
      text-align: center;

      .stat-value {
        font-size: 20px;
        font-weight: bold;
        color: #323233;
      }

      .stat-label {
        margin-top: 4px;
        font-size: 11px;
        color: #969799;
      }
    }
  }

  .menu-card {
    margin-top: 16px;
  }

  .logout-btn {
    margin: 24px 16px 0;
  }
</style>
