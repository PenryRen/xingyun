import {post, postWithLoadTip} from '@/utils/request'

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/train/archive/create`, data)
    } else {
        return postWithLoadTip(`/api/train/archive/update`, data)
    }
}

export function move(data: any): any {
    return postWithLoadTip(`/api/train/archive/move`, data);
}


export function tree(): any {
    return post(`/api/train/archive/tree`, undefined);
}


export function deleteArchive(id): any {
    return postWithLoadTip(`/api/train/archive/delete/${id}`, undefined);
}
