import {postWithLoadTip} from '@/utils/request'

export function login(data: any): any {
    return postWithLoadTip(`/api/user/login`, data);
}

export function permission(): any {
    return postWithLoadTip(`/api/user/menu`, undefined);
}

export function logout(): any {
    return postWithLoadTip(`/api/user/logout`, undefined);
}
