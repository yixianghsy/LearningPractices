<template>
  <div class="good-box">
    <div class="box-header">
      <h2 class="box-title">{{goodsItem.boxTitle}}</h2>
      <div class="type-select" v-if="goodsItem.isHot">
        <ul class="tab-list">
          <li class="tab-item"
          v-for="(item, index) in goodsItem.tabList"
          :key="index"
          :class="{'active': item.type === selectItem.type}"
          @mouseenter="selectType(item)">{{item.value}}</li>
        </ul>
      </div>
      <div class="type-more" v-else>
        <a :href="goodsItem.url" target="_blank">
          <span>查看全部</span>
          <i class="fa fa-angle-right"></i>
        </a>
      </div>
    </div>
    <div class="box-content">
       
      <div class="goods-content">
        <div class="goods-one" v-if="!goodsItem.isHot">
          <div class="goods-item brick-item"
          v-for="(item, index) in goodsItem.listData.goods"
          :key="index">
            <a :href="item.url" target="_blank">
              <img :src="item.src" :alt="item.value">
              <h3 class="name ellipsis">{{item.value}}</h3>
              <span class="desc ellipsis">{{item.desc}}</span>
              <div class="price">
                <span>{{item.newPrice}}元</span>
                <span v-if="item.sub">起</span>
                <del v-if="item.oldPrice">{{item.oldPrice}}元</del>
              </div>
            </a>
          </div>
        </div>
        <div class="goods-two" v-else>
          <template v-for="(item, index) in goodsData">
            <div class="goods-item-sm" :key="index" v-if="index === goodsData.length - 1 && goodsData.length % 2 === 0">
              <a class="brick-item" :href="item.url" target="_blank">
                <div class="text-container">
                  <h3 class="name">{{item.value}}</h3>
                  <div class="price">
                    <span>{{item.newPrice}}元</span>
                    <span v-if="item.sub">起</span>
                  </div>
                </div>
                <img :src="item.src" :alt="item.value">
              </a>
              <a class="brick-item" :href="selectItem.url" target="_blank">
                <div class="text-container look-more">
                  <h3 class="name">浏览更多</h3>
                  <span class="desc">{{selectItem.value}}</span>
                </div>
                <img src="/assets/image/right.png">
              </a>
            </div>
            <div class="goods-item-bg" :key="index" v-if="index === goodsData.length - 1 && goodsData.length % 2 !== 0">
              <a class="brick-item" :href="item.url" target="_blank">
                <img :src="item.src" :alt="item.value">
                <h3 class="name ellipsis">{{item.value}}</h3>
                <span class="desc ellipsis">{{item.desc}}</span>
                <div class="price">
                  <span>{{item.newPrice}}元</span>
                  <span v-if="item.sub">起</span>
                  <del v-if="item.oldPrice">{{item.oldPrice}}元</del>
                </div>
              </a>
              <a class="brick-more brick-item" :href="selectItem.url" target="_blank">
                <div class="text-container look-more">
                  <h3 class="name">浏览更多</h3>
                  <span class="desc">{{selectItem.value}}</span>
                </div>
                <img src="/assets/image/right.png">
              </a>
            </div>
            <div class="goods-item brick-item" :key="index" v-if="index < goodsData.length - 1">
              <a :href="item.url" target="_blank">
                <img :src="item.src" :alt="item.value">
                <h3 class="name ellipsis">{{item.value}}</h3>
                <span class="desc ellipsis">{{item.desc}}</span>
                <div class="price">
                  <span>{{item.newPrice}}元</span>
                  <span v-if="item.sub">起</span>
                  <del v-if="item.oldPrice">{{item.oldPrice}}元</del>
                </div>
              </a>
            </div>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      selectItem: '',
      goodsData: ''
    }
  },
  props: ['goodsItem'],
  methods: {
    selectType (item) {
      this.selectItem = item;
      this.goodsData = this.goodsItem.listData[item.type];
    },
    init () {
      if (this.goodsItem.tabList) {
        this.selectItem = this.goodsItem.tabList[0];
        this.goodsData = this.goodsItem.listData.hots;
      } else {
        this.selectItem = '';
        this.goodsData = '';
      }
    }
  },
  mounted() {
    this.init();
  }
}
</script>

<style lang="less">
  .good-box {
    position: relative;
    width: 1226px;
    height: auto;
    margin: 0 auto;
  }

  .box-header {
    .type-select {
      display: inline-block;
      .tab-list {
        display: flex;
        .tab-item {
          display: inline-block;
          margin-left: 30px;
          color: #424242;
          border-bottom: 2px solid transparent;
          transition: all .3s;
          cursor: pointer;
        }

        .active {
          border-bottom: 2px solid #ff6709;
          color: #ff6709;
        }
      }
    }

    .type-more {
      display: inline-block;
      a {
        font-size: 16px;
        display: flex;
        align-items: center;
        span {
          color: #424242;
          transition: all .4s;
        }

        i {
          width: 20px;
          height: 20px;
          margin-left: 8px;
          border-radius: 16px;
          font-size: 15px;
          line-height: 20px;
          background: #b0b0b0;
          color: #fff;
          text-align: center;
          vertical-align: 2px;
          transition: all .4s;
        }

        &:hover {
          span {
            color: #ff6709;
          }

          i {
            background: #ff6709;
          }
        }
      }
    }
  }

  .box-content {
    display: flex; 

    .promo-two {
      a {
        display: block;
        height: 300px;
        img {
          width: 234px;
          height: 100%;
        }
      }

      a:first-child {
        margin-bottom: 14px;
      }
    }

    .goods-content .goods-one,
    .goods-content .goods-two {
      width: 1240px;
      display: flex;
      flex-wrap: wrap;
      .goods-item {
        background: #fff;
        height: 300px;
        padding: 20px 0;
        width: 234px;
        margin-left: 14px;
        margin-bottom: 14px;
      }
    }

    .goods-two .goods-item-sm {
      height: 300px;
      width: 234px;
      margin-left: 14px;
      a {
        display: flex;
        justify-content: space-between;
        height: 143px;
        background: #fff;
        padding: 30px 20px 30px 30px;
        margin-bottom: 14px;
        img {
          width: 80px;
          height: 80px;
          margin: 0;
        }
      }
    }

    .goods-two .goods-item-bg {
      height: 300px;
      width: 496px;
      display: flex;
      a {
        display: block;
        width: 234px;
        background: #fff;
        margin-left: 14px;
        padding: 20px 0;
      }

      .brick-more {
        display: flex;
        justify-content: space-between;
        height: 143px;
        padding: 30px 20px 30px 30px;
        img {
          width: 80px;
          height: 80px;
          margin: 0;
        }
      }
    }

    .goods-content {
      a {
        display: block;
        img {
          width: 160px;
          height: 160px;
          margin: 0 auto 18px;
        }

        .name {
          font-size: 14px;
          font-weight: 400;
          color: #333;
          margin: 0 10px 2px;
        }

        .desc {
          display: block;
          margin: 0 10px 10px;
          height: 18px;
          font-size: 12px;
          color: #b0b0b0;
        }

        .price {
          margin: 0 10px 14px;
          font-size: 14px;
          color: #ff6700;
          del {
            color: #b0b0b0;
            margin-left: 8px;
          }
        }
      }
    }
  }

  .brick-item {
    transition: all .2s linear;
    &:hover {
      transform: translate3d(0, -2px, 0);
      box-shadow: 0 15px 30px rgba(0, 0, 0, .1);
    }
  }

  .goods-two .goods-item-sm,
  .goods-two .goods-item-bg {
    .look-more {
      padding-top: 0 !important;
      display: flex;
      flex-direction: column;
      align-self: center;
      .name {
        font-size: 18px;
        color: #333;
        margin: 0;
      }

      .desc {
        font-size: 12px;
        color: #757575;
        margin: 0;
      }
    }

    .text-container {
      width: 94px;
      padding-top: 10px;
      text-align: left;
      .name {
        margin: 0 0 5px;
      }

      .price {
        margin: 0;
      }
    }
  }
</style>

<style lang="less">
  .flash-sale {
    position: relative;
    width: 1226px;
    height: auto;
    padding-top: 4px;
    margin: 0 auto;
  }

  .box-header {
    text-align: start;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .box-title {
      display: inline-block;
      font-size: 22px;
      font-weight: 200;
      line-height: 58px;
      color: #333;
    }
  }

  .box-sale {
    display: flex;
    .sale-count-down {
      display: inline-block;
      width: 234px;
      height: 340px;
      padding-top: 39px;
      border-top: 1px solid #e53935;
      background: #f1eded;
      text-align: center;
      .sale-round {
        font-size: 21px;
        color: #ef3a3b;
        padding-top: 15px;
      }

      .time-left {
        color: rgba(0,0,0,.54);
        font-size: 15px
      }

      img {
        margin: 25px auto;
      }

      .count-down {
        width: 168px;
        margin: 28px auto 0;
        display: flex;
        span {
          display: block;
          width: 46px;
          height: 46px;
          background: #605751;
          color: #fff;
          font-size: 24px;
          font-weight: 600;
          line-height: 46px;
          text-align: center;
        }

        i {
          display: block;
          width: 15px;
          height: 46px;
          line-height: 46px;
          color: #605751;
          font-size: 28px;
          font-style: normal;
          text-align: center;
        }
      }
    }
  }

  .slide-control {
    display: flex;
    border: 1px solid #e0e0e0;
    .slide-button {
      padding: 0 4px;
      font-size: 20px;
      font-weight: 800;
      line-height: 20px;
      color: #b0b0b0;
      text-align: center;
      transition: color .5s;
      cursor: pointer;
      &:hover {
        color: #ff6700;
      }

      i {
        width: 26px;
        height: 18px;
      }
    }

    .divider {
      width: 1px;
      height: 21px;
      background: #e0e0e0;
    }
  }
</style>