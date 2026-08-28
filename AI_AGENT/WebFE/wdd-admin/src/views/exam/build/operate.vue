<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
      <el-form-item label="试卷分类：">
        <el-tree-select v-model="form.examPaperArchiveId" :data="examPaperArchiveTree" check-strictly
                        :render-after-expand="true" default-expand-all placeholder="试卷分类" clearable/>
      </el-form-item>
      <el-form-item label="实训环境：" prop="vmType" required>
        <el-select v-model="form.vmType" @change="changeVmType">
          <el-option v-for="item in vmTypeTree" :key="item.guid"
                     :value="item.guid"
                     :label="item.vmName"></el-option>
        </el-select>
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
            {{ form.buildConfig.examPaperApplySelect.limitStartTime }}
            至
            {{ form.buildConfig.examPaperApplySelect.limitEndTime }} 】
          </el-tag>
          <el-button type="success" size="small" class="link-left wdd-button-mini" @click="applySelectClick">
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
          <el-tag size="small" v-for="user in form.buildConfig.examPaperUserSelectList" :key="user.id"
                  @close="removeUser(user)"
                  closable>{{ user.realName }} - {{ user.userName }}
          </el-tag>
          <el-button type="success" size="small" class="link-left wdd-button-mini" @click="userSelectClick">
            选择员工
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
       <!-- <el-tooltip content="选择题选项打乱">
          <el-checkbox v-model="form.buildConfig.questionItemMess">选项打乱</el-checkbox>
        </el-tooltip>-->
      </el-form-item>
      <el-form-item :key="index" :label="`标题${index+1}：`" v-for="(titleItem,index) in form.titleItems"
                    class="form-item-content-block">
        <div class="exam-paper-title">
          <el-input v-model="titleItem.name" :validate-event="false"/>
          <el-button type="success" class="link-left" style="margin-left: 20px"
                     @click="addQuestion(titleItem)" v-if="action !== 1">选择题目
          </el-button>
          <el-popover placement="bottom" trigger="click" v-if="action !== 1" width="80px">
            <div class="paper-question-popover">
              <el-button type="success" size="small" v-for="item in EnumMap.exam.question.vmEditUrlSingleEnum"
                         :key="item.key" @click="questionAdd(item.value,titleItem)">{{ item.name }}
              </el-button>
            </div>
            <template #reference>
              <el-button type="success" class="link-left">添加题目</el-button>
            </template>
          </el-popover>
          <el-button type="danger" class="link-left" @click="removeTitleItem(index)" v-if="action !== 1">删除
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
                             v-if="action !== 1">编辑题目
                  </el-button>
                  <el-button type="danger" style="width: 100%;margin: 10px 0px 0px 0px"
                             @click="removeQuestion(titleItem,index)"
                             v-if="action !== 1">删除
                  </el-button>
                </div>
              </div>
            </template>
          </draggable>
        </el-card>
      </el-form-item>
      <el-form-item label="合格分：" prop="passScore" required>
        <el-input-number v-model="form.passScore" :precision="1" :step="1" :max="maxPassScore" :min="0.1"
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
      <el-form-item v-if="action !== 1">
        <el-button type="primary" @click="submitForm"
                   v-has-perm="['exam:paper:build:operate:create','exam:paper:build:operate:update']">提交
        </el-button>
        <el-button @click="resetForm">重置</el-button>
        <el-button type="success" @click="addTitle">添加标题</el-button>
      </el-form-item>
    </el-form>

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

    <el-dialog v-model="questionPage.showDialog" width="90%" class="question-select-dialog">
      <el-form :model="questionPage.queryParam" ref="questionQueryForm" :inline="true">
        <el-form-item label="ID：">
          <el-input v-model="questionPage.queryParam.id" clearable></el-input>
        </el-form-item>
        <el-form-item label="题干：">
          <el-input v-model="questionPage.queryParam.title" clearable></el-input>
        </el-form-item>
        <el-form-item label="题目分类：">
          <tree-select v-model="questionPage.queryParam.questionArchiveIdList" :data="questionArchiveTree"
                       :clearable="true"
                       placeholder="题目分类"/>
        </el-form-item>
        <el-form-item label="题型：">
          <el-select v-model="questionPage.queryParam.questionType" disabled>
            <el-option v-for="item in EnumMap.exam.question.vmTypeSingleEnum" :key="item.key" :value="item.key"
                       :label="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="实训环境：" prop="vmType" required>
          <el-select v-model="questionPage.queryParam.vmType" disabled>
            <el-option v-for="item in vmTypeTree" :key="item.guid"
                       :value="item.guid"
                       :label="item.vmName"></el-option>
          </el-select>
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
import {numberConvert, disabledDate} from '@/utils'
import QuestionShow from './components/Show.vue'
import {select, edit} from '@/api/examPaperBuild'
import {select as questionSelect, selectSync as questionSelectSync, page as questionPage} from '@/api/question'
import {tree as questionArchiveTree} from '@/api/questionArchive'
import {tree as paperArchiveTree} from '@/api/examPaperArchive'
import {tree as departmentTree} from '@/api/department'
import {employeePage} from '@/api/user'
import {list as credentialList} from '@/api/credential'
import {completePage as applyCompletePage} from '@/api/apply'
import {tree as applyArchiveTree} from '@/api/applyArchive'
import draggable from 'vuedraggable'
import useStore from '@/store'
import {vmTypeList as vmTypeTree} from '@/api/vmWare'

const {tagsView} = useStore()

export default {
  name: 'ExamPaperBuildOperate',
  components: {QuestionShow, draggable},
  data() {
    return {
      EnumMap: EnumMap,
      disabledDate: disabledDate,
      form: {
        id: null,
        buildType: 4,
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
          examPaperApplySelect: null
        },
        sumScore: 0,
        vmType:null,
      },
      formLoading: false,
      rules: {
        vmType: [
          {required: true, message: '请选择实训环境'}
        ],
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
        ],
      },
      questionPage: {
        showDialog: false,
        queryParam: {
          id: null,
          title: null,
          questionType: 7,
          pageIndex: 1,
          pageSize: 10,
          questionArchiveIdList: null,
          vmType:null
        },
        listLoading: false,
        tableData: [],
        total: 0,
        expandKeys: []
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
          status: 1,
          pageIndex: 1,
          pageSize: 10
        },
        listLoading: false,
        tableData: [],
        total: 0
      },
      currentTitleItem: null,
      questionArchiveTree: [],
      examPaperArchiveTree: [],
      departmentTree: [],
      credentialList: [],
      applyArchiveTree: [],
      action: 0,
      vmTypeTree:[],
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
    },
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

    vmTypeTree().then(re => {
      this.vmTypeTree = re.response
    })

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id, this.action).then(re => {
        this.form = re.response
        this.formNumberConvert()
        this.sumScoreFuc(false)
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
      this.$refs.form.validateField('vmType',valid=>{
        if(valid) {
          this.currentTitleItem = titleItem
          this.questionPage.queryParam.questionType = 7;
          this.questionPage.queryParam.vmType = this.form.vmType;
          this.questionPage.showDialog = true
          this.questionSearch()
        }else {
          this.$message.error("请先选择实训环境");
        }
      });
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
    /**
     * 改变实训环境
     * libl 2024.04.01
     */
    changeVmType(){
      this.form.titleItems=[];
      this.form.sumScore = 0;
      this.form.passScore = null;
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
      let url = Format(EnumMap.exam.question.vmEditUrlEnum, questionType)
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
                this.$router.push('/exam/paper/build/list')
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
        buildType: 1,
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
          examPaperApplySelect: null
        },
        sumScore: 0
      }
      this.$refs['form'].resetFields()
    }
  }
}
</script>
