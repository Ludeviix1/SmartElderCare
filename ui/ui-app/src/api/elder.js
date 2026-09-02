import request from "@/utils/request.js";

const elderApi = {
    login(elder) {
        return request.post("/elders/login", elder)
    },
    elderInfo() {
        return request.get("/elders/elderInfo")
    }
}

export default elderApi
