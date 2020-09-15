import request from '@/utils/request'

export function findWtpRegistry(query) {
  return request({
    url: '/wtpRegistry/find',
    method: 'get',
    params: query
  })
}

