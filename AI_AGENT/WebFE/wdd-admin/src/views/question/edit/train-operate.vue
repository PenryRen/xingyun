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
            <el-form-item label="实训环境：" prop="vmType" required>
              <el-select v-model="form.vmType">
                <el-option v-for="item in vmTypeTree" :key="item.guid"
                           :value="item.guid"
                           :label="item.vmName"></el-option>
              </el-select>
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
            <el-form-item label="规则类型：" prop="commandType" required>
              <el-radio-group v-model="form.commandType">
                <el-radio v-for="item in radioItems" :key="item.value" :label="item.value">
                  {{ item.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="执行命令：" prop="command" required>
              <el-input v-model="form.command">
                <template #suffix>
                  <el-icon class="el-input__icon wdd-question-edit-icon"
                           @click="inputClick(form,'command')"
                           size="large">
                    <IconEdit/>
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="正确结果：" prop="correct" required>
              <el-input v-model="form.correct">
                <template #suffix>
                  <el-icon class="el-input__icon wdd-question-edit-icon"
                           @click="inputClick(form,'correct')"
                           size="large">
                    <IconEdit/>
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>
            <!--<el-form-item label="解析：" prop="analyze">
              <el-input v-model="form.analyze">
                <template #suffix>
                  <el-icon class="el-input__icon wdd-question-edit-icon"
                           @click="inputClick(form,'analyze')"
                           size="large">
                    <IconEdit/>
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>-->
            <el-form-item label="分数：" prop="score" required>
              <el-input-number v-model="form.score" :precision="1" :step="1" :max="100" :min="0.1"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="难度：" prop="difficult" required>
              <el-rate v-model="form.difficult" class="question-item-rate" show-text :max="3" :min="1"
                       :texts="['简单','较难','困难']"></el-rate>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm">提交</el-button>
              <el-button @click="resetForm">重置</el-button>
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

    <el-dialog v-model="richEditor.dialogVisible" @opened="editorOpen" append-to-body
               :close-on-click-modal="false" width="80%" class="rich-editor-dialog"
               :show-close="false" center>
      <editor v-model="richEditor.content" :eFocus="true" ref="editor"></editor>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="editorConfirm">确 定</el-button>
          <el-button @click="editorClose">取 消</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>
  import {clearTagP, numberConvert} from '@/utils'
  import {Edit as IconEdit} from '@element-plus/icons-vue'
  import QuestionShow from '../components/Show.vue'
  import Editor from '@/components/Editor/index.vue'
  import {trainOperateEdit, trainOperateSelect} from '@/api/question'
  import {tree} from '@/api/questionArchive'
  import useStore from '@/store'
  import {vmTypeList as vmTypeTree} from '@/api/vmWare'

  const {tagsView} = useStore()

  export default {
    name: 'TrainOperate',
    components: {
      Editor, QuestionShow, IconEdit
    },
    data() {
      let checkRate = (rule, value, callback) => {
        if (1 <= value && value <= 3) {
          return callback()
        }
        return callback('请选择难度')
      }
      return {
        radioItems: [
          {label: '执行命令', value: '1'},
          {label: '匹配文本内容', value: '2'}
        ],
        form: {
          id: null,
          questionType: 7,
          questionArchiveId: null,
          vmType:null,
          title: '',
          items: [],
          analyze: '',
          commandType: "1",
          command: '',
          correct: '',
          score: null,
          difficult: null
        },
        questionArchiveTree: [],
        vmTypeTree:[],
        formLoading: false,
        rules: {
          vmType: [
            {required: true, message: '请选择实训环境'}
          ],
          title: [
            {required: true, message: '请输入题干'}
          ],
          commandType: [
            {required: true, message: '请选择规则类型'}
          ],
          command: [
            {required: true, message: '请输入执行命令'}
          ],
          correct: [
            {required: true, message: '请输入正确结果'}
          ],
          score: [
            {required: true, message: '请输入分数'}
          ],
          difficult: [
            {required: true, validator: checkRate}
          ]
        },
        richEditor: {
          dialogVisible: false,
          object: null,
          parameterName: '',
          content: null
        }
      }
    },
    created() {
      let id = this.$route.query.id

      tree().then(re => {
        this.questionArchiveTree = re.response
      })

      vmTypeTree().then(re => {
        this.vmTypeTree = re.response
      })

      if (id && parseInt(id) !== 0) {
        this.formLoading = true
        trainOperateSelect(id).then(re => {
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
        this.richEditor.dialogVisible = false
      },
      editorClose() {
        this.richEditor.object[this.richEditor.parameterName] = clearTagP(this.richEditor.content)
        this.richEditor.content = ''
        this.richEditor.dialogVisible = false
      },
      submitForm() {
        this.$refs.form.validate((valid) => {
          if (valid) {
            this.formLoading = true
            trainOperateEdit(this.form).then(re => {
              if (re.code === 1) {
                this.$message.success(re.message)
                if (window.parent.opener.editSuccess) {
                  window.parent.opener.editSuccess(re.response)
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
          questionType: 7,
          title: '',
          items: [],
          analyze: '',
          correct: '',
          commandType: "1",
          command: '',
          score: null,
          difficult: null
        }
        this.$refs['form'].resetFields()
      }
    }
  }
</script>
