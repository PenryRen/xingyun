<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="120px" v-loading="formLoading" :rules="formRules">
      <el-form-item label="文章分类：">
        <el-tree-select v-model="form.forumArchiveId" :data="forumArchiveTree"
                        check-strictly :render-after-expand="true" default-expand-all placeholder="文章分类"/>
      </el-form-item>
      <el-form-item label="标题：" prop="title" required>
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item label="内容：" prop="content" class="form-item-content-block" required>
        <editor v-model="form.content" :zIndex="1" @input="handleContentChange"></editor>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import {select, edit} from '@/api/forum'
import {tree} from '@/api/forumArchive'
import Editor from '@/components/Editor/index.vue'
import useStore from '@/store'

const {tagsView} = useStore()

export default {
  name: 'ForumEdit',
  components: {
    Editor
  },
  data() {
    return {
      form: {
        id: null,
        forumArchiveId: null,
        title: null,
        content: null
      },
      formRules: {
        title: [{required: true, message: '请输入文章标题'}, {
          max: 255,
          message: '长度小于255个字符'
        }],
        content: [{required: true, message: '请输入文章详细内容'}]
      },
      formLoading: false,
      forumArchiveTree: []
    }
  },
  created() {
    let id = this.$route.query.id

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id).then(re => {
        this.form = re.response
      }).finally(() => {
        this.formLoading = false
      })
    }

    tree().then(re => {
      this.forumArchiveTree = re.response
    })
  },
  methods: {
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          edit(this.form).then(data => {
            if (data.code === 1) {
              this.$message.success(data.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/forum/list')
              })
            } else {
              this.$message.error(data.message)
            }
          }).finally(() => {
            this.formLoading = false
          })
        }
      })
    },
    resetForm() {
      this.form = {
        id: null,
        forumArchiveId: null,
        title: null,
        content: null
      }
      this.$refs['form'].resetFields()
    },
    handleContentChange() {
      const that = this;
      setTimeout(function() {
        that.$refs.form.validateField('content').then(function (valid) {}).catch(error => {});
      }, 300);
    }
  }
}
</script>
