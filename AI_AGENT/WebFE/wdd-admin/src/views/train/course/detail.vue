<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="trainQueryForm" :inline="true">
      <el-form-item label="课程Id：" prop="id" :rules="[
        { required: true,  message: '请输入课程Id' },
        { type: 'number', message: '必须为数字值' }]" required>
        <el-input v-model.number="queryParam.id" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="trainSubmitForm">查看</el-button>
      </el-form-item>
    </el-form>

    <div class="wdd-show-row train-detail-card">
      <el-card class="wdd-show-col" header="培训信息">
        <p><span>课程名称：</span><span>{{ trainInfoVM.name }}</span></p>
        <p><span>学习时长：</span><span>{{ trainInfoVM.studyTimeStr }}</span></p>
        <p><span>开始时间：</span><span>{{ trainInfoVM.startTime }}</span></p>
        <p><span>结束时间：</span><span>{{ trainInfoVM.endTime }}</span></p>
        <p><span>创建人：</span><span>{{ trainInfoVM.createUserName }}</span></p>
      </el-card>
      <el-card class="wdd-show-col" header="课程信息">
        <p><span>未开始数：</span><span>{{ trainInfoVM.noStarCount }}</span></p>
        <p><span>进行中数：</span><span>{{ trainInfoVM.goingCount }}</span></p>
        <p><span>不合格数：</span><span>{{ trainInfoVM.noPassCount }}</span></p>
        <p><span>合格人数：</span><span>{{ trainInfoVM.passCount }}</span></p>
        <p><span>总人数：</span><span>{{ trainInfoVM.allUserCount }}</span></p>
      </el-card>
      <el-card class="wdd-show-col" header="试卷信息">
        <p><span>进行中数：</span><span>{{ trainInfoVM.paperGoing }}</span></p>
        <p><span>不合格数：</span><span>{{ trainInfoVM.paperNoPass }}</span></p>
        <p><span>合格数：</span><span>{{ trainInfoVM.paperPass }}</span></p>
        <p><span>待批改数：</span><span>{{ trainInfoVM.judgeCount }}</span></p>
        <p><span>答卷总数：</span><span>{{ trainInfoVM.paperCount }}</span></p>
      </el-card>
    </div>

    <div>
      <el-tabs type="border-card" class="train-detail-tab">
        <el-tab-pane label="培训">
          <el-form :model="train.queryParam" ref="trainQueryParam" :inline="true">
            <el-form-item label="状态：">
              <el-select v-model="train.queryParam.status" placeholder="状态" clearable>
                <el-option v-for="item in EnumMap.train.statusEnum" :key="item.key" :value="item.key"
                           :label="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="用户名：" prop="userName">
              <el-input v-model="train.queryParam.userName" clearable></el-input>
            </el-form-item>
            <el-form-item label="班级：">
              <tree-select v-model="train.queryParam.departmentIdList" :data="departmentTree" :clearable="true"
                           placeholder="班级" style="width: 100%"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="trainPage" v-has-perm="['train:course:detail:select']">查询</el-button>
              <el-button type="success" @click="trainExport" v-has-perm="['train:course:detail:export']">导出
              </el-button>
            </el-form-item>
          </el-form>
          <el-table :data="train.tableData" border fit highlight-current-row style="width: 100%;">
            <el-table-column prop="id" label="Id" width="100px"/>
            <el-table-column prop="userName" label="用户名"/>
            <el-table-column prop="realName" label="真实姓名"/>
            <el-table-column prop="workNo" label="工号"/>
            <el-table-column prop="departmentLevel" label="班级"/>
            <el-table-column prop="statusStr" label="状态" width="70px"/>
            <el-table-column prop="createTimeStr" label="开始时间" width="160px"/>
            <el-table-column prop="completeTimeStr" label="完成时间" width="160px"/>
          </el-table>
          <pagination v-show="train.total>0" :total="train.total" v-model:page="train.queryParam.pageIndex"
                      v-model:limit="train.queryParam.pageSize"
                      @pagination="trainSearch" :autoScroll="false"/>
        </el-tab-pane>
        <el-tab-pane label="课件">
          <el-form :model="courseWare.queryParam" ref="courseWareQueryParam" :inline="true">
            <el-form-item label="状态：">
              <el-select v-model="courseWare.queryParam.status" placeholder="状态" clearable>
                <el-option v-for="item in EnumMap.train.statusEnum" :key="item.key" :value="item.key"
                           :label="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="用户名：" prop="userName">
              <el-input v-model="courseWare.queryParam.userName" clearable></el-input>
            </el-form-item>
            <el-form-item label="班级：">
              <tree-select v-model="courseWare.queryParam.departmentIdList" :data="departmentTree" :clearable="true"
                           placeholder="班级" style="width: 100%"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="courseWarePage" v-has-perm="['train:course:detail:select']">查询
              </el-button>
            </el-form-item>
          </el-form>
          <el-table :data="courseWare.tableData" border fit highlight-current-row style="width: 100%;">
            <el-table-column prop="id" label="Id" width="100px"/>
            <el-table-column prop="name" label="课件名称"/>
            <el-table-column prop="userName" label="用户名"/>
            <el-table-column prop="realName" label="真实姓名"/>
            <el-table-column prop="currentNumberStr" label="当前时长"/>
            <el-table-column prop="passNumberStr" label="合格时长"/>
            <el-table-column prop="statusStr" label="状态" width="70px"/>
            <el-table-column prop="completeTimeStr" label="完成时间" width="160px"/>
          </el-table>
          <pagination v-show="courseWare.total>0" :total="courseWare.total" v-model:page="courseWare.queryParam.pageIndex"
                      v-model:limit="courseWare.queryParam.pageSize"
                      @pagination="courseWareSearch" :autoScroll="false"/>
        </el-tab-pane>
        <el-tab-pane label="试卷">
          <el-form ref="paperQueryParam" :inline="true">
            <el-form-item label="答卷状态：">
              <el-select v-model="paper.queryParam.status" placeholder="答卷状态" clearable>
                <el-option v-for="item in EnumMap.train.paperAnswerStatusEnum" :key="item.key" :value="item.key"
                           :label="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="用户名：" prop="userName">
              <el-input v-model="paper.queryParam.userName" clearable></el-input>
            </el-form-item>
            <el-form-item label="班级：">
              <tree-select v-model="paper.queryParam.departmentIdList" :data="departmentTree" :clearable="true"
                           placeholder="班级" style="width: 100%"/>
            </el-form-item>
            <el-form-item label="分数段：">
              <el-input-number v-model="paper.queryParam.minScoreStr" :precision="1" :step="1" :min="0"
                               :max="999"
                               placeholder="最小分数"></el-input-number>
              <span style="margin: 0px 10px">至</span>
              <el-input-number v-model="paper.queryParam.maxScoreStr" :precision="1" :step="1" :min="0"
                               :max="999"
                               placeholder="最大分数"></el-input-number>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="paperPage" v-has-perm="['train:course:detail:select']">查询</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="paper.tableData" border fit highlight-current-row style="width: 100%;">
            <el-table-column prop="id" label="答卷Id" width="100px"/>
            <el-table-column prop="userName" label="用户名"/>
            <el-table-column prop="realName" label="真实姓名"/>
            <el-table-column prop="departmentLevel" label="班级"/>
            <el-table-column label="得分比" width="100px">
              <template #default="{row}">
                {{ row.userScoreStr }} / {{ row.paperScoreStr }}
              </template>
            </el-table-column>
            <el-table-column label="正确比" width="100px">
              <template #default="{row}">
                {{ row.questionCorrect }} / {{ row.questionCount }}
              </template>
            </el-table-column>
            <el-table-column prop="doTimeStr" label="耗时" width="100px"/>
            <el-table-column prop="statusStr" label="状态" width="70px"/>
            <el-table-column prop="passStr" label="合格" width="60px"/>
            <el-table-column prop="createTimeStr" label="提交时间" width="160px"/>
            <el-table-column width="80px" label="操作" align="center">
              <template #default="{row}">
                <el-button v-show="row.status===1" @click="judge(row)"
                           v-has-perm="['train:course:detail:judge']" size="small" class="wdd-page-action-button">
                  去批改
                </el-button>
                <router-link target="_blank" :to="{path:'/train/paper/answer/read', query:{id:row.id}}"
                             v-show="row.status===2" class="link-left">
                  <el-button size="small" class="wdd-page-action-button">查看</el-button>
                </router-link>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="paper.total>0" :total="paper.total" v-model:page="paper.queryParam.pageIndex"
                      v-model:limit="paper.queryParam.pageSize"
                      @pagination="paperSearch" :autoScroll="false"/>
        </el-tab-pane>
        <el-tab-pane label="证书">
          <el-form :model="credential.queryParam" ref="credentialQueryParam" :inline="true">
            <el-form-item label="用户名：">
              <el-input v-model="credential.queryParam.userName" clearable></el-input>
            </el-form-item>
            <el-form-item label="班级：">
              <tree-select v-model="credential.queryParam.departmentIdList" :data="departmentTree" :clearable="true"
                           placeholder="班级" style="width: 100%"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="credentialPage" v-has-perm="['train:course:detail:select']">查询
              </el-button>
            </el-form-item>
          </el-form>
          <el-table :data="credential.tableData" border fit style="width: 100%;">
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
          <pagination v-show="credential.total>0" :total="credential.total" v-model:page="credential.queryParam.pageIndex"
                      v-model:limit="credential.queryParam.pageSize"
                      @pagination="credentialSearch" :autoScroll="false"/>
        </el-tab-pane>
      </el-tabs>
    </div>

  </div>
</template>

<script>

import {EnumMap} from '@/api/EnumMap'
import {tree} from '@/api/department'
import {info, userExport, userPage, userCourseWarePage} from '@/api/train'
import {userPage as credentialPage} from '@/api/credential'
import {userAnswerPage} from '@/api/trainExamPaper'

export default {
  name: 'TrainCourseDetail',
  data() {
    return {
      EnumMap: EnumMap,
      queryParam: {
        id: null
      },
      trainInfoVM: {},
      departmentTree: [],
      train: {
        queryParam: {
          trainId: null,
          status: null,
          userName: null,
          departmentIdList: null,
          pageIndex: 1,
          pageSize: 10
        },
        total: 0,
        tableData: []
      },
      courseWare: {
        queryParam: {
          trainId: null,
          userName: null,
          status: null,
          departmentIdList: null,
          pageIndex: 1,
          pageSize: 10
        },
        total: 0,
        tableData: []
      },
      paper: {
        queryParam: {
          trainId: null,
          userName: null,
          status: null,
          departmentIdList: null,
          minScoreStr: null,
          maxScoreStr: null,
          pageIndex: 1,
          pageSize: 10
        },
        total: 0,
        tableData: []
      },
      credential: {
        queryParam: {
          trainId: null,
          userName: null,
          departmentIdList: null,
          pageIndex: 1,
          pageSize: 10
        },
        total: 0,
        tableData: []
      }
    }
  },
  created() {
    let id = this.$route.query.id
    if (id && parseInt(id) !== 0) {
      this.queryParam.id = parseInt(id)
      this.train.queryParam.trainId = parseInt(id)
      this.courseWare.queryParam.trainId = parseInt(id)
      this.paper.queryParam.trainId = parseInt(id)
      this.credential.queryParam.trainId = parseInt(id)
      this.trainInfo()
      this.trainSearch()
      this.courseWareSearch()
      this.paperSearch()
      this.credentialSearch()
    }

    tree().then(re => {
      this.departmentTree = re.response
    })
  },
  methods: {
    trainInfo() {
      info(this.queryParam.id).then(re => {
        if (re.code === 1) {
          this.trainInfoVM = re.response
        } else {
          this.$message.error(re.message)
        }
      })
    },
    trainSubmitForm() {
      this.$refs['trainQueryForm'].validate((valid) => {
        if (valid) {
          this.train.queryParam.trainId = this.queryParam.id
          this.courseWare.queryParam.trainId = this.queryParam.id
          this.paper.queryParam.trainId = this.queryParam.id
          this.credential.queryParam.trainId = this.queryParam.id

          this.trainInfo()
          this.trainSearch()
          this.courseWareSearch()
          this.paperSearch()
          this.credentialSearch()
        }
      })
    },
    trainSearch() {
      userPage(this.train.queryParam).then(data => {
        if (data.code === 1) {
          const re = data.response
          this.train.tableData = re.list
          this.train.total = re.total
          this.train.queryParam.pageIndex = re.pageNum
        } else {
          this.$message.error(data.message)
        }
      })
    },
    trainPage() {
      this.train.queryParam.pageIndex = 1
      this.trainSearch()
    },
    trainExport() {
      userExport(this.train.queryParam).then(data => {
        if (data.code === 1) {
          window.open(data.response, '_blank')
        } else {
          this.$message.error(data.message)
        }
      })
    },
    courseWareSearch() {
      userCourseWarePage(this.courseWare.queryParam).then(data => {
        if (data.code === 1) {
          const re = data.response
          this.courseWare.tableData = re.list
          this.courseWare.total = re.total
          this.courseWare.queryParam.pageIndex = re.pageNum
        } else {
          this.$message.error(data.message)
        }
      })
    },
    courseWarePage() {
      this.courseWare.queryParam.pageIndex = 1
      this.courseWareSearch()
    },
    paperSearch() {
      userAnswerPage(this.paper.queryParam).then(data => {
        if (data.code === 1) {
          const re = data.response
          this.paper.tableData = re.list
          this.paper.total = re.total
          this.paper.queryParam.pageIndex = re.pageNum
        } else {
          this.$message.error(data.message)
        }
      })
    },
    paperPage() {
      this.paper.queryParam.pageIndex = 1
      this.paperSearch()
    },
    judge(row) {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let _this = this
      row.statusStr = '批改中'
      window.open(`/#/train/paper/answer/edit?id=${row.id}`, '_blank')
      window.judgeRefresh = function () {
        _this.trainInfo()
        _this.trainSearch()
        _this.paperSearch()
        _this.credentialSearch()
      }
    },
    credentialSearch() {
      credentialPage(this.credential.queryParam).then(data => {
        if (data.code === 1) {
          const re = data.response
          this.credential.tableData = re.list
          this.credential.total = re.total
          this.credential.queryParam.pageIndex = re.pageNum
        } else {
          this.$message.error(data.message)
        }
      })
    },
    credentialPage() {
      this.credential.queryParam.pageIndex = 1
      this.credentialSearch()
    }
  }
}
</script>
