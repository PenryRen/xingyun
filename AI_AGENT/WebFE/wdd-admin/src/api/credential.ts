import {post, postWithLoadTip} from '@/utils/request'

export function list(): any {
    return post(`/api/credential/list`, undefined);
}

export function page(data: any): any {
    return post(`/api/credential/page`, data);
}

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/credential/create`, data)
    } else {
        return postWithLoadTip(`/api/credential/update`, data)
    }
}

export function select(id: any): any {
    return post(`/api/credential/select/${id}`, undefined);
}

export function deleteCredential(id: any): any {
    return postWithLoadTip(`/api/credential/delete/${id}`, undefined);
}

export function preview(data: any): any {
    return postWithLoadTip(`/api/credential/preview`, data);
}

export function userPage(data: any): any {
    return post(`/api/credential/user/page`, data);
}
