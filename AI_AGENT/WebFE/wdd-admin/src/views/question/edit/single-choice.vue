<template>
  <div class="app-container">
    <div class="question-edit-contain">
      <div class="left">
        <el-card header="题目编辑区域：">
          <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
            <el-form-item label="分类：">
              <el-tree-select v-model="form.questionArchiveId" :data="questionArchiveTree" check-strictly
                              :render-after-expand="true" default-expand-all placeholder="题目分类" clearable/>
            </el-form-item>
            <el-form-item label="题干：" prop="title" required>
              <el-input v-model="form.title">
                <template #suffix>
                  <el-icon class="el-input__icon wdd-question-edit-icon"
                           @click="inputClick(form,'title')" size="large">
                    <IconEdit/>
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="选项：" prop="items" class="editor-form-item" required>
              <el-form-item :label="item.prefix" :key="index" v-for="(item,index) in form.items"
                            label-width="50px"
                            class="question-item-label">
                <div class="question-list-item">
                  <el-form-item class="item-prefix-parent" :prop="'items.' + index + '.prefix'" :rules="{required: true, message: '请输入选项前缀', trigger: 'blur'}">
                    <el-input v-model="item.prefix" class="item-prefix"/>
                  </el-form-item>
                  <el-form-item class="item-content-parent" :prop="'items.' + index + '.content'" :rules="{required: true, message: '请输入选项内容', trigger: 'blur'}">
                    <el-input v-model="item.content" class="question-item-content-input">
                      <template #suffix>
                        <el-icon class="el-input__icon wdd-question-edit-icon"
                                 @click="inputClick(item,'content')"
                                 size="large">
                          <IconEdit/>
                        </el-icon>
                      </template>
                    </el-input>
                  </el-form-item>
                </div>
              </el-form-item>
            </el-form-item>
            <el-form-item label="标答：" prop="correct" required>
              <el-radio-group v-model="form.correct">
                <el-radio v-for="item in form.items" :key="item.prefix" :label="item.prefix">
                  {{ item.prefix }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="解析：" prop="analyze">
              <el-input v-model="form.analyze">
                <template #suffix>
                  <el-icon class="el-input__icon wdd-question-edit-icon"
                           @click="inputClick(form,'analyze')"
                           size="large">
                    <IconEdit/>
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="分数：" prop="score" required>
              <el-input-number v-model="form.score" :precision="1" :step="1" :max="100"
                               :min="0.1"></el-input-number>
            </el-form-item>
            <el-form-item label="难度：" prop="difficult" required>
              <el-rate v-model="form.difficult" class="question-item-rate" show-text :max="3" :min="1"
                       :texts="['简单','较难','困难']"></el-rate>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm">提交</el-button>
              <el-button @click="resetForm">重置</el-button>
              <el-button type="success" @click="questionItemAdd">添加选项</el-button>
              <el-button type="danger" @click="questionItemRemove">删除选项</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
      <div class="right">
        <el-card header="题目预览：" class="question-preview">
          <QuestionShow :qType="this.form.questionType" :question="this.form"/>
        </el-card>
      </div>
    </div>
    <el-dialog v-model="richEditor.dialogVisible" append-to-body
               @opened="editorOpen" :close-on-click-modal="false" width="80%"
               class="rich-editor-dialog" :show-close="false" center>
      <editor v-model="richEditor.content" :eFocus="true" ref="editor"></editor>
      <template #footer>
         <span class="dialog-footer">
              <el-button type="primary" @click="editorConfirm"
                         v-has-perm="['question:create:singleChoice','question:update:singleChoice']">确 定</el-button>
              <el-button @click="editorClose">取 消</el-button>
            </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {clearTagP, numberConvert} from '@/utils'
import {Edit as IconEdit, Delete as IconDelete} from '@element-plus/icons-vue'
import QuestionShow from '../components/Show.vue'
import Editor from '@/components/Editor/index.vue'
import {singleChoiceSelect, singleChoiceEdit} from '@/api/question'
import {tree} from '@/api/questionArchive'
import useStore from '@/store'

const {tagsView} = useStore()

export default {
  name: 'QuestionSingleChoice',
  components: {
    Editor, QuestionShow, IconEdit, IconDelete
  },
  data() {
    let checkRate = (rule, value, callback) => {
      if (1 <= value && value <= 3) {
        return callback()
      }
      return callback('请选择难度')
    }
    return {
      form: {
        id: null,
        questionType: 1,
        questionArchiveId: null,
        title: '',
        domains: [{
          value: ''
        }],
        items: [
          {prefix: 'A', content: ''},
          {prefix: 'B', content: ''},
          {prefix: 'C', content: ''},
          {prefix: 'D', content: ''}
        ],
        analyze: '',
        correct: null,
        score: null,
        difficult: null
      },
      questionArchiveTree: [],
      formLoading: false,
      rules: {
        title: [
          {required: true, message: '请输入题干'}
        ],
        items: [
          {required: true, message: '请输入选项'}
        ],
        score: [
          {required: true, message: '请输入分数'}
        ],
        correct: [
          {required: true, message: '请选择正确答案'}
        ],
        difficult: [
          {required: true, validator: checkRate}
        ]
      },
      richEditor: {
        dialogVisible: false,
        object: null,
        parameterName: '',
        content: ''
      }
    }
  },
  created() {
    let id = this.$route.query.id

    tree().then(re => {
      this.questionArchiveTree = re.response
    })

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      singleChoiceSelect(id).then(re => {
        this.form = re.response
        this.form.score = numberConvert(this.form.score)
      }).finally(() => {
        this.formLoading = false
      })
    }
  },
  methods: {
    editorOpen() {
      this.$nextTick(() => {
        this.richEditor.content = this.richEditor.object[this.richEditor.parameterName]
        this.$refs.editor.editorFocus()
      })
    },
    inputClick(object, parameterName) {
      this.richEditor.object = object
      this.richEditor.parameterName = parameterName
      this.richEditor.dialogVisible = true
    },
    editorConfirm() {
      this.richEditor.object[this.richEditor.parameterName] = clearTagP(this.richEditor.content)
      this.richEditor.content = ''
      this.richEditor.dialogVisible = false
    },
    editorClose() {
      this.richEditor.object[this.richEditor.parameterName] = clearTagP(this.richEditor.content)
      this.richEditor.content = ''
      this.richEditor.dialogVisible = false
    },
    questionItemRemove() {
      if (this.form.items.length <= 2) {
        this.$message.error("题目选项不能少于2个");
        return;
      }
      this.form.items.splice(this.form.items.length - 1, 1)
      this.form.correct = null
    },
    questionItemAdd() {
      let items = this.form.items
      let newLastPrefix = 'A'
      if (items.length > 0) {
        let last = items[items.length - 1].prefix
        if (last.match(/\d{1,2}/g) !== null) {
          newLastPrefix = (parseInt(last) + 1).toString()
        } else {
          newLastPrefix = String.fromCharCode(last.charCodeAt() + 1)
        }
      }
      items.push({id: null, prefix: newLastPrefix, content: ''})
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          singleChoiceEdit(this.form).then(re => {
            if (re.code === 1) {
              this.$message.success(re.message)
              if (window.parent.opener.editSuccess) {
                window.parent.opener.editSuccess(re.response)
                window.close()
              } else if (window.parent.opener.addSuccess) {
                window.parent.opener.addSuccess(re.response)
                window.close()
              } else if (window.parent.opener.addSuccess) {
                window.parent.opener.addSuccess(re.response)
                window.close()
              } else {
                tagsView.delCurrentView(this).then(() => {
                  this.$router.push('/question/list')
                })
              }
            } else {
              this.$message.error(re.message)
            }
          }).finally(() => {
            this.formLoading = false
          })
        } else {
          return false
        }
      })
    },
    resetForm() {
      this.form = {
        id: null,
        questionType: 1,
        title: '',
        items: [
          {prefix: 'A', content: ''},
          {prefix: 'B', content: ''},
          {prefix: 'C', content: ''},
          {prefix: 'D', content: ''}
        ],
        analyze: '',
        correct: null,
        score: null,
        difficult: null
      }
      this.$refs['form'].resetFields()
    }
  }
}
</script>
