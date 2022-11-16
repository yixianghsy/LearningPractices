<template>
  <div class="navbar">
    <!--  default-active :　默认选中的菜单
            :router="true" true表示开启路由模式,开启之后, index值代表的就是路由地址
          -->
    <el-menu
      default-active="1"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
      background-color="#545c64"
      text-color="#fff"
      router
      active-text-color="#ffd04b"
    >
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-star-off"></i>
          <span>菜单一</span>
        </template>
        <el-menu-item index="/member">
          <i class="el-icon-star-off"></i>
          <span slot="title">member</span>
        </el-menu-item>
        <el-menu-item index="/supplier/">
          <i class="el-icon-star-off"></i>
          <span slot="title">supplier</span>
        </el-menu-item>
      </el-submenu>
      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-star-off"></i>
          <span>菜单二</span>
        </template>
        <el-menu-item index="/mysql">
          <i class="el-icon-star-off"></i>
          <span slot="title">MySql页面</span>
        </el-menu-item>
        <el-menu-item index="/redis">
          <i class="el-icon-star-off"></i>
          <span slot="title">Redis页面</span>
        </el-menu-item>
      </el-submenu>
      <el-submenu index="3">
        <template slot="title">
          <i class="el-icon-star-off"></i>
          <span>菜单三</span>
        </template>
        <el-menu-item index="/mysql">
          <i class="el-icon-star-off"></i>
          <span slot="title">MySql页面</span>
        </el-menu-item>
        <el-menu-item index="/redis">
          <i class="el-icon-star-off"></i>
          <span slot="title">Redis页面</span>
        </el-menu-item>
      </el-submenu>
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-star-off"></i>
          <span>菜单四</span>
        </template>
        <el-menu-item index="/mysql">
          <i class="el-icon-star-off"></i>
          <span slot="title">MySql页面</span>
        </el-menu-item>
        <el-menu-item index="/redis">
          <i class="el-icon-star-off"></i>
          <span slot="title">Redis页面</span>
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
      }
    }
}
</script>
<style scoped>
.el-menu {
  border-right: none;
}
</style>