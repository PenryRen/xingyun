<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="证书编号：">
        <el-input v-model="queryParam.no" clearable></el-input>
      </el-form-item>
      <el-form-item label="用户名：" prop="userName">
        <el-input v-model="queryParam.userName" clearable></el-input>
      </el-form-item>
      <el-form-item label="班级：" class="tree-select-contain page-select-contain">
        <tree-select v-model="queryParam.departmentIdList" :data="departmentTree" :clearable="true"
                     placeholder="班级"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['credential:user:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%" v-loading="formLoading">
      <el-table-column prop="id" label="Id" width="90px"/>
      <el-table-column prop="templateImagePath" label="证书" width="80px">
        <template #default="{row}">
          <el-image style="width: 50px; height: 50px" :src="row.credentialImagePath"
                    :preview-src-list="[row.credentialImagePath]" :hide-on-click-modal="true"
                    :preview-teleported="true" fit="fill"/>
        </template>
      </el-table-column>
      <el-table-column prop="no" label="证书编号"/>
      <el-table-column prop="userName" label="用户名"/>
      <el-table-column prop="realName" label="真实姓名"/>
      <el-table-column prop="workNo" label="工号"/>
      <el-table-column prop="departmentName" label="班级"/>
      <el-table-column prop="credentialName" label="证书名称"/>
      <el-table-column prop="company" label="发证机构" width="200px"/>
      <el-table-column prop="createTime" label="发证时间" width="160px"/>
    </el-table>
    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search"/>
  </div>
</template>

<script>

import {userPage} from '@/api/credential'
import {tree} from '@/api/department'

export default {
  name: 'CredentialShow',
  data() {
    return {
      queryParam: {
        no: null,
        userName:null,
        departmentIdList: [],
        pageIndex: 1,
        pageSize: 10
      },
      formLoading: false,
      tableData: [],
      total: 0,
      departmentTree: [],
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
      this.formLoading = true
      userPage(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
      }).finally(() => {
        this.formLoading = false
      })
    },
    clearForm() {
      this.queryParam = {
        no: null,
        departmentIdList: [],
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    submitForm() {
      this.queryParam.pageIndex = 1
      this.search()
    }
  }
}
</script>
