<template>
  <div class="app-container">

    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="试卷Id：" prop="id">
        <el-input v-model="queryParam.id" clearable></el-input>
      </el-form-item>
      <el-form-item label="用户名：" prop="userName">
        <el-input v-model="queryParam.userName" clearable></el-input>
      </el-form-item>
      <el-form-item label="班级：">
        <tree-select v-model="queryParam.departmentIdList" :data="departmentTree" :clearable="true"
                     placeholder="班级"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['exam:paper:monitor:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="monitor-card">
      <el-card v-for="(item,index) in tableData" :key="index">
        <div class="monitor-avatar">
          <el-avatar :src="item.imagePath" class="user-image" :size="100">暂无头像</el-avatar>
        </div>
        <div class="monitor-text">学员名称：{{ item.realName }}（{{ item.userName }}）</div>
        <div class="monitor-text">试卷名称：{{ item.paperName }}</div>
        <div class="monitor-text">作弊次数：{{ item.cheatCount }}</div>
        <div class="monitor-text">正确比：{{ item.questionCorrect }} / {{ item.questionCount }}</div>
        <div class="monitor-text">得分比：{{ item.userScore }} / {{ item.paperScore }}</div>
        <div class="monitor-text">更新时间：{{ item.updateTime }}</div>
        <div class="monitor-foot">
          <router-link target="_blank" :to="{path:'/exam/paper/answer/monitor/read', query:{id:item.id}}"
                       class="link-left">
            <el-button v-has-perm="['exam:paper:monitor:read']" size="small" class="wdd-page-action-button">
              查看答卷
            </el-button>
          </router-link>
          <router-link target="_blank" :to="{path:'/exam/paper/answer/monitor/camera', query:{id:item.id}}"
                       class="link-left">
            <el-button v-has-perm="['exam:paper:monitor:camera']" v-show="item.capture" size="small"
                       class="wdd-page-action-button">查看抓拍
            </el-button>
          </router-link>
        </div>
      </el-card>
    </div>
    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search"/>
  </div>
</template>

<script>

import {tree} from '@/api/department'
import {monitorPage} from '@/api/examPaperAnswer'


export default {
  name: 'ExamPaperMonitorList',
  data() {
    return {
      queryParam: {
        id: null,
        userName: null,
        departmentIdList: [],
        pageIndex: 1,
        pageSize: 20
      },
      listLoading: false,
      tableData: [],
      departmentTree: [],
      total: 0,
      canLoad: true
    }
  },
  created() {

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
      let id = this.$route.query.id
      if (id) {
        this.queryParam.id = parseInt(id)
      }

      if (!this.canLoad) {
        return
      }

      tree().then(re => {
        this.departmentTree = re.response
      })

      this.search()
    },
    search() {
      this.listLoading = true
      monitorPage(this.queryParam).then(data => {
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
    },
    clearForm() {
      this.queryParam = {
        id: null,
        departmentIdList: [],
        pageIndex: 1,
        pageSize: 10
      }
      if (this.$route.query.id) {
        this.$router.push('/exam/paper/answer/monitor/list')
      }
      this.search()
    }
  }
}
</script>
