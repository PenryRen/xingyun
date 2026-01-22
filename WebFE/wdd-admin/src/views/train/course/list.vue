<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="分类：">
        <tree-select v-model="queryParam.trainArchiveIdList" :data="trainArchiveTree"
                     :clearable="true"
                     placeholder="课程分类"/>
      </el-form-item>
      <el-form-item label="名称：">
        <el-input v-model="queryParam.name" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['train:course:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <router-link :to="{path:'/train/course/edit'}" class="link-left">
          <el-button type="primary" v-has-perm="['train:course:create']">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="90px"/>
      <el-table-column prop="level" label="课程分类" width="120px"/>
      <el-table-column prop="name" label="课程名称"/>
      <el-table-column prop="studyTimeStr" label="课程时长" width="130px"/>
      <el-table-column prop="startTime" label="开始时间" width="160px"/>
      <el-table-column prop="endTime" label="结束时间" width="160px"/>
      <el-table-column width="210px" label="操作" align="center">
        <template #default="{row}">
          <router-link :to="{path:'/train/course/edit', query:{id:row.id}}" class="link-left">
            <el-button v-has-perm="['train:course:update']" class="wdd-button-mini" size="small">编辑
            </el-button>
          </router-link>
          <router-link :to="{path:'/train/course/detail', query:{id:row.id}}" class="link-left">
            <el-button v-has-perm="['train:course:detail:select']" class="wdd-button-mini" size="small">详情
            </el-button>
          </router-link>
          <el-button type="danger" @click="trainDelete(row)" class="link-left wdd-button-mini"
                     size="small"
                     v-has-perm="['train:course:delete']">删除
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

import {EnumMap} from '@/api/EnumMap'
import {page, deleteTrain} from '@/api/train'
import {tree} from '@/api/trainArchive'

export default {
  name: 'TrainCourseList',
  data() {
    return {
      EnumMap: EnumMap,
      queryParam: {
        name: null,
        trainArchiveIdList: [],
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      total: 0,
      trainArchiveTree: [],
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
        this.trainArchiveTree = re.response
      })

      this.search()
    },
    search() {
      page(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
      })
    },
    submitForm() {
      this.queryParam.pageIndex = 1
      this.search()
    },
    clearForm() {
      this.queryParam = {
        name: null,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    trainDelete(row) {
      this.$confirm('此操作将永久删除该课程, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTrain(row.id).then(re => {
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
