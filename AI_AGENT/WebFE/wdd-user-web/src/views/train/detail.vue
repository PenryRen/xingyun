<template>
  <div class="app-item-contain train-contain">
    <none-show v-if="trainNoneShow"/>
    <div v-else>
      <el-card shadow="never">
        <div class="train-head">
          <div>
            <div class="train-name">{{ train.name }}</div>
            <div class="train-item-label">课程数量：{{ train.itemCount }}</div>
            <div class="train-item-label">课程时长：{{ train.studyTimeStr }}</div>
            <div class="train-item-label">开始时间：{{ train.startTime }}</div>
            <div class="train-item-label">结束时间：{{ train.endTime }}</div>
          </div>
          <div>
            <img :src="train.coverPath" style="width: 240px;height: 135px"/>
          </div>
        </div>
      </el-card>

      <el-tabs v-model="activeName" type="border-card" class="train-content-tab">
        <el-tab-pane label="描述" name="description">
          <div class="train-description train-tab-content" v-html="train.description"></div>
        </el-tab-pane>
        <el-tab-pane label="课程" name="course">
          <div class="train-tab-content">
            <div v-for="item in train.courseWareList" :key="item.itemOrder" @click="courseWatch(item)">
              <div class="train-course-item">
                <div class="train-course-item-left">
                  <el-icon>
                    <video-play v-if="item.fileType === 1"/>
                    <document v-else-if="item.fileType === 2"/>
                  </el-icon>
                </div>
                <div class="train-course-item-content">{{ item.itemOrder }}. {{ item.name }}
                  &nbsp;&nbsp;<el-tag v-if="item.vmType !== undefined && item.vmType !== null && item.vmType !==''">实训</el-tag>
                </div>

                <div class="train-course-item-right">
                  <el-progress :text-inside="true" :stroke-width="20" :status="item.status === 2 ? 'success':''" :percentage="item.percentage">
                    <div class="progress-content">{{ item.currentNumberStr }} / {{ item.passNumberStr }}</div>
                  </el-progress>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="试卷" name="examPaper">
          <none-show v-if="paperNoneShow"/>
          <div class="train-tab-content train-exam-paper-tab" v-else>
            <div class="train-paper-card" @click="paperDo(train.examPaper)">
              <div>{{ train.examPaper.name }}</div>
              <div class="paper-item-des" style="margin-top: 10px">合格分：{{ train.examPaper.passNumberStr }}</div>
              <div class="paper-item-des">总分：{{ train.examPaper.maxNumberStr }}</div>
              <div class="paper-item-des">考试次数：{{ train.examPaper.allowCount }}</div>
            </div>

            <div class="train-paper-record">
              <el-table :data="train.examPaper.answerList" style="width: 100%" row-class-name="">
                <el-table-column prop="paperName" label="名称"/>
                <el-table-column label="正确题数" width="100">
                  <template #default="{row}">{{ row.questionCorrect }} / {{ row.questionCount }}</template>
                </el-table-column>
                <el-table-column label="得分" width="100" sortable :sort-by="['userScore']">
                  <template #default="{row}">{{ row.userScore }} / {{ row.paperScore }}</template>
                </el-table-column>
                <el-table-column prop="doTime" label="耗时" width="100"/>
                <el-table-column prop="statusStr" label="状态" width="80"/>
                <el-table-column prop="passedStr" label="合格" width="80"/>
                <el-table-column prop="createTime" label="创建时间" width="180"/>
              </el-table>
              <div class="train-paper-table-foot">
                <span class="foot-item">考试结果：
                  <el-tag v-if="train.examPaper.status === 1" effect="dark">{{ train.examPaper.statusStr }}</el-tag>
                  <el-tag v-if="train.examPaper.status === 2" type="success" effect="dark">{{ train.examPaper.statusStr }}</el-tag>
                  <el-tag v-if="train.examPaper.status === 3" type="danger" effect="dark">{{ train.examPaper.statusStr }}</el-tag>
                </span>
                <span class="foot-item">完成时间：{{ train.examPaper.completeTime }}</span>
              </div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="证书" name="credential">
          <none-show v-if="credentialNoneShow"/>
          <div class="train-tab-content tab-credential-tab" v-else>
            <el-image style="height: 400px" :src="train.credential.credentialImagePath"
                      :preview-src-list="[train.credential.credentialImagePath]" :preview-teleported="true"
                      :hide-on-click-modal="true"></el-image>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>

import {VideoPlay, Document} from '@element-plus/icons-vue'
import {select, start, allocation, wareSelectOne} from '@/api/train'
import {Ssh_URL} from '@/utils/common'
import Cookies from "js-cookie";
import {checkVmWare,getVmWare} from "@/api/vmWare";
import {clearTagP} from "@/utils";
import {Base64} from "js-base64";
import {monitor} from "@/api/examPaperAnswer";
export default {
  components: {VideoPlay, Document},
  data() {
    return {
      vmQuery:{
        vmParentId:"",
        classes:"00",
        paperId:null,
      },
      answer: {},
      activeName: 'description',
      trainNoneShow: false,
      paperNoneShow: false,
      credentialNoneShow: false,
      train: {
        examPaper: {},
        credential: {
          credentialImagePath: null
        }
      },
      trainId: null,
      Ssh_URL:Ssh_URL
    }
  },
/*  mounted(){
    /!**
     * iframe-宽高自适应显示
     *!/
    function changeMapIframe(){
      const map = document.getElementById('map');
      const deviceWidth = document.body.clientWidth;
      const deviceHeight = document.body.clientHeight;
      console.log("宽" + deviceWidth);
      console.log("高" + deviceHeight);
      map.style.width = (Number(deviceWidth)-240) + 'px'; //数字是页面布局宽度差值
      map.style.height = (Number(deviceHeight)+764) + 'px'; //数字是页面布局高度差
    }

    changeMapIframe()

    window.onresize = function(){
      changeMapIframe()
    }
  },*/
  created() {
    let id = this.$route.query.id
    if (id && parseInt(id) !== 0) {
      this.trainId = id
      select(id).then(re => {
        if (re.code === 1) {
          let response = re.response
          this.paperNoneShow = response.examPaper === null
          this.credentialNoneShow = response.credential === null
          response.description = response.description.replaceAll('\n', '<br/>')
          this.train = response
        } else {
          this.$message.error(re.message)
        }
      }).finally(() => {

      })
    }
  },
  methods: {

    async courseWatch(item) {
      await this.checkStart(item, 1)
      if (item.vmType !== undefined && item.vmType !== null && item.vmType !== '') {
        this.vmQuery.vmParentId = item.vmType
        checkVmWare(this.vmQuery).then(checkResponse => {
          if (checkResponse && (checkResponse.code === 0 || checkResponse.code === 1)){
            this.setVmUrl(item.trainUserItemId,item.targetId,item.fileType);
          } else if (checkResponse && checkResponse.code === 2){
            this.$message.error(checkResponse.message);
          } else if (checkResponse && checkResponse.code === 3){
            this.$confirm(checkResponse.message, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.setVmUrl(item.trainUserItemId,item.targetId,item.fileType);
            })
          }
        })
      } else if (item.fileType === 1) {
        window.open(`#/course/ware/video?uId=${item.trainUserItemId}&cId=${item.targetId}`, '_blank')
      } else if (item.fileType === 2) {
        window.open(`#/course/ware/pdf?uId=${item.trainUserItemId}&cId=${item.targetId}`, '_blank')
      }
    },
    setVmUrl(trainUserItemId, targetId, fileType) {
      localStorage.setItem("trainVmCheck", "true");
      if (fileType === 1) {
        window.open(`#/course/ware/videoAndWare?uId=${trainUserItemId}&cId=${targetId}&tId=${this.$route.query.id}`, '_blank');
      } else if (fileType === 2) {
        window.open(`#/course/ware/pdfAndWare?uId=${trainUserItemId}&cId=${targetId}&tId=${this.$route.query.id}`, '_blank');
      }
    },
    paperAnswerMonitor() {
      let answerStr = JSON.stringify(this.answer)
      let answerMonitorCopy = JSON.parse(answerStr)
      answerMonitorCopy.questionAnswerFrameList.forEach(function (item, index, array) {
        if (item.questionType === 5) {
          item.content = clearTagP(item.content)
        }
      })
      let answerBase64 = Base64.encode(JSON.stringify(answerMonitorCopy))
      monitor({value: answerBase64}).then(re => {
      })
    },
    async paperDo(item) {
      await this.checkStart(item, 2)
      window.open(`#/train/paper/do?uId=${item.trainUserItemId}&pId=${item.targetId}`, '_blank')
    },
    async checkStart(trainItem, type) {
      if (null == trainItem.trainUserItemId) {
        let res = await start(this.trainId)
        this.train = res.response
        if (type === 1) {
          this.train.courseWareList.forEach(item => {
            if (item.targetId === trainItem.targetId) {
              trainItem.trainUserItemId = item.trainUserItemId
            }
          })
        } else if (type === 2) {
          trainItem.trainUserItemId = this.train.examPaper.trainUserItemId
        }
      }
    }
  }
}
</script>
