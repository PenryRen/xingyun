<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
      <el-form-item label="试卷分类：">
        <el-tree-select v-model="form.examPaperArchiveId" :data="examPaperArchiveTree" check-strictly
                        :render-after-expand="true" default-expand-all placeholder="试卷分类"/>
      </el-form-item>
      <el-form-item label="试卷名称：" prop="name" required>
        <el-input v-model="form.name"/>
      </el-form-item>
      <el-form-item>
        <el-tooltip content="离开试卷超过多次会自动提交试卷">
          <div class="exam-paper-cheat-div">
            <el-checkbox v-model="form.cheat">防作弊</el-checkbox>
            <el-input-number v-model="form.maxCheatCount" placeholder="防作弊次数" :step="1"
                             :max="99"
                             :min="1"
                             size="small" v-if="form.cheat" class="max-cheat-count"/>
          </div>
        </el-tooltip>
        <el-tooltip content="试卷题目顺序打乱">
          <el-checkbox v-model="form.questionMess">题序打乱</el-checkbox>
        </el-tooltip>
        <el-tooltip content="选择题选项打乱">
          <el-checkbox v-model="form.questionItemMess">选项打乱</el-checkbox>
        </el-tooltip>
      </el-form-item>
      <el-form-item :key="index" :label="`标题${index+1}：`" v-for="(titleItem,index) in form.titleItems"
                    class="form-item-content-block">
        <div class="exam-paper-title">
          <el-input v-model="titleItem.name" :validate-event="false"/>
          <el-button type="success" class="link-left" style="margin-left: 20px"
                     @click="addQuestion(titleItem)">选择题目
          </el-button>
          <el-popover placement="bottom" trigger="click" width="500px">
            <div class="paper-question-popover">
              <el-button type="success" size="small" v-for="item in EnumMap.exam.question.editUrlEnum"
                         :key="item.key" @click="questionAdd(item.value,titleItem)">{{ item.name }}
              </el-button>
            </div>
            <template #reference>
              <el-button type="success" class="link-left">添加题目</el-button>
            </template>
          </el-popover>
          <el-button type="danger" class="link-left" @click="removeTitleItem(index)">删除
          </el-button>
        </div>
        <el-card class="paper-question-show" v-if="titleItem.questionItems.length!==0">
          <draggable class="paper-question-contain" item-key="index" group="titleItemList" handle=".drag"
                     v-model="titleItem.questionItems">
            <template #item="{index,element }">
              <div class="paper-question-contain-item">
                <div class="title wdd-content-line-height">
                  题目{{ serialNumber(element.id) }}
                </div>
                <div class="content drag">
                  <QuestionShow :qType="element.questionType" :question="element"/>
                </div>
                <div class="action">
                  <el-input-number v-model="element.score" :precision="1" :step="1" :max="100"
                                   :min="0.1"
                                   @change="sumScoreFuc(true)"></el-input-number>
                  <el-button type="success" style="width: 100%;margin-top: 10px"
                             @click="editQuestion(element.questionType,element.id,index,titleItem)"
                  >编辑题目
                  </el-button>
                  <el-button type="danger" style="width: 100%;margin: 10px 0px 0px 0px"
                             @click="removeQuestion(titleItem,index)"
                  >删除
                  </el-button>
                </div>
              </div>
            </template>
          </draggable>
        </el-card>
      </el-form-item>
      <el-form-item label="试卷总分：" required>
        <span>{{ form.sumScore }}</span>
      </el-form-item>
      <el-form-item label="考试时长：" prop="suggestTime" required>
        <el-input-number v-model="form.suggestTime" placeholder="分钟" :step="1" :max="600" :min="1"
        ></el-input-number>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm"
                   v-has-perm="['train:exam:paper:create','train:exam:paper:update']">提交
        </el-button>
        <el-button @click="resetForm">重置</el-button>
        <el-button type="success" @click="addTitle">添加标题</el-button>
      </el-form-item>
    </el-form>

    <el-dialog v-model="questionPage.showDialog" width="90%" class="question-select-dialog">
      <el-form :model="questionPage.queryParam" ref="questionQueryForm" :inline="true">
        <el-form-item label="ID：">
          <el-input v-model="questionPage.queryParam.id" clearable></el-input>
        </el-form-item>
        <el-form-item label="题干：">
          <el-input v-model="questionPage.queryParam.title" clearable></el-input>
        </el-form-item>
        <el-form-item label="题型：">
          <el-select v-model="questionPage.queryParam.questionType" clearable>
            <el-option v-for="item in EnumMap.exam.question.typeEnum" :key="item.key" :value="item.key"
                       :label="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="题目分类：">
          <tree-select v-model="questionPage.queryParam.questionArchiveIdList" :data="questionArchiveTree"
                       :clearable="true"
                       placeholder="题目分类"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="questionQueryForm">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="questionPage.listLoading" ref="questionMultipleTable" :data="questionPage.tableData"
                @select="questionSelect" @select-all="questionSelectAll" @expand-change="expandChange" border fit
                highlight-current-row row-key="id" style="width: 100%">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column type="expand">
          <template #default="{row}">
            <QuestionShow :qType="row.question.questionType" :question="row.question"
                          class="table-question-show"/>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="Id" width="100px"/>
        <el-table-column prop="questionArchive" label="题目分类" width="100px"/>
        <el-table-column prop="typeEnumStr" label="题型" width="110px"/>
        <el-table-column label="题干">
          <template #default="{row}">
            <div v-html="row.title" class="wdd-question-content"></div>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="分数" width="60px"/>
        <el-table-column prop="difficultStr" label="难度" width="60px"/>
      </el-table>
      <pagination v-show="questionPage.total>0" :total="questionPage.total"
                  v-model:page="questionPage.queryParam.pageIndex"
                  v-model:limit="questionPage.queryParam.pageSize"
                  @pagination="questionSearch" :autoScroll="false"/>
    </el-dialog>
  </div>
</template>

<script>

import {EnumMap, Format} from '@/api/EnumMap'
import {numberConvert} from '@/utils'
import QuestionShow from '../../exam/build/components/Show.vue'
import {select, edit} from '@/api/trainExamPaper'
import {select as questionSelect, selectSync as questionSelectSync, page as questionPage} from '@/api/question'
import {tree as questionArchiveTree} from '@/api/questionArchive'
import {tree as paperArchiveTree} from '@/api/examPaperArchive'
import draggable from 'vuedraggable'
import useStore from '@/store'

const {tagsView} = useStore()

export default {
  name: 'TrainExamPaperEdit',
  components: {QuestionShow, draggable},
  data() {
    return {
      EnumMap: EnumMap,
      form: {
        id: null,
        examPaperArchiveId: null,
        name: '',
        cheat: false,
        maxCheatCount: null,
        questionMess: false,
        questionItemMess: false,
        suggestTime: null,
        titleItems: [],
        sumScore: 0
      },
      formLoading: false,
      rules: {
        name: [
          {required: true, message: '请输入试卷名称'}
        ],
        suggestTime: [
          {required: true, message: '请输入考试时长'},
          {type: 'number', message: '时长必须为数字值'}
        ]
      },
      questionPage: {
        showDialog: false,
        queryParam: {
          id: null,
          title: null,
          questionType: null,
          pageIndex: 1,
          pageSize: 10,
          questionArchiveIdList: null
        },
        listLoading: false,
        tableData: [],
        total: 0,
        expandKeys: []
      },
      currentTitleItem: null,
      questionArchiveTree: [],
      examPaperArchiveTree: []
    }
  },
  computed: {
    serialNumber() {
      return id => {
        let index = 0
        for (const title of this.form.titleItems) {
          for (const question of title.questionItems) {
            ++index
            if (question.id == id) {
              return index
            }
          }
        }
        return index
      }
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

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id).then(re => {
        this.form = re.response
        this.formNumberConvert()
        this.sumScoreFuc(false)
      }).finally(() => {
        this.formLoading = false
      })
    }
  },
  methods: {
    addTitle() {
      this.form.titleItems.push({
        name: '',
        questionItems: []
      })
    },
    removeTitleItem(index) {
      this.form.titleItems.splice(index, 1)
      this.sumScoreFuc(true)
    },
    addQuestion(titleItem) {
      this.currentTitleItem = titleItem
      this.questionPage.showDialog = true
      this.questionSearch()
    },
    async questionSelectArray(questionItem, action) {
      let index = null
      let titleArray = this.form.titleItems
      let indexTitle = null
      for (let i = 0; i < titleArray.length; i++) {
        let questionArray = titleArray[i].questionItems
        for (let j = 0; j < questionArray.length; j++) {
          if (questionItem.id === questionArray[j].id) {
            indexTitle = titleArray[i]
            index = j
            break
          }
        }
      }

      if (action === 1) { // revert
        if (index == null) {
          let re = await questionSelectSync(questionItem.id)
          re.response.score = numberConvert(re.response.score)
          this.currentTitleItem.questionItems.push(re.response)
        } else {
          indexTitle.questionItems.splice(index, 1)
        }
      } else if (action === 2) { // add
        if (index == null) {
          let re = await questionSelectSync(questionItem.id)
          re.response.score = numberConvert(re.response.score)
          this.currentTitleItem.questionItems.push(re.response)
        }
      } else if (action === 3) { // remove
        if (index != null) {
          indexTitle.questionItems.splice(index, 1)
        }
      }
    },
    async questionSelect(selection, row) {
      await this.questionSelectArray(row, 1)
      this.$message.success('操作成功')
      this.sumScoreFuc(true)
    },
    async questionSelectAll(selection) {
      if (selection.length === 0) {
        for (let i = 0; i < this.questionPage.tableData.length; i++) {
          await this.questionSelectArray(this.questionPage.tableData[i], 3)
        }
      } else {
        for (let i = 0; i < selection.length; i++) {
          await this.questionSelectArray(selection[i], 2)
        }
      }
      this.$message.success('操作成功')
      this.sumScoreFuc(true)
    },
    expandChange(row, expandedRows) {
      this.questionPage.expandKeys = expandedRows.map(item => item.id)
      questionSelect(row.id).then(re => {
        row.question = re.response
      })
    },
    questionInfoLoad() {
      for (const item of this.questionPage.tableData) {
        //题目展开
        let expandItem = this.questionPage.expandKeys.find(id => id == item.id)
        if (expandItem) {
          questionSelect(item.id).then(re => {
            item.question = re.response
          })
        }
        //题目勾选
        let titleArray = this.form.titleItems
        for (let i = 0; i < titleArray.length; i++) {
          let questionArray = titleArray[i].questionItems
          for (let j = 0; j < questionArray.length; j++) {
            if (item.id === questionArray[j].id) {
              this.$refs.questionMultipleTable.toggleRowSelection(item, true)
            }
          }
        }
      }
    },
    removeQuestion(titleItem, index) {
      titleItem.questionItems.splice(index, 1)
      this.sumScoreFuc(true)
    },
    questionAdd(url, titleItem) {
      window.open(`/#${url}`, '_blank')
      window.addSuccess = async (id) => {
        let re = await questionSelectSync(id)
        re.response.score = numberConvert(re.response.score)
        titleItem.questionItems.push(re.response)
        this.sumScoreFuc(true)
        this.$message.success('成功')
      }
    },
    editQuestion(questionType, questionId, index, titleItem) {
      let url = Format(EnumMap.exam.question.editUrlEnum, questionType)
      window.open(`/#${url}?id=${questionId}`, '_blank')
      window.editSuccess = async (id) => {
        let re = await questionSelectSync(id)
        re.response.score = numberConvert(re.response.score)
        titleItem.questionItems.splice(index, 1, re.response)
        this.sumScoreFuc(true)
        this.$message.success('成功')
      }
    },
    questionQueryForm() {
      this.questionPage.queryParam.pageIndex = 1
      this.questionSearch()
    },
    questionSearch() {
      this.questionPage.listLoading = true
      questionPage(this.questionPage.queryParam).then(data => {
        const re = data.response
        this.questionPage.tableData = re.list
        this.questionPage.total = re.total
        this.questionPage.queryParam.pageIndex = re.pageNum
        this.$nextTick(() => {
          this.questionInfoLoad()
        })
      }).finally(() => {
        this.questionPage.listLoading = false
      })
    },
    formNumberConvert() {
      this.form.titleItems.forEach(title => {
        title.questionItems.forEach(question => {
          question.score = numberConvert(question.score)
        })
      })
      this.form.passScore = numberConvert(this.form.passScore)
    },
    sumScoreFuc(resetPassScore) {
      let score = 0
      this.form.titleItems.forEach(ti => {
        ti.questionItems.forEach(q => {
          score += parseInt(parseFloat(q.score) * 10)
        })
      })
      this.form.sumScore = Number(score / 10).toFixed(1)
      if (resetPassScore) {
        this.form.passScore = null
      }
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          edit(this.form).then(re => {
            if (re.code === 1) {
              this.$message.success(re.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/train/exam/paper/list')
              })
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
        examPaperArchiveId: null,
        name: '',
        cheat: false,
        maxCheatCount: null,
        questionMess: false,
        questionItemMess: false,
        suggestTime: null,
        titleItems: [],
        sumScore: 0
      }
      this.$refs['form'].resetFields()
    }
  }
}
</script>
