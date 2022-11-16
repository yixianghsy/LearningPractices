<template>
  <div class="navbar">
    <!--  default-active :　默认选中的菜单
            :router="true" true表示开启路由模式,开启之后, index值代表的就是路由地址
          -->
    <el-menu
      default-active="2"
      :router="true"
      class="el-menu-vertical-deFo"
      @open="handleOpen"
      @close="handleClose"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
    >
      <el-menu-item index="/home">
        <i class="el-icon-s-home"></i>
        <span slot="title">首页</span>
      </el-menu-item>
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-star-off"></i>
          <span>商品管理</span>
        </template>
        <el-menu-item index="/item-add">
          <i class="el-icon-star-off"></i>
          <span slot="title">新增商品</span>
        </el-menu-item>
        <el-menu-item index="/item-list">
          <i class="el-icon-star-off"></i>
          <span slot="title">查询商品</span>
        </el-menu-item>
        <el-menu-item index="/item-param-list">
          <i class="el-icon-star-off"></i>
          <span slot="title">规格参数</span>
        </el-menu-item>
      </el-submenu>
      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-star-off"></i>
          <span>网站内容管理</span>
        </template>
        <el-menu-item index="/content-category">
          <i class="el-icon-star-off"></i>
          <span slot="title">内容分类管理</span>
        </el-menu-item>
        <el-menu-item index="/content">
          <i class="el-icon-star-off"></i>
          <span slot="title">内容管理</span>
        </el-menu-item>
      </el-submenu>
      <el-submenu index="3">
        <template slot="title">
          <i class="el-icon-star-off"></i>
          <span>索引库管理</span>
        </template>
        <el-menu-item index="/index-item">
          <i class="el-icon-star-off"></i>
          <span slot="title">solr索引库维护</span>
        </el-menu-item>
      </el-submenu>
    </el-menu>
  </div>
</template>
<script>
export default {
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>
<style scoped>
.el-menu {
  border-right: none;
}
</style>