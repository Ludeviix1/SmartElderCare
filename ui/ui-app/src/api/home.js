//首页假数据api
import {mockHealthData, mockNotices, mockUser} from '@/mock/data.js'

const delay = (ms = 300) => new Promise(resolve => setTimeout(resolve, ms))
const ok = (data, msg = '成功') => ({code: 1, msg, data})

const homeApi = {
    //健康数据
    healthData() {
        return delay().then(() => ok(mockHealthData))
    },
    //公告列表
    notices() {
        return delay().then(() => ok(mockNotices))
    },
    //问候语
    greeting() {
        const hour = new Date().getHours()
        let greeting = '晚上好'
        if (hour < 6) {
            greeting = '凌晨好'
        } else if (hour < 9) {
            greeting = '早上好'
        } else if (hour < 12) {
            greeting = '上午好'
        } else if (hour < 14) {
            greeting = '中午好'
        } else if (hour < 18) {
            greeting = '下午好'
        }
        return delay().then(() => ok(greeting + '，' + mockUser.name))
    }
}

export default homeApi
