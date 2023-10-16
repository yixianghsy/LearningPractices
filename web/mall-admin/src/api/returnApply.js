import request from '@/utils/request'
let url =  'order'
export function fetchList(params) {
  return request({
    url:url+ '/returnApply/list',
    method:'get',
    params:params
  })
}

export function deleteApply(params) {
  return request({
    url:url+'/returnApply/delete',
    method:'post',
    params:params
  })
}
export function updateApplyStatus(id,data) {
  return request({
    url:url+'/returnApply/update/status/'+id,
    method:'post',
    data:data
  })
}

export function getApplyDetail(id) {
  return request({
    url:url+'/returnApply/'+id,
    method:'get'
  })
}
