<template>
  <div class="app-item-contain">
    <el-card class="forum-detail" shadow="never">
      <div class="forum-title">{{ forum.title }}</div>
      <div class="forum-info">
        <div class="forum-info-left">
          <el-avatar :src="forum.imagePath" shape="circle" fit="scale-down" :size="24">无</el-avatar>
          <span>{{ forum.realName }}（{{ forum.userName }}）</span>
          <span>分类：{{ forum.forumArchive }}</span>
        </div>
        <div>
          <span>时间：{{ forum.createTime }}</span>
        </div>
      </div>
      <div v-html="forum.content" class="forum-content-detail forum-content-html"></div>
    </el-card>

    <el-card class="forum-comment" shadow="never">
      <editor v-model="comment.content" :zIndex="1"></editor>
      <div class="comment-submit">
        <el-button type="primary" class="wdd-primary-button-color" @click="commentAdd">提交评论</el-button>
      </div>
      <div>
        <div class="comment-total" id="comment-total">总共已有 {{ total }} 条评论</div>
        <div>
          <none-show v-if="forumCommentNoneShow"/>
          <div>
            <comment-show v-for="item in tableData" :comment="item" :key="item.id"
                          :first="false" :last="false" :reload="search" style="margin-bottom: 20px"/>
            <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                        v-model:limit="queryParam.pageSize"
                        @pagination="search" :autoScroll="false"/>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import Editor from '@/components/Editor/forum.vue'
import CommentShow from './components/comment-show.vue'
import {select, commentAdd, commentPage} from '@/api/forum'
import {clearTagP} from '@/utils'

export default {
  components: {
    CommentShow,
    Editor
  },
  data() {
    return {
      forum: {},
      comment: {
        parentId: null,
        forumId: null,
        forumArchiveId: null,
        content: null
      },
      formLoading: false,
      forumCommentNoneShow: false,
      queryParam: {
        forumId: null,
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      total: 0
    }
  },
  mounted() {
    let id = this.$route.query.id
    if (id && parseInt(id) !== 0) {
      this.comment.forumId = id
      this.queryParam.forumId = id
      this.search(false)
      select(id).then(re => {
        this.forum = re.response
        this.comment.forumArchiveId = this.forum.forumArchiveId
      })
    }
  },
  methods: {
    commentAdd() {
      this.comment.content = clearTagP(this.comment.content)
      commentAdd(this.comment).then(re => {
        this.comment.content = ''
        this.queryParam.pageIndex = 1
        this.search(true)
        this.$message.success('提交评论成功！')
      })
    },
    search(reset) {
      if (reset) {
        document.querySelector('#comment-total').scrollIntoView(true)
      }
      this.listLoading = true
      commentPage(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
        this.forumCommentNoneShow = this.total === 0
      }).finally(() => {
        this.listLoading = false
      })
    }
  }
}
</script>
