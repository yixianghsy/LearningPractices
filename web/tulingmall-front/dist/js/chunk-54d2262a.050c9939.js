(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-54d2262a"],{1276:function(t,e,i){"use strict";var n=i("d784"),r=i("44e7"),s=i("825a"),a=i("1d80"),c=i("4840"),l=i("8aa5"),o=i("50c4"),u=i("14c3"),d=i("9263"),p=i("d039"),f=[].push,v=Math.min,h=4294967295,g=!p((function(){return!RegExp(h,"y")}));n("split",2,(function(t,e,i){var n;return n="c"=="abbc".split(/(b)*/)[1]||4!="test".split(/(?:)/,-1).length||2!="ab".split(/(?:ab)*/).length||4!=".".split(/(.?)(.?)/).length||".".split(/()()/).length>1||"".split(/.?/).length?function(t,i){var n=String(a(this)),s=void 0===i?h:i>>>0;if(0===s)return[];if(void 0===t)return[n];if(!r(t))return e.call(n,t,s);var c,l,o,u=[],p=(t.ignoreCase?"i":"")+(t.multiline?"m":"")+(t.unicode?"u":"")+(t.sticky?"y":""),v=0,g=new RegExp(t.source,p+"g");while(c=d.call(g,n)){if(l=g.lastIndex,l>v&&(u.push(n.slice(v,c.index)),c.length>1&&c.index<n.length&&f.apply(u,c.slice(1)),o=c[0].length,v=l,u.length>=s))break;g.lastIndex===c.index&&g.lastIndex++}return v===n.length?!o&&g.test("")||u.push(""):u.push(n.slice(v)),u.length>s?u.slice(0,s):u}:"0".split(void 0,0).length?function(t,i){return void 0===t&&0===i?[]:e.call(this,t,i)}:e,[function(e,i){var r=a(this),s=void 0==e?void 0:e[t];return void 0!==s?s.call(e,r,i):n.call(String(r),e,i)},function(t,r){var a=i(n,t,this,r,n!==e);if(a.done)return a.value;var d=s(t),p=String(this),f=c(d,RegExp),x=d.unicode,m=(d.ignoreCase?"i":"")+(d.multiline?"m":"")+(d.unicode?"u":"")+(g?"y":"g"),C=new f(g?d:"^(?:"+d.source+")",m),b=void 0===r?h:r>>>0;if(0===b)return[];if(0===p.length)return null===u(C,p)?[p]:[];var y=0,_=0,E=[];while(_<p.length){C.lastIndex=g?_:0;var I,w=u(C,g?p:p.slice(_));if(null===w||(I=v(o(C.lastIndex+(g?0:_)),p.length))===y)_=l(p,_,x);else{if(E.push(p.slice(y,_)),E.length===b)return E;for(var R=1;R<=w.length-1;R++)if(E.push(w[R]),E.length===b)return E;_=y=I}}return E.push(p.slice(y)),E}]}),!g)},"14c3":function(t,e,i){var n=i("c6b6"),r=i("9263");t.exports=function(t,e){var i=t.exec;if("function"===typeof i){var s=i.call(t,e);if("object"!==typeof s)throw TypeError("RegExp exec method returned something other than an Object or null");return s}if("RegExp"!==n(t))throw TypeError("RegExp#exec called on incompatible receiver");return r.call(t,e)}},"44e7":function(t,e,i){var n=i("861d"),r=i("c6b6"),s=i("b622"),a=s("match");t.exports=function(t){var e;return n(t)&&(void 0!==(e=t[a])?!!e:"RegExp"==r(t))}},"5cdc":function(t,e,i){},6547:function(t,e,i){var n=i("a691"),r=i("1d80"),s=function(t){return function(e,i){var s,a,c=String(r(e)),l=n(i),o=c.length;return l<0||l>=o?t?"":void 0:(s=c.charCodeAt(l),s<55296||s>56319||l+1===o||(a=c.charCodeAt(l+1))<56320||a>57343?t?c.charAt(l):s:t?c.slice(l,l+2):a-56320+(s-55296<<10)+65536)}};t.exports={codeAt:s(!1),charAt:s(!0)}},8613:function(t,e,i){"use strict";i.r(e);var n=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"detail"},[i("product-param",{attrs:{title:t.product.name}}),i("div",{staticClass:"wrapper"},[i("div",{staticClass:"container clearfix"},[i("div",{staticClass:"swiper"},[i("swiper",{attrs:{options:t.swiperOption}},[t._l(t.albumPics,(function(t,e){return i("swiper-slide",{key:e},[i("img",{attrs:{src:t,alt:""}})])})),i("div",{staticClass:"swiper-pagination",attrs:{slot:"pagination"},slot:"pagination"})],2)],1),i("div",{staticClass:"content"},[i("h2",{staticClass:"item-title"},[t._v(t._s(t.product.name))]),i("p",{staticClass:"item-info"},[t._v(t._s(t.product.subTitle))]),i("div",{staticClass:"item-price"},[t._v(t._s(t.product.price)+"元")]),i("div",{staticClass:"line"}),t._m(0),i("div",{staticClass:"item-version clearfix"}),i("div",{staticClass:"item-total"},[i("div",{staticClass:"phone-total"},[t._v("总计："+t._s(t.product.price)+"元")])]),i("div",{staticStyle:{margin:"20px","font-size":"24px"}},[i("span",[t._v("验证码:")]),t._v(" "),i("input",{directives:[{name:"model",rawName:"v-model",value:t.verifyCode,expression:"verifyCode"}],staticStyle:{width:"80px",height:"32px",margin:"20px","font-size":"18px"},attrs:{type:"text",name:"verifyCode"},domProps:{value:t.verifyCode},on:{input:function(e){e.target.composing||(t.verifyCode=e.target.value)}}}),t._v(" "),i("img",{attrs:{src:t.verifyCodeInfoUrl,stype:"width:80px;height:32px;margin: 20px;"},on:{click:t.refreshCode}})]),i("div",{staticClass:"btn-group"},[i("a",{staticClass:"btn btn-huge fl",attrs:{href:"javascript:;"},on:{click:t.secKill}},[t._v("立即秒杀")])])])])]),t._m(1),i("service-bar")],1)},r=[function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"item-addr"},[i("i",{staticClass:"icon-loc"}),i("div",{staticClass:"addr"},[t._v("北京 北京市 朝阳区 安定门街道")]),i("div",{staticClass:"stock"},[t._v("有现货")])])},function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"price-info"},[i("div",{staticClass:"container"},[i("h2",[t._v("价格说明")]),i("div",{staticClass:"desc"},[i("img",{attrs:{src:"/imgs/detail/item-price.jpeg",alt:""}})])])])}],s=(i("ac1f"),i("1276"),i("7212")),a=i("9f26"),c=i("984c"),l={name:"detail",data:function(){return{id:this.$route.params.id,err:"",version:1,product:{},swiperOption:{autoplay:!0,pagination:{el:".swiper-pagination",clickable:!0}},skuId:0,albumPics:[],clicked:null,verifyCodeInfoUrl:"",verifyCode:""}},components:{swiper:s["swiper"],swiperSlide:s["swiperSlide"],ProductParam:a["a"],ServiceBar:c["a"]},mounted:function(){this.getProductInfo(),this.getVerifyCodeInfo()},methods:{getProductInfo:function(){var t=this;this.axios.get("/pms/productInfo/".concat(this.id)).then((function(e){t.product=e,t.albumPics=e.albumPics.split(","),null!=t.albumPics&&void 0!=t.albumPics&&""!=t.albumPics||(t.albumPics=e.pic.split(","))}))},secKill:function(){var t=this,e="/order/token?productId="+this.id+"&verifyCode="+this.verifyCode;this.axios.get(e).then((function(e){t.$router.push({path:"/secKillOrderConfirm/"+t.id+"/"+e})}))},getVerifyCodeInfo:function(){var t=this;this.axios.get("/order/verifyCode?productId="+this.id).then((function(e){t.verifyCodeInfoUrl="data:image/png;base64,"+e}))},refreshCode:function(){this.getVerifyCodeInfo()}}},o=l,u=(i("b493"),i("2877")),d=Object(u["a"])(o,n,r,!1,null,null,null);e["default"]=d.exports},"8aa5":function(t,e,i){"use strict";var n=i("6547").charAt;t.exports=function(t,e,i){return e+(i?n(t,e).length:1)}},9263:function(t,e,i){"use strict";var n=i("ad6d"),r=i("9f7f"),s=RegExp.prototype.exec,a=String.prototype.replace,c=s,l=function(){var t=/a/,e=/b*/g;return s.call(t,"a"),s.call(e,"a"),0!==t.lastIndex||0!==e.lastIndex}(),o=r.UNSUPPORTED_Y||r.BROKEN_CARET,u=void 0!==/()??/.exec("")[1],d=l||u||o;d&&(c=function(t){var e,i,r,c,d=this,p=o&&d.sticky,f=n.call(d),v=d.source,h=0,g=t;return p&&(f=f.replace("y",""),-1===f.indexOf("g")&&(f+="g"),g=String(t).slice(d.lastIndex),d.lastIndex>0&&(!d.multiline||d.multiline&&"\n"!==t[d.lastIndex-1])&&(v="(?: "+v+")",g=" "+g,h++),i=new RegExp("^(?:"+v+")",f)),u&&(i=new RegExp("^"+v+"$(?!\\s)",f)),l&&(e=d.lastIndex),r=s.call(p?i:d,g),p?r?(r.input=r.input.slice(h),r[0]=r[0].slice(h),r.index=d.lastIndex,d.lastIndex+=r[0].length):d.lastIndex=0:l&&r&&(d.lastIndex=d.global?r.index+r[0].length:e),u&&r&&r.length>1&&a.call(r[0],i,(function(){for(c=1;c<arguments.length-2;c++)void 0===arguments[c]&&(r[c]=void 0)})),r}),t.exports=c},"984c":function(t,e,i){"use strict";var n=function(){var t=this,e=t.$createElement;t._self._c;return t._m(0)},r=[function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"service"},[i("div",{staticClass:"container"},[i("ul",[i("li",[i("span",{staticClass:"icon-setting"}),t._v("预约维修服务")]),i("li",[i("span",{staticClass:"icon-7day"}),t._v("7天无理由退货")]),i("li",[i("span",{staticClass:"icon-15day"}),t._v("15天免费换货")]),i("li",[i("span",{staticClass:"icon-post"}),t._v("满150元包邮")])])])])}],s={name:"service-bar"},a=s,c=(i("f8b2"),i("2877")),l=Object(c["a"])(a,n,r,!1,null,null,null);e["a"]=l.exports},"9f7f":function(t,e,i){"use strict";var n=i("d039");function r(t,e){return RegExp(t,e)}e.UNSUPPORTED_Y=n((function(){var t=r("a","y");return t.lastIndex=2,null!=t.exec("abcd")})),e.BROKEN_CARET=n((function(){var t=r("^r","gy");return t.lastIndex=2,null!=t.exec("str")}))},ac1f:function(t,e,i){"use strict";var n=i("23e7"),r=i("9263");n({target:"RegExp",proto:!0,forced:/./.exec!==r},{exec:r})},ad6d:function(t,e,i){"use strict";var n=i("825a");t.exports=function(){var t=n(this),e="";return t.global&&(e+="g"),t.ignoreCase&&(e+="i"),t.multiline&&(e+="m"),t.dotAll&&(e+="s"),t.unicode&&(e+="u"),t.sticky&&(e+="y"),e}},b493:function(t,e,i){"use strict";i("5cdc")},b8a9:function(t,e,i){},d784:function(t,e,i){"use strict";i("ac1f");var n=i("6eeb"),r=i("d039"),s=i("b622"),a=i("9263"),c=i("9112"),l=s("species"),o=!r((function(){var t=/./;return t.exec=function(){var t=[];return t.groups={a:"7"},t},"7"!=="".replace(t,"$<a>")})),u=function(){return"$0"==="a".replace(/./,"$0")}(),d=s("replace"),p=function(){return!!/./[d]&&""===/./[d]("a","$0")}(),f=!r((function(){var t=/(?:)/,e=t.exec;t.exec=function(){return e.apply(this,arguments)};var i="ab".split(t);return 2!==i.length||"a"!==i[0]||"b"!==i[1]}));t.exports=function(t,e,i,d){var v=s(t),h=!r((function(){var e={};return e[v]=function(){return 7},7!=""[t](e)})),g=h&&!r((function(){var e=!1,i=/a/;return"split"===t&&(i={},i.constructor={},i.constructor[l]=function(){return i},i.flags="",i[v]=/./[v]),i.exec=function(){return e=!0,null},i[v](""),!e}));if(!h||!g||"replace"===t&&(!o||!u||p)||"split"===t&&!f){var x=/./[v],m=i(v,""[t],(function(t,e,i,n,r){return e.exec===a?h&&!r?{done:!0,value:x.call(e,i,n)}:{done:!0,value:t.call(i,e,n)}:{done:!1}}),{REPLACE_KEEPS_$0:u,REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE:p}),C=m[0],b=m[1];n(String.prototype,t,C),n(RegExp.prototype,v,2==e?function(t,e){return b.call(t,this,e)}:function(t){return b.call(t,this)})}d&&c(RegExp.prototype[v],"sham",!0)}},f8b2:function(t,e,i){"use strict";i("b8a9")}}]);
//# sourceMappingURL=chunk-54d2262a.050c9939.js.map