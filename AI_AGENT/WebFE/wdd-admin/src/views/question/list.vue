<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="题目ID：" prop="id"
                    :rules="[{ type: 'number', message: '必须为数字值' }]">
        <el-input v-model.number="queryParam.id" clearable></el-input>
      </el-form-item>
      <el-form-item label="题型：">
        <el-select v-model="queryParam.questionType" clearable>
          <el-option v-for="item in EnumMap.exam.question.vmTypeEnum" :key="item.key" :value="item.key"
                     :label="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分类：">
        <tree-select v-model="queryParam.questionArchiveIdList" :data="questionArchiveTree" :clearable="true"
                     placeholder="题目分类"/>
      </el-form-item>
      <el-form-item label="题干：" prop="title">
        <el-input v-model.trim="queryParam.title" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['question:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <el-popover placement="bottom" trigger="click" :width="240">
          <template #reference>
            <el-button type="primary" class="link-left">导入</el-button>
          </template>
          <div class="question-upload">
            <div>
              <a target="_blank" :href="`${VITE_APP_RESOURCE_URL}/template/file/题目导入模板.xlsx`"
                 style="float: left;margin-left: 5px;">
                <el-button type="success">Excel 模板</el-button>
              </a>
              <el-upload style="float: left;margin-left: 5px;" accept=".xlsx"
                         action="/api/question/upload/excel"
                         :show-file-list="false" :on-progress="uploadProgress"
                         :on-success="uploadFileSuccess" :on-error="uploadError">
                <el-button type="success" class="link-left" v-has-perm="['question:upload:excel']">Excel
                  导入
                </el-button>
              </el-upload>
            </div>
            <div class="word-upload">
              <a target="_blank" :href="`${VITE_APP_RESOURCE_URL}/template/file/题目导入模板.docx`"
                 style="float: left;margin-left: 5px;">
                <el-button type="success">Word 模板</el-button>
              </a>
              <el-upload style="float: left;margin-left: 5px;" accept=".docx"
                         action="/api/question/upload/word"
                         :show-file-list="false" :on-progress="uploadProgress"
                         :on-success="uploadFileSuccess" :on-error="uploadError">
                <el-button type="success" class="link-left" v-has-perm="['question:upload:word']">Word
                  导入
                </el-button>
              </el-upload>
            </div>
          </div>
        </el-popover>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row
              row-key="id" @expand-change="expandChange" style="width: 100%">
      <el-table-column type="expand">
        <template #default="{row}">
          <QuestionShow :qType="row.question.questionType" :question="row.question"
                        class="table-question-show"/>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="Id" width="100px"/>
      <el-table-column prop="questionArchive" label="分类" width="100px"/>
      <el-table-column prop="typeEnumStr" label="题型" width="110px"/>
      <el-table-column label="题干">
        <template #default="{row}">
          <div v-html="row.title" class="wdd-question-content"></div>
        </template>
      </el-table-column>
      <el-table-column prop="score" label="分数" width="60px"/>
      <el-table-column prop="difficultStr" label="难度" width="60px"/>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
      <el-table-column width="160px" label="操作" align="center">
        <template #default="{row}">
          <el-button @click="questionEdit(row)" class="wdd-button-mini" size="small"
                     v-has-perm="['question:update:singleChoice','question:update:multipleChoice','question:update:trueFalse','question:update:gapFilling','question:update:shortAnswer','question:update:trainOperate']">
            编辑
          </el-button>
          <el-button type="danger" @click="questionDelete(row)" class="wdd-button-mini link-left" size="small"
                     v-has-perm="['question:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search"/>
  </div>
</template>

<script>

import QuestionShow from './components/Show.vue'
import {EnumMap, Format} from '@/api/EnumMap'
import {select, page, deleteQuestion} from '@/api/question'
import {tree} from '@/api/questionArchive'

export default {
  name: 'QuestionList',
  components: {QuestionShow},
  data() {
    return {
      VITE_APP_RESOURCE_URL: import.meta.env.VITE_APP_RESOURCE_URL,
      EnumMap: EnumMap,
      queryParam: {
        id: null,
        title: null,
        questionType: null,
        questionArchiveIdList: null,
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      questionArchiveTree: [],
      total: 0,
      expandKeys: [],
      canLoad: true
    }
  },
  mounted() {
    this.init()
    this.canLoad = false
  },
  activated() {
    this.init()
  },
  deactivated() {
    this.canLoad = true
  },
  methods: {
    init() {
      if (!this.canLoad) {
        return
      }

      tree().then(re => {
        this.questionArchiveTree = re.response
      })

      this.search()
    },
    expandChange(row, expandedRows) {
      this.expandKeys = expandedRows.map(item => item.id)
      select(row.id).then(re => {
        row.question = re.response
      })
    },
    search() {
      this.$refs['queryForm'].validate((valid) => {
        if (valid) {
          page(this.queryParam).then(data => {
            const re = data.response
            this.tableData = re.list
            this.total = re.total
            this.queryParam.pageIndex = re.pageNum
            this.expandItemLoad()
          })
        }
      })
    },
    expandItemLoad() {
      for (const item of this.tableData) {
        let expandItem = this.expandKeys.find(id => id == item.id)
        if (expandItem) {
          select(item.id).then(re => {
            item.question = re.response
          })
        }
      }
    },
    submitForm() {
      this.queryParam.pageIndex = 1
      this.search()
    },
    clearForm() {
      this.queryParam = {
        id: null,
        title: null,
        questionType: null,
        questionArchiveIdList: null,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    questionEdit(row) {
      let url = Format(EnumMap.exam.question.vmEditUrlEnum, row.questionType)
      this.$router.push({path: url, query: {id: row.id}})
    },
    questionDelete(row) {
      this.$confirm('此操作将永久删除该题目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteQuestion(row.id).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
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
    uploadFileSuccess(re, file) {
      this.loading.close()
      if (re.code === 1) {
        this.search()
        window.location.href = re.response
      } else {
        this.$message.error(re.message)
      }
    },
    uploadError() {
      this.loading.close()
      this.$message.error('文件上传失败，请检查文件大小或文件格式')
    }
  }
}
</script>
