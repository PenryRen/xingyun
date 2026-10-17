import {post, postWithLoadTip} from '@/utils/request'

export function page(data: any): any {
    return post(`/api/exam/paper/build/page`, data);
}

export function select(id: any, action: any): any {
    if (action !== null && action === 3) { //模拟练习转正式考试
        return post(`/api/practice/build/covert/${id}`, undefined);
    } else {
        const promise = post(`/api/exam/paper/build/select/${id}`, undefined);
        if (action !== null && action === 2) { //试卷复制
            promise.then(re => {
                re.response.id = null
            })
        }
        return promise;
    }
}

export function edit(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/exam/paper/build/create`, data)
    } else {
        return postWithLoadTip(`/api/exam/paper/build/update`, data)
    }
}

export function random(data: any): any {
    if (data.id === null) {
        return postWithLoadTip(`/api/exam/paper/build/random/create`, data)
    } else {
        return postWithLoadTip(`/api/exam/paper/build/random/update`, data)
    }
}

export function checkExtract(data: any): any {
    return post(`/api/exam/paper/build/check/extract`, data);
}

export function checkRandom(data: any): any {
    return post(`/api/exam/paper/build/check/random`, data);
}

export function publish(id: any): any {
    return postWithLoadTip(`/api/exam/paper/build/publish/${id}`, undefined);
}

export function recall(id: any): any {
    return postWithLoadTip(`/api/exam/paper/build/recall/${id}`, undefined);
}

export function deleteExamPaperBuild(id: any): any {
    return postWithLoadTip(`/api/exam/paper/build/delete/${id}`, undefined);
}


