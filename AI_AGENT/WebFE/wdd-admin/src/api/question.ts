import {post, postSync, postWithLoadTip} from '@/utils/request'

export function page(data: any): any {
    return post(`/api/question/page`, data);
}

export function select(id: any): any {
    return post(`/api/question/select/${id}`, undefined);
}

export function selectSync(id: any): any {
    return postSync(`/api/question/select/${id}`, undefined);
}

export function singleChoiceSelect(id: any): any {
    return post(`/api/question/select/singleChoice/${id}`, undefined);
}

export function singleChoiceEdit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/question/create/singleChoice`, data)
    } else {
        return postWithLoadTip(`/api/question/update/singleChoice`, data)
    }
}

export function multipleChoiceSelect(id: any): any {
    return post(`/api/question/select/multipleChoice/${id}`, undefined);
}

export function multipleChoiceEdit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/question/create/multipleChoice`, data)
    } else {
        return postWithLoadTip(`/api/question/update/multipleChoice`, data)
    }
}

export function uncertainMultipleChoiceSelect(id: any): any {
    return post(`/api/question/select/uncertainMultipleChoice/${id}`, undefined);
}

export function uncertainMultipleChoiceEdit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/question/create/uncertainMultipleChoice`, data)
    } else {
        return postWithLoadTip(`/api/question/update/uncertainMultipleChoice`, data)
    }
}

export function trueFalseSelect(id: any): any {
    return post(`/api/question/select/trueFalse/${id}`, undefined);
}

export function trueFalseEdit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/question/create/trueFalse`, data)
    } else {
        return postWithLoadTip(`/api/question/update/trueFalse`, data)
    }
}

export function gapFillingSelect(id: any): any {
    return post(`/api/question/select/gapFilling/${id}`, undefined);
}

export function gapFillingEdit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/question/create/gapFilling`, data)
    } else {
        return postWithLoadTip(`/api/question/update/gapFilling`, data)
    }
}

export function shortAnswerSelect(id: any): any {
    return post(`/api/question/select/shortAnswer/${id}`, undefined);
}

export function shortAnswerEdit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/question/create/shortAnswer`, data)
    } else {
        return postWithLoadTip(`/api/question/update/shortAnswer`, data)
    }
}

export function deleteQuestion(id: any): any {
    return postWithLoadTip(`/api/question/delete/${id}`, undefined);
}

export function trainOperateSelect(id: any): any {
  return post(`/api/question/select/trainOperate/${id}`, undefined);
}

export function trainOperateEdit(data: any): any {
  if (data.id === null) {
    return postWithLoadTip(`/api/question/create/trainOperate`, data)
  } else {
    return postWithLoadTip(`/api/question/update/trainOperate`, data)
  }
}
