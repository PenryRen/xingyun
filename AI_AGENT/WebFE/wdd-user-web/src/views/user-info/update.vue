<template>
  <div class="app-item-contain">
    <el-card class="user-index-info-contain user-update-info" shadow="never">

      <info-menu url="/user/update"/>

      <div class="center">
        <el-upload class="upload" action="/api/upload/folder/file" accept=".jpg,.png" :data="{folder:'profile'}"
                   :show-file-list="false" :on-progress="uploadProgress" :on-success="uploadSuccess"
                   :on-error="uploadError">
          <el-avatar :size="100" :src="form.imagePath" shape="circle" fit="scale-down" class="user-image">暂无头像
          </el-avatar>
        </el-upload>
      </div>
      <div class="right">
        <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
          <el-form-item label="真实姓名：" prop="realName" required>
            <el-input v-model="form.realName" :disabled="disable.realName"></el-input>
          </el-form-item>
          <el-form-item label="工号：" prop="workNo" required>
            <el-input v-model="form.workNo" :disabled="disable.workNo"></el-input>
          </el-form-item>
          <el-form-item label="职位：" prop="jobTitle">
            <el-input v-model="form.jobTitle" :disabled="disable.jobTitle"></el-input>
          </el-form-item>
          <el-form-item label="身份证号：" prop="idCard" required>
            <el-input v-model="form.idCard" :disabled="disable.idCard"></el-input>
          </el-form-item>
          <el-form-item label="邮箱：" prop="email" required>
            <el-input v-model="form.email"></el-input>
          </el-form-item>
          <el-form-item label="手机号：" prop="phone" required>
            <el-input v-model="form.phone"></el-input>
          </el-form-item>
          <el-form-item label="性别：">
            <el-select v-model="form.sex" placeholder="性别" clearable>
              <el-option v-for="item in EnumMap.user.sexEnum" :key="item.key" :value="item.key"
                         :label="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="年龄：" prop="age">
            <el-input v-model.number="form.age"></el-input>
          </el-form-item>
          <el-form-item label="出生日期：">
            <el-date-picker v-model="form.birthDay" type="date" placeholder="选择日期"
                            value-format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm" class="wdd-primary-button-color">保存</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import InfoMenu from './components/info-menu.vue'
import {EnumMap} from '@/api/EnumMap'
import useStore from '@/store'
import {getCurrentUser, update} from '@/api/user'

const {user} = useStore()

export default {
  components: {InfoMenu},
  data() {
    return {
      EnumMap: EnumMap,
      form: {
        realName: null,
        age: null,
        sex: null,
        birthDay: null,
        phone: null,
        createTime: null,
        imagePath: null,
        email: null,
        idCard: null,
        workNo: null,
        jobTitle: null
      },
      formLoading: false,
      rules: {
        realName: [{required: true, message: '请输入真实姓名'},
          {max: 10, message: '长度在 10 个字符之下'}],
        phone: [{required: true, message: '请输入手机号'}],
        idCard: [{required: true, message: '请输入身份证号'}],
        workNo: [{required: true, message: '请输入工号'}],
        email: [
          {required: true, message: '请输入邮箱'},
          {type: 'email', message: '请输入正确的邮箱地址'}
        ],
        age: [{type: 'number', message: '年龄必须为数字值'}]
      },
      disable: {
        realName: false,
        idCard: false,
        workNo: false,
        jobTitle: false
      }
    }
  },
  created() {
    getCurrentUser().then(re => {
      this.form = re.response
      this.disableForm()
    })
  },
  methods: {
    disableForm() {
      if (this.form.realName !== null && this.form.realName.length > 0) {
        this.disable.realName = true
      }
      if (this.form.idCard !== null && this.form.idCard.length > 0) {
        this.disable.idCard = true
      }
      if (this.form.workNo !== null && this.form.workNo.length > 0) {
        this.disable.workNo = true
      }
      if (this.form.jobTitle !== null && this.form.jobTitle.length > 0) {
        this.disable.jobTitle = true
      }
    },
    uploadProgress() {
      this.loading = this.$loading({
        lock: true,
        text: '文件上传中…',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
    },
    uploadSuccess(re, file) {
      this.loading.close()
      if (re.code === 1) {
        this.form.imagePath = re.response
      } else {
        this.$message.error(re.message)
      }
    },
    uploadError() {
      this.loading.close()
      this.$message.error('文件上传失败，请检查文件大小或文件格式')
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          update(this.form).then(data => {
            if (data.code === 1) {
              user.updateRealName(this.form.realName)
              this.disableForm()
              this.$message.success(data.message)
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
    }
  }
}
</script>
