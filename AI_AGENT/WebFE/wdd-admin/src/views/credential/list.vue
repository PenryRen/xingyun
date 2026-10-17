<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="名称：">
        <el-input v-model="queryParam.name" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['credential:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <router-link :to="{path:'/credential/edit'}" class="link-left">
          <el-button type="primary" v-has-perm="['credential:create']">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="90px"/>
      <el-table-column prop="templateImagePath" label="证书模板" width="90px">
        <template #default="{row}">
          <el-image style="width: 50px; height: 50px" :src="row.templateImagePath"
                    :preview-src-list="[row.templateImagePath]" :preview-teleported="true"
                    :hide-on-click-modal="true" fit="fill"/>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="证书模板名称"/>
      <el-table-column prop="company" label="发证机构"/>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
      <el-table-column width="150px" label="操作" align="center">
        <template #default="{row}">
          <router-link :to="{path:'/credential/edit', query:{id:row.id}}" class="link-left">
            <el-button size="small" class="wdd-button-mini" v-has-perm="['credential:update']">编辑
            </el-button>
          </router-link>
          <el-button size="small" class="wdd-button-mini link-left" type="danger"
                     @click="credentialDelete(row)"
                     v-has-perm="['credential:delete']">删除
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
import {page, deleteCredential} from '@/api/credential'

export default {
  name: 'CredentialList',
  data() {
    return {
      queryParam: {
        name: null,
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
      page(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
      })
    },
    clearForm() {
      this.queryParam = {
        title: null,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    submitForm() {
      this.queryParam.pageIndex = 1
      this.search()
    },
    credentialDelete(row) {
      this.$confirm('此操作将永久删除该证书模板, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCredential(row.id).then(re => {
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
