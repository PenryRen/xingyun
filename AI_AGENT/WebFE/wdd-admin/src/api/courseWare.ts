import {post, postWithLoadTip} from '@/utils/request'

export function page(data: any): any {
    return post(`/api/course/ware/page`, data);
}

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/course/ware/create`, data)
    } else {
        return postWithLoadTip(`/api/course/ware/update`, data)
    }
}

export function deleteCourseWare(id: any): any {
    return postWithLoadTip(`/api/course/ware/delete/${id}`, undefined);
}

export function select(id: any): any {
    return post(`/api/course/ware/select/${id}`, undefined);
}
