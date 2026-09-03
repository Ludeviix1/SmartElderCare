import request from '@/utils/request.js'
import {useTokenStore} from '@/store/token.js'

const chatApi = {
  ask(message) {
    return request.post('/chat', {message})
  },
  async chatStream(message, {signal, onDelta}) {
    const token = useTokenStore().token
    const response = await fetch(`/api/app/chat/chatStream?message=${encodeURIComponent(message)}`, {
      method: 'POST', headers: {Authorization: token, Accept: 'text/event-stream'}, signal
    })
    if (!response.ok || !response.body) throw new Error(`Chat stream failed: ${response.status}`)
    const reader = response.body.getReader()
    const decoder = new TextDecoder('utf-8')
    let buffer = ''
    let finished = false
    while (!finished) {
      const {done, value} = await reader.read()
      buffer += decoder.decode(value || new Uint8Array(), {stream: !done})
      const events = buffer.split(/\r?\n\r?\n/)
      buffer = events.pop() || ''
      for (const event of events) {
        const text = event.split(/\r?\n/).filter(line => line.startsWith('data:')).map(line => line.slice(5).trimStart()).join('\n')
        if (!text) continue
        if (text === '[END]') { finished = true; break }
        onDelta(text)
      }
      if (done) break
    }
  },
  clearHistory() {
    return request.delete('/chat/history')
  }
}

export default chatApi
