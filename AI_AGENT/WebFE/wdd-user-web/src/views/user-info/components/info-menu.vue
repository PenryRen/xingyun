<template>
  <div class="left">
    <span class="title">个人中心</span>
    <router-link :to="{path:'/user/index'}" :class="urlClass('/user/index')">
      <svg-icon icon-class="user-index"/>
      <span>个人信息</span>
    </router-link>
    <router-link :to="{path:'/user/update'}" :class="urlClass('/user/update')">
      <svg-icon icon-class="user-update"/>
      <span>更新信息</span>
    </router-link>
    <router-link :to="{path:'/user/passwordChange'}" :class="urlClass('/user/passwordChange')">
      <svg-icon icon-class="user-password-change"/>
      <span>修改密码</span>
    </router-link>
    <router-link :to="{path:'/user/apply'}" :class="urlClass('/user/apply')">
      <svg-icon icon-class="user-apply"/>
      <span>我的报名</span>
    </router-link>
    <router-link :to="{path:'/user/comment'}" :class="urlClass('/user/comment')">
      <svg-icon icon-class="user-comment"/>
      <span>我的评论</span>
    </router-link>
    <router-link :to="{path:'/user/train'}" :class="urlClass('/user/train')">
      <svg-icon icon-class="user-train" size="16"/>
      <span>我的培训</span>
    </router-link>
    <router-link :to="{path:'/user/credential'}" :class="urlClass('/user/credential')">
      <svg-icon icon-class="user-credential" size="16"/>
      <span>我的证书</span>
    </router-link>
    <router-link :to="{path:'/user/event'}" :class="urlClass('/user/event')">
      <svg-icon icon-class="user-event" size="16"/>
      <span>个人动态</span>
    </router-link>
  </div>
</template>


<script setup lang="ts">
import SvgIcon from '@/components/SvgIcon/index.vue';

const props = defineProps({
  url: {
    type: String,
    default: ''
  }
});


function urlClass(menuUrl) {
  return props.url === menuUrl ? 'link-active' : 'link'
}
</script>
