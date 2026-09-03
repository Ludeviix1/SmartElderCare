<script setup>
import {ref, onMounted, onBeforeUnmount} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import examAppointmentApi from '@/api/examAppointment.js'
import elderApi from '@/api/elder.js'
import examPackageApi from '@/api/examPackage.js'
import userApi from '@/api/user.js'

const list = ref([]), total = ref(0), elders = ref([]), packages = ref([]), caregivers = ref([])
const query = ref({elderId: null, packageId: null, caregiverId: null, assignmentStatus: null, status: null, page: 1, limit: 10})
const dialogVisible = ref(false), form = ref({}), saving = ref(false)
const assignDialogVisible = ref(false), assignForm = ref({id: null, caregiverId: null})
const statusText = s => ({0:'待体检',1:'体检中',2:'已完成',3:'已取消',4:'已过期'}[s] || '未知')
const load = () => examAppointmentApi.list(query.value).then(r => {
  if (r.code !== 1 || !r.data) { list.value = []; total.value = 0; ElMessage.error(r.msg || '预约列表加载失败'); return }
  list.value = r.data.records || []; total.value = r.data.total || 0
}).catch(() => { list.value = []; total.value = 0 })
const loadOptions = () => {
  elderApi.list({page:1,limit:1000}).then(r => { if (r.data) elders.value = r.data.records || [] })
  examPackageApi.list({page:1,limit:1000,status:1}).then(r => { if (r.data) packages.value = r.data.records || [] })
  userApi.listByRoleCode('hugong').then(r => caregivers.value = r.data || [])
}
loadOptions(); load()
let refreshTimer
onMounted(() => { refreshTimer = setInterval(load, 30000) })
onBeforeUnmount(() => clearInterval(refreshTimer))
const search = () => { query.value.page = 1; load() }
const openAdd = () => { form.value = {}; dialogVisible.value = true }
const add = () => { saving.value = true; examAppointmentApi.add(form.value).then(r => { if (r.code === 1) { ElMessage.success('预约成功'); dialogVisible.value = false; load() } else ElMessage.error(r.msg) }).finally(() => saving.value = false) }
const assign = row => { assignForm.value = {id: row.id, caregiverId: row.caregiverId || null}; assignDialogVisible.value = true }
const confirmAssign = () => examAppointmentApi.assign(assignForm.value.id, assignForm.value.caregiverId).then(() => { ElMessage.success('分配成功'); assignDialogVisible.value = false; load() })
const autoAssign = row => examAppointmentApi.autoAssign(row.id).then(r => { if (r.code === 1) { ElMessage.success('已自动分配给工作量最少的护工'); load() } else ElMessage.error(r.msg) })
const remove = row => ElMessageBox.confirm('确认删除该预约吗？','提示',{type:'warning'}).then(() => examAppointmentApi.deleteById(row.id)).then(() => { ElMessage.success('删除成功'); load() }).catch(() => {})
</script>
<template>
  <el-card>
    <template #header><div class="header"><span>体检预约管理</span><el-button type="primary" @click="openAdd">新增预约</el-button></div></template>
    <el-form :inline="true" class="query-form">
      <el-form-item label="老人"><el-select v-model="query.elderId" clearable filterable placeholder="全部老人"><el-option v-for="e in elders" :key="e.id" :label="e.name" :value="e.id"/></el-select></el-form-item>
      <el-form-item label="体检套餐"><el-select v-model="query.packageId" clearable filterable placeholder="全部套餐"><el-option v-for="p in packages" :key="p.id" :label="p.name" :value="p.id"/></el-select></el-form-item>
      <el-form-item label="护工"><el-select v-model="query.caregiverId" clearable filterable placeholder="全部护工"><el-option v-for="u in caregivers" :key="u.id" :label="u.name" :value="u.id"/></el-select></el-form-item>
      <el-form-item label="分配状态"><el-select v-model="query.assignmentStatus" clearable placeholder="全部"><el-option label="未分配" :value="0"/><el-option label="已分配" :value="1"/></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="search">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="ID" width="70"/><el-table-column prop="elderName" label="老人"/><el-table-column prop="packageName" label="体检套餐"/><el-table-column prop="appointmentDate" label="预约日期"/><el-table-column prop="appointmentTime" label="预约时间"/><el-table-column label="分配状态" width="110"><template #default="{row}"><el-tag :type="row.assignmentStatus === 1 ? 'success' : 'warning'">{{ row.assignmentStatus === 1 ? '已分配' : '未分配' }}</el-tag></template></el-table-column><el-table-column prop="caregiverName" label="负责护工"/><el-table-column label="体检状态" width="100"><template #default="{row}">{{ statusText(row.status) }}</template></el-table-column>
      <el-table-column label="操作" fixed="right" width="250"><template #default="{row}"><el-button size="small" type="primary" @click="assign(row)">手动分配</el-button><el-button size="small" type="success" @click="autoAssign(row)">自动分配</el-button><el-button size="small" type="danger" @click="remove(row)">删除</el-button></template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" v-model:page-size="query.limit" :total="total" layout="total, prev, pager, next" @change="load" />
  </el-card>
  <el-dialog v-model="dialogVisible" title="新增体检预约" width="500px"><el-form :model="form" label-width="90px"><el-form-item label="老人"><el-select v-model="form.elderId" filterable><el-option v-for="e in elders" :key="e.id" :label="e.name" :value="e.id"/></el-select></el-form-item><el-form-item label="体检套餐"><el-select v-model="form.packageId" filterable><el-option v-for="p in packages" :key="p.id" :label="p.name" :value="p.id"/></el-select></el-form-item><el-form-item label="预约日期"><el-date-picker v-model="form.date" value-format="YYYY-MM-DD" type="date"/></el-form-item><el-form-item label="预约时间"><el-time-picker v-model="form.time" value-format="HH:mm" format="HH:mm"/></el-form-item><el-form-item label="备注"><el-input v-model="form.remark" type="textarea"/></el-form-item></el-form><template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" :loading="saving" @click="add">确定</el-button></template></el-dialog>
  <el-dialog v-model="assignDialogVisible" title="手动分配护工" width="420px"><el-form label-width="80px"><el-form-item label="护工"><el-select v-model="assignForm.caregiverId" filterable placeholder="请选择护工" style="width:100%"><el-option v-for="u in caregivers" :key="u.id" :label="u.name" :value="u.id"/></el-select></el-form-item></el-form><template #footer><el-button @click="assignDialogVisible=false">取消</el-button><el-button type="primary" @click="confirmAssign">确定分配</el-button></template></el-dialog>
</template>
<style scoped>.header{display:flex;align-items:center;justify-content:space-between}.el-pagination{justify-content:flex-end;margin-top:18px}</style>
