import Vue from 'vue'
import VueRouter from 'vue-router';
import Login from './views/login'
import Home from './views/home'
import Layout from '@/components/Layout.vue'
Vue.use(VueRouter)
const router = new VueRouter({ 
    mode:"history", 
    routes: [
      {
        path: '/login',
        name: 'login', //路由名称
        component: Login //组件对象
      },
      {
        path: '/',
        name: 'layout', //路由名称
        component: Layout, //组件对象
        redirect: '/home',
        children: [
          {
            path: '/home',
            component: Home,
            meta: {
              title: '首页'}
          },
          {
            path: "/member",
            name: "member",
            component: resolve => require(["@/views/member/index.vue"], resolve),
          },
          {
            path: "/supplier",
            name: "supplier",
            component: resolve => require(["@/views/supplier/index.vue"], resolve),
          },

        
        ]
      },
    ]
});
// 重写路由push方法
const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) return routerPush.call(this, location, onResolve, onReject);
  return routerPush.call(this, location).catch(error => error)
};
// 设置路由拦截
// router.beforeEach((to, from, next) => {
//   let isLogin = sessionStorage.getItem('token');
//   if (isLogin) {
//     //如果用户信息存在则往下执行。
//     next()
//   } else {
//     //如果用户token不存在则跳转到login页面
//     if (to.path === '/login') {
//       next()
//     } else {
//       next('/login')
//     }
//   }
// })
export default router