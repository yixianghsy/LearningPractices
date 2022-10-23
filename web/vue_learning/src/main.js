import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import "@/plugins/ztree/js/jquery-3.2.1.min";
import "@/plugins/ztree/js/jquery.ztree.core.js";
import "@/plugins/ztree/js/jquery.ztree.excheck";
import "@/plugins/ztree/css/zTreeStyle/zTreeStyle.css";
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
