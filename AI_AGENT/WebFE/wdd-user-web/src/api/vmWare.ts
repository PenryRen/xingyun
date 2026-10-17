import {post, postWithLoadTip} from '@/utils/request'

/**
 * 检查授权过期时间
 * @param data
 */
export function refreshExpiration(data: any): any {
  return post(`/api/vmWare/refreshExpiration`, data);
}

/**
 * 检查虚拟机
 * @param data
 */
export function checkVmWare(data: any): any {
  return post(`/api/vmWare/checkVmWare`, data);
}

/**
 * 分配虚拟机
 * @param data
 */
export function getVmWare(data: any): any {
  return post(`/api/vmWare/getVmWare`, data);
}

/**
 * 核验课程培训检查点
 * @param data
 */
export function checkTrain(data: any): any {
  return post(`/api/vmWare/checkTrain`, data);
}

/**
 * 查询虚拟机剩余时间(实训)
 * @param data
 */
export function queryRemainingTime(data: any): any {
  return post(`/api/vmWare/queryRemainingTime`, data);
}

/**
 * 虚拟机续期(实训)
 * @param data
 */
export function renewal(data: any): any {
  return post(`/api/vmWare/renewal`, data);
}
