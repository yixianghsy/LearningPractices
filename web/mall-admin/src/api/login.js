import request from '@/utils/request'
let  url = 'sso'
export function login(username, password) {
  return request({
    url: url +'/admin/login',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export function getInfo() {
  return request({
    url: url +'/admin/info',
    method: 'get',
  })
}

export function logout() {
  return request({
    url: url +'/admin/logout',
    method: 'post'
  })
}

export function fetchList(params) {
  return request({
    url: url +'/admin/list',
    method: 'get',
    params: params
  })
}

export function createAdmin(data) {
  return request({
    url: url +'/admin/register',
    method: 'post',
    data: data
  })
}

export function updateAdmin(id, data) {
  return request({
    url: url +'/admin/update/' + id,
    method: 'post',
    data: data
  })
}

export function updateStatus(id, params) {
  return request({
    url: url +'/admin/updateStatus/' + id,
    method: 'post',
    params: params
  })
}

export function deleteAdmin(id) {
  return request({
    url: url +'/admin/delete/' + id,
    method: 'post'
  })
}

export function getRoleByAdmin(id) {
  return request({
    url: url +'/admin/role/' + id,
    method: 'get'
  })
}

export function allocRole(data) {
  return request({
    url: url +'/admin/role/update',
    method: 'post',
    data: data
  })
}
