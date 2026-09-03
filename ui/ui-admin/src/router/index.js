// 创建一个路由器，并暴露出去
// 第一步：引入createRouter
import {createRouter, createWebHistory} from 'vue-router'
// 引入一个一个可能要呈现组件
import Index from '@/views/Index.vue'
import Login from '@/views/Login.vue'
import User from '@/views/User.vue'
import Elder from '@/views/Elder.vue'
import Tag from '@/views/Tag.vue'
import Role from '@/views/Role.vue'
import Permission from '@/views/Permission.vue'
import CareItem from '@/views/CareItem.vue'
import CareLevel from '@/views/CareLevel.vue'
import CarePlan from '@/views/CarePlan.vue'
import CareTask from '@/views/CareTask.vue'
import ExamItem from '@/views/ExamItem.vue'
import ExamPackage from '@/views/ExamPackage.vue'
import FamilyMember from '@/views/FamilyMember.vue'
import Bed from '@/views/Bed.vue'
import ExamAppointment from '@/views/ExamAppointment.vue'

//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: [
        {path: '/login', component: Login},
        {
            path: '/', component: Index, children: [
                {path: '/user', component: User},
                {path: '/elder', component: Elder},
                {path: '/tag', component: Tag},
                {path: '/care-item', component: CareItem},
                {path: '/care-level', component: CareLevel},
                {path: '/care-plan', component: CarePlan},
                {path: '/care-task', component: CareTask},
                {path: '/exam-item', component: ExamItem},
                {path: '/exam-package', component: ExamPackage},
                {path: '/family-member', component: FamilyMember},
                {path: '/bed', component: Bed},
                {path: '/exam-appointment', component: ExamAppointment},
                {path: '/role', component: Role},
                {path: '/permission', component: Permission}
            ]
        }
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
