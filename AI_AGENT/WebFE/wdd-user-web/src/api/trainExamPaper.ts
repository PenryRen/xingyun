import {post, postWithLoadTip} from '@/utils/request'


export function select(uId: any, pId: any): any {
    return post(`/api/train/exam/paper/select/${uId}/${pId}`, undefined);
}

export function cheat(id: any): any {
    return post(`/api/train/exam/paper/cheat/${id}`, undefined);
}

export function submit(uId: any, data: any): any {
    return postWithLoadTip(`/api/train/exam/paper/submit/${uId}`, data);
}
