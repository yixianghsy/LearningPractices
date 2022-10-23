<template>
    <div class="navbar">
    <!-- :router 开启路由功能,则 index指定路由地址
    default-active 默认选中哪个菜单,选中为黄色,
    注意 v-bind:default-active 才可以指定表达式 -->
    <el-menu
        :router="true" :default-active="$route.path" class="el-menu-vertical-demo" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
            <el-menu-item index="/home">
                <i class="el-icon-s-home"></i>
                <span slot="title">首页</span>
            </el-menu-item>
            <el-menu-item index="/member/">
                <i class="el-icon-user-solid"></i>
                <span slot="title">会员管理</span>
            </el-menu-item>
            <el-menu-item index="/supplier/">
                <i class="el-icon-s-cooperation"></i>
                <span slot="title">供应商管理</span>
            </el-menu-item>
            <el-menu-item index="/goods/">
                <i class="el-icon-s-goods"></i>
                <span slot="title">商品管理</span>
             </el-menu-item>
             <el-menu-item index="/staff/">
                <i class="el-icon-user"></i>
                <span slot="title">员工管理</span>
             </el-menu-item>  
    </el-menu>
    </div>
</template>     
<script>
   export default {
    } 
</script>
<style scoped>
    .el-menu {
    border-right: none;
    } 
</style>