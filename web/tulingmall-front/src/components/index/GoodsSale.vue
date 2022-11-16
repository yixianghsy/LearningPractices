<template>
  <div class="goods-sale">
    <poster :postItem="postItem1"></poster>
    <goods-box :goodsItem="goodsItem1"></goods-box> 
  </div>
</template>

<script>
import Poster from './Poster'
import GoodsBox from './GoodsBox'

export default {
  data () {
    return {
      postItem1: {src: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/1a2f39c9fe0804ace1d3707574c7c86f.jpg?w=2452&h=240', url: 'https://www.mi.com/redminote7pro/'},
     
      goodsItem1: {
        boxTitle: '手机',
        isHot: false,
        vertical: true,
        url: 'https://www.mi.com/a/h/9891.html',
        listData: {
          promo: [{src: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/79ed6663b57e30554a5d1f0653c68b78.jpg?w=468&h=1228', url: 'https://www.mi.com/mix2s/'}],
          goods: [
            {value: 'Redmi Note 8 Pro', desc: '6400万全场景四摄', sub: true, newPrice: '1399', oldPrice: '', src: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/6f2493e6c6fe8e2485c407e5d75e3651.jpg', url: 'http://www.mi.com/redminote8pro'},
            {value: 'Redmi Note 8', desc: '千元4800万四摄', sub: true, newPrice: '999', oldPrice: '', src: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/4c87947d104ee5833913e4c520108f16.jpg', url: 'http://www.mi.com/redminote8'},
            {value: 'Redmi K20 Pro 6GB+128GB', desc: '骁龙855， 弹出全面屏', sub: false, newPrice: '2299', oldPrice: '2599', src: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/beee478168e5c4fd0d66369386fddbbe.jpg', url: 'https://www.mi.com/redmik20pro/'},
            {value: 'Redmi Note 7', desc: '4800万拍照千元机', sub: false, newPrice: '1099', oldPrice: '1399', src: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/0ce61b71e2f81df62bd0c05aaa901d22.jpg', url: 'https://www.mi.com/redminote7/'},
            {value: 'Redmi 7', desc: '4000mAh超长续航', sub: true, newPrice: '699', oldPrice: '799', src: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/9aab8a7fa9005ef918c9aa2d5f17c806.jpg', url: 'https://www.mi.com/redmi7/'},
            {value: '小米MIX 3 8GB+128GB', desc: 'DxO百分拍照手机', sub: false, newPrice: '2599', oldPrice: '3599', src: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/411d540271f6efbdd882fa33daee0de3.jpg', url: 'https://www.mi.com/mix3/'},
            {value: '小米CC9', desc: '3200万自拍，4800万三摄', sub: true, newPrice: '1699', oldPrice: '1799', src: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/bd25cc614a670f4d5546fe82e239ef86.jpg', url: 'https://www.mi.com/micc9/'},
            {value: '小米9 SE', desc: '索尼4800万三摄，骁龙712', sub: true, newPrice: '1599', oldPrice: '1999', src: 'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/134efaa5dec3267ce29e36bad3ec26c9.jpg', url: 'https://www.mi.com/mi9se/'}
          ]
        }
      }, 
    }
  },
  components: {
    'Poster': Poster,
    'GoodsBox': GoodsBox
  }
}
</script>

<style lang="less">
  .goods-sale {
      position: relative;
      
    text-align: center;
      width: 1226px;
      height: auto;
      margin: 0 auto;
      display: flex;
      flex-direction: column;
  }
  .goods-sale *{    box-sizing: border-box;}
</style>