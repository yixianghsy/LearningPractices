import request from '@/utils/request'
let url =  'order'
export function getOrderSetting(id) {
  return request({
    url:url +'/orderSetting/'+id,
    method:'get',
  })
}

export function updateOrderSetting(id,data) {
  return request({
    url: url+'/orderSetting/update/'+id,
    method:'post',
    data:data
  })
}
