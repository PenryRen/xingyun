<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="名称：">
        <el-input v-model="queryParam.name" clearable></el-input>
      </el-form-item>
      <el-form-item label="分类：">
        <tree-select v-model="queryParam.examPaperArchiveIdList" :data="examPaperArchiveTree" :clearable="true"
                     placeholder="试卷分类" style="width: 100%"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['exam:paper:answer:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="100px"/>
      <el-table-column prop="name" label="名称"/>
      <el-table-column prop="examPaperArchive" label="分类"/>
      <el-table-column prop="buildTypeStr" label="组卷类型" width="100px"/>
      <el-table-column prop="judgeCount" label="待批改数" width="90px"/>
      <el-table-column prop="completeCount" label="完成人数" width="90px"/>
      <el-table-column prop="allCount" label="考试人数" width="90px"/>
      <el-table-column label="考试时间" width="160px">
        <template #default="{row}">
          <div>{{ row.limitStartTime }}</div>
          <div>{{ row.limitEndTime }}</div>
        </template>
      </el-table-column>
      <el-table-column width="160px" label="操作" align="center">
        <template #default="{row}">
          <router-link :to="{path:'/exam/paper/answer/detail', query:{id:row.id}}">
            <el-button v-has-perm="['exam:paper:answer:info']" size="small" class="wdd-page-action-button">
              详情
            </el-button>
          </router-link>
          <router-link :to="{path:'/exam/paper/answer/monitor/list', query:{id:row.id}}" class="link-left">
            <el-button v-has-perm="['exam:paper:monitor:page']" size="small" class="wdd-page-action-button">
              监考
            </el-button>
          </router-link>
          <router-link :to="{path:'/exam/paper/answer/credential', query:{id:row.id}}" class="link-left">
            <el-button v-show="row.credentialTemplateId !== null"
                       v-has-perm="['exam:paper:answer:credential']"
                       size="small" class="wdd-page-action-button">证书
            </el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search"/>
  </div>
</template>

<script>


import {pageList} from '@/api/examPaperAnswer'
import {tree} from '@/api/examPaperArchive'

export default {
  name: 'ExamPaperAnswerList',
  data() {
    return {
      queryParam: {
        name: null,
        examPaperArchiveIdList: [],
        status: 2,
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      examPaperArchiveTree: [],
      total: 0,
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
        this.examPaperArchiveTree = re.response
      })

      this.search()
    },
    search() {
      this.$refs['queryForm'].validate((valid) => {
        if (valid) {
          pageList(this.queryParam).then(data => {
            const re = data.response
            this.tableData = re.list
            this.total = re.total
            this.queryParam.pageIndex = re.pageNum
          })
        }
      })
    },
    submitForm() {
      this.queryParam.pageIndex = 1
      this.search()
    },
    clearForm() {
      this.queryParam = {
        name: null,
        examPaperArchiveIdList: [],
        status: 2,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    }
  }
}
</script>
