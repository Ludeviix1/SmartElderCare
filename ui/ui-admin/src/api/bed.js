import request from '@/utils/request.js'

const bedApi = {
  list(params) { return request.get('/beds', {params}) },
  getById(id) { return request.get(`/beds/${id}`) },
  add(data) { return request.post('/beds', data) },
  update(id, data) { return request.put(`/beds/${id}`, data) },
  assign(id, elderId) { return request.post(`/beds/${id}/assign`, null, {params: {elderId}}) },
  release(id) { return request.post(`/beds/${id}/release`) },
  deleteById(id) { return request.delete(`/beds/${id}`) },
  deleteAll(ids) { return request.delete('/beds', {data: ids}) }
}

export default bedApi
