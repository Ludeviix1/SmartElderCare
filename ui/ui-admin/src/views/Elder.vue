<script setup>
  import elderApi from '@/api/elder.js'
  import tagApi from '@/api/tag.js'
  import {ref} from 'vue'
  import {ElMessage, ElMessageBox} from 'element-plus'
  //图片上传
  import {Plus, Search, Refresh} from '@element-plus/icons-vue'
  import {useTokenStore} from '@/store/token.js'
  const tokenStore = useTokenStore();

  //表格数据
  const list = ref([])
  const total = ref(0)
  //分页信息和搜索条件
  const elderQuery = ref({
    name: '',
    phone: '',
    tagIds: [],
    page: 1,
    limit: 10
  })

  //时间范围
  const createTimeRange = ref([])
  const loadData = () => {
    elderQuery.value.beginCreateTime = createTimeRange.value?.[0]
    elderQuery.value.endCreateTime = createTimeRange.value?.[1]
    //tagIds数组转逗号分隔字符串，后端List<Long>自动解析
    const params = {...elderQuery.value, tagIds: elderQuery.value.tagIds.join(',')}
    elderApi.list(params).then(result => {
      list.value = result.data.records
      total.value = result.data.total
    })
  }

  loadData()

  //搜索下拉用的所有标签
  const tagOptions = ref([])
  const loadTagOptions = () => {
    tagApi.list({page: 1, limit: 1000}).then(result => {
      tagOptions.value = result.data.records
    })
  }
  loadTagOptions()

  const onSearch = () => {
    elderQuery.value.page = 1
    loadData()
  }

  const resetSearch = () => {
    elderQuery.value = {name: '', phone: '', tagIds: [], page: 1, limit: 10}
    createTimeRange.value = []
    loadData()
  }

  //状态（0：禁用，1：启用，2：请假，3：退住中，4：入住中，5：已退住）
  const statusOptions = ref([
    {value: 0, label: '禁用'},
    {value: 1, label: '启用'},
    {value: 2, label: '请假'},
    {value: 3, label: '退住中'},
    {value: 4, label: '入住中'},
    {value: 5, label: '已退住'}
  ])

  const statusText = (status) => {
    const statusObj = statusOptions.value.find(item => item.value === status)
    return statusObj ? statusObj.label : '未知'
  }

  const statusTagType = (status) => {
    const map = {0: 'danger', 1: 'success', 2: 'warning', 3: 'info', 4: 'primary', 5: 'info'}
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
      elderApi.deleteById(id).then(result => {
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
      elderApi.deleteAll(ids).then(result => {
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
  const elder = ref({})
  const title = ref()

  const showAddDialog = () => {
    dialogFormVisible.value = true
    title.value = '添加'
    elder.value = {}
  }

  const showUpdateDialog = (id) => {
    dialogFormVisible.value = true
    title.value = '编辑'
    elder.value = {}
    elderApi.selectById(id).then(result => {
      elder.value = result.data
    })
  }

  const addOrUpdate = () => {
    if (elder.value.id) {//编辑
      elderApi.update(elder.value.id, elder.value).then(result => {
        if (result.code === 1) {
          ElMessage.success(result.msg)
          dialogFormVisible.value = false
          loadData()
        } else {
          ElMessage.error(result.msg)
        }
      })
    } else {//添加
      elderApi.add(elder.value).then(result => {
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

  //上传头像
  const handleAvatarSuccess = (result) => {
    elder.value.avatar = result.data
  }

  //分配标签
  const dialogTagVisible = ref(false)
  const tagList = ref([])
  const assignedTagIdList = ref([])
  const showAssignedTagDialog = (row) => {
    elder.value = row;
    elderApi.selectAssignedTag(row.id).then((result) => {
      tagList.value = result.data.tagList;
      assignedTagIdList.value = result.data.assignedTagIdList;
      dialogTagVisible.value = true;
    });
  }

  const assignTag = () => {
    const tagIds = assignedTagIdList.value.join(',');
    // /elders/assignTag?elderId=1&tagIds=1,2,3
    elderApi.assignTag(elder.value.id, tagIds).then((result) => {
      if (result.code === 1) {
        ElMessage.success(result.msg);
        dialogTagVisible.value = false;
        loadData(); //刷新列表，让标签列同步更新
      } else {
        ElMessage.error(result.msg);
      }
    });
  }




</script>

<template>
  <el-card class="">
    <template #header>
      <div class="header">
        <el-button type="primary" @click="showAddDialog">添加</el-button>
        <el-button type="danger" @click="deleteAll">批量删除</el-button>
      </div>
    </template>
    <el-form :inline="true" class="query-form" @keyup.enter="onSearch">
      <el-form-item label="名字">
        <el-input v-model="elderQuery.name" placeholder="请输入名字" clearable style="width: 200px"/>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="elderQuery.phone" placeholder="请输入手机号" clearable style="width: 200px"/>
      </el-form-item>
      <el-form-item label="标签">
        <el-select
            v-model="elderQuery.tagIds"
            multiple
            clearable
            collapse-tags
            collapse-tags-tooltip
            :max-collapse-tags="4"
            placeholder="请选择标签"
            style="width: 260px"
        >
          <el-option v-for="tag in tagOptions" :key="tag.id" :label="tag.name" :value="tag.id"/>
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
        <el-button type="primary" :icon="Search" @click="onSearch">搜索</el-button>
        <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="list" border style="width: 100%" show-overflow-tooltip ref="multipleTableRef" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column fixed prop="id" label="ID"/>
      <el-table-column prop="name" label="名字"/>
      <el-table-column prop="password" label="密码"/>
      <el-table-column prop="phone" label="电话"/>
      <el-table-column prop="idCardNo" label="身份证号" :show-overflow-tooltip="true"/>
      <el-table-column prop="avatar" label="头像">
        <template #default="{row}">
          <img :src="row.avatar" alt="老人头像" style="max-height: 40px; max-width: 120px;"/>
        </template>
      </el-table-column>
      <el-table-column prop="birthday" label="出生日期" width="100"/>
      <el-table-column prop="address" label="家庭住址" :show-overflow-tooltip="true"/>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="statusTagType(row.status)">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="tagNames" label="标签" width="200" :show-overflow-tooltip="false">
        <template #default="{row}">
          <el-tag
              v-for="tagName in row.tagNames"
              :key="tagName"
              size="small"
              effect="plain"
              style="margin: 0 4px 4px 0"
          >{{ tagName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" :show-overflow-tooltip="true"/>
      <el-table-column prop="createTime" label="创建时间" width="200"/>
      <el-table-column align="center" width="200px" fixed="right" label="操作">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteById(row.id)">删除</el-button>
          <el-button type="success" size="small" @click="showAssignedTagDialog(row)">标签</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        v-model:current-page="elderQuery.page"
        v-model:page-size="elderQuery.limit"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
    />
  </el-card>


  <!--添加、编辑弹出框-->
  <el-dialog v-model="dialogFormVisible" :title="title" width="500" :lock-scroll="false" :close-on-click-modal="false">
    <el-form :model="elder">
      <el-form-item label="名字" :label-width="80">
        <el-input v-model="elder.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="密码" :label-width="80">
        <el-input v-model="elder.password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="手机号" :label-width="80">
        <el-input v-model="elder.phone" autocomplete="off" />
      </el-form-item>
      <el-form-item label="身份证号" :label-width="80">
        <el-input v-model="elder.idCardNo" autocomplete="off" />
      </el-form-item>
      <el-form-item label="出生日期" :label-width="80">
        <el-date-picker
            v-model="elder.birthday"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择出生日期"
            style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="家庭住址" :label-width="80">
        <el-input v-model="elder.address" autocomplete="off" />
      </el-form-item>
      <el-form-item label="状态" :label-width="80">
        <el-select v-model="elder.status" placeholder="请选择状态">
          <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" :label-width="80">
        <el-input v-model="elder.remark" type="textarea" autocomplete="off" />
      </el-form-item>
      <el-form-item label="照片" :label-width="80">
        <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :headers="{Authorization: tokenStore.token}"
        >
          <img v-if="elder.avatar" :src="elder.avatar" class="avatar" alt="老人头像"/>
          <el-icon v-else class="avatar-uploader-icon">
            <Plus/>
          </el-icon>
        </el-upload>
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

  <!-- 标签分配dialog-->
  <el-dialog title="标签" v-model="dialogTagVisible" width="40%">
    <el-form ref="form" :model="elder" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="elder.name" disabled></el-input>
      </el-form-item>
      <el-form-item label="标签列表">
        <el-checkbox-group v-model="assignedTagIdList">
          <el-checkbox v-for="tag in tagList" :key="tag.id" :label="tag.id">{{tag.name}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="assignTag">保存</el-button>
        <el-button  @click="dialogTagVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<style scoped>
  .avatar-uploader .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>

