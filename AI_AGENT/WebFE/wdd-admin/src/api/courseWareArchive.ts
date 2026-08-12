import {post, postWithLoadTip} from '@/utils/request'

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/course/ware/archive/create`, data)
    } else {
        return postWithLoadTip(`/api/course/ware/archive/update`, data)
    }
}

export function move(data: any): any {
    return postWithLoadTip(`/api/course/ware/archive/move`, data);
}


export function tree(): any {
    return post(`/api/course/ware/archive/tree`, undefined);
}


export function deleteArchive(id): any {
    return postWithLoadTip(`/api/course/ware/archive/delete/${id}`, undefined);
}
