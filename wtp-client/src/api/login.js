import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/info',
    method: 'get',
    params: { token }
  })
}

export function logout(token) {
  return request({
    url: '/logout',
    method: 'post',
    params: { token: token }
  })
}
