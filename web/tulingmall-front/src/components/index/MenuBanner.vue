<template>
  <div class="menu-banner">
    <div class="menu-container" @mouseleave="bannerMenuHide()">
      <!-- 循环所有的分类 -->
      <ul class="menu-list">
        <li
          class="list-item"
          v-for="(item, index) in menus"
          :key="index"
          @mouseenter="bannerMenuShow(item.type)"
        >
          <a :href="item.url" target="blank">{{ item.value }}</a>
          <i class="fa fa-angle-right"></i>
        </li>
      </ul>
    </div>
    <div
      class="menu-info"
      v-show="bannerMenuFlag"
      @mouseleave="bannerMenuHide()"
      @mouseenter="bannerMenuShow()"
    >
      <ul
        class="menu-info-list"
        v-for="(list, key) in menuListMatch"
        :key="key"
        :data-key="key"
      >
        <li
          class="info-list-item"
          v-for="(item, index) in list"
          :key="index"
          :data-index="index"
        >
          <a :href="'/#/detail/'+item.id">
            <img :src="item.pic" :alt="item.name" />
            <span class="">{{ item.name }}</span>
          </a>
        </li>
      </ul>
    </div>
    <banner :banners="banners"></banner>
  </div>
</template>

<script>
import Banner from "./Banner";

export default {
  data() {
    return {
      bannerMenuFlag: false,
      menuTimer: "",
      listInfoData: [],   
      // 一级分类
      menus: [
        // {
        //   value: "手机 电话卡",
        //   url: "/#/searchResult/s",
        //   type: "phone",
        // },
      ],
      banners: [
        // {
        //   src:
        //     "https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/cefed8336bae62768afeeb6a3b8f55c8.jpg?w=2452&h=920",
        //   url: "https://www.mi.com/redminote7/",
        // } 
      ],  
      products:{
      }
    };
  },
  methods: { 
    bannerMenuHide() {
      this.menuTimer = setTimeout(() => {
        this.bannerMenuFlag = false;
      }, 300);
    },
    bannerMenuShow(type) {   //2
      if (type) {
        this.listInfoData = this.products[type]; 
      }

      this.bannerMenuFlag = true;
      clearTimeout(this.menuTimer);
    },
    init() {
        this.axios.get("/home/menus_banner").then((res) => { 
          // 处理类型
          let homeMenusList= res.homeMenusList;
          for (let i = 0; i < homeMenusList.length; i++) {
            const cate = homeMenusList[i];
            this.menus.push({
              value:cate.name,
              type:cate.id,
              url:"/#/searchResult/"+cate.name
            });
            
            // 处理商品
            this.products[cate.id]= cate.productList 
          }

          // banner
          let homeAdvertisesList= res.homeAdvertisesList;
          for (let index = 0; index < homeAdvertisesList.length; index++) {
            const adver = homeAdvertisesList[index];
            this.banners.push({
              src:adver.pic,
              url:adver.url
            })
            
          }
          
       });

      // this.axios.get("/home/content").then((res) => {
      //   this.banners = res.advertiseList;
      // });
    },
  },
  // 生命周期钩子函数
  mounted() {
    this.init();
  },
  computed: {
    menuListMatch() {
      const matchData = [];
      if (this.listInfoData && this.listInfoData.length) {
        for (let i = 0; i < this.listInfoData.length; i += 6) {
          matchData.push(this.listInfoData.slice(i, i + 6));
        }
      }
      return matchData;
      
    },
  },
  components: {
    Banner: Banner,
  },
};
</script>

<style lang="less">
.menu-banner {
  position: relative;
  width: 1226px;
  height: auto;
  margin: 0 auto;
  .menu-container {
    position: absolute;
    left: 0;
    top: 0;
    width: 235px;
    height: auto;
    background: rgba(0, 0, 0, 0.3);
    z-index: 10;
  }
}

.menu-list {
  padding: 20px 0;
  height: 420px;
  .list-item {
    display: flex;
    padding-left: 30px;
    &:hover {
      background-color: #ff6700;
    }

    a {
      position: relative;
      display: inline-block;
      width: 170px;
      height: 42px;
      line-height: 42px;
      color: #fff;
      text-align: left;
      background-color: transparent;
    }

    i {
      font-size: 22px;
      color: #e0e0e0;
      line-height: 42px;
    }
  }
}

.menu-info {
  position: absolute;
  display: flex;
  flex-flow: row nowrap;
  justify-content: flex-start;
  left: 235px;
  top: 0;
  z-index: 11;
  border: 1px solid #e0e0e0;
  box-shadow: 3px 8px 16px rgba(0, 0, 0, 0.18);
  .menu-info-list {
    width: 248px;
    height: 458px;
    margin: 0;
    padding: 0;
    list-style: none;
    background: #fff;
    a {
      display: flex;
      padding: 18px 20px;
      line-height: 40px;
      color: #333;
      img {
        width: 40px;
        height: 40px;
        margin-right: 12px;
        vertical-align: middle;
      }

      span {
        width: 172px;
        line-height: 40px;
        font-size: 14px;
        text-align: start;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
      }

      &:hover {
        span {
          color: #ff6700;
        }
      }
    }
  }
}
</style>