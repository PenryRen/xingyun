<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="名称：">
        <el-input v-model="queryParam.name" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['role:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <router-link :to="{path:'/permission/role/edit'}" class="link-left">
          <el-button type="primary" v-has-perm="['role:create']">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="100px"/>
      <el-table-column prop="name" label="名称" width="120px"/>
      <el-table-column prop="menu" label="菜单列表"/>
      <el-table-column prop="dataFilterStr" label="数据权限" width="160px"/>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
      <el-table-column width="160px" label="操作" align="center">
        <template #default="{row}">
          <router-link :to="{path:'/permission/role/edit', query:{id:row.id}}" class="link-left">
            <el-button v-has-perm="['role:update']" size="small" class="wdd-button-mini">编辑</el-button>
          </router-link>
          <el-button type="danger" @click="roleDelete(row)" size="small" class="wdd-button-mini link-left"
                     v-has-perm="['role:delete']">删除
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

import {page, deleteRole} from '@/api/role'

export default {
  name: 'RoleList',
  data() {
    return {
      queryParam: {
        name: null,
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      total: 0
    }
  },
  created() {
    this.init()
  },
  activated() {
    this.init()
  },
  methods: {
    init() {
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
    roleDelete(row) {
      this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRole(row.id).then(re => {
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
