import request from '@/utils/request.js'
export default {
  list(params) { return request.get('/exam-appointment', {params}) },
  add(data) { return request.post('/exam-appointment', data) },
  assign(id, caregiverId) { return request.put(`/exam-appointment/${id}/assign`, null, {params: {caregiverId}}) },
  autoAssign(id) { return request.post(`/exam-appointment/${id}/auto-assign`) },
  executionDetail(id) { return request.get(`/exam-appointment/${id}/execution`) },
  execute(id, data) { return request.put(`/exam-appointment/${id}/execution`, data) },
  deleteById(id) { return request.delete(`/exam-appointment/${id}`) }
}
