<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="文章ID：" prop="forumId"
                    :rules="[{ type: 'number', message: '必须为数字值'}]">
        <el-input v-model.number="queryParam.forumId" clearable></el-input>
      </el-form-item>
      <el-form-item label="用户名：" prop="userName">
        <el-input v-model="queryParam.userName" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['forum:comment:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="90px"/>
      <el-table-column prop="content" label="评论">
        <template #default="{row}">
          <div v-html="row.content" class="wdd-question-content"></div>
        </template>
      </el-table-column>
      <el-table-column prop="createUserStr" label="创建人" width="150px"/>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
      <el-table-column width="90px" label="操作" align="center">
        <template #default="{row}">
          <el-button type="danger" @click="forumCommentDelete(row)" class="link-left"
                     v-has-perm="['forum:comment:delete']">删除
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

import {commentDelete, commentPage} from '@/api/forum'

export default {
  name: 'ForumCommentList',
  data() {
    return {
      queryParam: {
        forumId: null,
        userName:null,
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
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

      this.search()
    },
    search() {
      this.$refs['queryForm'].validate((valid) => {
        if (valid) {
          commentPage(this.queryParam).then(data => {
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
        forumId: null,
        userName:null,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    forumCommentDelete(row) {
      this.$confirm('此操作将永久删除该评论, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        commentDelete(row.id).then(re => {
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
