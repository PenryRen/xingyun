<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="announcementQueryForm" :inline="true">
      <el-form-item label="公告Id：" prop="id" :rules="[
        { required: true,  message: '请输入公告Id' },
        { type: 'number', message: '必须为数字值'}]" required>
        <el-input v-model.number="queryParam.id" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="announcementSubmitForm">查看</el-button>
      </el-form-item>
    </el-form>
    <div class="wdd-show-row">
      <el-card class="wdd-show-col">
        <p><span>公告标题：</span><span>{{ announcementInfoVM.title }}</span></p>
        <p><span>是否顶置：</span><span>{{ announcementInfoVM.overheadStr }}</span></p>
        <p><span>是否重要：</span><span>{{ announcementInfoVM.importantedStr }}</span></p>
        <p><span>创建人：</span><span>{{ announcementInfoVM.createUser }}</span></p>
      </el-card>
      <el-card class="wdd-show-col">
        <p><span>创建时间：</span><span>{{ announcementInfoVM.createTime }}</span></p>
        <p><span>公告分类：</span><span>{{ announcementInfoVM.announcementArchive }}</span></p>
        <p><span>发布班级：</span><span>{{ announcementInfoVM.departmentNameList }}</span></p>
        <p><span>班级总人数：</span><span>{{ announcementInfoVM.departmentUserCount }}</span></p>
      </el-card>
    </div>
    <el-form :model="queryParam" ref="queryForm" :inline="true" style="margin-top: 30px;">
      <el-form-item label="用户名：" prop="userName">
        <el-input v-model="queryParam.userName" clearable></el-input>
      </el-form-item>
      <el-form-item label="班级：">
        <tree-select v-model="queryParam.departmentIdList" :data="departmentTree" :clearable="true"
                     placeholder="班级"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="listLoading" :data="tableData" border fit highlight-current-row style="width: 100%;">
      <el-table-column prop="userName" label="用户名"/>
      <el-table-column prop="realName" label="真实姓名"/>
      <el-table-column prop="jobTitle" label="职位"/>
      <el-table-column prop="workNo" label="工号"/>
      <el-table-column prop="id_card" label="身份证号"/>
      <el-table-column prop="phone" label="手机号"/>
      <el-table-column prop="departmentLevel" label="班级"/>
      <el-table-column prop="createTime" label="查看时间" width="160px"/>
    </el-table>
    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search"/>
  </div>
</template>

<script>

import {info, userPage} from '@/api/announcement'
import {tree} from '@/api/department'

export default {
  name: 'AnnouncementShow',
  data() {
    return {
      announcementInfoVM: {},
      queryParam: {
        id: null,
        userName: null,
        departmentIdList: null,
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: false,
      tableData: [],
      departmentTree: [],
      total: 0
    }
  },
  created() {
    let id = this.$route.query.id
    if (id && parseInt(id) !== 0) {
      this.queryParam.id = parseInt(id)
      this.loadAnnouncementInfo()
    }

    tree().then(re => {
      this.departmentTree = re.response
    })
  },
  methods: {
    announcementSubmitForm() {
      this.$refs.announcementQueryForm.validate((valid) => {
        if (valid) {
          this.loadAnnouncementInfo()
        }
      })
    },
    loadAnnouncementInfo() {
      info(this.queryParam.id).then(re => {
        if (re.code === 1) {
          this.announcementInfoVM = re.response
          this.search()
        } else {
          this.$message.error(re.message)
        }
      })
    },
    search() {
      this.listLoading = true
      userPage(this.queryParam).then(data => {
        if (data.code === 1) {
          const re = data.response
          this.tableData = re.list
          this.total = re.total
          this.queryParam.pageIndex = re.pageNum
        } else {
          this.$message.error(data.message)
        }
      }).finally(() => {
        this.listLoading = false
      })
    },
    submitForm() {
      this.queryParam.pageIndex = 1
      this.search()
    }
  }
}
</script>
