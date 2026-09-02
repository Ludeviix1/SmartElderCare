import {defineStore} from 'pinia'

export const useElderInfoStore = defineStore('elderInfo', {
    state() {
        return {
            elder: {}
        }
    },
    actions: {
        setElderInfo(elder) {
            this.elder = elder
        },
        removeElderInfo() {
            this.elder = {}
        }
    },
    persist: {
        enabled: true
    }
})
