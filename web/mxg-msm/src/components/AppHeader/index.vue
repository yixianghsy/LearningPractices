<template>
  <div class="header">
    <a href="#/">
      <img class="logo" src="@/assets/logo.png" width="25px" />
      <span class="company">梦学谷会员管理系统</span>
    </a>

    <el-dropdown @command="handleCommand">
      <span class="el-dropdown-link">
        下拉菜单<i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item icon="el-icon-edit" command="a"
          >修改密码</el-dropdown-item
        >
        <el-dropdown-item icon="el-icon-s-fold" command="b"
          >退出系统</el-dropdown-item
        >
      </el-dropdown-menu>
    </el-dropdown>
    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="500px">
      <el-form
        :model="ruleForm"
        status-icon
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        style="width: 400px"
      >
        <el-form-item label="原密码" prop="oldPass">
          <el-input type="password" v-model="ruleForm.oldPass"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="pass">
          <el-input type="password" v-model="ruleForm.pass"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="ruleForm.checkPass"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')"
            >提交</el-button
          >
          <!-- <el-button >重置</el-button> -->
          <el-button @click="$refs['ruleForm'].resetFields()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { logout } from "@/api/login";
import passwordApi from "@/api/password";
export default {
  data() {
    //校验原密码
    const validateOldPass = (rule, value, callback) => {
      passwordApi.checkPwd(this.user.id,value).then(response =>{
        const resp = response.data
        this.$message(resp)
        if(resp.flag){
          callback()
        }else{
          callback(new Error(resp.message))
        }
      })
    }
    // rule 当前校验表单对象
    const validatePass = (rule,value,callback) =>{
      if(value !=this.ruleForm.pass){
        callback(new Error('两次输入密码不一致'));

      }else{
        callback()
      }
    }
    return {
      dialogFormVisible: false,
      ruleForm: {
        oldPass: "",
        pass: "",
        checkPass: "",
      },
      // user 获取用户信息
      user: JSON.parse(localStorage.getItem("mxg-msm-user")),
      rules: {
        oldPass: [
          { required: true, message: "旧密码不能为空", trigger: "blur" },
          { balidator: validateOldPass, trigger: "blur" },
        ],
        pass: [
          { required: true, message: "新密码不能为空", trigger: "blur" },
          { balidator: validateOldPass, trigger: "blur" },
        ],
        checkPass: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { balidator: validateOldPass, trigger: "blur" },
        ],
      },
    };
  },
  // 注意是在 return 上面

  methods: {
    handleCommand(command) {
      switch (command) {
        case "a":
          // 修改密码
          // this.$message(`点击修改密码`)
          this.handlePwd();
          break;
        case "b":
          // 退出系统
          this.handleLogout();
          break;
        default:
          break;
      }
    },
    //修改密码
    handlePwd() {
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["ruleForm"].resetFields();
      });
    },
    //推出系统
    handleLogout() {
      const token = localStorage.getItem("mxg-msm-token");
      logout(localStorage.getItem("mxg-msm-token")).then((response) => {
        const resp = response.data;
        if (resp.flag) {
          // 退出成功
          // 清除本地数据
          localStorage.removeItem("mxg-msm-token");
          localStorage.removeItem("mxg-msm-user");
          // 回到登录页面
          this.$router.push("/login");
        } else {
          this.$message({
            message: resp.message,
            type: "warning",
            duration: 500, // 弹出停留时间
          });
        }
      });
    },
    submitForm(formName) {
      //this.$message(`test`);
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 验证通过，提交添加
          passwordApi
            .updatePwd(this.user.id, this.ruleForm.checkPass)
            .then((response) => {
              const resp = response.data;
              this.$message({
                message: resp.message,
                type: resp.flag ? "success" : "warning",
              });
              if (resp.flag) {
                // 修改成功, 清除本地数据, 重新登录
                this.handleLogout();
                // 关闭窗口
                this.dialogFormVisible = false;
              }
            });
        } else {
          // 验证不通过
          this.$message(`校验不通过`);
          return false;
        }
      });
      // this.$message(`test`);
    },
  },
};
</script>

<style scoped>
.logo {
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