<script setup>
  import permissionApi from "@/api/permission.js";
  import {ref} from "vue";
  import {ElMessage, ElMessageBox} from "element-plus";
  import IconPicker from "@/components/IconPicker.vue";

  //保存返回树形结构数据，List<PermissionVO>
  const list = ref([])
  const loadData = () => {
    permissionApi.selectPermissionTree().then(result => {
      list.value = result.data
    })
  }
  loadData()

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
      permissionApi.deleteById(id).then(result => {
        ElMessage.success(result.msg)
        loadData()
      })
    })
  }

  //添加、编辑
  const dialogFormVisible = ref(false)
  const permission = ref({})
  const title = ref()
  const typeDisabled = ref(false)
  const type0Disabled = ref(false)
  const type1Disabled = ref(false)
  const type2Disabled = ref(false)

  const showAddDialog = (row) => {
    dialogFormVisible.value = true;
    permission.value = {};
    title.value = "添加下级菜单";

    if (row) {//点击右侧添加按钮
      permission.value.parentId = row.id;
      permission.value.parentName = row.name;
      if (row.type == 0) {
        //在目录下面添加，可以添加：菜单
        permission.value.type = 1;
        typeDisabled.value = false;
        type0Disabled.value = true;
        type1Disabled.value = false;
        type2Disabled.value = true;
      } else if (row.type == 1) {
        //在菜单下面添加：只能添加：按钮
        permission.value.type = 2;
        typeDisabled.value = false;
        type0Disabled.value = true;
        type1Disabled.value = true;
        type2Disabled.value = false;
      }
    } else {
      //添加顶级菜单
      title.value = "添加顶级菜单";
      //typeDisabled.value = true;
      type0Disabled.value = false;
      type1Disabled.value = false;
      type2Disabled.value = true;
      permission.value.type = 0;
      permission.value.parentId = 0;
    }
  }

  const showUpdateDialog = (row) => {
    dialogFormVisible.value = true
    title.value = '编辑'
    permission.value = {}
    permissionApi.selectById(row.id).then(result => {
      permission.value = result.data
    })
  }

  const addOrUpdate = () => {
    if (permission.value.id) {//编辑
      permissionApi.update(permission.value).then(result => {
        ElMessage.success(result.msg)
        dialogFormVisible.value = false
        loadData()
      })
    } else {//添加
      permissionApi.add(permission.value).then(result => {
        ElMessage.success(result.msg)
        dialogFormVisible.value = false
        loadData()

      })
    }
  }
</script>

<template>
  <el-button type="success" size="mini" @click="showAddDialog()">添加顶级菜单</el-button>
  <el-table :data="list" style="width: 100%; margin-bottom: 20px" row-key="id" border default-expand-all>
    <el-table-column prop="name" label="名称"/>
    <el-table-column prop="icon" width="80px" label="图标" #default="{row}">
      <el-icon><component :is="row.icon" /></el-icon>
    </el-table-column>
    <el-table-column prop="type" label="权限类型" #default="{row}">
      <el-tag v-if="row.type == 0">目录权限</el-tag>
      <el-tag v-if="row.type == 1" type="success">菜单权限</el-tag>
      <el-tag v-if="row.type == 2" type="warning">按钮权限</el-tag>
    </el-table-column>
    <el-table-column prop="path" label="路由地址"></el-table-column>
    <el-table-column prop="permissionValue" label="按钮权限"></el-table-column>
    <el-table-column prop="sort" label="排序"></el-table-column>
    <el-table-column label="操作" align="center" width="200px" fixed="right" #default="{row}">
      <el-button size="small" type="success" @click="showAddDialog(row)">添加</el-button>
      <el-button size="small" type="primary" @click="showUpdateDialog(row)">修改</el-button>
      <el-button size="small" type="danger" @click="deleteById(row.id)" :disabled="row.children?.length > 0">删除</el-button>
    </el-table-column>
  </el-table>

  <!-- 添加、修改的dialog -->
  <el-dialog v-model="dialogFormVisible" :title="title" width="500" :lock-scroll="false">
    <el-form :model="permission" label-width="80px">
      <el-form-item label="上级权限">
        <el-input v-model="permission.parentName" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="权限类型">
        <el-radio-group v-model="permission.type" :disabled="typeDisabled">
          <el-radio :label="0" :disabled="type0Disabled">目录</el-radio>
          <el-radio :label="1" :disabled="type1Disabled">菜单</el-radio>
          <el-radio :label="2" :disabled="type2Disabled">按钮</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="权限名字">
        <el-input v-model="permission.name"></el-input>
      </el-form-item>
      <el-form-item label="图标" v-if="permission.type == 0 || permission.type === 1">
        <!--<el-input v-model="permission.icon"></el-input>-->
        <IconPicker width="100px" v-model="permission.icon"></IconPicker>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number
            v-model="permission.sort"
            controls-position="right"
            :min="0"
        />
      </el-form-item>
      <el-form-item label="路由地址" v-if="permission.type === 1">
        <el-input v-model="permission.path"/>
      </el-form-item>
      <el-form-item label="按钮权限" v-if="permission.type === 2">
        <el-input
            v-model="permission.permissionValue"
            maxlength="100"
        />
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

<style scoped>

</style>