<template>
  <div class="container">
    <div style="width: 400px; padding: 30px; background-color: white; border-radius: 5px">
      <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #333" >登录</div>
      <el-form :model="form" :rules="rules" ref="formRef" >
        <div class="top">
          <div class="top-left">
            <el-form-item>
              <el-button style="width: 100%; background-color: #FF4102FF; border-color: #FF4102FF; color: white" >QQ登录</el-button>
            </el-form-item>
            <el-form-item>
              <el-button style="width: 100%; background-color: #02FFAEFF; border-color: #02FFAEFF; color: white" >微信登录</el-button>
            </el-form-item>
            <el-form-item>
              <el-button style="width: 100%; background-color: #00A6FFFF; border-color: #00A6FFFF; color: white" >支付宝登录</el-button>
            </el-form-item>
          </div>
          <div class="right">
            <el-form-item prop="name">
              <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button style="width: 100%; background-color: #333; border-color: #333; color: white" @click="login">登 录</el-button>
            </el-form-item>
          </div>
        </div>
        <div class="bottom">
          <div class="bottom-left">
            <el-form-item>
              <el-button style="width: 100%; background-color: #333; border-color: #333; color: white" @click="register">注册用户</el-button>
            </el-form-item>
          </div>
          <div class="right">
            <el-form-item>
              <el-button style="width: 100%; background-color: #333; border-color: #333; color: white" @click="passwordChange">修改密码</el-button>
            </el-form-item>
          </div>
        </div>
        <!--        <div style="display: flex; align-items: center">-->
        <!--          <div style="flex: 1"></div>-->
        <!--          <div style="flex: 1; text-align: right">-->
        <!--            还没有账号？请 <a href="/register">注册</a>-->
        <!--          </div>-->
        <!--        </div>-->
      </el-form>
    </div>
  </div>
</template>

<script>
import password from "@/views/PasswordChange.vue";

export default {
  name: "Login",
  computed: {
    password() {
      return password
    }
  },
  data() {
    return {
      form: { role: 'USERT' },
      rules: {
        name: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      }
    }
  },
  created() {

  },
  methods: {
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              this.$router.push('/home')  // 跳转主页
              this.$message.success('登录成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    register() {
      this.$router.push('/register')
    },
    passwordChange(){
      this.$router.push('/passwordChange')
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg3.jpeg");
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}
a {
  color: #2a60c9;
}
.top{
  display: flex;
}
.bottom{
  display: flex;
}
.top-left{
  padding: 15px;
  flex:5;
  border-color: darkgray;
  border-right-style: solid;
}
.bottom-left{
  padding: 15px;
  flex:5;
}
.right{
  padding: 15px;
  flex:5;
}
@media screen and (max-width: 800px) {
  .top{display: inline}
  .bottom{display: inline}
}
</style>