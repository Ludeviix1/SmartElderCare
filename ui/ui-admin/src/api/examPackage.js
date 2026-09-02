import request from "@/utils/request.js";

const examPackageApi = {
    list(examPackageQuery) {
        return request.get("/exam-package", {params: examPackageQuery});
    },
    deleteById(id) {
        return request.delete(`/exam-package/${id}`);
    },
    add(examPackage) {
        return request.post("/exam-package", examPackage)
    },
    selectById(id) {
        return request.get(`/exam-package/${id}`)
    },
    update(id, examPackage) {
        return request.put(`/exam-package/${id}`, examPackage)
    },
    deleteAll(ids) {
        // axios 的 delete 第2个参数是 config，请求体必须放在 data 字段里
        return request.delete("/exam-package", {data: ids})
    },
    listExamItemIds(packageId) {
        return request.get(`/exam-package-item/${packageId}`)
    },
    assignExamItems(packageId, examItemIds) {
        return request.put(`/exam-package-item/${packageId}`, examItemIds)
    }
}

export default examPackageApi
