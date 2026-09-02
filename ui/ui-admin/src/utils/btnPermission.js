import {useUserInfoStore} from '@/store/userInfo.js'
//判断当前用户是否具有这个按钮权限 user:add
export default function hasBtnPermission(permission) {
    const userInfoStore = useUserInfoStore()
    //获取当前用户所有的按钮权限
    const btnList = userInfoStore.btnList;
    //['user:add','user:deleteById']
    return btnList.indexOf(permission) !== -1
}
