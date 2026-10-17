import {post, postWithLoadTip} from '@/utils/request'

export function page(data: any): any {
    return post(`/api/practice/build/page`, data);
}

export function deletePracticeBuild(id: any): any {
    return postWithLoadTip(`/api/practice/build/delete/${id}`, undefined);
}


export function select(id: any): any {
    return post(`/api/practice/build/select/${id}`, undefined);
}

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/practice/build/create`, data)
    } else {
        return postWithLoadTip(`/api/practice/build/update`, data)
    }
}


export function answerInfo(id: any): any {
    return post(`/api/practice/answer/info/${id}`, undefined);
}

export function answerPage(data: any): any {
    return post(`/api/practice/answer/page`, data);
}

export function answerRead(id: any): any {
    return post(`/api/practice/answer/read/${id}`, undefined);
}


export function answerEdit(data: any): any {
    return postWithLoadTip(`/api/practice/answer/edit`, data);
}


