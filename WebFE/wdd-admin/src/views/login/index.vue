<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on"
             label-position="left">

      <div class="title-container">
        <h3 class="title">{{ VITE_APP_SYSTEM_NAME }}</h3>
      </div>

      <el-form-item prop="userName">
        <span class="svg-container">
          <svg-icon icon-class="user"/>
        </span>
        <el-input
            ref="userName"
            v-model="loginForm.userName"
            placeholder="用户名"
            name="userName"
            type="text"
            tabindex="1"
            auto-complete="on"
        />
      </el-form-item>


      <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password"/>
          </span>
        <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="密码"
            name="password"
            tabindex="2"
            auto-complete="on"
            @keyup.enter="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"/>
          </span>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:40px;margin-top: 20px"
                 @click.prevent="handleLogin" class="wdd-login-button">登录
      </el-button>

    </el-form>

    <div class="account-foot-copyright">
      <span>{{ VITE_APP_COPYRIGHT }}</span>
    </div>
  </div>
</template>

<script>
import SvgIcon from '@/components/SvgIcon/index.vue'
import {ElMessage} from 'element-plus'
import {rsaEncryptStr} from '@/utils'
import router from '@/router'
import useStore from '@/store'

const {user, tagsView, permission} = useStore()
import {login} from '@/api/login'

export default {
  name: 'Login',
  components: {SvgIcon},
  data() {
    return {
      VITE_APP_SYSTEM_NAME: import.meta.env.VITE_APP_SYSTEM_NAME,
      VITE_APP_COPYRIGHT: import.meta.env.VITE_APP_COPYRIGHT,
      loginForm: {
        userName: '',
        password: ''
      },
      loginRules: {
        userName: [{required: true, message: '请输入用户名'},
          {pattern: /^[A-Za-z0-9]{5,24}$/, message: '用户名由5至24位字母和数字组成'}],
        password: [{required: true, message: '请输入密码'},
          {min: 5, max: 24, message: '密码长度在5到24个字符之间'}]
      },
      passwordType: 'password',
      loading: false
    }
  },
  created() {
  },
  mounted() {
    if (this.loginForm.userName === '') {
      this.$refs.userName.focus()
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          let encodePassword = rsaEncryptStr(import.meta.env.VITE_APP_PAIR_ONE_PUBLIC_KEY, this.loginForm.password)
          let data = {userName: this.loginForm.userName, password: encodePassword}
          login(data).then(function (result) {
            if (result && result.code === 1) {
              user.login(result.response)
              tagsView.delAllViews()
              permission.clearRoutes()
              router.push({path: '/'}).catch(err => {
              })
            } else {
              ElMessage.error(result.message)
            }
          }).finally(() => {
            this.loading = false
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>


<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  display: flex;
  align-content: center;
  align-items: center;
  justify-content: center;
  padding-bottom: 100px;

  .login-form {
    width: 520px;
    max-width: 100%;
    padding: 30px 50px 10px 50px;
    background: rgba(252, 254, 255, 0.11);
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
