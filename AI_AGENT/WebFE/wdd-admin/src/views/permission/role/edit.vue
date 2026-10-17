<template>
  <div class="app-container">

    <el-form :model="form" ref="form" label-width="130px" v-loading="formLoading" :rules="formRules">
      <el-form-item label="角色名称：" prop="name" required>
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="数据权限：">
        <tree-select v-model="form.dataFilter.departmentIdList" :data="departmentTree" :clearable="true"
                     placeholder="班级" style="width: 100%"/>
      </el-form-item>
      <el-form-item label="菜单列表：" prop="menuSelect">
        <div class="role-menu-contain">
          <div class="role-menu-item" v-for="menuItem in form.roleMenuVMList" :key="menuItem.id">
            <label class="menu-name-label">{{ menuItem.menuName }}</label>
            <div class="menu-content">
              <div v-for="item in menuItem.roleMenuItemVMList" :key="item.id" style="margin-left: 30px">
                <el-checkbox :label="item.id" style="float: left;width:100px" @change="menuChange(item)"
                             v-model="item.rootSelect">{{ item.menuName }}
                </el-checkbox>
                <el-checkbox-group v-model="item.permissionSelect"
                                   style="float: left;margin-left: 100px">
                  <el-checkbox :label="pItem.identification" :key="pItem.id"
                               v-for="pItem in item.menuPermissions"
                               @change="childMenuChange(item,pItem)">{{ pItem.name }}
                  </el-checkbox>
                </el-checkbox-group>
              </div>
            </div>
          </div>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {select, create, edit} from '@/api/role'
import {tree} from '@/api/department'
import useStore from '@/store'

const {tagsView} = useStore()

export default {
  name: 'RoleEdit',
  data() {
    return {
      form: {
        id: null,
        name: null,
        dataFilter: {departmentIdList: []},
        roleMenuVMList: []
      },
      formRules: {
        name: [{required: true, message: '请输入角色名称'},
          {max: 255, message: '长度小于255个字符'}]
      },
      departmentTree: [],
      formLoading: false
    }
  },
  created() {
    let id = this.$route.query.id

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id).then(re => {
        this.form = re.response
      }).finally(() => {
        this.formLoading = false
      })
    } else {
      this.formLoading = true
      create().then(re => {
        this.form = re.response
      }).finally(() => {
        this.formLoading = false
      })
    }

    tree().then(re => {
      this.departmentTree = re.response
    })
  },
  methods: {
    menuChange(item) {
      if (item.permissionSelect.length > 0) {
        item.permissionSelect = []
      } else {
        item.permissionSelect = item.permissionAllSelect
      }
    },
    childMenuChange(item, pItem) {
      item.rootSelect = item.permissionSelect.length > 0
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          edit(this.form).then(data => {
            if (data.code === 1) {
              this.$message.success(data.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/permission/role/list')
              })
            } else {
              this.$message.error(data.message)
            }
          }).finally(() => {
            this.formLoading = false
          })
        }
      })
    },
    resetForm() {
      window.location.reload()
    }
  }
}
</script>


<style lang="scss" scoped>
.role-menu-contain {
  display: flex;
  flex-direction: column;

  .role-menu-item {
    .menu-name-label {
      width: 200px;
    }

    .menu-content {
      flex: 1;
      display: flex;
      flex-direction: column;
    }
  }

}
</style>
