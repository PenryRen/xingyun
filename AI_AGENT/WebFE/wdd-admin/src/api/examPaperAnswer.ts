import {post, postWithLoadTip} from '@/utils/request'

export function pageList(data: any): any {
    return post(`/api/exam/paper/answer/page`, data);
}

export function info(id: any): any {
    return post(`/api/exam/paper/answer/info/${id}`, undefined);
}

export function userAnswerPage(data: any): any {
    return post(`/api/exam/paper/answer/user/page`, data);
}

export function exportAnswerPage(data: any): any {
    return postWithLoadTip(`/api/exam/paper/answer/export`, data);
}

export function edit(data: any): any {
    return postWithLoadTip(`/api/exam/paper/answer/edit`, data);
}

export function read(id: any): any {
    return post(`/api/exam/paper/answer/read/select/${id}`, undefined);
}

export function camera(id: any): any {
    return post(`/api/exam/paper/answer/camera/${id}`, undefined);
}

export function credentialAnswerPage(data: any): any {
    return post(`/api/exam/paper/answer/credential/page`, data);
}

export function credentialInfo(id: any): any {
    return post(`/api/exam/paper/answer/credential/info/${id}`, undefined);
}

export function resit(data: any): any {
    return postWithLoadTip(`/api/exam/paper/answer/resit`, data);
}

export function userExport(id: any): any {
    return postWithLoadTip(`/api/exam/paper/answer/user/export/${id}`, undefined);
}

export function monitorPage(data: any): any {
    return post(`/api/exam/paper/monitor/page`, data);
}

export function monitorCamera(id: any): any {
    return post(`/api/exam/paper/monitor/camera/${id}`, undefined);
}

export function monitorRead(id: any): any {
    return post(`/api/exam/paper/monitor/read/select/${id}`, undefined);
}

export function monitorSubmit(id: any): any {
    return postWithLoadTip(`/api/exam/paper/monitor/submit/${id}`, undefined);
}

export function nextJudge(id: any): any {
    return post(`/api/exam/paper/answer/next/judge/${id}`, undefined);
}
