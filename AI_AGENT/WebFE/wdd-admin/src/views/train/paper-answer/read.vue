<template>
  <div class="wdd-paper-contain">
    <div class="paper-item-order-fake">
      <div class="paper-item-order">
        <el-card shadow="never">
          <div class="paper-item-order-content">
            <div v-for="(tItem,tIndex) in paper.examPaperDoTitleList" :key="tIndex">
              <div class="wdd-anchor-title">{{ tItem.name }}</div>
              <div class="wdd-question-anchor">
                                <span :key="qItem.itemOrder" v-for="qItem in tItem.questionFrameList">
                                <el-tag :type="questionDoRightTag(answer.questionAnswerFrameList[qItem.itemOrder-1].doRight)"
                                        class="do-exam-title-tag"
                                        @click="goAnchor(`#question-${qItem.itemOrder}`)">{{ qItem.itemOrder }}</el-tag>
                                </span>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <div class="paper-question-content">
      <el-card shadow="never">
        <el-form v-loading="formLoading">
          <div :key="index" v-for="(titleItem,index) in paper.examPaperDoTitleList">
            <div class="wdd-exam-question-content wdd-paper-title">{{ titleItem.name }}</div>
            <div class="exam-paper-item-box" v-if="titleItem.questionFrameList.length!==0">
              <div :key="questionItem.itemOrder"
                   v-for="(questionItem,qIndex) in titleItem.questionFrameList"
                   :class="qIndex != (titleItem.questionFrameList.length - 1) ? 'exam-paper-item-box-contain  exam-paper-item-box-contain-line': 'exam-paper-item-box-contain'"
                   :id="`question-${questionItem.itemOrder}`">
                <QuestionRead :itemOrder="`${questionItem.itemOrder}`"
                              :qType="questionItem.questionType"
                              :question="questionItem"
                              :answer="answer.questionAnswerFrameList[questionItem.itemOrder-1]"/>
              </div>
            </div>
          </div>
        </el-form>
      </el-card>
    </div>

    <div class="paper-item-order-fake">
      <div class="paper-item-order">
        <el-card shadow="never">
          <div class="paper-item-order-content">
            <div class="paper-name">
              {{ paper.name }}
            </div>
            <div class="paper-info">
              <div>用户名：{{ userName }}</div>
              <div>真实姓名：{{ realName }}</div>
              <div>工号：{{ workNo }}</div>
              <div>身份证号：{{ idCard }}</div>
              <div>职位：{{ jobTitle }}</div>
              <div>班级：{{ departmentLevel }}</div>
              <div>得分：{{ answer.userScore }} / {{ paper.score }}</div>
              <div>耗时：{{ answer.doTimeStr }}</div>
              <div v-if="null !== judgeUser">批改人：{{ judgeUser }}</div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

  </div>
</template>

<script>

import {EnumMap, Format} from '@/api/EnumMap'
import {setPageTitle} from '@/utils'
import QuestionRead from '../../answer/components/QuestionRead.vue'
import {answerRead} from '@/api/trainExamPaper'


export default {
  name: 'TrainExamPaperAnswerRead',
  components: {QuestionRead},
  data() {
    return {
      paper: {},
      answer: {},
      userName: null,
      realName: null,
      workNo: null,
      idCard: null,
      jobTitle: null,
      departmentLevel: null,
      judgeUser: null,
      formLoading: false
    }
  },
  beforeCreate() {
    document.querySelector('body').setAttribute('style', 'background-color:#eff3f7')
  },
  beforeUnmount() {
    document.querySelector('body').removeAttribute('style')
  },
  created() {
    let id = this.$route.query.id

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      answerRead(id).then(re => {
        if (re.code === 1) {
          let response = re.response
          this.userName = response.userName
          this.userName = response.userName
          this.realName = response.realName
          this.workNo = response.workNo
          this.idCard = response.idCard
          this.jobTitle = response.jobTitle
          this.departmentLevel = response.departmentLevel
          this.judgeUser = response.judgeUser
          this.paper = response.paper
          this.answer = response.answer
          setPageTitle(`${response.paper.name} | 试卷查看`)
        } else {
          this.$message.error(re.message)
        }
      }).finally(() => {
        this.formLoading = false
      })
    }
  },
  methods: {
    questionDoRightTag(status) {
      return Format(EnumMap.exam.question.answer.doRightTag, status)
    },
    goAnchor(selector) {
      this.$el.querySelector(selector).scrollIntoView({behavior: 'instant', block: 'center', inline: 'nearest'})
    }
  }
}
</script>
