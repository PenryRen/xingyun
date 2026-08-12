<template>
    <div class="navbar">
        <hamburger
                id="hamburger-container"
                :is-active="sidebar.opened"
                class="hamburger-container"
                @toggleClick="toggleSideBar"
        />

        <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>

        <div class="right-menu">
            <el-dropdown
                    class="avatar-container right-menu-item hover-effect"
                    trigger="click"
            >
                <div class="avatar-wrapper">
                    <span>{{ userName }}</span>
                    <CaretBottom style="width: 0.6em; height: 0.6em; margin-left: 5px"/>
                </div>

                <template #dropdown>
                    <el-dropdown-menu>
                        <router-link to="/">
                            <el-dropdown-item>主页</el-dropdown-item>
                        </router-link>
                        <router-link to="/profile/index">
                            <el-dropdown-item>简介</el-dropdown-item>
                        </router-link>
                        <el-dropdown-item divided @click="logout">
                            退出
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </div>
        <div class="vm-expiration" :class="{'is-local-development': isLocalDevelopment}">{{expiration}}</div>
    </div>
</template>
<script setup lang="ts">
import {computed, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {ElMessageBox} from 'element-plus';
import {getExpiration} from '@/api/vmWare'

import useStore from '@/store';

// 组件依赖
import Breadcrumb from '@/components/Breadcrumb/index.vue';
import Hamburger from '@/components/Hamburger/index.vue';

// 图标依赖
import {CaretBottom} from '@element-plus/icons-vue';

const {app, user, tagsView} = useStore();

const route = useRoute();
const router = useRouter();

const sidebar = computed(() => app.sidebar);
const userName = computed(() => user.userName);
const isLocalDevelopment = import.meta.env.VITE_APP_LOCAL_DEVELOPMENT === 'true';
const expiration = ref(isLocalDevelopment ? "本地开发模式（虚拟机功能已禁用）" : "");

function toggleSideBar() {
    app.toggleSidebar();
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
                tagsView.delAllViews();
            })
            .then(() => {
                router.push(`/login?redirect=${route.fullPath}`);
            });
    });
}
function queryExpiration() {
  if (isLocalDevelopment) {
    return;
  }
  getExpiration(null).then(re => {
    if (null != re.response) {
      expiration.value = "授权到期时间："+re.response;
    }
  })
}
queryExpiration();
</script>

<style lang="scss" scoped>
ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        position: relative;
        height: 100%;
        display: flex;
        align-items: center;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
  .vm-expiration{
    display: inline-block;
    padding: 0 8px;
    height: 100%;
    font-size: 16px;
    color: #5a5e66;
    float: right;
    line-height: 50px;
    margin-right: 30px;

    &.is-local-development {
      color: #b26a00;
      font-weight: 600;
    }
  }
}
</style>
