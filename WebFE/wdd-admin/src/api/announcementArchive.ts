import {post, postWithLoadTip} from '@/utils/request'

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/announcement/archive/create`, data)
    } else {
        return postWithLoadTip(`/api/announcement/archive/update`, data)
    }
}

export function move(data: any): any {
    return postWithLoadTip(`/api/announcement/archive/move`, data);
}


export function tree(): any {
    return post(`/api/announcement/archive/tree`, undefined);
}


export function deleteArchive(id): any {
    return postWithLoadTip(`/api/announcement/archive/delete/${id}`, undefined);
}
