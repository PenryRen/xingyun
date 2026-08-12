<template>
  <div>
    <div>
      <img src="@/assets/carousel/banner.png" class="carousel-img">
    </div>

    <div class="app-item-contain">
      <el-card class="index-paper-show" shadow="never">
        <span class="index-title-h3" style="background-color: #d9a287">通知公告</span>
        <div class="announcement-index-card-contain" v-loading="announcementListLoading">
          <none-show customer-style="no-data-layout-index" v-if="announcementNoneShow"/>
          <div v-for="(item, index) in announcementTableData" :key="index">
            <div class="announcement-index-contain">
              <img :src="item.imageSrc" class="announcement-image">
              <div class="announcement-title-contain">
                <div class="announcement-title">
                  <router-link :to="{path:'/announcement/detail',query:{id:item.id}}"
                               class="announcement-link">
                    <el-tag effect="dark" type="" class="announcement-tag" v-if="item.overhead">顶置
                    </el-tag>
                    <el-tag effect="dark" type="danger" class="announcement-tag"
                            v-if="item.importanted">
                      重要
                    </el-tag>
                    {{ item.title }}
                  </router-link>
                </div>
                <div class="announcement-time">发布时间：{{ item.createTime }}</div>
              </div>
            </div>
            <div class="line" v-if="index !== announcementTableData.length-1"></div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 左下角的 AI 小人 -->
    <!-- <section class="robot" @click="visible = !visible">
      <img src="../../assets/robot.png" alt="robot">
    </section> -->
    <!-- 弹窗 -->
    <!-- <el-dialog 
      v-model="visible" title="AI 问答" top="10vh" width="1200"
    >
      <div aspect-ratio="16/9">
        <div aspect-ratio-content>
          <iframe 
            src="http://192.168.1.210:19001/"
            frameborder="0" 
            allowfullscreen
          ></iframe>
        </div>
      </div>
    </el-dialog> -->
  </div>
</template>

<script>
import { page as announcementPage } from '@/api/announcement'

export default {
  data() {
    return {
      announcementListLoading: true,
      announcementTableData: [],
      announcementNoneShow: false,
      visible: false
    }
  },
  created() {
    this.announcementLoad()
  },
  methods: {
    announcementLoad() {
      this.announcementListLoading = true
      announcementPage({pageIndex: 1, pageSize: 5}).then(data => {
        const re = data.response
        this.announcementTableData = re.list
        this.announcementNoneShow = re.total === 0
      }).finally(() => {
        this.announcementListLoading = false
      })
    }
  }
}
</script>

<style scoped>
/* ::v-deep .el-dialog__header {
  margin-right: 0px; padding: 15px 20px;
  background-color: #68BFD6;
}

::v-deep .el-dialog__title {
  color: #FFFFFF;
}

::v-deep .el-dialog__headerbtn {
  top: 3px;
}

::v-deep .el-dialog__headerbtn .el-dialog__close {
  color: #FFFFFF;
}

::v-deep .el-dialog__body { padding: 20px; } */

/* .robot {
  position: fixed; left: 30px; bottom: 30px; cursor: pointer;
}

.robot img { width: 100px; } */

/* [aspect-ratio] {
  position: relative; width: 100%;
}

[aspect-ratio]::before {
  content: ""; display: block; margin-left: -1px; width: 1px; height: 0;
}

[aspect-ratio-content] {
  position: absolute; top: 0; left: 0; right: 0; bottom: 0;
}

[aspect-ratio="16/9"]::before { padding-top: 56.25%; }

[aspect-ratio-content] iframe { width: 100%; height: 100%; } */
</style>