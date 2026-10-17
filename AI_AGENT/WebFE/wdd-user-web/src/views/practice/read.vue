<template>
  <none-show v-if="paperNoneShow"/>
  <div class="wdd-paper-contain" v-else>
    <div class="paper-item-order-fake">
      <div class="paper-item-order">
        <el-card shadow="never">
          <div class="paper-item-order-content">
            <div class="paper-name">
              {{ paper.name }}
            </div>
            <div class="paper-info">
              <div>得分：{{ answer.userScore }} / {{ paper.score }}</div>
              <div>耗时：{{ answer.doTimeStr }}</div>
              <div>合格分：{{ answer.passScore }}</div>
              <div>考试通过：{{ answer.passed ? '是' : '否' }}</div>
            </div>
            <div class="wdd-split" style="margin-bottom: 10px"></div>
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
  </div>
</template>

<script>
import {EnumMap, Format} from '@/api/EnumMap'
import {setPageTitle, paperDecrypt} from '@/utils'
import QuestionRead from '../exam/components/QuestionRead.vue'
import {read} from '@/api/practice'

export default {
  components: {QuestionRead},
  data() {
    return {
      paper: {},
      answer: {},
      formLoading: false,
      paperNoneShow: true
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
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    let _this = this
    if (id && parseInt(id) !== 0) {
      _this.formLoading = true
      read(id).then(re => {
        if (re.code === 1) {
          let response = paperDecrypt(import.meta.env.VITE_APP_PAIR_TWO_PRIVATE_KEY, re.message, re.response)
          _this.paper = response.paper
          _this.answer = response.answer
          setPageTitle(`${response.paper.name} | 答卷查看`)
          _this.paperNoneShow = false
        } else {
          _this.paperNoneShow = true
          _this.$message.error(re.message)
        }
      }).finally(() => {
        _this.formLoading = false
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
