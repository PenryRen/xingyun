<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="用户Id：" prop="userId"
                    :rules="[{ type: 'number', message: '必须为数字值'}]">
        <el-input v-model.number="queryParam.userId" clearable></el-input>
      </el-form-item>
      <el-form-item label="用户名：">
        <el-input v-model="queryParam.userName" clearable></el-input>
      </el-form-item>
      <el-form-item label="班级：" class="tree-select-contain page-select-contain">
        <tree-select v-model="queryParam.departmentIdList" :data="departmentTree" :clearable="true"
                     placeholder="班级"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['user:log:event:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="100"/>
      <el-table-column prop="departmentLevel" label="班级" width="220"/>
      <el-table-column prop="userName" label="用户名" width="150"/>
      <el-table-column prop="content" label="动态"/>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
    </el-table>
    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search"/>
  </div>
</template>

<script>

import {eventPage} from '@/api/log'
import {tree} from '@/api/department'

export default {
  name: 'LogEventList',
  data() {
    return {
      queryParam: {
        userId: null,
        userName: null,
        departmentIdList: [],
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      departmentTree: [],
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
        this.departmentTree = re.response
      })

      this.search()
    },
    search() {
      this.$refs['queryForm'].validate((valid) => {
        if (valid) {
          eventPage(this.queryParam).then(data => {
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
        userId: null,
        userName: null,
        departmentIdList: [],
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    }
  }
}
</script>
