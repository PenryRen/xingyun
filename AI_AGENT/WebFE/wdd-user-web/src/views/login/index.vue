<template>
  <div class="login-back">
    <a href="http://www.ueit.com.cn" class="login-head-link">
      <img src="@/assets/logo-white.png" style="height: 60px">
    </a>
    <img src="@/assets/background.png" class="register-image-background">
    <el-card class="login-card" shadow="never">
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules">
        <el-form-item class="login-title-item">
          <span class="form-title">{{ VITE_APP_SYSTEM_NAME }}登录</span>
        </el-form-item>
        <el-form-item prop="userName" required>
          <el-input v-model="loginForm.userName" placeholder="请输入用户名" type="text" auto-complete="on"
          />
        </el-form-item>
        <el-form-item prop="password" required>
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" auto-complete="on"
          />
        </el-form-item>
        <el-form-item prop="code" class="login-kaptcha" required>
          <el-input v-model="loginForm.code" placeholder="请输入验证码" auto-complete="on"/>
          <img :src="kaptchaUrl" @click="kaptchaRefresh">
        </el-form-item>
        <el-form-item class="login-forget">
          <span class="login-link" @click="forget">
            忘记密码
          </span>
        </el-form-item>
        <el-form-item style="margin-top:5px;">
          <input type="button" value="登录" @click="handleLogin" style="width: 100%" class="login-submit"/>
        </el-form-item>
<!--        <el-form-item class="login-register-link">
          <span>没有账号？</span>
          <router-link to="/register" class="login-link">
            注册
          </router-link>
        </el-form-item>-->
      </el-form>
    </el-card>
    <div class="account-foot-copyright">
      <span>{{ VITE_APP_COPYRIGHT }}</span>
    </div>
  </div>
</template>

<script>
import {ElMessage} from 'element-plus'
import {login} from '@/api/login'
import {paperEncrypt} from '@/utils'
import {v4} from 'uuid'
import useStore from '@/store'
import router from '@/router'

const {user} = useStore()

export default {
  name: 'Login',
  data() {
    return {
      VITE_APP_SYSTEM_NAME: import.meta.env.VITE_APP_SYSTEM_NAME,
      VITE_APP_COPYRIGHT: import.meta.env.VITE_APP_COPYRIGHT,
      kaptchaUrl: '/api/kaptcha/refresh',
      loginForm: {
        userName: '',
        password: '',
        codeKey: null,
        code: null
      },
      loginRules: {
        userName: [{required: true, message: '请输入用户名'},
          {pattern: /^[A-Za-z0-9]{5,24}$/, message: '用户名由5至24位字母和数字组成'}],
        password: [{required: true, message: '请输入密码'},
          {min: 5, max: 24, message: '密码长度在5到24个字符之间'}],
        code: [{required: true, message: '请输入验证码'},
          {min: 4, max: 4, message: '长度为 4 个字符'}]
      }
    }
  },
  created() {
    this.kaptchaRefresh()
  },
  methods: {
    kaptchaRefresh() {
      let uuidKey = v4().toString()
      this.loginForm.codeKey = uuidKey
      this.kaptchaUrl = `/api/kaptcha/refresh?code=${uuidKey}`
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          login(paperEncrypt(import.meta.env.VITE_APP_PAIR_ONE_PUBLIC_KEY, this.loginForm)).then(function (re) {
            if (re && re.code === 1) {
              localStorage.clear()
              user.login(re.response)
              router.push({path: '/'}).catch(err => {})
            } else {
              ElMessage.error(re.message)
            }
          })
        } else {
          return false
        }
      })
    },
    forget() {
      ElMessage.warning('请联系管理员')
    }
  }
}
</script>
