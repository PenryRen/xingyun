<template>
  <div class="vm-train-container">
    <div class="vm-train-pdf">
      <iframe :src="pdf.previewPath" frameborder="0" style="width: 100%; height: 100%"></iframe>
    </div>
    <div class="vm-train-vm">
      <div class="vm-train-right-box">
        <div class="vm-train-right-box-top">
          <el-button-group>
            <el-button type="primary" text v-show="!expired">
              <el-icon :size="20">
                <Timer/>
              </el-icon>
              <span>剩余时间：{{ formatSeconds(remainTime)}}</span>
            </el-button>
            <el-button type="danger" text v-show="expired">
              <el-icon :size="20">
                <Timer/>
              </el-icon>
              <span>实训机器已到期，请及时续期，否则将在20分钟内回收</span>
            </el-button>
            <el-button type="primary" text @click="handleQuestionDetail">
              <el-icon :size="20">
                <CircleCheck/>
              </el-icon>
              <span>任务详情</span>
            </el-button>
            <el-button type="primary" text @click="renewal">
              <el-icon :size="20">
                <Tickets/>
              </el-icon>
              <span>续期</span>
            </el-button>
          </el-button-group>
          <el-dialog v-model="showDialog" width="600" title="任务详情" class="question-select-dialog">
            <div class="question-select-dialog-top">
              <el-button v-if="!checkDisabled" type="primary" text @click="handleCheckQuestion">
                <el-icon :size="20">
                  <VideoPlay/>
                </el-icon>
                <span>检测任务</span>
              </el-button>
              <el-button v-if="checkDisabled" type="info" disabled text>
                <el-icon :size="20">
                  <VideoPlay/>
                </el-icon>
                <span>{{disabledSecond}}秒后可再次检测</span>
              </el-button>
            </div>
            <el-table :data="tableData" style="width: 100%">
              <el-table-column prop="title" label="任务名称">
              </el-table-column>
              <el-table-column prop="completion" label="完成状态" align="right">
                <template #default="{row}">
                  <el-tag v-if="row.completion === '未完成'" type="info">未完成</el-tag>
                  <el-icon v-else :size="20" style="color:var(--el-color-success)">
                    <CircleCheck/>
                  </el-icon>
                </template>
              </el-table-column>
            </el-table>
          </el-dialog>
        </div>
        <div class="vm-train-right-box-bottom">
          <iframe :src="vmUrl" id="myIframe" ref="myIframe" style="width: 100%; height: 100%"></iframe>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
  #app {
    overflow-y: hidden;
  }

  .vm-train-container {
    display: flex;
    width: 100%;
    height: 100%;
    flex: 1;
  }

  .vm-train-pdf {

  }

  .vm-train-vm {

  }

  .vm-train-right-box {
    display: flex;
    flex-direction: column;
    height: 100%;
  }

  .vm-train-right-box-top {
    width: 100%;
    display: flex;
    justify-content: flex-end;
  }

  .vm-train-right-box-bottom {
    width: 100%;
    height: 100%;
    flex: 1;
  }

  .question-select-dialog {
    min-height: 50%;
  }

  .question-select-dialog-top {
    display: flex;
    justify-content: flex-end;
  }

  .el-dialog__headerbtn {
    width: 48px;
    height: 48px;
    top: 0;
  }

  .el-dialog__body {
    padding: 0 16px 16px 16px;
  }
</style>

<script>
  import QuestionDo from '../exam/components/QuestionDo.vue'
  import QuestionRead from '../exam/components/QuestionRead.vue'
  import {select, watch} from '@/api/courseWare'
  import {paperDecrypt, setPageTitle, formatSeconds} from '@/utils/index'
  import {checkVmWare, getVmWare, checkTrain, queryRemainingTime, renewal} from '@/api/vmWare'
  import {userQuestionList} from '@/api/trainItemUserQuestion'
  import {CircleCheck, Timer, Tickets, VideoPlay} from '@element-plus/icons-vue'
  import Split from 'split.js'

  export default {
    components: {QuestionDo, QuestionRead, CircleCheck, Timer, Tickets, VideoPlay},
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
          vmInterval: null,
          second: 0,
          start: true
        },
        questionDialogShow: false,
        vmQuery: {
          vmParentId: "",
          classes: "00",
          paperId: null,
        },
        questionQuery: {
          trainId: null,
          courseWareId: null
        },
        vmUrl: '',
        tableData: [],
        showDialog: false,
        remainTime: 0,
        singleSecond: 0,
        timer: null,
        stopInterval: false,
        expired: false,
        disabledTimer: null,
        stopDisabledInterval: false,
        checkDisabled: false,
        disabledSecond: 60,
      }
    },
    created() {
      this.id = this.$route.query.cId
      this.uId = this.$route.query.uId
      this.questionQuery.courseWareId = this.$route.query.cId
      this.questionQuery.trainId = this.$route.query.tId
      if (this.id && parseInt(this.id) !== 0) {
        select(this.id).then(re => {
          let response = paperDecrypt(import.meta.env.VITE_APP_PAIR_TWO_PRIVATE_KEY, re.message, re.response)
          this.pdf = response
          setPageTitle(`${response.name} | 课件观看`)
          this.vmQuery.vmParentId = response.vmType;
          this.setQuestionList(response.courseWareQuestionVMList)
          this.preCheck();
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
        }
      }, 1000)
      clearInterval(this.timeUpdate.vmInterval)
      this.timeUpdate.vmInterval = setInterval(function () {
        _this.checkQuestionList();
        _this.getQuestionList();
      }, 60 * 1000)
    },
    beforeUnmount() {
      if (this.timeUpdate.documentInterval) {
        clearInterval(this.timeUpdate.documentInterval)
        clearInterval(this.timeUpdate.vmInterval)
        this.timeUpdate.documentInterval = null
        this.timeUpdate.vmInterval = null
      }
      this.stopAll();
      document.title = `${import.meta.env.VITE_APP_SYSTEM_NAME}`;
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
      /**
       * 检查虚拟机分配
       */
      preCheck() {
        var trainVmCheck = localStorage.getItem("trainVmCheck");
        if (trainVmCheck !== undefined && trainVmCheck === "true") {
          this.setVmUrl();
          localStorage.removeItem("trainVmCheck")
        } else {
          checkVmWare(this.vmQuery).then(checkResponse => {
            if (checkResponse != null && (checkResponse.code === 0 || checkResponse.code === 1)) {
              this.setVmUrl();
            } else if (checkResponse != null && checkResponse.code === 2) {
              let _this = this;
              this.$alert(checkResponse.message, '提示', {
                confirmButtonText: '确定',
                callback: function () {
                  _this.alertFail();
                }
              });
            } else if (checkResponse != null && checkResponse.code === 3) {
              this.$confirm(checkResponse.message, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                this.setVmUrl();
              }).catch(() => {
                this.alertFail();
              });
            }
          })
        }
      },
      /**
       * 分配虚拟机
       */
      setVmUrl() {
        getVmWare(this.vmQuery).then(re => {
          if (re.code === 1) {
            this.vmUrl = re.response.url;
            this.vmQuery.vmGuid = re.response.vmGuid;
            if (re.response.status !== "00" && re.response.msg != null && re.response.msg !== '') {
              this.$confirm(re.response.msg, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                this.showSplit();
              }).catch(() => {
                this.alertFail(false);
              });
            } else {
              this.showSplit();
            }
          } else {
            this.showSplit();
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
       * 查询虚拟机剩余时间(实训)
       */
      getRemainTime() {
        queryRemainingTime(this.vmQuery).then(timeRe => {
          this.remainTime = timeRe.response.remainTime;
          this.singleSecond = timeRe.response.singleSecond;
          this.expired = timeRe.response.remainTime === 0;
          this.stopInterval = false;
          // 倒计时
          this.timeReduce()
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
      showSplit() {
        this.$nextTick(() => {
          Split([".vm-train-pdf", ".vm-train-vm"], {
            sizes: [45, 55],
            minSize: [600, 1040],
          });
        });
        this.getRemainTime();
        this.getQuestionList();
      },
      handleCheckQuestion() {
        checkTrain(this.questionQuery).then(re => {
          this.$message.success(re.message);
          this.checkDisabled = true;
          this.stopDisabledInterval = false;
          this.timeOutCheckDisabled();
        })
      },
      /**
       * 核验功能点是否完成
       */
      checkQuestionList() {
        checkTrain(this.questionQuery);
      },
      /**
       * 设置功能点列表
       */
      setQuestionList(courseWareQuestionVMList) {
        for (let i = 0; i < courseWareQuestionVMList.length; i++) {
          let questionFrame = courseWareQuestionVMList[i].questionFrame
          const question = {
            questionId: questionFrame.questionId,
            title: questionFrame.title,
            completion: "未完成"
          }
          this.tableData.push(question);
        }
      },
      /**
       * 获取功能点完成情况
       */
      getQuestionList() {
        userQuestionList(this.questionQuery).then(re => {
          if (re.code === 1) {
            if (re.response != null && re.response.length > 0) {
              const questionList = re.response;
              const oldTableData = this.tableData;
              for (let i = 0; i < questionList.length; i++) {
                const question = questionList[i];
                for (let j = 0; j < oldTableData.length; j++) {
                  const item = oldTableData[j];
                  if (question.questionId === item.questionId && question.completion) {
                    this.tableData[j].completion = '已完成';
                    break;
                  }
                }
              }
            }
          }
        })
      },
      formatSeconds(theTime) {
        return formatSeconds(theTime)
      },
      /**
       * 倒计时
       */
      timeReduce() {
        // eslint-disable-next-line @typescript-eslint/no-this-alias
        let _this = this
        clearInterval(this.timer)
        this.timer = setInterval(function () {
          if (!_this.stopInterval) {
            if (_this.remainTime <= 0) {
              _this.stopAll();
              _this.expired = true;
            } else {
              --_this.remainTime
            }
          }
        }, 1000)
      },
      /**
       * 清空倒计时
       */
      stopAll() {
        this.stopInterval = true;
        clearInterval(this.timer);
        this.timer = null;
      },
      timeOutCheckDisabled() {
        let _this = this;
        clearInterval(this.disabledTimer);
        this.disabledTimer = setInterval(function () {
          if (!_this.stopDisabledInterval) {
            if (_this.disabledSecond <= 1) {
              _this.stopCheckDisabled();
              _this.checkDisabled = false;
            } else {
              --_this.disabledSecond;
            }
          }
        }, 1000);
      },
      stopCheckDisabled() {
        this.stopDisabledInterval = true;
        clearInterval(this.disabledTimer);
        this.disabledTimer = null;
        this.disabledSecond = 60;
      },
      handleQuestionDetail() {
        this.showDialog = true
      },
      /**
       * 虚拟机续期
       */
      renewal() {
        renewal(this.vmQuery).then(re => {
          if (re.code === 1) {
            //成功
            this.$message.success(re.message);
            this.getRemainTime();
          } else if (re.code === 3) {
            //当前实训机器已回收，是否重新申请资源
            this.$confirm(re.message, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.preCheck();
            }).catch(() => {
              this.alertFail();
            });
          } else {
            //当前剩余时间充足，请勿频繁续期
            this.$message.warning(re.message);
          }
        })
      },
    }
  }
</script>
