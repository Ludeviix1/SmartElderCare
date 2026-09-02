<script setup>
import {ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus} from '@element-plus/icons-vue'
import familyMemberApi from '@/api/familyMember.js'
import elderApi from '@/api/elder.js'

const list = ref([])
const total = ref(0)
const query = ref({name: '', phone: '', elderId: null, page: 1, limit: 10})
const elderOptions = ref([])
const dialogVisible = ref(false)
const form = ref({})
const title = ref('')
let selectedIds = []

const loadElders = () => elderApi.list({page: 1, limit: 1000}).then(result => {
  elderOptions.value = result.data.records
})
const loadData = () => familyMemberApi.list(query.value).then(result => {
  list.value = result.data.records
  total.value = result.data.total
})
const search = () => { query.value.page = 1; loadData() }
const showAdd = () => { title.value = '新增家属'; form.value = {isPrimary: 0}; dialogVisible.value = true }
const showEdit = id => familyMemberApi.getById(id).then(result => {
  title.value = '编辑家属'; form.value = result.data; dialogVisible.value = true
})
const save = () => {
  const request = form.value.id ? familyMemberApi.update(form.value.id, form.value) : familyMemberApi.add(form.value)
  request.then(result => {
    if (result.code === 1) { ElMessage.success(result.msg); dialogVisible.value = false; loadData() }
  })
}
const remove = id => ElMessageBox.confirm('确定删除该家属信息吗？', '提示', {type: 'warning', lockScroll: false}).then(() =>
  familyMemberApi.deleteById(id).then(result => { if (result.code === 1) { ElMessage.success(result.msg); loadData() } })
)
const removeAll = () => {
  if (!selectedIds.length) return ElMessage.warning('请先选择需要删除的数据')
  ElMessageBox.confirm('确定删除选中的家属信息吗？', '提示', {type: 'warning', lockScroll: false}).then(() =>
    familyMemberApi.deleteAll(selectedIds).then(result => { if (result.code === 1) { ElMessage.success(result.msg); loadData() } })
  )
}

loadElders()
loadData()
</script>

<template>
  <el-card>
    <template #header>
      <el-button type="primary" :icon="Plus" @click="showAdd">新增家属</el-button>
      <el-button type="danger" @click="removeAll">批量删除</el-button>
    </template>
    <el-form :inline="true">
      <el-form-item label="家属姓名"><el-input v-model="query.name" clearable placeholder="请输入姓名" /></el-form-item>
      <el-form-item label="联系电话"><el-input v-model="query.phone" clearable placeholder="请输入电话" /></el-form-item>
      <el-form-item label="关联老人">
        <el-select v-model="query.elderId" clearable filterable placeholder="全部老人" style="width: 180px">
          <el-option v-for="elder in elderOptions" :key="elder.id" :label="elder.name" :value="elder.id" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="search">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="list" border @selection-change="rows => selectedIds = rows.map(row => row.id)">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="elderName" label="关联老人" min-width="110" />
      <el-table-column prop="name" label="家属姓名" min-width="100" />
      <el-table-column prop="relation" label="关系" min-width="90" />
      <el-table-column prop="phone" label="联系电话" min-width="130" />
      <el-table-column prop="idCardNo" label="身份证号" min-width="180" />
      <el-table-column label="主要联系人" width="110"><template #default="{row}"><el-tag :type="row.isPrimary ? 'success' : 'info'">{{ row.isPrimary ? '是' : '否' }}</el-tag></template></el-table-column>
      <el-table-column prop="remark" label="备注" min-width="140" show-overflow-tooltip />
      <el-table-column label="操作" fixed="right" width="160" align="center"><template #default="{row}"><el-button size="small" type="primary" @click="showEdit(row.id)">编辑</el-button><el-button size="small" type="danger" @click="remove(row.id)">删除</el-button></template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" v-model:page-size="query.limit" :page-sizes="[10, 20, 30, 40]" :total="total" layout="total, sizes, prev, pager, next, jumper" style="margin-top: 20px; justify-content: flex-end" @change="loadData" />
  </el-card>
  <el-dialog v-model="dialogVisible" :title="title" width="520" :lock-scroll="false" :close-on-click-modal="false">
    <el-form :model="form" label-width="90px">
      <el-form-item label="关联老人" required><el-select v-model="form.elderId" filterable placeholder="请选择老人" style="width: 100%"><el-option v-for="elder in elderOptions" :key="elder.id" :label="elder.name" :value="elder.id" /></el-select></el-form-item>
      <el-form-item label="家属姓名" required><el-input v-model="form.name" /></el-form-item>
      <el-form-item label="关系" required><el-input v-model="form.relation" placeholder="如：子女、配偶" /></el-form-item>
      <el-form-item label="联系电话" required><el-input v-model="form.phone" /></el-form-item>
      <el-form-item label="身份证号"><el-input v-model="form.idCardNo" /></el-form-item>
      <el-form-item label="家庭住址"><el-input v-model="form.address" /></el-form-item>
      <el-form-item label="主要联系人"><el-radio-group v-model="form.isPrimary"><el-radio :value="1">是</el-radio><el-radio :value="0">否</el-radio></el-radio-group></el-form-item>
      <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
    </el-form>
    <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
  </el-dialog>
</template>
