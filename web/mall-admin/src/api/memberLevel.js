import request from '@/utils/request'
let  url = 'sso'
export function fetchList(params) {
  return request({
    url: url+'/memberLevel/list',
    method:'get',
    params:params
  })
}
