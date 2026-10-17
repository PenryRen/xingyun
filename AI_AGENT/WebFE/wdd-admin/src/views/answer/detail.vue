<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="paperQueryForm" :inline="true">
      <el-form-item label="试卷Id：" prop="id" :rules="[
        { required: true,  message: '请输入试卷Id' },
        { type: 'number', message: '必须为数字值' }]" required>
        <el-input v-model.number="queryParam.id" clearable></el-input>
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
      </el-card>
      <el-card class="wdd-show-col">
        <p><span>题目总数：</span><span>{{ paperInfoVM.questionCount }}</span></p>
        <p><span>待批改数：</span><span>{{ paperInfoVM.judgeCount }}</span></p>
        <p><span>合格人数：</span><span>{{ paperInfoVM.passCount }}</span></p>
        <p><span>完成人数：</span><span>{{ paperInfoVM.completeCount }}</span></p>
        <p><span>总人数：</span><span>{{ paperInfoVM.allCount }}</span></p>
      </el-card>
      <el-card class="wdd-show-col">
        <p><span>合格分：</span><span>{{ paperInfoVM.passScore }}</span></p>
        <p><span>试卷总分：</span><span>{{ paperInfoVM.score }}</span></p>
        <p><span>合格率：</span><span>{{ paperInfoVM.passPercent }}</span></p>
        <p><span>正确率：</span><span>{{ paperInfoVM.correctPercent }}</span></p>
        <p><span>得分率：</span><span>{{ paperInfoVM.scorePercent }}</span></p>
      </el-card>
      <el-card class="wdd-show-col">
        <div id="echarts-score-pie" style="width: 100%;min-height:150px;"/>
      </el-card>
    </div>

    <el-card>
      <div id="echarts-score-count" style="width: 100%;height:300px;"/>
    </el-card>

    <el-form :model="queryParam" ref="queryForm" :inline="true" style="margin-top: 30px;">
      <el-form-item label="答卷状态：">
        <el-select v-model="queryParam.status" placeholder="答卷状态" clearable>
          <el-option v-for="item in EnumMap.exam.examPaperAnswer.statusEnum" :key="item.key" :value="item.key"
                     :label="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="用户名：" prop="userName">
        <el-input v-model="queryParam.userName" clearable></el-input>
      </el-form-item>
      <el-form-item label="班级：">
        <tree-select v-model="queryParam.departmentIdList" :data="departmentTree" :clearable="true"
                     placeholder="班级" style="width: 100%"/>
      </el-form-item>
      <el-form-item label="分数段：">
        <el-input-number v-model="queryParam.minScoreStr" :precision="1" :step="1" :min="0"
                         :max="Number(paperInfoVM.score)"
                         placeholder="最小分数"></el-input-number>
        <span style="margin: 0px 10px">至</span>
        <el-input-number v-model="queryParam.maxScoreStr" :precision="1" :step="1" :min="0"
                         :max="Number(paperInfoVM.score)"
                         placeholder="最大分数"></el-input-number>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['exam:paper:answer:user']">查询</el-button>
        <el-button type="success" @click="exportForm" v-has-perm="['exam:paper:answer:result:export']">成绩导出
        </el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="listLoading" :data="tableData" border fit highlight-current-row style="width: 100%;">
      <el-table-column prop="id" label="答卷Id" width="100px"/>
      <el-table-column prop="userName" label="用户名"/>
      <el-table-column prop="realName" label="真实姓名"/>
      <el-table-column prop="workNo" label="工号"/>
      <el-table-column prop="departmentLevel" label="班级"/>
      <el-table-column prop="userScoreStr" label="得分" width="70px"/>
      <el-table-column prop="questionCorrect" label="正确" width="60px"/>
      <el-table-column prop="doTimeStr" label="耗时" width="100px"/>
      <el-table-column prop="statusStr" label="状态" width="70px"/>
      <el-table-column prop="passStr" label="合格" width="60px"/>
      <el-table-column prop="createTimeStr" label="提交时间" width="160px"/>
      <el-table-column width="210px" label="操作" align="center">
        <template #default="{row}">
          <el-button v-show="row.status===1" @click="judge(row)"
                     v-has-perm="['exam:paper:answer:read:edit']" size="small" class="wdd-page-action-button">
            去批改
          </el-button>
          <el-button v-show="row.passed === false && row.status ===2" @click="resitClick(row)" type="warning"
                     v-has-perm="['exam:paper:answer:resit']" size="small"
                     class="link-left wdd-page-action-button">补考
          </el-button>
          <router-link target="_blank" :to="{path:'/exam/paper/answer/read', query:{id:row.id}}"
                       v-show="row.status===2"
                       class="link-left">
            <el-button v-has-perm="['exam:paper:answer:read:select']" size="small"
                       class="wdd-page-action-button">查看
            </el-button>
          </router-link>
          <!--<el-button size="small" class="link-left wdd-page-action-button" @click="userAnswerExport(row)"
                     v-show="row.status===2" v-has-perm="['exam:paper:answer:print:export']">导出
          </el-button>-->
          <router-link target="_blank" :to="{path:'/exam/paper/answer/camera', query:{id:row.id}}"
                       v-show="row.status===2" class="link-left">
            <el-button v-has-perm="['exam:paper:answer:camera']" v-show="paperInfoVM.capture"
                       size="small" class="wdd-page-action-button">抓拍
            </el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search" :autoScroll="false"/>

    <el-dialog v-model="resit.showDialog" title="补考时间" width="600px">
      <el-form :model="resit.form" ref="resitForm" label-width="100px">
        <el-form-item label="补考时间：" class="form-item-content-block">
          <el-date-picker v-model="resit.form.limitDateTime" value-format="YYYY-MM-DD HH:mm:ss"
                          type="datetimerange"
                          range-separator="至" start-placeholder="补考开始时间" end-placeholder="补考结束时间"
                          :disabled-date="disabledDate">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="confirmResit">提交</el-button>
          <el-button @click="cancelResit">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>

import {disabledDate} from '@/utils/index'
import {resize} from '@/utils/resize'
import {EnumMap} from '@/api/EnumMap'
import {tree} from '@/api/department'
import {info, userAnswerPage, exportAnswerPage, userExport, resit} from '@/api/examPaperAnswer'
import * as echarts from 'echarts'

export default {
  mixins: [resize],
  name: 'ExamPaperAnswerDetail',
  data() {
    return {
      disabledDate: disabledDate,
      EnumMap: EnumMap,
      paperInfoVM: {},
      queryParam: {
        id: null,
        status: null,
        userName: null,
        departmentIdList: null,
        minScoreStr: null,
        maxScoreStr: null,
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: false,
      tableData: [],
      departmentTree: [],
      total: 0,
      resit: {
        showDialog: false,
        form: {
          paperAnswerId: null,
          limitDateTime: []
        }
      }
    }
  },
  mounted() {
    let id = this.$route.query.id
    if (id && parseInt(id) !== 0) {
      this.queryParam.id = parseInt(id)
      this.loadPaperInfo()
      this.search()
    }

    tree().then(re => {
      this.departmentTree = re.response
    })
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
      let echartsScorePie = echarts.init(document.getElementById('echarts-score-pie'), 'light')
      let echartsScoreCount = echarts.init(document.getElementById('echarts-score-count'), 'light')

      info(this.queryParam.id).then(re => {
        if (re.code === 1) {
          let response = re.response
          this.paperInfoVM = response
          echartsScorePie.setOption(this.pieOption(response.pieList))
          echartsScoreCount.setOption(this.option(response.x, response.y, response.completeCount))
          resize.initChat([echartsScorePie, echartsScoreCount])
        } else {
          this.$message.error(re.message)
        }
      })
    },
    search() {
      this.listLoading = true
      userAnswerPage(this.queryParam).then(data => {
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
    exportForm() {
      exportAnswerPage(this.queryParam).then(data => {
        if (data.code === 1) {
          window.open(data.response, '_blank')
        } else {
          this.$message.error(data.message)
        }
      })
    },
    judge(row) {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let _this = this
      row.statusStr = '批改中'
      window.open(`/#/exam/paper/answer/edit?id=${row.id}`, '_blank')
      window.judgeRefresh = function () {
        _this.loadPaperInfo()
        _this.search()
      }
    },
    resitClick(row) {
      this.resit.showDialog = true
      this.resit.form.paperAnswerId = row.id
    },
    userAnswerExport(row) {
      if (row.previewFilePath === null) {
        userExport(row.id).then(data => {
          row.previewFilePath = data.response
          window.open(data.response, '_blank')
        })
      } else {
        window.open(row.previewFilePath, '_blank')
      }
    },
    confirmResit() {
      resit(this.resit.form).then(data => {
        if (data.code === 1) {
          this.search()
          this.$message.success('设置补考成功！')
          this.cancelResit()
        } else {
          this.$message.error(data.message)
        }
      })
    },
    cancelResit() {
      this.resit.showDialog = false
      this.resit.form.paperAnswerId = null
    },
    pieOption(data) {
      return {
        tooltip: {
          trigger: 'item',
          formatter: '分数段：{b} <br/>人数: {c} ({d}%)'
        },
        series: [
          {
            name: '分数段分布',
            type: 'pie',
            radius: '80%',
            data: data
          }
        ]
      }
    },
    option(x, y, max) {
      return {
        title: {
          text: '分数段人数统计'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
            label: {
              show: true
            }
          },
          formatter: function (params, ticket, callback) {
            let xName = params[0].name
            let yValue = params[0].value
            if (xName.indexOf('ㅣ') === -1) {
              return `分数：${xName}<br/>人数：${yValue}`
            } else {
              let split = xName.replace('\n', '').split('ㅣ')
              return `分数：${split[0]}<=分<${split[1]}<br/>人数：${yValue}`
            }
          }
        },
        toolbox: {
          show: true,
          feature: {
            mark: {
              show: true
            },
            dataView: {
              show: true,
              readOnly: false,
              title: '数据视图'
            },
            magicType: {
              show: true,
              type: ['line', 'bar'],
              title: {
                line: '切换为折线图',
                bar: '切换为柱状图'
              }
            },
            restore: {
              show: true,
              title: '还原'
            },
            saveAsImage: {
              show: true,
              title: '保存'
            }
          }
        },
        xAxis: [
          {
            name: '分数段',
            type: 'category',
            data: x
          }
        ],
        yAxis: [
          {
            name: '人数',
            type: 'value',
            max: max
          }
        ],
        series: [
          {
            data: y,
            type: 'bar',
            showBackground: true,
            backgroundStyle: {
              color: 'rgba(180, 180, 180, 0.2)'
            }
          }
        ]
      }
    }
  }
}
</script>
