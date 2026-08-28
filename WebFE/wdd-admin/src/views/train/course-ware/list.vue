<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="类型：">
        <el-select v-model="queryParam.fileType" clearable>
          <el-option v-for="item in EnumMap.courseWare.fileType" :key="item.key" :value="item.key"
                     :label="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分类：">
        <tree-select v-model="queryParam.courseWareArchiveIdList" :data="courseWareArchiveTree"
                     :clearable="true"
                     placeholder="课件分类"/>
      </el-form-item>
      <el-form-item label="名称：">
        <el-input v-model="queryParam.name" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['train:course:ware:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <router-link :to="{path:'/train/course/ware/edit'}" class="link-left">
          <el-button type="primary" v-has-perm="['train:course:ware:create']">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="90px"/>
      <el-table-column prop="fileTypeStr" label="课件类型" width="90px"/>
      <el-table-column prop="level" label="课件分类" width="120px"/>
      <el-table-column prop="name" label="课件名称"/>
      <el-table-column prop="fileName" label="文件名称"/>
      <el-table-column prop="maxLengthStr" label="时长" width="120px"/>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
      <el-table-column width="150px" label="操作" align="center">
        <template #default="{row}">
          <router-link :to="{path:'/train/course/ware/edit', query:{id:row.id}}" class="link-left">
            <el-button v-has-perm="['train:course:ware:update']" class="wdd-button-mini" size="small">编辑
            </el-button>
          </router-link>
          <el-button type="danger" @click="courseWareDelete(row)" class="link-left wdd-button-mini"
                     size="small"
                     v-has-perm="['train:course:ware:delete']">删除
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
import {page, deleteCourseWare} from '@/api/courseWare'
import {tree} from '@/api/courseWareArchive'

export default {
  name: 'CourseWareList',
  data() {
    return {
      EnumMap: EnumMap,
      queryParam: {
        name: null,
        fileType: null,
        courseWareArchiveIdList: [],
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      total: 0,
      courseWareArchiveTree: [],
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
        this.courseWareArchiveTree = re.response
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
        fileType: null,
        courseWareArchiveIdList: [],
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    courseWareDelete(row) {
      this.$confirm('此操作将永久删除该课件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCourseWare(row.id).then(re => {
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
