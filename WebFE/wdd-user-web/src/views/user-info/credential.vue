<template>
  <div class="app-item-contain">
    <el-card class="user-index-info-contain user-credential-info" shadow="never">

      <info-menu url="/user/credential"/>

      <div class="right">
        <div class="credential-contain">
          <div class="credential-card-contain" v-loading="listLoading">
            <none-show v-if="paperNoneShow"/>
            <el-card v-for="(item,index) in tableData" :key="item.id" class="credential-card"
                     shadow="hover">
              <el-image :src="item.credentialImagePath" :initial-index="index"
                        :preview-src-list="credentialImageList" :hide-on-click-modal="true" fit="fill"/>
            </el-card>
          </div>

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
import {page} from '@/api/credential'

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
      credentialImageList: [],
      total: 0,
      paperNoneShow: false
    }
  },
  created() {
    this.search()
  },
  methods: {
    search() {
      this.listLoading = true
      page(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
        this.paperNoneShow = this.total === 0
        this.credentialImageList = []
        for (const item of re.list) {
          this.credentialImageList.push(item.credentialImagePath)
        }
      }).finally(() => {
        this.listLoading = false
      })
    }
  }
}
</script>
