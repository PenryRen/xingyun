<template>
  <div class="app-item-contain">

    <div class="announcement-content">
      <div class="left">
        <el-card shadow="never" class="card-none-border" header="公告分类">
          <el-tree class="filter-tree" :data="announcementArchiveTree" :props="defaultProps" show-checkbox
                   default-expand-all node-key="id" highlight-current @check="treeCheck" ref="tree">
          </el-tree>
        </el-card>
      </div>
      <div class="right">
        <div class="announcement-card-contain" v-loading="listLoading">
          <none-show v-if="announcementNoneShow"/>
          <div v-for="(item, index) in tableData" :key="index">
            <div class="announcement-index-contain">
              <img :src="item.imageSrc" class="announcement-image">
              <div class="announcement-title-contain">
                <router-link :to="{path:'/announcement/detail',query:{id:item.id}}"
                             class="announcement-title announcement-link">
                  <el-tag effect="dark" type="" class="announcement-tag" v-if="item.overhead">顶置
                  </el-tag>
                  <el-tag effect="dark" type="danger" class="announcement-tag"
                          v-if="item.importanted">重要
                  </el-tag>
                  {{ item.title }}
                </router-link>
                <div class="announcement-time">
                  <div>发布者：{{ item.createRealName }}（{{ item.createUserName }}）</div>
                  <div>发布时间：{{ item.createTime }}</div>
                </div>
              </div>
            </div>
            <div class="line" v-if="index !== tableData.length-1"></div>
          </div>

          <pagination v-show="total>0" :total="total" v-model:page="queryParam.pageIndex"
                      v-model:limit="queryParam.pageSize"
                      @pagination="search"/>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import {tree, page} from '@/api/announcement'

export default {
  data() {
    return {
      defaultProps: {
        children: 'child',
        label: 'name'
      },
      queryParam: {
        announcementArchiveIdList: [],
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: true,
      tableData: [],
      total: 0,
      announcementArchiveTree: [],
      announcementNoneShow: false
    }
  },
  created() {
    tree().then(re => {
      this.announcementArchiveTree = re.response
    })

    this.search()
  },
  methods: {
    treeCheck(data, checked) {
      this.queryParam.announcementArchiveIdList = checked.checkedKeys
      this.queryParam.pageIndex = 1
      this.search()
    },
    search() {
      this.listLoading = true
      page(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.announcementNoneShow = this.total === 0
        this.queryParam.pageIndex = re.pageNum
      }).finally(() => {
        this.listLoading = false
      })
    }
  }
}
</script>
