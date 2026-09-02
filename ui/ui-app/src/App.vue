<script setup>
</script>

<template>
  <router-view></router-view>
</template>

<style lang="scss">
  /*全局基础样式：移动端页面背景*/
  html, body {
    margin: 0;
    padding: 0;
    background-color: #f5f6f8;
    -webkit-tap-highlight-color: transparent; /*去除移动端点击高亮*/
  }

  #app {
    /*内容区最大宽度：桌面浏览器打开时模拟手机屏*/
    max-width: 480px;
    margin: 0 auto;
    min-height: 100vh;
    background-color: #f5f6f8;
  }
</style>
