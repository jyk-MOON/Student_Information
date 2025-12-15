<template>
  <div class="container">
    <div style="wnameth: 400px; padding: 30px; background-color: white; border-radius: 5px;">
      <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #333">注册新用户</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <div class="top">
          <div class="top-left">
            <el-form-item prop="name">
              <el-input style="width: 100%" prefix-icon="el-icon-user" placeholder="用户名" v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input style="width: 100%" prefix-icon="el-icon-lock" placeholder="密码" show-password  v-model="form.password"></el-input>
            </el-form-item>
            <el-form-item prop="email">
              <el-input style="width: 100%" prefix-icon="el-icon-s-promotion" placeholder="邮箱" v-model="form.email"></el-input>
            </el-form-item>
            <el-form-item prop="birthday">
              <el-date-picker style="width: 100%"prefix-icon="el-icon-date" placeholder="年/月/日" show-password  v-model="form.birthday"></el-date-picker>
            </el-form-item>
          </div>
          <div class="right">
            <el-form-item >
              <el-upload
                  class="img-uploader"
                  :action="$baseUrl + '/files/upload'"
                  :headers="{ token: userT.token }"
                  list-type="picture"
                  :on-success="handleImgSuccess"
              >
                <i class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
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
              <el-button style="width: 100%; background-color: #333; border-color: #333; color: white" @click="login">返回登录</el-button>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    // 验证码校验
    const validatePassword = (rule, confirmPass, callback) => {
      if (confirmPass === '') {
        callback(new Error('请确认密码'))
      } else if (confirmPass !== this.form.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      form: {role: 'USERT'},
      userT: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
        ],
        birthday: [
          { required: true, message: '请输入生日', trigger: 'blur' },
        ],
        avatar: [
          { required: true, img: '请输入头像', trigger: 'blur' },
        ],
      }
    }
  },
  created() {

  },
  methods: {
    register() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/register', this.form).then(res => {
            if (res.code === '200') {
              this.$router.push('/login')  // 跳转登录页面
              this.$message.success('注册成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    login(){
      this.$router.push('/login')
    },
    handleImgSuccess(response, file, fileList) {
      this.form.avatar = response.data
    },
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
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border-color: black;
  border-style: solid;
  margin:40px 50px 50px 50px;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}
@media screen and (max-width: 800px) {
  .top{display: inline}
  .bottom{display: inline}
  .top-left{border-right-style:none }
}
</style>