import request from "@/utils/request.js";

const careItemApi = {
    list(careItemQuery) {
        return request.get("/care-item", {params: careItemQuery});
    },
    deleteById(id) {
        return request.delete(`/care-item/${id}`);
    },
    add(careItem) {
        return request.post("/care-item", careItem)
    },
    selectById(id) {
        return request.get(`/care-item/${id}`)
    },
    update(id, careItem) {
        return request.put(`/care-item/${id}`, careItem)
    },
    deleteAll(ids) {
        // axios 的 delete 第2个参数是 config，请求体必须放在 data 字段里
        return request.delete("/care-item", {data: ids})
    }
}

export default careItemApi
