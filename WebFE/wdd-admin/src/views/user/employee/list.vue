<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="用户名：">
        <el-input v-model="queryParam.userName" clearable></el-input>
      </el-form-item>
      <el-form-item label="真实姓名：">
        <el-input v-model="queryParam.realName" clearable></el-input>
      </el-form-item>
      <el-form-item label="班级：">
        <tree-select v-model="queryParam.departmentIdList" :data="departmentTree" :clearable="true"
                     placeholder="班级"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['user:employee:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <el-popover placement="bottom" trigger="click" :width="240">
          <template #reference>
            <el-button type="primary" class="link-left" v-has-perm="['user:employee:import']">导入
            </el-button>
          </template>
          <div class="wdd-popover-content">
            <a target="_blank" :href="`${VITE_APP_RESOURCE_URL}/template/file/员工导入模板.xlsx`"
               style="float: left;margin-left: 5px;">
              <el-button type="success">Excel 模板</el-button>
            </a>
            <el-upload style="float: left;margin-left: 5px;" accept=".xlsx"
                       action="/api/user/employee/import"
                       :show-file-list="false" :on-progress="uploadProgress"
                       :on-success="uploadExcelSuccess" :on-error="uploadError">
              <el-button type="success" class="link-left">Excel 导入</el-button>
            </el-upload>
          </div>
        </el-popover>
        <el-button type="primary" class="link-left" @click="userExport" v-has-perm="['user:employee:export']">导出
        </el-button>
        <router-link :to="{path:'/user/employee/edit'}" class="link-left">
          <el-button type="primary" v-has-perm="['user:employee:create']">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column width="60px" align="center">
        <template #default="{row}">
          <el-avatar :size="35" :src="row.imagePath" class="user-image-avatar"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="Id" width="90px"/>
      <el-table-column prop="userName" label="用户名"/>
      <el-table-column prop="realName" label="真实姓名"/>
      <el-table-column prop="departmentLevel" label="班级"/>
      <el-table-column prop="jobTitle" label="职位"/>
      <el-table-column prop="workNo" label="工号"/>
      <el-table-column prop="idCard" label="身份证号"/>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
      <el-table-column prop="statusStr" label="状态" width="70px"/>
      <el-table-column width="160px" label="操作" align="center">
        <template #default="{ row }">
          <router-link :to="{path:'/user/employee/edit', query:{id:row.id}}" class="link-left">
            <el-button v-has-perm="['user:employee:update']" size="small" class="wdd-button-mini">编辑
            </el-button>
          </router-link>
          <el-button type="danger" @click="userDelete(row)" size="small" class="wdd-button-mini link-left"
                     v-has-perm="['user:employee:delete']">删除
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

import {employeePage, employeeDelete, employeeExport} from '@/api/user'
import {tree} from '@/api/department'

export default {
  name: 'EmployeeList',
  data() {
    return {
      VITE_APP_RESOURCE_URL: import.meta.env.VITE_APP_RESOURCE_URL,
      queryParam: {
        userName: null,
        realName: null,
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
      employeePage(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
      })
    },
    userDelete(row) {
      this.$confirm('此操作将永久删除该员工, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        employeeDelete(row.id).then(re => {
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
        departmentIdList: [],
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    userExport() {
      employeeExport(this.queryParam).then(data => {
        if (data.code === 1) {
          window.open(data.response, '_blank')
        } else {
          this.$message.error(data.message)
        }
      })
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
    },
    uploadError() {
      this.loading.close()
      this.$message.error('文件上传失败，请检查文件大小或文件格式')
    }
  }
}
</script>
