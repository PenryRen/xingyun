import {post, postWithLoadTip} from '@/utils/request'

export function page(data: any): any {
    return post(`/api/apply/page`, data);
}

export function completePage(data: any): any {
    return post(`/api/apply/complete/page`, data);
}

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/apply/create`, data)
    } else {
        return postWithLoadTip(`/api/apply/update`, data)
    }
}

export function select(id: any): any {
    return post(`/api/apply/select/${id}`, undefined);
}

export function deleteApply(id: any): any {
    return postWithLoadTip(`/api/apply/delete/${id}`, undefined);
}

export function publish(id: any): any {
    return postWithLoadTip(`/api/apply/publish/${id}`, undefined);
}

export function close(id: any): any {
    return postWithLoadTip(`/api/apply/close/${id}`, undefined);
}

export function info(id: any): any {
    return post(`/api/apply/info/${id}`, undefined);
}

export function applyUserPage(data: any): any {
    return post(`/api/apply/user/page`, data);
}

export function removeUserApply(id: any): any {
    return postWithLoadTip(`/api/apply/user/remove/${id}`, undefined);
}

export function auditUserPage(data: any): any {
    return post(`/api/apply/audit/user/page`, data);
}

export function auditConfirm(id: any): any {
    return postWithLoadTip(`/api/apply/audit/confirm/${id}`, undefined);
}
