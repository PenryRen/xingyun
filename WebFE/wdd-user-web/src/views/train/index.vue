<template>
  <div class="app-item-contain course-ware-contain">

    <div class="course-index-header">
      <div class="item-contain">
        <span class="title">课程分类：</span>
        <div class="item-radio">
          <el-radio-group v-model="queryParam.trainArchiveId">
            <el-radio v-for="item in trainArchiveList" :key="item.id" :label="item.id"
                      @click.prevent="trainArchiveChange(item.id)">{{ item.name }}
            </el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>

    <div class="course-index-content">
      <div class="course-card-contain" v-loading="listLoading">
        <none-show v-if="trainNoneShow"/>
        <router-link v-for="item in tableData" :key="item.id" :to="{path:'/train/detail', query:{id:item.id}}"
                     class="urlLink">
          <el-card class="course-card" shadow="hover">
            <img class="course-image" :src="item.coverPath"/>
            <div class="course-name">
              {{ item.name }}
            </div>
            <div class="course-child-item">
              共{{ item.itemCount }}节 | {{ item.studyTimeStr }}
            </div>
          </el-card>
        </router-link>
      </div>
    </div>

    <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                v-model:limit="queryParam.pageSize"
                @pagination="search"/>

  </div>
</template>

<script>

import {list, page} from '@/api/train'

export default {
  data() {
    return {
      queryParam: {
        trainArchiveId: null,
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: true,
      tableData: [],
      total: 0,
      trainArchiveList: [],
      trainNoneShow: false
    }
  },
  created() {
    list().then(re => {
      this.trainArchiveList = re.response
    })

    this.search()
  },
  methods: {
    trainArchiveChange(id) {
      if (id === this.queryParam.trainArchiveId) {
        this.queryParam.trainArchiveId = null
      } else {
        this.queryParam.trainArchiveId = id
      }
      this.search()
    },
    search() {
      this.listLoading = true
      page(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.trainNoneShow = this.total === 0
        this.queryParam.pageIndex = re.pageNum
      }).finally(() => {
        this.listLoading = false
      })
    }
  }
}
</script>
