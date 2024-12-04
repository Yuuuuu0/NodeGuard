import request from '@/utils/request'
import { DictItem } from '@/types'

export function getDictList(params?: { dictType?: string }) {
  return request<DictItem[]>({
    url: '/dict/list',
    method: 'get',
    params
  })
}

export function saveDict(data: Partial<DictItem>) {
  return request({
    url: '/dict/save',
    method: 'post',
    data
  })
}

export function deleteDict(id: number) {
  return request({
    url: `/dict/${id}`,
    method: 'delete'
  })
} 