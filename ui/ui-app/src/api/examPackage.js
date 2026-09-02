import request from "@/utils/request.js";

const examPackageApi = {
    //体检套餐列表（上架的）
    list() {
        return request.get("/exam-package")
    },
    //套餐详情（含包含的体检项目）
    selectById(id) {
        return request.get(`/exam-package/${id}`)
    }
}

export default examPackageApi
