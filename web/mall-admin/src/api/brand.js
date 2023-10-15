import request from '@/utils/request'
let  url = 'admin'
export function fetchList(params) {
  return request({
    url: url+ '/brand/list',
    method:'get',
    params:params
  })
}
export function createBrand(data) {
  return request({
    url: url+'/brand/create',
    method:'post',
    data:data
  })
}
export function updateShowStatus(data) {
  return request({
    url: url+'/brand/update/showStatus',
    method:'post',
    data:data
  })
}

export function updateFactoryStatus(data) {
  return request({
    url: url+'/brand/update/factoryStatus',
    method:'post',
    data:data
  })
}

export function deleteBrand(id) {
  return request({
    url: url+'/brand/delete/'+id,
    method:'get',
  })
}

export function getBrand(id) {
  return request({
    url: url+'/brand/'+id,
    method:'get',
  })
}

export function updateBrand(id,data) {
  return request({
    url: url+'/brand/update/'+id,
    method:'post',
    data:data
  })
}

