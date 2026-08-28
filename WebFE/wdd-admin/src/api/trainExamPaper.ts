import {post, postWithLoadTip} from '@/utils/request'

export function page(data: any): any {
    return post(`/api/train/exam/paper/page`, data);
}

export function select(id: any): any {
    return post(`/api/train/exam/paper/select/${id}`, undefined);
}

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/train/exam/paper/create`, data)
    } else {
        return postWithLoadTip(`/api/train/exam/paper/update`, data)
    }
}

export function deleteExamPaper(id: any): any {
    return postWithLoadTip(`/api/train/exam/paper/delete/${id}`, undefined);
}


export function userAnswerPage(data: any): any {
    return post(`/api/train/exam/paper/answer/page`, data);
}

export function answerRead(id: any): any {
    return post(`/api/train/exam/paper/answer/read/${id}`, undefined);
}

export function answerEdit(data: any): any {
    return postWithLoadTip(`/api/train/exam/paper/answer/edit`, data);
}

export function nextJudge(id: any): any {
    return post(`/api/train/exam/paper/next/judge/${id}`, undefined);
}
