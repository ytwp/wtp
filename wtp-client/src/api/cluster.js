import request from '@/utils/request'

export function page(query) {
  return request({
    url: '/cluster/page',
    method: 'get',
    params: query
  })
}

export function create(form) {
  return request({
    url: '/cluster/create',
    method: 'post',
    params: form
  })
}

export function update(form) {
  return request({
    url: '/cluster/update',
    method: 'post',
    params: form
  })
}

export function clusterOptions(appId) {
  return request({
    url: `/cluster/options?appId=${appId}`,
    method: 'get'
  })
}

export function get(id) {
  return request({
    url: `/cluster/get?id=${id}`,
    method: 'get'
  })
}
