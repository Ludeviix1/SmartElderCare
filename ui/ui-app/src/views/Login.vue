<script setup>
  //定义数据模型
  import {ref} from "vue";
  import elderApi from "@/api/elder.js";
  import {showToast} from "vant";
  import {useRouter} from 'vue-router'
  const router = useRouter()
  import {useTokenStore} from '@/store/token.js'
  import {useElderInfoStore} from '@/store/elderInfo.js'
  const tokenStore = useTokenStore();
  const elderInfoStore = useElderInfoStore();

  const elder = ref({
    name: '',
    password: ''
  })

  const loading = ref(false)

  const login = () => {
    if (!elder.value.name || !elder.value.password) {
      showToast('请输入用户名和密码')
      return
    }
    loading.value = true
    elderApi.login(elder.value).then(result => {
      if (result.code == 1) {
        //登录成功，保存token
        tokenStore.setToken(result.data)
        //拉取当前登录老人的信息
        return elderApi.elderInfo().then(elderInfoResult => {
          if (elderInfoResult.code == 1) {
            elderInfoStore.setElderInfo(elderInfoResult.data)
          }
          showToast('登录成功')
          router.push('/')
        })
      } else {
        showToast(result.msg)
      }
    }).finally(() => {
      loading.value = false
    })
  }

</script>

<template>
  <div class="login-page">
    <div class="login-bg">
      <div class="logo-circle">
        <span class="logo-icon">❤</span>
      </div>
      <h1 class="app-title">智慧养老社区</h1>
      <p class="app-subtitle">贴心守护 · 安享晚年</p>
    </div>

    <div class="login-form">
      <van-cell-group inset>
        <van-field
            v-model="elder.name"
            label="用户名"
            placeholder="请输入老人姓名"
            left-icon="manager"
            clearable
        />
        <van-field
            v-model="elder.password"
            type="password"
            label="密码"
            placeholder="请输入密码"
            left-icon="lock"
        />
      </van-cell-group>
      <div class="login-btn">
        <van-button type="primary" block round :loading="loading" loading-text="登录中..." @click="login">
          登 录
        </van-button>
      </div>
      <p class="login-tip">账号为老人姓名，密码请联系社区管理员</p>
    </div>
  </div>
</template>

<style scoped lang="scss">
  .login-page {
    min-height: 100vh;
    background-color: #fff;
    display: flex;
    flex-direction: column;
  }

  .login-bg {
    padding: 80px 0 50px;
    text-align: center;
    background: linear-gradient(180deg, #1989fa 0%, #e8f3ff 100%);

    .logo-circle {
      width: 72px;
      height: 72px;
      margin: 0 auto 16px;
      border-radius: 50%;
      background-color: #fff;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4px 12px rgba(25, 137, 250, 0.3);

      .logo-icon {
        font-size: 36px;
        color: #1989fa;
      }
    }

    .app-title {
      margin: 0;
      font-size: 24px;
      color: #fff;
      letter-spacing: 2px;
    }

    .app-subtitle {
      margin: 8px 0 0;
      font-size: 13px;
      color: rgba(255, 255, 255, 0.9);
    }
  }

  .login-form {
    flex: 1;
    padding: 40px 16px 0;

    .login-btn {
      margin: 32px 16px 0;
    }

    .login-tip {
      margin-top: 20px;
      text-align: center;
      font-size: 12px;
      color: #969799;
    }
  }
</style>
