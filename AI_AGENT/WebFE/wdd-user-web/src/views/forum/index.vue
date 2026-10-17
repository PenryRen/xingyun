<template>

  <div class="app-item-contain exam-paper-contain">
    <div class="forum-content">
      <div class="left">
        <el-card shadow="never" class="card-none-border">
          <template #header>
            <el-button type="primary" class="wdd-primary-button-color public-forum" @click="forumAdd">发布文章
            </el-button>
          </template>
          <el-tree
              class="filter-tree" :data="forumArchiveTree" :props="defaultProps" show-checkbox
              node-key="id" highlight-current @check="treeCheck"
              ref="tree">
          </el-tree>
        </el-card>
      </div>
      <div class="right">
        <none-show v-if="forumNoneShow"/>
        <div>
          <div class="forum-card-contain">
            <router-link v-for="item in tableData" :key="item.id"
                         :to="{path:'/forum/select', query:{id:item.id}}"
                         class="urlLink" target="_blank">
              <el-card class="forum-card" shadow="never">
                <div class="forum-title">
                  {{ item.title }}
                </div>
                <div class="forum-content">
                  {{ item.content }}
                </div>
                <div class="forum-foot">
                  <el-avatar :src="item.imagePath" shape="circle" fit="scale-down" :size="24">无
                  </el-avatar>
                  <span class="forum-foot-item" style="margin-left: 5px">{{
                                        item.realName
                                        }}（{{ item.userName }}）</span>
                  <span class="forum-foot-item">时间：{{ item.createTime }}</span>
                  <span class="forum-foot-item">分类：{{ item.forumArchive }}</span>
                </div>
              </el-card>
            </router-link>
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
import {tree, page} from '@/api/forum'

export default {
  data() {
    return {
      queryParam: {
        forumArchiveIdList: [],
        pageIndex: 1,
        pageSize: 10
      },
      forumNoneShow: false,
      listLoading: false,
      tableData: [],
      total: 0,
      forumArchiveTree: [],
      defaultProps: {
        children: 'child',
        label: 'name'
      },
      searchContent: null
    }
  },
  created() {
    tree().then(re => {
      this.forumArchiveTree = re.response
    })
    this.search()
  },
  methods: {
    treeCheck(data, checked) {
      this.queryParam.forumArchiveIdList = checked.checkedKeys
      this.queryParam.pageIndex = 1
      this.search()
    },
    forumAdd() {
      this.$router.push({path: '/forum/add'})
    },
    search() {
      this.listLoading = true
      page(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
        this.forumNoneShow = this.total === 0
      }).finally(() => {
        this.listLoading = false
      })
    }
  }
}
</script>
