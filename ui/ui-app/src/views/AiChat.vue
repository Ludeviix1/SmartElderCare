<script setup>
import {computed, nextTick, onBeforeUnmount, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {showConfirmDialog, showToast} from 'vant'
import {useElderInfoStore} from '@/store/elderInfo.js'
import chatApi from '@/api/chat.js'

const router = useRouter()
const elderInfoStore = useElderInfoStore()
const messages = ref([])
const input = ref('')
const sending = ref(false)
const listRef = ref(null)
const abortController = ref(null)
let id = 0
const suggestions = [
  '我最近有哪些体检预约？',
  '我的护理计划和待执行任务是什么？',
  '最近完成了哪些护理？',
  '体检前需要空腹吗？',
  '高血压平时要注意什么？'
]
const canSend = computed(() => input.value.trim() && !sending.value)
const elderInitial = computed(() => (elderInfoStore.elder.name || '我').slice(0, 1))

function scrollToBottom() { nextTick(() => { if (listRef.value) listRef.value.scrollTop = listRef.value.scrollHeight }) }

function runStream(question, answer) {
  sending.value = true
  const controller = new AbortController()
  abortController.value = controller
  chatApi.chatStream(question, {signal: controller.signal, onDelta(delta) { answer.content += delta; scrollToBottom() }})
    .then(() => { answer.status = 'done' })
    .catch(error => { if (error.name !== 'AbortError') { answer.status = 'error'; if (!answer.content) answer.content = '网络连接异常，请稍后重试。' } })
    .finally(() => { sending.value = false; abortController.value = null; scrollToBottom() })
}

function send(text) {
  const question = (text || input.value).trim()
  if (!question || sending.value) return
  input.value = ''
  messages.value.push({id: ++id, role: 'user', content: question, status: 'done'})
  const answer = reactive({id: ++id, role: 'ai', content: '', status: 'streaming'})
  messages.value.push(answer)
  scrollToBottom()
  runStream(question, answer)
}

function stop() { abortController.value?.abort() }
function retry() {
  const answer = messages.value[messages.value.length - 1]
  const question = [...messages.value].reverse().find(item => item.role === 'user')
  if (!answer || !question || answer.status !== 'error' || sending.value) return
  answer.content = ''; answer.status = 'streaming'; runStream(question.content, answer)
}
function clearHistory() {
  showConfirmDialog({title: '清空对话', message: '将清空本次显示内容和服务器上的问答上下文。'}).then(async () => {
    await chatApi.clearHistory(); messages.value = []; showToast('对话已清空')
  }).catch(() => {})
}
onBeforeUnmount(stop)
</script>

<template>
  <div class="chat-page">
    <van-nav-bar title="智能问答助手" left-arrow fixed placeholder @click-left="router.back()"><template #right><van-icon name="delete-o" size="20" @click="clearHistory" /></template></van-nav-bar>
    <main ref="listRef" class="chat-body">
      <section v-if="!messages.length" class="welcome">
        <div class="message-row ai"><div class="avatar ai-avatar">智</div><div><div class="bubble">您好，我是康养智能助手。体检、饮食、用药和日常运动方面的问题都可以问我。</div><p class="notice">AI 内容仅供参考，不能替代医生诊断；紧急不适请及时就医。</p></div></div>
        <div class="suggestions"><van-button v-for="item in suggestions" :key="item" plain type="primary" size="small" @click="send(item)">{{ item }}</van-button></div>
      </section>
      <div v-for="(message, index) in messages" :key="message.id" class="message-row" :class="message.role">
        <div v-if="message.role === 'ai'" class="avatar ai-avatar">智</div>
        <div class="message-main"><div class="bubble"><span v-if="message.status === 'streaming' && !message.content" class="typing"><i></i><i></i><i></i></span><template v-else>{{ message.content }}<span v-if="message.status === 'streaming'" class="cursor"></span></template></div><van-button v-if="message.status === 'error' && index === messages.length - 1" size="small" plain type="primary" class="retry" @click="retry">重新提问</van-button></div>
        <div v-if="message.role === 'user'" class="avatar user-avatar">{{ elderInitial }}</div>
      </div>
    </main>
    <footer class="input-bar"><van-field v-model="input" type="textarea" rows="1" autosize maxlength="500" placeholder="输入您想咨询的问题" @keypress.enter.exact.prevent="send()" /><van-button v-if="sending" plain type="danger" @click="stop">停止</van-button><van-button v-else type="primary" :disabled="!canSend" @click="send()">发送</van-button></footer>
  </div>
</template>

<style scoped lang="scss">
.chat-page{display:flex;flex-direction:column;height:100dvh;background:#f5f6f8}.chat-body{flex:1;overflow-y:auto;padding:16px 12px}.message-row{display:flex;align-items:flex-start;gap:8px;margin-bottom:16px}.message-row.user{flex-direction:row-reverse}.avatar{flex:0 0 36px;width:36px;height:36px;display:flex;align-items:center;justify-content:center;border-radius:50%;font-weight:600}.ai-avatar{color:#fff;background:#1989fa}.user-avatar{color:#07c160;background:#e8f8ef}.message-main{max-width:calc(100% - 52px)}.bubble{padding:10px 14px;border-radius:8px;background:#fff;color:#323233;font-size:16px;line-height:1.6;white-space:pre-wrap;word-break:break-word}.user .bubble{color:#fff;background:#1989fa}.notice{margin:6px 0 0;color:#969799;font-size:12px;line-height:1.5}.suggestions{display:flex;flex-wrap:wrap;gap:8px;margin:14px 0 0 44px}.input-bar{display:flex;align-items:flex-end;gap:8px;padding:8px 12px calc(8px + env(safe-area-inset-bottom));background:#fff;border-top:1px solid #ebedf0}.input-bar :deep(.van-cell){padding:8px;background:#f5f6f8;border-radius:6px}.input-bar .van-button{flex-shrink:0;height:40px}.retry{margin-top:8px}.typing{display:inline-flex;gap:4px;padding:5px 0}.typing i{width:7px;height:7px;border-radius:50%;background:#969799;animation:blink 1.2s infinite}.typing i:nth-child(2){animation-delay:.2s}.typing i:nth-child(3){animation-delay:.4s}.cursor{display:inline-block;width:2px;height:1em;margin-left:2px;vertical-align:-2px;background:#1989fa;animation:blink .8s infinite}@keyframes blink{0%,60%,100%{opacity:.3}30%{opacity:1}}
</style>
