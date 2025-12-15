<template>
  <div class="container">
    <div style="width: 400px; padding: 30px; background-color: white; border-radius: 5px;">
      <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #333">修改密码</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <div class="top">
          <div class="top-left">
            <el-form-item prop="name">
              <el-input style="width: 100%" prefix-icon="el-icon-user" placeholder="用户名" v-model="form.name"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input style="width: 100%" prefix-icon="el-icon-lock" placeholder="旧密码" show-password  v-model="form.password"></el-input>
            </el-form-item>
          </div>
          <div class="right">
            <el-form-item prop="newPassword">
              <el-input style="width: 100%" prefix-icon="el-icon-lock" placeholder="新密码" show-password v-model="form.newPassword"></el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input style="width: 100%" prefix-icon="el-icon-lock" placeholder="确认密码" show-password  v-model="form.confirmPassword"></el-input>
            </el-form-item>
          </div>
        </div>
        <div class="bottom">
          <div class="bottom-left">
            <el-form-item>
              <el-button style="width: 100%; background-color: #333; border-color: #333; color: white" @click="update">修改密码</el-button>
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
  name: "PasswordChange",
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.form.newPassword) {
        callback(new Error('确认密码错误'))
      } else {
        callback()
      }
    }

    return {
      userT: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      form: { role: 'USERT'},
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入原始密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      }
    }
  },
  created() {

  },
  methods: {
    update() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          this.$request.put('/updatePassword', this.form).then(res => {
            if (res.code === '200') {
              // 成功更新
              this.$message.success('修改密码成功')
              this.$router.push('/login')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    login(){
      this.$router.push('/login')
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
  .top-left{border-right-style:none }
}

</style>