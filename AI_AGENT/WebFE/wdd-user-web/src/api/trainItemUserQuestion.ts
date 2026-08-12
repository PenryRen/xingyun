import {post, postWithLoadTip} from '@/utils/request'

/**
 * 获取用户课件功能点列表
 * @param data
 */
export function userQuestionList(data: any): any {
  return post(`/api/train/item/user/question/list`, data);
}
