<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="150px" v-loading="formLoading" :rules="formRules">
      <el-form-item label="模板名称：" prop="name" required>
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="发证机构：" prop="company" required>
        <el-input v-model="form.company"></el-input>
      </el-form-item>
      <el-form-item label="模板图片：" prop="templateImagePath" required>
        <el-upload style="float: left;" accept=".jpg,.png" :data="{folder:'credential'}"
                   action="/api/upload/folder/file"
                   :show-file-list="false"
                   :on-progress="uploadProgress" :on-success="uploadSuccess" :on-error="uploadError">
          <el-button size="small" class="wdd-button-mini link-left" type="success">上传模板</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="证书配置：">
        <el-card>
          <div class="credential-image-div">
            <div class="credential-left">
              <div class="credential-left-item">
                <div class="credential-left-item-name">节点类型：</div>
                <el-select v-model="elementCurrent.type" placeholder="节点类型"
                           class="credential-left-item-content"
                           @change="typeChange">
                  <el-option v-for="item in EnumMap.credential.textTypeEnum" :key="item.key"
                             :value="item.key"
                             :label="item.value"></el-option>
                </el-select>
              </div>
              <div class="credential-left-item" v-if="fontTextShow">
                <div class="credential-left-item-name">文字：</div>
                <el-input v-model="elementCurrent.text"
                          class="credential-left-item-content"></el-input>
              </div>
              <div class="credential-left-item" v-if="show.validityShow">
                <div class="credential-left-item-name">有效期：</div>
                <el-input-number v-model="elementCurrent.validityMonth" :precision="0" :step="1"
                                 :max="100" :min="1"
                                 class="credential-left-item-content"
                                 placeholder="月"></el-input-number>
              </div>
              <div class="credential-left-item" v-if="show.fontWeightShow">
                <div class="credential-left-item-name">文字粗细：</div>
                <el-select v-model="elementCurrent.fontWeight" placeholder="文字粗细"
                           class="credential-left-item-content">
                  <el-option v-for="item in EnumMap.credential.fontWeightTypeEnum" :key="item.key"
                             :value="item.key"
                             :label="item.value"></el-option>
                </el-select>
              </div>
              <div class="credential-left-item" v-if="show.fontSizeShow">
                <div class="credential-left-item-name">文字大小：</div>
                <el-input-number v-model="elementCurrent.fontSize" :precision="0" :step="1" :max="100"
                                 :min="1"
                                 class="credential-left-item-content"></el-input-number>
              </div>
              <div class="credential-left-item" v-if="show.fontColorShow">
                <div class="credential-left-item-name">文字颜色：</div>
                <el-color-picker v-model="elementCurrent.fontColor"></el-color-picker>
              </div>
              <div class="credential-left-item credential-left-add">
                <el-button type="success" @click="itemClick">添加</el-button>
              </div>
            </div>
            <div class="credential-right" id="credential-image-contain">
              <img :src="form.templateImagePath" class="credential-image" id="credential-image"
                   ref="credentialImage">
            </div>
          </div>
        </el-card>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-has-perm="['credential:create','credential:update']">提交
        </el-button>
        <el-button @click="resetForm">重置</el-button>
        <el-button @click="previewForm" type="success">预览</el-button>
      </el-form-item>
    </el-form>
    <el-dialog v-model="dialog.visible" append-to-body :close-on-click-modal="true" :show-close="true" center>
      <el-image :src="dialog.url"/>
    </el-dialog>
  </div>
</template>

<script>
import $ from 'jquery'
import {v4} from 'uuid'
import {EnumMap, Format} from '@/api/EnumMap'
import {select, edit, preview} from '@/api/credential'
import useStore from '@/store'

const {tagsView} = useStore()
export default {
  name: 'CredentialEdit',
  data() {
    return {
      EnumMap: EnumMap,
      form: {
        id: null,
        name: null,
        company: null,
        templateImagePath: '',
        credentialItemVMList: []
      },
      elementCurrent: {
        key: v4(),
        type: 1,
        text: '考试名称',
        validityMonth: null,
        fontWeight: 1,
        fontSize: 22,
        fontColor: '#000000',
        elementX: 0,
        elementY: 0,
        elementLeft: '0px',
        elementTop: '0px'
      },
      show: {
        validityShow: false,
        fontWeightShow: true,
        fontSizeShow: true,
        fontColorShow: true
      },
      fontTextShow: false,
      formRules: {
        name: [{required: true, message: '请输入证书模板名称'}, {
          max: 255,
          message: '长度小于255个字符'
        }],
        company: [{required: true, message: '请输入发证机构名称'}, {
          max: 255,
          message: '长度小于255个字符'
        }],
        templateImagePath: [{required: true, message: '请上传证书模板'}, {
          max: 255,
          message: '长度小于255个字符'
        }]
      },
      formLoading: false,
      dialog: {
        visible: false,
        url: null
      }
    }
  },
  mounted() {
    let id = this.$route.query.id

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id).then(re => {
        this.form = re.response
        for (const credentialItemVMListElement of this.form.credentialItemVMList) {
          this.elementInit(credentialItemVMListElement)
        }
      }).finally(() => {
        this.formLoading = false
      })
    }
  },
  methods: {
    typeChange(value) {
      this.elementCurrent.elementLeft = '0px'
      this.elementCurrent.elementTop = '0px'
      this.elementCurrent.text = Format(EnumMap.credential.textTypeEnum, value)

      if (value === 5) {
        this.elementCurrent.validityMonth = null
        this.show.validityShow = true
      } else {
        this.show.validityShow = false
      }

      if (value === 6) {
        this.elementCurrent.fontWeight = 1
        this.show.fontWeightShow = false
        this.show.fontSizeShow = false
        this.show.fontColorShow = false
      } else {
        this.show.fontWeightShow = true
        this.show.fontSizeShow = true
        this.show.fontColorShow = true
      }

      if (value === 7) {
        this.elementCurrent.text = ''
        this.fontTextShow = true
      } else {
        this.fontTextShow = false
      }
    },
    itemClick() {
      if (this.elementCurrent.text === '') {
        this.$message.error('文字不能为空')
        return
      }
      if (this.elementCurrent.fontColor === null || this.elementCurrent.fontColor === '') {
        this.$message.error('文字颜色不能为空')
        return
      }

      if (this.elementCurrent.type === 5 && this.elementCurrent.validityMonth === null) {
        this.$message.error('有效期不能为空')
        return
      }
      this.elementCurrent.key = v4()
      let copyEl = JSON.parse(JSON.stringify(this.elementCurrent))
      this.form.credentialItemVMList.push(copyEl)
      this.elementInit(copyEl)
    },
    elementInit(elementCurrent) {
      let updatePosition = this.updatePosition
      let credentialImageRef = this.$refs.credentialImage
      let fontWeight = elementCurrent.fontWeight === 2 ? 'bold' : 'normal'
      let divClass = elementCurrent.type === 6 ? 'image-seal' : 'image-font'
      $('<div>', {
        text: elementCurrent.text,
        class: divClass,
        style: `color:${elementCurrent.fontColor};font-size:${elementCurrent.fontSize}px;font-weight:${fontWeight};left:${elementCurrent.elementLeft};top:${elementCurrent.elementTop}`,
        'data-key': elementCurrent.key,
        dblclick: this.elementDBClick,
        mousedown: function (e) {
          let target = e.target
          let disX = e.clientX - target.offsetLeft
          let disY = e.clientY - target.offsetTop
          let textEl = e.target.getBoundingClientRect()

          document.onmousemove = (e) => {
            let imageEl = credentialImageRef.getBoundingClientRect()
            let left = e.clientX - disX
            if (left >= 0 && left <= (imageEl.width - textEl.width)) {
              target.style.left = left + 'px'
            }
            let top = e.clientY - disY
            if (top >= 0 && top <= (imageEl.height - textEl.height)) {
              target.style.top = top + 'px'
            }
          }
          document.onmouseup = (e) => {
            document.onmousemove = null
            document.onmouseup = null
            updatePosition(e)
          }
        }
      }).insertAfter($('#credential-image'))
    },
    elementDBClick(e) {
      let key = e.target.dataset.key
      let i = 0
      for (; i < this.form.credentialItemVMList.length; i++) {
        const elementItemElement = this.form.credentialItemVMList[i]
        if (elementItemElement.key === key) {
          this.elementCurrent = elementItemElement
          if (this.elementCurrent.type === 5) {
            this.show.validityShow = true
          } else {
            this.show.validityShow = false
          }

          if (this.elementCurrent.type === 6) {
            this.elementCurrent.fontWeight = 1
            this.elementCurrent.fontColor = '#FF0000'
            this.show.fontWeightShow = false
            this.show.fontSizeShow = false
            this.show.fontColorShow = false
          } else {
            this.show.fontWeightShow = true
            this.show.fontSizeShow = true
            this.show.fontColorShow = true
          }

          if (this.elementCurrent.type === 7) {
            this.fontTextShow = true
          } else {
            this.fontTextShow = false
          }
          break
        }
      }
      this.form.credentialItemVMList.splice(i, 1)
      e.target.remove()
    },
    updatePosition(e) {
      let key = e.target.dataset.key
      let textEl = e.target.getBoundingClientRect()
      let imageEl = this.$refs.credentialImage.getBoundingClientRect()
      for (const elementItemElement of this.form.credentialItemVMList) {
        if (elementItemElement.key === key) {
          elementItemElement.elementLeft = e.target.style.left
          elementItemElement.elementTop = e.target.style.top
          elementItemElement.elementX = textEl.left - imageEl.left
          elementItemElement.elementY = textEl.top - imageEl.top
          break
        }
      }
    },
    uploadProgress() {
      this.loading = this.$loading({
        lock: true,
        text: '文件上传中…',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
    },
    uploadSuccess(re, file) {
      this.loading.close()
      if (re.code === 1) {
        this.form.templateImagePath = re.response.path
      } else {
        this.$message.error(re.message)
      }
    },
    uploadError() {
      this.loading.close()
      this.$message.error('文件上传失败，请检查文件大小或文件格式')
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if(this.form.credentialItemVMList.length === 0){
            this.$message.error("请添加证书节点");
            return;
          }
          this.formLoading = true
          edit(this.form).then(data => {
            if (data.code === 1) {
              this.$message.success(data.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/credential/list')
              })
            } else {
              this.$message.error(data.message)
            }
          }).finally(() => {
            this.formLoading = false
          })
        }
      })
    },
    previewForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if(this.form.credentialItemVMList.length === 0){
            this.$message.error("请添加证书节点");
            return;
          }
          this.formLoading = true
          preview(this.form).then(data => {
            if (data.code === 1) {
              this.dialog.url = data.response
              this.dialog.visible = true
            } else {
              this.$message.error(data.message)
            }
          }).finally(() => {
            this.formLoading = false
          })
        }
      })
    },
    resetForm() {
      window.location.reload()
    }
  }
}
</script>
