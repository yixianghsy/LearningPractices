import request from '@/utils/request'
let  url = 'sso'
export function fetchList(params) {
  return request({
    url: url +'/role/list',
    method: 'get',
    params: params
  })
}

export function createRole(data) {
  return request({
    url: url +'/role/create',
    method: 'post',
    data: data
  })
}

export function updateRole(id, data) {
  return request({
    url: url +'/role/update/' + id,
    method: 'post',
    data: data
  })
}

export function updateStatus(id, params) {
  return request({
    url: url +'/role/updateStatus/' + id,
    method: 'post',
    params: params
  })
}

export function deleteRole(data) {
  return request({
    url: url +'/role/delete',
    method:'post',
    data:data
  })
}

export function fetchAllRoleList() {
  return request({
    url: url +'/role/listAll',
    method: 'get'
  })
}

export function listMenuByRole(roleId) {
  return request({
    url: url +'/role/listMenu/'+roleId,
    method: 'get'
  })
}

export function listResourceByRole(roleId) {
  return request({
    url: url +'/role/listResource/'+roleId,
    method: 'get'
  })
}

export function allocMenu(data) {
  return request({
    url: url +'/role/allocMenu',
    method: 'post',
    data:data
  })
}

export function allocResource(data) {
  return request({
    url: url +'/role/allocResource',
    method: 'post',
    data:data
  })
}
