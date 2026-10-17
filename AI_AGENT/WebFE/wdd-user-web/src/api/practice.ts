import {post, postWithLoadTip} from '@/utils/request'

export function page(data: any): any {
    return post(`/api/practice/page`, data);
}


export function select(id: any): any {
    return post(`/api/practice/select/${id}`, undefined);
}


export function submit(data: any): any {
    return postWithLoadTip(`/api/practice/submit`, data);
}


export function random(id: any): any {
    return postWithLoadTip(`/api/practice/random/build/${id}`, undefined);
}

export function read(id: any): any {
    return post(`/api/practice/read/${id}`, undefined);
}

export function edit(data: any): any {
    return postWithLoadTip(`/api/practice/edit`, data);
}

export function answerPage(data: any): any {
    return post(`/api/practice/answer/page`, data);
}
