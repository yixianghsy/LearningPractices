(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-3023edc4"],{2320:function(t,s,i){"use strict";i.r(s);var e=function(){var t=this,s=t.$createElement,i=t._self._c||s;return i("div",{staticClass:"product"},[i("product-param",{attrs:{title:t.product.name},scopedSlots:t._u([{key:"buy",fn:function(){return[i("button",{staticClass:"btn",on:{click:t.buy}},[t._v("立即购买")])]},proxy:!0}])}),i("div",{staticClass:"content"},[i("div",{staticClass:"item-bg"},[i("h2",[t._v(t._s(t.product.name))]),i("h3",[t._v(t._s(t.product.subTitle))]),i("div",{staticClass:"price"},[i("span",[t._v("￥"),i("em",[t._v(t._s(t.product.price))])])])]),i("div",{staticClass:"item-bg-2"}),i("div",{staticClass:"item-bg-3"}),i("div",{staticClass:"item-swiper"},[i("swiper",{attrs:{options:t.swiperOption}},[i("swiper-slide",[i("img",{attrs:{src:"/imgs/product/gallery-2.png",alt:""}})]),i("swiper-slide",[i("img",{attrs:{src:"/imgs/product/gallery-3.png",alt:""}})]),i("swiper-slide",[i("img",{attrs:{src:"/imgs/product/gallery-4.png",alt:""}})]),i("swiper-slide",[i("img",{attrs:{src:"/imgs/product/gallery-5.jpg",alt:""}})]),i("swiper-slide",[i("img",{attrs:{src:"/imgs/product/gallery-6.jpg",alt:""}})]),i("div",{staticClass:"swiper-pagination",attrs:{slot:"pagination"},slot:"pagination"})],1),i("p",{staticClass:"desc"},[t._v("小米8 AI变焦双摄拍摄")])],1),i("div",{staticClass:"item-video"},[t._m(0),t._m(1),i("div",{staticClass:"video-bg",on:{click:function(s){t.showSlide="slideDown"}}}),i("div",{directives:[{name:"show",rawName:"v-show",value:t.showSlide,expression:"showSlide"}],staticClass:"video-box"},[i("div",{staticClass:"overlay"}),i("div",{staticClass:"video",class:t.showSlide},[i("span",{staticClass:"icon-close",on:{click:t.closeVideo}}),i("video",{attrs:{src:"/imgs/product/video.mp4",muted:"",autoplay:"",controls:"controls"},domProps:{muted:!0}})])])])])],1)},a=[function(){var t=this,s=t.$createElement,i=t._self._c||s;return i("h2",[t._v("60帧超慢动作摄影"),i("br"),t._v("慢慢回味每一瞬间的精彩")])},function(){var t=this,s=t.$createElement,i=t._self._c||s;return i("p",[t._v("后置960帧电影般超慢动作视频，将眨眼间的美妙展现得淋漓尽致！"),i("br"),t._v("更能AI 精准分析视频内容，15个场景智能匹配背景音效。")])}],o=i("7212"),r=i("9f26"),c={name:"product",components:{swiper:o["swiper"],swiperSlide:o["swiperSlide"],ProductParam:r["a"]},data:function(){return{showSlide:"",product:{},swiperOption:{autoplay:!0,slidesPerView:3,spaceBetween:30,freeMode:!0,pagination:{el:".swiper-pagination",clickable:!0}}}},mounted:function(){window.scroll(0,0),this.getProductInfo()},methods:{getProductInfo:function(){var t=this,s=this.$route.params.id;this.axios.get("/pms/productInfo/".concat(s)).then((function(s){t.product=s}))},buy:function(){var t=this.$route.params.id;this.$router.push("/detail/".concat(t))},secKillbuy:function(){var t=this.$route.params.id;this.$router.push("/secKillDetail/".concat(t))},closeVideo:function(){var t=this;this.showSlide="slideUp",setTimeout((function(){t.showSlide=""}),600)}}},n=c,l=(i("7aba"),i("2877")),d=Object(l["a"])(n,e,a,!1,null,null,null);s["default"]=d.exports},"7aba":function(t,s,i){"use strict";i("7dc5")},"7dc5":function(t,s,i){}}]);
//# sourceMappingURL=chunk-3023edc4.ecf89fea.js.map