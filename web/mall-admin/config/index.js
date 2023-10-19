'use strict'
// Template version: 1.3.1
// see http://vuejs-templates.github.io/webpack for documentation.

const path = require('path')
module.exports = {
  dev: {

    // Paths
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    proxyTable: {
    //后期需要封装，避免代码臃肿
        '/sso': {
        target: 'http://sso.e3mall.cn',
        changeOrigin: true,
        pathRewrite: {
          '^/sso': ''
        }
      },
      '/admin': {
        target: 'http://localhost:8025',
        changeOrigin: true,
        pathRewrite: {
          '^/admin': ''
        }
      },
      '/order': {
        target: 'http://localhost:8027',
        changeOrigin: true,
        pathRewrite: {
          '^/order': ''
        }
      },
    //反向代理配置,在这里可以配置特定的请求代理到对应的API接口
    // 例如：localhost:8080/代理到http://localhost:8888
    // 例如：请求地址为："http://218.78.187.216/api/v1/authentication"
    // 请求的时候可以写为：
    // this.$axios.post("/api/anthentication",params).then(res=>{
    //     console.log(res);
    // },err=>{
    //     console.log(err);
    // })

      // '/api': {
      //   target: 'http://sso.mall.cn/api/v1',//设置调用的接口域名和端口，必须是http或https开头的网址,否者接口报错500、404
      //   changeOrigin: true,
      //   pathRewrite: {
      //     '^/api': ''//注意这里后面是一根斜杠也可以是空''；用'/api' 代替 'http://218.78.187.216/api/v1'
      //   }
      // },
    },

    // Various Dev Server settings
    host: 'localhost', // can be overwritten by process.env.HOST
    port: 8088, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
    autoOpenBrowser: false,
    errorOverlay: true,
    notifyOnErrors: true,
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-


    /**
     * Source Maps
     */

    // https://webpack.js.org/configuration/devtool/#development
    devtool: 'cheap-module-eval-source-map',

    // If you have problems debugging vue-files in devtools,
    // set this to false - it *may* help
    // https://vue-loader.vuejs.org/en/options.html#cachebusting
    cacheBusting: true,

    cssSourceMap: true
  },

  build: {
    // Template for index.html
    index: path.resolve(__dirname, '../dist/index.html'),

    // Paths
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: './',

    /**
     * Source Maps
     */

    productionSourceMap: true,
    // https://webpack.js.org/configuration/devtool/#production
    devtool: '#source-map',

    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],

    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
