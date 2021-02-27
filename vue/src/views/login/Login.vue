<template>
  <div>
    <el-form ref="form" :rules="rules" :model="loginForm" class="loginContainer">
      <h3 class="loginTitle">系统登录</h3>
      <el-form-item prop="userName">
        <el-input type="text" v-model="loginForm.userName" auto-complete="false" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" auto-complete="false" placeholder="请输入密码" />
      </el-form-item>
      <el-form-item prop="code">
        <el-input type="text" v-model="loginForm.code" auto-complete="false" placeholder="请输入验证码" style="width: 250px;margin-right: 5px" />
        <img :src="captchaUrl" alt="">
      </el-form-item>
      <el-checkbox v-model="checked" class="loginRememberMe">记住我</el-checkbox>
      <el-button type="primary" style="width: 100%" @click="loginSubmit">登录</el-button>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      captchaUrl: "",
      checked: false,
      loginForm: {
        userName: "",
        password: "",
        code: ""
      },
      rules: {
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min: 4, max: 4, message: '长度为 4 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    loginSubmit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          alert('submit!');
        } else {
          this.$message.error('请按照格式要求，输入相应字段！');
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
  .loginContainer{
    border-radius: 15px;
    background-clip: padding-box;
    margin: 188px auto;
    width: 350px;
    padding: 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .loginTitle{
    margin: 0 auto 40px auto;
    text-align: center;
  }
  .loginRememberMe{
    text-align: left;
    margin: 0 0 15px 0;
  }
</style>