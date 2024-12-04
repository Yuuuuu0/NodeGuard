import request from '@/utils/request'
import type { VlessNode } from '@/types'

export function getNodeList() {
  return request<VlessNode[]>({
    url: '/vless/list',
    method: 'get'
  })
}

export function saveNode(data: Partial<VlessNode>) {
  return request({
    url: '/vless/save',
    method: 'post',
    data
  })
}

export function deleteNode(id: number) {
  return request({
    url: `/vless/${id}`,
    method: 'delete'
  })
}

export function importNode(vlessUrl: string) {
  return request({
    url: '/vless/import',
    method: 'post',
    data: vlessUrl
  })
}

export function batchDelete(ids: number[]) {
  return request({
    url: '/vless/batch',
    method: 'delete',
    data: ids
  })
} 