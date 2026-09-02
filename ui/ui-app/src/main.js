import { createApp } from 'vue'
import App from '@/App.vue'

import router from '@/router'
import {createPinia} from 'pinia'
const pinia = createPinia()
//引入持久化插件
import piniaPluginPersistedstate from "pinia-plugin-persistedstate"
//使用持久化插件
pinia.use(piniaPluginPersistedstate)

//Vant组件库：函数式组件需要显式引入样式（showToast等）
import Vant from 'vant'
import 'vant/lib/index.css'

const app = createApp(App)
app.use(router).use(pinia).use(Vant)
app.mount('#app')
