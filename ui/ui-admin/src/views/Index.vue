<script setup>
  import {
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom,
    Plus
  } from '@element-plus/icons-vue'
  //条目被点击后,调用的函数
  import {useRouter, useRoute} from 'vue-router'
  const router = useRouter();
  const route = useRoute();
  import * as ElementPlusIconsVue from '@element-plus/icons-vue'
  import userApi from "@/api/user.js";
  import dashboardApi from "@/api/dashboard.js";
  import {useTokenStore} from '@/store/token.js'
  const tokenStore = useTokenStore();
  import {useUserInfoStore} from '@/store/userInfo.js'
  import {ref, onMounted, onBeforeUnmount, nextTick, watch} from "vue";
  import * as echarts from 'echarts';
  import {ElMessage} from "element-plus";
  const userInfoStore = useUserInfoStore();

  const occupancyChart = ref(null)
  const trendChart = ref(null)
  const taskChart = ref(null)
  const dashboard = ref({
    residentCount: 0, occupiedBeds: 0, availableBeds: 0, totalBeds: 0,
    todayTasks: 0, completedToday: 0, examPending: 0, examCompleted: 0,
    trendDates: [], taskTotals: [], taskCompleted: [], taskPending: 0, taskSkipped: 0
  })
  let charts = []

  const completionRate = () => dashboard.value.todayTasks
    ? Math.round(dashboard.value.completedToday * 100 / dashboard.value.todayTasks)
    : 0
  const occupancyRate = () => dashboard.value.totalBeds
    ? Math.round(dashboard.value.occupiedBeds * 100 / dashboard.value.totalBeds)
    : 0

  const initLegacyDashboardCharts = async () => {
    await nextTick()
    const data = dashboard.value
    const chartOptions = [
      {
        el: occupancyChart.value,
        option: {
          tooltip: {trigger: 'item'},
          legend: {bottom: 0, left: 'center', itemWidth: 10, itemHeight: 10, textStyle: {color: '#6d7d78'}},
          series: [{type: 'pie', radius: ['58%', '78%'], center: ['50%', '45%'], avoidLabelOverlap: false,
            label: {show: true, position: 'center', formatter: '入住率\\n{big|86%}', rich: {big: {fontSize: 23, fontWeight: 700, color: '#20332f', lineHeight: 34}, color: '#6d7d78', lineHeight: 20}},
            data: [{value: 86, name: '已入住', itemStyle: {color: '#16736a'}}, {value: 14, name: '空置床位', itemStyle: {color: '#dfeae5'}}]}]
        }
      },
      {
        el: trendChart.value,
        option: {
          tooltip: {trigger: 'axis'},
          grid: {left: 38, right: 20, top: 18, bottom: 28},
          xAxis: {type: 'category', boundaryGap: false, data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'], axisLine: {lineStyle: {color: '#dce5e0'}}, axisLabel: {color: '#6d7d78'}},
          yAxis: {type: 'value', min: 0, max: 100, splitNumber: 4, splitLine: {lineStyle: {color: '#edf2ef'}}, axisLabel: {color: '#8a9893'}},
          series: [{name: '护理完成率', type: 'line', smooth: true, symbol: 'circle', symbolSize: 7, data: [76, 82, 79, 88, 91, 86, 94], lineStyle: {width: 3, color: '#c76b43'}, itemStyle: {color: '#c76b43'}, areaStyle: {color: 'rgba(199,107,67,.12)'}}]
        }
      },
      {
        el: taskChart.value,
        option: {
          tooltip: {trigger: 'item'},
          series: [{type: 'pie', radius: ['52%', '76%'], center: ['50%', '45%'], label: {show: false}, data: [
            {value: 68, name: '已完成', itemStyle: {color: '#16736a'}}, {value: 18, name: '进行中', itemStyle: {color: '#d49a47'}}, {value: 6, name: '待处理', itemStyle: {color: '#dce5e0'}}
          ]}]
        }
      }
    ]
    charts = chartOptions.filter(item => item.el).map(item => {
      const chart = echarts.init(item.el)
      chart.setOption(item.option)
      return chart
    })
  }

  const initDashboardCharts = async () => {
    await nextTick()
    const data = dashboard.value
    const occupancy = echarts.init(occupancyChart.value)
    occupancy.setOption({
      tooltip: {trigger: 'item'},
      legend: {bottom: 0, left: 'center', itemWidth: 10, itemHeight: 10, textStyle: {color: '#6d7d78'}},
      series: [{type: 'pie', radius: ['58%', '78%'], center: ['50%', '45%'], avoidLabelOverlap: false,
        label: {show: true, position: 'center', formatter: `入住率\n{big|${occupancyRate()}%}`, rich: {big: {fontSize: 23, fontWeight: 700, color: '#20332f', lineHeight: 34}, color: '#6d7d78', lineHeight: 20}},
        data: [{value: data.occupiedBeds, name: '已入住', itemStyle: {color: '#16736a'}}, {value: data.availableBeds, name: '空置床位', itemStyle: {color: '#dfeae5'}}]}]
    })

    const trend = echarts.init(trendChart.value)
    trend.setOption({
      tooltip: {trigger: 'axis'},
      grid: {left: 38, right: 20, top: 18, bottom: 28},
      xAxis: {type: 'category', boundaryGap: false, data: data.trendDates.map(date => date.slice(5)), axisLine: {lineStyle: {color: '#dce5e0'}}, axisLabel: {color: '#6d7d78'}},
      yAxis: {type: 'value', min: 0, max: 100, splitNumber: 4, splitLine: {lineStyle: {color: '#edf2ef'}}, axisLabel: {color: '#8a9893', formatter: '{value}%'}},
      series: [{name: '护理完成率', type: 'line', smooth: true, symbol: 'circle', symbolSize: 7, data: data.taskTotals.map((total, index) => total ? Math.round(data.taskCompleted[index] * 100 / total) : 0), lineStyle: {width: 3, color: '#c76b43'}, itemStyle: {color: '#c76b43'}, areaStyle: {color: 'rgba(199,107,67,.12)'}}]
    })

    const task = echarts.init(taskChart.value)
    task.setOption({
      tooltip: {trigger: 'item'},
      series: [{type: 'pie', radius: ['52%', '76%'], center: ['50%', '45%'], label: {show: false}, data: [
        {value: data.completedToday, name: '已完成', itemStyle: {color: '#16736a'}},
        {value: data.taskPending, name: '待执行', itemStyle: {color: '#d49a47'}},
        {value: data.taskSkipped, name: '已取消', itemStyle: {color: '#dce5e0'}}
      ]}]
    })
    charts = [occupancy, trend, task]
  }

  const loadDashboard = async () => {
    if (route.path !== '/') return
    const result = await dashboardApi.overview()
    if (result.code === 1) {
      dashboard.value = {...dashboard.value, ...result.data}
      charts.forEach(chart => chart.dispose())
      charts = []
      await initDashboardCharts()
    }
  }

  const resizeCharts = () => charts.forEach(chart => chart.resize())
  watch(() => route.path, async (path) => {
    charts.forEach(chart => chart.dispose())
    charts = []
    if (path === '/') await loadDashboard()
  })
  onMounted(() => { loadDashboard(); window.addEventListener('resize', resizeCharts) })
  onBeforeUnmount(() => { window.removeEventListener('resize', resizeCharts); charts.forEach(chart => chart.dispose()) })

  const dialogFormVisible = ref(false)
  const user = ref({})

  const handleCommand = (command) => {
    //判断指令
    if (command === 'logout') {
      //退出登录
      tokenStore.removeToken();
      router.push('/login')
    } else if (command === 'updateUserInfo') {
      dialogFormVisible.value = true
      //user.value = userInfoStore.user
      Object.assign(user.value, userInfoStore.user)
    } else if (command === 'resetPassword'){
      dialogResetPasswordDialog.value = true
      userPasswordDTO.value = {}
      //resetForm.value.resetFields()
    } else {
      //路由
      router.push('/user/' + command)
    }
  }

  //获取用户信息
  const getUserInfo = () => {
    userApi.userInfo().then(result => {
      if (result.code === 1) {
        userInfoStore.setUserInfo(result.data.user)
        menuData.value = normalizeMenuData(result.data.routerList)
        userInfoStore.setBtnList(result.data.btnList)
      }
    })
  }
  getUserInfo()

  //上传头像
  const handleAvatarSuccess = (result) => {
    console.log(result)
    user.value.avatar = result.data
  }

  const updateUserInfo = () => {
    userApi.update(user.value.id, user.value).then(result => {
      if (result.code === 1) {
        ElMessage.success(result.msg)
        dialogFormVisible.value = false
        getUserInfo()
      }
    })
  }

  //重置密码
  const userPasswordDTO = ref({
    'oldPassword': '',
    'newPassword': ''
  });
  const dialogResetPasswordDialog = ref(false)

  //自定义确认密码的校验函数
  const rePasswordValid = (rule, value, callback) => {
    if (value == null || value === '') {
      return callback(new Error('请再次确认密码'))
    }
    //响应式对象要：registerData.value才能拿到值
    if (userPasswordDTO.value.newPassword !== value) {
      return callback(new Error('两次输入密码不一致'))
    }

    callback()
  }

  const rules = ref({
    oldPassword: [
      {required: true, message: '请输入密码', trigger: 'blur'},
      {min: 3, max: 16, message: '密码长度必须为3~16位', trigger: 'blur'}
    ],
    newPassword: [
      {required: true, message: '请输入密码', trigger: 'blur'},
      {min: 3, max: 16, message: '密码长度必须为3~16位', trigger: 'blur'}
    ],
    reNewPassword: [
      {required: true, message: '请输入密码', trigger: 'blur'},
      {validator: rePasswordValid, trigger: 'blur' }
    ]
  })
  const resetForm = ref()
  const resetPassword = async (formEl) => {
    if (!formEl) return
    await formEl.validate((valid) => {
      if (valid) {
        userApi.resetPassword(userPasswordDTO.value).then(result => {
          if (result.code === 1) {
            ElMessage.success(result.msg)
            dialogResetPasswordDialog.value = false
            tokenStore.removeToken();
            userInfoStore.removeUserInfo();
            // 跳转到登录
            router.push('/login')
          } else {
            ElMessage.error(result.msg)
          }
        })
      } else {
        ElMessage.error('表单验证失败');
      }
    })
  }

  // 菜单  用户管理， 分类管理， 商品管理
  const menuData = ref([
    {name: '首页', icon: 'HomeFilled', path: "/"},
    {name: '家属管理', icon: 'UserFilled', path: "/family-member"},
    {name: '床位管理', icon: 'House', path: "/bed"},
    {name: '老人管理', icon: 'Notebook', path: "/elder"},
    {name: '标签管理', icon: 'TrendCharts', path: "/tag"},
    {name: '护理项目管理', icon: 'FirstAidKit', path: "/care-item"},
    {name: '护理等级管理', icon: 'Medal', path: "/care-level"},
    {name: '体检项目管理', icon: 'DataAnalysis', path: "/exam-item"},
    {name: '体检套餐管理', icon: 'Present', path: "/exam-package"},
    {
      name: '权限管理', icon: 'GobletFull', children: [
        {name: '管理员管理', icon: 'GobletSquareFull', path: "/user"},
        {name: '角色管理', icon: 'TrendCharts', path: "/role"},
        {name: '权限管理', icon: 'TrendCharts', path: "/permission"},
      ]
    }
  ]);

  // 后端允许配置图标名称，但非法名称会让 Vue 调用 createElement 时直接崩溃，导致整个侧边栏失效。
  const getIcon = (icon) => {
    return typeof icon === 'string' && Object.prototype.hasOwnProperty.call(ElementPlusIconsVue, icon)
        ? icon
        : 'Menu'
  }

  const normalizeMenuData = (menus = []) => {
    const normalizedMenus = menus.map((menu, index) => ({
      ...menu,
      icon: getIcon(menu.icon),
      children: menu.children?.map(child => ({
        ...child,
        icon: getIcon(child.icon)
      })) ?? [],
      // 保留后端返回顺序，作为 sort 相同时的稳定排序依据
      _menuIndex: index
    }))

    // 权限管理是固定的兜底目录：无论后端新增多少栏目，都始终放在最后。
    const permissionMenu = normalizedMenus.filter(menu =>
        menu.name === '权限管理' && !menu.path
    )
    const otherMenus = normalizedMenus.filter(menu =>
        !(menu.name === '权限管理' && !menu.path)
    )

    // sort 越小越靠前；未设置 sort 的新栏目排在已有栏目之后。
    otherMenus.sort((a, b) => {
      const aSort = Number.isFinite(a.sort) ? a.sort : Number.MAX_SAFE_INTEGER
      const bSort = Number.isFinite(b.sort) ? b.sort : Number.MAX_SAFE_INTEGER
      return aSort - bSort || a._menuIndex - b._menuIndex
    })

    const orderedMenus = [...otherMenus, ...permissionMenu].map(({_menuIndex, ...menu}) => menu)
    if (!orderedMenus.some(menu => menu.path === '/')) {
      orderedMenus.unshift({name: '首页', icon: 'HomeFilled', path: '/'})
    }
    return orderedMenus
  }


</script>

<template>
  <!-- element-plus中的容器 -->
  <el-container class="layout-container">
    <!-- 左侧菜单 -->
    <el-aside width="200px">
      <div class="el-aside__logo"></div>
      <!-- element-plus的菜单标签 -->
      <el-menu active-text-color="#126b62" background-color="#ffffff" text-color="#4d625c" router :default-active="route.path">
        <!-- 动态生成菜单 -->
        <template v-for="(menu, index) in menuData">
          <el-sub-menu v-if="menu.children?.length>0" :index="menu.path || ('sub-' + index)">
            <template #title>
              <component
                  class="icons"
                  :is="getIcon(menu.icon)"
                  style="width: 1em; height: 1em; margin-right: 8px" >
              </component>
              <span>{{ menu.name }}</span>
            </template>
            <el-menu-item v-for="child in menu.children" :index="child.path">
              <el-icon><component :is="getIcon(child.icon)"></component></el-icon>
              <span>{{ child.name }}</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item v-else :index="menu.path">
            <el-icon><component :is="getIcon(menu.icon)"></component></el-icon>
            <span>{{ menu.name }}</span>
          </el-menu-item>
        </template>
      </el-menu>

<!-- <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff" router>
        <el-menu-item index="/elder">
          <el-icon>
            <Promotion/>
          </el-icon>
          <span>老人管理</span>
        </el-menu-item>
        <el-menu-item index="/tag">
          <el-icon>
            <PriceTag/>
          </el-icon>
          <span>标签管理</span>
        </el-menu-item>
        <el-sub-menu>
          <template #title>
            <el-icon>
              <UserFilled/>
            </el-icon>
            <span>权限管理</span>
          </template>
          <el-menu-item index="/user">
            <el-icon>
              <User/>
            </el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/role">
            <el-icon>
              <Crop/>
            </el-icon>
            <span>角色管理</span>
          </el-menu-item>
          <el-menu-item index="/permission">
            <el-icon>
              <EditPen/>
            </el-icon>
            <span>权限管理</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>-->
    </el-aside>
    <!-- 右侧主区域 -->
    <el-container>
      <!-- 头部区域 -->
      <el-header>
        <div><strong>智慧社区养老系统</strong></div>
        <!-- 下拉菜单 -->
        <!-- command: 条目被点击后会触发,在事件函数上可以声明一个参数,接收条目对应的指令 -->
        <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="userInfoStore.user.avatar || undefined" :icon="UserFilled"/>
                        <el-icon>
                            <CaretBottom/>
                        </el-icon>
                    </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="updateUserInfo" :icon="User">基本资料</el-dropdown-item>
              <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
              <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <!-- 中间区域 -->
      <el-main>
        <section v-if="route.path === '/'" class="dashboard">
          <div class="dashboard-heading">
            <div>
              <p class="eyebrow">运营概览 · {{ new Date().toLocaleDateString('zh-CN', {month: 'long', day: 'numeric'}) }}</p>
              <h1>欢迎回来，{{ userInfoStore.user.name || '管理员' }}</h1>
              <p class="heading-note">数据大屏</p>
            </div>
            <el-button type="primary" plain @click="router.push('/elder')">查看老人档案 <el-icon><CaretBottom /></el-icon></el-button>
          </div>

          <div class="metric-grid">
            <div class="metric-card metric-green"><span class="metric-label">在住老人</span><strong>{{ dashboard.residentCount }}</strong><span class="metric-change">当前入住状态为“入住中”的老人</span><el-icon><UserFilled /></el-icon></div>
            <div class="metric-card metric-orange"><span class="metric-label">今日护理任务</span><strong>{{ dashboard.todayTasks }}</strong><span class="metric-change">已完成 <b>{{ completionRate() }}%</b></span><el-icon><EditPen /></el-icon></div>
            <div class="metric-card metric-blue"><span class="metric-label">可用床位</span><strong>{{ dashboard.availableBeds }}</strong><span class="metric-change">总床位 {{ dashboard.totalBeds }} 张</span><el-icon><Crop /></el-icon></div>
            <div class="metric-card metric-teal"><span class="metric-label">待体检预约</span><strong>{{ dashboard.examPending }}</strong><span class="metric-change">已完成体检 <b>{{ dashboard.examCompleted }}</b> 人次</span><el-icon><Plus /></el-icon></div>
          </div>
          <div v-if="false" class="metric-grid">
            <div class="metric-card metric-green"><span class="metric-label">在住老人</span><strong>428</strong><span class="metric-change">较上月 <b>+8.2%</b></span><el-icon><UserFilled /></el-icon></div>
            <div class="metric-card metric-orange"><span class="metric-label">今日护理任务</span><strong>96</strong><span class="metric-change">已完成 <b>68%</b></span><el-icon><EditPen /></el-icon></div>
            <div class="metric-card metric-blue"><span class="metric-label">可用床位</span><strong>72</strong><span class="metric-change">总床位 500 张</span><el-icon><Crop /></el-icon></div>
            <div class="metric-card metric-teal"><span class="metric-label">本月新入住</span><strong>24</strong><span class="metric-change">较上月 <b>+12.5%</b></span><el-icon><Plus /></el-icon></div>
          </div>

          <div class="dashboard-grid dashboard-grid-top">
            <div class="panel chart-panel occupancy-panel"><div class="panel-title"><div><h2>床位使用情况</h2><span>实时入住与空置分布</span></div><el-tag type="success" effect="plain">运行正常</el-tag></div><div ref="occupancyChart" class="chart chart-donut"></div></div>
            <div class="panel chart-panel"><div class="panel-title"><div><h2>护理完成率</h2><span>过去 7 天任务达成趋势</span></div><strong class="panel-value">{{ completionRate() }}<small>%</small></strong></div><div ref="trendChart" class="chart chart-trend"></div></div>
          </div>

          <div class="dashboard-grid dashboard-grid-bottom">
            <div class="panel chart-panel task-panel"><div class="panel-title"><div><h2>任务状态</h2><span>今日护理任务总览</span></div></div><div class="task-content"><div ref="taskChart" class="chart chart-task"></div><div class="task-legend"><div><i class="dot dot-done"></i><span>已完成</span><b>{{ dashboard.completedToday }}</b></div><div><i class="dot dot-progress"></i><span>待执行</span><b>{{ dashboard.taskPending }}</b></div><div><i class="dot dot-pending"></i><span>已取消</span><b>{{ dashboard.taskSkipped }}</b></div></div></div></div>
            <div class="panel activity-panel"><div class="panel-title"><div><h2>快捷入口</h2><span>快速访问常用管理功能</span></div></div><div class="quick-links"><button @click="router.push('/care-task')"><el-icon><EditPen /></el-icon><span>护理任务</span><small>查看今日安排</small></button><button @click="router.push('/bed')"><el-icon><Crop /></el-icon><span>床位管理</span><small>分配与调整床位</small></button><button @click="router.push('/elder')"><el-icon><User /></el-icon><span>老人档案</span><small>维护住户信息</small></button></div></div>
          </div>
        </section>
        <router-view v-else></router-view>
      </el-main>
      <!-- 底部区域 -->
    </el-container>
  </el-container>

  <el-dialog v-model="dialogFormVisible" title="修改个人信息" width="500" :lock-scroll="false">
    <el-form :model="user">
      <el-form-item label="名字" :label-width="60">
        <el-input v-model="user.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="邮箱" :label-width="60">
        <el-input v-model="user.email" autocomplete="off" />
      </el-form-item>
      <el-form-item label="手机号" :label-width="60">
        <el-input v-model="user.phone" autocomplete="off" />
      </el-form-item>
      <el-form-item label="头像" :label-width="60">
        <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :headers="{Authorization: tokenStore.token}">
          <img v-if="user.avatar" :src="user.avatar" class="avatar" alt="用户头像" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateUserInfo">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog  v-model="dialogResetPasswordDialog" title="重置密码" width="500" :lock-scroll="false">
    <el-form ref="resetForm" :rules="rules" :model="userPasswordDTO">
      <el-form-item prop="oldPassword" label="原密码" :label-width="100">
        <el-input v-model="userPasswordDTO.oldPassword" autocomplete="off"/>
      </el-form-item>
      <el-form-item prop="newPassword" label="新密码" :label-width="100">
        <el-input v-model="userPasswordDTO.newPassword" autocomplete="off"/>
      </el-form-item>
      <el-form-item prop="reNewPassword" label="重复新密码" :label-width="100">
        <el-input v-model="userPasswordDTO.reNewPassword" autocomplete="off"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogResetPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="resetPassword(resetForm)">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
  .layout-container {
    min-height: 100vh;
    background: var(--app-canvas);

    .el-aside {
      background: #ffffff;
      border-right: 1px solid var(--app-line);
      overflow: hidden;

      &__logo {
        height: 88px;
        margin: 0 18px;
        border-bottom: 1px solid var(--app-line);
        background: url('@/assets/logo.svg') no-repeat left center / 44px auto;
      }

      .el-menu {
        border-right: none;
        padding: 12px 10px;
        --el-menu-bg-color: #ffffff !important;
        --el-menu-hover-bg-color: #f0f7f4 !important;
        --el-menu-text-color: #4d625c !important;
        --el-menu-active-color: #126b62 !important;
        background: #ffffff !important;

        :deep(.el-menu-item),
        :deep(.el-sub-menu__title) {
          height: 44px;
          margin: 3px 0;
          border-radius: 5px;
          color: #4d625c !important;
          line-height: 44px;
        }

        :deep(.el-menu-item:hover),
        :deep(.el-sub-menu__title:hover) {
          background: #f0f7f4 !important;
        }

        :deep(.el-sub-menu .el-menu) {
          background: #f7faf8 !important;
        }

        :deep(.el-icon) {
          color: currentColor;
        }
      }
    }

    .el-header {
      height: 64px;
      padding: 0 28px;
      background: #fff;
      display: flex;
      align-items: center;
      justify-content: space-between;
      border-bottom: 1px solid var(--app-line);

      > div strong {
        color: var(--app-ink);
        font-size: 16px;
        font-weight: 650;
      }

      .el-dropdown__box {
        min-height: 40px;
        padding: 0 4px 0 10px;
        border: 1px solid transparent;
        border-radius: 6px;
        display: flex;
        align-items: center;
        cursor: pointer;

        &:hover {
          background: #f2f7f4;
          border-color: var(--app-line);
        }

        .el-icon {
          color: var(--app-muted);
          margin-left: 10px;
        }

        &:active,
        &:focus {
          outline: none;
        }
      }
    }

    .el-footer {
      height: 42px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #fff;
      border-top: 1px solid var(--app-line);
      color: var(--app-muted);
      font-size: 12px;
    }

    :deep(.el-main) {
      padding: 20px 24px 26px;
      background: var(--app-canvas);
    }
  }

  .avatar-uploader .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }

  .avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }

  .avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
  }

  .el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
  }

  @media (max-width: 768px) {
    .layout-container {
      .el-aside {
        width: 68px !important;

        &__logo {
          height: 64px;
          margin: 0 12px;
          background-position: center;
          background-size: 34px auto;
        }

        .el-menu {
          padding: 8px;

          :deep(.el-menu-item),
          :deep(.el-sub-menu__title) {
            padding: 0 14px !important;
          }

          :deep(.el-menu-item span),
          :deep(.el-sub-menu__title span),
          :deep(.el-sub-menu__icon-arrow) {
            display: none;
          }
        }
      }

      .el-header {
        height: 56px;
        padding: 0 14px;

        > div strong {
          font-size: 14px;
        }
      }

      :deep(.el-main) {
        padding: 12px 0 18px;
      }
    }
  }

  .dashboard { max-width: 1480px; margin: 0 auto; }
  .dashboard-heading { display:flex; align-items:flex-end; justify-content:space-between; gap:20px; margin-bottom:24px; }
  .eyebrow { margin:0 0 7px; color:var(--app-brand); font-size:12px; font-weight:700; letter-spacing:.08em; text-transform:uppercase; }
  .dashboard-heading h1 { margin:0; color:var(--app-ink); font-size:26px; font-weight:700; }
  .heading-note { margin:6px 0 0; color:var(--app-muted); }
  .dashboard-heading .el-button { height:40px; }
  .metric-grid { display:grid; grid-template-columns:repeat(4, 1fr); gap:16px; margin-bottom:18px; }
  .metric-card { position:relative; min-height:132px; padding:20px; border:1px solid var(--app-line); border-radius:7px; background:#fff; overflow:hidden; }
  .metric-card .metric-label,.metric-card .metric-change { display:block; color:var(--app-muted); font-size:13px; }
  .metric-card strong { display:block; margin:9px 0 5px; color:var(--app-ink); font-size:30px; line-height:1; }
  .metric-change b { color:var(--app-brand); font-weight:700; }
  .metric-card .el-icon { position:absolute; right:20px; top:20px; padding:10px; border-radius:7px; font-size:20px; }
  .metric-green .el-icon { color:#16736a; background:#e5f3ef; } .metric-orange .el-icon { color:#c76b43; background:#fbede7; }
  .metric-blue .el-icon { color:#4d7ba8; background:#eaf2fa; } .metric-teal .el-icon { color:#4c8c85; background:#e6f3f1; }
  .dashboard-grid { display:grid; gap:18px; margin-bottom:18px; }
  .dashboard-grid-top { grid-template-columns: minmax(0, .9fr) minmax(0, 1.6fr); }
  .dashboard-grid-bottom { grid-template-columns: minmax(0, .9fr) minmax(0, 1.6fr); }
  .panel { min-width:0; border:1px solid var(--app-line); border-radius:7px; background:#fff; }
  .panel-title { display:flex; justify-content:space-between; align-items:flex-start; padding:18px 20px 0; }
  .panel-title h2 { margin:0; color:var(--app-ink); font-size:16px; font-weight:700; }
  .panel-title span { display:block; margin-top:5px; color:var(--app-muted); font-size:12px; }
  .panel-value { color:var(--app-brand); font-size:25px; line-height:1; } .panel-value small { font-size:14px; }
  .chart { width:100%; } .chart-donut { height:235px; } .chart-trend { height:235px; } .chart-task { width:190px; height:190px; }
  .task-content { display:flex; align-items:center; justify-content:center; gap:14px; min-height:225px; padding:0 16px 12px; }
  .task-legend { min-width:105px; } .task-legend div { display:grid; grid-template-columns:10px 1fr auto; align-items:center; gap:8px; margin:12px 0; color:var(--app-muted); font-size:12px; } .task-legend b { color:var(--app-ink); font-size:14px; }
  .dot { width:8px; height:8px; border-radius:50%; } .dot-done { background:#16736a; } .dot-progress { background:#d49a47; } .dot-pending { background:#dce5e0; }
  .quick-links { display:grid; grid-template-columns:repeat(3,1fr); gap:10px; padding:22px 20px 24px; }
  .quick-links button { display:flex; flex-direction:column; align-items:flex-start; min-height:118px; padding:16px; border:1px solid var(--app-line); border-radius:6px; background:#fbfcfb; color:var(--app-ink); text-align:left; cursor:pointer; transition:.2s; }
  .quick-links button:hover { border-color:#9cc8c1; background:#f3faf7; transform:translateY(-2px); } .quick-links .el-icon { margin-bottom:12px; color:var(--app-brand); font-size:20px; } .quick-links span { font-weight:700; } .quick-links small { margin-top:5px; color:var(--app-muted); font-size:11px; }
  @media (max-width: 900px) { .metric-grid { grid-template-columns:repeat(2,1fr); } .dashboard-grid-top,.dashboard-grid-bottom { grid-template-columns:1fr; } }
  @media (max-width: 560px) { .dashboard-heading { align-items:flex-start; flex-direction:column; } .dashboard-heading h1 { font-size:22px; } .metric-grid { gap:10px; } .metric-card { padding:15px; min-height:118px; } .metric-card strong { font-size:25px; } .metric-card .el-icon { right:13px; top:13px; } .quick-links { grid-template-columns:1fr; } .task-content { justify-content:space-around; } }


</style>
