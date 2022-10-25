import request from "@/utils/request"
export default {
    // 分页获取列表
    search(page, size, searchMap) {
        return request({
            url: `/goods/list/search/${page}/${size}`,
            method: 'post',
            data: searchMap
        })
    },
}