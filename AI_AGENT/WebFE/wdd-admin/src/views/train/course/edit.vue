<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="120px" v-loading="formLoading" :rules="formRules">
      <el-form-item label="课程分类：">
        <el-tree-select v-model="form.trainArchiveId" :data="trainArchiveTree" check-strictly :render-after-expand="true" default-expand-all placeholder="分类"/>
      </el-form-item>
      <el-form-item label="课程名称：" prop="name" required>
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="发布班级：" prop="departmentIdList" require>
        <tree-select v-model="form.departmentIdList" :data="departmentTree" :clearable="true"
                     placeholder="发布班级" style="width: 100%"/>
      </el-form-item>
      <el-form-item label="课程时间：" class="form-item-content-block" prop="limitDateTime" required>
        <el-date-picker v-model="form.limitDateTime" value-format="YYYY-MM-DD HH:mm:ss" type="datetimerange" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" :disabled-date="disabledDate">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="封面：" prop="coverPath" required>
        <el-upload accept=".jpg,.png" name="file" :data="{folder:'train/course'}" action="/api/upload/folder/file" :show-file-list="false" :on-progress="uploadProgress" :on-success="uploadImageSuccess" :on-error="uploadError">
          <el-image style="width: 350px; height: 200px" :src="form.coverPath" fit="fill"></el-image>
        </el-upload>
      </el-form-item>
      <el-form-item label="课程描述：" prop="description" required>
        <el-input v-model="form.description" type="textarea" rows="8"></el-input>
      </el-form-item>
      <el-form-item label="课程列表：" prop="courseWareItemList">
        <el-tabs type="border-card" class="train-tab">
          <el-tab-pane label="课件">
            <div>
              <draggable class="train-course-ware-content" item-key="index" group="courseWareItemList" handle=".drag" v-model="form.courseWareItemList">
                <template #item="{index,element }">
                  <div class="train-course-ware">
                    <div class="train-course-ware-action">
                      <el-button size="small" circle @click="courseWarePreview(element.previewPath)">
                        <el-icon>
                          <icon-view/>
                        </el-icon>
                      </el-button>
                    </div>
                    <div class="train-course-ware-content drag">{{ element.name }}</div>
                    <div class="train-course-ware-time">
                      <div>合格时长：</div>
                      <el-input class="train-course-ware-input" placeholder="00:00:00" :formatter="(value) => `${value}`.replace(/[^\d|:]/g, '')" v-model.trim="element.passNumberStr"/>
                      <div class="max-number-time">总时长：{{ element.maxNumberStr }}</div>
                    </div>
                    <div class="train-course-ware-action">
                      <el-button type="danger" circle @click="courseWareRemove(index)">
                        <el-icon>
                          <icon-delete/>
                        </el-icon>
                      </el-button>
                    </div>
                  </div>
                </template>
              </draggable>
            </div>
            <div>
              <el-button type="primary" @click="addCourseWare">添加课件</el-button>
            </div>
          </el-tab-pane>
          <el-tab-pane label="试卷">
            <div class="train-course-ware" v-if="form.examPaperItem.targetId">
              <div class="train-course-ware-action">
                <el-button size="small" circle @click="examPaperPreview(form.examPaperItem.targetId)">
                  <el-icon>
                    <icon-view/>
                  </el-icon>
                </el-button>
              </div>
              <div class="train-course-ware-content">{{ form.examPaperItem.name }}</div>
              <div class="train-course-ware-time">
                <div class="train-exam-paper-limit">
                  <div class="train-exam-paper-limit-item">
                    <div class="train-exam-paper-limit-item-label">合格分：</div>
                    <el-input-number v-model="form.examPaperItem.passNumberStr" :precision="1" :step="1" :max="maxPassScore" :min="0.1"></el-input-number>
                  </div>
                  <div class="train-exam-paper-limit-item" style="margin-top: 5px">
                    <div class="train-exam-paper-limit-item-label">考试次数：</div>
                    <el-input-number v-model="form.examPaperItem.allowCount" :step="1" :max="100" :min="0"></el-input-number>
                  </div>
                </div>
                <div class="max-number-time">试卷总分：{{ form.examPaperItem.maxNumberStr }}</div>
              </div>
              <div class="train-course-ware-action">
                <el-button type="danger" circle @click="examPaperRemove">
                  <el-icon>
                    <icon-delete/>
                  </el-icon>
                </el-button>
              </div>
            </div>
            <div>
              <el-button type="primary" @click="addExamPaper">选择试卷</el-button>
            </div>
          </el-tab-pane>
          <el-tab-pane label="证书">
            <div class="train-course-ware" v-if="form.credentialItem.targetId">
              <div class="train-course-ware-action">
                <el-button size="small" circle @click="credentialShow(form.credentialItem.targetId)">
                  <el-icon>
                    <icon-view/>
                  </el-icon>
                </el-button>
              </div>
              <div class="train-credential-content">
                <el-image style="width: 50px; height: 50px" :src="form.credentialItem.templateImagePath" fit="fill"/>
                <div class="train-credential-content-company">{{ form.credentialItem.name }}</div>
              </div>
              <div class="train-course-ware-action">
                <el-button type="danger" circle @click="credentialRemove">
                  <el-icon>
                    <icon-delete/>
                  </el-icon>
                </el-button>
              </div>
            </div>
            <div>
              <el-button type="primary" @click="addCredential">选择证书</el-button>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-form-item>
      <el-form-item label="排序：" prop="itemOrder">
        <el-input-number v-model="form.itemOrder" :precision="0" :step="1" :min="0" :max="9999999"
        ></el-input-number>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-dialog v-model="courseWarePage.showDialog" width="90%">
      <el-form :model="courseWarePage.queryParam" ref="courseWareQueryForm" :inline="true">
        <el-form-item label="类型：">
          <el-select v-model="courseWarePage.queryParam.fileType" clearable>
            <el-option v-for="item in EnumMap.courseWare.fileType" :key="item.key" :value="item.key" :label="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分类：">
          <tree-select v-model="courseWarePage.queryParam.courseWareArchiveIdList" :data="courseWareArchiveTree" :clearable="true" placeholder="课件分类"/>
        </el-form-item>
        <el-form-item label="实训环境：">
          <el-select v-model="courseWarePage.queryParam.vmType">
            <el-option v-for="item in vmTypeTree" :key="item.guid"
                       :value="item.guid"
                       :label="item.vmName"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称：">
          <el-input v-model="courseWarePage.queryParam.name" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="courseWareSearch">查询</el-button>
          <el-button @click="courseWareClear">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="courseWarePage.tableData" @row-click="courseWareClick" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="id" label="Id" width="90px"/>
        <el-table-column prop="fileTypeStr" label="课件类型" width="90px"/>
        <el-table-column prop="level" label="课件分类" width="120px"/>
        <el-table-column prop="vmType" label="实训环境" width="120px"/>
        <el-table-column prop="name" label="课件名称"/>
        <el-table-column prop="fileName" label="文件名称"/>
        <el-table-column prop="maxLengthStr" label="时长" width="120px"/>
        <el-table-column prop="createTime" label="创建时间" width="160px"/>
        <el-table-column width="100px" label="操作" align="center">
          <template #default="{row}">
            <el-button class="wdd-button-mini" size="small" @click.stop="courseWarePreview(row.previewPath)">
              预览
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="courseWarePage.total>0" :total="courseWarePage.total" v-model:page="courseWarePage.queryParam.pageIndex" v-model:limit="courseWarePage.queryParam.pageSize" @pagination="courseWareSearch"/>
    </el-dialog>


    <el-dialog v-model="examPaperPage.showDialog" width="90%">
      <el-form :model="examPaperPage.queryParam" ref="examPaperQueryForm" :inline="true">
        <el-form-item label="分类：">
          <tree-select v-model="examPaperPage.queryParam.examPaperArchiveIdList" :data="examPaperArchiveTree" :clearable="true" placeholder="试卷分类"/>
        </el-form-item>
        <el-form-item label="名称：">
          <el-input v-model="examPaperPage.queryParam.name" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="examPaperSearch">查询</el-button>
          <el-button @click="examPaperClear">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="examPaperPage.tableData" @row-click="examPaperClick" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="id" label="Id" width="90px"/>
        <el-table-column prop="level" label="试卷分类" width="120px"/>
        <el-table-column prop="name" label="试卷名称"/>
        <el-table-column prop="score" label="总分" width="100px"/>
        <el-table-column prop="questionCount" label="题目数" width="70px"/>
        <el-table-column prop="suggestTimeStr" label="考试时长" width="110px"/>
        <el-table-column prop="createTime" label="创建时间" width="160px"/>
        <el-table-column width="100px" label="操作" align="center">
          <template #default="{row}">
            <el-button class="wdd-button-mini" size="small" @click.stop="examPaperPreview(row.id)">
              预览
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="examPaperPage.total>0" :total="examPaperPage.total" v-model:page="examPaperPage.queryParam.pageIndex" v-model:limit="examPaperPage.queryParam.pageSize" @pagination="examPaperSearch"/>
    </el-dialog>


    <el-dialog v-model="credentialPage.showDialog" width="90%">
      <el-form :model="credentialPage.queryParam" ref="credentialQueryForm" :inline="true">
        <el-form-item label="名称：">
          <el-input v-model="credentialPage.queryParam.name" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="credentialSearch">查询</el-button>
          <el-button @click="credentialClear">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="credentialPage.tableData" @row-click="credentialClick" border fit highlight-current-row
                style="width: 100%">
        <el-table-column prop="id" label="Id" width="90px"/>
        <el-table-column prop="templateImagePath" label="证书模板" width="90px">
          <template #default="{row}">
            <el-image style="width: 50px; height: 50px" :src="row.templateImagePath" fit="fill"/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="证书模板名称"/>
        <el-table-column prop="company" label="发证机构"/>
        <el-table-column prop="createTime" label="创建时间" width="160px"/>
        <el-table-column width="100px" label="操作" align="center">
          <template #default="{row}">
            <el-button size="small" class="wdd-button-mini" @click.stop="credentialShow(row.id)">
              预览
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="credentialPage.total>0" :total="credentialPage.total" v-model:page="credentialPage.queryParam.pageIndex" v-model:limit="credentialPage.queryParam.pageSize" @pagination="credentialSearch"/>
    </el-dialog>

  </div>
</template>

<script>

import {EnumMap} from '@/api/EnumMap'
import {numberConvert, disabledDate} from '@/utils'
import {Delete as IconDelete, View as IconView} from '@element-plus/icons-vue'
import {tree as archiveTree} from '@/api/trainArchive'
import {tree as courseWareTree} from '@/api/courseWareArchive'
import {tree as examPaperTree} from '@/api/examPaperArchive'
import {tree} from '@/api/department'
import {select, edit} from '@/api/train'
import {page as courseWarePage} from '@/api/courseWare'
import {page as examPaperPage} from '@/api/trainExamPaper'
import {page as credentialPage, select as credentialSelect, preview as credentialPreview} from '@/api/credential'
import useStore from '@/store'
import draggable from 'vuedraggable'
import {vmTypeList as vmTypeTree} from '@/api/vmWare'


const {tagsView} = useStore()

export default {
  name: 'TrainCourseEdit',
  components: {IconDelete, IconView, draggable},
  data() {
    return {
      EnumMap: EnumMap,
      disabledDate: disabledDate,
      vmTypeTree:[],
      form: {
        id: null,
        trainArchiveId: null,
        name: null,
        coverPath: null,
        description: null,
        limitDateTime: [],
        departmentIdList: [],
        courseWareItemList: [],
        examPaperItem: {},
        credentialItem: {},
        itemOrder: null
      },
      formRules: {
        name: [{required: true, message: '请输入课程名称'}, {
          max: 255,
          message: '长度小于255个字符'
        }],
        departmentIdList: [{required: true, message: '请选择发布班级'}],
        coverPath: [{required: true, message: '请上传课程封面'}],
        limitDateTime: [{required: true, message: '请选择课程时间'}],
        description: [{required: true, message: '请输入课程描述'}, {
          max: 500,
          message: '长度小于500个字符'
        }],
        courseWareItemList:[{required: true, message: '请添加课件'}]
      },
      courseWarePage: {
        showDialog: false,
        queryParam: {
          name: null,
          fileType: null,
          courseWareArchiveIdList: [],
          vmType:null,
          selectIdList: [],
          pageIndex: 1,
          pageSize: 10
        },
        tableData: [],
        total: 0
      },
      examPaperPage: {
        showDialog: false,
        queryParam: {
          name: null,
          examPaperArchiveIdList: [],
          selectIdList: [],
          pageIndex: 1,
          pageSize: 10
        },
        tableData: [],
        total: 0
      },
      credentialPage: {
        showDialog: false,
        queryParam: {
          name: null,
          selectIdList: [],
          pageIndex: 1,
          pageSize: 10
        },
        tableData: [],
        total: 0
      },
      formLoading: false,
      departmentTree: [],
      trainArchiveTree: [],
      courseWareArchiveTree: [],
      examPaperArchiveTree: []
    }
  },
  computed: {
    maxPassScore: function () {
      let sumScore = this.form.examPaperItem.maxNumberStr
      if (undefined !== sumScore && null !== sumScore && sumScore > 0) {
        return Number(sumScore)
      }
      return 100
    }
  },
  created() {
    vmTypeTree().then(re => {
      this.vmTypeTree = re.response
    });
    let id = this.$route.query.id

    if (id && parseInt(id) !== 0) {
      this.formLoading = true
      select(id).then(re => {
        this.form = re.response
        if (this.form.examPaperItem.passNumberStr) {
          this.form.examPaperItem.passNumberStr = numberConvert(this.form.examPaperItem.passNumberStr)
        }
      }).finally(() => {
        this.formLoading = false
      })
    }

    tree().then(re => {
      this.departmentTree = re.response
    })

    archiveTree().then(re => {
      this.trainArchiveTree = re.response
    })

    courseWareTree().then(re => {
      this.courseWareArchiveTree = re.response
    })

    examPaperTree().then(re => {
      this.examPaperArchiveTree = re.response
    })
  },
  methods: {
    uploadProgress() {
      this.loading = this.$loading({
        lock: true,
        text: '文件上传中…',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
    },
    uploadImageSuccess(re, file) {
      this.loading.close()
      this.form.coverPath = re.response.path
    },
    uploadError() {
      this.loading.close()
      this.$message.error('文件上传失败，请检查文件大小或文件格式')
    },
    addCourseWare() {
      this.courseWareSearch()
      this.courseWarePage.showDialog = true
    },
    courseWareSearch() {
      this.courseWarePage.queryParam.selectIdList = []
      for (const item of this.form.courseWareItemList) {
        this.courseWarePage.queryParam.selectIdList.push(item.targetId)
      }
      courseWarePage(this.courseWarePage.queryParam).then(data => {
        const re = data.response
        this.courseWarePage.tableData = re.list
        this.courseWarePage.total = re.total
        this.courseWarePage.queryParam.pageIndex = re.pageNum
      })
    },
    courseWareClear() {
      this.courseWarePage.queryParam = {
        name: null,
        fileType: null,
        courseWareArchiveIdList: [],
        selectIdList: [],
        pageIndex: 1,
        pageSize: 10
      }
      this.courseWareSearch()
    },
    courseWareClick(row) {
      this.form.courseWareItemList.push({
        targetType: 1,
        name: row.name,
        targetId: row.id,
        passNumber: null,
        passNumberStr: null,
        maxNumber: row.maxLength,
        maxNumberStr: row.maxLengthStr,
        previewPath: row.previewPath,
        fileType: row.fileType
      })
      this.courseWarePage.showDialog = false
      this.$refs.form.validateField('courseWareItemList').then(function (valid) {}).catch(error => {});
    },
    courseWareRemove(index) {
      this.form.courseWareItemList.splice(index, 1)
    },
    courseWarePreview(url) {
      window.open(url, '_blank')
    },
    addExamPaper() {
      this.examPaperSearch()
      this.examPaperPage.showDialog = true
    },
    examPaperSearch() {
      this.examPaperPage.queryParam.selectIdList = []
      if (null != this.form.examPaperItem.targetId) {
        this.examPaperPage.queryParam.selectIdList.push(this.form.examPaperItem.targetId)
      }
      examPaperPage(this.examPaperPage.queryParam).then(data => {
        const re = data.response
        this.examPaperPage.tableData = re.list
        this.examPaperPage.total = re.total
        this.examPaperPage.queryParam.pageIndex = re.pageNum
      })
    },
    examPaperClear() {
      this.examPaperPage.queryParam = {
        name: null,
        examPaperArchiveIdList: [],
        selectIdList: [],
        pageIndex: 1,
        pageSize: 10
      }
      this.examPaperSearch()
    },
    examPaperClick(row) {
      this.form.examPaperItem = {
        targetType: 2,
        name: row.name,
        targetId: row.id,
        passNumber: null,
        passNumberStr: null,
        allowCount: null,
        maxNumberStr: row.score
      }
      this.examPaperPage.showDialog = false
    },
    examPaperRemove() {
      this.form.examPaperItem = {}
    },
    examPaperPreview(id) {
      window.open(`/#/train/exam/paper/show?id=${id}`, '_blank')
    },
    addCredential() {
      this.credentialSearch()
      this.credentialPage.showDialog = true
    },
    credentialSearch() {
      this.credentialPage.queryParam.selectIdList = []
      if (null != this.form.credentialItem.targetId) {
        this.credentialPage.queryParam.selectIdList.push(this.form.credentialItem.targetId)
      }
      credentialPage(this.credentialPage.queryParam).then(data => {
        const re = data.response
        this.credentialPage.tableData = re.list
        this.credentialPage.total = re.total
        this.credentialPage.queryParam.pageIndex = re.pageNum
      })
    },
    credentialClear() {
      this.credentialPage.queryParam = {
        name: null,
        pageIndex: 1,
        pageSize: 10
      }
      this.credentialSearch()
    },
    credentialClick(row) {
      this.form.credentialItem = {
        targetType: 3,
        name: row.name,
        targetId: row.id,
        templateImagePath: row.templateImagePath
      }
      this.credentialPage.showDialog = false
    },
    credentialRemove() {
      this.form.credentialItem = {}
    },
    credentialShow(id) {
      credentialSelect(id).then(re => {
        credentialPreview(re.response).then(re => {
          window.open(re.response, '_blank')
        })
      })
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          edit(this.form).then(data => {
            if (data.code === 1) {
              this.$message.success(data.message)
              tagsView.delCurrentView(this).then(() => {
                this.$router.push('/train/course/list')
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
        trainArchiveId: null,
        name: null,
        coverPath: null,
        description: null,
        limitDateTime: [],
        departmentIdList: [],
        courseWareItemList: [],
        examPaperItem: {},
        credentialItem: {},
        itemOrder: null
      }
      this.$refs['form'].resetFields()
    }
  }
}
</script>
