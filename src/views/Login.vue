<template>
  <div class="login-container">
    <!-- 美化背景 - 渐变背景 + 动态粒子 -->
    <div class="bg-animation">
      <div class="gradient-bg"></div>
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
        <div class="shape shape-4"></div>
      </div>
    </div>
    
    <!-- 登录卡片 -->
    <div class="login-card">
      <!-- 卡片头部 -->
      <div class="card-header">
        <div class="logo">
          <i class="el-icon-user-solid"></i>
        </div>
        <h1 class="title">用户登录</h1>
        <p class="subtitle">Welcome Back</p>
      </div>

      <!-- 表单区域 -->
      <el-form :model="form" :rules="rules" ref="formRef" class="login-form">
        <!-- 账号输入 - 修复图标与文字重叠问题 -->
        <el-form-item prop="name">
          <div class="input-with-icon">
            <i class="el-icon-user input-icon"></i>
            <el-input 
              v-model="form.name"
              placeholder="请输入账号/手机号/邮箱"
              size="large"
              class="custom-input"
              :class="{ 'has-icon': true }"
            >
            </el-input>
          </div>
        </el-form-item>

        <!-- 密码输入 - 修复图标与文字重叠问题 -->
        <el-form-item prop="password">
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
              @keyup.enter="login"
            >
            </el-input>
          </div>
        </el-form-item>

        <!-- 记住我和忘记密码 -->
        <div class="form-options">
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          <a href="javascript:;" class="forgot-password" @click="passwordChange">
            忘记密码？
          </a>
        </div>

        <!-- 登录按钮 -->
        <el-form-item>
          <el-button 
            type="primary" 
            @click="login" 
            :loading="loading"
            size="large"
            class="login-btn"
          >
            <i class="el-icon-s-promotion login-btn-icon"></i>
            {{ loading ? '登录中...' : '立即登录' }}
          </el-button>
        </el-form-item>

        <!-- 第三方登录 - 添加对应软件图标 -->
        <div class="social-login">
          <div class="divider">
            <span class="divider-text">快捷登录</span>
          </div>
          
          <div class="social-buttons">
            <!-- QQ登录 -->
            <el-button 
              circle 
              class="social-btn qq-btn"
              @click="socialLogin('qq')"
              title="QQ登录"
            >
              <div class="social-icon-wrapper">
                <!-- QQ图标 - 可以使用自定义SVG或图片 -->
                <svg class="social-icon qq-icon" viewBox="0 0 1024 1024">
                  <path d="M824.8 613.2c-16-51.4-34.4-94.6-62.7-165.3C766.5 262.2 689.3 112 511.5 112S256.5 262.2 261.8 447.9c-28.4 70.8-46.7 114-62.7 165.3-34 109.5-23 154.8-14.6 155.8 18 2 54.9-26.7 79.4-63.8 7.5-9.3 15.2-20.4 22.3-33 24.9 12.1 52.7 18.9 81.6 18.9 28.9 0 56.6-6.8 81.5-18.9 7.2 12.6 14.8 23.7 22.3 33 24.5 37.1 61.4 65.8 79.4 63.8 8.5-1 19.5-46.3-14.5-155.8z" fill="#04B4FF"/>
                </svg>
              </div>
            </el-button>
            
            <!-- 微信登录 -->
            <el-button 
              circle 
              class="social-btn wechat-btn"
              @click="socialLogin('wechat')"
              title="微信登录"
            >
              <div class="social-icon-wrapper">
                <!-- 微信图标 -->
                <svg class="social-icon wechat-icon" viewBox="0 0 1024 1024">
                  <path d="M648.3 349.2c-18.1 0-32.8 14.6-32.8 32.8 0 18.1 14.6 32.8 32.8 32.8 18.1 0 32.8-14.6 32.8-32.8 0-18.1-14.7-32.8-32.8-32.8zM376.2 349.2c-18.1 0-32.8 14.6-32.8 32.8 0 18.1 14.6 32.8 32.8 32.8 18.1 0 32.8-14.6 32.8-32.8 0-18.2-14.7-32.8-32.8-32.8z" fill="#09BB07"/>
                  <path d="M512.2 67.1c-246.5 0-446.4 174.1-446.4 388.9 0 136.9 69.8 259 178.5 337.9l-47.5 150.7c-1.8 5.7 0.5 11.9 5.6 15.4 2.1 1.5 4.6 2.2 7.1 2.2 3.3 0 6.5-1.2 9-3.4l127.3-105.7c40.3 13.6 82.9 20.8 127.4 20.8 246.5 0 446.4-174.1 446.4-388.9S758.6 67.1 512.2 67.1z m-177 494.5c-29.5 0-53.4-21.9-53.4-48.9 0-27 23.9-48.9 53.4-48.9s53.4 21.9 53.4 48.9c0 27-23.9 48.9-53.4 48.9z m353.9 0c-29.5 0-53.4-21.9-53.4-48.9 0-27 23.9-48.9 53.4-48.9s53.4 21.9 53.4 48.9c0 27-23.9 48.9-53.4 48.9z" fill="#09BB07"/>
                </svg>
              </div>
            </el-button>
            
            <!-- 支付宝登录 -->
            <el-button 
              circle 
              class="social-btn alipay-btn"
              @click="socialLogin('alipay')"
              title="支付宝登录"
            >
              <div class="social-icon-wrapper">
                <!-- 支付宝图标 -->
                <svg class="social-icon alipay-icon" viewBox="0 0 1024 1024">
                  <path d="M789 610.6c-38.6-12.4-89.9-29.5-89.9-29.5 10.2-55.1 35.3-179.1 35.3-179.1-89.6 33.5-155.5 102.1-155.5 102.1 52.6-17.5 97.4-26.8 141.7-26.8 33 0 64.2 5.6 94.4 17.9-29.6-95.8-100.9-172.7-189.3-215.4-95.4-46.2-217-40.3-310.6 20.1-88.4 57-143.3 156.6-143.3 269.3 0 49.6 13.7 98.1 39.7 140.3 0 0-30.1 10.1-70.8 23.5 18.4 29.8 67.6 102.9 184.2 102.9 0 0 19.4-1.7 45.5-8.9 72.1-20 116.9-69.6 116.9-69.6s13.6 26.9 38.9 53.5c35.2 37.4 110.2 85.1 196.2 67.2 81.5-16.9 135.6-72.2 155.9-136.7 17.1-54.2 11.2-108.8-8.4-153.8z m-478.9-88.6c-13.1-4.6-27.5-7.2-42.5-7.2-14.5 0-28.5 2.4-41.3 6.9 1.5-51.1 21.7-97.8 56.8-133.3 36.1-36.4 84-56.5 134.7-56.5 24.7 0 48.8 4.7 71.5 14 44.7 18.2 79.4 52.7 98.1 97.4-19.5 7.9-41.7 15.8-66.8 23.5-74.5-55.9-167.5-51.8-210.5-44.8z" fill="#1677FF"/>
                </svg>
              </div>
            </el-button>
          </div>
        </div>

        <!-- 注册链接 -->
        <div class="register-link">
          还没有账号？
          <a href="javascript:;" class="register-btn" @click="register">
            立即注册
          </a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: { 
        name: '',
        password: '',
        role: 'USER'
      },
      rules: {
        name: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 3, max: 20, message: '账号长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ]
      },
      rememberMe: false,
      loading: false
    }
  },
  created() {
    // 如果有记住的账号，自动填充
    const savedName = localStorage.getItem('savedUsername')
    if (savedName) {
      this.form.name = savedName
      this.rememberMe = true
    }
  },
  methods: {
    async login() {
      try {
        const valid = await this.$refs.formRef.validate()
        if (!valid) return
        
        this.loading = true
        
        // 如果需要记住账号
        if (this.rememberMe) {
          localStorage.setItem('savedUsername', this.form.name)
        } else {
          localStorage.removeItem('savedUsername')
        }
        
        const res = await this.$request.post('/login', this.form)
        
        if (res.code === '200') {
          localStorage.setItem("xm-user", JSON.stringify(res.data))
          
          this.$message.success({
            message: '登录成功',
            duration: 1500,
            showClose: true
          })
          
          setTimeout(() => {
            this.$router.push('/home')
          }, 800)
        } else {
          this.$message.error({
            message: res.msg || '登录失败',
            showClose: true
          })
        }
      } catch (error) {
        console.error('登录错误:', error)
        this.$message.error({
          message: '网络错误，请稍后重试',
          showClose: true
        })
      } finally {
        this.loading = false
      }
    },
    
    register() {
      this.$router.push('/register')
    },
    
    passwordChange() {
      this.$router.push('/passwordChange')
    },
    
    socialLogin(type) {
      const platformName = type === 'qq' ? 'QQ' : type === 'wechat' ? '微信' : '支付宝'
      this.$message.info(`${platformName}登录功能开发中`)
      // 这里可以调用第三方登录接口
    }
  }
}
</script>

<style scoped>
/* 基础容器 */
.login-container {
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

/* 登录卡片 */
.login-card {
  width: 100%;
  max-width: 420px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 40px 36px;
  box-shadow: 
    0 20px 60px rgba(0, 0, 0, 0.15),
    0 0 0 1px rgba(255, 255, 255, 0.1);
  z-index: 1;
  animation: slideUp 0.8s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.login-card::before {
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
  margin-bottom: 36px;
}

.logo {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 
    0 10px 30px rgba(102, 126, 234, 0.3),
    0 0 0 8px rgba(102, 126, 234, 0.1);
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

/* 修复输入框图标重叠问题 */
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
  height: 48px;
  font-size: 15px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.9);
}

.custom-input.has-icon:deep(.el-input__inner):focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
}

.custom-input.has-icon:deep(.el-input__inner):focus + .input-icon {
  color: #667eea;
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.forgot-password {
  color: #667eea;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
  padding: 4px 8px;
  border-radius: 6px;
}

.forgot-password:hover {
  color: #764ba2;
  background: rgba(102, 126, 234, 0.05);
  text-decoration: none;
}

/* 登录按钮 */
.login-btn {
  width: 100%;
  height: 50px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
  margin-top: 10px;
  position: relative;
  overflow: hidden;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 
    0 15px 30px rgba(102, 126, 234, 0.4),
    0 0 0 1px rgba(255, 255, 255, 0.1);
}

.login-btn:active {
  transform: translateY(0);
}

.login-btn-icon {
  margin-right: 8px;
  font-size: 18px;
}

/* 第三方登录图标优化 */
.social-login {
  margin-top: 30px;
}

.divider {
  display: flex;
  align-items: center;
  margin: 24px 0;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, #e1e5e9, transparent);
}

.divider-text {
  padding: 0 16px;
  color: #999;
  font-size: 14px;
  white-space: nowrap;
  font-weight: 500;
}

.social-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.social-btn {
  width: 56px !important;
  height: 56px !important;
  border: 2px solid rgba(225, 229, 233, 0.8) !important;
  background: rgba(255, 255, 255, 0.9) !important;
  transition: all 0.3s ease !important;
  backdrop-filter: blur(10px);
  padding: 0 !important;
}

.social-btn:hover {
  transform: translateY(-5px) scale(1.1);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
}

.social-icon-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.social-icon {
  width: 28px;
  height: 28px;
  transition: transform 0.3s ease;
}

.qq-btn:hover {
  border-color: #04B4FF !important;
  background: linear-gradient(135deg, #04B4FF, #00A8FF) !important;
}

.qq-btn:hover .qq-icon {
  filter: brightness(0) invert(1);
}

.wechat-btn:hover {
  border-color: #09BB07 !important;
  background: linear-gradient(135deg, #09BB07, #5CFF57) !important;
}

.wechat-btn:hover .wechat-icon {
  filter: brightness(0) invert(1);
}

.alipay-btn:hover {
  border-color: #1677FF !important;
  background: linear-gradient(135deg, #1677FF, #4096FF) !important;
}

.alipay-btn:hover .alipay-icon {
  filter: brightness(0) invert(1);
}

/* 注册链接 */
.register-link {
  text-align: center;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid rgba(240, 240, 240, 0.5);
  color: #666;
  font-size: 14px;
  font-weight: 400;
}

.register-btn {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
  margin-left: 5px;
  transition: all 0.3s ease;
  padding: 4px 8px;
  border-radius: 6px;
}

.register-btn:hover {
  color: #764ba2;
  background: rgba(102, 126, 234, 0.05);
  text-decoration: none;
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
@media (max-width: 480px) {
  .login-card {
    padding: 30px 24px;
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
  
  .social-buttons {
    gap: 15px;
  }
  
  .social-btn {
    width: 50px !important;
    height: 50px !important;
  }
  
  .social-icon {
    width: 24px;
    height: 24px;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .login-card {
    background: rgba(30, 30, 40, 0.95);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .custom-input.has-icon:deep(.el-input__inner) {
    background: rgba(40, 40, 50, 0.8);
    border-color: rgba(255, 255, 255, 0.1);
    color: #fff;
  }
  
  .title {
    background: linear-gradient(90deg, #8A2BE2, #00BFFF);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
  
  .social-btn {
    background: rgba(40, 40, 50, 0.8) !important;
    border-color: rgba(255, 255, 255, 0.1) !important;
  }
}
</style>