import request from '@/utils/request'
import { UserInfo } from '@/types'

export function getUserList() {
  return request<UserInfo[]>({
    url: '/user/list',
    method: 'get'
  })
}

export function saveUser(data: Partial<UserInfo>) {
  return request({
    url: '/user/save',
    method: 'post',
    data
  })
}

export function deleteUser(id: number) {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
} 