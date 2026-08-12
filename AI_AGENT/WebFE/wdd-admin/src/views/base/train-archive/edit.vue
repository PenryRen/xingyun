<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="150px">
      <el-form-item label="培训分类结构：" required>
        <el-tree :data="trainArchiveTree" class="wdd-tree" node-key="id" default-expand-all
                 :expand-on-click-node="false" :draggable="treeDraggable" @node-drop="treeNodeDrop">
          <template #default="{ node, data }">
                      <span class="customer-tree-node">
                        <span>{{ node.label }}</span>
                        <span class="tree-action">
                          <el-button size="small" @click="() => append(data)" v-has-perm="['train:archive:create']">
                               <el-icon class="button-icon">
                                 <circle-plus/>
                               </el-icon>
                               添加
                            </el-button>
                          <el-button size="small" @click="() => update(data)" v-has-perm="['train:archive:update']">
                              <el-icon class="button-icon">
                                 <icon-edit/>
                               </el-icon>
                               修改
                          </el-button>
                          <el-button size="small" @click="() => remove(node, data)" v-has-perm="['train:archive:delete']">
                             <el-icon class="button-icon">
                                 <icon-delete/>
                               </el-icon>删除
                          </el-button>
                        </span>
                      </span>
          </template>
        </el-tree>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="addRoot" v-has-perm="['train:archive:create']">添加根节点
        </el-button>
      </el-form-item>
    </el-form>

    <el-dialog v-model="dialog.visible" append-to-body :close-on-click-modal="false" :show-close="false" center>
      <el-form :model="form" :rules="nameFormRules" ref="nameForm" label-width="120px">
        <el-form-item label="培训分类名称：" prop="name" required>
          <el-input v-model.trim="form.name" :validate-event="false" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="confirm">确 定</el-button>
          <el-button @click="close">取 消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>

import {edit, move, tree, deleteArchive} from '@/api/trainArchive'
import useStore from '@/store'
import {
  CirclePlus, Edit as IconEdit, Delete as IconDelete
} from '@element-plus/icons-vue'

const {permission} = useStore()

export default {
  name: 'TrainArchiveEdit',
  components: {CirclePlus, IconEdit, IconDelete},
  data() {
    return {
      form: {
        id: null,
        name: '',
        parentId: null,
        action: null
      },
      dialog: {
        visible: false
      },
      nameFormRules: {
        name: [{required: true, message: '培训分类名称不能为空'}, {
          max: 255,
          message: '长度小于255个字符'
        }]
      },
      trainArchiveTree: [],
      currentItem: null
    }
  },
  computed: {
    treeDraggable: function () {
      return permission.perms.some(per => {
        return per === 'department:move'
      })
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      tree().then(re => {
        this.trainArchiveTree = re.response
      })
    },
    addRoot() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.currentItem = null
          this.form.name = ''
          this.form.action = 1
          this.form.parentId = null
          this.dialog.visible = true
        } else {
          return false
        }
      })
    },
    close() {
      this.$refs.nameForm.resetFields()
      this.dialog.visible = false
    },
    confirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$refs.nameForm.validate((nameValid) => {
            if (nameValid) {
              if (this.form.action === 1) {
                this.form.id = null
              }
              edit(this.form).then(re => {
                if (re.code === 1) {
                  this.load()
                  this.dialog.visible = false
                } else {
                  this.$message.error(re.message)
                }
              })
            } else {
              return false
            }
          })
        } else {
          return false
        }
      })
    },
    treeNodeDrop(draggingNode, dropNode, dropType, ev) {
      let message = `此操作会将培训分类【${draggingNode.label}】放置到【${dropNode.label}】, 是否继续?`
      this.$confirm(message, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        move({
          draggingNodeId: draggingNode.data.id,
          dropNodeId: dropNode.data.id,
          dropType: dropType
        }).then(re => {
          if (re.code !== 1) {
            this.$message.error(re.message)
          }
          this.load()
        })
      }).catch(() => {
        this.load()
      })
    },
    append(data) {
      this.form.action = 1
      this.currentItem = data
      this.form.parentId = data.id
      this.form.name = ''
      this.dialog.visible = true
    },
    update(data) {
      this.form.action = 2
      this.currentItem = data
      this.form.id = data.id
      this.form.name = data.name
      this.dialog.visible = true
    },
    remove(node, data) {
      this.$confirm('此操作将永久删除该培训分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteArchive(data.id).then(re => {
          this.load()
        })
      })
    }
  }
}
</script>
