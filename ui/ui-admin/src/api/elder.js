import request from "@/utils/request.js";

const elderApi = {
    list(elderQuery) {
        return request.get("/elders", {params: elderQuery});
    },
    deleteById(id) {
        return request.delete(`/elders/${id}`);
    },
    add(elder) {
        return request.post("/elders", elder)
    },
    selectById(id) {
        return request.get(`/elders/${id}`)
    },
    update(id, elder) {
        return request.put(`/elders/${id}`, elder)
    },
    deleteAll(ids) {
        // axios 的 delete 第2个参数是 config，请求体必须放在 data 字段里
        return request.delete("/elders", {data: ids})
    },
    //返回所有的标签，和这个老人已经分配的标签的id
    selectAssignedTag(elderId) {
        return request.get(`/elders/selectAssignedTag/${elderId}`)
    },
    assignTag(elderId, tagIds) {
        return request.post(`/elders/assignTag`, null, {params: {elderId, tagIds}});
    }
}

export default elderApi
