import {post, postWithLoadTip} from '@/utils/request'

export function page(data: any): any {
    return post(`/api/exam/paper/answer/page`, data);
}

export function monitor(data: any): any {
    return post(`/api/exam/paper/answer/monitor`, data);
}

export function submit(data: any): any {
    return postWithLoadTip(`/api/exam/paper/answer/submit`, data);
}

export function read(id: any): any {
    return post(`/api/exam/paper/answer/read/select/${id}`, undefined);
}
