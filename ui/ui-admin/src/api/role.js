import request from "@/utils/request.js";

const roleApi = {
    list(roleQuery) {
        return request.get("/role", {params: roleQuery});
    },
    deleteById(id) {
        return request.delete(`/role/${id}`);
    },
    add(role) {
        return request.post("/role", role)
    },
    selectById(id) {
        return request.get(`/role/${id}`)
    },
    update(id, role) {
        return request.put(`/role/${id}`, role)
    },
    deleteAll(ids) {
        // axios 的 delete 第2个参数是 config，请求体必须放在 data 字段里
        return request.delete("/role", {data: ids})
    },
    selectAssignedPermission(roleId) {
        return request.get(`/role/selectAssignedPermission/${roleId}`)
    },
    assignPermission(roleId, permissionIds) {
        return request.post('/role/assignPermission', null, {params: {roleId, permissionIds}})
    }
}

export default roleApi
