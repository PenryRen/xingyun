import {post, postSync, postWithLoadTip} from '@/utils/request'

export function menu(): any {
    return postSync(`/api/role/menu`, undefined);
}

export function list(): any {
    return post(`/api/role/list`, undefined);
}

export function page(data: any): any {
    return post(`/api/role/page`, data);
}

export function select(id: any): any {
    return post(`/api/role/select/${id}`, undefined);
}

export function create(): any {
    return postWithLoadTip(`/api/role/create`, undefined);
}

export function edit(data: any): any {
    return postWithLoadTip(`/api/role/edit`, data);
}

export function deleteRole(id: any): any {
    return postWithLoadTip(`/api/role/delete/${id}`, undefined);
}
