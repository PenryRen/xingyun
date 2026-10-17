<template>
  <div class="app-container random-contain">

    <el-card header="试卷编辑：">
      <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
        <el-form-item label="试卷分类：" class="tree-select-contain">
          <el-tree-select v-model="form.examPaperArchiveId" :data="examPaperArchiveTree" check-strictly
                          :render-after-expand="true" default-expand-all placeholder="试卷分类"/>
        </el-form-item>
        <el-form-item label="试卷名称：" prop="name" required>
          <el-input v-model="form.name"/>
        </el-form-item>

        <el-form-item label="发布班级：">
          <tree-select v-model="form.buildConfig.departmentIdList" :data="departmentTree" :clearable="true"
                       placeholder="班级" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="练习时间："
                      class="form-item-content-block" required>
          <el-date-picker v-model="form.limitDateTime" value-format="YYYY-MM-DD HH:mm:ss" type="datetimerange"
                          range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间"
                          :disabled-date="disabledDate">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="合格分：" prop="passScore" required>
          <el-input-number v-model="form.passScore" :precision="1" :step="1" :min="0.1" :max="maxPassScore"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="试卷总分：" required>
          <span>{{ form.sumScore }}</span>
        </el-form-item>
        <el-form-item label="考试时长：" prop="suggestTime" required>
          <el-input-number v-model="form.suggestTime" placeholder="分钟" :step="1" :max="600" :min="1"
          ></el-input-number>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card header="组题策略：" style="margin-top: 20px">
      <div class="paper-random-contain">
        <div class="paper-random-left">
          <el-form :model="randomAdd" ref="randomAddForm" label-width="110px">
            <el-form-item label="题目类型：">
              <el-select v-model="randomAdd.questionType" clearable>
                <el-option v-for="item in EnumMap.exam.question.typeEnum" :key="item.key"
                           :value="item.key"
                           :label="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="题目分类：" class="tree-select-contain">
              <el-tree-select v-model="randomAdd.questionArchiveId" :data="questionArchiveTree"
                              check-strictly
                              :render-after-expand="true" default-expand-all placeholder="题目分类"/>
            </el-form-item>
            <el-form-item label="题目难度：" prop="difficult">
              <el-rate v-model="randomAdd.difficult" class="question-item-rate" show-text :max="3"
                       :texts="['简单','较难','困难']"></el-rate>
            </el-form-item>
            <el-form-item label="题目数量：" prop="difficult">
              <el-input-number v-model="randomAdd.number" :step="1" :max="100" :min="1"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="每题分数：" prop="difficult">
              <el-input-number v-model="randomAdd.score" :precision="1" :step="1" :max="100" :min="0.1"
              ></el-input-number>
            </el-form-item>
            <el-form-item style="text-align: center">
              <el-button type="primary" style="width: 100%;margin-left: -80px;" @click="randomAddClick">
                添加
              </el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="paper-random-right">
          <draggable class="paper-random-right-drag" item-key="index" group="examPaperBuildRandomList"
                     handle=".drag"
                     v-model="form.buildConfig.examPaperBuildRandomList">
            <template #item="{index,element }">
              <div class="paper-random-item">
                <div class="drag">
                  题型：{{ questionTypeFormat(element.questionType) }}
                </div>
                <div class="drag">
                  分类：{{ element.questionArchiveStr }}
                </div>
                <div class="paper-random-right-rate drag" style="margin-right: 0px">
                  难度：
                  <el-rate v-model="element.difficult" :max="3" :disabled="true"></el-rate>
                </div>
                <div class="drag">
                  数量： {{ element.number }}
                </div>
                <div class="drag">
                  分数： {{ element.score }}
                </div>
                <div>
                  <el-button type="primary" circle @click="editorRandomItem(index,element)">
                    <el-icon>
                      <icon-edit/>
                    </el-icon>
                  </el-button>
                  <el-button type="danger" circle @click="removeRandomItem(index)">
                    <el-icon>
                      <icon-delete/>
                    </el-icon>
                  </el-button>
                </div>
              </div>
            </template>
          </draggable>
        </div>
      </div>
    </el-card>

    <div class="random-save-button">
      <el-button type="primary" @click="randomSubmit" style="width:100px;"
                 v-has-perm="['practice:build:create','practice:build:update']">提交
      </el-button>
      <el-button @click="resetForm" style="width:100px;">重置</el-button>
    </div>
  </div>
</template>

<script>

import {EnumMap, Format} from '@/api/EnumMap'
import {numberConvert, disabledDate} from '@/utils'
import {
  Edit as IconEdit, Delete as IconDelete
} from '@element-plus/icons-vue'
import {checkRandom} from '@/api/examPaperBuild'
import {tree as questionArchiveTree} from '@/api/questionArchive'
import {tree as paperArchiveTree} from '@/api/examPaperArchive'
import {tree as departmentTree} from '@/api/department'
import {select, edit} from '@/api/practice'
import draggable from 'vuedraggable'
import useStore from '@/store'

const {tagsView} = useStore()

export default {
  name: 'ExamPaperBuildRandom',
  components: {IconEdit, IconDelete, draggable},
  data() {
    return {
      EnumMap: EnumMap,
      disabledDate: disabledDate,
      randomFormLoading: false,
      randomAdd: {
        questionType: null,
        questionArchiveId: null,
        number: null,
        score: null,
        difficult: null
      },
      form: {
        id: null,
        buildType: 3,
        examPaperArchiveId: null,
        credentialTemplateId: null,
        limitDateTime: [],
        name: '',
        rangeType: 2,
        passScore: null,
        suggestTime: null,
        titleItems: [],
        buildConfig: {
          watch: true,
          cheat: false,
          maxCheatCount: null,
          faceCheck: false,
          capture: false,
          questionMess: false,
          questionItemMess: false,
          departmentIdList: [],
          examPaperUserSelectList: [],
          examPaperApplySelect: null,
          examPaperBuildRandomList: []
        },
        sumScore: 0
      },
      formLoading: false,
      rules: {
        name: [
          {required: true, message: '请输入试卷名称'}
        ],
        passScore: [
          {required: true, message: '请输入合格分'},
          {type: 'number', message: '合格分必须为数字值'}
        ],
        suggestTime: [
          {required: true, message: '请输入考试时长'},
          {type: 'number', message: '时长必须为数字值'}
        ]
      },
      questionArchiveTree: [],
      examPaperArchiveTree: [],
      departmentTree: []
    }
  },
  computed: {
    maxPassScore: function () {
      let sumScore = this.form.sumScore
      if (undefined !== sumScore && null !== sumScore && sumScore > 0) {
        return Number(sumScore)
      }
      return 100
    }
  },
  created() {
    let id = this.$route.query.id

    paperArchiveTree().then(re => {
      this.examPaperArchiveTree = re.response
    })

    questionArchiveTree().then(re => {
      this.questionArchiveTree = re.response
    })

    departmentTree().then(re => {
      this.departmentTree = re.response
    })


    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id).then(re => {
        this.form = re.response
        this.formNumberConvert()
      }).finally(() => {
        this.formLoading = false
      })
    }
  },
  methods: {
    randomSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          edit(this.form).then(re => {
            if (re.code === 1) {
              this.$message.success(re.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/practice/list')
              })
            } else {
              this.$message.error(re.message)
            }
          })
        } else {
          return false
        }
      })
    },
    questionTypeFormat(questionType) {
      return Format(EnumMap.exam.question.typeEnum, questionType)
    },
    sumScoreChange() {
      let score = 0
      let questionCount = 0
      for (const item of this.form.buildConfig.examPaperBuildRandomList) {
        questionCount += item.number
        score += parseInt(item.number * parseFloat(item.score) * 10)
      }
      if (score !== 0) {
        this.form.sumScore = Number(score / 10).toFixed(1)
        this.form.passScore = null
      }
      this.form.questionCount = questionCount
    },
    editorRandomItem(index, item) {
      this.randomAdd.questionType = item.questionType
      this.randomAdd.questionArchiveId = item.questionArchiveId
      this.randomAdd.number = item.number
      this.randomAdd.score = item.score
      this.randomAdd.difficult = item.difficult
      this.form.buildConfig.examPaperBuildRandomList.splice(index, 1)
      this.sumScoreChange()
    },
    removeRandomItem(index) {
      this.form.buildConfig.examPaperBuildRandomList.splice(index, 1)
      this.sumScoreChange()
    },
    randomAddClick() {
      let currentItem = this.randomAdd
      let randomList = this.form.buildConfig.examPaperBuildRandomList
      for (const item of randomList) {
        if (item.questionType === currentItem.questionType && item.questionArchiveId === currentItem.questionArchiveId && item.difficult === currentItem.difficult) {
          this.$message.error('已存在相同出题策略')
          return
        }
      }

      checkRandom(this.randomAdd).then(re => {
        if (re.code === 1) {
          this.form.buildConfig.examPaperBuildRandomList.push({
            questionType: currentItem.questionType,
            questionArchiveStr: re.response,
            questionArchiveId: currentItem.questionArchiveId,
            difficult: currentItem.difficult,
            number: currentItem.number,
            score: currentItem.score
          })
          this.sumScoreChange()
        } else {
          this.$message.error(re.message)
        }
      })
    },
    formNumberConvert() {
      this.form.passScore = numberConvert(this.form.passScore)
      this.form.buildConfig.examPaperBuildRandomList.forEach(item => {
        item.score = numberConvert(item.score)
      })
    },
    resetForm() {
      this.form = {
        id: null,
        buildType: 3,
        examPaperArchiveId: null,
        credentialTemplateId: null,
        limitDateTime: [],
        name: '',
        rangeType: 2,
        passScore: null,
        suggestTime: null,
        titleItems: [],
        buildConfig: {
          watch: true,
          cheat: false,
          maxCheatCount: null,
          faceCheck: false,
          capture: false,
          questionMess: false,
          questionItemMess: false,
          departmentIdList: [],
          examPaperUserSelectList: [],
          examPaperApplySelect: null,
          examPaperBuildRandomList: []
        },
        sumScore: 0
      }
      this.$refs['form'].resetFields()
    }
  }
}
</script>
