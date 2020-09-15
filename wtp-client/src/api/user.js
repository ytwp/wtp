import request from '@/utils/request'

export function page(query) {
  return request({
    url: '/user/page',
    method: 'get',
    params: query
  })
}

export function create(form) {
  return request({
    url: '/user/create',
    method: 'post',
    params: form
  })
}

export function update(form) {
  return request({
    url: '/user/update',
    method: 'post',
    params: form
  })
}

