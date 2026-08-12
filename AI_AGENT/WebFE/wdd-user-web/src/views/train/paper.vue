<template>
  <div class="wdd-paper-contain" style="padding: 20px 30px 20px 20px" v-show="!paperNoneShow">
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
              <div class="paper-time">时长：{{ paper.suggestTimeStr }}</div>
            </div>
            <div class="wdd-split"></div>
            <div class="wdd-remain-time">
              <div class="wdd-remain-time-label">剩余时间</div>
              <div class="wdd-remain-time-content">{{ formatSeconds(remainTime) }}</div>
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
  disablePageEvent,
  enablePageEvent,
  paperDecrypt,
  paperEncrypt
} from '@/utils'
import {Base64} from 'js-base64'
import {EnumMap, Format} from '@/api/EnumMap'
import {h} from 'vue'
import {ElMessageBox} from 'element-plus'
import QuestionDo from '../exam/components/QuestionDo.vue'
import {select, cheat, submit} from '@/api/trainExamPaper'

export default {
  components: {QuestionDo},
  data() {
    return {
      paperNoneShow: true,
      paper: {},
      answer: {},
      formLoading: false,
      timer: null,
      cheatCount: 0,
      remainTime: 0,
      stopInterval: false,
      beforeUnloadTime: 0,
      cacheKey: null,
      uId: null
    }
  },
  beforeCreate() {
    document.querySelector('body').setAttribute('style', 'background-color:#eff3f7')
  },
  beforeUnmount() {
    document.querySelector('body').removeAttribute('style')
    this.stopAll()
    enablePageEvent()
  },
  created() {
    let id = this.$route.query.pId
    this.uId = this.$route.query.uId
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    let _this = this
    if (id && parseInt(id) !== 0 && parseInt(this.uId) !== 0) {
      _this.formLoading = true
      _this.cacheKey = `wdd-train-exam-answer-${id}`
      select(this.uId, id).then(re => {
        if (re.code === 1) {
          let response = paperDecrypt(import.meta.env.VITE_APP_PAIR_TWO_PRIVATE_KEY, re.message, re.response)
          _this.cheatCount = response.cheatCount
          _this.remainTime = response.remainTime
          _this.paper = response.paper

          //首次进入，清理缓存
          if(response.initFirst){
            localStorage.removeItem(_this.cacheKey)
          }

          _this.answer = _this.revertCache(response.answer)

          setPageTitle(`${response.paper.name} | 试卷答题`)

          disablePageEvent()

          // 防作弊开启
          if (_this.paper.cheat !== null && _this.paper.cheat) {
            // 超过最大防作弊次数
            if (_this.cheatCount >= _this.paper.maxCheatCount) {
              _this.paperAnswerSubmit()
              return
            }

            window.addEventListener('visibilitychange', _this.checkCheat)
            window.addEventListener('beforeunload', _this.beforeCloseWindow)
            window.addEventListener('unload', _this.closeWindow)
            document.onkeydown = _this.windowKeyDown
          }

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

      let answerBase64 = Base64.encode(JSON.stringify(cacheAnswer))
      localStorage.setItem(this.cacheKey, answerBase64)
    },
    revertCache(answer) {
      let cacheAnswerStr = localStorage.getItem(this.cacheKey)
      if (cacheAnswerStr !== null) {
        let cacheAnswer = JSON.parse(Base64.decode(cacheAnswerStr))
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
    windowKeyDown(event) {
      if (event && event.keyCode === 123) {  //禁用F12
        event.returnValue = false
      }
    },
    beforeCloseWindow() {
      this.beforeUnloadTime = new Date().getTime()
      return '关闭页面将不会保存考试数据'
    },
    closeWindow(e) {
      // 关闭窗口 作弊 + 1
      let gapTime = new Date().getTime() - this.beforeUnloadTime
      if (gapTime <= 5) {
        navigator.sendBeacon(`/api/exam/paper/cheat/${this.paper.id}`, null)
      }
    },
    checkCheat() {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let _this = this
      if (document.hidden) {
        ++_this.cheatCount
        cheat(_this.paper.id).then(re => {
        }).finally(() => {
          if (_this.cheatCount > _this.paper.maxCheatCount) {
            _this.stopAll()
            _this.$router.push('/train/index')
          } else if (_this.cheatCount === _this.paper.maxCheatCount) {
            _this.stopAll()
            _this.paperAnswerSubmit()
          } else {
            ElMessageBox({
              title: '警告',
              message: h('p', null, [
                h('span', {style: 'color: red'}, `请不要离开试卷，超过${_this.paper.maxCheatCount}次会自动提交试卷！`)
              ]),
              confirmButtonText: '确定',
              closeOnClickModal: false
            })
          }
        })
      }
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
          if (_this.remainTime <= 0) {
            _this.stopAll()
            _this.paperAnswerSubmit()
          } else {
            ++_this.answer.doTime
            --_this.remainTime

            // 页面缓存答案
            if (_this.remainTime % 30 === 0) {
              _this.cacheSave()
            }
          }
        }
      }, 1000)
    },
    stopAll() {
      // 清除防作弊事件
      if (this.paper.cheat !== null && this.paper.cheat) {
        window.removeEventListener('visibilitychange', this.checkCheat)
        window.removeEventListener('beforeunload', this.beforeCloseWindow)
        window.removeEventListener('unload', this.closeWindow)
        document.onkeydown = null
      }
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
      submit(this.uId, paperEncrypt(import.meta.env.VITE_APP_PAIR_ONE_PUBLIC_KEY, this.answer)).then(re => {
        if (re.code === 1) {
          localStorage.removeItem(_this.cacheKey)
          _this.$alert(`试卷得分：${re.message}分`, '考试结果', {
            confirmButtonText: '返回课程',
            callback: action => {
              _this.$router.push(`/train/detail?id=${re.response}`)
            }
          })
        } else {
          _this.$message.error(re.message)
        }
      })
    },
    cancelForm() {
      window.close()
    }
  }
}
</script>
