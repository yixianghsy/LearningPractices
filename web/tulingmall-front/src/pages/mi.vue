<template>
  <div class="index-page"> 
    <menu-banner></menu-banner>
    <div class="gray-page"> 
      <goods-sale></goods-sale> 
    </div>
  </div>
</template>

<script> 
import MenuBanner from '../components/index/MenuBanner' 
import GoodsSale from '../components/index/GoodsSale'

export default {
  data () {
    return {}
  },
  components: { 
    'MenuBanner': MenuBanner, 
    'GoodsSale': GoodsSale,
  }
}
</script>

<style lang="less">
  .index-page {
    .gray-page {
      display: flex;
      flex-direction: column;
      width: 100%;
      height: auto;
      background-color: #f5f5f5;
    }
  }
</style>