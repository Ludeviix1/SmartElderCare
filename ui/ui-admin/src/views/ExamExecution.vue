<script setup>
import {ref} from 'vue'
import {ElMessage} from 'element-plus'
import examAppointmentApi from '@/api/examAppointment.js'
import elderApi from '@/api/elder.js'
import userApi from '@/api/user.js'
import {useUserInfoStore} from '@/store/userInfo.js'
import hasBtnPermission from '@/utils/btnPermission.js'

const list = ref([]), total = ref(0), elders = ref([]), caregivers = ref([]), isHugong = ref(false)
const query = ref({elderId:null, caregiverId:null, status:null, assignmentStatus:1, page:1, limit:10})
const dialogVisible = ref(false), reportMode = ref(false), reportEditing = ref(false), execution = ref({appointment:{}, items:[], remark:''})
const load = () => examAppointmentApi.list(query.value).then(r => { list.value = r.data?.records || []; total.value = r.data?.total || 0 })
const loadOptions = () => { elderApi.list({page:1,limit:1000}).then(r => elders.value = r.data?.records || []); userApi.listByRoleCode('hugong').then(r => caregivers.value = r.data || []); const currentId = useUserInfoStore().user?.id; if (currentId) userApi.selectAssignedRole(currentId).then(r => { const ids = r.data?.assignedRoleIdList || []; isHugong.value = (r.data?.roleList || []).some(role => role.code === 'hugong' && ids.includes(role.id)) }) }
loadOptions(); load()
const openExecution = row => { reportMode.value = false; reportEditing.value = false; examAppointmentApi.executionDetail(row.id).then(r => { execution.value = {...r.data, appointment:{...row, ...r.data.appointment}, remark:r.data.appointment.remark || ''}; dialogVisible.value = true }) }
const openReport = row => { reportMode.value = true; reportEditing.value = false; examAppointmentApi.executionDetail(row.id).then(r => { execution.value = {...r.data, appointment:{...row, ...r.data.appointment}, remark:r.data.appointment.remark || ''}; dialogVisible.value = true }) }
const saveExecution = () => examAppointmentApi.execute(execution.value.appointment.id, {items:execution.value.items, remark:execution.value.remark}).then(r => { if (r.code === 1) { ElMessage.success('体检执行已完成'); dialogVisible.value=false; load() } else ElMessage.error(r.msg) })
const saveReport = () => examAppointmentApi.execute(execution.value.appointment.id, {items:execution.value.items, remark:execution.value.remark}).then(r => { if (r.code === 1) { ElMessage.success('报告已修改'); reportEditing.value=false; load() } else ElMessage.error(r.msg) })
const statusText = value => ({0:'待体检',1:'体检中',2:'已完成',3:'已取消',4:'已过期'}[value] || '未知')
</script>
<template>
  <el-card>
    <template #header><div class="header"><span>体检执行管理</span><span class="hint">已分配预约可录入检查结果并完成执行</span></div></template>
    <el-form :inline="true" class="query-form">
      <el-form-item label="老人"><el-select v-model="query.elderId" clearable filterable placeholder="全部老人"><el-option v-for="e in elders" :key="e.id" :label="e.name" :value="e.id"/></el-select></el-form-item>
      <el-form-item v-if="!isHugong" label="护工"><el-select v-model="query.caregiverId" clearable filterable placeholder="全部护工"><el-option v-for="u in caregivers" :key="u.id" :label="u.name" :value="u.id"/></el-select></el-form-item>
      <el-form-item label="状态"><el-select v-model="query.status" clearable placeholder="全部状态" class="status-select"><el-option label="待体检" :value="0"/><el-option label="体检中" :value="1"/><el-option label="已完成" :value="2"/></el-select></el-form-item><el-form-item><el-button type="primary" @click="query.page=1;load()">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="list" border stripe><el-table-column prop="id" label="ID" width="70"/><el-table-column prop="elderName" label="老人"/><el-table-column prop="packageName" label="体检套餐"/><el-table-column prop="appointmentDate" label="预约日期"/><el-table-column prop="appointmentTime" label="预约时间"/><el-table-column prop="caregiverName" label="负责护工"/><el-table-column label="执行状态"><template #default="{row}"><el-tag :type="row.status === 2 ? 'success' : 'warning'">{{statusText(row.status)}}</el-tag></template></el-table-column><el-table-column label="操作" width="130" fixed="right"><template #default="{row}"><el-button v-if="row.status === 2" size="small" type="success" @click="openReport(row)">查看报告</el-button><el-button v-else size="small" type="primary" :disabled="row.status === 3" @click="openExecution(row)">执行体检</el-button></template></el-table-column></el-table>
    <el-pagination v-model:current-page="query.page" v-model:page-size="query.limit" :total="total" layout="total, prev, pager, next" @change="load"/>
  </el-card>
  <el-dialog v-model="dialogVisible" :title="reportMode ? '体检报告' : '录入体检结果'" width="820px" :close-on-click-modal="false"><el-descriptions :column="3" border><el-descriptions-item label="老人">{{execution.appointment.elderName}}</el-descriptions-item><el-descriptions-item label="预约日期">{{execution.appointment.appointmentDate}}</el-descriptions-item><el-descriptions-item label="预约时间">{{execution.appointment.appointmentTime}}</el-descriptions-item></el-descriptions><el-table :data="execution.items" border style="margin-top:18px"><el-table-column prop="itemName" label="检查项目"/><el-table-column label="数值结果" width="160"><template #default="{row}"><el-input-number v-model="row.resultValue" :disabled="reportMode && !reportEditing" :controls="false" style="width:100%"/></template></el-table-column><el-table-column label="单位" width="120"><template #default="{row}"><el-input v-model="row.resultUnit" :disabled="reportMode && !reportEditing"/></template></el-table-column><el-table-column label="文字结果"><template #default="{row}"><el-input v-model="row.resultText" :disabled="reportMode && !reportEditing" placeholder="检查结果说明"/></template></el-table-column><el-table-column label="异常" width="100"><template #default="{row}"><el-switch v-model="row.abnormal" :disabled="reportMode && !reportEditing" :active-value="1" :inactive-value="0" active-text="是" inactive-text="否"/></template></el-table-column><el-table-column label="备注"><template #default="{row}"><el-input v-model="row.remark" :disabled="reportMode && !reportEditing"/></template></el-table-column></el-table><el-form label-width="80px" style="margin-top:18px"><el-form-item label="执行备注"><el-input v-model="execution.remark" :disabled="reportMode && !reportEditing" type="textarea" :rows="3"/></el-form-item></el-form><template #footer><el-button @click="dialogVisible=false">返回</el-button><el-button v-if="reportMode && !reportEditing && hasBtnPermission('examExecution:updateReport')" type="primary" @click="reportEditing=true">修改报告</el-button><el-button v-if="reportMode && reportEditing" type="primary" @click="saveReport">保存修改</el-button><el-button v-if="!reportMode" type="primary" @click="saveExecution">确认完成体检</el-button></template></el-dialog>
</template>
<style scoped>.header{display:flex;justify-content:space-between;align-items:center}.hint{color:var(--app-muted);font-size:13px}.status-select{width:140px}.el-pagination{justify-content:flex-end;margin-top:18px}</style>
