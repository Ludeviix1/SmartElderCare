<script setup>
  import examPackageApi from '@/api/examPackage.js'
  import examItemApi from '@/api/examItem.js'
  import {ref} from 'vue'
  import {ElMessage, ElMessageBox} from 'element-plus'
  import {Plus} from '@element-plus/icons-vue'
  import {useTokenStore} from '@/store/token.js'
  const tokenStore = useTokenStore();

  //表格数据
  const list = ref([])
  const total = ref(0)
  //分页信息和搜索条件
  const examPackageQuery = ref({
    name: '',
    status: null,
    page: 1,
    limit: 10
  })

  //时间范围
  const createTimeRange = ref([])
  const loadData = () => {
    examPackageQuery.value.beginCreateTime = createTimeRange.value?.[0]
    examPackageQuery.value.endCreateTime = createTimeRange.value?.[1]

    examPackageApi.list(examPackageQuery.value).then(result => {
      list.value = result.data.records
      total.value = result.data.total
    })
  }

  loadData()

  const onSearch = () => {
    examPackageQuery.value.page = 1
    loadData()
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
      examPackageApi.deleteById(id).then(result => {
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
      examPackageApi.deleteAll(ids).then(result => {
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
  const examPackage = ref({})
  const title = ref()

  const showAddDialog = () => {
    dialogFormVisible.value = true
    title.value = '添加'
    examPackage.value = {}
  }

  const showUpdateDialog = (id) => {
    dialogFormVisible.value = true
    title.value = '编辑'
    examPackage.value = {}
    examPackageApi.selectById(id).then(result => {
      examPackage.value = result.data
    })
  }

  const addOrUpdate = () => {
    if (examPackage.value.id) {//编辑
      examPackageApi.update(examPackage.value.id, examPackage.value).then(result => {
        if (result.code === 1) {
          ElMessage.success(result.msg)
          dialogFormVisible.value = false
          loadData()
        } else {
          ElMessage.error(result.msg)
        }
      })
    } else {//添加
      examPackageApi.add(examPackage.value).then(result => {
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

  //上传图片
  const handleImageSuccess = (result) => {
    examPackage.value.image = result.data
  }

  //分配体检项目
  const assignDialogVisible = ref(false)
  const currentPackage = ref({})
  //Transfer的数据源和已选项（值为体检项目ID）
  const examItemList = ref([])
  const assignedExamItemIds = ref([])

  const showAssignDialog = (row) => {
    currentPackage.value = row
    assignDialogVisible.value = true
    examItemList.value = []
    assignedExamItemIds.value = []
    //并行加载所有体检项目和该套餐已分配的项目ID
    Promise.all([
      examItemApi.list({page: 1, limit: 1000}),
      examPackageApi.listExamItemIds(row.id)
    ]).then(([itemsResult, idsResult]) => {
      examItemList.value = itemsResult.data.records
      assignedExamItemIds.value = idsResult.data
    })
  }

  const assignExamItems = () => {
    examPackageApi.assignExamItems(currentPackage.value.id, assignedExamItemIds.value).then(result => {
      if (result.code === 1) {
        ElMessage.success(result.msg)
        assignDialogVisible.value = false
      } else {
        ElMessage.error(result.msg)
      }
    })
  }

</script>

<template>
  <el-card class="">
    <template #header>
      <div class="header">
        <el-button type="primary" :icon="Plus" @click="showAddDialog">添加</el-button>
        <el-button type="danger" @click="deleteAll">批量删除</el-button>
      </div>
    </template>
    <el-form :inline="true">
      <el-form-item label="名称">
        <el-input v-model="examPackageQuery.name" placeholder="请输入名称" clearable style="width: 200px"/>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="examPackageQuery.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="上架" :value="1"/>
          <el-option label="下架" :value="0"/>
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
      <el-table-column prop="price" label="价格(元)"/>
      <el-table-column prop="image" label="图片">
        <template #default="{row}">
          <img :src="row.image" style="max-height: 40px; max-width: 120px;"/>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="套餐说明" :show-overflow-tooltip="true"/>
      <el-table-column prop="sort" label="排序" width="80"/>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '上架' : '下架' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="200"/>
      <el-table-column align="center" width="280px" fixed="right" label="操作">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
          <el-button size="small" type="warning" @click="showAssignDialog(row)">分配项目</el-button>
          <el-button size="small" type="danger" @click="deleteById(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        v-model:current-page="examPackageQuery.page"
        v-model:page-size="examPackageQuery.limit"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
    />
  </el-card>


  <!--添加、编辑弹出框-->
  <el-dialog v-model="dialogFormVisible" :title="title" width="500" :lock-scroll="false" :close-on-click-modal="false">
    <el-form :model="examPackage">
      <el-form-item label="名称" :label-width="80">
        <el-input v-model="examPackage.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="价格(元)" :label-width="80">
        <el-input-number v-model="examPackage.price" :precision="2" :step="1" :min="0" autocomplete="off" />
      </el-form-item>
      <el-form-item label="图片" :label-width="80">
        <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleImageSuccess"
            :headers="{Authorization: tokenStore.token}"
        >
          <img v-if="examPackage.image" :src="examPackage.image" class="avatar"/>
          <el-icon v-else class="avatar-uploader-icon">
            <Plus/>
          </el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="套餐说明" :label-width="80">
        <el-input v-model="examPackage.description" type="textarea" :rows="3" autocomplete="off" />
      </el-form-item>
      <el-form-item label="排序" :label-width="80">
        <el-input-number v-model="examPackage.sort" :min="0" autocomplete="off" />
      </el-form-item>
      <el-form-item label="状态" :label-width="80">
        <el-radio-group v-model="examPackage.status">
          <el-radio :value="1">上架</el-radio>
          <el-radio :value="0">下架</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="addOrUpdate">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>

  <!--分配体检项目弹出框-->
  <el-dialog v-model="assignDialogVisible" :title="'给【' + currentPackage.name + '】分配体检项目'" width="800" :lock-scroll="false" :close-on-click-modal="false">
    <el-transfer
        v-model="assignedExamItemIds"
        :data="examItemList"
        :props="{key: 'id', label: 'name'}"
        :titles="['待选项目', '已选项目']"
        filterable
        filter-placeholder="请输入项目名称"
    />
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="assignExamItems">
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
