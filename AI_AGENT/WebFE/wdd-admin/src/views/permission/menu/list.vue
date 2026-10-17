<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="名称：">
        <el-input v-model="queryParam.name" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['menu:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <router-link :to="{path:'/permission/menu/edit'}" class="link-left">
          <el-button type="primary" v-has-perm="['menu:create']">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="100px"/>
      <el-table-column prop="menuName" label="菜单名称"/>
      <el-table-column prop="metaTitle" label="页面标题"/>
      <el-table-column prop="path" label="Url路径"/>
      <el-table-column prop="name" label="页面名称"/>
      <el-table-column prop="component" label="视图路径"/>
      <el-table-column prop="levelStr" label="层级" width="90px"/>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
      <el-table-column width="160px" label="操作" align="center">
        <template #default="{row}">
          <router-link :to="{path:'/permission/menu/edit', query:{id:row.id}}" class="link-left">
            <el-button v-has-perm="['menu:update']" size="small" class="wdd-button-mini">编辑</el-button>
          </router-link>
          <el-button type="danger" @click="menuDelete(row)" size="small" class="wdd-button-mini link-left"
                     v-has-perm="['menu:delete']">删除
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

import {page, deleteMenu} from '@/api/menu'

export default {
  name: 'MenuList',
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
    clearForm() {
      this.queryParam = {
        name: null,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    submitForm() {
      this.queryParam.pageIndex = 1
      this.search()
    },
    menuDelete(row) {
      this.$confirm('此操作将永久删除该菜单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMenu(row.id).then(re => {
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
