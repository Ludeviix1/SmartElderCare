import {defineStore} from 'pinia'

//预约状态：与后端一致 0待体检 1体检中 2已完成 3已取消 4已过期
export const useAppointmentStore = defineStore('appointment', {
    state() {
        return {
            //预约列表（从后端拉取）
            appointmentList: [],
            //脏标记：提交/取消后置true，列表页据此重新拉取
            dirty: true
        }
    },
    getters: {
        //待体检的预约
        pendingList(state) {
            return state.appointmentList.filter(item => item.status === 0)
        }
    },
    actions: {
        //设置预约列表
        setAppointmentList(appointmentList) {
            this.appointmentList = appointmentList
            this.dirty = false
        },
        //标记列表已变化，需要重新拉取
        markDirty() {
            this.dirty = true
        },
        //清空列表（切换到后端数据源时清掉本地残留的假数据）
        clearAppointments() {
            this.appointmentList = []
            this.dirty = true
        },
        //清空用户相关数据（退出登录时调用）
        reset() {
            this.appointmentList = []
            this.dirty = true
        }
    },
    persist: {
        enabled: true
    }
})
