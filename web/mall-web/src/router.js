import Vue from "vue";
import Router from "vue-router";
// import Login from './views/login/index.vue'
//  下面情况，默认的会导入 ./views/login 目录下的index.vue组件
import Login from './views/login'
import Layout from '@/components/Layout.vue'
import Home from './views/home'
// import Item from './views/item'
// import Member from './views/member'
// import Supplier from './views/supplier'
// import Goods from './views/goods'
// import Staff from './views/staff'

Vue.use(Router);

export default new Router({
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
          path: '/item-list',
          component: () => import('@/views/item/item-list.vue'),
          name: 'item-list',
          meta: { title: '商品查询', icon: 'dashboard', affix: true }
        },
        {
          path: '/item-add',
          component: () => import('@/views/item/item-add.vue'),
          name: 'item-add',
          meta: { title: '新增商品', icon: 'dashboard', affix: true }
        },
        {
          path: '/item-param-list',
          component: () => import('@/views/item/item-param-add.vue'),
          name: 'item-param-list',
          meta: { title: '规格参数', icon: 'dashboard', affix: true }
        },
      ]
    },


  ]
})
