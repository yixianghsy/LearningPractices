module.exports = {
  devServer:{  
    disableHostCheck: true, // 让vue不强制要求使用host来访问
    host:'localhost',
    port:8081, 
    proxy:{
      '/api':{
        // target:'http://localhost:8081',
        // target:'http://yangguo.natapp1.cc',
       target:'http://localhost:8888',
        changeOrigin:true,
        pathRewrite:{
          '/api':''
        }
      },
      'baiduMapApi':{ 
        target:'http://api.map.baidu.com',
        changeOrigin:true,
        pathRewrite:{
          '/baiduMapApi':''
        }
      }
    }
  },
  // publicPath:'/app',
  // outputDir:'dist',
  // indexPath:'index2.html',
  // lintOnSave:false,
  productionSourceMap:true,
  chainWebpack:(config)=>{
    config.plugins.delete('prefetch');
  }
}