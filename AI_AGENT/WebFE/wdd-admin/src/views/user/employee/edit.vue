<template>
  <div class="app-container">

    <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
      <el-form-item label="用户名：" prop="userName" required>
        <el-input v-model="form.userName" :disabled="userNameDisable"></el-input>
      </el-form-item>
      <el-form-item label="密码：">
        <el-input v-model="form.password" type="password"></el-input>
      </el-form-item>
      <el-form-item label="真实姓名：" prop="realName" required>
        <el-input v-model="form.realName"></el-input>
      </el-form-item>
      <el-form-item label="班级：" prop="departmentId" required>
        <el-tree-select v-model="form.departmentId" :data="departmentTree" check-strictly
                        :render-after-expand="true" default-expand-all placeholder="班级"/>
      </el-form-item>
      <el-form-item label="头像：" prop="imagePath">
        <el-upload class="upload" action="/api/upload/folder/file" accept=".jpg,.png" :data="{folder:'profile'}"
                   :show-file-list="false" :on-progress="uploadProgress" :on-success="uploadSuccess"
                   :on-error="uploadError">
          <el-avatar :size="100" :src="form.imagePath" shape="circle" fit="scale-down" class="user-image">暂无头像
          </el-avatar>
        </el-upload>
      </el-form-item>
      <el-form-item label="职位：" prop="jobTitle">
        <el-input v-model="form.jobTitle"></el-input>
      </el-form-item>
      <el-form-item label="工号：" prop="workNo">
        <el-input v-model="form.workNo"></el-input>
      </el-form-item>
      <el-form-item label="身份证号：" prop="idCard">
        <el-input v-model="form.idCard"></el-input>
      </el-form-item>
      <el-form-item label="年龄：" prop="age">
        <el-input v-model.number="form.age"></el-input>
      </el-form-item>
      <el-form-item label="性别：">
        <el-select v-model="form.sex" placeholder="性别" clearable>
          <el-option v-for="item in EnumMap.user.sexEnum" :key="item.key" :value="item.key"
                     :label="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="出生日期：">
        <el-date-picker v-model="form.birthDay" value-format="YYYY-MM-DD" type="date" placeholder="选择日期"/>
      </el-form-item>
      <el-form-item label="手机：" prop="phone">
        <el-input v-model="form.phone"></el-input>
      </el-form-item>
      <el-form-item label="邮箱：" prop="email">
        <el-input v-model="form.email"></el-input>
      </el-form-item>
      <el-form-item label="状态：" prop="status" required>
        <el-select v-model="form.status" placeholder="状态">
          <el-option v-for="item in EnumMap.user.statusEnum" :key="item.key" :value="item.key"
                     :label="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {EnumMap} from '@/api/EnumMap'
import {employeeSelect, employeeEdit} from '@/api/user'
import {tree} from '@/api/department'
import useStore from '@/store'

const {tagsView} = useStore()

export default {
  name: 'EmployeeEdit',
  data() {
    return {
      EnumMap: EnumMap,
      form: {
        id: null,
        userName: null,
        password: null,
        realName: null,
        idCard: null,
        workNo: null,
        imagePath: null,
        jobTitle: null,
        systemRole: 1,
        status: 1,
        age: null,
        sex: null,
        birthDay: null,
        phone: null,
        email: null,
        departmentId: null
      },
      departmentTree: [],
      formLoading: false,
      userNameDisable: false,
      rules: {
        userName: [{required: true, message: '请输入用户名'},
          {pattern: /^[A-Za-z0-9]{5,24}$/, message: '用户名由5至24位字母和数字组成'}],
        departmentId: [{required: true, message: '请选择班级'}],
        realName: [{required: true, message: '请输入真实姓名'}, {
          max: 255,
          message: '长度小于255个字符'
        }],
        age: [{type: 'number', message: '年龄必须为数字值'}],
        phone: [{max: 255, message: '长度小于255个字符'}],
        email: [{type: 'email', message: '请输入正确的邮箱地址'}, {
          max: 255,
          message: '长度小于255个字符'
        }]
      }
    }
  },
  created() {
    let id = this.$route.query.id

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      employeeSelect(id).then(re => {
        this.form = re.response
        this.userNameDisable = true
      }).finally(() => {
        this.formLoading = false
      })
    }

    tree().then(re => {
      this.departmentTree = re.response
    })
  },
  methods: {
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          employeeEdit(this.form).then(re => {
            if (re.code === 1) {
              this.$message.success(re.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/user/employee/list')
              })
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
        this.form.imagePath = re.response.path
      } else {
        this.$message.error(re.message)
      }
    },
    uploadError() {
      this.loading.close()
      this.$message.error('文件上传失败，请检查文件大小或文件格式')
    },
    resetForm() {
      this.form = {
        id: null,
        userName: null,
        password: null,
        realName: null,
        idCard: null,
        workNo: null,
        imagePath: null,
        jobTitle: null,
        systemRole: 1,
        status: 1,
        age: null,
        sex: null,
        birthDay: null,
        phone: null,
        email: null,
        departmentId: null
      }
      this.$refs['form'].resetFields()
    }
  }
}
</script>
