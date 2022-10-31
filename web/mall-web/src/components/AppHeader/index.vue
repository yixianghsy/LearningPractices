<template>
    <div class="header">
        <a href="#/">
            <img class="logo" src="@/assets/logo.png" width="25px"/>
            <span class="company">后台管理系统</span>
        </a>
        <!--头部右侧下拉菜单-->
        <el-dropdown  @command="handleCommand" >
            <span class="el-dropdown-link" >
                admin <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="a">修改密码</el-dropdown-item>
                <el-dropdown-item command="b">退出系统</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>

    </div>
</template>
<script>
export default{

}
</script>
<style scped>
/* 下拉菜单 */
.el-dropdown {
    float: right;
    margin-right: 40px;
}
.el-dropdown-link {
    cursor: pointer;
    color: #fff;
}
.logo {
    vertical-align: middle;
    padding: 0 10px 0 40px;
}
.company {
    position: absolute;
    color: wheat;
}
</style>