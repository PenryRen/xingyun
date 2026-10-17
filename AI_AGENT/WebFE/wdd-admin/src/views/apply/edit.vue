<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="120px" v-loading="formLoading" :rules="formRules">
      <el-form-item label="报名分类：">
        <el-tree-select v-model="form.applyArchiveId" :data="applyArchiveTree" check-strictly
                        :render-after-expand="true" default-expand-all placeholder="报名分类"/>
      </el-form-item>
      <el-form-item label="发布班级：" prop="departmentIdList" require>
        <tree-select v-model="form.departmentIdList" :data="departmentTree" :clearable="true"
                     placeholder="发布班级" style="width: 100%"/>
      </el-form-item>
      <el-form-item label="报名名称：" prop="name" required>
        <el-input v-model="form.name" :validate-event="false"></el-input>
      </el-form-item>
      <el-form-item label="报名截止时间：" prop="applyEndTime" required>
        <el-date-picker v-model="form.applyEndTime" type="datetime" :disabled-date="disabledDate"
                        value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择日期时间" :validate-event="false">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="是否需要审核：" prop="needAudit">
        <el-checkbox v-model="form.needAudit"></el-checkbox>
      </el-form-item>
      <el-form-item label="是否限制人数：" prop="limited">
        <el-checkbox v-model="form.limited"></el-checkbox>
      </el-form-item>
      <el-form-item label="人数限制：" prop="count" v-show="form.limited">
        <el-input-number v-model.number="form.count" :step="1"></el-input-number>
      </el-form-item>
      <el-form-item label="考试时间：" prop="limitDateTime" class="form-item-content-block" required>
        <el-date-picker v-model="form.limitDateTime" value-format="YYYY-MM-DD HH:mm:ss" type="datetimerange"
                        range-separator="至" start-placeholder="考试开始时间" end-placeholder="考试结束时间"
                        :disabled-date="disabledDate">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import {disabledDate} from '@/utils/index'
import {select, edit} from '@/api/apply'
import {tree as departmentTree} from '@/api/department'
import {tree as applyArchiveTree} from '@/api/applyArchive'
import useStore from '@/store'

const {tagsView} = useStore()

export default {
  name: 'ApplyEdit',
  data() {
    return {
      disabledDate: disabledDate,
      form: {
        id: null,
        name: null,
        applyEndTime: null,
        limitDateTime: [],
        needAudit: false,
        limited: false,
        count: null,
        departmentIdList: [],
        applyArchiveId: null
      },
      formRules: {
        name: [{required: true, message: '请输入报名名称'}, {
          max: 255,
          message: '长度小于255个字符'
        }],
        applyEndTime: [{required: true, message: '请选择报名截止时间'}],
        limitDateTime: [{required: true, message: '请选择考试时间'}],
        departmentIdList: [{required: true, message: '班级不能为空'}]
      },
      formLoading: false,
      departmentTree: [],
      applyArchiveTree: []
    }
  },
  created() {
    let id = this.$route.query.id

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id).then(re => {
        this.form = re.response
      }).finally(() => {
        this.formLoading = false
      })
    }

    departmentTree().then(re => {
      this.departmentTree = re.response
    })

    applyArchiveTree().then(re => {
      this.applyArchiveTree = re.response
    })
  },
  methods: {
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          edit(this.form).then(data => {
            if (data.code === 1) {
              this.$message.success(data.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/apply/list')
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
        name: null,
        applyEndTime: null,
        limitDateTime: [],
        needAudit: false,
        limited: false,
        count: null,
        departmentIdList: [],
        applyArchiveId: null
      }
      this.$refs['form'].resetFields()
    }
  }
}
</script>
