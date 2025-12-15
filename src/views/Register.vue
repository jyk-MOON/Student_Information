<template>
  <div class="register-container">
    <!-- 背景装饰 - 与登录页面保持一致 -->
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
        <h1 class="title">创建新账户</h1>
        <p class="subtitle">Join Us Today</p>
      </div>

      <!-- 表单区域 -->
      <el-form :model="form" :rules="rules" ref="formRef" class="register-form">
        <div class="form-row">
          <!-- 左侧表单 -->
          <div class="form-left">
            <!-- 用户名 -->
            <el-form-item prop="name" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-user input-icon"></i>
                <el-input 
                  v-model="form.name"
                  placeholder="请输入用户名"
                  size="large"
                  class="custom-input"
                >
                </el-input>
              </div>
            </el-form-item>

            <!-- 密码 -->
            <el-form-item prop="password" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-lock input-icon"></i>
                <el-input 
                  v-model="form.password"
                  type="password"
                  placeholder="请输入密码 (6-20位)"
                  size="large"
                  show-password
                  class="custom-input"
                >
                </el-input>
              </div>
            </el-form-item>

            <!-- 确认密码 -->
            <el-form-item prop="confirmPassword" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-lock input-icon"></i>
                <el-input 
                  v-model="form.confirmPassword"
                  type="password"
                  placeholder="请确认密码"
                  size="large"
                  show-password
                  class="custom-input"
                >
                </el-input>
              </div>
            </el-form-item>

            <!-- 邮箱 -->
            <el-form-item prop="email" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-message input-icon"></i>
                <el-input 
                  v-model="form.email"
                  placeholder="请输入邮箱"
                  size="large"
                  class="custom-input"
                >
                </el-input>
              </div>
            </el-form-item>

            <!-- 生日 -->
            <el-form-item prop="birthday" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-date input-icon"></i>
                <el-date-picker
                  v-model="form.birthday"
                  type="date"
                  placeholder="请选择生日"
                  size="large"
                  class="custom-date-picker"
                  :picker-options="pickerOptions"
                >
                </el-date-picker>
              </div>
            </el-form-item>
          </div>

          <!-- 右侧头像上传 -->
          <div class="form-right">
            <div class="avatar-section">
              <h3 class="avatar-title">上传头像</h3>
              <p class="avatar-subtitle">支持 JPG、PNG 格式，最大 2MB</p>
              
              <el-upload
                class="avatar-uploader"
                :action="$baseUrl + '/files/upload'"
                :headers="{ token: userT.token }"
                :show-file-list="false"
                :on-success="handleImgSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <div v-if="form.avatar" class="avatar-preview">
                  <img :src="form.avatar" class="avatar-image" />
                  <div class="avatar-overlay">
                    <i class="el-icon-refresh overlay-icon"></i>
                    <span class="overlay-text">更换头像</span>
                  </div>
                </div>
                <div v-else class="avatar-upload">
                  <i class="el-icon-plus avatar-upload-icon"></i>
                  <div class="upload-text">点击上传</div>
                </div>
              </el-upload>

              <div v-if="form.avatar" class="avatar-actions">
                <el-button 
                  type="text" 
                  class="action-btn"
                  @click="form.avatar = ''"
                >
                  <i class="el-icon-delete"></i> 移除
                </el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 注册按钮 -->
        <div class="register-actions">
          <el-button 
            type="primary" 
            @click="register" 
            :loading="loading"
            size="large"
            class="register-btn"
          >
            <i class="el-icon-check register-btn-icon"></i>
            {{ loading ? '注册中...' : '立即注册' }}
          </el-button>
          
          <div class="login-link">
            已有账号？
            <a href="javascript:;" class="login-btn" @click="login">
              立即登录
            </a>
          </div>
        </div>
      </el-form>

      <!-- 协议说明 -->
      <div class="agreement">
        <el-checkbox v-model="agreementChecked">
          我已阅读并同意
        </el-checkbox>
        <a href="javascript:;" class="agreement-link">《用户服务协议》</a>
        和
        <a href="javascript:;" class="agreement-link">《隐私政策》</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    // 密码验证规则
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value.length < 6 || value.length > 20) {
        callback(new Error('密码长度在 6 到 20 个字符'))
      } else {
        if (this.form.confirmPassword !== '') {
          this.$refs.formRef.validateField('confirmPassword')
        }
        callback()
      }
    }
    
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }

    const validateEmail = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入邮箱'))
      } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
        callback(new Error('请输入有效的邮箱地址'))
      } else {
        callback()
      }
    }

    return {
      form: {
        name: '',
        password: '',
        confirmPassword: '',
        email: '',
        birthday: '',
        avatar: '',
        role: 'USER'
      },
      userT: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      loading: false,
      agreementChecked: false,
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date())
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            picker.$emit('pick', date)
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', date)
          }
        }]
      },
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, validator: validatePassword, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validateConfirmPassword, trigger: 'blur' }
        ],
        email: [
          { required: true, validator: validateEmail, trigger: 'blur' }
        ],
        birthday: [
          { required: true, message: '请选择生日', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    async register() {
      if (!this.agreementChecked) {
        this.$message.warning('请先阅读并同意用户协议')
        return
      }

      try {
        const valid = await this.$refs.formRef.validate()
        if (!valid) return
        
        this.loading = true
        
        const res = await this.$request.post('/register', this.form)
        
        if (res.code === '200') {
          this.$message.success({
            message: '注册成功！即将跳转到登录页面',
            duration: 2000,
            showClose: true
          })
          
          setTimeout(() => {
            this.$router.push('/login')
          }, 1500)
        } else {
          this.$message.error({
            message: res.msg || '注册失败',
            showClose: true
          })
        }
      } catch (error) {
        console.error('注册错误:', error)
        this.$message.error({
          message: '网络错误，请稍后重试',
          showClose: true
        })
      } finally {
        this.loading = false
      }
    },
    
    login() {
      this.$router.push('/login')
    },
    
    handleImgSuccess(response, file) {
      if (response.code === '200') {
        this.form.avatar = response.data
        this.$message.success('头像上传成功')
      } else {
        this.$message.error(response.msg || '头像上传失败')
      }
    },
    
    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2
      
      if (!isImage) {
        this.$message.error('只能上传图片格式文件!')
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
      }
      
      return isImage && isLt2M
    }
  }
}
</script>

<style scoped>
/* 基础容器 - 与登录页面保持一致 */
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

/* 背景动画 - 与登录页面一致 */
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
    #6a11cb 0%, 
    #2575fc 25%, 
    #00d2ff 50%, 
    #00c9ff 75%, 
    #92fe9d 100%);
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
  max-width: 900px;
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
    #FF4102FF, 
    #FF6B6B, 
    #4ECDC4, 
    #45B7D1, 
    #96CEB4, 
    #FFEAA7);
  animation: gradientFlow 3s ease infinite;
  background-size: 200% 100%;
}

/* 卡片头部 */
.card-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  width: 70px;
  height: 70px;
  margin: 0 auto 15px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 
    0 10px 30px rgba(102, 126, 234, 0.3),
    0 0 0 8px rgba(102, 126, 234, 0.1);
}

.logo i {
  font-size: 32px;
  color: white;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 6px;
  background: linear-gradient(90deg, #667eea, #764ba2);
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

/* 表单布局 */
.form-row {
  display: flex;
  gap: 40px;
  margin-bottom: 30px;
}

.form-left {
  flex: 1;
}

.form-right {
  flex: 0 0 300px;
}

/* 表单项 */
.form-item {
  margin-bottom: 20px;
}

/* 输入框样式 - 与登录页面一致 */
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

.custom-input:deep(.el-input__inner) {
  padding-left: 48px !important;
  border-radius: 12px;
  border: 2px solid #e1e5e9;
  height: 48px;
  font-size: 15px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.9);
}

.custom-input:deep(.el-input__inner):focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
}

.custom-input:deep(.el-input__inner):focus + .input-icon {
  color: #667eea;
}

/* 日期选择器 */
.custom-date-picker:deep(.el-input__inner) {
  padding-left: 48px !important;
  border-radius: 12px;
  border: 2px solid #e1e5e9;
  height: 48px;
  font-size: 15px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.9);
}

.custom-date-picker:deep(.el-input__inner):focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
}

/* 头像上传区域 */
.avatar-section {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 16px;
  padding: 25px;
  height: 100%;
  border: 2px dashed #dee2e6;
  transition: all 0.3s ease;
}

.avatar-section:hover {
  border-color: #667eea;
  background: linear-gradient(135deg, #f0f2f5 0%, #e2e6ea 100%);
}

.avatar-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  text-align: center;
}

.avatar-subtitle {
  color: #888;
  font-size: 13px;
  text-align: center;
  margin-bottom: 25px;
}

/* 头像上传器 */
.avatar-uploader {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  cursor: pointer;
}

.avatar-upload {
  width: 160px;
  height: 160px;
  border: 3px dashed #dee2e6;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  transition: all 0.3s ease;
  background: white;
}

.avatar-upload:hover {
  border-color: #667eea;
  transform: scale(1.05);
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.2);
}

.avatar-upload-icon {
  font-size: 36px;
  color: #999;
  margin-bottom: 10px;
}

.upload-text {
  color: #666;
  font-size: 14px;
  font-weight: 500;
}

/* 头像预览 */
.avatar-preview {
  position: relative;
  width: 160px;
  height: 160px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.avatar-preview:hover {
  transform: scale(1.05);
}

.avatar-preview:hover .avatar-overlay {
  opacity: 1;
}

.avatar-image {
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
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.overlay-icon {
  font-size: 28px;
  color: white;
  margin-bottom: 8px;
}

.overlay-text {
  color: white;
  font-size: 14px;
  font-weight: 500;
}

/* 头像操作 */
.avatar-actions {
  display: flex;
  justify-content: center;
  margin-top: 15px;
}

.action-btn {
  color: #667eea;
  font-size: 14px;
  transition: all 0.3s ease;
}

.action-btn:hover {
  color: #764ba2;
  background: rgba(102, 126, 234, 0.05);
  border-radius: 6px;
}

.action-btn i {
  margin-right: 5px;
}

/* 注册按钮区域 */
.register-actions {
  text-align: center;
  margin-top: 30px;
}

.register-btn {
  width: 100%;
  max-width: 300px;
  height: 50px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 
    0 15px 30px rgba(102, 126, 234, 0.4),
    0 0 0 1px rgba(255, 255, 255, 0.1);
}

.register-btn:active {
  transform: translateY(0);
}

.register-btn-icon {
  margin-right: 8px;
  font-size: 18px;
}

.login-link {
  margin-top: 20px;
  color: #666;
  font-size: 14px;
}

.login-btn {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
  margin-left: 5px;
  transition: all 0.3s ease;
  padding: 4px 8px;
  border-radius: 6px;
}

.login-btn:hover {
  color: #764ba2;
  background: rgba(102, 126, 234, 0.05);
  text-decoration: none;
}

/* 协议说明 */
.agreement {
  margin-top: 25px;
  padding-top: 20px;
  border-top: 1px solid rgba(240, 240, 240, 0.5);
  text-align: center;
  color: #666;
  font-size: 14px;
}

.agreement-link {
  color: #667eea;
  text-decoration: none;
  transition: color 0.3s ease;
  margin: 0 2px;
}

.agreement-link:hover {
  color: #764ba2;
  text-decoration: underline;
}

:deep(.el-checkbox__label) {
  color: #666;
  font-size: 14px;
}

/* 动画效果 - 与登录页面一致 */
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
@media (max-width: 768px) {
  .register-card {
    padding: 30px 20px;
    margin: 20px;
  }
  
  .form-row {
    flex-direction: column;
    gap: 20px;
  }
  
  .form-right {
    flex: none;
    width: 100%;
  }
  
  .avatar-section {
    height: auto;
  }
  
  .register-btn {
    max-width: 100%;
  }
}

@media (max-width: 480px) {
  .card-header {
    margin-bottom: 20px;
  }
  
  .logo {
    width: 60px;
    height: 60px;
  }
  
  .logo i {
    font-size: 28px;
  }
  
  .title {
    font-size: 24px;
  }
  
  .avatar-upload {
    width: 140px;
    height: 140px;
  }
  
  .avatar-preview {
    width: 140px;
    height: 140px;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .register-card {
    background: rgba(30, 30, 40, 0.95);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .custom-input:deep(.el-input__inner),
  .custom-date-picker:deep(.el-input__inner) {
    background: rgba(40, 40, 50, 0.8);
    border-color: rgba(255, 255, 255, 0.1);
    color: #fff;
  }
  
  .title {
    background: linear-gradient(90deg, #8A2BE2, #00BFFF);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
  
  .avatar-section {
    background: linear-gradient(135deg, rgba(40, 40, 50, 0.8), rgba(50, 50, 60, 0.8));
    border-color: rgba(255, 255, 255, 0.1);
  }
  
  .avatar-upload {
    background: rgba(50, 50, 60, 0.8);
    border-color: rgba(255, 255, 255, 0.1);
  }
  
  :deep(.el-checkbox__label) {
    color: #bbb;
  }
}
</style>