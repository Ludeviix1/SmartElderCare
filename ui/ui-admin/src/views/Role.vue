<script setup>
  import roleApi from '@/api/role.js'
  import {ref} from 'vue'
  import {ElMessage, ElMessageBox} from 'element-plus'

  //表格数据
  const list = ref([])
  const total = ref(0)
  //分页信息和搜索条件
  const roleQuery = ref({
    name: '',
    page: 1,
    limit: 10
  })

  //时间范围
  const createTimeRange = ref([])
  const loadData = () => {
    roleQuery.value.beginCreateTime = createTimeRange.value?.[0]
    roleQuery.value.endCreateTime = createTimeRange.value?.[1]

    roleApi.list(roleQuery.value).then(result => {
      list.value = result.data.records
      total.value = result.data.total
    })
  }

  loadData()

  const onSearch = () => {
    roleQuery.value.page = 1
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
      roleApi.deleteById(id).then(result => {
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
      roleApi.deleteAll(ids).then(result => {
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
  const role = ref({})
  const title = ref()

  const showAddDialog = () => {
    dialogFormVisible.value = true
    title.value = '添加'
    role.value = {}
  }

  const showUpdateDialog = (id) => {
    dialogFormVisible.value = true
    title.value = '编辑'
    role.value = {}
    roleApi.selectById(id).then(result => {
      role.value = result.data
    })
  }

  const addOrUpdate = () => {
    if (role.value.id) {//编辑
      roleApi.update(role.value.id, role.value).then(result => {
        if (result.code === 1) {
          ElMessage.success(result.msg)
          dialogFormVisible.value = false
          loadData()
        } else {
          ElMessage.error(result.msg)
        }
      })
    } else {//添加
      roleApi.add(role.value).then(result => {
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

  //权限分配的树形结构
  const dialogPermissionVisible = ref(false)
  const treeData = ref([])
  const treeRef = ref()
  const defaultProps = ref({
    children: 'children',
    label: 'name'
  })
  const showAssignedPermissionDialog = (row) => {
    role.value = row;
    dialogPermissionVisible.value = true;
    treeData.value = [];
    roleApi.selectAssignedPermission(row.id).then((result) => {
      if (result.code === 1) {
        treeData.value = result.data.permissionVOList;
        let checkedLeafIdList = [];
        //找到所有这个角色已经分配的权限里面的叶子权限
        getCheckedLeafIdList(result.data.permissionVOList, result.data.assignedPermissionIdList, checkedLeafIdList);
        treeRef.value.setCheckedKeys(checkedLeafIdList);
      }
    });
  }

  const getCheckedLeafIdList = (permissionVOList, assignedPermissionIdList, checkedLeafIdList) => {
    permissionVOList.forEach(permissionVO => {
      assignedPermissionIdList.forEach(id => {
        //这个角色下面的权限，而且是没有孩子的叶子节点
        if (permissionVO.id==id && permissionVO.children.length == 0) {
          checkedLeafIdList.push(id);
        } else if(permissionVO.id==id && permissionVO.children.length != 0) {
          getCheckedLeafIdList(permissionVO.children, assignedPermissionIdList, checkedLeafIdList);
        }
      })
    });
  }

  const assignPermission = () => {
    let checkedNodes = treeRef.value.getCheckedNodes(false, true);
    let permissionIds = checkedNodes.map((node) => node.id);
    permissionIds = permissionIds.join(',');
    roleApi.assignPermission(role.value.id, permissionIds).then((result) => {
      if (result.code === 1) {
        ElMessage({message: result.msg, type: 'success',})
        dialogPermissionVisible.value = false;
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
    <el-form :inline="true">
      <el-form-item label="名称">
        <el-input v-model="roleQuery.name" placeholder="请输入名称" clearable style="width: 200px"/>
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
      <el-table-column prop="code" label="编码" :show-overflow-tooltip="true"/>
      <el-table-column prop="name" label="名称"/>
      <el-table-column prop="description" label="描述" :show-overflow-tooltip="true"/>
      <el-table-column prop="createTime" label="创建时间" width="200"/>
      <el-table-column align="center" width="200px" fixed="right" label="操作">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteById(row.id)">删除</el-button>
          <el-button size="small" type="success" @click="showAssignedPermissionDialog(row)">权限</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        v-model:current-page="roleQuery.page"
        v-model:page-size="roleQuery.limit"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
    />
  </el-card>


  <!--添加、编辑弹出框-->
  <el-dialog v-model="dialogFormVisible" :title="title" width="500" :lock-scroll="false" :close-on-click-modal="false">
    <el-form :model="role">
      <el-form-item label="编码" :label-width="60">
        <el-input v-model="role.code" autocomplete="off" placeholder="如：ADMIN"/>
      </el-form-item>
      <el-form-item label="名称" :label-width="60">
        <el-input v-model="role.name" autocomplete="off" placeholder="如：管理员"/>
      </el-form-item>
      <el-form-item label="描述" :label-width="60">
        <el-input v-model="role.description" autocomplete="off" placeholder="如：拥有全部权限"/>
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

  <el-dialog
      title="分配权限"
      v-model="dialogPermissionVisible"
      width="40%" :lock-scroll="false">
    <el-tree
        :data="treeData"
        ref="treeRef"
        show-checkbox
        node-key="id"
        default-expand-all
        :props="defaultProps">
    </el-tree>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="assignPermission()">保存</el-button>
        <el-button  @click="dialogPermissionVisible = false">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>

</style>
