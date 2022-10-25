// import axios from '@/utils/request'
import request from '@/utils/request'

// /dev-api http://mengxuegu.com:7300/mock/5d477ccbfacc296cd6834fe5/db.json
const BASE_URI = process.env.VUE_APP_BASE_API

// request.get('/db.json').then(response => {
//     console.log(response.data)
// })

// 测试2， 以对象配置的方式进行指定请求方式
request({
    method: 'get',
    // url: BASE_URI + '/db.json'
    url: '/db.json'
}).then(response => {
    console.log('get2', response.data)
})

export default {
    getList() {
        const req = request({
            method: 'get',
            // url: BASE_URI + '/db.json'
            url: '/db.json'
        })
        console.log(req) // Promise.then()
        return req
    }
    
}