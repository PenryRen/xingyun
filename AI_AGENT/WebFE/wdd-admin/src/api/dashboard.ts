import {post} from '@/utils/request'

export function index(): any {
    return post(`/api/dashboard/index`, undefined);
}

export function systemInfo(): any {
    return post(`/api/dashboard/systemInfo`, undefined);
}
