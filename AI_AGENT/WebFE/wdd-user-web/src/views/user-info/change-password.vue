<template>
  <div class="app-item-contain">
    <el-card class="user-index-info-contain user-change-password" shadow="never">
      <info-menu url="/user/passwordChange"/>

      <div class="right">
        <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
          <el-form-item label="旧密码：" prop="oldPassword" required>
            <el-input v-model="form.oldPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="新密码：" prop="newPassword" required>
            <el-input v-model="form.newPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="确认密码：" prop="confirmPassword" required>
            <el-input v-model="form.confirmPassword" type="password"></el-input>
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
import {changePassword} from '@/api/user'
import {paperEncrypt} from '@/utils'
import useStore from '@/store'

const {user} = useStore()


export default {
  components: {InfoMenu},
  data() {
    let checkPassword = (rule, value, callback) => {
      if (value !== this.form.newPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      form: {
        oldPassword: null,
        newPassword: null,
        confirmPassword: null
      },
      formLoading: false,
      rules: {
        oldPassword: [{required: true, message: '请输入旧密码'},
          {min: 5, max: 24, message: '旧密码长度在5到24个字符之间'}],
        newPassword: [{required: true, message: '请输入新密码'},
          {min: 5, max: 24, message: '新密码长度在5到24个字符之间'}],
        confirmPassword: [{required: true, message: '请确认密码'},
          {min: 5, max: 24, message: '确认密码长度在5到24个字符之间'},
          {validator: checkPassword}]
      }
    }
  },
  created() {
  },
  methods: {
    submitForm() {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let _this = this
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          changePassword(paperEncrypt(import.meta.env.VITE_APP_PAIR_ONE_PUBLIC_KEY, this.form)).then(data => {
            if (data.code === 1) {
              this.$message.success(data.message)
              user
                  .logout()
                  .then(() => {
                    _this.$router.push(`/login`)
                  })
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
