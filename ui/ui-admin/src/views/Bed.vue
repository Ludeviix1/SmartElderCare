<script setup>
import {ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus, Refresh, Search} from '@element-plus/icons-vue'
import bedApi from '@/api/bed.js'
import elderApi from '@/api/elder.js'

const list = ref([])
const total = ref(0)
const query = ref({building: '', roomNo: '', status: null, page: 1, limit: 10})
const dialogVisible = ref(false)
const assignVisible = ref(false)
const form = ref({})
const assignBed = ref(null)
const selectedElderId = ref(null)
const elderOptions = ref([])
const title = ref('')
let selectedIds = []

const buildingOptions = Array.from({length: 6}, (_, index) => `${index + 1}号楼`)
const floorOptions = Array.from({length: 6}, (_, index) => index + 1)
const roomOptions = Array.from({length: 25}, (_, index) => String(index + 1))
const statusText = status => ({0: '空闲', 1: '已入住', 2: '停用'})[status] || '未知'
const statusType = status => ({0: 'success', 1: 'warning', 2: 'info'})[status] || 'info'

const loadData = () => bedApi.list(query.value).then(result => {
  list.value = result.data.records
  total.value = result.data.total
})
const loadElders = () => elderApi.list({page: 1, limit: 1000}).then(result => { elderOptions.value = result.data.records })
const search = () => { query.value.page = 1; loadData() }
const resetSearch = () => { query.value = {building: '', roomNo: '', status: null, page: 1, limit: 10}; loadData() }
const showAdd = () => { title.value = '新增床位'; form.value = {status: 0, floor: null, building: '', roomNo: ''}; dialogVisible.value = true }
const showEdit = id => bedApi.getById(id).then(result => { title.value = '编辑床位'; form.value = result.data; dialogVisible.value = true })
const save = () => {
  if (!form.value.building || !form.value.floor || !form.value.roomNo || !form.value.bedNo) return ElMessage.warning('请完整选择楼栋、楼层、房间号并填写床位号')
  const request = form.value.id ? bedApi.update(form.value.id, form.value) : bedApi.add(form.value)
  request.then(result => { if (result.code === 1) { ElMessage.success(result.msg || '保存成功'); dialogVisible.value = false; loadData() } })
}
const showAssign = row => { assignBed.value = row; selectedElderId.value = null; assignVisible.value = true }
const assign = () => {
  if (!selectedElderId.value) return ElMessage.warning('请选择入住老人')
  bedApi.assign(assignBed.value.id, selectedElderId.value).then(result => { if (result.code === 1) { ElMessage.success(result.msg); assignVisible.value = false; loadData() } })
}
const release = row => ElMessageBox.confirm(`确定为 ${row.elderName} 办理退床吗？`, '提示', {type: 'warning', lockScroll: false}).then(() =>
  bedApi.release(row.id).then(result => { if (result.code === 1) { ElMessage.success(result.msg); loadData() } })
)
const remove = id => ElMessageBox.confirm('确定删除该床位吗？', '提示', {type: 'warning', lockScroll: false}).then(() =>
  bedApi.deleteById(id).then(result => { if (result.code === 1) { ElMessage.success(result.msg); loadData() } })
)
const removeAll = () => {
  if (!selectedIds.length) return ElMessage.warning('请先选择需要删除的数据')
  ElMessageBox.confirm('确定删除选中的床位吗？', '提示', {type: 'warning', lockScroll: false}).then(() =>
    bedApi.deleteAll(selectedIds).then(result => { if (result.code === 1) { ElMessage.success(result.msg); loadData() } })
  )
}

loadElders()
loadData()
</script>

<template>
  <el-card>
    <template #header>
      <el-button type="primary" :icon="Plus" @click="showAdd">新增床位</el-button>
      <el-button type="danger" @click="removeAll">批量删除</el-button>
    </template>
    <el-form :inline="true" class="query-form" @keyup.enter="search">
      <el-form-item label="楼栋"><el-input v-model="query.building" clearable placeholder="如：1号楼" /></el-form-item>
      <el-form-item label="房间号"><el-input v-model="query.roomNo" clearable /></el-form-item>
      <el-form-item label="状态"><el-select v-model="query.status" clearable placeholder="全部" style="width: 120px"><el-option label="空闲" :value="0" /><el-option label="已入住" :value="1" /><el-option label="停用" :value="2" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="search">查询</el-button><el-button :icon="Refresh" @click="resetSearch">重置</el-button></el-form-item>
    </el-form>
    <el-table :data="list" border @selection-change="rows => selectedIds = rows.map(row => row.id)">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="building" label="楼栋" min-width="90" /><el-table-column prop="floor" label="楼层" width="75" /><el-table-column prop="roomNo" label="房间号" min-width="90" /><el-table-column prop="bedNo" label="床位号" min-width="90" />
      <el-table-column prop="bedType" label="床位类型" min-width="100" /><el-table-column prop="monthlyPrice" label="月费（元）" min-width="100" /><el-table-column prop="elderName" label="入住老人" min-width="110" />
      <el-table-column label="状态" width="95"><template #default="{row}"><el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag></template></el-table-column>
      <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
      <el-table-column label="操作" fixed="right" width="250" align="center"><template #default="{row}"><el-button size="small" type="primary" @click="showEdit(row.id)">编辑</el-button><el-button v-if="row.status === 0" size="small" type="success" @click="showAssign(row)">入住</el-button><el-button v-if="row.status === 1" size="small" type="warning" @click="release(row)">退床</el-button><el-button size="small" type="danger" @click="remove(row.id)">删除</el-button></template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" v-model:page-size="query.limit" :page-sizes="[10, 20, 30, 40]" :total="total" layout="total, sizes, prev, pager, next, jumper" style="margin-top: 20px; justify-content: flex-end" @change="loadData" />
  </el-card>

  <el-dialog v-model="dialogVisible" :title="title" width="500" :lock-scroll="false" :close-on-click-modal="false">
    <el-form :model="form" label-width="90px">
      <el-form-item label="楼栋" required><el-select v-model="form.building" placeholder="请选择楼栋" style="width: 100%"><el-option v-for="building in buildingOptions" :key="building" :label="building" :value="building" /></el-select></el-form-item>
      <el-form-item label="楼层" required><el-select v-model="form.floor" placeholder="请选择楼层" style="width: 100%"><el-option v-for="floor in floorOptions" :key="floor" :label="`${floor}层`" :value="floor" /></el-select></el-form-item>
      <el-form-item label="房间号" required><el-select v-model="form.roomNo" placeholder="请选择房间号" style="width: 100%"><el-option v-for="room in roomOptions" :key="room" :label="`${room}号`" :value="room" /></el-select></el-form-item>
      <el-form-item label="床位号" required><el-input v-model="form.bedNo" /></el-form-item>
      <el-form-item label="床位类型"><el-input v-model="form.bedType" placeholder="如：单人床" /></el-form-item>
      <el-form-item label="月费"><el-input-number v-model="form.monthlyPrice" :precision="2" :min="0" /></el-form-item>
      <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio :value="0">空闲</el-radio><el-radio :value="2">停用</el-radio></el-radio-group></el-form-item>
      <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
    </el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
  </el-dialog>

  <el-dialog v-model="assignVisible" title="办理入住" width="420" :lock-scroll="false">
    <el-form label-width="80px"><el-form-item label="床位"><span>{{ assignBed?.building }} {{ assignBed?.roomNo }} 房 {{ assignBed?.bedNo }} 床</span></el-form-item><el-form-item label="入住老人"><el-select v-model="selectedElderId" filterable placeholder="请选择老人" style="width: 100%"><el-option v-for="elder in elderOptions" :key="elder.id" :label="`${elder.name}（${elder.phone || '无电话'}）`" :value="elder.id" /></el-select></el-form-item></el-form>
    <template #footer><el-button @click="assignVisible = false">取消</el-button><el-button type="primary" @click="assign">确认入住</el-button></template>
  </el-dialog>
</template>
