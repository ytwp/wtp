import request from '@/utils/request'

export function page(query) {
  return request({
    url: '/app/page',
    method: 'get',
    params: query
  })
}

export function create(form) {
  return request({
    url: '/app/create',
    method: 'post',
    params: form
  })
}

export function update(form) {
  return request({
    url: '/app/update',
    method: 'post',
    params: form
  })
}

export function appOptions() {
  return request({
    url: '/app/options',
    method: 'get'
  })
}

export function get(id) {
  return request({
    url: `/app/get?id=${id}`,
    method: 'get'
  })
}

export function del(form) {
  return request({
    url: `/app/del`,
    method: 'get',
    params: form
  })
}
