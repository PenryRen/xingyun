<template>
  <div class="app-container">
    <el-form :model="applyQueryParam" ref="applyQueryForm" :inline="true">
      <el-form-item label="报名Id：" prop="id" :rules="[
        { required: true,  message: '请输入报名Id' },
        { type: 'number', message: '必须为数字值'}]" required>
        <el-input v-model.number="applyQueryParam.id" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="querySubmitForm">查看</el-button>
      </el-form-item>
    </el-form>

    <div class="wdd-show-row">
      <el-card class="wdd-show-col">
        <p><span>报名名称：</span><span>{{ applyInfoVM.name }}</span></p>
        <p><span>截止时间：</span><span>{{ applyInfoVM.applyEndTime }}</span></p>
        <p><span>考试时间：</span><span>{{ applyInfoVM.limitStartTime }} 至 {{ applyInfoVM.limitEndTime }}</span>
        </p>
        <p><span>创建人：</span><span>{{ applyInfoVM.createUser }}</span></p>
        <p><span>创建时间：</span><span>{{ applyInfoVM.createTime }}</span></p>
      </el-card>
      <el-card class="wdd-show-col">
        <p><span>报名分类：</span><span>{{ applyInfoVM.applyArchive }}</span></p>
        <p><span>报名状态：</span><span>{{ applyInfoVM.statusStr }}</span></p>
        <p><span>发布班级：</span><span>{{ applyInfoVM.departmentNameList }}</span></p>
        <p><span>报名人数：</span><span>{{ applyInfoVM.applyCount }}</span></p>
        <p><span>班级总人数：</span><span>{{ applyInfoVM.departmentUserCount }}</span></p>
      </el-card>
    </div>

    <div>
      <el-tabs type="border-card" class="apply-show-tab">
        <el-tab-pane label="已报名人员">
          <el-form :model="applyQueryParam" ref="queryForm" :inline="true" style="margin-top: 30px;">
            <el-form-item label="用户名：" prop="userName">
              <el-input v-model="applyQueryParam.userName" clearable></el-input>
            </el-form-item>
            <el-form-item label="班级：">
              <tree-select v-model="applyQueryParam.departmentIdList" :data="departmentTree" :clearable="true"
                           placeholder="班级"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="applySubmitForm">查询</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="listLoading" :data="applyTableData" border fit highlight-current-row style="width: 100%;">
            <el-table-column prop="userName" label="用户名"/>
            <el-table-column prop="realName" label="真实姓名"/>
            <el-table-column prop="jobTitle" label="职位"/>
            <el-table-column prop="workNo" label="工号"/>
            <el-table-column prop="id_card" label="身份证号"/>
            <el-table-column prop="phone" label="手机号"/>
            <el-table-column prop="departmentLevel" label="班级"/>
            <el-table-column prop="createTime" label="报名时间" width="160px"/>
            <el-table-column width="80px" label="操作" align="center">
              <template #default="{row}">
                <el-button type="danger" @click="applyUserRemove(row)" v-has-perm="['apply:user:remove']"
                           size="small" class="wdd-page-action-button">移除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="applyTotal>0" :total="applyTotal" v-model:page="applyQueryParam.pageIndex"
                      v-model:limit="applyQueryParam.pageSize"
                      @pagination="applySearch"/>
        </el-tab-pane>
        <el-tab-pane label="报名申请人员">
          <el-form :model="auditQueryParam" ref="queryForm" :inline="true" style="margin-top: 30px;">
            <el-form-item label="用户名：" prop="userName">
              <el-input v-model="auditQueryParam.userName" clearable></el-input>
            </el-form-item>
            <el-form-item label="班级：">
              <tree-select v-model="auditQueryParam.departmentIdList" :data="departmentTree" :clearable="true"
                           placeholder="班级"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="auditSubmitForm">查询</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="listLoading" :data="auditTableData" border fit highlight-current-row style="width: 100%;">
            <el-table-column prop="userName" label="用户名"/>
            <el-table-column prop="realName" label="真实姓名"/>
            <el-table-column prop="jobTitle" label="职位"/>
            <el-table-column prop="workNo" label="工号"/>
            <el-table-column prop="id_card" label="身份证号"/>
            <el-table-column prop="phone" label="手机号"/>
            <el-table-column prop="departmentLevel" label="班级"/>
            <el-table-column prop="createTime" label="报名申请时间" width="160px"/>
            <el-table-column width="80px" label="操作" align="center">
              <template #default="{row}">
                <el-button type="success" @click="auditConfirm(row)" v-has-perm="['apply:audit:confirm']" size="small" class="wdd-page-action-button">
                  确认
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="auditTotal>0" :total="auditTotal" v-model:page="auditQueryParam.pageIndex"
                      v-model:limit="auditQueryParam.pageSize"
                      @pagination="auditSearch"/>
        </el-tab-pane>
      </el-tabs>
    </div>

  </div>
</template>

<script>

import {removeUserApply, info, applyUserPage, auditUserPage, auditConfirm} from '@/api/apply'
import {tree} from '@/api/department'

export default {
  name: 'ApplyShow',
  data() {
    return {
      applyInfoVM: {},
      applyQueryParam: {
        id: null,
        userName: null,
        departmentIdList: null,
        pageIndex: 1,
        pageSize: 10
      },
      applyTableData: [],
      applyTotal: 0,
      auditQueryParam: {
        id: null,
        userName: null,
        departmentIdList: null,
        pageIndex: 1,
        pageSize: 10
      },
      auditTableData: [],
      auditTotal: 0,
      listLoading: false,
      departmentTree: []
    }
  },
  created() {
    let id = this.$route.query.id
    if (id && parseInt(id) !== 0) {
      this.applyQueryParam.id = parseInt(id)
      this.auditQueryParam.id = parseInt(id)
      this.loadApplyInfo()
    }
    tree().then(re => {
      this.departmentTree = re.response
    })
  },
  methods: {
    querySubmitForm() {
      this.$refs['applyQueryForm'].validate((valid) => {
        if (valid) {
          this.loadApplyInfo()
        }
      })
    },
    loadApplyInfo() {
      info(this.applyQueryParam.id).then(re => {
        if (re.code === 1) {
          this.applyInfoVM = re.response
          this.applySearch()
          this.auditSearch()
        } else {
          this.$message.error(re.message)
        }
      })
    },
    applySearch() {
      this.listLoading = true
      applyUserPage(this.applyQueryParam).then(data => {
        if (data.code === 1) {
          const re = data.response
          this.applyTableData = re.list
          this.applyTotal = re.applyTotal
          this.applyQueryParam.pageIndex = re.pageNum
        } else {
          this.$message.error(data.message)
        }
      }).finally(() => {
        this.listLoading = false
      })
    },
    applyUserRemove(row) {
      this.$confirm('此操作将取消该报名人员, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeUserApply(row.id).then(re => {
          if (re.code === 1) {
            this.loadApplyInfo()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    },
    auditConfirm(row) {
      auditConfirm(row.id).then(re => {
        if (re.code === 1) {
          this.loadApplyInfo()
          this.$message.success(re.message)
        } else {
          this.$message.error(re.message)
        }
      })
    },
    auditSearch() {
      this.listLoading = true
      auditUserPage(this.auditQueryParam).then(data => {
        if (data.code === 1) {
          const re = data.response
          this.auditTableData = re.list
          this.auditTotal = re.auditTotal
          this.auditQueryParam.pageIndex = re.pageNum
        } else {
          this.$message.error(data.message)
        }
      }).finally(() => {
        this.listLoading = false
      })
    },
    applySubmitForm() {
      this.applyQueryParam.pageIndex = 1
      this.applySearch()
    },
    auditSubmitForm() {
      this.auditQueryParam.pageIndex = 1
      this.auditSearch()
    }
  }
}
</script>
