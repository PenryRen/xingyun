<template>
  <el-container style="min-height: 100%">
    <el-header class="user-header">
      <div>
        <img src="@/assets/logo.png" class="logo">
      </div>
      <el-menu class="el-menu-title" :ellipsis="false" mode="horizontal" :default-active="defaultUrl"
               :router="true"
               background-color="#fff" text-color="#606266" active-text-color="#68bfd6">
        <el-menu-item index="/index">首 页</el-menu-item>
        <el-menu-item index="/announcement/index">通知公告</el-menu-item>
        <el-menu-item index="/forum/index">交流圈</el-menu-item>
        <el-menu-item index="/apply/index">报名中心</el-menu-item>
        <el-menu-item index="/paper/index">试卷中心</el-menu-item>
        <el-menu-item index="/train/index">培训中心</el-menu-item>
        <el-menu-item index="/record/index">考试记录</el-menu-item>
        <el-menu-item index="/ai/index">AI 学习中心</el-menu-item>
      </el-menu>
      <div class="user-info">
        <el-dropdown placement="bottom">
          <div class="user-info-name">
            <el-icon>
              <User/>
            </el-icon>
            <span class="user-name">{{ userName }}</span>
            <span class="real-name" v-if="realName !== null && realName !== 'null' && realName !== ''"> - {{
                realName
              }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push({path:'/user/index'})">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push({path:'/user/update'})">更新信息</el-dropdown-item>
              <el-dropdown-item @click="router.push({path:'/user/passwordChange'})">修改密码
              </el-dropdown-item>
              <el-dropdown-item @click="router.push({path:'/user/apply'})">我的报名</el-dropdown-item>
              <el-dropdown-item @click="router.push({path:'/user/comment'})">我的评论</el-dropdown-item>
              <el-dropdown-item @click="router.push({path:'/user/train'})">我的培训</el-dropdown-item>
              <el-dropdown-item @click="router.push({path:'/user/credential'})">我的证书
              </el-dropdown-item>
              <el-dropdown-item @click="router.push({path:'/user/event'})">个人动态</el-dropdown-item>
              <el-dropdown-item @click="logout" divided>退出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-main class="user-main">
      <router-view/>
    </el-main>

    <el-footer height="340" class="user-footer app-item-contain">
      <div class="container">
        <div class="left">
          <!--<div class="footer-main">
            <span>新手指南</span>
            <a href="http://www.ueit.com.cn/buy.html" target="_blank">购买中心</a>
            <a href="http://www.ueit.com.cn/document/维多多培训考试系统.pdf" target="_blank">系统简介</a>
            <a href="http://www.ueit.com.cn/document/维多多培训考试系统用户手册.pdf"
               target="_blank">用户手册</a>
          </div>
          <div class="footer-main">
            <span>合作伙伴</span>
            <a>中国红十字会</a>
            <a>中国人民解放军</a>
            <a>国家标准技术审评中心</a>
            <a>中科软科技股份有限公司</a>
          </div>-->
          <div class="footer-main">
            <span>关于我们</span>
            <a href="http://127.0.0.1:5500/behavio.html" target="_blank">TEAM</a>
<!--            <a href="http://www.ueit.com.cn/wdd.html" target="_blank">产品介绍</a>-->
            <a @click="feedbackClick" class="feedback-link">意见反馈</a>
          </div>
        </div>
        <div class="right">
          <table>
            <tr>
              <td>
                <img src="@/assets/wechat/h5.png" height="100" width="100"/>
              </td>
              <!--<td>
                <img src="@/assets/wechat/2.jpg" height="100" width="100"/>
              </td>-->
            </tr>
            <tr>
              <td>
                <span>H5</span>
              </td>
              <!--<td>
                <span>微信小程序</span>
              </td>-->
            </tr>
          </table>
        </div>
      </div>
    </el-footer>
    <div class="foot-copyright">
      <span>{{ copyright }}</span>
    </div>

    <el-dialog v-model="feedbackVisible" center class="feedback-dialog">
      <el-form :model="feedbackForm" ref="feedbackFormRef" label-width="100px" :rules="feedbackFormRules">
        <el-form-item label="联系方式：" prop="contact" required>
          <el-input v-model="feedbackForm.contact" clearable></el-input>
        </el-form-item>
        <el-form-item label="意见内容：" prop="content" required>
          <el-input v-model="feedbackForm.content" type="textarea" rows="5"
                    clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="confirmFeedBack" class="wdd-primary-button-color">确定</el-button>
          <el-button @click="feedbackVisible = false">取 消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <back-to-top :visibility-height="100" :back-position="0" transition-name="fade" ref="backTop"/>
  </el-container>
</template>

<style scoped>
:deep(.el-menu-title) {
  margin-left: 32px !important;
}

:deep(.el-menu--horizontal > .el-menu-item) {
  padding: 0 14px;
}

@media (max-width: 1200px) {
  :deep(.el-menu-title) {
    margin-left: 8px !important;
  }

  :deep(.el-menu--horizontal > .el-menu-item) {
    padding: 0 8px;
    font-size: 14px !important;
  }

  .real-name {
    display: none;
  }
}

@media (max-width: 720px) {
  .user-header {
    overflow: hidden;
  }

  .logo {
    flex: 0 0 auto;
  }

  :deep(.el-menu-title) {
    width: 0;
    min-width: 0;
    flex: 1 1 auto;
    margin-left: 0 !important;
    overflow-x: auto;
    overflow-y: hidden;
    scrollbar-width: none;
  }

  :deep(.el-menu-title::-webkit-scrollbar) {
    display: none;
  }

  .user-info {
    flex: 0 0 auto;
    margin-right: 8px !important;
  }

  .user-name {
    display: none;
  }
}
</style>
<script setup lang="ts">
import {watch, reactive, ref, toRefs} from 'vue';
import {ElDialog, ElMessage, ElMessageBox} from 'element-plus';
import {User} from '@element-plus/icons-vue';
import BackToTop from '@/components/BackToTop/index.vue'
import {useRouter} from 'vue-router';
import useStore from '@/store';
import {feedback} from '@/api/user'

const {user} = useStore();
const router = useRouter();

const userName = user.userName;
const realName = user.realName;
const copyright = import.meta.env.VITE_APP_COPYRIGHT;

const feedbackFormRef = ref(ElDialog);

const state = reactive({
  defaultUrl: '/index',
  feedbackVisible: false,
  feedbackForm: {
    contact: null,
    content: null
  },
  feedbackFormRules: {
    contact: [
      {required: true, message: '请输入联系方式'},
      {max: 255, message: '长度小于255个字符'},
      {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur"}
    ],
    content: [
      {required: true, message: '请输入意见内容'},
      {max: 500, message: '长度小于500个字符'}
    ]
  }
});

const {
  defaultUrl, feedbackVisible, feedbackForm, feedbackFormRules
} = toRefs(state);

state.defaultUrl = routeSelect(router.currentRoute.value.path) as any

function routeSelect(path) {
  const pathTree = [{root: '/', child: []},
    {root: '/index', child: []},
    {root: '/announcement/index', child: ['/announcement/detail']},
    {root: '/forum/index', child: ['/forum/add', '/forum/select']},
    {root: '/apply/index', child: []},
    {root: '/paper/index', child: []},
    {root: '/train/index', child: ['/train/detail']},
    {root: '/record/index', child: []},
    {root: '/ai/index', child: []},
    {root: '/credential/index', child: []}
  ]

  for (let i = 0; i < pathTree.length; i++) {
    let item = pathTree[i]
    if (item.root === path) {
      return item.root
    }
    if (item.child.indexOf(path) !== -1) {
      return item.root
    }
  }

  return null
}


function logout() {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    user
        .logout()
        .then(() => {
          router.push(`/login`);
        });
  });
}

function feedbackClick() {
  state.feedbackForm = {
    contact: null,
    content: null
  }
  state.feedbackVisible = true
}

function confirmFeedBack() {
  feedbackFormRef.value.validate((valid) => {
    if (valid) {
      feedback(state.feedbackForm).then(data => {
        if (data.code === 1) {
          state.feedbackVisible = false
          ElMessage.success('意见反馈提交成功')
        } else {
          ElMessage.error(data.message)
        }
      })
    }
  })
}


watch(() => router.currentRoute.value.path, (toPath) => {
  state.defaultUrl = routeSelect(toPath) as any
})
</script>
