<script setup>
  import userApi from '@/api/user.js'
  import {ref} from 'vue'
  import {ElMessage, ElMessageBox} from 'element-plus'
  import {
    Delete,
    Edit,
    Upload,
    Download,
    Plus
  } from '@element-plus/icons-vue'
  import {useTokenStore} from '@/store/token.js'
  import elderApi from "@/api/elder.js";
  import hasBtnPermission from "@/utils/btnPermission.js";
  const tokenStore = useTokenStore();

  //表格数据
  const list = ref([])
  const total = ref(0)
  //分页信息和搜索条件
  const userQuery = ref({
    name: '',
    email: '',
    page: 1,
    limit: 10
  })

  /*function loadData() {
      userApi.list(userQuery.value).then(result => {
          list.value = result.data.records
          total.value = result.data.total
      })
  }*/

  //时间范围
  const createTimeRange = ref([])
  const loadData = () => {
    userQuery.value.beginCreateTime = createTimeRange.value?.[0]
    userQuery.value.endCreateTime = createTimeRange.value?.[1]

    userApi.list(userQuery.value).then(result => {
      list.value = result.data.records
      total.value = result.data.total
    })
  }

  loadData()

  const onSearch = () => {
    userQuery.value.page = 1
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
      userApi.deleteById(id).then(result => {
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
    console.log(ids)
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
      userApi.deleteAll(ids).then(result => {
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
  const user = ref({})
  const title = ref()

  const showAddDialog = () => {
    dialogFormVisible.value = true
    title.value = '添加'
    user.value = {}
  }

  const showUpdateDialog = (id) => {
    dialogFormVisible.value = true
    title.value = '编辑'
    user.value = {}
    userApi.selectById(id).then(result => {
      user.value = result.data
    })
  }

  const addOrUpdate = () => {
    if (user.value.id) {//编辑
      userApi.update(user.value.id, user.value).then(result => {
        if (result.code === 1) {
          ElMessage.success(result.msg)
          dialogFormVisible.value = false
          loadData()
        } else {
          ElMessage.error(result.msg)
        }
      })
    } else {//添加
      userApi.add(user.value).then(result => {
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

  //switch
  const handleSwitchChange = (row) => {
    const user = {}
    user.id = row.id
    user.status = row.status
    userApi.update(user.id, user).then(result => {
      if (result.code === 1) {
        ElMessage.success(result.msg)
      } else {
        ElMessage.error(result.msg)
      }
    })
  }

  //上传头像
  const handleAvatarSuccess = (result) => {
    console.log(result)
    user.value.avatar = result.data
  }

  //导入、导出excel
  const exportExcel = () => {
    userApi.exportExcel().then((response) => {
      //从响应头 Content-Disposition 解析后端返回的文件名,后端做过 URLEncoder.encode,需要解码
      const disposition = response.headers['content-disposition'];
      let fileName = '用户信息.xlsx'; //兜底名
      if (disposition) {
        fileName = decodeURIComponent(disposition.split('filename=')[1]);
      }
      //responseType 为 blob 时 result.data 本身就是 Blob,直接用即可
      let url = window.URL.createObjectURL(response.data);
      const link = document.createElement("a"); // 创建a标签
      link.href = url;
      link.download = fileName; // 使用后端返回的文件名
      link.click();
      URL.revokeObjectURL(url);
    });
  }

  const importExcelSuccess = (result) => {
    if (result.code ==1) {
      ElMessage.success(result.msg)
      loadData()
    } else {
      ElMessage.error(result.msg)
    }
  }

  const dialogTagVisible = ref(false)
  const roleList = ref([])
  const assignedRoleIdList = ref([])
  const showAssignedRoleDialog = (row) => {
    user.value = row;
    userApi.selectAssignedRole(row.id).then((result) => {
      roleList.value = result.data.roleList;
      assignedRoleIdList.value = result.data.assignedRoleIdList;
      dialogTagVisible.value = true;
    });
  }

  const assignRole = () => {
    /*const dataDTO = {
      userId: user.value.id,
      assignedRoleIdList: assignedRoleIdList.value
    };*/
    const roleIds = assignedRoleIdList.value.join(',');
    userApi.assignRole(user.value.id, roleIds).then((result) => {
      if (result.code === 1) {
        ElMessage.success(result.msg);
        dialogTagVisible.value = false;
        loadData();
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
        <el-button type="primary" :icon="Plus" @click="showAddDialog" :disabled="!hasBtnPermission('user:add')">添加</el-button>
        <el-button type="danger" :icon="Delete" @click="deleteAll" :disabled="!hasBtnPermission('user:deleteAll')">批量删除</el-button>
        <el-button type="primary" :icon="Download" @click="exportExcel">导出Excel</el-button>
        <el-upload
            :icon="Upload"
            class="inline-block"
            multiple=""
            method="post"
            action="/api/users/importExcel"
            style="display:inline-block;margin-left: 12px"
            accept=".xlsx,.xls"
            :show-file-list="false"
            :on-success="importExcelSuccess"
            :headers="{Authorization: tokenStore.token}"
            name="file">
          <el-button type="primary" :icon="Upload">导入Excel</el-button>
        </el-upload>
      </div>
    </template>
    <el-form :inline="true">
      <el-form-item label="名字">
        <el-input v-model="userQuery.name" placeholder="请输入名字" clearable style="width: 200px"/>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="userQuery.email" placeholder="请输入邮箱" clearable style="width: 200px"/>
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
      <el-table-column prop="name" label="名字"/>
      <el-table-column prop="password" label="密码"/>
      <el-table-column prop="phone" label="电话"/>
      <el-table-column prop="email" label="邮箱" :show-overflow-tooltip="true"/>
      <el-table-column prop="avatar" label="头像">
        <template #default="{row}">
          <img :src="row.avatar" style="max-height: 40px; max-width: 120px;"/>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-switch
              v-model="row.status"
              style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
              :active-value="1"
              :inactive-value="0"
              inline-prompt
              active-text="正常"
              inactive-text="禁用"
              @change="handleSwitchChange(row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="200"/>
      <el-table-column align="center" width="200px" fixed="right" label="操作">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteById(row.id)">删除</el-button>
          <el-button type="success" size="small" @click="showAssignedRoleDialog(row)">角色</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        v-model:current-page="userQuery.page"
        v-model:page-size="userQuery.limit"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
    />
  </el-card>


  <!--添加、编辑弹出框-->
  <el-dialog v-model="dialogFormVisible" :title="title" width="500" :lock-scroll="false" :close-on-click-modal="false">
    <el-form :model="user">
      <el-form-item label="名字" :label-width="60">
        <el-input v-model="user.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="密码" :label-width="60">
        <el-input v-model="user.password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="邮箱" :label-width="60">
        <el-input v-model="user.email" autocomplete="off" />
      </el-form-item>
      <el-form-item label="手机号" :label-width="60">
        <el-input v-model="user.phone" autocomplete="off" />
      </el-form-item>
      <el-form-item label="照片" :label-width="60">
        <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :headers="{Authorization: tokenStore.token}"
        >
          <img v-if="user.avatar" :src="user.avatar" class="avatar"/>
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

  <!-- 角色dialog-->
  <el-dialog title="角色" v-model="dialogTagVisible" width="40%">
    <el-form ref="form" :model="user" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="user.name" disabled></el-input>
      </el-form-item>
      <el-form-item label="标签列表">
        <el-checkbox-group v-model="assignedRoleIdList">
          <el-checkbox v-for="role in roleList" :key="role.id" :label="role.id">{{role.name}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="assignRole">保存</el-button>
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