<template>
  <div class="register-back">
    <a href="http://www.ueit.com.cn" class="register-head-link">
      <img src="@/assets/logo-white.png" style="height: 60px">
    </a>
    <img src="@/assets/background.png" class="register-image-background">
    <el-card class="register-card" shadow="never">
      <el-form ref="registerForm" :model="registerForm" :rules="registerRules">
        <el-form-item class="register-title-item">
          <span class="form-title">{{ VITE_APP_SYSTEM_NAME }}注册</span>
        </el-form-item>
        <el-form-item class="register-register-link">
          <span class="register-font">已有账号？</span>
          <router-link to="/login" class="register-link">
            登录
          </router-link>
        </el-form-item>
        <el-form-item prop="departmentId" required>
          <el-tree-select v-model="registerForm.departmentId" :data="departmentTree" :props="defaultProps"
                          check-strictly
                          :render-after-expand="true" default-expand-all placeholder="请选择班级"
                          style="width: 100%"/>
        </el-form-item>
        <el-form-item prop="userName" required>
          <el-input v-model="registerForm.userName" placeholder="请输入用户名" type="text"
                    auto-complete="on"/>
        </el-form-item>
        <el-form-item prop="realName" required>
          <el-input v-model="registerForm.realName" placeholder="请输入真实姓名" type="text"
                    auto-complete="on"/>
        </el-form-item>
        <el-form-item prop="password" required>
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"
                    auto-complete="on"/>
        </el-form-item>
        <el-form-item prop="confirmPassword" required>
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码"
                    auto-complete="on"/>
        </el-form-item>
        <el-form-item class="register-agree-contain">
          <el-checkbox v-model="registerForm.agree">
            <a href="http://www.ueit.com.cn/policy/wk/user.html" target="_blank"
               style="text-decoration: none"><span
                class="register-agree">同意用户及服务条款</span></a>
          </el-checkbox>
        </el-form-item>
        <el-form-item style="margin-top:5px;">
          <input type="button" value="注册" @click="handleRegister" style="width: 100%" class="login-submit"/>
        </el-form-item>
      </el-form>
    </el-card>
    <div class="account-foot-copyright">
      <span>{{ VITE_APP_COPYRIGHT }}</span>
    </div>
  </div>
</template>

<script>

import {ElMessage} from 'element-plus'
import router from '@/router'
import {tree, register} from '@/api/register'
import {paperEncrypt} from '@/utils'

export default {
  name: 'Register',
  data() {
    let checkPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      VITE_APP_SYSTEM_NAME: import.meta.env.VITE_APP_SYSTEM_NAME,
      VITE_APP_COPYRIGHT: import.meta.env.VITE_APP_COPYRIGHT,
      defaultProps: {
        value: 'id',
        children: 'child',
        label: 'name'
      },
      registerForm: {
        departmentId: null,
        userName: null,
        realName: null,
        password: null,
        confirmPassword: null,
        agree: null
      },
      registerRules: {
        departmentId: [{required: true, message: '请选择班级'}],
        userName: [{required: true, message: '请输入用户名'},
          {pattern: /^[A-Za-z0-9]{5,24}$/, message: '用户名由5至24位字母和数字组成'}],
        realName: [{required: true, message: '请输入真实姓名'},
          {max: 10, message: '长度在 10 个字符之下'}],
        password: [{required: true, message: '请输入密码'},
          {min: 5, max: 24, message: '密码长度在5到24个字符之间'}],
        confirmPassword: [{required: true, message: '请输入确认密码'},
          {min: 5, max: 24, message: '确认密码长度在5到24个字符之间'},
          {validator: checkPassword}]
      },
      departmentTree: []
    }
  },
  created() {
    tree().then(re => {
      this.departmentTree = re.response
    })
  },
  methods: {
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          if (!this.registerForm.agree) {
            ElMessage.error('请同意用户及服务条款')
            return
          }
          register(paperEncrypt(import.meta.env.VITE_APP_PAIR_ONE_PUBLIC_KEY, this.registerForm)).then(function (result) {
            if (result && result.code === 1) {
              router.push({path: '/login'})
              ElMessage.success(result.response)
            } else {
              ElMessage.error(result.message)
            }
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>
