<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="paperQueryForm" :inline="true">
      <el-form-item label="试卷Id：" prop="id" :rules="[
        { required: true,  message: '请输入试卷Id' },
        { type: 'number', message: '必须为数字值' }]" required>
        <el-input v-model.number="queryParam.id" clearable></el-input>
      </el-form-item>
      <el-form-item label="证书Id：" prop="credentialId" :rules="[
        { type: 'number', message: '必须为数字值' }]">
        <el-input v-model.number="queryParam.credentialId" clearable></el-input>
      </el-form-item>
      <el-form-item label="用户名：" prop="userName">
        <el-input v-model="queryParam.userName" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="paperSubmitForm">查看</el-button>
      </el-form-item>
    </el-form>

    <div class="wdd-show-row">
      <el-card class="wdd-show-col">
        <p><span>试卷名称：</span><span>{{ paperInfoVM.name }}</span></p>
        <p><span>组卷类型：</span><span>{{ paperInfoVM.buildTypeStr }}</span></p>
        <p><span>试卷分类：</span><span>{{ paperInfoVM.examPaperArchiveStr }}</span></p>
        <p><span>考试时间：</span><span>{{ paperInfoVM.limitStartTime }} 至 {{ paperInfoVM.limitEndTime }}</span>
        </p>
        <p><span>考试时长：</span><span>{{ paperInfoVM.suggestTimeStr }}</span></p>
        <p><span>题目总数：</span><span>{{ paperInfoVM.questionCount }}</span></p>
      </el-card>
      <el-card class="wdd-show-col">
        <p><span>已生成证书数量：</span><span>{{ credentialInfo.buildCredential }}</span></p>
        <p><span>未生成证书数量：</span><span>{{ credentialInfo.waitCredential }}</span></p>
        <p><span>总证书数量：</span><span>{{ credentialInfo.allCredential }}</span></p>
        <p><span>试卷完成数：</span><span>{{ paperInfoVM.completeCount }}</span></p>
        <p><span>创建人：</span><span>{{ paperInfoVM.createUserStr }}</span></p>
        <p><span>创建时间：</span><span>{{ paperInfoVM.createTime }}</span></p>
      </el-card>
    </div>

    <div class="analyze-paper-top">
      <el-card header="用户证书">
        <el-image class="paper-credential" :src="item" :preview-src-list="tableData" :preview-teleported="true"
                  :hide-on-click-modal="true"
                  v-for="(item,index) in tableData" :initial-index="index" :key="index"></el-image>
        <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                    v-model:limit="queryParam.pageSize"
                    :autoScroll="false" @pagination="search"/>
      </el-card>
    </div>

  </div>
</template>

<script>
import {info, credentialInfo, credentialAnswerPage} from '@/api/examPaperAnswer'

export default {
  name: 'ExamPaperAnswerCredential',
  data() {
    return {
      paperInfoVM: {},
      credentialInfo: {},
      queryParam: {
        id: null,
        credentialId: null,
        userName: null,
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: false,
      tableData: [],
      total: 0
    }
  },
  created() {
    let id = this.$route.query.id
    if (id && parseInt(id) !== 0) {
      this.queryParam.id = parseInt(id)
      this.loadPaperInfo()
      this.search()
    }
  },
  methods: {
    paperSubmitForm() {
      this.$refs['paperQueryForm'].validate((valid) => {
        if (valid) {
          this.loadPaperInfo()
          this.search()
        }
      })
    },
    loadPaperInfo() {
      info(this.queryParam.id).then(re => {
        if (re.code === 1) {
          this.paperInfoVM = re.response
        } else {
          this.$message.error(re.message)
        }
      })

      credentialInfo(this.queryParam.id).then(re => {
        if (re.code === 1) {
          this.credentialInfo = re.response
        } else {
          this.$message.error(re.message)
        }
      })
    },
    search() {
      this.listLoading = true
      credentialAnswerPage(this.queryParam).then(data => {
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
