<template>
  <div class="app-container random-contain">

    <el-card header="试卷编辑：">
      <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
        <el-form-item label="试卷分类：" class="tree-select-contain">
          <el-tree-select v-model="form.examPaperArchiveId" :data="examPaperArchiveTree" check-strictly
                          :render-after-expand="true" default-expand-all placeholder="试卷分类" clearable/>
        </el-form-item>
        <el-form-item label="试卷名称：" prop="name" required>
          <el-input v-model="form.name"/>
        </el-form-item>

        <el-form-item label="考试范围：" prop="rangeType" required>
          <el-select v-model="form.rangeType" @change="rangeTypeChange" clearable>
            <el-option v-for="item in EnumMap.exam.build.rangeTypeEnum" :key="item.key" :value="item.key"
                       :label="item.value"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="考试报名：" class="tree-select-contain"
                      v-if="form.rangeType !== null && form.rangeType === 1">
          <div class="apply-user">
            <el-tag @close="removeApply()" v-if="null !== form.buildConfig.examPaperApplySelect" closable>
              {{ form.buildConfig.examPaperApplySelect.name }}【
              {{ form.buildConfig.examPaperApplySelect.limitStartTime }} 至
              {{ form.buildConfig.examPaperApplySelect.limitEndTime }} 】
            </el-tag>
            <el-button type="success" size="small" class="link-left wdd-button-mini"
                       @click="applySelectClick">
              选择报名
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="考试班级：" v-if="form.rangeType !== null && form.rangeType === 2">
          <tree-select v-model="form.buildConfig.departmentIdList" :data="departmentTree" :clearable="true"
                       placeholder="班级" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="考试员工：" class="tree-select-contain"
                      v-if="form.rangeType !== null && form.rangeType === 2">
          <div class="paper-user">
            <el-tag v-for="user in form.buildConfig.examPaperUserSelectList" :key="user.id"
                    @close="removeUser(user)"
                    closable>{{ user.realName }} - {{ user.userName }}
            </el-tag>
            <el-button type="success" size="small" class="link-left wdd-button-mini"
                       @click="userSelectClick">选择员工
            </el-button>
            <el-upload accept=".xlsx"
                       action="/api/exam/paper/build/employee/import"
                       :show-file-list="false" :on-progress="uploadProgress"
                       :on-success="uploadExcelSuccess" :on-error="uploadError" class="paper-user-upload">
              <el-button type="success" size="small" class="link-left wdd-button-mini">导入员工</el-button>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="考试时间：" v-if="form.rangeType !== null && form.rangeType === 2"
                      class="form-item-content-block">
          <el-date-picker v-model="form.limitDateTime" value-format="YYYY-MM-DD HH:mm:ss" type="datetimerange"
                          range-separator="至" start-placeholder="考试开始时间" end-placeholder="考试结束时间"
                          :disabled-date="disabledDate">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-tooltip content="是否允许考生查看答卷">
            <el-checkbox v-model="form.buildConfig.watch">查看试卷</el-checkbox>
          </el-tooltip>
          <el-tooltip content="离开试卷超过多次会自动提交试卷">
            <div class="exam-paper-cheat-div">
              <el-checkbox v-model="form.buildConfig.cheat">防作弊</el-checkbox>
              <el-input-number v-model="form.buildConfig.maxCheatCount" placeholder="防作弊次数" :step="1"
                               :max="99"
                               :min="1"
                               size="small" v-if="form.buildConfig.cheat" class="max-cheat-count"/>
            </div>
          </el-tooltip>
          <!--<el-tooltip content="考试前人脸识别">
            <el-checkbox v-model="form.buildConfig.faceCheck">人脸识别</el-checkbox>
          </el-tooltip>-->
          <el-tooltip content="每分钟抓拍一张">
            <el-checkbox v-model="form.buildConfig.capture">考试抓拍</el-checkbox>
          </el-tooltip>
          <el-tooltip content="试卷题目顺序打乱">
            <el-checkbox v-model="form.buildConfig.questionMess">题序打乱</el-checkbox>
          </el-tooltip>
          <el-tooltip content="选择题选项打乱">
            <el-checkbox v-model="form.buildConfig.questionItemMess">选项打乱</el-checkbox>
          </el-tooltip>
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
        <el-form-item label="合格证书：" prop="credentialTemplateId">
          <el-select v-model="form.credentialTemplateId" placeholder="无" clearable>
            <el-option v-for="item in credentialList" :key="item.value" :value="item.value"
                       :label="item.name"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card header="组题策略：" style="margin-top: 20px">
      <div class="paper-random-contain">
        <div class="paper-random-left">
          <el-form :model="randomAdd" ref="randomAddForm" label-width="110px" :rules="randomAddRules">
            <el-form-item label="题目类型：" prop="questionType" required>
              <el-select v-model="randomAdd.questionType" clearable>
                <el-option v-for="item in EnumMap.exam.question.typeEnum" :key="item.key"
                           :value="item.key"
                           :label="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="题目分类：" class="tree-select-contain" prop="questionArchiveId" required>
              <el-tree-select v-model="randomAdd.questionArchiveId" :data="questionArchiveTree"
                              check-strictly
                              :render-after-expand="true" default-expand-all placeholder="题目分类"/>
            </el-form-item>
            <el-form-item label="题目难度：" prop="difficult" required>
              <el-rate v-model="randomAdd.difficult" class="question-item-rate" show-text :max="3"
                       :texts="['简单','较难','困难']"></el-rate>
            </el-form-item>
            <el-form-item label="题目数量：" prop="number" required>
              <el-input-number v-model="randomAdd.number" :step="1" :max="100" :min="1"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="每题分数：" prop="score" required>
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

    <div class="random-save-button" v-if="action !== 1">
      <el-button type="primary" @click="randomSubmit" style="width:100px;"
                 v-has-perm="['exam:paper:build:random:create','exam:paper:build:random:update']">提交
      </el-button>
      <el-button @click="resetForm" style="width:100px;">重置</el-button>
    </div>

    <el-dialog v-model="applyPage.showDialog" width="90%">
      <el-form :model="applyPage.queryParam" ref="queryForm" :inline="true">
        <el-form-item label="名称：">
          <el-input v-model="applyPage.queryParam.name" clearable></el-input>
        </el-form-item>
        <el-form-item label="报名分类：">
          <tree-select v-model="applyPage.queryParam.applyArchiveIdList" :data="applyArchiveTree"
                       :clearable="true"
                       placeholder="报名分类"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="applyQueryForm">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="applyMultipleTable" v-loading="applyPage.listLoading" :data="applyPage.tableData"
                @current-change="applyHandleChange" border
                fit highlight-current-row style="width: 100%">
        <el-table-column prop="id" label="Id" width="90px"/>
        <el-table-column prop="applyArchive" label="报名分类" width="120px"/>
        <el-table-column prop="name" label="报名名称"/>
        <el-table-column prop="applyCount" label="报名人数" width="100px"/>
        <el-table-column prop="limitStartTime" label="考试开始时间" width="160px"/>
        <el-table-column prop="limitEndTime" label="考试结束时间" width="160px"/>
        <el-table-column prop="departmentNameList" label="发布班级"/>
      </el-table>
      <pagination v-show="applyPage.total>0" :total="applyPage.total"
                  v-model:page="applyPage.queryParam.pageIndex"
                  v-model:limit="applyPage.queryParam.pageSize"
                  @pagination="applySearch" :autoScroll="false"/>
    </el-dialog>

    <el-dialog v-model="userPage.showDialog" width="90%">
      <el-form :model="userPage.queryParam" ref="queryForm" :inline="true">
        <el-form-item label="用户名：">
          <el-input v-model="userPage.queryParam.userName" clearable></el-input>
        </el-form-item>
        <el-form-item label="真实姓名：">
          <el-input v-model="userPage.queryParam.realName" clearable></el-input>
        </el-form-item>
        <el-form-item label="班级：">
          <tree-select v-model="userPage.queryParam.departmentIdList" :data="departmentTree" :clearable="true"
                       placeholder="班级"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="userQueryForm">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="userPage.listLoading" ref="userMultipleTable" :data="userPage.tableData"
                @select="useSelect"
                @select-all="useSelectAll" border fit highlight-current-row style="width: 100%">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column prop="id" label="Id" width="90px"/>
        <el-table-column prop="userName" label="用户名"/>
        <el-table-column prop="realName" label="真实姓名"/>
        <el-table-column prop="departmentLevel" label="班级"/>
        <el-table-column prop="sexStr" label="性别" width="60px;"/>
        <el-table-column prop="phone" label="手机号"/>
        <el-table-column prop="email" label="邮箱"/>
        <el-table-column prop="createTime" label="创建时间" width="160px"/>
      </el-table>
      <pagination v-show="userPage.total>0" :total="userPage.total" v-model:page="userPage.queryParam.pageIndex"
                  v-model:limit="userPage.queryParam.pageSize"
                  @pagination="userSearch" :autoScroll="false"/>
    </el-dialog>
  </div>
</template>

<script>

import {EnumMap, Format} from '@/api/EnumMap'
import {numberConvert, disabledDate} from '@/utils'
import {
  Edit as IconEdit, Delete as IconDelete
} from '@element-plus/icons-vue'
import {select, edit, random, checkRandom} from '@/api/examPaperBuild'
import {tree as questionArchiveTree} from '@/api/questionArchive'
import {tree as paperArchiveTree} from '@/api/examPaperArchive'
import {tree as departmentTree} from '@/api/department'
import {employeePage} from '@/api/user'
import {list as credentialList} from '@/api/credential'
import {completePage as applyCompletePage} from '@/api/apply'
import {tree as applyArchiveTree} from '@/api/applyArchive'
import draggable from 'vuedraggable'
import useStore from '@/store'

const {tagsView} = useStore()

export default {
  name: 'ExamPaperBuildRandom',
  components: {IconEdit, IconDelete, draggable},
  data() {
    let checkRate = (rule, value, callback) => {
      if (1 <= value && value <= 3) {
        return callback()
      }
      return callback('请选择题目难度')
    }
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
        rangeType: null,
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
        rangeType: [
          {required: true, message: '请选择考试范围'}
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
      randomAddRules:{
        questionType: [
          {required: true, message: '请选择题目类型'}
        ],
        questionArchiveId: [
          {required: true, message: '请选择题目分类'}
        ],
        difficult: [
          {required: true, validator: checkRate}
        ],
        number: [
          {required: true, message: '请输入题目数量'}
        ],
        score: [
          {required: true, message: '请输入每题分数'}
        ],
      },
      applyPage: {
        showDialog: false,
        queryParam: {
          name: null,
          applyArchiveIdList: null,
          pageIndex: 1,
          pageSize: 10
        },
        listLoading: false,
        tableData: [],
        total: 0
      },
      userPage: {
        showDialog: false,
        queryParam: {
          userName: null,
          realName: null,
          departmentIdList: [],
          excludeDepartmentIdList: [],
          exclude:true,
          status: 1,
          pageIndex: 1,
          pageSize: 10
        },
        listLoading: false,
        tableData: [],
        total: 0
      },
      questionArchiveTree: [],
      examPaperArchiveTree: [],
      departmentTree: [],
      credentialList: [],
      applyArchiveTree: [],
      action: 0
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
    let action = this.$route.query.action
    if (action) {
      this.action = parseInt(action)
    }

    paperArchiveTree().then(re => {
      this.examPaperArchiveTree = re.response
    })

    questionArchiveTree().then(re => {
      this.questionArchiveTree = re.response
    })

    departmentTree().then(re => {
      this.departmentTree = re.response
    })

    credentialList().then(re => {
      this.credentialList = re.response
    })

    applyArchiveTree().then(re => {
      this.applyArchiveTree = re.response
    })

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id, this.action).then(re => {
        this.form = re.response
        this.formNumberConvert()
      }).finally(() => {
        this.formLoading = false
      })
    }
  },
  methods: {
    rangeTypeChange(value) {
      if (value === 1) {
        this.form.limitDate = null
        this.form.limitStartTime = null
        this.form.limitEndTime = null
        this.form.buildConfig.departmentIdList = []
        this.form.buildConfig.examPaperUserSelectList = []
      } else if (value === 2) {
        this.form.buildConfig.examPaperApplySelect = null
      }
    },
    randomSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          random(this.form).then(re => {
            if (re.code === 1) {
              this.$message.success(re.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/exam/paper/build/list')
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
    applySelectClick() {
      this.applyPage.showDialog = true
      this.applySearch()
    },
    applyHandleChange(item) {
      if (item != null) {
        let selectItem = this.form.buildConfig.examPaperApplySelect
        // eslint-disable-next-line no-empty
        if (selectItem != null && selectItem.id === item.id) {

        } else {
          this.form.buildConfig.examPaperApplySelect = {
            id: item.id,
            name: item.name,
            limitStartTime: item.limitStartTime,
            limitEndTime: item.limitEndTime
          }
          this.applyPage.showDialog = false
        }
      }
    },
    applyQueryForm() {
      this.applyPage.queryParam.pageIndex = 1
      this.applySearch()
    },
    removeApply() {
      this.form.buildConfig.examPaperApplySelect = null
    },
    applySearch() {
      this.applyPage.listLoading = true
      applyCompletePage(this.applyPage.queryParam).then(data => {
        const re = data.response
        this.applyPage.tableData = re.list
        this.applyPage.total = re.total
        this.applyPage.queryParam.pageIndex = re.pageNum
        this.$nextTick(() => {
          let selectItem = this.form.buildConfig.examPaperApplySelect
          this.applyPage.tableData.forEach(row => {
            if (selectItem != null && selectItem.id === row.id) {
              this.$refs.applyMultipleTable.setCurrentRow(row)
            }
          })
        })
      }).finally(() => {
        this.applyPage.listLoading = false
      })
    },
    userSelectClick() {
      this.userPage.showDialog = true
      this.userSearch()
    },
    useSelectArray(userItem, action) {
      let array = this.form.buildConfig.examPaperUserSelectList
      let index = null
      for (let i = 0; i < array.length; i++) {
        if (userItem.id === array[i].id) {
          index = i
        }
      }
      if (action === 1) { // revert
        if (index == null) {
          this.form.buildConfig.examPaperUserSelectList.push({
            id: userItem.id,
            userName: userItem.userName,
            realName: userItem.realName,
            departmentId: userItem.departmentId
          })
        } else {
          this.form.buildConfig.examPaperUserSelectList.splice(index, 1)
        }
      } else if (action === 2) { // add
        if (index == null) {
          this.form.buildConfig.examPaperUserSelectList.push({
            id: userItem.id,
            userName: userItem.userName,
            realName: userItem.realName,
            departmentId: userItem.departmentId
          })
        }
      } else if (action === 3) { // remove
        if (index != null) {
          this.form.buildConfig.examPaperUserSelectList.splice(index, 1)
        }
      }
    },
    useSelect(selection, row) {
      this.useSelectArray(row, 1)
      this.$message.success('操作成功')
    },
    useSelectAll(selection) {
      if (selection.length === 0) {
        this.userPage.tableData.forEach(item => {
          this.useSelectArray(item, 3)
        })
      } else {
        selection.forEach(item => {
          this.useSelectArray(item, 2)
        })
      }
      this.$message.success('操作成功')
    },
    userQueryForm() {
      this.userPage.queryParam.pageIndex = 1
      this.userSearch()
    },
    removeUser(user) {
      this.form.buildConfig.examPaperUserSelectList.splice(this.form.buildConfig.examPaperUserSelectList.indexOf(user), 1)
    },
    userSearch() {
      this.userPage.queryParam.excludeDepartmentIdList = this.form.buildConfig.departmentIdList
      this.userPage.listLoading = true
      employeePage(this.userPage.queryParam).then(data => {
        const re = data.response
        this.userPage.tableData = re.list
        this.userPage.total = re.total
        this.userPage.queryParam.pageIndex = re.pageNum
        this.$nextTick(() => {
          let userSelectList = this.form.buildConfig.examPaperUserSelectList
          this.userPage.tableData.forEach(row => {
            for (let i = 0; i < userSelectList.length; i++) {
              if (userSelectList[i].id === row.id) {
                this.$refs.userMultipleTable.toggleRowSelection(row, true)
              }
            }
          })
        })
      }).finally(() => {
        this.userPage.listLoading = false
      })
    },
    uploadProgress() {
      this.loading = this.$loading({
        lock: true,
        text: '文件上传中…',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
    },
    uploadExcelSuccess(re, file) {
      this.loading.close()
      if (re.code === 1) {
        for (const userItem of re.response.userList) {
          this.form.buildConfig.examPaperUserSelectList.push({
            id: userItem.id,
            userName: userItem.userName,
            realName: userItem.realName,
            departmentId: userItem.departmentId
          })
        }
        window.location.href = re.response.filePath
      } else {
        this.$message.error(re.message)
      }
    },
    uploadError() {
      this.loading.close()
      this.$message.error('文件上传失败，请检查文件大小或文件格式')
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
          this.$message.error('已存在相同组题策略')
          return
        }
      }
      this.$refs["randomAddForm"].validate((valid) => {
        if (valid) {
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
        }
      });
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
        rangeType: null,
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
