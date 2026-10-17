<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="虚拟机名称：">
        <el-input v-model="queryParam.vmName" clearable></el-input>
      </el-form-item>
      <el-form-item label="类型：">
        <el-select v-model="queryParam.vmType" clearable>
          <el-option v-for="item in EnumMap.vmWare.vmType" :key="item.key" :value="item.key"
                     :label="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态：">
        <el-select v-model="queryParam.status" clearable>
          <el-option v-for="item in EnumMap.vmWare.status" :key="item.key" :value="item.key"
                     :label="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分类：">
        <el-select v-model="queryParam.classes" clearable>
          <el-option v-for="item in EnumMap.vmWare.classes" :key="item.key" :value="item.key"
                     :label="item.value"></el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="ip：">
        <el-input v-model="queryParam.realName" clearable></el-input>
      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['vmWare:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
<!--        <router-link :to="{path:'/vmWare/edit'}" class="link-left">
          <el-button type="primary" v-has-perm="['user:admin:create']">添加</el-button>
        </router-link>-->
        <el-popover placement="bottom" trigger="click" :width="240">
          <template #reference>
            <el-button type="primary" class="link-left" v-has-perm="['vmWare:import']">导入
            </el-button>
          </template>
          <div class="wdd-popover-content">
            <a target="_blank" :href="`${VITE_APP_RESOURCE_URL}/template/file/vm_template  .xlsx`"
               style="float: left;margin-left: 5px;">
              <el-button type="success">Excel 模板</el-button>
            </a>
            <el-upload style="float: left;margin-left: 5px;" accept=".xlsx"
                       action="/api/vmWare/import"
                       :show-file-list="false" :on-progress="uploadProgress"
                       :on-success="uploadExcelSuccess" :on-error="uploadError">
              <el-button type="success" class="link-left">Excel 导入</el-button>
            </el-upload>
          </div>
        </el-popover>
        <el-button type="primary" class="link-left" @click="vmExport" v-has-perm="['vmWare:export']">导出
        </el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%" class="vm-operate-list">
      <el-table-column prop="id" label="Id" width="90px" fixed />
      <el-table-column prop="vmName" label="虚拟机名称" width="200px" :show-overflow-tooltip="true" fixed />
      <el-table-column prop="vmType" label="虚拟机类型" width="100px;" fixed>
        <template #default="{ row, column }">
          <el-tag v-if="checkExist(row,column)" :type="formatterTag(row,column)">{{formatterText(row,column)}}</el-tag>
          <div v-else></div>
        </template>
      </el-table-column>
      <el-table-column prop="vmIp" label="ip" width="140px"/>
      <el-table-column prop="vmCpu" label="cpu" width="60px;"/>
      <el-table-column prop="vmStorage" label="内存" width="60px;"/>
      <el-table-column prop="diskName" label="硬盘名称" width="200px" :show-overflow-tooltip="true"/>
      <el-table-column prop="diskSize" label="硬盘大小" width="90px;"/>
      <el-table-column prop="status" label="状态" width="100px;">
        <template #default="{ row, column }">
          <el-tag v-if="checkExist(row,column)" :type="formatterTag(row,column)">{{formatterText(row,column)}}</el-tag>
          <div v-else></div>
        </template>
      </el-table-column>
      <el-table-column prop="classes" label="分类" width="100px;">
        <template #default="{ row, column }">
          <el-tag v-if="checkExist(row,column)" :type="formatterTag(row,column)">{{formatterText(row,column)}}</el-tag>
          <div v-else></div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
      <el-table-column prop="updateTime" label="更新时间" width="160px"/>
      <el-table-column prop="validCreateTime" label="有效创建时间" width="160px"/>
      <el-table-column prop="validEndTime" label="有效结束时间" width="160px"/>
      <el-table-column width="210px" label="操作" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status === '01' && row.vmType === '01'" @click="start(row)" size="small" class="wdd-button-mini"
                     v-has-perm="['vmWare:start']">开机
          </el-button>
          <el-button v-if="row.status === '00' && row.vmType === '01'" @click="reStart(row)" size="small" class="wdd-button-mini link-left"
                     v-has-perm="['vmWare:shutdown']">重启
          </el-button>
          <el-button v-if="row.status === '00' && row.vmType === '01'" @click="shutdown(row)" type="info" size="small" class="wdd-button-mini link-left"
                     v-has-perm="['vmWare:reStart']">关机
          </el-button>
          <router-link
            v-if="row.vmType === '00' && (row.vmParentId === null || row.vmParentId === undefined || row.vmParentId === '')"
            :to="{ path: '/vmWare/edit', query: { id: row.id } }"
            class="link-left"
            style="margin-right: 6px"
          >
            <el-button type="primary" size="small" class="wdd-button-mini" v-has-perm="['vmWare:page', 'vmWare:import']">编辑模板
            </el-button>
          </router-link>
          <el-button v-if="row.vmType === '00'" @click="clone(row)" type="primary" size="small" class="wdd-button-mini link-left"
                     v-has-perm="['vmWare:clone']">克隆
          </el-button>
          <el-button v-if="row.vmType === '01'" @click="release(row)" type="danger" size="small" class="wdd-button-mini link-left"
                     v-has-perm="['vmWare:release']">释放
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search"/>
  </div>
</template>
<style scoped>

  .vm-operate-list .el-table__row .el-button{
    margin-bottom: 5px;
  }
</style>
<script>

import {PageList, shutdown, start, reStart, clone, release, vmExport} from '@/api/vmWare'
import {EnumMap} from "@/api/EnumMap";

export default {
  name: 'vmWareInfo',
  computed: {
    EnumMap() {
      return EnumMap
    }
  },
  data() {
    return {
      queryParam: {
        userName: null,
        realName: null,
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      total: 0,
      canLoad: true,
      timeUpdate: {
        documentInterval: null
      },
    }
  },
  mounted() {
    this.init()
    this.canLoad = false;
    clearInterval(this.timeUpdate.documentInterval)
    let _this = this;
    this.timeUpdate.documentInterval = setInterval(function () {
      _this.search();
    }, 10000)
  },
  activated() {
    this.init()
  },
  deactivated() {
    this.canLoad = true
  },
  beforeUnmount() {
    if (this.timeUpdate.documentInterval) {
      clearInterval(this.timeUpdate.documentInterval)
      this.timeUpdate.documentInterval = null
    }
  },
  methods: {
    init() {
      if (!this.canLoad) {
        return
      }

      this.search()
    },
    search() {
      PageList(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
      })
    },
    /**
     * 关机
     * @param row
     */
    shutdown(row) {
      this.$confirm('此操作将关闭此虚拟化, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        shutdown(row).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    },
    /**
     * 开机
     * @param row
     */
    start(row) {
      this.$confirm('此操作将开启此虚拟化, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        start(row).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    },
    /**
     * 重启
     * @param row
     */
    reStart(row) {
      this.$confirm('此操作将重启此虚拟化, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        reStart(row).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    },
    /**
     * 释放
     * @param row
     */
    release(row) {
      this.$confirm('此操作将释放此虚拟化, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        release(row).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    },
    /**
     * 克隆
     * @param row
     */
    clone(row){
      this.$confirm('此操作将克隆此虚拟化, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        clone(row).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    },
    submitForm() {
      this.queryParam.pageIndex = 1
      this.search()
    },
    clearForm() {
      this.queryParam = {
        userName: null,
        realName: null,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    uploadProgress() {
      this.loading = this.$loading({
        lock: true,
        text: '文件上传中…',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
    },
    uploadExcelSuccess(re, file) {
      this.loading.close()
      if (re.code === 1) {
        this.search()
        window.location.href = re.response
      } else {
        this.$message.error(re.message)
      }
    },
    checkExist(row,column){
      if (column.property === "vmType") {
        if (row && row.vmType !== null && row.vmType !== undefined) {
          return !!this.getEnumMapValue(row.vmType, EnumMap.vmWare.vmType);
        }
      }
      if (column.property === "classes") {
        if (row && row.classes !== null && row.classes !== undefined) {
          return !!this.getEnumMapValue(row.classes,EnumMap.vmWare.classes)
        }
      }
      if (column.property === "status") {
        if (row && row.status !== null && row.status !== undefined) {
          return !!this.getEnumMapValue(row.status,EnumMap.vmWare.status)
        }
      }
    },
    formatterText(row,column){
      if (column.property === "vmType") {
        if (row && row.vmType !== null && row.vmType !== undefined) {
          return this.getEnumMapValue(row.vmType,EnumMap.vmWare.vmType)
        }
      }
      if (column.property === "classes") {
        if (row && row.classes !== null && row.classes !== undefined) {
          return this.getEnumMapValue(row.classes,EnumMap.vmWare.classes)
        }
      }
      if (column.property === "status") {
        if (row && row.status !== null && row.status !== undefined) {
          return this.getEnumMapValue(row.status,EnumMap.vmWare.status)
        }
      }
    },
    formatterTag(row,column){
      if (column.property === "vmType") {
        if (row && row.vmType !== null && row.vmType !== undefined) {
          return this.getEnumMapValue(row.vmType,EnumMap.vmWare.vmTypeTag)
        }
      }
      if (column.property === "classes") {
        if (row && row.classes !== null && row.classes !== undefined) {
          return this.getEnumMapValue(row.classes,EnumMap.vmWare.classesTag)
        }
      }
      if (column.property === "status") {
        if (row && row.status !== null && row.status !== undefined) {
          return this.getEnumMapValue(row.status,EnumMap.vmWare.statusTag)
        }
      }
    },
    getEnumMapValue(value, map) {
      for (let i = 0; i < map.length; i++) {
        const statusObj = map[i];
        if (statusObj.key === value) {
          return statusObj.value;
        }
      }
    },
    uploadError() {
      this.loading.close()
      this.$message.error('文件上传失败，请检查文件大小或文件格式')
    },
    vmExport(){
      vmExport(this.queryParam).then(data => {
        if (data.code === 1) {
          window.open(data.response, '_blank')
        } else {
          this.$message.error(data.message)
        }
      })
    }
  }
}
</script>
