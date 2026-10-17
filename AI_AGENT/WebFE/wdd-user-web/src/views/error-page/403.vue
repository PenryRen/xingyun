<template>
  <div class="errPage-container">
    <el-row>
      <el-col :span="12">
        <h1 class="text-jumbo text-ginormous">
          错误!
        </h1>
        <h2>{{errMsg}}</h2>
        <h6>{{errDes}}</h6>
      </el-col>
      <el-col :span="12">
        <img :src="errGif" width="313" height="428" alt="Girl has dropped her ice cream.">
      </el-col>
    </el-row>
  </div>
</template>

<script>
import errGif from '@/assets/403_images/403.gif'
import useStore from '@/store';
import {refreshExpiration} from '@/api/vmWare'

export default {
  name: 'Page403',
  data() {
    return {
      errGif: errGif + '?' + +new Date(),
      errMsg:'',
      errDes:''
    }
  },
  created() {
    const {user} = useStore()
    if (user.errMsg && user.errMsg !== '' && user.errMsg !=="undefined" && user.errMsg !== undefined) {
      this.errMsg = user.errMsg;
    }
    if(user.expiration!== undefined && user.expiration === true){
      this.errDes = '对不起，当前系统授权过期，请不要进行非法操作！您可以尝试联系管理员';
      refreshExpiration().then(re => {})
    } else {
      this.$router.push({ path: '/' })
    }
  },
  methods: {
    back() {
      if (this.$route.query.noGoBack) {
        this.$router.push({ path: '/' })
      } else {
        this.$router.go(-1)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .errPage-container {
    width: 800px;
    max-width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    margin: 0 auto;
    .el-row{
      width: 100%;
    }
    .h1, .h2, .h3, .h4, .h5, .h6, h1, h2, h3, h4, h5, h6 {
      font-family: inherit;
      font-weight: 500;
      line-height: 1.1;
      color: inherit;
    }
    .pan-back-btn {
      background: #008489;
      color: #fff;
      border: none!important;
    }
    .pan-gif {
      margin: 0 auto;
      display: block;
    }
    .pan-img {
      display: block;
      margin: 0 auto;
      width: 100%;
    }
    .text-jumbo {
      font-size: 60px;
      font-weight: 700;
      color: #484848;
    }
    .list-unstyled {
      font-size: 14px;
      li {
        padding-bottom: 5px;
      }
      a {
        color: #008489;
        text-decoration: none;
        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
</style>
