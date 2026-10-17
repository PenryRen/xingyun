<template>
  <div class="app-item-contain">
    <el-card class="user-index-info-contain user-event-info" shadow="never">

      <info-menu url="/user/event"/>

      <div class="right">
        <div v-for="item in tableData" :key="item.id" class="event-list">
          <div class="item">
            <span class="content">{{ item.content }}</span>
            <span class="createTime">{{ item.createTime }}</span>
          </div>
        </div>
        <div class="page-list-top">
          <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                      v-model:limit="queryParam.pageSize"
                      @pagination="search"/>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>

import InfoMenu from './components/info-menu.vue'
import {event} from '@/api/user'

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
      total: 0
    }
  },
  created() {
    this.search()
  },
  methods: {
    search() {
      this.listLoading = true
      event(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
      }).finally(() => {
        this.listLoading = false
      })
    }
  }
}
</script>
