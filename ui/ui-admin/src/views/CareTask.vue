<script setup>
  import careTaskApi from '@/api/careTask.js'
  import elderApi from '@/api/elder.js'
  import careItemApi from '@/api/careItem.js'
  import userApi from '@/api/user.js'
  import {ref} from 'vue'
  import {ElMessage, ElMessageBox} from 'element-plus'
  import {Plus} from '@element-plus/icons-vue'
  import hasBtnPermission from "@/utils/btnPermission.js";
  import {useUserInfoStore} from '@/store/userInfo.js'
  //打卡照片上传
  import {useTokenStore} from '@/store/token.js'
  const tokenStore = useTokenStore();

  //表格数据
  const list = ref([])
  const total = ref(0)
  //分页信息和搜索条件
  const careTaskQuery = ref({
    elderId: null,
    careItemId: null,
    userId: null,
    status: null,
    page: 1,
    limit: 10
  })

  //计划执行日期范围
  const planExecuteDateRange = ref([])
  const loadData = () => {
    careTaskQuery.value.beginPlanExecuteDate = planExecuteDateRange.value?.[0]
    careTaskQuery.value.endPlanExecuteDate = planExecuteDateRange.value?.[1]

    careTaskApi.list(careTaskQuery.value).then(result => {
      list.value = result.data.records
      total.value = result.data.total
    })
  }

  const userInfoStore = useUserInfoStore()
  const isHugong = ref(false)

  //搜索下拉用的老人、护理项目
  const elderOptions = ref([])
  const careItemOptions = ref([])
  const userOptions = ref([])
  const loadOptions = () => {
    elderApi.list({page: 1, limit: 1000}).then(result => {
      elderOptions.value = result.data.records
    })
    careItemApi.list({page: 1, limit: 1000}).then(result => {
      careItemOptions.value = result.data.records
    })
    userApi.listByRoleCode('hugong').then(result => { userOptions.value = result.data || [] })
  }
  loadOptions()
  const initAccess = () => {
    const currentUserId = userInfoStore.user?.id
    if (!currentUserId) return Promise.resolve()
    return userApi.selectAssignedRole(currentUserId).then(result => {
      const assignedIds = result.data?.assignedRoleIdList || []
      isHugong.value = (result.data?.roleList || []).some(role => role.code === 'hugong' && assignedIds.includes(role.id))
      if (isHugong.value) careTaskQuery.value.userId = currentUserId
    })
  }
  initAccess().finally(loadData)

  const onSearch = () => {
    careTaskQuery.value.page = 1
    loadData()
  }

  //状态（0：待执行，1：已完成，2：已跳过）
  const statusOptions = ref([
    {value: 0, label: '待执行'},
    {value: 1, label: '已完成'},
    {value: 2, label: '已跳过'}
  ])

  const statusText = (status) => {
    const statusObj = statusOptions.value.find(item => item.value === status)
    return statusObj ? statusObj.label : '未知'
  }

  const statusTagType = (status) => {
    const map = {0: 'warning', 1: 'success', 2: 'info'}
    return map[status] || 'info'
  }

  //根据id删除
  const deleteById = (id) => {
    ElMessageBox.confirm(
        '您确认要删除么?',
        '警告',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
          lockScroll: false //防止抖动
        }
    ).then(() => {
      careTaskApi.deleteById(id).then(result => {
        if (result.code === 1) {
          ElMessage.success(result.msg)
          loadData()
        } else {
          ElMessage.error(result.msg)
        }
      })
    })
  }

  let ids = []
  const handleSelectionChange = (rows) => {
    //console.log('多选', rows)
    ids = rows.map(row => row.id)
  }

  const deleteAll = () => {
    ElMessageBox.confirm(
        '您确认要删除么?',
        '警告',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
          lockScroll: false //防止抖动
        }
    ).then(() => {
      careTaskApi.deleteAll(ids).then(result => {
        if (result.code === 1) {
          ElMessage.success(result.msg)
          loadData()
        } else {
          ElMessage.error(result.msg)
        }
      })
    })
  }


  //编辑（护理任务由护理计划生成，没有添加）
  const dialogFormVisible = ref(false)
  const careTask = ref({})
  const title = ref()

  const showUpdateDialog = (id) => {
    dialogFormVisible.value = true
    title.value = '编辑'
    careTask.value = {}
    careTaskApi.selectById(id).then(result => {
      careTask.value = result.data
    })
  }

  const update = () => {
    careTaskApi.update(careTask.value.id, careTask.value).then(result => {
      if (result.code === 1) {
        ElMessage.success(result.msg)
        dialogFormVisible.value = false
        loadData()
      } else {
        ElMessage.error(result.msg)
      }
    })
  }

  //上传打卡照片
  const handleImgSuccess = (result) => {
    careTask.value.executeImg = result.data
  }

</script>

<template>
  <el-card class="">
    <template #header>
      <div class="header">
        <el-button type="danger" @click="deleteAll" :disabled="!hasBtnPermission('careTask:deleteAll')">批量删除</el-button>
      </div>
    </template>
    <el-form :inline="true">
      <el-form-item label="老人">
        <el-select v-model="careTaskQuery.elderId" placeholder="请选择老人" clearable filterable style="width: 160px">
          <el-option v-for="elder in elderOptions" :key="elder.id" :label="elder.name" :value="elder.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="护理项目">
        <el-select v-model="careTaskQuery.careItemId" placeholder="请选择护理项目" clearable filterable style="width: 160px">
          <el-option v-for="careItem in careItemOptions" :key="careItem.id" :label="careItem.name" :value="careItem.id"/>
        </el-select>
      </el-form-item>
      <el-form-item v-if="!isHugong" label="护理人员">
        <el-select v-model="careTaskQuery.userId" placeholder="全部护工" clearable filterable style="width: 160px">
          <el-option v-for="user in userOptions" :key="user.id" :label="user.name" :value="user.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="careTaskQuery.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="计划执行日期">
        <el-date-picker
            v-model="planExecuteDateRange"
            type="daterange"
            value-format="YYYY-MM-DD"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="list" border style="width: 100%" show-overflow-tooltip ref="multipleTableRef" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column fixed prop="id" label="ID"/>
      <el-table-column prop="elderName" label="老人"/>
      <el-table-column prop="carePlanName" label="护理计划"/>
      <el-table-column prop="careItemName" label="护理项目"/>
      <el-table-column prop="userName" label="护理员"/>
      <el-table-column prop="planExecuteDate" label="计划执行日期" width="110"/>
      <el-table-column prop="planExecuteTime" label="计划执行时间" width="100"/>
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{row}">
          <el-tag :type="statusTagType(row.status)">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="actualExecuteTime" label="实际完成时间" width="170"/>
      <el-table-column prop="executeResult" label="执行结果" :show-overflow-tooltip="true"/>
      <el-table-column prop="executeImg" label="打卡照片">
        <template #default="{row}">
          <img v-if="row.executeImg" :src="row.executeImg" style="max-height: 40px; max-width: 80px;"/>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" :show-overflow-tooltip="true"/>
      <el-table-column prop="createTime" label="创建时间" width="200"/>
      <el-table-column align="center" width="200px" fixed="right" label="操作">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteById(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        v-model:current-page="careTaskQuery.page"
        v-model:page-size="careTaskQuery.limit"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
    />
  </el-card>


  <!--编辑弹出框（状态、执行打卡信息）-->
  <el-dialog v-model="dialogFormVisible" :title="title" width="500" :lock-scroll="false" :close-on-click-modal="false">
    <el-form :model="careTask">
      <el-form-item label="状态" :label-width="80">
        <el-radio-group v-model="careTask.status">
          <el-radio :value="0">待执行</el-radio>
          <el-radio :value="1">已完成</el-radio>
          <el-radio :value="2">已跳过</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="实际完成时间" :label-width="100">
        <el-date-picker
            v-model="careTask.actualExecuteTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择实际完成时间"
            style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="执行结果" :label-width="80">
        <el-input v-model="careTask.executeResult" autocomplete="off" />
      </el-form-item>
      <el-form-item label="备注" :label-width="80">
        <el-input v-model="careTask.remark" type="textarea" :rows="3" autocomplete="off" />
      </el-form-item>
      <el-form-item label="打卡照片" :label-width="80">
        <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleImgSuccess"
            :headers="{Authorization: tokenStore.token}"
        >
          <img v-if="careTask.executeImg" :src="careTask.executeImg" class="avatar"/>
          <el-icon v-else class="avatar-uploader-icon">
            <Plus/>
          </el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="update">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
  .avatar-uploader .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }

  .avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
  }

  .el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
  }
</style>
