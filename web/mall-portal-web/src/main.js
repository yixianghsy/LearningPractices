import Vue from 'vue'
import ElementUI from 'element-ui'//新添加1
import 'element-ui/lib/theme-chalk/index.css' //新添加2，避免后期打包样式不同，要放在
import App from './App.vue'
import router from './router'
import store from './store'

Vue.use(ElementUI)  //新添加3
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
