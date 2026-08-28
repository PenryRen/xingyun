import {post, postWithLoadTip} from '@/utils/request'

export function add(data: any): any {
    return postWithLoadTip(`/api/forum/add`, data);
}

export function tree(): any {
    return post(`/api/forum/tree`, undefined);
}

export function page(data: any): any {
    return post(`/api/forum/page`, data);
}

export function select(id: any): any {
    return post(`/api/forum/select/${id}`, undefined);
}

export function commentAdd(data: any): any {
    return postWithLoadTip(`/api/forum/comment/add`, data);
}

export function commentPage(data: any): any {
    return post(`/api/forum/comment/page`, data);
}

export function commentDelete(id: any): any {
    return postWithLoadTip(`/api/forum/comment/delete/${id}`, undefined);
}
