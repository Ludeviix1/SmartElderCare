import request from '@/utils/request.js'


const permissionApi = {
    selectPermissionTree() {
        return request.get('/permissions/selectPermissionTree')
    },
    selectByParentId(id) {
        return request.get(`/permissions/selectByParentId/${id}`)
    },
    deleteById(id) {
        return request.delete(`/permissions/${id}`)
    },
    add(permission) {
        return request.post('/permissions', permission)
    },
    selectById(id) {
        return request.get(`/permissions/${id}`)
    },
    update(permission) {
        return request.put('/permissions', permission)
    },
}

export default permissionApi
