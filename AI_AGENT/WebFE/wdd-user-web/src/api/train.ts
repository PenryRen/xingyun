import {post, postWithLoadTip, postSync} from '@/utils/request'

export function list(): any {
    return post(`/api/train/archive/list`, undefined);
}

export function page(data: any): any {
    return post(`/api/train/page`, data);
}

export function select(id: any): any {
    return post(`/api/train/select/${id}`, undefined);
}


export function start(id: any): any {
    return postSync(`/api/train/start/${id}`, undefined);
}

export function allocation(data: any): any {
  return post(`http://192.168.3.135:6002/api/vmWare/allocation`, data);
}

export function wareSelectOne(data: any): any {
  return post(`http://192.168.3.135:6002/api/vmWare/wareSelectOne`, data);
}
