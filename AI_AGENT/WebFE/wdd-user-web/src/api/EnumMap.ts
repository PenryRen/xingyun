const EnumMap = {
    user: {
        sexEnum: [{key: 1, value: '男'}, {key: 2, value: '女'}]
    },
    exam: {
        paperTypeEnum: [{key: 1, value: '正考'}, {key: 3, value: '练习'}],
        paperTypeAllEnum: [{key: 1, value: '正考'}, {key: 2, value: '补考'}, {key: 3, value: '练习'}],
        question: {
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
            }
        },
        passed: [{key: true, value: '合格'}, {key: false, value: '不合格'}]
    },
    course: {
        ware: {
            fileTypeTag: [{key: 1, value: '视频'}, {key: 2, value: '文档'}]
        }
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
