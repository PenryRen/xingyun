<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="120px" v-loading="formLoading" :rules="formRules">
      <el-form-item label="公告分类：">
        <el-tree-select v-model="form.announcementArchiveId" :data="announcementArchiveTree"
                        check-strictly :render-after-expand="true"
                        default-expand-all placeholder="公告分类"/>
      </el-form-item>
      <el-form-item label="是否顶置：" prop="overhead">
        <el-checkbox v-model="form.overhead"></el-checkbox>
      </el-form-item>
      <el-form-item label="是否重要：" prop="importanted">
        <el-checkbox v-model="form.importanted"></el-checkbox>
      </el-form-item>
      <el-form-item label="发布班级：" prop="departmentIdList" require>
        <tree-select v-model="form.departmentIdList" :data="departmentTree" :clearable="true"
                     placeholder="发布班级" style="width: 100%"/>
      </el-form-item>
      <el-form-item label="封面：" prop="imageSrc" required>
        <el-upload accept=".jpg,.png" name="upFile" action="/api/upload/folder/image"
                   :data="{folder:'announcement'}"
                   :show-file-list="false" :on-progress="uploadProgress" :on-success="uploadImageSuccess"
                   :on-error="uploadError">
          <el-image style="width: 288px; height: 176px" :src="form.imageSrc" fit="fill" v-if="imgRender">
            <template #error>
              <div class="image-slot">
                <el-icon>
                  <icon-picture/>
                </el-icon>
              </div>
            </template>
          </el-image>
        </el-upload>
      </el-form-item>
      <el-form-item label="标题：" prop="title" required>
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item label="详细内容：" prop="content" class="form-item-content-block" required>
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

import {Picture as IconPicture} from '@element-plus/icons-vue'
import Editor from '@/components/Editor/index.vue'
import {select, edit} from '@/api/announcement'
import {tree as departmentTree} from '@/api/department'
import {tree as archiveTree} from '@/api/announcementArchive'
import useStore from '@/store'

const {tagsView} = useStore()

export default {
  name: 'AnnouncementEdit',
  components: {
    Editor, IconPicture
  },
  data() {
    return {
      form: {
        id: null,
        title: null,
        imageSrc: null,
        content: null,
        departmentIdList: [],
        announcementArchiveId: null,
        overhead: false,
        importanted: false
      },
      imgRender: false,
      formRules: {
        title: [{required: true, message: '请输入公告标题'}, {
          max: 255,
          message: '长度小于255个字符'
        }],
        content: [{required: true, message: '请输入公告详细内容'}],
        imageSrc: [{required: true, message: '请上传公告封面'}],
        departmentIdList: [{required: true, message: '班级不能为空'}]
      },
      formLoading: false,
      departmentTree: [],
      announcementArchiveTree: []
    }
  },
  created() {
    let id = this.$route.query.id

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id).then(re => {
        this.form = re.response
        this.imgRender = true
      }).finally(() => {
        this.formLoading = false
      })
    } else {
      this.imgRender = true
    }

    departmentTree().then(re => {
      this.departmentTree = re.response
    })

    archiveTree().then(re => {
      this.announcementArchiveTree = re.response
    })
  },
  methods: {
    changeEditor() {
    },
    uploadProgress() {
      this.loading = this.$loading({
        lock: true,
        text: '文件上传中…',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
    },
    uploadImageSuccess(re, file) {
      this.loading.close()
      this.form.imageSrc = re.url
    },
    uploadError() {
      this.loading.close()
      this.$message.error('文件上传失败，请检查文件大小或文件格式')
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          edit(this.form).then(data => {
            if (data.code === 1) {
              this.$message.success(data.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/announcement/list')
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
        title: null,
        imageSrc: null,
        content: null,
        departmentIdList: [],
        announcementArchiveId: null,
        overhead: false,
        importanted: false
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

<style lang="scss">
.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 30px;
}
</style>
