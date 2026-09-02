import request from "@/utils/request.js";

//护理任务由护理计划生成，没有add方法
const careTaskApi = {
    list(careTaskQuery) {
        return request.get("/care-task", {params: careTaskQuery});
    },
    deleteById(id) {
        return request.delete(`/care-task/${id}`);
    },
    selectById(id) {
        return request.get(`/care-task/${id}`)
    },
    update(id, careTask) {
        return request.put(`/care-task/${id}`, careTask)
    },
    deleteAll(ids) {
        // axios 的 delete 第2个参数是 config，请求体必须放在 data 字段里
        return request.delete("/care-task", {data: ids})
    }
}

export default careTaskApi
