import {post, postWithLoadTip} from '@/utils/request'

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/forum/archive/create`, data)
    } else {
        return postWithLoadTip(`/api/forum/archive/update`, data)
    }
}

export function move(data: any): any {
    return postWithLoadTip(`/api/forum/archive/move`, data);
}


export function tree(): any {
    return post(`/api/forum/archive/tree`, undefined);
}


export function deleteArchive(id): any {
    return postWithLoadTip(`/api/forum/archive/delete/${id}`, undefined);
}
