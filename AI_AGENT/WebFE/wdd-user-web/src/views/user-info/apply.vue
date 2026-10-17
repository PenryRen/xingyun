<template>
  <div class="app-item-contain">
    <el-card class="user-index-info-contain user-apply-info" shadow="never">

      <info-menu url="/user/apply"/>

      <div class="right">
        <none-show v-if="applyNoneShow"/>
        <el-card v-for="item in tableData" :key="item.id" class="user-apply-card" shadow="never">
          <div class="user-apply-content">
            <div class="apply-title">{{ item.name }}</div>
            <div class="apply-item" style="margin-top: 10px">报名状态：{{ item.statusStr }}</div>
            <div class="apply-item">人数限制：{{ item.limitCount }}</div>
            <div class="apply-item">
              <div class="apply-left">截止时间：</div>
              <div class="apply-right apply-limit-time-color">{{ item.applyEndTimeStr }}</div>
            </div>
            <div class="apply-item">
              <div class="apply-left">考试时间：</div>
              <div class="apply-right apply-limit-time-color">{{ item.limitStartTimeStr }} 至
                {{ item.limitEndTimeStr }}
              </div>
            </div>
            <div class="apply-item">报名时间：{{ item.applyDateTimeStr }}</div>

            <div class="apply-item apply-button" v-if="item.status === 1">
              <el-button type="danger" class="reply-cancel-button"
                         @click="cancelApply(item.applyId,item.id)">取消报名
              </el-button>
            </div>
          </div>
        </el-card>

        <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                    v-model:limit="queryParam.pageSize"
                    @pagination="search"/>

      </div>
    </el-card>
  </div>
</template>

<script>
import InfoMenu from './components/info-menu.vue'
import {record, cancelApply} from '@/api/apply'

export default {
  components: {InfoMenu},
  data() {
    return {
      queryParam: {
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: true,
      tableData: [],
      total: 0,
      applyNoneShow: false
    }
  },
  created() {
    this.search()
  },
  methods: {
    search() {
      this.listLoading = true
      record(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
        this.applyNoneShow = this.total === 0
      }).finally(() => {
        this.listLoading = false
      })
    },
    cancelApply(applyId, userApplyId) {
      this.$confirm('确定取消报名吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancelApply(applyId, userApplyId).then(re => {
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
