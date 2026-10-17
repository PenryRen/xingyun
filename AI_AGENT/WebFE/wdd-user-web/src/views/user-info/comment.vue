<template>
  <div class="app-item-contain">
    <el-card class="user-index-info-contain user-comment-info" shadow="never">

      <info-menu url="/user/comment"/>

      <div class="right">
        <none-show v-if="commentNoneShow"/>
        <el-card v-for="item in tableData" :key="item.id" class="user-comment-card" shadow="never">
          <div class="user-comment-content">
            <div class="forum-title">
              <router-link target="_blank" :to="{path:'/forum/select',query:{id:item.forumId}}"
                           class="wdd-link">
                {{ item.title }}
              </router-link>
            </div>
            <div class="forum-content-html comment-content reply-content" v-html="item.replyContent"
                 v-if="item.replyContent.length > 0"></div>
            <div class="forum-content-html comment-content user-comment-content"
                 v-html="item.content"></div>
            <div class="comment-button">
              <span class="comment-create-time">{{ item.createTime }}</span>
              <el-button type="danger" class="comment-cancel-button" @click="deleteComment(item.id)">删除评论
              </el-button>
            </div>
          </div>
        </el-card>

        <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                    v-model:limit="queryParam.pageSize"
                    @pagination="search"/>

      </div>
    </el-card>
  </div>
</template>

<script>
import InfoMenu from './components/info-menu.vue'
import {comment} from '@/api/user'
import {commentDelete} from '@/api/forum'

export default {
  components: {InfoMenu},
  data() {
    return {
      queryParam: {
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: true,
      tableData: [],
      total: 0,
      commentNoneShow: false
    }
  },
  created() {
    this.search()
  },
  methods: {
    search() {
      this.listLoading = true
      comment(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
        this.commentNoneShow = this.total === 0
      }).finally(() => {
        this.listLoading = false
      })
    },
    deleteComment(id) {
      this.$confirm('确定删除评论吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        commentDelete(id).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    }
  }
}
</script>
