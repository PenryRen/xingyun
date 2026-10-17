<template>
  <div class="app-item-contain video-play-contain">
    <el-card shadow="never" class="video-card left">
      <div class="video-name">{{ video.name }}</div>
      <div class="video-level">{{ video.level }}</div>
      <video controls="controls" class="wdd-video" :src="video.originalPath" @timeupdate="onPlayerTimeupdate"
             @ended="onPlayerEnded"
             ref="courseVideo" controlslist="nodownload nofullscreen noremoteplayback noplaybackrate"
             disablePictureInPicture></video>
    </el-card>
    <el-card shadow="never" class="video-card right">
      <div class="video-create">
        <el-avatar :size="60" :src="video.createImagePath" shape="circle" fit="scale-down"
                   class="video-user-image">
          暂无头像
        </el-avatar>
        <div class="video-real-name">
          <div>{{ video.createUserName }}</div>
          <div>{{ video.createRealName }}</div>
        </div>
      </div>
      <div class="video-description">{{ video.description }}</div>
    </el-card>
    <el-dialog v-model="questionDialogShow" :close-on-click-modal="false" :show-close="false"
               class="course-ware-question-dialog">
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

<style lang="scss">
  // 全屏
  video::-webkit-media-controls-fullscreen-button {
    display: none !important;
  }
</style>

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
        video: {
          courseWareQuestionVMList: []
        },
        playSecond: null,
        watchCall: null,
        lastTime: 0,
        initTime: false,
        questionCall: null,
        currentAnchor: {
          question: {},
          answer: {},
          submit: false,
          progress: {
            percentage: 0,
            content: ''
          }
        },
        questionDialogShow: false
      }
    },
    created() {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let _this = this
      this.id = this.$route.query.cId
      this.uId = this.$route.query.uId
      if (this.id && parseInt(this.id) !== 0) {
        select(this.id).then(re => {
          let response = paperDecrypt(import.meta.env.VITE_APP_PAIR_TWO_PRIVATE_KEY, re.message, re.response)
          this.video = response
          this.$nextTick(() => {
            _this.initTime = true
            _this.$refs.courseVideo.currentTime = _this.video.currentTime + 1
          })
          setPageTitle(`${response.name} | 课件观看`)
        })
      }
    },
    mounted() {

    },
    beforeUnmount() {
      document.title = `${import.meta.env.VITE_APP_SYSTEM_NAME}`;
    },
    methods: {
      onPlayerEnded() {
        // eslint-disable-next-line @typescript-eslint/no-this-alias
        let _this = this
        let currentTime = this.$refs.courseVideo.currentTime
        watch({
          trainItemUserId: this.uId,
          courseWareId: this.id,
          watchTime: Number(currentTime.toFixed(0)),
          watchEnd: true
        }).then(re => {
          if (re.code === 1) {
            _this.$alert(re.message, '观看提示', {
              confirmButtonText: '返回课程',
              callback: action => {
                _this.$router.push(`/train/detail?id=${re.response}`)
              }
            })
          }
        })
      },
      onPlayerTimeupdate() {
        let currentTime = this.$refs.courseVideo.currentTime
        if (this.initTime) {
          this.lastTime = currentTime
          this.initTime = false
        } else {
          if (this.watchDrag(currentTime)) {
            return
          }
        }

        let playSecond = Number(currentTime.toFixed(0))
        this.watchRecord(playSecond)
        this.watchQuestion(playSecond)
        this.playSecond = playSecond
      },
      watchDrag(currentTime) {
        if (currentTime - this.lastTime > 2) {
          this.$refs.courseVideo.currentTime = this.lastTime
          return true
        } else {
          this.lastTime = currentTime
          return false
        }
      },
      watchRecord(playSecond) {
        // 每分钟执行一次
        // eslint-disable-next-line @typescript-eslint/no-this-alias
        let _this = this
        const duration = 60
        let lastPlaySecond = this.playSecond
        if (playSecond !== 0 && playSecond % duration === 0) {
          if (lastPlaySecond === playSecond && !this.watchCall) {
            this.watchCall = true
            let request = {
              trainItemUserId: this.uId,
              courseWareId: this.id,
              watchTime: playSecond,
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
          if (lastPlaySecond !== playSecond) {
            this.watchCall = false
          }
        }
      },
      watchQuestion(playSecond) {
        const duration = 1
        let lastPlaySecond = this.playSecond
        if (playSecond !== 0 && playSecond % duration === 0) {
          if (lastPlaySecond === playSecond && !this.questionCall) {
            this.questionCall = true
            let questionCount = this.video.courseWareQuestionVMList.length
            for (let i = 0; i < questionCount; i++) {
              let item = this.video.courseWareQuestionVMList[i]
              if (item.anchorSecond === playSecond) {
                this.currentAnchor.question = item.questionFrame
                this.currentAnchor.answer = item.questionAnswerFrame
                this.currentAnchor.progress.content = `${i + 1} / ${questionCount}`
                this.currentAnchor.progress.percentage = (i + 1) * 100 / questionCount
                this.questionDialogShow = true
                this.$refs.courseVideo.pause()
                if (document.fullscreenElement) {
                  document.exitFullscreen()
                }
              }
            }
          }
          if (lastPlaySecond !== playSecond) {
            this.questionCall = false
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
          this.$refs.courseVideo.play()
          this.questionDialogShow = false
        }
      }
    }
  }
</script>
