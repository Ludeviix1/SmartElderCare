<script setup>
  import examItemApi from '@/api/examItem.js'
  import {ref} from 'vue'
  import {ElMessage, ElMessageBox} from 'element-plus'
  import {Plus} from '@element-plus/icons-vue'
  import hasBtnPermission from "@/utils/btnPermission.js";

  //表格数据
  const list = ref([])
  const total = ref(0)
  //分页信息和搜索条件
  const examItemQuery = ref({
    name: '',
    status: null,
    page: 1,
    limit: 10
  })

  //时间范围
  const createTimeRange = ref([])
  const loadData = () => {
    examItemQuery.value.beginCreateTime = createTimeRange.value?.[0]
    examItemQuery.value.endCreateTime = createTimeRange.value?.[1]

    examItemApi.list(examItemQuery.value).then(result => {
      list.value = result.data.records
      total.value = result.data.total
    })
  }

  loadData()

  const onSearch = () => {
    examItemQuery.value.page = 1
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
      examItemApi.deleteById(id).then(result => {
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
      examItemApi.deleteAll(ids).then(result => {
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
  const examItem = ref({})
  const title = ref()

  const showAddDialog = () => {
    dialogFormVisible.value = true
    title.value = '添加'
    examItem.value = {}
  }

  const showUpdateDialog = (id) => {
    dialogFormVisible.value = true
    title.value = '编辑'
    examItem.value = {}
    examItemApi.selectById(id).then(result => {
      examItem.value = result.data
    })
  }

  const addOrUpdate = () => {
    if (examItem.value.id) {//编辑
      examItemApi.update(examItem.value.id, examItem.value).then(result => {
        if (result.code === 1) {
          ElMessage.success(result.msg)
          dialogFormVisible.value = false
          loadData()
        } else {
          ElMessage.error(result.msg)
        }
      })
    } else {//添加
      examItemApi.add(examItem.value).then(result => {
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

</script>

<template>
  <el-card class="">
    <template #header>
      <div class="header">
        <el-button type="primary" :icon="Plus" @click="showAddDialog" >添加</el-button>
        <el-button type="danger" @click="deleteAll" >批量删除</el-button>
      </div>
    </template>
    <el-form :inline="true">
      <el-form-item label="名称">
        <el-input v-model="examItemQuery.name" placeholder="请输入名称" clearable style="width: 200px"/>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="examItemQuery.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="启用" :value="1"/>
          <el-option label="禁用" :value="0"/>
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
      <el-table-column prop="unit" label="单位" width="80"/>
      <el-table-column prop="resultType" label="结果类型" width="100">
        <template #default="{row}">
          <el-tag :type="row.resultType === 1 ? 'success' : 'info'">{{ row.resultType === 1 ? '数值' : '文本' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="参考范围" width="180">
        <template #default="{row}">
          {{ row.referenceMin }} ~ {{ row.referenceMax }} {{ row.referenceUnit }}
        </template>
      </el-table-column>
      <el-table-column prop="description" label="项目说明" :show-overflow-tooltip="true"/>
      <el-table-column prop="sort" label="排序" width="80"/>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
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
        v-model:current-page="examItemQuery.page"
        v-model:page-size="examItemQuery.limit"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
    />
  </el-card>


  <!--添加、编辑弹出框-->
  <el-dialog v-model="dialogFormVisible" :title="title" width="500" :lock-scroll="false" :close-on-click-modal="false">
    <el-form :model="examItem">
      <el-form-item label="名称" :label-width="100">
        <el-input v-model="examItem.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="单项价格(元)" :label-width="100">
        <el-input-number v-model="examItem.price" :precision="2" :step="1" :min="0" autocomplete="off" />
      </el-form-item>
      <el-form-item label="单位" :label-width="100">
        <el-input v-model="examItem.unit" autocomplete="off" />
      </el-form-item>
      <el-form-item label="结果类型" :label-width="100">
        <el-radio-group v-model="examItem.resultType">
          <el-radio :value="0">文本</el-radio>
          <el-radio :value="1">数值</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="参考范围下限" :label-width="100">
        <el-input-number v-model="examItem.referenceMin" :precision="2" :step="1" autocomplete="off" />
      </el-form-item>
      <el-form-item label="参考范围上限" :label-width="100">
        <el-input-number v-model="examItem.referenceMax" :precision="2" :step="1" autocomplete="off" />
      </el-form-item>
      <el-form-item label="参考范围单位" :label-width="100">
        <el-input v-model="examItem.referenceUnit" autocomplete="off" />
      </el-form-item>
      <el-form-item label="项目说明" :label-width="100">
        <el-input v-model="examItem.description" type="textarea" :rows="3" autocomplete="off" />
      </el-form-item>
      <el-form-item label="排序" :label-width="100">
        <el-input-number v-model="examItem.sort" :min="0" autocomplete="off" />
      </el-form-item>
      <el-form-item label="状态" :label-width="100">
        <el-radio-group v-model="examItem.status">
          <el-radio :value="1">启用</el-radio>
          <el-radio :value="0">禁用</el-radio>
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
</template>
