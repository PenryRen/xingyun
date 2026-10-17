<template>
  <div class="app-item-contain">
    <el-card class="user-index-info-contain user-train-info" shadow="never">

      <info-menu url="/user/train"/>

      <div class="right">
        <div v-for="item in tableData" :key="item.id" class="train-list">
          <div class="train-item">
            <img class="course-image" :src="item.coverPath" @click="link(item)"/>
            <div class="user-train-right">
              <div class="train-title" @click="link(item)">{{ item.name }}</div>
              <div class="train-description">{{ item.description }}</div>
              <div class="train-foot">
                <div>{{ item.statusStr }}</div>
                <div>{{ item.createTimeStr }}</div>
              </div>
            </div>
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
import {train} from '@/api/user'

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
    link(item) {
      this.$router.push(`/train/detail?id=${item.trainId}`)
    },
    search() {
      this.listLoading = true
      train(this.queryParam).then(data => {
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
