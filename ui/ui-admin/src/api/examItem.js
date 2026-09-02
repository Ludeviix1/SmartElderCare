import request from "@/utils/request.js";

const examItemApi = {
    list(examItemQuery) {
        return request.get("/exam-item", {params: examItemQuery});
    },
    deleteById(id) {
        return request.delete(`/exam-item/${id}`);
    },
    add(examItem) {
        return request.post("/exam-item", examItem)
    },
    selectById(id) {
        return request.get(`/exam-item/${id}`)
    },
    update(id, examItem) {
        return request.put(`/exam-item/${id}`, examItem)
    },
    deleteAll(ids) {
        // axios 的 delete 第2个参数是 config，请求体必须放在 data 字段里
        return request.delete("/exam-item", {data: ids})
    }
}

export default examItemApi
