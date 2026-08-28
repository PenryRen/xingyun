<template>
  <div class="app-container">
    <div class="wdd-show-row">
      <div class="wdd-show-col">
        <el-card header="菜单">
          <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="formRules">
            <el-form-item label="层级：" prop="level" required>
              <el-select v-model="form.level" placeholder="菜单层级">
                <el-option v-for="item in EnumMap.menu.levelEnum" :key="item.key" :value="item.key"
                           :label="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="父级菜单：" prop="parentId" v-if="form.level===2">
              <el-select v-model="form.parentId" placeholder="父级菜单">
                <el-option v-for="item in parentLevelEnum" :key="item.id" :value="item.id"
                           :label="item.metaTitle"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="菜单名称：" prop="menuName" required>
              <el-input v-model="form.menuName"></el-input>
            </el-form-item>
            <el-form-item label="页面标题：" prop="metaTitle" required>
              <el-input v-model="form.metaTitle"></el-input>
            </el-form-item>
            <el-form-item label="Url路径：" prop="path" required>
              <el-input v-model="form.path"></el-input>
            </el-form-item>
            <el-form-item label="页面名称：">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item label="视图路径：" prop="component" v-if="form.level===2">
              <el-input v-model="form.component"></el-input>
            </el-form-item>
            <el-form-item label="视图图标：" prop="metaIcon" v-if="form.level===1">
              <el-input v-model="form.metaIcon"></el-input>
            </el-form-item>
            <el-form-item label="是否隐藏：" v-if="form.level===2">
              <el-radio-group v-model="form.hidden">
                <el-radio :key="true" :label="true">是</el-radio>
                <el-radio :key="false" :label="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="高亮菜单：" v-if="form.level===2 && form.hidden" prop="metaActiveMenu">
              <el-input v-model="form.metaActiveMenu"></el-input>
            </el-form-item>
            <el-form-item label="排序：" prop="itemOrder" required>
              <el-input v-model.number="form.itemOrder"></el-input>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
      <div class="wdd-show-col">
        <el-card header="菜单权限">
          <el-table :data="form.permissionList" border fit highlight-current-row style="width: 100%"
                    row-key="identification" v-loading="formLoading" class="menu-permission-list">
            <el-table-column prop="name" label="名称" width="100px">
              <template #default="{row}">
                <div class="role-menu-permission-name">
                  {{ row.name }}
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="identification" label="权限标识"/>
            <el-table-column label="操作" align="center" width="150px">
              <template #default="{row}">
                <el-button @click="updatePermission(row)" size="small" class="wdd-button-mini">修改
                </el-button>
                <el-button type="danger" @click="removePermission(row)" size="small"
                           class="wdd-button-mini link-left">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="menu-list-submit">
            <el-button type="primary" @click="addPermission">添加权限</el-button>
          </div>
        </el-card>
      </div>
    </div>

    <div class="menu-list-submit">
      <el-button type="primary" @click="submitForm">提交</el-button>
      <el-button @click="resetForm">重置</el-button>
    </div>

    <el-dialog v-model="permission.showDialog" center class="permission-dialog" title="权限编辑"
               :close-on-click-modal="false">
      <el-form :model="permission.current" ref="permissionForm" label-width="100px" :rules="permission.formRules">
        <el-form-item label="权限名称：" prop="name" required>
          <el-input v-model="permission.current.name" :validate-event="false" clearable></el-input>
        </el-form-item>
        <el-form-item label="权限标识：" prop="identification" required>
          <el-input v-model="permission.current.identification" :validate-event="false" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="permissionSubmitForm">提交</el-button>
          <el-button @click="permissionCloseForm">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>

</template>

<script>
import {EnumMap} from '@/api/EnumMap'
import {list as menuList, select, edit} from '@/api/menu'
import Sortable from 'sortablejs'
import useStore from '@/store'

const {tagsView} = useStore()

export default {
  name: 'MenuEdit',
  data() {
    return {
      EnumMap: EnumMap,
      form: {
        id: null,
        menuName: null,
        path: null,
        name: null,
        metaTitle: null,
        metaIcon: null,
        component: null,
        level: null,
        parentId: null,
        itemOrder: null,
        hidden: false,
        metaActiveMenu: null,
        permissionList: []
      },
      permission: {
        showDialog: false,
        current: {
          name: null,
          identification: null
        },
        formRules: {
          name: [
            {required: true, message: '请输入权限名称'}
          ],
          identification: [
            {required: true, message: '请输入权限标识'}
          ]
        }
      },
      formRules: {
        level: [{required: true, message: '请选择层级'}],
        menuName: [{required: true, message: '请输入菜单名称'},
          {max: 255, message: '长度小于255个字符'}],
        metaTitle: [{required: true, message: '请输入页面标题'},
          {max: 255, message: '长度小于255个字符'}],
        path: [{required: true, message: '请输入页面路径'},
          {max: 255, message: '长度小于255个字符'}],
        itemOrder: [{required: true, message: '排序不能为空'}]
      },
      parentLevelEnum: [],
      formLoading: false
    }
  },
  mounted() {
    let id = this.$route.query.id

    menuList(1).then(re => {
      this.parentLevelEnum = re.response
    })
    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id).then(re => {
        this.form = re.response
      }).finally(() => {
        this.formLoading = false
      })
    }
    this.tableDraga()
  },
  methods: {
    tableDraga() {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let _this = this
      this.$nextTick(() => {
        const tbody = document.querySelector('.menu-permission-list tbody')
        Sortable.create(tbody, {
          handle: '.role-menu-permission-name',
          onEnd({newIndex, oldIndex}) {
            const currRow = _this.form.permissionList.splice(oldIndex, 1)[0]
            _this.form.permissionList.splice(newIndex, 0, currRow)
          }
        })
      })
    },
    addPermission() {
      this.permission.current = {}
      this.permission.showDialog = true
    },
    updatePermission(row) {
      this.permission.current = {...row}
      this.permission.showDialog = true
    },
    removePermission(row) {
      this.form.permissionList.forEach((item, index, arr) => {
        if (item.identification === row.identification) {
          arr.splice(index, 1)
        }
      })
    },
    permissionSubmitForm() {
      this.$refs.permissionForm.validate((valid) => {
        if (valid) {
          let result = this.form.permissionList.find(item => {
            return item.id === this.permission.current.id
          })
          if (result === undefined) {
            this.form.permissionList.push({
              name: this.permission.current.name,
              identification: this.permission.current.identification
            })
          } else {
            result.name = this.permission.current.name;
            result.identification = this.permission.current.identification;
          }
          this.permission.showDialog = false
        }
      })
    },
    permissionCloseForm() {
      this.permission.showDialog = false
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          edit(this.form).then(data => {
            if (data.code === 1) {
              this.$message.success(data.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/permission/menu/list')
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
      this.form = {
        id: null,
        menuName: null,
        path: null,
        name: null,
        metaTitle: null,
        metaIcon: null,
        component: null,
        level: null,
        parentId: null,
        itemOrder: null,
        hidden: false,
        metaActiveMenu: null,
        permissionList: []
      }
      this.$refs['form'].resetFields()
    }
  }
}
</script>

