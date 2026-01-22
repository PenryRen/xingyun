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
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['practice:build:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <el-popover placement="bottom" trigger="click" :width="330">
          <template #reference>
            <el-button type="primary" v-has-perm="['practice:build:create']">添加</el-button>
          </template>
          <div class="practice-popover-content">
            <el-button type="success" v-for="item in EnumMap.practice.editUrlEnum" :key="item.key" @click="createClick(item.key)">
              {{ item.name }}
            </el-button>
          </div>
        </el-popover>

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
      <el-table-column width="200px" label="操作" align="center">
        <template #default="{row}">
          <el-button @click="editClick(row)" class="link-left wdd-page-action-button" size="small"
                     v-has-perm="['practice:build:update']">
            编辑
          </el-button>
          <el-button @click="copyClick(row)" class="link-left wdd-page-action-button" size="small"
                     v-has-perm="['exam:paper:build:manual:update','exam:paper:build:extract:update','exam:paper:build:random:update']">
            转考
          </el-button>
          <router-link :to="{path:'/practice/answer/detail', query:{id:row.id}}">
            <el-button v-has-perm="['practice:answer:detail']" size="small" class="link-left wdd-page-action-button">
              详情
            </el-button>
          </router-link>
          <el-button type="danger" @click="practiceBuildDelete(row)"
                     size="small" class="link-left wdd-page-action-button"
                     v-has-perm="['practice:build:delete']">删除
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
import {page, deletePracticeBuild} from '@/api/practice'
import {tree} from '@/api/examPaperArchive'


export default {
  name: 'PracticeBuildList',
  data() {
    return {
      EnumMap: EnumMap,
      queryParam: {
        name: null,
        examPaperArchiveIdList: null,
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
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    createClick(type) {
      let url = Format(EnumMap.practice.editUrlEnum, type)
      this.$router.push({path: url})
    },
    editClick(row) {
      let url = Format(EnumMap.practice.editUrlEnum, row.buildType)
      this.$router.push({path: url, query: {id: row.id}})
    },
    copyClick(row) {
      let url = Format(EnumMap.exam.build.editUrlEnum, row.buildType)
      this.$router.push({path: url, query: {id: row.id, action: 3}})
    },
    practiceBuildDelete(row) {
      this.$confirm('此操作将永久删除该模拟练习, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletePracticeBuild(row.id).then(re => {
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
