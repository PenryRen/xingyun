import {post, postWithLoadTip} from '@/utils/request'

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/question/archive/create`, data)
    } else {
        return postWithLoadTip(`/api/question/archive/update`, data)
    }
}

export function move(data: any): any {
    return postWithLoadTip(`/api/question/archive/move`, data);
}


export function tree(): any {
    return post(`/api/question/archive/tree`, undefined);
}


export function deleteArchive(id): any {
    return postWithLoadTip(`/api/question/archive/delete/${id}`, undefined);
}
