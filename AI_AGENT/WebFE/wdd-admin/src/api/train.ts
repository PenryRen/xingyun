import {post, postWithLoadTip} from '@/utils/request'

export function page(data: any): any {
    return post(`/api/train/course/page`, data);
}

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/train/course/create`, data)
    } else {
        return postWithLoadTip(`/api/train/course/update`, data)
    }
}

export function deleteTrain(id: any): any {
    return postWithLoadTip(`/api/train/course/delete/${id}`, undefined);
}

export function select(id: any): any {
    return post(`/api/train/course/select/${id}`, undefined);
}

export function info(id: any): any {
    return post(`/api/train/course/detail/${id}`, undefined);
}

export function userPage(data: any): any {
    return post(`/api/train/course/detail/page`, data);
}

export function userExport(data: any): any {
    return postWithLoadTip(`/api/train/course/detail/export`, data);
}


export function userCourseWarePage(data: any): any {
    return post(`/api/train/course/detail/course/ware/page`, data);
}
