<template>
  <div class="app-item-contain exam-paper-contain">

    <div class="paper-index-header">
      <div class="item-contain">
        <span class="title">试卷类型：</span>
        <div class="item-radio">
          <el-radio-group v-model="queryParam.paperType">
            <el-radio v-for="item in EnumMap.exam.paperTypeEnum" :key="item.key" :label="item.key"
                      @click.prevent="paperTypeChange(item.key)">{{ item.value }}
            </el-radio>
          </el-radio-group>
        </div>
      </div>
      <div class="item-contain">
        <span class="title">考试结果：</span>
        <div class="item-radio">
          <el-radio-group v-model="queryParam.passed">
            <el-radio v-for="item in EnumMap.exam.passed" :key="item.key" :label="item.key"
                      @click.prevent="passedChange(item.key)">{{ item.value }}
            </el-radio>
          </el-radio-group>
        </div>
      </div>
      <div class="item-contain">
        <span class="title">试卷分类：</span>
        <div class="item-radio">
          <el-radio-group v-model="queryParam.examPaperArchiveId">
            <el-radio v-for="item in examPaperArchiveList" :key="item.id" :label="item.id"
                      @click.prevent="examPaperArchiveChange(item.id)">{{ item.name }}
            </el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>

    <div class="answer-card-contain" v-loading="listLoading">
      <none-show v-if="recordNoneShow"/>
      <el-card v-for="item in tableData" :key="item.id" class="answer-card" shadow="never">
        <div class="paperTitleInfo">
          <div class="title">{{ item.paperName }}</div>
          <div>提交时间：{{ item.createTime }}</div>
          <div class="record-result">考试结果：
            <el-tag type="success" v-if="item.passed !== null && item.passed">合格</el-tag>
            <el-tag type="warning" v-else-if="item.status === 1">待批改</el-tag>
            <el-tag type="warning" v-else-if="item.status === 3">待核验</el-tag>
            <el-tag type="warning" v-else-if="item.status === 4">待核验</el-tag>
            <el-tag type="danger" v-else-if="item.passed !== null && !item.passed">不合格</el-tag>
          </div>
        </div>
        <div class="paperInfo">
          <div>正确题数：{{ item.questionCorrect }}</div>
          <div>题目数量：{{ item.questionCount }}</div>
          <div>合格分：{{ item.passScore }}</div>
          <div>试卷总分：{{ item.paperScore }}</div>
        </div>
        <div class="answerInfo">
          <div>自动批改：{{ item.systemScore }}</div>
          <div>最终得分：{{ item.userScore }}</div>
          <div>耗时：{{ item.doTime }}</div>
          <div>试卷状态：{{ item.statusStr }}</div>
        </div>
        <div class="tag">
          <div class="tag-top">
            <el-tag type="info" v-for="(tagItem,index) in item.examPaperTagList" :key="index">
              {{ tagItem.tagName }} x
              {{ tagItem.groupCount }}
            </el-tag>
          </div>
          <div class="tag-bottom">
            <div v-if="queryParam.paperType === 1 && item.watch">
              <router-link target="_blank" :to="{path:'/read',query:{id:item.id}}"
                           class="wdd-link-button">
                <el-button type="primary" class="btn-link">查看试卷</el-button>
              </router-link>
            </div>
            <div v-else-if="queryParam.paperType === 3">
              <router-link target="_blank" :to="{path:'/practice/paper/edit',query:{id:item.id}}"
                           class="wdd-link-button" v-if="item.status === 1">
                <el-button type="primary" class="btn-link">去批改</el-button>
              </router-link>
              <router-link target="_blank" :to="{path:'/practice/paper/read',query:{id:item.id}}"
                           class="wdd-link-button" v-else-if="item.status === 2">
                <el-button type="primary" class="btn-link">查看试卷</el-button>
              </router-link>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search"/>

  </div>

</template>

<script>

import {EnumMap} from '@/api/EnumMap'
import {page as paperPage} from '@/api/examPaperAnswer'
import {answerPage as practicePage} from '@/api/practice'
import {list} from '@/api/examPaper'

export default {
  data() {
    return {
      EnumMap: EnumMap,
      queryParam: {
        paperType: 1,
        passed: null,
        examPaperArchiveId: null,
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: false,
      tableData: [],
      total: 0,
      examPaperArchiveList: [],
      recordNoneShow: false
    }
  },
  created() {
    list().then(re => {
      this.examPaperArchiveList = re.response
    })
    this.search()
  },
  methods: {
    paperTypeChange(id) {
      if (id !== this.queryParam.paperType) {
        this.queryParam.paperType = id
        this.search()
      }
    },
    passedChange(id) {
      if (id === this.queryParam.passed) {
        this.queryParam.passed = null
      } else {
        this.queryParam.passed = id
      }
      this.search()
    },
    examPaperArchiveChange(id) {
      if (id === this.queryParam.examPaperArchiveId) {
        this.queryParam.examPaperArchiveId = null
      } else {
        this.queryParam.examPaperArchiveId = id
      }
      this.search()
    },
    search() {
      this.listLoading = true
      let page
      if (this.queryParam.paperType === 1) {
        page = paperPage
      } else if (this.queryParam.paperType === 3) {
        page = practicePage
      }
      page(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.recordNoneShow = this.total === 0
        this.queryParam.pageIndex = re.pageNum
      }).finally(() => {
        this.listLoading = false
      })
    }
  }
}
</script>
