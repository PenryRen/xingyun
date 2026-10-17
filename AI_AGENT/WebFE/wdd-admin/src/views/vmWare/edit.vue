<template>
  <div class="app-container">
    <el-alert
      v-if="loaded && !canEditTemplate"
      title="当前为子机或由父级关联的记录"
      type="warning"
      :closable="false"
      show-icon
      class="mb-16"
      description="子机由阿里云无影按学员使用按需创建，桌面 ID与便捷用户保存在本记录中；请勿在此改 BundleId。如需调整镜像模板，请编辑对应「主机」模板行。"
    />
    <el-form
      :model="form"
      ref="form"
      label-width="140px"
      v-loading="formLoading"
      :rules="rules"
      :disabled="loaded && !canEditTemplate"
    >
      <el-form-item label="虚拟机名称：" prop="vmName" required>
        <el-input v-model="form.vmName" placeholder="模板显示名称" />
      </el-form-item>
      <el-form-item label="登录用户名：" prop="vmUsername">
        <el-input v-model="form.vmUsername" placeholder="可选，云桌面内系统账号说明" />
      </el-form-item>
      <el-form-item label="登录密码：" prop="vmPassword">
        <el-input v-model="form.vmPassword" type="password" show-password placeholder="模板或说明用密码" />
      </el-form-item>
      <el-form-item label="云电脑镜像 BundleId：" prop="url" required>
        <el-input
          v-model="form.url"
          placeholder="对应阿里云无影 CreateDesktops 的 BundleId，写入库字段 url"
        />
        <div class="form-tip">与 Excel 导入列「虚拟机路径」对应同一字段；请填控制台镜像/套餐 BundleId。</div>
      </el-form-item>
      <el-form-item label="CPU（核）：" prop="vmCpu">
        <el-input v-model.number="form.vmCpu" placeholder="可选，说明用" />
      </el-form-item>
      <el-form-item label="内存：" prop="vmStorage">
        <el-input v-model="form.vmStorage" placeholder="可选，说明用" />
      </el-form-item>
      <el-form-item label="硬盘名称：" prop="diskName">
        <el-input v-model="form.diskName" />
      </el-form-item>
      <el-form-item label="硬盘大小：" prop="diskSize">
        <el-input v-model="form.diskSize" />
      </el-form-item>
      <el-form-item label="IP：" prop="vmIp">
        <el-input v-model="form.vmIp" placeholder="无影模式下可为空" />
      </el-form-item>
      <el-form-item label="分类：" prop="classes" required>
        <el-select v-model="form.classes" placeholder="分类" clearable>
          <el-option
            v-for="item in EnumMap.vmWare.classes"
            :key="item.key"
            :value="item.key"
            :label="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="类型：">
        <el-select v-model="form.vmType" disabled placeholder="类型">
          <el-option
            v-for="item in EnumMap.vmWare.vmType"
            :key="item.key"
            :value="item.key"
            :label="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="不可分配：" prop="disabled">
        <el-switch v-model="form.disabled" />
      </el-form-item>
      <el-form-item label="有效开始时间：" prop="validCreateTime">
        <el-date-picker
          v-model="form.validCreateTime"
          type="datetime"
          value-format="YYYY-MM-DD HH:mm:ss"
          placeholder="选择时间"
        />
      </el-form-item>
      <el-form-item label="有效结束时间：" prop="validEndTime">
        <el-date-picker
          v-model="form.validEndTime"
          type="datetime"
          value-format="YYYY-MM-DD HH:mm:ss"
          placeholder="选择时间"
        />
      </el-form-item>
      <el-form-item v-if="canEditTemplate">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="resetFromServer">恢复为上次加载</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { EnumMap } from '@/api/EnumMap'
import { vmWareSelect, vmWareEdit } from '@/api/vmWare'
import useStore from '@/store'

const { tagsView } = useStore()

export default {
  name: 'VmWareEdit',
  data() {
    return {
      EnumMap,
      formLoading: false,
      loaded: false,
      snapshot: null,
      form: {
        id: null,
        vmType: null,
        vmParentId: null,
        vmName: null,
        vmUsername: null,
        vmPassword: null,
        url: null,
        vmCpu: null,
        vmStorage: null,
        diskName: null,
        diskSize: null,
        vmIp: null,
        classes: null,
        disabled: false,
        validCreateTime: null,
        validEndTime: null
      },
      rules: {
        vmName: [{ required: true, message: '请输入虚拟机名称' }],
        url: [{ required: true, message: '请输入 BundleId' }],
        classes: [{ required: true, message: '请选择分类' }]
      }
    }
  },
  computed: {
    canEditTemplate() {
      if (!this.loaded) {
        return true
      }
      const parentEmpty =
        this.form.vmParentId === null ||
        this.form.vmParentId === undefined ||
        this.form.vmParentId === ''
      return this.form.vmType === '00' && parentEmpty
    }
  },
  created() {
    const id = this.$route.query.id
    if (!id || parseInt(id, 10) === 0) {
      this.$message.error('缺少记录 id，请从虚拟机列表进入')
      return
    }
    this.load(parseInt(id, 10))
  },
  methods: {
    load(id) {
      this.formLoading = true
      vmWareSelect(id)
        .then((re) => {
          if (re.code === 1 && re.response) {
            this.applyResponse(re.response)
            this.loaded = true
          } else {
            this.$message.error(re.message || '加载失败')
          }
        })
        .finally(() => {
          this.formLoading = false
        })
    },
    applyResponse(row) {
      this.form = {
        id: row.id,
        vmType: row.vmType,
        vmParentId: row.vmParentId,
        vmName: row.vmName,
        vmUsername: row.vmUsername,
        vmPassword: row.vmPassword,
        url: row.url,
        vmCpu: row.vmCpu,
        vmStorage: row.vmStorage,
        diskName: row.diskName,
        diskSize: row.diskSize,
        vmIp: row.vmIp,
        classes: row.classes,
        disabled: !!row.disabled,
        validCreateTime: row.validCreateTime,
        validEndTime: row.validEndTime
      }
      this.snapshot = JSON.parse(JSON.stringify(this.form))
    },
    resetFromServer() {
      if (this.snapshot) {
        this.form = JSON.parse(JSON.stringify(this.snapshot))
      }
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (!valid) {
          return
        }
        if (!this.canEditTemplate) {
          this.$message.warning('当前记录不允许保存')
          return
        }
        this.formLoading = true
        vmWareEdit(this.form)
          .then((re) => {
            if (re.code === 1) {
              this.$message.success(re.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/vmWare/list')
              })
            } else {
              this.$message.error(re.message)
            }
          })
          .finally(() => {
            this.formLoading = false
          })
      })
    }
  }
}
</script>

<style scoped>
.mb-16 {
  margin-bottom: 16px;
}
.form-tip {
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
  margin-top: 4px;
}
</style>
