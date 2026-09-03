// 创建一个路由器，并暴露出去
// 第一步：引入createRouter
import {createRouter, createWebHistory} from 'vue-router'
// 引入一个一个可能要呈现组件
import Login from '@/views/Login.vue'
import Layout from '@/views/Layout.vue'
import Home from '@/views/Home.vue'
import ExamPackage from '@/views/ExamPackage.vue'
import PackageDetail from '@/views/PackageDetail.vue'
import AppointmentForm from '@/views/AppointmentForm.vue'
import MyAppointment from '@/views/MyAppointment.vue'
import Profile from '@/views/Profile.vue'
import ExamReport from '@/views/ExamReport.vue'
import AiChat from '@/views/AiChat.vue'

//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: [
        {path: '/login', component: Login},
        {
            path: '/', component: Layout, children: [
                {path: '', redirect: '/home'},
                {path: '/home', component: Home},
                {path: '/package', component: ExamPackage},
                {path: '/appointment', component: MyAppointment},
                {path: '/profile', component: Profile}
            ]
        },
        //独立页面（不在底部Tabbar内）
        {path: '/package/:id', component: PackageDetail},
        {path: '/appointment-form/:packageId', component: AppointmentForm},
        {path: '/appointment/:id/report', component: ExamReport},
        {path: '/ai-chat', component: AiChat}
    ]
})

//路由守卫
//全局前置守卫
import {useTokenStore} from '@/store/token.js'
let whiteList = ['/login']; // 白名单
router.beforeEach((to) => {
    const tokenStore = useTokenStore()
    const token = tokenStore.token;
    if (!whiteList.includes(to.path) && !token) {
        return '/login'
    }
})

// 暴露出去router
export default router
