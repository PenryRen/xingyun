import {post} from '@/utils/request'

export function page(data: any): any {
    return post(`/api/credential/page`, data);
}
