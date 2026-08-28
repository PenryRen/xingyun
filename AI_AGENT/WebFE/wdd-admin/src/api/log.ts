import {post} from '@/utils/request'

export function eventPage(data: any): any {
    return post(`/api/user/log/event/page`, data);
}

export function feedbackPage(data: any): any {
    return post(`/api/user/log/feedback/page`, data);
}
