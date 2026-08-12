import {post, postWithLoadTip} from '@/utils/request'

export function list(): any {
    return post(`/api/course/ware/archive/list`, undefined);
}

export function page(data: any): any {
    return post(`/api/course/ware/page`, data);
}


export function select(id: any): any {
    return post(`/api/course/ware/select/${id}`, undefined);
}

export function watch(data: any): any {
    return post(`/api/course/ware/watch`, data);
}

export function submit(data: any): any {
    return postWithLoadTip(`/api/course/ware/question/submit`, data);
}
