import {post, postWithLoadTip} from '@/utils/request'

export function current(): any {
    return post(`/api/profile/current`, undefined);
}

export function edit(data: any): any {
    return postWithLoadTip(`/api/profile/edit`, data);
}

export function changePassword(data: any): any {
    return postWithLoadTip(`/api/profile/change/password`, data);
}
