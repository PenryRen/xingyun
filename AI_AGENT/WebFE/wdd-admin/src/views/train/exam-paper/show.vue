<template>
  <div class="wdd-paper-contain">
    <div class="paper-item-order-fake">
      <div class="paper-item-order">
        <el-card shadow="never">
          <div class="paper-item-order-content">
            <div v-for="(tItem,tIndex) in paper.titleItems" :key="tIndex">
              <div class="wdd-anchor-title">{{ tItem.name }}</div>
              <div class="wdd-question-anchor">
                                <span :key="qItem.itemOrder" v-for="qItem in tItem.questionItems">
                                <el-tag class="do-exam-title-tag" type="info"
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
          <div :key="index" v-for="(titleItem,index) in paper.titleItems">
            <div class="wdd-exam-question-content wdd-paper-title">{{ titleItem.name }}</div>
            <div class="exam-paper-item-box" v-if="titleItem.questionItems.length!==0">
              <div :key="questionItem.itemOrder"
                   v-for="(questionItem,qIndex) in titleItem.questionItems"
                   :class="qIndex != (titleItem.questionItems.length - 1) ? 'exam-paper-item-box-contain  exam-paper-item-box-contain-line': 'exam-paper-item-box-contain'"
                   :id="`question-${questionItem.itemOrder}`">
                <QuestionRead :itemOrder="`${questionItem.itemOrder}`"
                              :qType="questionItem.questionType"
                              :question="questionItem"/>
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
              <div>总分：{{ paper.score }}</div>
              <div>时长：{{ paper.suggestTime }} 分钟</div>
              <div>题目打乱：{{ paper.questionMess ? '是' : '否' }}</div>
              <div>选项打乱：{{ paper.questionItemMess ? '是' : '否' }}</div>
              <div>防作弊：{{ paper.cheat ? '是' : '否' }}</div>
              <div>防作弊次数：{{ paper.maxCheatCount }}</div>
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
import QuestionRead from './components/QuestionRead.vue'
import {select} from '@/api/trainExamPaper'

export default {
  name: 'TrainExamPaperShow',
  components: {QuestionRead},
  data() {
    return {
      paper: {},
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
      select(id).then(re => {
        if (re.code === 1) {
          let response = re.response
          this.paper = response
          setPageTitle(`${this.paper.name} | 试卷查看`)
        } else {
          this.$message.error(re.message)
        }
      }).finally(() => {
        this.formLoading = false
      })
    }
  },
  methods: {
    goAnchor(selector) {
      this.$el.querySelector(selector).scrollIntoView({behavior: 'instant', block: 'center', inline: 'nearest'})
    }
  }
}
</script>
