import {post, postWithLoadTip} from '@/utils/request'

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/exam/paper/archive/create`, data)
    } else {
        return postWithLoadTip(`/api/exam/paper/archive/update`, data)
    }
}

export function move(data: any): any {
    return postWithLoadTip(`/api/exam/paper/archive/move`, data);
}


export function tree(): any {
    return post(`/api/exam/paper/archive/tree`, undefined);
}


export function deleteArchive(id): any {
    return postWithLoadTip(`/api/exam/paper/archive/delete/${id}`, undefined);
}
