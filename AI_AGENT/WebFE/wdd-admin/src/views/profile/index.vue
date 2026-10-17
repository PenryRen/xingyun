<template>
  <div class="app-container">
    <div class="wdd-show-row">

      <div style="width: 450px;margin: 0px  10px 10px 0px;">
        <el-card header="个人简介">
          <div class="user-profile">
            <div class="box-center">
              <el-upload class="upload" action="/api/profile/image" accept=".jpg,.png"
                         :data="{folder:'profile'}"
                         :show-file-list="false" :on-progress="uploadProgress" :on-success="uploadSuccess"
                         :on-error="uploadError">
                <el-avatar :src="userInfo.imagePath" class="user-image" :size="100">暂无头像</el-avatar>
              </el-upload>
            </div>
            <div class="box-center">
              <div class="user-name text-center">{{ userInfo.userName }}</div>
              <div class="user-role text-center text-muted">{{ userInfo.roleStr }}</div>
            </div>
          </div>
          <div class="user-bio">
            <div class="user-education user-bio-section">
              <div class="user-bio-section-header">
                <svg-icon icon-class="education"/>
                <span>个人信息</span></div>
              <div class="user-bio-section-body">
                <div class="text-muted">
                  <div class="item"><span>用户名：</span><span>{{ userInfo.userName }}</span></div>
                  <div class="item"><span>真实姓名：</span><span>{{ userInfo.realName }}</span></div>
                  <div class="item"><span>角色：</span><span>{{ userInfo.roleStr }}</span></div>
                  <div class="item"><span>班级：</span><span>{{ userInfo.departmentStr }}</span></div>
                  <div class="item"><span>手机号：</span><span>{{ userInfo.phone }}</span></div>
                  <div class="item"><span>邮箱：</span><span>{{ userInfo.email }}</span></div>
                  <div class="item"><span>年龄：</span><span>{{ userInfo.age }}</span></div>
                  <div class="item"><span>创建时间：</span><span>{{ userInfo.createTime }}</span></div>
                </div>
              </div>
            </div>

          </div>
        </el-card>
      </div>
      <div class="wdd-show-col">
        <el-card>
          <el-tabs v-model="activeName">
            <el-tab-pane label="时间线" name="timeline">
              <div class="block">
                <el-timeline>
                  <el-timeline-item placement="top" :timestamp="item.createTime"
                                    v-for="item in eventLog"
                                    :key="item.id">
                    <p>{{ item.content }}</p>
                  </el-timeline-item>
                </el-timeline>
              </div>
            </el-tab-pane>
            <el-tab-pane label="修改资料" name="account">
              <el-form label-width="100px" :model="userInfo" ref="form" v-loading="formLoading"
                       :rules="rules"
                       class="block">
                <el-form-item label="真实姓名" prop="realName" required>
                  <el-input v-model.trim="userInfo.realName"/>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model.trim="userInfo.phone"/>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model.trim="userInfo.email"/>
                </el-form-item>
                <el-form-item label="年龄" prop="age">
                  <el-input v-model.number="userInfo.age"/>
                </el-form-item>
                <el-form-item label="性别：" prop="sex">
                  <el-select v-model="userInfo.sex" placeholder="性别">
                    <el-option v-for="item in EnumMap.user.sexEnum" :key="item.key"
                               :value="item.key"
                               :label="item.value"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submit">更新</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="password">
              <el-form :model="passwordForm" ref="pwdForm" label-width="100px" :rules="passwordRules">
                <el-form-item label="旧密码：" prop="oldPassword" required>
                  <el-input v-model="passwordForm.oldPassword" type="password"></el-input>
                </el-form-item>
                <el-form-item label="新密码：" prop="newPassword" required>
                  <el-input v-model="passwordForm.newPassword" type="password"></el-input>
                </el-form-item>
                <el-form-item label="确认密码：" prop="confirmPassword" required>
                  <el-input v-model="passwordForm.confirmPassword" type="password"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="passwordClick" class="wdd-primary-button-color">
                    提交
                  </el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>

    </div>
  </div>
</template>

<script>

import {EnumMap} from '@/api/EnumMap'
import SvgIcon from '@/components/SvgIcon/index.vue'
import {current, edit, changePassword} from '@/api/profile'
import useStore from '@/store'

const {user, tagsView} = useStore()

export default {
  name: 'ProfileIndex',
  components: {SvgIcon},
  data() {
    let checkPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      EnumMap: EnumMap,
      activeName: 'timeline',
      userInfo: {},
      eventLog: [],
      passwordForm: {
        oldPassword: null,
        newPassword: null,
        confirmPassword: null
      },
      formLoading: false,
      rules: {
        realName: [{required: true, message: '请输入真实姓名'}, {
          max: 255,
          message: '长度小于255个字符'
        }],
        age: [{
          type: 'number',
          message: '年龄必须为数字值'
        }]
      },
      passwordRules: {
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
    this.load()
  },
  methods: {
    load() {
      current().then(re => {
        this.userInfo = re.response.profileInfoVM
        this.eventLog = re.response.userEventLogList
      })
    },
    submit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          edit(this.userInfo).then(re => {
            if (re.code === 1) {
              user.updateRealName(this.userInfo.realName)
              this.$message.success(re.message)
              this.load()
            } else {
              this.$message.error(re.message)
            }
          }).finally(() => {
            this.formLoading = false
          })
        } else {
          return false
        }
      })
    },
    passwordClick() {
      this.$refs.pwdForm.validate((valid) => {
        if (valid) {
          changePassword(this.passwordForm).then(data => {
            if (data.code === 1) {
              this.$message.success(data.message)
              user
                  .logout()
                  .then(() => {
                    tagsView.delAllViews()
                  })
                  .then(() => {
                    this.$router.push(`/login`)
                  })
            } else {
              this.$message.error(data.message)
            }
          })
        } else {
          return false
        }
      })
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
        this.userInfo.imagePath = re.response
      } else {
        this.$message.error(re.message)
      }
    },
    uploadError() {
      this.loading.close()
      this.$message.error('文件上传失败，请检查文件大小或文件格式')
    }
  }
}
</script>

<style lang="scss" scoped>
.box-center {
  margin: 0 auto;
  display: table;
}

.text-muted {
  color: #777;
  padding: 10px;

  .item {
    padding: 3px 0px;
    display: flex;

    span:first-child {
      width: 80px;
    }

    span:last-child {
      flex: 1;
    }
  }
}

.user-profile {
  .user-name {
    font-weight: bold;
  }

  .box-center {
    padding-top: 10px;
  }

  .user-role {
    padding-top: 10px;
    font-weight: 400;
    font-size: 14px;
  }

  .box-social {
    padding-top: 30px;

    .el-table {
      border-top: 1px solid #dfe6ec;
    }
  }

  .user-follow {
    padding-top: 20px;
  }
}

.user-bio {
  margin-top: 20px;
  color: #606266;

  span {
    padding-left: 4px;
  }

  .user-bio-section {
    font-size: 14px;
    padding: 15px 0;

    .user-bio-section-header {
      border-bottom: 1px solid #dfe6ec;
      padding-bottom: 10px;
      margin-bottom: 10px;
      font-weight: bold;
    }
  }
}

.pan-thumb {
  cursor: pointer;
}

</style>
