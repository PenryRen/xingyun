import {post, postWithLoadTip} from '@/utils/request'

export function getCurrentUser(): any {
    return post(`/api/user/current`, undefined);
}

export function update(data: any): any {
    return postWithLoadTip(`/api/user/update`, data);
}

export function changePassword(data: any): any {
    return postWithLoadTip(`/api/user/changePassword`, data);
}

export function comment(data: any): any {
    return post(`/api/user/comment`, data);
}

export function event(data: any): any {
    return post(`/api/user/event`, data);
}

export function feedback(data: any): any {
    return postWithLoadTip(`/api/user/feedback`, data);
}

export function train(data: any): any {
    return post(`/api/user/train`, data);
}
