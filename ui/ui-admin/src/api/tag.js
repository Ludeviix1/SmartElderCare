import request from "@/utils/request.js";

const tagApi = {
    list(tagQuery) {
        return request.get("/tag", {params: tagQuery});
    },
    deleteById(id) {
        return request.delete(`/tag/${id}`);
    },
    add(tag) {
        return request.post("/tag", tag)
    },
    selectById(id) {
        return request.get(`/tag/${id}`)
    },
    update(id, tag) {
        return request.put(`/tag/${id}`, tag)
    },
    deleteAll(ids) {
        // axios 的 delete 第2个参数是 config，请求体必须放在 data 字段里
        return request.delete("/tag", {data: ids})
    }
}

export default tagApi
