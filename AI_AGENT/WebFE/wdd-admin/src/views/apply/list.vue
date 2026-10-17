<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="名称：">
        <el-input v-model="queryParam.name" clearable></el-input>
      </el-form-item>
      <el-form-item label="分类：">
        <tree-select v-model="queryParam.applyArchiveIdList" :data="applyArchiveTree" :clearable="true"
                     placeholder="报名分类"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['apply:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <router-link :to="{path:'/apply/edit'}" class="link-left">
          <el-button type="primary" v-has-perm="['apply:create']">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="90px"/>
      <el-table-column prop="applyArchive" label="分类" width="120px"/>
      <el-table-column prop="name" label="报名名称"/>
      <el-table-column prop="applyCount" label="报名人数" width="100px"/>
      <el-table-column prop="applyEndTime" label="截止时间" width="160px"/>
      <el-table-column label="考试时间" width="160px">
        <template #default="{row}">
          <div>{{ row.limitStartTime }}</div>
          <div>{{ row.limitEndTime }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="departmentNameList" label="发布班级"/>
      <el-table-column prop="statusStr" label="状态" width="70px"/>
      <el-table-column width="160px" label="操作" align="center">
        <template #default="{row}">
          <el-button type="success" @click="applyPublish(row)" v-show="row.status === 1"
                     v-has-perm="['apply:publish']" size="small" class="wdd-page-action-button">发布
          </el-button>
          <router-link :to="{path:'/apply/edit', query:{id:row.id}}" class="link-left">
            <el-button v-show="row.status === 1" v-has-perm="['apply:update']"
                       size="small" class="wdd-page-action-button">编辑
            </el-button>
          </router-link>
          <el-button type="danger" @click="applyClose(row)" v-show="row.status === 2"
                     v-has-perm="['apply:close']" size="small" class="wdd-page-action-button">关闭
          </el-button>
          <router-link :to="{path:'/apply/show', query:{id:row.id}}" class="link-left">
            <el-button v-show="row.status === 2 || row.status === 3" v-has-perm="['apply:show']"
                       size="small" class="wdd-page-action-button">查看
            </el-button>
          </router-link>
          <el-button type="danger" @click="applyDelete(row)" size="small"
                     class="link-left wdd-page-action-button"
                     v-has-perm="['apply:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search"/>
  </div>
</template>

<script>
import {page, close, publish, deleteApply} from '@/api/apply'
import {tree} from '@/api/applyArchive'

export default {
  name: 'ApplyList',
  data() {
    return {
      queryParam: {
        name: null,
        applyArchiveIdList: null,
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      total: 0,
      applyArchiveTree: [],
      canLoad: true
    }
  },
  mounted() {
    this.init()
    this.canLoad = false
  },
  activated() {
    this.init()
  },
  deactivated() {
    this.canLoad = true
  },
  methods: {
    init() {
      if (!this.canLoad) {
        return
      }

      tree().then(re => {
        this.applyArchiveTree = re.response
      })

      this.search()
    },
    search() {
      page(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
      })
    },
    submitForm() {
      this.queryParam.pageIndex = 1
      this.search()
    },
    clearForm() {
      this.queryParam = {
        name: null,
        applyArchiveIdList: null,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    applyClose(row) {
      this.$confirm('此操作将关闭该报名, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        close(row.id).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    },
    applyPublish(row) {
      publish(row.id).then(re => {
        if (re.code === 1) {
          this.search()
          this.$message.success(re.message)
        } else {
          this.$message.error(re.message)
        }
      })
    },
    applyDelete(row) {
      this.$confirm('此操作将永久删除该报名, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteApply(row.id).then(re => {
          if (re.code === 1) {
            this.search()
            this.$message.success(re.message)
          } else {
            this.$message.error(re.message)
          }
        })
      })
    }
  }
}
</script>
