<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="名称：">
        <el-input v-model="queryParam.name" clearable></el-input>
      </el-form-item>
      <el-form-item label="分类：">
        <tree-select v-model="queryParam.examPaperArchiveIdList" :data="examPaperArchiveTree" :clearable="true"
                     placeholder="试卷分类"/>
      </el-form-item>
      <el-form-item label="状态：">
        <el-select v-model="queryParam.status" placeholder="状态" clearable>
          <el-option v-for="item in EnumMap.exam.build.statusEnum" :key="item.key" :value="item.key"
                     :label="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['exam:paper:build:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="100px"/>
      <el-table-column prop="name" label="名称"/>
      <el-table-column prop="examPaperArchive" label="分类"/>
      <el-table-column prop="buildTypeStr" label="组卷类型" width="90px"/>
      <el-table-column prop="score" label="总分" width="60px"/>
      <el-table-column prop="questionCount" label="题目数" width="70px"/>
      <el-table-column prop="suggestTimeStr" label="考试时长" width="110px"/>
      <el-table-column label="考试时间" width="160px">
        <template #default="{row}">
          <div>{{ row.limitStartTime }}</div>
          <div>{{ row.limitEndTime }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="buildStatusStr" label="状态" width="70px"/>
      <el-table-column width="210px" label="操作" align="center">
        <template #default="{row}">
          <el-button type="success" @click="examPaperBuildPublish(row)" v-show="row.buildStatus === 1"
                     v-has-perm="['exam:paper:build:publish']" size="small"
                     class="link-left wdd-page-action-button">发布
          </el-button>
          <el-button @click="examPaperBuildAction(row,0)" v-show="row.buildStatus === 1"
                     v-has-perm="['exam:paper:build:manual:update','exam:paper:build:extract:update','exam:paper:build:random:update']"
                     size="small" class="link-left wdd-page-action-button">
            编辑
          </el-button>
          <el-button @click="examPaperBuildAction(row,1)" v-show="row.buildStatus !== 1"
                     v-has-perm="['exam:paper:build:manual:update','exam:paper:build:extract:update','exam:paper:build:random:update']"
                     size="small" class="link-left wdd-page-action-button">
            查看
          </el-button>
          <el-button type="danger" @click="examPaperBuildRecall(row)" v-show="row.buildStatus === 2"
                     v-has-perm="['exam:paper:build:recall']" size="small"
                     class="link-left wdd-page-action-button">撤回
          </el-button>
          <el-button @click="examPaperBuildAction(row,2)"
                     v-has-perm="['exam:paper:build:copy']" size="small"
                     class="link-left wdd-page-action-button">复制
          </el-button>
          <el-button type="danger" @click="examPaperBuildDelete(row)"
                     size="small" class="link-left wdd-page-action-button"
                     v-has-perm="['exam:paper:build:delete']">删除
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

import {EnumMap, Format} from '@/api/EnumMap'
import {page, recall, publish, deleteExamPaperBuild} from '@/api/examPaperBuild'
import {tree} from '@/api/examPaperArchive'


export default {
  name: 'ExamPaperBuildList',
  data() {
    return {
      EnumMap: EnumMap,
      queryParam: {
        name: null,
        examPaperArchiveIdList: null,
        status: null,
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
          page(this.queryParam).then(data => {
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
        examPaperArchiveIdList: null,
        status: null,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    examPaperBuildRecall(row) {
      this.$confirm('此操作将撤回该试卷, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        recall(row.id).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    },
    examPaperBuildPublish(row) {
      publish(row.id).then(re => {
        if (re.code === 1) {
          this.search()
          this.$message.success(re.message)
        } else {
          this.$message.error(re.message)
        }
      })
    },
    examPaperBuildAction(row, action) {
      let url = Format(EnumMap.exam.build.editUrlEnum, row.buildType)
      this.$router.push({path: url, query: {id: row.id, action: action}})
    },
    examPaperBuildDelete(row) {
      this.$confirm('此操作将永久删除该试卷, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteExamPaperBuild(row.id).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    }
  }
}
</script>
