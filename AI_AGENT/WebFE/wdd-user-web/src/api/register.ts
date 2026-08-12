import {post, postWithLoadTip} from '@/utils/request'

export function register(data: any): any {
    return postWithLoadTip(`/api/user/register`, data);
}

export function tree(): any {
    return post(`/api/user/department/tree`, undefined);
}
