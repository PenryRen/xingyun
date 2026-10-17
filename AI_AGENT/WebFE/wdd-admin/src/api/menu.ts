import {post, postWithLoadTip} from '@/utils/request'

export function level(): any {
    return post(`/api/menu/level`, undefined);
}

export function list(level: number): any {
    return post(`/api/menu/list`, {level: level});
}

export function page(data: any): any {
    return post(`/api/menu/page`, data);
}

export function edit(data: any): any {
    return postWithLoadTip(`/api/menu/edit`, data);
}

export function select(id: any): any {
    return post(`/api/menu/select/${id}`, undefined);
}

export function deleteMenu(id: any): any {
    return postWithLoadTip(`/api/menu/delete/${id}`, undefined);
}
