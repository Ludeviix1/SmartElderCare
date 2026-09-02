import {defineStore} from 'pinia'

export const useTokenStore = defineStore('token', {
    //存储数据地方
    state() {
        return {
            token: ''
        }
    },
    //actions里面放的是一个一个方法
    actions: {
        setToken(newToken) {
            console.log(newToken)
            this.token = newToken
        },
        removeToken(value) {
            this.token = ''
        }
    },
    persist: {
        enabled: true, // 开启缓存  默认会存储在本地localstorage
    }
})