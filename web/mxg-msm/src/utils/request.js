import axios from 'axios'
// 按需导入 ElementUI 组件
import { Loading, Message } from 'element-ui'

// 加载数据时打开和关闭动效对象
const loading = {
    loadingInstance: null, // Loading实例
    open: function () { // 打开加载
        console.log('加载中', this.loadingInstance)
        if (this.loadingInstance === null) { // 创建单例, 防止切换路由重复加载
            console.log('创建加载实例..')
            this.loadingInstance = Loading.service({
                text: '拼命加载中',
                target: '.main', // 效果显示区域
                background: 'rgba(0, 0, 0, 0.5)' // 加载效果
            })
        }
    },
    close: function () { // 关闭加载
        if (this.loadingInstance !== null) {
            this.loadingInstance.close()
            console.log('结束加载')
        }
        this.loadingInstance = null // 关闭后实例重新赋值null, 下次让它重新创建
    }
}

const request = axios.create({
    // /db.json >  通过 axios > /dev-api/db.json >  通过 代理转发（vue.config.js）》 http://localhost:8001/db.json
    // baseURL: '/dev-api', 
    baseURL: process.env.VUE_APP_BASE_API,
    // baseURL: '/',
    timeout: 5000 // 请求超时，5000毫秒
})

// 请求拦截器
request.interceptors.request.use(config => {
    loading.open()//打开效果
    // 请求拦截
    return config
}, error => {
    // 出现异常
    //关闭加载效果
    loading.close()
    return Promise.reject(error);
})
// 响应拦截器
request.interceptors.response.use(response => {
    //关闭加载效果
    loading.close()
    const resp = response.data
    //如果后台响应状态码不是2000.说明后台服务器又异常，统一在此处处理
    if (resp.code !== 2000) {
        Message({
            message: resp.message || '系统异常',
            type: 'warning',
            duration: 5 * 1000//停留时间
        })
    }
    // response.data
    return response
}, error => {
    //关闭加载效果
    loading.close()
    // 当请求接口出错时, 进行弹出错误提示, 如 404, 500, 请求超时
    console.log('response error', error.response.status)
    Message({
        message: error.message,
        type: 'error',
        duration: 5 * 1000
    })
    return Promise.reject(error);
})

//  http://localhost:8888/dev-api/db.json 404
// request.get('/db.json').then(response => {
//     console.log(response.data)
// })

export default request // 导出自定义创建 axios 对象