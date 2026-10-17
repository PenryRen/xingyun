import {post, postWithLoadTip} from '@/utils/request'

export function list(): any {
    return post(`/api/exam/paper/archive/list`, undefined);
}

export function cheat(id: any): any {
    return post(`/api/exam/paper/cheat/${id}`, undefined);
}

export function faceCheck(data: any): any {
    return postWithLoadTip(`/api/exam/paper/face/check`, data);
}

export function select(type: any, id: any): any {
    return post(`/api/exam/paper/select/${type}/${id}`, undefined);
}

export function page(data: any): any {
    return post(`/api/exam/paper/page`, data);
}

export function camera(data: any): any {
    return post(`/api/exam/paper/camera`, data);
}
