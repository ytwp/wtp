import request from '@/utils/request'

export function realTime(form) {
  return request({
    url: '/wtpLog/realTime',
    method: 'get',
    params: form
  })
}

export function report(form) {
  return request({
    url: '/wtpLog/chart',
    method: 'get',
    params: form
  })
}
