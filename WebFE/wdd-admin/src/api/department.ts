import {post, postWithLoadTip,get} from '@/utils/request'

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/department/create`, data)
    } else {
        return postWithLoadTip(`/api/department/update`, data)
    }
}

export function move(data: any): any {
    return postWithLoadTip(`/api/department/move`, data);
}


export function tree(): any {
    return post(`/api/department/tree`, undefined);
}

export function allTree(): any {
    return post(`/api/department/all/tree`, undefined);
}


export function deleteDepartment(id): any {
    return postWithLoadTip(`/api/department/delete/${id}`, undefined);
}

export function vmList(): any {
  return post(`/api/vmWare/vmTypeList`, undefined);
}
