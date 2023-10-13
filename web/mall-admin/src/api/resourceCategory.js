import request from '@/utils/request'
let  url = 'sso'
export function listAllCate() {
  return request({
    url: url+'/resourceCategory/listAll',
    method: 'get'
  })
}

export function createResourceCategory(data) {
  return request({
    url: url+'/resourceCategory/create',
    method: 'post',
    data: data
  })
}

export function updateResourceCategory(id, data) {
  return request({
    url: url+'/resourceCategory/update/' + id,
    method: 'post',
    data: data
  })
}

export function deleteResourceCategory(id) {
  return request({
    url: url+'/resourceCategory/delete/' + id,
    method: 'post'
  })
}
