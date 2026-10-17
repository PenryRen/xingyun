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

    <div class="paper-question-content paper-edit-content">
      <el-card shadow="never">
        <el-form v-loading="formLoading">
          <div :key="index" v-for="(titleItem,index) in paper.examPaperDoTitleList">
            <div class="wdd-exam-question-content wdd-paper-title">{{ titleItem.name }}</div>
            <div class="exam-paper-item-box" v-if="titleItem.questionFrameList.length!==0">
              <div :key="questionItem.itemOrder"
                   v-for="(questionItem,qIndex) in titleItem.questionFrameList"
                   :class="qIndex != (titleItem.questionFrameList.length - 1) ? 'exam-paper-item-box-contain  exam-paper-item-box-contain-line': 'exam-paper-item-box-contain'"
                   :id="`question-${questionItem.itemOrder}`">
                <div class="itemOrder wdd-content-line-height">
                  {{ `${questionItem.itemOrder}.` }}
                </div>
                <div class="title-contain exam-paper-show">
                  <QuestionEdit :qType="questionItem.questionType" :question="questionItem"
                                :answer="answer.questionAnswerFrameList[questionItem.itemOrder-1]"/>
                  <div class="question-judge item-correct"
                       v-if="answer.questionAnswerFrameList[questionItem.itemOrder-1].doRight === null  || answer.questionAnswerFrameList[questionItem.itemOrder-1].doRight === undefined">
                    <span class="title correct-title">批改：</span>
                    <span class="correct-content judge-score">
                    <el-input-number :precision="1"
                                     v-model="answer.questionAnswerFrameList[questionItem.itemOrder-1].judgeScoreVM"
                                     :min="0" :max="Number(questionItem.trickScore)"
                                     @change="judgeChange(answer.questionAnswerFrameList[questionItem.itemOrder-1])"></el-input-number>
                  </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-form>
      </el-card>
    </div>


    <div class="paper-item-order-fake">
      <div class="paper-info-submit">
        <el-card shadow="never">
          <div class="paper-item-order-content">
            <div class="paper-name">
              {{ paper.name }}
            </div>
            <div class="paper-info">
              <div>得分：{{ answer.userScore }} / {{ paper.score }}</div>
              <div>耗时：{{ answer.doTimeStr }}</div>
              <div>合格分：{{ answer.passScore }}</div>
            </div>
            <div class="wdd-split"></div>
            <div class="wdd-submit">
              <el-button type="primary" @click="submitForm"
                         class="wdd-paper-submit wdd-primary-button-color">提交批改
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>

import {paperDecrypt, paperEncrypt, setPageTitle} from '@/utils'
import QuestionEdit from '../exam/components/QuestionEdit.vue'
import {read, edit} from '@/api/practice'
import {EnumMap, Format} from '@/api/EnumMap'

export default {
  name: 'ExamPaperAnswerEdit',
  components: {QuestionEdit},
  data() {
    return {
      paper: {},
      answer: {},
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
      this.initLoad(id)
    }
  },
  mounted() {
  },
  methods: {
    initLoad(id) {
      this.formLoading = true
      read(id).then(re => {
        if (re.code === 1) {
          let response = paperDecrypt(import.meta.env.VITE_APP_PAIR_TWO_PRIVATE_KEY, re.message, re.response)
          this.paper = response.paper
          this.answer = response.answer
          setPageTitle(`${response.paper.name} | 试卷批改`)
        } else {
          this.$message.error(re.message)
        }
      }).finally(() => {
        this.formLoading = false
      })
    },
    judgeChange(answer) {
      answer.doRight = answer.judgeScoreVM !== null ? undefined : null
    },
    submitForm() {
      edit(paperEncrypt(import.meta.env.VITE_APP_PAIR_ONE_PUBLIC_KEY, this.answer)).then(re => {
        if (re.code === 1) {
          this.$alert(`练习得分：${re.response}分`, '考试结果', {
            confirmButtonText: '查看答卷',
            callback: action => {
              this.$router.push(`/practice/paper/read?id=${this.answer.id}`)
            }
          })
        } else {
          this.$message.error(re.message)
        }
      })
    },
    questionDoRightTag(status) {
      return Format(EnumMap.exam.question.answer.doRightTag, status)
    },
    goAnchor(selector) {
      this.$el.querySelector(selector).scrollIntoView({behavior: 'instant', block: 'center', inline: 'nearest'})
    }
  }
}
</script>
