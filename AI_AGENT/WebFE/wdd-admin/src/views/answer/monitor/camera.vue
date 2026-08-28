<template>
  <div class="app-container">
    <div class="wdd-show-row">
      <el-card class="wdd-show-col">
        <p><span>试卷名称：</span><span>{{ info.name }}</span></p>
        <p><span>组卷类型：</span><span>{{ info.buildTypeStr }}</span></p>
        <p><span>试卷分类：</span><span>{{ info.examPaperArchiveStr }}</span></p>
        <p><span>考试时间：</span><span>{{ info.limitStartTime }} 至 {{ info.limitEndTime }}</span></p>
        <p><span>考试时长：</span><span>{{ info.suggestTimeStr }}</span></p>
        <p><span>题目总数：</span><span>{{ info.questionCount }}</span></p>
      </el-card>
      <el-card class="wdd-show-col">
        <p><span>答卷人：</span><span>{{ info.answerUserName }}</span></p>
        <p><span>得分：</span><span>{{ info.userScore }} / {{ info.score }}</span></p>
        <p><span>作弊次数：</span><span>{{ info.cheatCount }}</span></p>
        <p><span>耗时：</span><span>{{ info.doTime }}</span></p>
        <p><span>创建时间：</span><span>{{ info.createTime }}</span></p>
        <p><span>更新时间：</span><span>{{ info.updateTime }}</span></p>
      </el-card>
    </div>

    <div class="analyze-paper-top">
      <el-card header="抓拍相册">
        <el-image class="paper-camera" :src="item" :preview-src-list="info.imageList" :preview-teleported="true"
                  :hide-on-click-modal="true"
                  v-for="(item,index) in info.imageList" :initial-index="index" :key="index"></el-image>
      </el-card>
    </div>

  </div>
</template>

<script>

import {monitorCamera} from '@/api/examPaperAnswer'

export default {
  name: 'ExamPaperMonitorCamera',
  data() {
    return {
      info: {}
    }
  },
  created() {
    let id = this.$route.query.id
    if (id && parseInt(id) !== 0) {
      monitorCamera(id).then(re => {
        if (re.code === 1) {
          this.info = re.response
        } else {
          this.$message.error(re.message)
        }
      })
    }
  }
}
</script>
