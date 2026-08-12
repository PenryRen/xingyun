<template>
  <div style="width: 100%; height: 100%">
    <iframe :src="pdf.previewPath" frameborder="0" style="width: 100%; height: 100%"></iframe>
    <el-dialog v-model="questionDialogShow" :close-on-click-modal="false" :show-close="false">
      <template #header>
        <el-progress :percentage="currentAnchor.progress.percentage"
                     status="success">
          <span>{{ currentAnchor.progress.content }}</span>
        </el-progress>
      </template>
      <div class="course-ware-question">
        <QuestionDo :qType="currentAnchor.question.questionType"
                    :question="currentAnchor.question"
                    :answer="currentAnchor.answer" v-if="!currentAnchor.submit"/>
        <QuestionRead itemOrder="" :qType="currentAnchor.question.questionType"
                      :question="currentAnchor.question"
                      :answer="currentAnchor.answer" v-else/>
        <div class="course-ware-button">
          <el-button type="primary" class="wdd-paper-submit wdd-primary-button-color" @click="questionClick">
            提交
          </el-button>
          <el-button type="danger" class="wdd-paper-submit" @click="questionClose">关闭
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  import QuestionDo from '../exam/components/QuestionDo.vue'
  import QuestionRead from '../exam/components/QuestionRead.vue'
  import {select, watch, submit} from '@/api/courseWare'
  import {paperDecrypt, paperEncrypt, setPageTitle} from '@/utils/index'

  export default {
    components: {QuestionDo, QuestionRead},
    data() {
      return {
        id: null,
        uId: null,
        pdf: {
          courseWareQuestionVMList: []
        },
        currentAnchor: {
          question: {},
          answer: {},
          submit: false,
          progress: {
            percentage: 0,
            content: ''
          }
        },
        timeUpdate: {
          documentInterval: null,
          second: 0,
          start: true
        },
        questionDialogShow: false
      }
    },
    created() {
      this.id = this.$route.query.cId
      this.uId = this.$route.query.uId
      if (this.id && parseInt(this.id) !== 0) {
        select(this.id).then(re => {
          let response = paperDecrypt(import.meta.env.VITE_APP_PAIR_TWO_PRIVATE_KEY, re.message, re.response)
          this.pdf = response
          setPageTitle(`${response.name} | 课件观看`)
        })
      }
    },
    mounted() {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let _this = this
      clearInterval(this.timeUpdate.documentInterval)
      this.timeUpdate.documentInterval = setInterval(function () {
        if (_this.timeUpdate.start) {
          ++_this.timeUpdate.second
          _this.watchRecord()
          _this.watchQuestion()
        }
      }, 1000)
    },
    beforeUnmount() {
      if (this.timeUpdate.documentInterval) {
        clearInterval(this.timeUpdate.documentInterval)
        this.timeUpdate.documentInterval = null
      }
      document.title = `${import.meta.env.VITE_APP_SYSTEM_NAME}`
    },
    methods: {
      watchRecord() {
        // eslint-disable-next-line @typescript-eslint/no-this-alias
        let _this = this
        if (this.timeUpdate.second % 60 === 0) {
          let request = {
            trainItemUserId: this.uId,
            courseWareId: this.id,
            watchTime: this.timeUpdate.second,
            watchEnd: false
          }
          watch(request).then(re => {
            if (re.code === 1) {
              _this.$alert(re.message, '观看提示', {
                confirmButtonText: '返回课程',
                callback: action => {
                  _this.$router.push(`/train/detail?id=${re.response}`)
                }
              })
            }
          })
        }
      },
      watchQuestion() {
        let questionCount = this.pdf.courseWareQuestionVMList.length
        for (let i = 0; i < questionCount; i++) {
          let item = this.pdf.courseWareQuestionVMList[i]
          if (item.anchorSecond === this.timeUpdate.second) {
            this.currentAnchor.question = item.questionFrame
            this.currentAnchor.answer = item.questionAnswerFrame
            this.currentAnchor.progress.content = `${i + 1} / ${questionCount}`
            this.currentAnchor.progress.percentage = (i + 1) * 100 / questionCount
            this.questionDialogShow = true
            this.timeUpdate.start = false
          }
        }
      },
      questionClick() {
        let parameter = paperEncrypt(import.meta.env.VITE_APP_PAIR_ONE_PUBLIC_KEY, {
          id: this.id,
          answer: this.currentAnchor.answer
        })
        submit(parameter).then(re => {
          let response = paperDecrypt(import.meta.env.VITE_APP_PAIR_TWO_PRIVATE_KEY, re.message, re.response)
          this.currentAnchor.answer = response
          this.currentAnchor.submit = true
        })
      },
      questionClose() {
        if (this.currentAnchor.submit) {
          this.currentAnchor.submit = false
          this.timeUpdate.start = true
          this.questionDialogShow = false
        }
      }
    }
  }
</script>

<style lang="scss">
  #app {
    overflow-y: hidden;
  }
</style>
