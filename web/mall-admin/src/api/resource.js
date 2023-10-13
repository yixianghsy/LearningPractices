import request from '@/utils/request'
let  url = 'sso'
export function fetchList(params) {
  return request({
    url: url+'/resource/list',
    method: 'get',
    params: params
  })
}

export function createResource(data) {
  return request({
    url: url+'/resource/create',
    method: 'post',
    data: data
  })
}

export function updateResource(id, data) {
  return request({
    url: url+'/resource/update/' + id,
    method: 'post',
    data: data
  })
}

export function deleteResource(id) {
  return request({
    url: url+'/resource/delete/' + id,
    method: 'post'
  })
}

export function fetchAllResourceList() {
  return request({
    url: url+'/resource/listAll',
    method: 'get'
  })
}
