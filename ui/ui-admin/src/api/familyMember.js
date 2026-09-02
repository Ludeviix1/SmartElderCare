import request from '@/utils/request.js'

const familyMemberApi = {
  list(params) { return request.get('/family-members', {params}) },
  getById(id) { return request.get(`/family-members/${id}`) },
  add(data) { return request.post('/family-members', data) },
  update(id, data) { return request.put(`/family-members/${id}`, data) },
  deleteById(id) { return request.delete(`/family-members/${id}`) },
  deleteAll(ids) { return request.delete('/family-members', {data: ids}) }
}

export default familyMemberApi
