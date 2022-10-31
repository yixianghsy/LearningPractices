import Vue from "vue";
// 注意引入在 Vue的下面
//  ElementUI 组件库
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from "./App.vue";
import router from "./router";

// 使用 ElementUI
Vue.use(ElementUI);

// 权限拦截
import './permission'

Vue.config.productionTip = process.env.NODE_ENV === 'production';
console.log(process.env.VUE_APP_SERVICE_URL) // 开发环境 development, 生产环境 production 

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
