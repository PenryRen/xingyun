<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="用户名：">
        <el-input v-model="queryParam.userName" clearable></el-input>
      </el-form-item>
      <el-form-item label="真实姓名：">
        <el-input v-model="queryParam.realName" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['user:admin:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <router-link :to="{path:'/user/admin/edit'}" class="link-left">
          <el-button type="primary" v-has-perm="['user:admin:create']">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="90px"/>
      <el-table-column prop="userName" label="用户名"/>
      <el-table-column prop="realName" label="真实姓名"/>
      <el-table-column prop="roleStr" label="角色"/>
      <el-table-column prop="departmentLevel" label="班级"/>
      <el-table-column prop="sexStr" label="性别" width="60px;"/>
      <el-table-column prop="phone" label="手机号"/>
      <el-table-column prop="email" label="邮箱"/>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
      <el-table-column prop="statusStr" label="状态" width="70px"/>
      <el-table-column width="160px" label="操作" align="center">
        <template #default="{ row }">
          <router-link :to="{path:'/user/admin/edit', query:{id:row.id}}" class="link-left">
            <el-button v-has-perm="['user:admin:update']" size="small" class="wdd-button-mini">编辑
            </el-button>
          </router-link>
          <el-button type="danger" @click="userDelete(row)" size="small" class="wdd-button-mini link-left"
                     v-has-perm="['user:admin:delete']">删除
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

import {adminPage, adminDelete} from '@/api/user'

export default {
  name: 'AdminList',
  data() {
    return {
      queryParam: {
        userName: null,
        realName: null,
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
      adminPage(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
      })
    },
    userDelete(row) {
      this.$confirm('此操作将永久删除该管理员, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        adminDelete(row.id).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    },
    submitForm() {
      this.queryParam.pageIndex = 1
      this.search()
    },
    clearForm() {
      this.queryParam = {
        userName: null,
        realName: null,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    uploadProgress() {
      this.loading = this.$loading({
        lock: true,
        text: '文件上传中…',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
    },
    uploadExcelSuccess(re, file) {
      this.loading.close()
      if (re.code === 1) {
        this.search()
        window.location.href = re.response
      } else {
        this.$message.error(re.message)
      }
    }
  }
}
</script>
