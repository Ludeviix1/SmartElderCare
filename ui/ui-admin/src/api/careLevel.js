import request from "@/utils/request.js";

const careLevelApi = {
    list(careLevelQuery) {
        return request.get("/care-level", {params: careLevelQuery});
    },
    deleteById(id) {
        return request.delete(`/care-level/${id}`);
    },
    add(careLevel) {
        return request.post("/care-level", careLevel)
    },
    selectById(id) {
        return request.get(`/care-level/${id}`)
    },
    update(id, careLevel) {
        return request.put(`/care-level/${id}`, careLevel)
    },
    deleteAll(ids) {
        // axios 的 delete 第2个参数是 config，请求体必须放在 data 字段里
        return request.delete("/care-level", {data: ids})
    }
}

export default careLevelApi
