<template>
    <div class="header">
        <a href="#/">
            <img class="logo" src="@/assets/logo.png" width="25px">
            <span class="company">梦学谷会员管理系统</span>
        </a>
        
        <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
                下拉菜单<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item icon="el-icon-edit" command="a">修改密码</el-dropdown-item>
                <el-dropdown-item icon="el-icon-s-fold" command="b">退出系统</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script>
import {logout} from '@/api/login'
export default {
    
    methods: {
        handleCommand(command) {
            
            switch (command) {
                case 'a':
                    // 修改密码
                    this.$message(`点击修改密码`)
                    break;
                case 'b':
                    // 退出系统
                    // const token = localStorage.getItem('mxg-msm-token')
                    logout( localStorage.getItem('mxg-msm-token') ).then(response => {
                        const resp = response.data
                        if(resp.flag) {
                            // 退出成功
                            // 清除本地数据
                            localStorage.removeItem('mxg-msm-token')
                            localStorage.removeItem('mxg-msm-user')
                            // 回到登录页面
                            this.$router.push('/login')
                        }else {
                            this.$message({
                                message: resp.message,
                                type: 'warning',
                                duration: 500 // 弹出停留时间
                            });
                        }
                    })
                    break;
                default:
                    break;
            }
        }
    }
}
</script>

<style scoped>
.logo{
    vertical-align: middle;
    padding: 0px 10px 0 40px;
}
.company {
    position: absolute;
    color: white;
}

/* 下拉菜单  */
.el-dropdown {
    float: right;
    margin-right: 40px;
}
.el-dropdown-link {
    color: white;
    cursor: pointer;
}
</style>