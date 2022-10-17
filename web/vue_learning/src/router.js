// 1\. 导入模块
import Vue from 'vue';
import VueRouter from 'vue-router';

// 2\. 使用VueRouter插件
Vue.use(VueRouter)

// 3\. 导入组件
import Home from './components/Home.vue';
import List from './components/List.vue';
import About from './components/About.vue';

// 4\. 配置路由与组件映射关系
let routes = [
  {
    path:"/",
    // 路由重定向:
    // 虽然访问的是/根路由,但是自动重新转义到/home路由
    redirect: "/home"
  },
    {
        path:'/home',
        component: Home
    },
    {
        path:'/list',
        component: List
    },
    {
        path:'/about',
        component: About
    }
]

// 5.实例化路由
const router = new VueRouter({
    mode:"history",
    routes
})

export default router
