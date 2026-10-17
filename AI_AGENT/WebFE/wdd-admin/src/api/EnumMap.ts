const EnumMap = {
  vmWare: {
    vmType: [{key: '00', value: '主机'},{key: '01', value: '子机'}],
    vmTypeTag: [{key: '00', value: 'info'},{key: '01', value: 'primary'}],
    status: [
      {key: '00', value: '正在运行'},
      {key: '01', value: '关机'},
      {key: '02', value: '启动中'},
      {key: '100', value: '克隆中'},
      {key: '101', value: '重启中'},
    ],
    statusTag: [
      {key: '00', value: 'primary'},
      {key: '01', value: 'info'},
      {key: '02', value: 'warning'},
      {key: '100', value: 'info'},
      {key: '101', value: 'warning'},
    ],
    classes: [{key: '00', value: '实训'}, {key: '01', value: '考试'}],
    classesTag: [{key: '00', value: 'primary'}, {key: '01', value: 'success'}],
    statusBtn: [{key: 1, value: '禁用'}, {key: 2, value: '启用'}],
  },
    user: {
        sexEnum: [{key: 1, value: '男'}, {key: 2, value: '女'}],
        statusEnum: [{key: 1, value: '启用'}, {key: 2, value: '禁用'}],
        statusTag: [{key: 1, value: 'success'}, {key: 2, value: 'danger'}],
        statusBtn: [{key: 1, value: '禁用'}, {key: 2, value: '启用'}]
    },
    menu: {
        levelEnum: [{key: 1, value: '一级菜单'}, {key: 2, value: '二级菜单'}]
    },
    exam: {
        examPaper: {
            paperTypeEnum: [{key: 1, value: '人工试卷'}, {key: 2, value: '抽题试卷'}, {key: 3, value: '随机试卷'}]
        },
        examPaperAnswer: {
            statusEnum: [{key: 3, value: '未参加'}, {key: 1, value: '待批改'}, {key: 2, value: '已完成'}],
        },
        question: {
            vmTypeSingleEnum: [
              {key: 7, value: '实训题'}],
            typeEnum: [{key: 1, value: '单选题'},
              {key: 2, value: '多选题'},
              {key: 6, value: '不定项选择题'},
              {key: 3, value: '判断题'},
              {key: 4, value: '填空题'},
              {key: 5, value: '简答题'}],
            vmTypeEnum: [{key: 1, value: '单选题'},
              {key: 2, value: '多选题'},
              {key: 6, value: '不定项选择题'},
              {key: 3, value: '判断题'},
              {key: 4, value: '填空题'},
              {key: 5, value: '简答题'},
              {key: 7, value: '实训题'}],
            vmEditUrlSingleEnum: [
              {key: 7, value: '/question/train/operate', name: '实训题'}],
            editUrlEnum: [{key: 1, value: '/question/single/choice', name: '单选题'},
                {key: 2, value: '/question/multiple/choice', name: '多选题'},
                {key: 6, value: '/question/uncertain/multiple/choice', name: '不定项选择题'},
                {key: 3, value: '/question/true/false', name: '判断题'},
                {key: 4, value: '/question/gap/filling', name: '填空题'},
                {key: 5, value: '/question/short/answer', name: '简答题'}],
            vmEditUrlEnum: [{key: 1, value: '/question/single/choice', name: '单选题'},
              {key: 2, value: '/question/multiple/choice', name: '多选题'},
              {key: 6, value: '/question/uncertain/multiple/choice', name: '不定项选择题'},
              {key: 3, value: '/question/true/false', name: '判断题'},
              {key: 4, value: '/question/gap/filling', name: '填空题'},
              {key: 5, value: '/question/short/answer', name: '简答题'},
              {key: 7, value: '/question/train/operate', name: '实训题'}],
            difficultEnum: [{key: 1, value: '简单'}, {key: 2, value: '较难'}, {key: 3, value: '困难'}],
            answer: {
                doRightTag: [
                  {key: true, value: 'success'}, {key: false, value: 'danger'},
                  {key: null, value: 'warning'}, {key: undefined, value: 'warning'}],
                doRightEnum: [
                  {key: true, value: '正确'}, {key: false, value: '错误'},
                  {key: null, value: '待批改'}, {key: undefined, value: '待批改'}],
                doCheckTag: [
                  {key: true, value: 'success'}, {key: false, value: 'danger'},
                  {key: null, value: 'warning'}, {key: undefined, value: 'warning'}],
                doCheckEnum: [
                  {key: true, value: '核验通过'}, {key: false, value: '核验未通过'},
                  {key: null, value: '待核验'}, {key: undefined, value: '待核验'}],
                doCompletedTag: [{key: false, value: 'info'}, {key: true, value: 'success'}]
            },
            monitor: {
                doRightTag: [{key: true, value: 'success'}, {key: false, value: 'danger'}, {
                    key: null,
                    value: 'info'
                }, {key: undefined, value: ''}],
                doRightEnum: [{key: true, value: '正确'}, {key: false, value: '错误'}, {
                    key: null,
                    value: '进行中'
                }, {key: undefined, value: '已批改'}],
                doCompletedTag: [{key: false, value: 'info'}, {key: true, value: 'success'}]
            }
        },
        build: {
            rangeTypeEnum: [{key: 2, value: '自定义'}, {key: 1, value: '报名'}],
            editUrlEnum: [{key: 1, value: '/exam/paper/build/manual', name: '人工组卷'},
                {key: 2, value: '/exam/paper/build/extract', name: '抽题组卷'},
                {key: 3, value: '/exam/paper/build/random', name: '随机组卷'},
                {key: 4, value: '/exam/paper/build/operate', name: '实训组卷'}],
            statusEnum: [{key: 1, value: '待发布'}, {key: 2, value: '已发布'}, {key: 3, value: '已撤回'}]
        }
    },
    practice: {
        editUrlEnum: [{key: 1, value: '/practice/manual', name: '人工组卷'},
            {key: 2, value: '/practice/extract', name: '抽题组卷'},
            {key: 3, value: '/practice/random', name: '随机组卷'}],
        answerStatusEnum: [{key: 1, value: '待批改'}, {key: 2, value: '已完成'}],
    },
    credential: {
        textTypeEnum: [{key: 1, value: '试卷名称'}, {key: 8, value: '课程名称'}, {key: 2, value: '学员姓名'}, {
            key: 3,
            value: '证书编号'
        }, {key: 4, value: '创建时间'}, {key: 5, value: '有效期'}, {key: 6, value: '电子印章'}, {
            key: 7,
            value: '自定义'
        }],
        fontWeightTypeEnum: [{key: 1, value: '标准'}, {key: 2, value: '粗体'}]
    },
    courseWare: {
        fileType: [{key: 1, value: '视频'}, {key: 2, value: '文档'}]

    },
    vmeWare: {
      vmware: [{id: 1232, vmName: 'Kiny-1'}, {id: 1233, vmName: 'Kiny-2'}]
  },
    train: {
        statusEnum: [{key: 1, value: '进行中'}, {key: 2, value: '合格'}, {key: 3, value: '不合格'}],
        paperAnswerStatusEnum: [{key: 1, value: '待批改'}, {key: 2, value: '已完成'}]
    }
}

const Format = function (array, key) {
    for (const item of array) {
        if (item.key === key) {
            return item.value
        }
    }
    return null
}
export {EnumMap, Format} ;
