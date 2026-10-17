import {post, postWithLoadTip} from '@/utils/request'

export function employeePage(data: any): any {
    return post(`/api/user/employee/page`, data);
}

export function employeeEdit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/user/employee/create`, data)
    } else {
        return postWithLoadTip(`/api/user/employee/update`, data)
    }
}

export function employeeSelect(id: any): any {
    return post(`/api/user/employee/select/${id}`, undefined);
}

export function employeeDelete(id: any): any {
    return postWithLoadTip(`/api/user/employee/delete/${id}`, undefined);
}

export function employeeExport(data: any): any {
    return postWithLoadTip(`/api/user/employee/export`, data);
}

export function adminPage(data: any): any {
    return post(`/api/user/admin/page`, data);
}

export function adminEdit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/user/admin/create`, data)
    } else {
        return postWithLoadTip(`/api/user/admin/update`, data)
    }
}

export function adminSelect(id: any): any {
    return post(`/api/user/admin/select/${id}`, undefined);
}

export function adminDelete(id: any): any {
    return postWithLoadTip(`/api/user/admin/delete/${id}`, undefined);
}

export function userByUserName(data: any): any {
    return post(`/api/user/selectByUserName`, data);
}

