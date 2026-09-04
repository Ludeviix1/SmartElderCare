import request from '@/utils/request.js'

export default {
  overview() {
    return request.get('/dashboard/overview')
  }
}
