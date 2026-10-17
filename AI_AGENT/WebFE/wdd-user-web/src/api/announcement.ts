import {post} from '@/utils/request'

export function tree(): any {
    return post(`/api/announcement/tree`, undefined);
}

export function page(data: any): any {
    return post(`/api/announcement/page`, data);
}

export function select(id: any): any {
    return post(`/api/announcement/select/${id}`, undefined);
}

export function read(id: any): any {
    return post(`/api/announcement/read/${id}`, undefined);
}
