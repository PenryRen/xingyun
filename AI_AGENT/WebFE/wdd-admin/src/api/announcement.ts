import {post, postWithLoadTip} from '@/utils/request'

export function page(data: any): any {
    return post(`/api/announcement/page`, data);
}

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/announcement/create`, data)
    } else {
        return postWithLoadTip(`/api/announcement/update`, data)
    }
}

export function select(id: any): any {
    return post(`/api/announcement/select/${id}`, undefined);
}

export function deleteAnnouncement(id: any): any {
    return postWithLoadTip(`/api/announcement/delete/${id}`, undefined);
}

export function info(id: any): any {
    return post(`/api/announcement/info/${id}`, undefined);
}

export function userPage(data: any): any {
    return post(`/api/announcement/user/page`, data);
}
