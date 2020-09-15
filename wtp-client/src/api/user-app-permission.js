import request from '@/utils/request'

export function find(query) {
  return request({
    url: '/userAppPermission/find',
    method: 'get',
    params: query
  })
}

export function create(form) {
  return request({
    url: '/userAppPermission/create',
    method: 'post',
    params: form
  })
}

export function del(form) {
  return request({
    url: '/userAppPermission/del',
    method: 'post',
    params: form
  })
}
