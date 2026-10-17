<template>
  <div class="forum-comment-item"
       :style="{
        'border-top': comment.parentId !== null && first ? '1px solid #E8E8E9' : '0px',
        'border-bottom': comment.parentId !== null && !last ? '1px solid #E8E8E9' : '0px',
        'border-left': comment.parentId !== null  ? '1px solid #E8E8E9' : '0px'
        }">
    <div class="comment-item-left">
      <el-avatar :src="comment.imagePath" shape="circle" fit="scale-down" :size="50">暂无</el-avatar>
    </div>
    <div class="comment-item-right">
      <div class="comment-user-name">{{ comment.realName }}（{{ comment.userName }}）</div>
      <div class="comment-reply-time">
        <el-button type="success" :icon="Position" size="small" class="reply-button" @click="replyClick">回复
        </el-button>
        <el-button type="danger" :icon="Delete" v-if="comment.createByMe" class="reply-button"
                   size="small" @click="deleteClick(comment)">删除
        </el-button>
        <span class="create-time">
          {{ comment.createTime }}
        </span>
      </div>
      <div v-html="comment.content" class="forum-content-html forum-comment-child-content"></div>
      <div v-if="commentShow" class="comment-reply">
        <editor v-model="replyContent" :zIndex="1"></editor>
        <div class="comment-reply-button">
          <el-button type="primary" class="wdd-primary-button-color" @click="commentAddClick">提交评论
          </el-button>
          <el-button class="wdd-primary-button" @click="commentCancel">取消回复</el-button>
        </div>
      </div>
      <div class="comment-item-child">
        <div v-for="(item,index) in comment.child" :key="item.id">
          <CommentShow :comment="item" :reload="reload" :first="index === 0"
                       :last="(index+1) === comment.child.length"/>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import {reactive, toRefs} from 'vue';
import {Position, Delete} from '@element-plus/icons-vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import Editor from '@/components/Editor/forum.vue'
import {clearTagP} from '@/utils'
import {commentAdd, commentDelete} from '@/api/forum'


const props = defineProps({
  comment: {
    type: Object,
    default: function () {
      return {}
    }
  },
  reload: {
    type: Function,
    default: function () {

    }
  },
  first: {
    type: Boolean,
    default: false
  },
  last: {
    type: Boolean,
    default: false
  }
});

const state = reactive({
  commentShow: false,
  replyContent: null
});

const {
  commentShow, replyContent
} = toRefs(state);

function replyClick() {
  state.commentShow = true
}

function commentAddClick() {
  let form = {
    forumArchiveId: props.comment.forumArchiveId,
    forumId: props.comment.forumId,
    content: clearTagP(state.replyContent),
    parentId: props.comment.id
  }
  commentAdd(form).then(re => {
    state.commentShow = false
    state.replyContent = null
    props.reload(false)
    ElMessage.success('提交评论成功！')
  })
}

function deleteClick(comment) {
  ElMessageBox.confirm('确定删除评论吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    commentDelete(comment.id).then(re => {
      if (re.code === 1) {
        props.reload(false)
        ElMessage.success(re.message)
      } else {
        ElMessage.error(re.message)
      }
    })
  })
}

function commentCancel() {
  state.commentShow = false
}


</script>
