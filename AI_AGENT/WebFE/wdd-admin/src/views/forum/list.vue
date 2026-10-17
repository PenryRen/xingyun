<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="分类：">
        <tree-select v-model="queryParam.forumArchiveIdList" :data="forumArchiveTree" :clearable="true"
                     placeholder="文章分类"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['forum:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <router-link :to="{path:'/forum/edit'}" class="link-left">
          <el-button type="primary" v-has-perm="['forum:create']">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="90px"/>
      <el-table-column prop="forumArchiveStr" label="分类" width="150px"/>
      <el-table-column prop="title" label="标题" width="250px"/>
      <el-table-column prop="content" label="内容"/>
      <el-table-column prop="createUserStr" label="创建人" width="150px"/>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
      <el-table-column width="150px" label="操作" align="center">
        <template #default="{row}">
          <router-link :to="{path:'/forum/edit', query:{id:row.id}}">
            <el-button v-has-perm="['forum:update']" size="small" class="wdd-button-mini">编辑</el-button>
          </router-link>
          <el-button type="danger" @click="forumDelete(row)" size="small" class="wdd-button-mini link-left"
                     v-has-perm="['forum:delete']">删除
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

import {page, deleteForum} from '@/api/forum'
import {tree} from '@/api/forumArchive'

export default {
  name: 'ForumList',
  data() {
    return {
      queryParam: {
        forumArchiveIdList: null,
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      total: 0,
      canLoad: true,
      forumArchiveTree: []
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
        this.forumArchiveTree = re.response
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
        forumArchiveIdList: null,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    forumDelete(row) {
      this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteForum(row.id).then(re => {
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
