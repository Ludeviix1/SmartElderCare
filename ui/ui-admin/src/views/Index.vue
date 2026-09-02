<script setup>
  import {
    Management,
    Promotion,
    PriceTag,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
  } from '@element-plus/icons-vue'
  //条目被点击后,调用的函数
  import {useRouter, useRoute} from 'vue-router'
  const router = useRouter();
  const route = useRoute();
  import * as ElementPlusIconsVue from '@element-plus/icons-vue'
  import userApi from "@/api/user.js";
  import {useTokenStore} from '@/store/token.js'
  const tokenStore = useTokenStore();
  import {useUserInfoStore} from '@/store/userInfo.js'
  import {ref} from "vue";
  import {ElMessage} from "element-plus";
  const userInfoStore = useUserInfoStore();

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
      if (result.code == 1) {
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
      if (result.code == 1) {
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
    await formEl.validate((valid, fields) => {
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

    return [...otherMenus, ...permissionMenu].map(({_menuIndex, ...menu}) => menu)
  }


</script>

<template>
  <!-- element-plus中的容器 -->
  <el-container class="layout-container">
    <!-- 左侧菜单 -->
    <el-aside width="200px">
      <div class="el-aside__logo"></div>
      <!-- element-plus的菜单标签 -->
      <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff" router :default-active="route.path">
        <!-- 动态生成菜单 -->
        <template v-for="(menu, index) in menuData" :index="index.toString()">
          <el-sub-menu v-if="menu.children?.length>0" :index="menu.path || ('sub-' + index)">
            <template #title>
              <component
                  class="icons"
                  :is="getIcon(menu.icon)"
                  style="width: 1em; height: 1em; margin-right: 8px" >
              </component>
              <span>{{ menu.name }}</span>
            </template>
            <el-menu-item v-for="(child, ind) in menu.children" :index="child.path">
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
        <div><strong>思途后台管理系统{{ zhansgan }}</strong></div>
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
              <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
              <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
              <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <!-- 中间区域 -->
      <el-main>
        <!-- <div style="width: 1290px; height: 570px;border: 1px solid red;">
                    内容展示区
                </div> -->
        <router-view></router-view>
      </el-main>
      <!-- 底部区域 -->
      <el-footer>后台管理 ©2024 Created by Gao</el-footer>
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
          <img v-if="user.avatar" :src="user.avatar" class="avatar" />
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
      background: #143f3a;
      border-right: 1px solid rgba(255, 255, 255, 0.12);
      overflow: hidden;

      &__logo {
        height: 88px;
        margin: 0 18px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.14);
        background: url('@/assets/logo.svg') no-repeat left center / 44px auto;
      }

      .el-menu {
        border-right: none;
        padding: 12px 10px;
        background: transparent !important;

        :deep(.el-menu-item),
        :deep(.el-sub-menu__title) {
          height: 44px;
          margin: 3px 0;
          border-radius: 5px;
          color: #dce9e5 !important;
          line-height: 44px;
        }

        :deep(.el-menu-item:hover),
        :deep(.el-sub-menu__title:hover) {
          background: rgba(255, 255, 255, 0.09) !important;
        }

        :deep(.el-menu-item.is-active) {
          background: #e2f0ea !important;
          color: #115b52 !important;
          font-weight: 600;
        }

        :deep(.el-sub-menu .el-menu) {
          background: rgba(0, 0, 0, 0.1) !important;
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


</style>
