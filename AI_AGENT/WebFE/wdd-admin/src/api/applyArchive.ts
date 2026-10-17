import {post, postWithLoadTip} from '@/utils/request'

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/apply/archive/create`, data)
    } else {
        return postWithLoadTip(`/api/apply/archive/update`, data)
    }
}

export function move(data: any): any {
    return postWithLoadTip(`/api/apply/archive/move`, data);
}


export function tree(): any {
    return post(`/api/apply/archive/tree`, undefined);
}


export function deleteArchive(id): any {
    return postWithLoadTip(`/api/apply/archive/delete/${id}`, undefined);
}
