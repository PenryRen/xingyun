<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="标题：">
        <el-input v-model="queryParam.title" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['announcement:page']">查询</el-button>
        <el-button @click="clearForm">重置</el-button>
        <router-link :to="{path:'/announcement/edit'}" class="link-left">
          <el-button type="primary" v-has-perm="['announcement:create']">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="90px"/>
      <el-table-column prop="imagePath" label="封面" width="105px">
        <template #default="{row}">
          <el-image style="width: 77px; height: 44px" :src="row.imageSrc"
                    :preview-src-list="[row.imageSrc]" :hide-on-click-modal="true" :preview-teleported="true"
                    fit="fill"/>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题"/>
      <el-table-column prop="overheadStr" label="是否顶置" width="90px"/>
      <el-table-column prop="importantedStr" label="是否重要" width="90px"/>
      <el-table-column prop="departmentNameList" label="发布班级"/>
      <el-table-column prop="createTime" label="创建时间" width="160px"/>
      <el-table-column width="210px" label="操作" align="center">
        <template #default="{row}">
          <router-link :to="{path:'/announcement/edit', query:{id:row.id}}">
            <el-button v-has-perm="['announcement:update']" size="small" class="wdd-button-mini">编辑
            </el-button>
          </router-link>
          <router-link :to="{path:'/announcement/show', query:{id:row.id}}" class="link-left">
            <el-button v-has-perm="['announcement:show']" size="small" class="wdd-button-mini">查看
            </el-button>
          </router-link>
          <el-button type="danger" @click="announcementDelete(row)" size="small"
                     class="wdd-button-mini link-left"
                     v-has-perm="['announcement:delete']">删除
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
import {page, deleteAnnouncement} from '@/api/announcement'

export default {
  name: 'AnnouncementList',
  data() {
    return {
      queryParam: {
        title: null,
        pageIndex: 1,
        pageSize: 10
      },
      tableData: [],
      total: 0,
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
        title: null,
        pageIndex: 1,
        pageSize: 10
      }
      this.search()
    },
    announcementDelete(row) {
      this.$confirm('此操作将永久删除该通知公告, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAnnouncement(row.id).then(re => {
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
