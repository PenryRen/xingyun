<template>
  <div class="wdd-paper-contain" style="padding: 20px 30px 20px 20px" v-show="!paperNoneShow" id="wdd-paper-do">
    <div v-show="vmHide" class="paper-item-order-fake">
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
    <div class="paper-question-split-content">
      <div class="paper-question-content">
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
      <div v-show="!vmHide" class="paper-vm-content">
          <el-card shadow="never">
            <div class="paper-vm">
              <iframe v-if="!wuyingMode" width="100%" height="100%" style="border:0" :src="vmUrl"></iframe>
              <iframe v-show="wuyingMode" id="wuying-desktop-session" title="无影云桌面" width="100%" height="100%" style="border:0"></iframe>
            </div>
          </el-card>
      </div>
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
            <div class="wdd-split" v-if="camera.initSuccess"></div>
            <div class="exam-paper-camera" v-show="camera.initSuccess">
              <video id="camera-video" width="180" height="150" autoplay="autoplay"></video>
              <canvas id="camera-canvas" width="640" height="480" style="display: none"></canvas>
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
<style>
  .paper-question-split-content{
    display: flex;
    flex: 1;
  }
  .paper-vm-content{
    margin-right: 20px;
    top: 20px;
    position: sticky;
    height: calc(100vh - 40px) !important;
  }
  .paper-vm-content .el-card{
    width: 100%;
    height: 100%;
  }
  .paper-vm-content .el-card .el-card__body{
    width: 100%;
    height: 100%;
  }
  .paper-vm-content .paper-vm {
    width: 100%;
    height: 100%;
  }
</style>

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
import QuestionDo from '../components/QuestionDo.vue'
import {select, camera, cheat} from '@/api/examPaper'
import {monitor, submit} from '@/api/examPaperAnswer'
import {checkVmWare,getVmWare} from '@/api/vmWare'
import {startWuyingDesktopSession} from '@/utils/wuyingDesktop'
import Split from 'split.js'

export default {
  components: {QuestionDo},
  data() {
    return {
      paperNoneShow: true,
      vmHide:true,
      vmUrl:"",
      wuyingMode: false,
      lastRemoteResp: null,
      paper: {},
      answer: {},
      formLoading: false,
      timer: null,
      cheatCount: 0,
      remainTime: 0,
      stopInterval: false,
      beforeUnloadTime: 0,
      cacheKey: null,
      camera: {
        initSuccess: false,
        video: null,
        canvas: null,
        context: null,
        random: 50
      },
      vmQuery: {
        vmParentId: "",
        classes: "01",
        examPaperId: null,
      }
    }
  },
  beforeCreate() {
    document.querySelector('body').setAttribute('style', 'background-color:#eff3f7')
  },
  beforeUnmount() {
    document.querySelector('body').removeAttribute('style')
    if (this.paper.paperType === 4) {
      this.paperAnswerMonitor();
    }
    this.stopAll()
    enablePageEvent()
  },
  created() {
    this.camera.random = Math.floor(Math.random() * 10) + 50
    let id = this.$route.query.id
    let type = this.$route.query.type
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    let _this = this
    if (id && parseInt(id) !== 0) {
      _this.formLoading = true
      _this.cacheKey = `wdd-exam-answer-${id}`
      select(type, id).then(re => {
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

          if(_this.paper.paperType === 4){
            _this.vmQuery.vmParentId = _this.paper.vmType;
            _this.vmQuery.examPaperId = _this.paper.paperId;
            this.preCheck();
          } else {
            // 倒计时
            _this.timeReduce()
          }

          // 开启抓拍，摄像头初始化
          if (_this.paper.capture !== null && _this.paper.capture) {
            this.$nextTick(function () {
              this.cameraInit()
            })
          }

          if(_this.paper.paperType !== 4){
            _this.paperNoneShow = false
          }

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
    /**
     * 检查虚拟机分配
     */
    preCheck(){
      checkVmWare(this.vmQuery).then(checkResponse => {
        if (checkResponse != null && (checkResponse.code === 0 || checkResponse.code === 1)){
          this.setVmUrl();
        } else if (checkResponse != null && checkResponse.code === 2){
          let _this = this;
          this.$alert(checkResponse.message, '提示', {
            confirmButtonText: '确定',
            callback: function() {
              _this.alertFail(false);
            }
          });
        } else if (checkResponse != null && checkResponse.code === 3){
          this.$confirm(checkResponse.message, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.setVmUrl();
          }).catch(() => {
            this.alertFail(false);
          });
        }
      })
    },
    /**
     * 分配虚拟机
     */
    setVmUrl(){
      getVmWare(this.vmQuery).then(re => {
        if (re.code === 1) {
          this.lastRemoteResp = re.response
          this.wuyingMode = !!(re.response.useWuyingWebSdk && re.response.authCode)
          this.vmUrl = this.wuyingMode ? '' : (re.response.url || '')
          this.answer.vmGuid = re.response.vmGuid;
          if (re.response.status !== "00" && re.response.msg != null && re.response.msg !== '') {
            this.$message.info(re.response.msg);
          } else {
            this.showSplit();
          }
        } else {
          let _this = this;
          this.$alert(re.message, '提示', {
            confirmButtonText: '确定',
            callback: function () {
              _this.alertFail();
            }
          });
        }
      })
    },
    /**
     * 提示
     */
    alertFail(timeOut) {
      if (timeOut) {
        setTimeout(function () {
          window.close();
        }, timeOut)
      } else {
        window.close();
      }
    },
    /**
     * 左右分割插件
     */
    showSplit(){
      this.paperAnswerMonitor();
      // 倒计时
      this.timeReduce();
      this.vmHide = false
      this.paperNoneShow = false
      this.$nextTick(() => {
        Split([".paper-question-content", ".paper-vm-content"], {
          sizes: [33, 67],
          minSize: [400,1100]
        });
        if (this.wuyingMode && this.lastRemoteResp && this.lastRemoteResp.authCode) {
          startWuyingDesktopSession(this.lastRemoteResp).catch(e => {
            this.$message.error(e.message || '无影连接失败')
          })
        }
      });
    },
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
    cameraInit() {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let _this = this
      this.camera.video = document.getElementById('camera-video')
      this.camera.canvas = document.getElementById('camera-canvas')
      this.camera.context = this.camera.canvas.getContext('2d')

      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {}
      }
      if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function (constraints) {
          let getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia
          if (!getUserMedia) {
            return Promise.reject(new Error('getUserMedia is not implemented in this browser'))
          }
          return new Promise(function (resolve, reject) {
            getUserMedia.call(navigator, constraints, resolve, reject)
          })
        }
      }
      let constraints = {video: true}
      navigator.mediaDevices.getUserMedia(constraints)
          .then(function (stream) {
            if ('srcObject' in _this.camera.video) {
              _this.camera.video.srcObject = stream
            } else {
              _this.camera.video.src = window.URL.createObjectURL(stream)
            }
            _this.camera.video.onloadedmetadata = function (e) {
              _this.camera.initSuccess = true
              _this.camera.video.play()
            }
          })
          // eslint-disable-next-line handle-callback-err
          .catch(function (err) {
            _this.camera.initSuccess = false
            _this.$message.error('摄像头打开失败，请检查权限问题')
          })
    },
    cameraImageSave() {
      this.camera.context.drawImage(this.camera.video, 0, 0, 640, 480)
      let imageBase64 = this.camera.canvas.toDataURL('image/png').substring(22)
      camera({paperId: this.paper.id, imageBase64: imageBase64}).then(re => {
      })
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
            _this.$router.push('/record/index')
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

            //考试抓拍
            if (_this.paper.capture !== null && _this.paper.capture && _this.camera.initSuccess) {
              if (_this.remainTime % _this.camera.random === 0) {
                _this.cameraImageSave()
              }
            }

            //浏览器全屏
            if (_this.paper.cheat !== null && _this.paper.cheat) {
              _this.fullScreen()
            }

            // 页面缓存答案
            if (_this.remainTime % 30 === 0) {
              _this.cacheSave()
              _this.paperAnswerMonitor()
            }
          }
        }
      }, 1000)
    },
    fullScreen() {
      if (!document.fullscreen) {
        document.documentElement.requestFullscreen()
            .catch(e => {
            })
      }
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
      submit(paperEncrypt(import.meta.env.VITE_APP_PAIR_ONE_PUBLIC_KEY, this.answer)).then(re => {
        if (re.code === 1) {
          localStorage.removeItem(_this.cacheKey)
          _this.$alert(`试卷得分：${re.response}分`, '考试结果', {
            confirmButtonText: '返回考试记录',
            callback: action => {
              _this.$router.push('/record/index')
            }
          })
        } else {
          _this.$message.error(re.message)
        }
      })
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
    cancelForm() {
      window.close()
    }
  }
}
</script>
