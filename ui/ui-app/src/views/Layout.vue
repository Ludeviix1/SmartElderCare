<script setup>
  import {useRoute} from 'vue-router'
  import {ref, watch} from 'vue'

  const route = useRoute()
  //底部Tabbar当前选中的菜单
  const active = ref(0)

  //路由和Tabbar序号的对应
  const tabRoutes = ['/home', '/package', '/appointment', '/profile']
  watch(() => route.path, (path) => {
    const index = tabRoutes.indexOf(path)
    if (index !== -1) {
      active.value = index
    }
  }, {immediate: true})
</script>

<template>
  <div class="layout">
    <!--中间内容区-->
    <div class="layout-body">
      <router-view></router-view>
    </div>
    <!--底部导航-->
    <van-tabbar v-model="active" route placeholder>
      <van-tabbar-item replace to="/home" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item replace to="/package" icon="shopping-cart-o">体检套餐</van-tabbar-item>
      <van-tabbar-item replace to="/appointment" icon="clock-o">我的预约</van-tabbar-item>
      <van-tabbar-item replace to="/profile" icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<style scoped lang="scss">
  .layout {
    min-height: 100vh;
  }
</style>
