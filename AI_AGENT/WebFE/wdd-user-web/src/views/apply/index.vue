<template>
  <div class="app-item-contain apply-contain">

    <div class="apply-index-header">
      <div class="item-contain">
        <span class="title">报名分类：</span>
        <div class="item-radio">
          <el-radio-group v-model="queryParam.applyArchiveId">
            <el-radio v-for="item in applyArchiveList" :key="item.id" :label="item.id"
                      @click.prevent="applyArchiveChange(item.id)">{{ item.name }}
            </el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>

    <div class="apply-card-contain">
      <none-show v-if="applyNoneShow"/>
      <el-card v-for="item in tableData" :key="item.id" class="apply-card" shadow="hover">
        <div @click="goApply(item.id)" class="wdd-hand">
          <div class="apply-title">{{ item.name }}</div>
          <div class="apply-item" style="margin-top: 10px">人数限制：{{ item.limitCount }}</div>
          <div class="apply-item">
            <div class="apply-left">报名截止时间：</div>
            <div class="apply-right">{{ item.applyEndTime }}</div>
          </div>
          <div class="apply-item">
            <div class="apply-left apply-limit-time-color">考试开始时间：</div>
            <div class="apply-right apply-limit-time-color">{{ item.limitStartTime }}</div>
          </div>
          <div class="apply-item">
            <div class="apply-left apply-limit-time-color">考试结束时间：</div>
            <div class="apply-right apply-limit-time-color">{{ item.limitEndTime }}</div>
          </div>
          <div class="apply-item">发布时间：{{ item.createTime }}</div>
        </div>
      </el-card>
    </div>

    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search"/>
  </div>
</template>

<script>

import {list, page, goApply} from '@/api/apply'

export default {
  data() {
    return {
      queryParam: {
        applyArchiveId: null,
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: true,
      tableData: [],
      total: 0,
      applyArchiveList: [],
      applyNoneShow: false
    }
  },
  created() {
    list().then(re => {
      this.applyArchiveList = re.response
    })

    this.search()
  },
  methods: {
    applyArchiveChange(id) {
      if (id === this.queryParam.applyArchiveId) {
        this.queryParam.applyArchiveId = null
      } else {
        this.queryParam.applyArchiveId = id
      }
      this.search()
    },
    search() {
      this.listLoading = true
      page(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.applyNoneShow = this.total === 0
        this.queryParam.pageIndex = re.pageNum
      }).finally(() => {
        this.listLoading = false
      })
    },
    goApply(id) {
      this.$confirm('此操作将提交报名申请, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        goApply(id).then(re => {
          if (re.code === 1) {
            this.$message.success(re.response)
            this.search()
          } else {
            this.$message.error(re.message)
          }
        })
      })
    }
  }
}
</script>
