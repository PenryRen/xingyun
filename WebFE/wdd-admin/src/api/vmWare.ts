import {post, postWithLoadTip} from '@/utils/request'

/**
 * 实训环境列表
 */
export function vmTypeList(): any {
  return post(`/api/vmWare/vmTypeList`, undefined);
}

export function PageList(data: any): any {
  return post(`/api/vmWare/page`, data);
}
export function shutdown(data: any): any {
  return post(`/api/vmWare/shutdown`, data);
}
export function start(data: any): any {
  return post(`/api/vmWare/start`, data);
}
export function reStart(data: any): any {
  return post(`/api/vmWare/reStart`, data);
}
export function clone(data: any): any {
  return post(`/api/vmWare/clone`, data);
}
export function release(data: any): any {
  return post(`/api/vmWare/release`, data);
}

/**
 * 刷新授权过期时间
 * @param data
 */
export function refreshExpiration(data: any): any {
  return post(`/api/vmWare/refreshExpiration`, data);
}

/**
 * 获取授权过期时间
 * @param data
 */
export function getExpiration(data: any): any {
  return post(`/api/vmWare/getExpiration`, data);
}

/**
 * 导出
 * @param data
 */
export function vmExport(data: any): any {
  return postWithLoadTip(`/api/vmWare/export`, data);
}
