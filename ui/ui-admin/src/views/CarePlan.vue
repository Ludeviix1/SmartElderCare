<script setup>
  import carePlanApi from '@/api/carePlan.js'
  import elderApi from '@/api/elder.js'
  import userApi from '@/api/user.js'
  import careLevelApi from '@/api/careLevel.js'
  import careItemApi from '@/api/careItem.js'
  import {ref} from 'vue'
  import {ElMessage, ElMessageBox} from 'element-plus'
  import {Plus} from '@element-plus/icons-vue'
  import hasBtnPermission from "@/utils/btnPermission.js";
  import {useUserInfoStore} from '@/store/userInfo.js'

  //表格数据
  const list = ref([])
  const total = ref(0)
  //分页信息和搜索条件
  const carePlanQuery = ref({
    name: '',
    elderId: null,
    careLevelId: null,
    status: null,
    userId: null,
    page: 1,
    limit: 10
  })

  //时间范围
  const createTimeRange = ref([])
  const loadData = () => {
    carePlanQuery.value.beginCreateTime = createTimeRange.value?.[0]
    carePlanQuery.value.endCreateTime = createTimeRange.value?.[1]

    carePlanApi.list(carePlanQuery.value).then(result => {
      list.value = result.data.records
      total.value = result.data.total
    })
  }

  const userInfoStore = useUserInfoStore()
  const isHugong = ref(false)

  //搜索、表单下拉用的老人、护理人员、护理等级、护理项目
  const elderOptions = ref([])
  const userOptions = ref([])
  const initAccess = () => {
    const currentUserId = userInfoStore.user?.id
    if (!currentUserId) return Promise.resolve()
    return userApi.selectAssignedRole(currentUserId).then(result => {
      const assignedIds = result.data?.assignedRoleIdList || []
      isHugong.value = (result.data?.roleList || []).some(role => role.code === 'hugong' && assignedIds.includes(role.id))
      if (isHugong.value) carePlanQuery.value.userId = currentUserId
    })
  }
  const careLevelOptions = ref([])
  const careItemOptions = ref([])
  const loadOptions = () => {
    elderApi.list({page: 1, limit: 1000}).then(result => {
      elderOptions.value = result.data.records
    })
    //护理人员下拉只显示护工角色的用户
    userApi.listByRoleCode('hugong').then(result => {
      userOptions.value = result.data
    })
    careLevelApi.list({page: 1, limit: 1000}).then(result => {
      careLevelOptions.value = result.data.records
    })
    careItemApi.list({page: 1, limit: 1000}).then(result => {
      careItemOptions.value = result.data.records
    })
  }
  loadOptions()
  initAccess().finally(loadData)

  const onSearch = () => {
    carePlanQuery.value.page = 1
    loadData()
  }

  //状态（0：结束，1：开始）
  const statusOptions = ref([
    {value: 0, label: '结束'},
    {value: 1, label: '开始'}
  ])

  const statusText = (status) => {
    const statusObj = statusOptions.value.find(item => item.value === status)
    return statusObj ? statusObj.label : '未知'
  }

  const statusTagType = (status) => {
    const map = {0: 'info', 1: 'success'}
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
      carePlanApi.deleteById(id).then(result => {
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
      carePlanApi.deleteAll(ids).then(result => {
        if (result.code === 1) {
          ElMessage.success(result.msg)
          loadData()
        } else {
          ElMessage.error(result.msg)
        }
      })
    })
  }


  //添加、编辑
  const dialogFormVisible = ref(false)
  const carePlan = ref({})
  const title = ref()

  const showAddDialog = () => {
    dialogFormVisible.value = true
    title.value = '添加'
    carePlan.value = {carePlanItemList: []}
  }

  const showUpdateDialog = (id) => {
    dialogFormVisible.value = true
    title.value = '编辑'
    carePlan.value = {carePlanItemList: []}
    carePlanApi.selectById(id).then(result => {
      carePlan.value = result.data
      //没有明细时回填空数组
      carePlan.value.carePlanItemList = result.data.carePlanItemList || []
    })
  }

  const addOrUpdate = () => {
    if (carePlan.value.id) {//编辑
      carePlanApi.update(carePlan.value.id, carePlan.value).then(result => {
        if (result.code === 1) {
          ElMessage.success(result.msg)
          dialogFormVisible.value = false
          loadData()
        } else {
          ElMessage.error(result.msg)
        }
      })
    } else {//添加
      carePlanApi.add(carePlan.value).then(result => {
        if (result.code === 1) {
          ElMessage.success(result.msg)
          dialogFormVisible.value = false
          loadData()
        } else {
          ElMessage.error(result.msg)
        }
      })
    }
  }

  //护理项目明细：执行周期（0：天，1：周，2：月）
  const cycleOptions = ref([
    {value: 0, label: '天'},
    {value: 1, label: '周'},
    {value: 2, label: '月'}
  ])

  const addProject = () => {
    carePlan.value.carePlanItemList.push({
      careItemId: null,
      executeTime: null,
      executeCycle: 0,
      executeFrequency: 1
    })
  }

  const deleteProject = (index) => {
    carePlan.value.carePlanItemList.splice(index, 1)
  }

</script>

<template>
  <el-card class="">
    <template #header>
      <div class="header">
        <el-button type="primary" :icon="Plus" @click="showAddDialog" :disabled="!hasBtnPermission('carePlan:add')">添加</el-button>
        <el-button type="danger" @click="deleteAll" :disabled="!hasBtnPermission('carePlan:deleteAll')">批量删除</el-button>
      </div>
    </template>
    <el-form :inline="true">
      <el-form-item label="名称">
        <el-input v-model="carePlanQuery.name" placeholder="请输入名称" clearable style="width: 200px"/>
      </el-form-item>
      <el-form-item label="老人">
        <el-select v-model="carePlanQuery.elderId" placeholder="请选择老人" clearable filterable style="width: 160px">
          <el-option v-for="elder in elderOptions" :key="elder.id" :label="elder.name" :value="elder.id"/>
        </el-select>
      </el-form-item>
      <el-form-item v-if="!isHugong" label="护理人员">
        <el-select v-model="carePlanQuery.userId" placeholder="请选择护理人员" clearable filterable style="width: 160px">
          <el-option v-for="user in userOptions" :key="user.id" :label="user.name" :value="user.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="护理等级">
        <el-select v-model="carePlanQuery.careLevelId" placeholder="请选择护理等级" clearable filterable style="width: 160px">
          <el-option v-for="careLevel in careLevelOptions" :key="careLevel.id" :label="careLevel.name" :value="careLevel.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="carePlanQuery.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="开始" :value="1"/>
          <el-option label="结束" :value="0"/>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
            v-model="createTimeRange"
            type="datetimerange"
            value-format="YYYY-MM-DD HH:mm:ss"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="list" border style="width: 100%" show-overflow-tooltip ref="multipleTableRef" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column fixed prop="id" label="ID"/>
      <el-table-column prop="name" label="名称"/>
      <el-table-column prop="elderName" label="老人"/>
      <el-table-column prop="userName" label="护理人员"/>
      <el-table-column prop="careLevelName" label="护理等级"/>
      <el-table-column prop="startDate" label="开始日期" width="110"/>
      <el-table-column prop="endDate" label="结束日期" width="110"/>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="statusTagType(row.status)">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="200"/>
      <el-table-column align="center" width="200px" fixed="right" label="操作">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteById(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        v-model:current-page="carePlanQuery.page"
        v-model:page-size="carePlanQuery.limit"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
    />
  </el-card>


  <!--添加、编辑弹出框-->
  <el-dialog v-model="dialogFormVisible" :title="title" width="1000" :lock-scroll="false" :close-on-click-modal="false">
    <el-form :model="carePlan">
      <el-form-item label="名称" :label-width="80">
        <el-input v-model="carePlan.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="老人" :label-width="80">
        <el-select v-model="carePlan.elderId" placeholder="请选择老人" filterable style="width: 100%">
          <el-option v-for="elder in elderOptions" :key="elder.id" :label="elder.name" :value="elder.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="护理人员" :label-width="80">
        <el-select v-model="carePlan.userId" placeholder="请选择护理人员" filterable style="width: 100%">
          <el-option v-for="user in userOptions" :key="user.id" :label="user.name" :value="user.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="护理等级" :label-width="80">
        <el-select v-model="carePlan.careLevelId" placeholder="请选择护理等级" filterable style="width: 100%">
          <el-option v-for="careLevel in careLevelOptions" :key="careLevel.id" :label="careLevel.name" :value="careLevel.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="开始日期" :label-width="80">
        <el-date-picker
            v-model="carePlan.startDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择开始日期"
            style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="结束日期" :label-width="80">
        <el-date-picker
            v-model="carePlan.endDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择结束日期"
            style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="状态" :label-width="80">
        <el-radio-group v-model="carePlan.status">
          <el-radio :value="1">开始</el-radio>
          <el-radio :value="0">结束</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <!--护理项目明细-->
    <div class="item-header">
      <span>护理项目明细</span>
      <el-button type="primary" :icon="Plus" size="small" @click="addProject">添加项目</el-button>
    </div>
    <el-table :data="carePlan.carePlanItemList" border style="width: 100%">
      <!-- 护理项目 -->
      <el-table-column label="护理项目" min-width="200">
        <template #default="{row}">
          <el-select v-model="row.careItemId" placeholder="请选择护理项目" filterable style="width: 100%">
            <el-option v-for="careItem in careItemOptions" :key="careItem.id" :label="careItem.name" :value="careItem.id"/>
          </el-select>
        </template>
      </el-table-column>
      <!-- 护理服务时间 -->
      <el-table-column label="护理服务时间" min-width="180">
        <template #default="{row}">
          <el-time-picker
              v-model="row.executeTime"
              format="HH:mm"
              value-format="HH:mm"
              placeholder="选择时间"
              style="width: 100%"
          />
        </template>
      </el-table-column>
      <!-- 执行周期 -->
      <el-table-column label="执行周期" min-width="160">
        <template #default="{row}">
          <el-select v-model="row.executeCycle" style="width: 100%">
            <el-option v-for="item in cycleOptions" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </template>
      </el-table-column>
      <!-- 执行频次 -->
      <el-table-column label="执行频次" min-width="160">
        <template #default="{row}">
          <el-input-number v-model="row.executeFrequency" :min="1" :max="99" controls-position="right" style="width: 100%"/>
        </template>
      </el-table-column>
      <!-- 操作 -->
      <el-table-column label="操作" width="100" align="center">
        <template #default="{ $index }">
          <el-button type="danger" @click="deleteProject($index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="addOrUpdate">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
  .item-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 10px 0;
  }
</style>
