<template>
  <div class="app-container">

    <el-form :model="form" ref="form" label-width="120px" v-loading="formLoading" :rules="rules">
      <el-form-item label="虚拟机名称：" prop="vmName" required>
        <el-input v-model="form.vmName" :disabled="userNameDisable"></el-input>
      </el-form-item>
      <el-form-item label="虚拟机密码：" prop="vmPassword" required>
        <el-input v-model="form.vmPassword"></el-input>
      </el-form-item>
      <el-form-item label="虚拟机cpu：" prop="vmCpu">
        <el-input v-model="form.vmCpu"></el-input>
      </el-form-item>
      <el-form-item label="虚拟机内存：" prop="vmStorage">
        <el-input v-model.number="form.vmStorage"></el-input>
      </el-form-item>
      <el-form-item label="分类：" required>
        <el-select v-model="form.sex" placeholder="分类" clearable >
          <el-option v-for="item in EnumMap.vmWare.classes" :key="item.key" :value="item.key"
                     :label="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="类型：" required>
        <el-select v-model="form.vmType" placeholder="类型" clearable >
          <el-option v-for="item in EnumMap.vmWare.type" :key="item.key" :value="item.key"
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
import {adminSelect, adminEdit} from '@/api/user'
import {list as roleList} from '@/api/role'
import {tree} from '@/api/department'
import useStore from '@/store'

const {tagsView} = useStore()

export default {
  name: 'AdminEdit',
  data() {
    return {
      EnumMap: EnumMap,
      form: {
        id: null,
        userName: null,
        password: null,
        realName: null,
        systemRole: 2,
        status: 1,
        age: null,
        sex: null,
        birthDay: null,
        phone: null,
        email: null,
        roleId: null,
        departmentId: null
      },
      departmentTree: [],
      formLoading: false,
      userNameDisable: false,
      roleList: [],
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
        }],
        roleId: [{required: true, message: '请选择管理员角色'}]
      }
    }
  },
  created() {
    let id = this.$route.query.id

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      adminSelect(id).then(re => {
        this.form = re.response
        this.userNameDisable = true
      }).finally(() => {
        this.formLoading = false
      })
    }

    roleList().then(re => {
      this.roleList = re.response
    })

    tree().then(re => {
      this.departmentTree = re.response
    })
  },
  methods: {
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          adminEdit(this.form).then(re => {
            if (re.code === 1) {
              this.$message.success(re.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/user/admin/list')
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
    resetForm() {
      this.form = {
        id: null,
        userName: null,
        password: null,
        realName: null,
        systemRole: 2,
        status: 1,
        age: null,
        sex: null,
        birthDay: null,
        phone: null,
        email: null,
        roleId: null,
        departmentId: null
      }
      this.$refs['form'].resetFields()
    }
  }
}
</script>
