<template>
    <div class="header"> header</div>
</template>
<script>
import {logout} from '@/api/login'
export default {
    methods: {
        handleCommand(command) {
            switch (command) {
                case 'a':
            // 修改密码
                break;
            case 'b':
                // 退出系统
                logout(localStorage.getItem(localStorage.getItem('mgx-msmtoken'))).then(response => {
            // 清除本地数据
            localStorage.removeItem('mgx-msm-token')
            localStorage.removeItem('mgx-msm-user')
            this.$router.push('/login')
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
 </style>