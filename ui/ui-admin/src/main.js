import { createApp } from 'vue'
import App from '@/App.vue'

import router from '@/router'
import {createPinia} from 'pinia'
const pinia = createPinia()
//引入持久化插件
import piniaPluginPersistedstate from "pinia-plugin-persistedstate"
//使用持久化插件
pinia.use(piniaPluginPersistedstate)
import ElementPlus from 'element-plus' //导入element-plus
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/dist/index.css' //导入element-plus样式

const app = createApp(App)
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 全局挂载和注册 element-plus 的所有 icon
app.config.globalProperties.$icons = []
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.config.globalProperties.$icons.push(key)
    app.component(key, component)
}

app.use(router).use(pinia).use(ElementPlus, {locale: zhCn }).mount('#app')
