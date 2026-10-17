<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="120px" v-loading="formLoading" :rules="formRules">
      <el-form-item label="课件分类：">
        <el-tree-select v-model="form.courseWareArchiveId" :data="courseWareArchiveTree" check-strictly
                        :render-after-expand="true" default-expand-all placeholder="分类"/>
      </el-form-item>
      <el-form-item label="课件类型：" prop="fileType" required>
        <el-select v-model="form.fileType" clearable>
          <el-option v-for="item in EnumMap.courseWare.fileType" :key="item.key" :value="item.key"
                     :label="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="实训环境：" prop="vmType">
        <el-select v-model="form.vmType" clearable @change="changeVmType">
          <el-option v-for="item in vmTypeTree" :key="item.guid"
                     :value="item.guid"
                     :label="item.vmName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课件名称：" prop="name" required>
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="课件描述：" prop="description" required>
        <el-input v-model="form.description" type="textarea" rows="8"></el-input>
      </el-form-item>
      <el-form-item label="视频：" prop="originalPath" v-if="form.fileType===1" required :rules="{required: true, message: '请上传视频'}">
        <div class="course-ware-video">
          <el-upload accept=".mp4,.ogg,.webm" action="/api/course/ware/video/file"
                     :show-file-list="false" :on-progress="uploadProgress"
                     :on-success="uploadSuccess" :on-error="uploadError">
            <el-button type="success" size="small" class="wdd-button-mini">上传视频</el-button>
          </el-upload>
          <video :src="form.originalPath" controls="controls" class="course-ware-video-player"/>
        </div>
      </el-form-item>
      <el-form-item label="文件：" prop="originalPath" v-if="form.fileType===2" required :rules="{required: true, message: '请上传文件'}">
        <el-upload style="float: left;" accept=".pdf"
                   action="/api/course/ware/document/file" :show-file-list="false"
                   :on-progress="uploadProgress" :on-success="uploadSuccess" :on-error="uploadError">
          <el-button type="success" size="small" class="wdd-button-mini">上传文件</el-button>
        </el-upload>
        <!--<el-upload style="float: left;" accept=".doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf"
                   action="/api/course/ware/document/file" :show-file-list="false"
                   :on-progress="uploadProgress" :on-success="uploadSuccess" :on-error="uploadError">
          <el-button type="success" size="small" class="link-left wdd-button-mini">上传文件</el-button>
        </el-upload>-->
        <el-button type="success" size="small" style="margin-left: 20px;" class="wdd-button-mini"
                   @click="preView"
                   v-if="form.previewPath !== null">
          课件预览
        </el-button>
      </el-form-item>
      <el-form-item label="课件时长：" prop="maxLengthStr" v-if="form.fileType===2" required>
        <el-input class="video-question-input" placeholder="00:00:00"
                  :formatter="(value) => `${value}`.replace(/[^\d|:]/g, '')"
                  v-model.trim="form.maxLengthStr"/>
      </el-form-item>
      <el-form-item label="题目列表：">
        <el-card style="width: 100%;min-height: 200px">
          <draggable class="video-question-content" item-key="index" group="questionList" handle=".drag"
                     v-model="form.questionList">
            <template #item="{index,element }">
              <div class="video-question">
                <el-input v-if="form.vmType == null || form.vmType === ''" class="video-question-input" placeholder="00:00:00"
                          :formatter="(value) => `${value}`.replace(/[^\d|:]/g, '')"
                          v-model.trim="element.anchorFormat"/>
                <div v-html="element.title" class="video-question-content drag"></div>
                <div class="video-question-action">
                  <el-button type="danger" circle @click="removeQuestion(index)">
                    <el-icon>
                      <icon-delete/>
                    </el-icon>
                  </el-button>
                </div>
              </div>
            </template>
          </draggable>
        </el-card>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="addQuestion">添加题目</el-button>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-dialog v-model="questionPage.showDialog" width="90%" class="question-select-dialog">
      <el-form :model="questionPage.queryParam" ref="questionQueryForm" :inline="true">
        <el-form-item label="ID：">
          <el-input v-model="questionPage.queryParam.id" clearable></el-input>
        </el-form-item>
        <el-form-item label="题干：">
          <el-input v-model="questionPage.queryParam.title" clearable></el-input>
        </el-form-item>
        <el-form-item label="题目分类：">
          <tree-select v-model="questionPage.queryParam.questionArchiveIdList" :data="questionArchiveTree"
                       :clearable="true"
                       placeholder="题目分类"/>
        </el-form-item>
        <el-form-item label="题型：" v-if="questionPage.queryParam.vmType == null || questionPage.queryParam.vmType === ''">
          <el-select v-model="questionPage.queryParam.questionType" clearable>
            <el-option v-for="item in EnumMap.exam.question.typeEnum" :key="item.key" :value="item.key"
                       :label="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="题型：" v-if="questionPage.queryParam.vmType != null && questionPage.queryParam.vmType !== ''">
          <el-select v-model="questionPage.queryParam.questionType" disabled>
            <el-option v-for="item in EnumMap.exam.question.vmTypeSingleEnum" :key="item.key" :value="item.key"
                       :label="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="实训环境：" v-if="questionPage.queryParam.vmType != null && questionPage.queryParam.vmType !== ''">
          <el-select v-model="questionPage.queryParam.vmType" disabled>
            <el-option v-for="item in vmTypeTree" :key="item.guid"
                       :value="item.guid"
                       :label="item.vmName"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="questionQueryForm">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="questionPage.listLoading" ref="questionTable" :data="questionPage.tableData" border fit
                @expand-change="expandChange" @row-click="questionClick" highlight-current-row row-key="id"
                style="width: 100%">
        <el-table-column type="expand">
          <template #default="{row}">
            <QuestionShow :qType="row.question.questionType" :question="row.question"
                          class="table-question-show"/>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="Id" width="100px"/>
        <el-table-column prop="questionArchive" label="题目分类" width="100px"/>
        <el-table-column prop="typeEnumStr" label="题型" width="110px"/>
        <el-table-column label="题干">
          <template #default="{row}">
            <div v-html="row.title" class="wdd-question-content"></div>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="分数" width="60px"/>
        <el-table-column prop="difficultStr" label="难度" width="60px"/>
      </el-table>
      <pagination v-show="questionPage.total>0" :total="questionPage.total"
                  v-model:page="questionPage.queryParam.pageIndex"
                  v-model:limit="questionPage.queryParam.pageSize"
                  @pagination="questionSearch" :autoScroll="false"/>
    </el-dialog>
  </div>
</template>

<script>

import {EnumMap} from '@/api/EnumMap'
import QuestionShow from '../../question/components/Show.vue'
import {Delete as IconDelete} from '@element-plus/icons-vue'
import {select as questionSelect, page as questionPage} from '@/api/question'
import {tree as archiveTree} from '@/api/courseWareArchive'
import {tree as questionArchiveTree} from '@/api/questionArchive'
import {tree, vmList} from '@/api/department'
import {select, edit} from '@/api/courseWare'
import useStore from '@/store'
import draggable from 'vuedraggable'
import {vmTypeList as vmTypeTree} from '@/api/vmWare'
const {tagsView} = useStore()

export default {
  name: 'CourseWareEdit',
  components: {QuestionShow, IconDelete, draggable},
  data() {
    return {
      EnumMap: EnumMap,
      VmMap: [],
      vmTypeTree:[],
      form: {
        id: null,
        courseWareArchiveId: null,
        fileType: null,
        name: null,
        description: null,
        originalPath: null,
        previewPath: null,
        fileName: null,
        maxLength: null,
        maxLengthStr: null,
        departmentIdList: [],
        questionList: [],
        vmname: null,
        vmType: null
      },
      formRules: {
        fileType: [{required: true, message: '请选择课件类型'}],
        name: [{required: true, message: '请输入课件名称'}, {
          max: 255,
          message: '长度小于255个字符'
        }],
        description: [{required: true, message: '请输入课件描述'}, {
          max: 500,
          message: '长度小于500个字符'
        }],
        maxLengthStr:[{required: true, message: '请输入课件时长'}]
      },
      formLoading: false,
      departmentTree: [],
      courseWareArchiveTree: [],
      questionArchiveTree: [],
      questionPage: {
        showDialog: false,
        queryParam: {
          id: null,
          title: null,
          questionType: null,
          pageIndex: 1,
          pageSize: 10,
          questionArchiveIdList: null,
          selectIdList: [],
          vmType:null,
          exclude:true
        },
        listLoading: false,
        tableData: [],
        total: 0,
        expandKeys: []
      }
    }
  },
  created() {

    vmTypeTree().then(re => {
      this.vmTypeTree = re.response
    })
    let id = this.$route.query.id

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id).then(re => {
        this.form = re.response
        if (re.response.vmType  !== null && re.response.vmType  !== ""){
          this.questionPage.queryParam.questionType = 7;
          this.questionPage.queryParam.exclude = false;
        }
      }).finally(() => {
        this.formLoading = false
      })
    }
    vmList().then(re => {
      this.VmMap = re.response
    })

    tree().then(re => {
      this.departmentTree = re.response
    })

    archiveTree().then(re => {
      this.courseWareArchiveTree = re.response
    })

    questionArchiveTree().then(re => {
      this.questionArchiveTree = re.response
    })
  },
  methods: {
    /**
     * 改变实训环境
     * libl 2025.04.01
     */
    changeVmType(){
      this.form.questionList=[];
      if (this.form.vmType !== null && this.form.vmType !== "") {
        this.questionPage.queryParam.questionType = 7;
        this.questionPage.queryParam.exclude = false;
      } else {
        this.questionPage.queryParam.questionType = null
        this.questionPage.queryParam.exclude = true;
      }
      this.questionPage.queryParam.vmType = this.form.vmType;
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
        if (this.form.fileType === 1) {
          this.form.originalPath = re.response.path
          this.form.maxLength = re.response.videoLength
        } else {
          this.form.originalPath = re.response.originalPath
          this.form.previewPath = re.response.previewPath
        }
        this.form.fileName = re.response.fileName
        if (this.form.fileType === 1) {
          let videoSrc = this.playerOptions.sources[0]
          videoSrc.src = re.response.path
        }
      } else {
        this.$message.error(re.message)
      }
    },
    uploadError() {
      this.loading.close()
      this.$message.error('文件上传失败，请检查文件大小或文件格式')
    },
    preView() {
      window.open(this.form.previewPath, '_blank')
    },
    questionClick(row) {
      this.form.questionList.push({
        questionId: row.id,
        questionFrameId: row.questionFrameId,
        title: row.title,
        anchorSecond: 0,
        anchorFormat: ''
      })
      this.questionPage.showDialog = false
    },
    expandChange(row, expandedRows) {
      this.questionPage.expandKeys = expandedRows.map(item => item.id)
      questionSelect(row.id).then(re => {
        row.question = re.response
      })
    },
    questionQueryForm() {
      this.questionPage.queryParam.pageIndex = 1
      this.questionSearch()
    },
    removeQuestion(index) {
      this.form.questionList.splice(index, 1)
    },
    questionSearch() {
      this.questionPage.listLoading = true
      this.questionPage.queryParam.selectIdList = []
      for (const item of this.form.questionList) {
        this.questionPage.queryParam.selectIdList.push(item.questionId)
      }
      questionPage(this.questionPage.queryParam).then(data => {
        const re = data.response
        this.questionPage.tableData = re.list
        this.questionPage.total = re.total
        this.questionPage.queryParam.pageIndex = re.pageNum
        //题目展开
        this.$nextTick(() => {
          for (const item of this.questionPage.tableData) {
            let expandItem = this.questionPage.expandKeys.find(id => id == item.id)
            if (expandItem) {
              questionSelect(item.id).then(re => {
                item.question = re.response
              })
            }
          }
        })
      }).finally(() => {
        this.questionPage.listLoading = false
      })
    },
    addQuestion() {
      if (this.form.vmType !== undefined && this.form.vmType !== null && this.form.vmType !== "") {
        this.questionPage.queryParam.questionType = 7;
        this.questionPage.queryParam.exclude = false;
      } else {
        this.questionPage.queryParam.questionType = null
        this.questionPage.queryParam.exclude = true;
      }
      this.questionPage.queryParam.vmType = this.form.vmType;
      this.questionPage.showDialog = true
      this.questionSearch()
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          edit(this.form).then(data => {
            if (data.code === 1) {
              this.$message.success(data.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/train/course/ware/list')
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
    resetForm() {
      this.form = {
        id: null,
        courseWareArchiveId: null,
        fileType: null,
        name: null,
        description: null,
        originalPath: null,
        previewPath: null,
        fileName: null,
        maxLength: null,
        maxLengthStr: null,
        departmentIdList: [],
        questionList: []
      }
      this.$refs['form'].resetFields()
    }
  }
}
</script>

<style lang="scss">
.course-ware-video {
  display: flex;
  flex-direction: row;

  .course-ware-video-player {
    margin-left: 20px;
    width: 600px;
  }
}
</style>
