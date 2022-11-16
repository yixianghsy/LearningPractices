import request from '@/utils/request'
export default {
     // page 当前页码, size每页查询条数, searchMap条件查询的条件值
         // 获取会员列表数据
    getList() {
        return request({
            url: 'item/list',
            method: 'get'
        })
    },
    search(page, rows, searchMap) {
        return request({
            url: `/item/list/${page}/${rows}`,
            method: 'get',
            data: searchMap
        })
    }
}
