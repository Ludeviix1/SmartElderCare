import request from "@/utils/request.js";

const carePlanApi = {
    list(carePlanQuery) {
        return request.get("/care-plan", {params: carePlanQuery});
    },
    deleteById(id) {
        return request.delete(`/care-plan/${id}`);
    },
    add(carePlan) {
        return request.post("/care-plan", carePlan)
    },
    selectById(id) {
        return request.get(`/care-plan/${id}`)
    },
    update(id, carePlan) {
        return request.put(`/care-plan/${id}`, carePlan)
    },
    deleteAll(ids) {
        // axios 的 delete 第2个参数是 config，请求体必须放在 data 字段里
        return request.delete("/care-plan", {data: ids})
    }
}

export default carePlanApi
