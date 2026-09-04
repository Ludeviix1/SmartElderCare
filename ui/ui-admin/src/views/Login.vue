<script setup>
  //定义数据模型
  import {ref} from "vue";
  import {User, Lock} from "@element-plus/icons-vue";
  import userApi from "@/api/user.js";
  import {ElMessage} from "element-plus";
  import {useRouter} from 'vue-router'
  const router = useRouter()
  import {useTokenStore} from '@/store/token.js'
  const tokenStore = useTokenStore();

  const user = ref({
    name: '',
    password: ''
  })

  const login = () => {
    console.log(user.value)
    userApi.login(user.value).then(result => {
      if (result.code == 1) {
        ElMessage.success(result.msg)
        tokenStore.setToken(result.data)
        router.push('/')
      } else {
        ElMessage.error(result.msg)
      }

    })
  }

  //表单校验模型
  const rules = ref({
    name: [
      {required: true, message: '请输入用户名', trigger: 'blur'},
      {min: 3, max: 16, message: '用户名的长度必须为3~16位', trigger: 'blur'}
    ],
    password: [
      {required: true, message: '请输入密码', trigger: 'blur'},
      {min: 3, max: 16, message: '密码长度必须为3~16位', trigger: 'blur'}
    ]
  })

</script>

<template>
  <div class="login-bg">
    <!-- 登录表单 -->
    <el-form class="form-login" ref="form" size="large" autocomplete="off" :model="user" :rules="rules">
      <el-form-item>
        <h1>登录</h1>
      </el-form-item>
      <el-form-item prop="name">
        <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="user.name"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码"
                  v-model="user.password"></el-input>
      </el-form-item>
      <el-form-item class="flex">
        <div class="flex">
          <el-checkbox>记住我</el-checkbox>
          <el-link type="primary" :underline="false">忘记密码？</el-link>
        </div>
      </el-form-item>
      <!-- 登录按钮 -->
      <el-form-item>
        <el-button class="button" type="primary" auto-insert-space @click="login">登录</el-button>
      </el-form-item>
    </el-form>
  </div>

</template>

<style scoped>

  .login-bg {
    min-height: 100vh;
    padding: 48px 10vw 48px 24px;
    background-color: #eaf1ed;
    background-image: url('@/assets/login-background.png');
    background-repeat: no-repeat;
    background-position: center;
    background-attachment: fixed;
    background-size: cover;
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }

  .form-login {
    width: min(380px, 100%);
    padding: 32px;
    background-color: rgba(255, 255, 255, 0.96);
    border: 1px solid rgba(255, 255, 255, 0.7);
    border-radius: 8px;
    box-shadow: 0 18px 42px rgba(22, 63, 58, 0.24);
  }

  .form-login :deep(h1) {
    margin: 0 0 8px;
    color: #173b36;
    font-size: 24px;
    font-weight: 650;
    letter-spacing: 0;
    text-align: center;
  }

  .form-login :deep(.el-input__wrapper) {
    min-height: 44px;
    background: #f7faf8;
    box-shadow: 0 0 0 1px #d8e4df inset;
  }

  .form-login :deep(.button) {
    width: 100%;
    min-height: 44px;
    font-weight: 600;
  }

  @media (max-width: 520px) {
    .login-bg {
      padding: 24px 16px;
      justify-content: center;
    }

    .form-login {
      padding: 26px 22px;
    }
  }
</style>
