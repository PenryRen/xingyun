<template>
  <div class="wdd-paper-contain" style="padding: 20px 30px 20px 20px" v-show="!paperNoneShow" id="wdd-paper-do">
    <div class="paper-item-order-fake">
      <div class="paper-item-order">
        <el-card shadow="never">
          <div class="paper-item-order-content">
            <div v-for="(tItem,tIndex) in paper.examPaperDoTitleList" :key="tIndex">
              <div class="wdd-anchor-title">{{ tItem.name }}</div>
              <div class="wdd-question-anchor">
                <span :key="qItem.itemOrder" v-for="qItem in tItem.questionFrameList">
                <el-tag :type="questionCompleted(answer.questionAnswerFrameList[qItem.itemOrder-1].completed)"
                        class="do-exam-title-tag"
                        @click="goAnchor(`#question-${qItem.itemOrder}`)">{{ qItem.itemOrder }}</el-tag>
                </span>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
    <div class="paper-question-content" style="margin-right: 20px;">
      <el-card shadow="never">
        <el-form v-loading="formLoading">
          <div :key="index" v-for="(titleItem,index) in paper.examPaperDoTitleList">
            <div class="sdd-exam-question-content wdd-paper-title">{{ titleItem.name }}</div>
            <div class="exam-paper-item-box" v-if="titleItem.questionFrameList.length!==0">
              <div :key="questionItem.itemOrder"
                   v-for="(questionItem,qIndex) in titleItem.questionFrameList"
                   :class="qIndex != (titleItem.questionFrameList.length - 1) ? 'exam-paper-item-box-contain  exam-paper-item-box-contain-line': 'exam-paper-item-box-contain'"
                   :id="`question-${questionItem.itemOrder}`">
                <div class="itemOrder wdd-content-line-height">
                  {{ `${questionItem.itemOrder}.` }}
                </div>
                <div class="title-contain">
                  <QuestionDo :qType="questionItem.questionType"
                              :question="questionItem"
                              :answer="answer.questionAnswerFrameList[questionItem.itemOrder-1]"/>
                </div>
              </div>
            </div>
          </div>
        </el-form>
      </el-card>
    </div>
    <div class="paper-info-submit-fake">
      <div class="paper-info-submit">
        <el-card shadow="never">
          <div class="paper-item-order-content">
            <div class="paper-name">
              {{ paper.name }}
            </div>
            <div class="paper-info">
              <div>总分：{{ paper.score }}</div>
              <div class="paper-time">建议时长：{{ paper.suggestTimeStr }}</div>
            </div>
            <div class="wdd-split"></div>
            <div class="wdd-remain-time">
              <div class="wdd-remain-time-label">考试计时</div>
              <div class="wdd-remain-time-content">{{ formatSeconds(answer.doTime) }}</div>
            </div>
            <div class="wdd-split"></div>
            <div class="wdd-submit">
              <el-button type="primary" @click="submitForm"
                         class="wdd-paper-submit wdd-primary-button-color">提交试卷
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import {
  setPageTitle,
  clearTagP,
  formatSeconds,
  paperDecrypt,
  paperEncrypt
} from '@/utils'
import {Base64} from 'js-base64'
import {EnumMap, Format} from '@/api/EnumMap'
import QuestionDo from '../exam/components/QuestionDo.vue'
import {select, submit} from '@/api/practice'

export default {
  components: {QuestionDo},
  data() {
    return {
      paperNoneShow: true,
      paper: {},
      answer: {
        doTime: 0
      },
      formLoading: false,
      timer: null,
      remainTime: 0,
      stopInterval: false,
      cacheKey: null
    }
  },
  beforeCreate() {
    document.querySelector('body').setAttribute('style', 'background-color:#eff3f7')
  },
  beforeUnmount() {
    document.querySelector('body').removeAttribute('style')
    this.stopAll()
  },
  created() {
    let id = this.$route.query.id
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    let _this = this
    if (id && parseInt(id) !== 0) {
      _this.formLoading = true
      _this.cacheKey = `wdd-practice-answer-${id}`
      select(id).then(re => {
        if (re.code === 1) {
          let response = paperDecrypt(import.meta.env.VITE_APP_PAIR_TWO_PRIVATE_KEY, re.message, re.response)
          _this.paper = response.paper
          _this.answer = _this.revertCache(response.answer)

          setPageTitle(`${response.paper.name} | 试卷答题`)
          // 倒计时
          _this.timeReduce()

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
    cacheSave() {
      let cacheAnswer = []
      for (let i = 0; i < this.answer.questionAnswerFrameList.length; i++) {
        let pageItem = this.answer.questionAnswerFrameList[i]
        cacheAnswer.push({
          questionId: pageItem.questionId,
          contentKey: pageItem.contentKey,
          contentArrayKey: pageItem.contentArrayKey,
          contentArray: pageItem.contentArray,
          content: pageItem.content,
          completed: pageItem.completed
        })
      }
      let practiceCache = {
        doTime: this.answer.doTime,
        cacheAnswer: cacheAnswer
      }
      let answerBase64 = Base64.encode(JSON.stringify(practiceCache))
      localStorage.setItem(this.cacheKey, answerBase64)
    },
    revertCache(answer) {
      let cacheAnswerStr = localStorage.getItem(this.cacheKey)
      if (cacheAnswerStr !== null) {
        let practiceCache = JSON.parse(Base64.decode(cacheAnswerStr))
        let cacheAnswer = practiceCache.cacheAnswer
        answer.doTime = practiceCache.doTime
        for (let i = 0; i < answer.questionAnswerFrameList.length; i++) {
          let pageItem = answer.questionAnswerFrameList[i]
          for (let j = 0; j < cacheAnswer.length; j++) {
            let cacheItem = cacheAnswer[j]
            if (pageItem.questionId === cacheItem.questionId) {
              pageItem.contentKey = cacheItem.contentKey
              pageItem.contentArrayKey = cacheItem.contentArrayKey
              pageItem.contentArray = cacheItem.contentArray
              if (pageItem.questionType === 5) {
                this.$nextTick(() => {
                  this.answer.questionAnswerFrameList[i].content = cacheItem.content
                })
              } else {
                pageItem.content = cacheItem.content
              }
              pageItem.completed = cacheItem.completed
            }
          }
        }
      }
      return answer
    },
    formatSeconds(theTime) {
      return formatSeconds(theTime)
    },
    timeReduce() {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let _this = this
      clearInterval(this.timer)
      this.timer = setInterval(function () {
        if (!_this.stopInterval) {
          ++_this.answer.doTime
          // 页面缓存答案
          if (_this.answer.doTime % 10 === 0) {
            _this.cacheSave()
          }
        }
      }, 1000)
    },
    stopAll() {
      this.stopInterval = true
      clearInterval(this.timer)
      this.timer = null
    },
    questionCompleted(completed) {
      return Format(EnumMap.exam.question.answer.doCompletedTag, completed)
    },
    goAnchor(selector) {
      this.$el.querySelector(selector).scrollIntoView({behavior: 'instant', block: 'center', inline: 'nearest'})
    },
    submitForm() {
      this.$confirm('此操作将提交该试卷, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.stopAll()
        this.paperAnswerSubmit()
      })
    },
    paperAnswerSubmit() {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let _this = this
      this.answer.questionAnswerFrameList.forEach(function (item, index, array) {
        if (item.questionType === 5) {
          item.content = clearTagP(item.content)
        }
      })
      submit(paperEncrypt(import.meta.env.VITE_APP_PAIR_ONE_PUBLIC_KEY, this.answer)).then(re => {
        localStorage.removeItem(_this.cacheKey)
        if (re.code === 1) {  //无需批改
          _this.$alert(`练习得分：${re.message}分`, '考试结果', {
            confirmButtonText: '查看答卷',
            callback: action => {
              _this.$router.push(`/practice/paper/read?id=${re.response}`)
            }
          })
        } else if (re.code === 2) {  //需要批改
          _this.$alert(`主观题得分：${re.message}`, '考试结果', {
            confirmButtonText: '去批改',
            callback: action => {
              _this.$router.push(`/practice/paper/edit?id=${re.response}`)
            }
          })
        }
      })
    },
    cancelForm() {
      window.close()
    }
  }
}
</script>
