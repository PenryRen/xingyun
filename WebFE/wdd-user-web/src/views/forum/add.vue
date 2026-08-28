<template>
  <div class="app-item-contain">
    <el-card class="forum-add">
      <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
        <el-form-item label="分类：" class="tree-select-contain" prop="forumArchiveId">
          <el-tree-select v-model="form.forumArchiveId" :data="forumArchiveTree" check-strictly
                          :props="defaultProps"
                          :render-after-expand="true" default-expand-all placeholder="文章分类"
                          style="width: 100%"/>
        </el-form-item>
        <el-form-item label="标题：" prop="title" required>
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="内容：" prop="content" class="form-item-content-block" required>
          <editor v-model="form.content" :zIndex="1" @input="handleContentChange"></editor>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm" class="wdd-primary-button-color">提交</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import Editor from '@/components/Editor/forum.vue'
import {tree, add} from '@/api/forum'

export default {
  components: {Editor},
  data() {
    return {
      defaultProps: {
        value: 'id',
        children: 'child',
        label: 'name'
      },
      form: {
        forumArchiveId: null,
        title: null,
        content: null
      },
      formLoading: false,
      rules: {
        title: [{required: true, message: '请输入文章标题'}],
        content: [{required: true, message: '请输入文章内容'}]
      },
      forumArchiveTree: []
    }
  },
  created() {
    tree().then(re => {
      this.forumArchiveTree = re.response
    })
  },
  methods: {
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          add(this.form).then(data => {
            if (data.code === 1) {
              this.$message.success(data.message)
              this.$router.push({path: '/forum/index'})
            } else {
              this.$message.error(data.message)
            }
          }).finally(() => {
            this.formLoading = false
          })
        } else {
          return false
        }
      })
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
