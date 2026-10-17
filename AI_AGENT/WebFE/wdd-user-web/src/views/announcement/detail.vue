<template>
  <div class="app-item-contain">
    <div class="post-title">
      <el-tag effect="dark" type="" size="large" class="announcement-detail-tag" v-if="announcement.overhead">顶置
      </el-tag>
      <el-tag effect="dark" type="danger" size="large" class="announcement-detail-tag"
              v-if="announcement.importanted">
        重要
      </el-tag>
      {{ announcement.title }}
    </div>
    <div class="post-des">
      <span>发布者：{{ announcement.createRealName }}（{{ announcement.createUserName }}）</span>
      <span>发布时间：{{ announcement.createTime }}</span>
    </div>

    <div class="post-content">
      <div class="post-content-image">
        <img :src="announcement.imageSrc"/>
      </div>
      <div v-html="announcement.content"></div>
    </div>
  </div>
</template>

<script>

import {setPageTitle} from '@/utils'
import {select, read} from '@/api/announcement'

export default {
  data() {
    return {
      announcement: {}
    }
  },

  created() {
    let id = this.$route.query.id
    if (id && parseInt(id) !== 0) {
      select(id).then(re => {
        if (re.code === 1) {
          this.announcement = re.response
          setPageTitle(`${re.response.title}`)
          read(id).then(re => {
          })
        } else {
          this.$message.error(re.message)
        }
      }).finally(() => {

      })
    }
  }
}
</script>
