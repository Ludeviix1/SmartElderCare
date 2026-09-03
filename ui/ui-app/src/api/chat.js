import request from '@/utils/request.js'

const chatApi = {
  ask(message) {
    return request.post('/chat', {message})
  },
  clearHistory() {
    return request.delete('/chat/history')
  }
}

export default chatApi
