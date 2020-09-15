import request from '@/utils/request'

export function page(query) {
  return request({
    url: '/wtp/page',
    method: 'get',
    params: query
  })
}

export function create(form) {
  return request({
    url: '/wtp/create',
    method: 'post',
    params: form
  })
}

export function queueOptions() {
  return request({
    url: '/wtp/queueOptions',
    method: 'get'
  })
}

export function rejectedOptions() {
  return request({
    url: '/wtp/rejectedOptions',
    method: 'get'
  })
}

export function get(wtpId) {
  return request({
    url: `/wtp/get?wtpId=${wtpId}`,
    method: 'get'
  })
}

export function update(form) {
  return request({
    url: '/wtp/update',
    method: 'post',
    params: form
  })
}

export function del(form) {
  return request({
    url: '/wtp/del',
    method: 'post',
    params: form
  })
}
