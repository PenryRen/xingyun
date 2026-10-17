<template>
  <div class="app-item-contain">
    <el-card class="user-index-info-contain user-index-info" shadow="never">

      <info-menu url="/user/index"/>

      <div class="center">
        <div>
          <span>用户名：</span><span>{{ userInfo.userName }}</span>
        </div>
        <div>
          <span>姓名：</span><span>{{ userInfo.realName }}</span>
        </div>
        <div>
          <span>工号：</span><span>{{ userInfo.workNo }}</span>
        </div>
        <div>
          <span>身份证号：</span><span>{{ userInfo.idCard }}</span>
        </div>
        <div>
          <span>邮箱：</span><span>{{ userInfo.email }}</span>
        </div>
        <div>
          <span>手机号：</span><span>{{ userInfo.phone }}</span>
        </div>
        <div>
          <span>性别：</span><span>{{ sexFormatter(userInfo.sex) }}</span>
        </div>
        <div>
          <span>年龄：</span><span>{{ userInfo.age }}</span>
        </div>
        <div>
          <span>出生日期：</span><span>{{ userInfo.birthDay }}</span>
        </div>
        <div>
          <span>注册时间：</span><span>{{ userInfo.createTime }}</span>
        </div>
      </div>
      <div class="right">
        <div>
          <span>班级：</span><span>{{ userInfo.departmentStr }}</span>
        </div>
        <div>
          <span>职位：</span><span>{{ userInfo.jobTitle }}</span>
        </div>
        <el-avatar :src="userInfo.imagePath" shape="circle" fit="scale-down" :size="100" class="user-image">暂无头像
        </el-avatar>
      </div>
    </el-card>
  </div>
</template>

<script>
import {EnumMap, Format} from '@/api/EnumMap'
import InfoMenu from './components/info-menu.vue'
import {getCurrentUser} from '@/api/user'

export default {
  components: {InfoMenu},
  data() {
    return {
      userInfo: {}
    }
  },
  created() {
    this.infoLoad()
  },
  methods: {
    infoLoad() {
      getCurrentUser().then(re => {
        this.userInfo = re.response
      })
    },
    sexFormatter(value) {
      return Format(EnumMap.user.sexEnum, value)
    }
  }
}
</script>
