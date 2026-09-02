import request from "@/utils/request.js";

const appointmentApi = {
    //提交体检预约
    add(appointment) {
        return request.post("/appointment", appointment)
    },
    //我的预约列表
    list() {
        return request.get("/appointment")
    },
    //取消预约
    cancel(id) {
        return request.put(`/appointment/${id}/cancel`)
    }
}

export default appointmentApi
