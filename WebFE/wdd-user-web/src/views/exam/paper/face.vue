<template>
  <div class="face-div">
    <el-card header="人脸识别">
      <div class="face-dialog">
        <video id="camera-video" width="180" height="150" autoplay="autoplay"></video>
        <canvas id="camera-canvas" width="640" height="480" style="display: none"></canvas>
        <div class="face-button">
          <el-button type="primary" @click="faceConfirm">确定</el-button>
          <el-button @click="faceClose">关闭</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>

import {faceCheck} from '@/api/examPaper'

export default {
  data() {
    return {
      id: null,
      paperType: null,
      camera: {
        initSuccess: false,
        video: null,
        canvas: null,
        context: null
      }
    }
  },
  created() {
    this.id = this.$route.query.id
    this.paperType = this.$route.query.type
    this.$nextTick(function () {
      this.cameraInit()
    })
  },
  methods: {
    cameraInit() {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      let _this = this

      if (_this.camera.initSuccess) {
        _this.camera.video.play()
        return
      }

      this.camera.video = document.getElementById('camera-video')
      this.camera.canvas = document.getElementById('camera-canvas')
      this.camera.context = this.camera.canvas.getContext('2d')

      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {}
      }
      if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function (constraints) {
          let getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia
          if (!getUserMedia) {
            return Promise.reject(new Error('getUserMedia is not implemented in this browser'))
          }
          return new Promise(function (resolve, reject) {
            getUserMedia.call(navigator, constraints, resolve, reject)
          })
        }
      }
      let constraints = {video: true}
      navigator.mediaDevices.getUserMedia(constraints)
          .then(function (stream) {
            if ('srcObject' in _this.camera.video) {
              _this.camera.video.srcObject = stream
            } else {
              _this.camera.video.src = window.URL.createObjectURL(stream)
            }
            _this.camera.video.onloadedmetadata = function (e) {
              _this.camera.initSuccess = true
              _this.camera.video.play()
            }
          })
          // eslint-disable-next-line handle-callback-err
          .catch(function (err) {
            _this.camera.initSuccess = false
            _this.$message.error('摄像头打开失败，请检查权限问题')
          })
    },
    faceConfirm() {
      if (!this.camera.initSuccess) {
        this.$message.error('摄像头打开失败，请检查权限问题')
        return
      }
      this.camera.context.drawImage(this.camera.video, 0, 0, 640, 480)
      let imageBase64 = this.camera.canvas.toDataURL('image/png').substring(22)
      faceCheck({id: this.id, paperType: this.paperType, imageBase: imageBase64}).then(re => {
        if (re.code === 1) {
          /*this.$router.push({path: '/do', query: {id: this.id, type: this.paperType}})*/
          window.open(`#/do?id=${this.id}&type=${this.paperType}`, '_blank')
          window.close()
        } else {
          this.$message.error(re.message)
        }
      })
    },
    faceClose() {
      window.close()
    }
  }
}
</script>
