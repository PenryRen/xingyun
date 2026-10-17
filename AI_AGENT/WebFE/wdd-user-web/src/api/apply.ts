import {post, postWithLoadTip} from '@/utils/request'

export function list(): any {
    return post(`/api/apply/archive/list`, undefined);
}

export function page(data: any): any {
    return post(`/api/apply/page`, data);
}

export function record(data: any): any {
    return post(`/api/apply/record`, data);
}

export function goApply(id: any): any {
    return postWithLoadTip(`/api/apply/go/${id}`, undefined);
}

export function cancelApply(applyId: any, userApplyId: any): any {
    return postWithLoadTip(`/api/apply/cancel/${applyId}/${userApplyId}`, undefined);
}
