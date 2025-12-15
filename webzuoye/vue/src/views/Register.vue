<template>
  <div class="register-container">
    <!-- 美化背景 -->
    <div class="bg-animation">
      <div class="gradient-bg"></div>
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
        <div class="shape shape-4"></div>
      </div>
    </div>
    
    <!-- 注册卡片 -->
    <div class="register-card">
      <!-- 卡片头部 -->
      <div class="card-header">
        <div class="logo">
          <i class="el-icon-user-solid"></i>
        </div>
        <h1 class="title">注册新用户</h1>
        <p class="subtitle">Join Our Community</p>
      </div>

      <!-- 主要内容区域 -->
      <el-form :model="form" :rules="rules" ref="formRef" class="register-form">
        <div class="main-content">
          <!-- 左侧：表单输入 -->
          <div class="form-section">
            <!-- 用户名输入 -->
            <el-form-item prop="name" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-user input-icon"></i>
                <el-input 
                  v-model="form.name"
                  placeholder="请输入用户名"
                  size="large"
                  class="custom-input"
                  :class="{ 'has-icon': true }"
                >
                </el-input>
              </div>
            </el-form-item>

            <!-- 密码输入 -->
            <el-form-item prop="password" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-lock input-icon"></i>
                <el-input 
                  v-model="form.password"
                  type="password"
                  placeholder="请输入密码"
                  size="large"
                  show-password
                  class="custom-input"
                  :class="{ 'has-icon': true }"
                >
                </el-input>
              </div>
            </el-form-item>

            <!-- 邮箱输入 -->
            <el-form-item prop="email" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-s-promotion input-icon"></i>
                <el-input 
                  v-model="form.email"
                  placeholder="请输入邮箱"
                  size="large"
                  class="custom-input"
                  :class="{ 'has-icon': true }"
                >
                </el-input>
              </div>
            </el-form-item>

            <!-- 生日选择 -->
            <el-form-item prop="birthday" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-date input-icon"></i>
                <el-date-picker
                  style="width: 100%"
                  v-model="form.birthday"
                  type="date"
                  placeholder="请选择生日"
                  size="large"
                  format="YYYY/MM/DD"
                  value-format="YYYY-MM-DD"
                  class="custom-datepicker"
                  :class="{ 'has-icon': true }"
                >
                </el-date-picker>
              </div>
            </el-form-item>
          </div>
          
          <!-- 右侧分隔线 -->
          <div class="vertical-divider">
            <div class="divider-line"></div>
          </div>

          <!-- 右侧：头像上传 -->
          <div class="upload-section">
            <div class="section-title">
              <span class="title-text">上传头像</span>
              <div class="title-line"></div>
            </div>
            
            <el-form-item prop="avatar" class="upload-item">
              <el-upload
                class="avatar-uploader"
                :action="$baseUrl + '/files/upload'"
                :headers="{ token: userT.token }"
                :show-file-list="false"
                :on-success="handleImgSuccess"
              >
                <div class="upload-area">
                  <div v-if="form.avatar" class="avatar-preview">
                    <img :src="form.avatar" class="avatar-img" />
                    <div class="avatar-overlay">
                      <i class="el-icon-camera camera-icon"></i>
                      <span class="change-text">更换头像</span>
                    </div>
                  </div>
                  <div v-else class="upload-placeholder">
                    <i class="el-icon-plus avatar-uploader-icon"></i>
                    <div class="upload-text">点击上传头像</div>
                    <div class="upload-hint">支持 JPG、PNG 格式</div>
                  </div>
                </div>
              </el-upload>
              <div class="upload-tips" v-if="!form.avatar">
                建议尺寸：200×200px，大小不超过 2MB
              </div>
            </el-form-item>
          </div>
        </div>

        <!-- 底部按钮 -->
        <div class="bottom-section">
          <el-button 
            class="bottom-btn register-btn"
            @click="register"
          >
            <i class="el-icon-user"></i>
            注册用户
          </el-button>
          
          <el-button 
            class="bottom-btn return-btn"
            @click="login"
          >
            <i class="el-icon-back"></i>
            返回登录
          </el-button>
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
          { required: true, message: '请上传头像', trigger: 'blur' },
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
/* 基础容器 */
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  position: relative;
  overflow: hidden;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

/* 美化背景动画 */
.bg-animation {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 0;
}

.gradient-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, 
    #FF6B6B 0%, 
    #FFD166 25%, 
    #06D6A0 50%, 
    #118AB2 75%, 
    #073B4C 100%);
  background-size: 400% 400%;
  animation: gradientMove 15s ease infinite;
}

.floating-shapes {
  position: absolute;
  width: 100%;
  height: 100%;
}

.shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  animation: float 20s infinite ease-in-out;
}

.shape-1 {
  width: 300px;
  height: 300px;
  top: 10%;
  left: 5%;
  animation-delay: 0s;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.15), transparent 70%);
}

.shape-2 {
  width: 200px;
  height: 200px;
  bottom: 15%;
  right: 10%;
  animation-delay: 5s;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1), transparent 70%);
}

.shape-3 {
  width: 150px;
  height: 150px;
  top: 60%;
  left: 15%;
  animation-delay: 10s;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.08), transparent 70%);
}

.shape-4 {
  width: 250px;
  height: 250px;
  bottom: 20%;
  left: 70%;
  animation-delay: 15s;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.12), transparent 70%);
}

/* 注册卡片 */
.register-card {
  width: 100%;
  max-width: 1000px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 40px;
  box-shadow: 
    0 20px 60px rgba(0, 0, 0, 0.15),
    0 0 0 1px rgba(255, 255, 255, 0.1);
  z-index: 1;
  animation: slideUp 0.8s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.register-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, 
    #FF6B6B, 
    #FFD166, 
    #06D6A0, 
    #118AB2, 
    #073B4C);
  animation: gradientFlow 3s ease infinite;
  background-size: 200% 100%;
}

/* 卡片头部 */
.card-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  background: linear-gradient(135deg, #FF6B6B 0%, #FFD166 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 
    0 10px 30px rgba(255, 107, 107, 0.3),
    0 0 0 8px rgba(255, 107, 107, 0.1);
  transition: transform 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.logo i {
  font-size: 36px;
  color: white;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
  background: linear-gradient(90deg, #FF6B6B, #FFD166);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: 1px;
}

.subtitle {
  color: #888;
  font-size: 14px;
  margin: 0;
  letter-spacing: 2px;
  text-transform: uppercase;
  font-weight: 400;
}

/* 主要内容区域 */
.main-content {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 30px;
}

.form-section {
  flex: 1;
  padding-right: 40px;
}

.form-item {
  margin-bottom: 24px;
}

/* 输入框图标样式 */
.input-with-icon {
  position: relative;
}

.input-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
  font-size: 18px;
  z-index: 2;
  transition: color 0.3s ease;
}

.custom-input.has-icon:deep(.el-input__inner) {
  padding-left: 48px !important;
  border-radius: 12px;
  border: 2px solid #e1e5e9;
  padding: 0 48px 0 48px;
  height: 50px;
  font-size: 15px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.9);
}

.custom-input.has-icon:deep(.el-input__inner):focus {
  border-color: #FF6B6B;
  box-shadow: 0 0 0 3px rgba(255, 107, 107, 0.15);
}

.custom-input.has-icon:deep(.el-input__inner):focus + .input-icon {
  color: #FF6B6B;
}

/* 日期选择器样式 */
.custom-datepicker.has-icon:deep(.el-input__inner) {
  padding-left: 48px !important;
  border-radius: 12px;
  border: 2px solid #e1e5e9;
  padding: 0 48px 0 48px;
  height: 50px;
  font-size: 15px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.9);
}

.custom-datepicker.has-icon:deep(.el-input__inner):focus {
  border-color: #FF6B6B;
  box-shadow: 0 0 0 3px rgba(255, 107, 107, 0.15);
}

/* 垂直分隔线 */
.vertical-divider {
  position: relative;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 40px;
}

.divider-line {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 50%;
  width: 1px;
  background: linear-gradient(to bottom, 
    transparent, 
    #e1e5e9 20%, 
    #e1e5e9 80%, 
    transparent);
  transform: translateX(-50%);
}

.divider-text {
  position: relative;
  background: white;
  padding: 8px 16px;
  color: #999;
  font-size: 14px;
  font-weight: 500;
  border-radius: 20px;
  border: 1px solid #e1e5e9;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

/* 上传区域 */
.upload-section {
  flex: 1;
  padding-left: 40px;
}

.section-title {
  margin-bottom: 25px;
  position: relative;
}

.title-text {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  background: linear-gradient(90deg, #118AB2, #06D6A0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  padding-right: 15px;
  position: relative;
  z-index: 1;
  display: inline-block;
}

.title-line {
  position: absolute;
  bottom: -8px;
  left: 0;
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #118AB2, #06D6A0);
  border-radius: 3px;
}

.upload-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-uploader {
  width: 220px;
  height: 220px;
}

.upload-area {
  width: 100%;
  height: 100%;
  border: 3px dashed #d9d9d9;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.8);
  overflow: hidden;
  position: relative;
}

.upload-area:hover {
  border-color: #06D6A0;
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(6, 214, 160, 0.2);
}

.avatar-preview {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
  border-radius: 18px;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  border-radius: 18px;
}

.avatar-preview:hover .avatar-overlay {
  opacity: 1;
}

.camera-icon {
  font-size: 32px;
  color: white;
  margin-bottom: 8px;
}

.change-text {
  color: white;
  font-size: 14px;
  font-weight: 500;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.avatar-uploader-icon {
  font-size: 48px;
  color: #118AB2;
  margin-bottom: 15px;
  transition: transform 0.3s ease;
}

.upload-area:hover .avatar-uploader-icon {
  transform: scale(1.1);
  color: #06D6A0;
}

.upload-text {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.upload-hint {
  font-size: 12px;
  color: #999;
  text-align: center;
}

.upload-tips {
  margin-top: 15px;
  font-size: 12px;
  color: #999;
  text-align: center;
  max-width: 220px;
}

/* 底部按钮区域 */
.bottom-section {
  display: flex;
  gap: 20px;
  margin-top: 30px;
  padding-top: 30px;
  border-top: 1px solid rgba(240, 240, 240, 0.5);
}

.bottom-btn {
  flex: 1;
  height: 50px;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
  border: 2px solid transparent;
  transition: all 0.3s ease;
}

.bottom-btn i {
  margin-right: 8px;
  font-size: 16px;
}

.register-btn {
  background: linear-gradient(135deg, #06D6A0 0%, #118AB2 100%);
  color: white;
  border: none;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(6, 214, 160, 0.3);
}

.return-btn {
  background: linear-gradient(135deg, #FF6B6B 0%, #FFD166 100%);
  color: white;
  border: none;
}

.return-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(255, 107, 107, 0.3);
}

/* 动画效果 */
@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(40px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes gradientMove {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  33% {
    transform: translateY(-20px) rotate(120deg);
  }
  66% {
    transform: translateY(10px) rotate(240deg);
  }
}

@keyframes gradientFlow {
  0% {
    background-position: 0% 0%;
  }
  100% {
    background-position: 200% 0%;
  }
}

/* 响应式设计 */
@media (max-width: 992px) {
  .main-content {
    flex-direction: column;
    gap: 40px;
  }
  
  .form-section {
    padding-right: 0;
    width: 100%;
  }
  
  .vertical-divider {
    display: none;
  }
  
  .upload-section {
    padding-left: 0;
    width: 100%;
  }
  
  .register-card {
    max-width: 500px;
    padding: 30px;
  }
}

@media (max-width: 480px) {
  .register-card {
    padding: 25px 20px;
    margin: 20px;
    border-radius: 20px;
  }
  
  .logo {
    width: 70px;
    height: 70px;
  }
  
  .logo i {
    font-size: 30px;
  }
  
  .title {
    font-size: 24px;
  }
  
  .bottom-section {
    flex-direction: column;
    gap: 15px;
  }
  
  .avatar-uploader {
    width: 180px;
    height: 180px;
  }
  
  .avatar-uploader-icon {
    font-size: 40px;
  }
  
  .upload-text {
    font-size: 14px;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .register-card {
    background: rgba(30, 30, 40, 0.95);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .custom-input.has-icon:deep(.el-input__inner),
  .custom-datepicker.has-icon:deep(.el-input__inner) {
    background: rgba(40, 40, 50, 0.8);
    border-color: rgba(255, 255, 255, 0.1);
    color: #fff;
  }
  
  .title {
    background: linear-gradient(90deg, #FF6B6B, #FFD166);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
  
  .upload-area {
    background: rgba(40, 40, 50, 0.8);
    border-color: rgba(255, 255, 255, 0.1);
  }
  
  .upload-text {
    color: #ddd;
  }
  
  .divider-text {
    background: rgba(30, 30, 40, 0.95);
    border-color: rgba(255, 255, 255, 0.1);
    color: #aaa;
  }
}
</style>