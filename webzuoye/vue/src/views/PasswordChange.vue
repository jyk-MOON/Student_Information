<template>
  <div class="password-container">
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
    
    <!-- 修改密码卡片 -->
    <div class="password-card">
      <!-- 卡片头部 -->
      <div class="card-header">
        <div class="logo">
          <i class="el-icon-key"></i>
        </div>
        <h1 class="title">修改密码</h1>
        <p class="subtitle">Secure Your Account</p>
      </div>

      <!-- 主要内容区域 -->
      <el-form :model="form" :rules="rules" ref="formRef" class="password-form">
        <div class="main-content">
          <!-- 左侧：用户名和旧密码 -->
          <div class="left-section">
            <div class="section-title">
              <span class="title-text">身份验证</span>
              <div class="title-line"></div>
            </div>
            
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

            <!-- 旧密码输入 -->
            <el-form-item prop="password" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-lock input-icon"></i>
                <el-input 
                  v-model="form.password"
                  type="password"
                  placeholder="请输入旧密码"
                  size="large"
                  show-password
                  class="custom-input"
                  :class="{ 'has-icon': true }"
                >
                </el-input>
              </div>
            </el-form-item>
            
            <!-- 安全提示 -->
            <div class="security-tips">
              <div class="security-header">
                <i class="el-icon-warning-outline"></i>
                <span class="security-title">安全提示</span>
              </div>
              <div class="security-content">
                <p>• 请确保在安全环境下操作</p>
                <p>• 新密码应包含字母、数字和特殊字符</p>
                <p>• 定期更换密码可提高账户安全性</p>
              </div>
            </div>
          </div>
          
          <!-- 中间分隔线 -->
          <div class="vertical-divider">
            <div class="divider-line"></div>
          </div>

          <!-- 右侧：新密码设置 -->
          <div class="right-section">
            <div class="section-title">
              <span class="title-text">新密码设置</span>
              <div class="title-line"></div>
            </div>
            
            <!-- 新密码输入 -->
            <el-form-item prop="newPassword" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-lock input-icon"></i>
                <el-input 
                  v-model="form.newPassword"
                  type="password"
                  placeholder="请输入新密码"
                  size="large"
                  show-password
                  class="custom-input"
                  :class="{ 'has-icon': true }"
                >
                </el-input>
              </div>
            </el-form-item>

            <!-- 确认密码输入 -->
            <el-form-item prop="confirmPassword" class="form-item">
              <div class="input-with-icon">
                <i class="el-icon-lock input-icon"></i>
                <el-input 
                  v-model="form.confirmPassword"
                  type="password"
                  placeholder="请确认新密码"
                  size="large"
                  show-password
                  class="custom-input"
                  :class="{ 'has-icon': true }"
                >
                </el-input>
              </div>
            </el-form-item>
            
            <!-- 密码强度指示器 -->
            <div class="password-strength">
              <div class="strength-header">
                <span class="strength-title">密码强度</span>
                <span class="strength-value" :class="getPasswordStrengthClass">
                  {{ getPasswordStrengthText }}
                </span>
              </div>
              <div class="strength-bar">
                <div 
                  class="strength-fill" 
                  :class="getPasswordStrengthClass"
                  :style="{ width: getPasswordStrengthWidth }"
                ></div>
              </div>
              <div class="strength-criteria">
                <span>至少8个字符，包含大小写字母和数字</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 底部按钮 -->
        <div class="bottom-section">
          <el-button 
            class="bottom-btn update-btn"
            @click="update"
          >
            <i class="el-icon-refresh-right"></i>
            修改密码
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
          { min: 6, message: '密码长度至少6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      }
    }
  },
  computed: {
    // 计算密码强度
    getPasswordStrength() {
      const password = this.form.newPassword || ''
      if (password.length === 0) return 0
      
      let strength = 0
      
      // 长度检查
      if (password.length >= 8) strength += 20
      if (password.length >= 12) strength += 10
      
      // 字符类型检查
      if (/[a-z]/.test(password)) strength += 20
      if (/[A-Z]/.test(password)) strength += 20
      if (/[0-9]/.test(password)) strength += 20
      if (/[^a-zA-Z0-9]/.test(password)) strength += 20
      
      return Math.min(strength, 100)
    },
    
    getPasswordStrengthText() {
      const strength = this.getPasswordStrength
      if (strength === 0) return '未输入'
      if (strength < 40) return '弱'
      if (strength < 70) return '中等'
      return '强'
    },
    
    getPasswordStrengthClass() {
      const strength = this.getPasswordStrength
      if (strength === 0) return 'strength-weak'
      if (strength < 40) return 'strength-weak'
      if (strength < 70) return 'strength-medium'
      return 'strength-strong'
    },
    
    getPasswordStrengthWidth() {
      return `${this.getPasswordStrength}%`
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
/* 基础容器 */
.password-container {
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
    #6A11CB 0%, 
    #2575FC 25%, 
    #4ECDC4 50%, 
    #45B7D1 75%, 
    #96CEB4 100%);
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

/* 修改密码卡片 */
.password-card {
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

.password-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, 
    #6A11CB, 
    #2575FC, 
    #4ECDC4, 
    #45B7D1, 
    #96CEB4);
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
  background: linear-gradient(135deg, #6A11CB 0%, #2575FC 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 
    0 10px 30px rgba(106, 17, 203, 0.3),
    0 0 0 8px rgba(106, 17, 203, 0.1);
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
  background: linear-gradient(90deg, #6A11CB, #2575FC);
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

.left-section,
.right-section {
  flex: 1;
}

.left-section {
  padding-right: 40px;
}

.right-section {
  padding-left: 40px;
}

.section-title {
  margin-bottom: 30px;
  position: relative;
}

.title-text {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  background: linear-gradient(90deg, #6A11CB, #2575FC);
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
  background: linear-gradient(90deg, #6A11CB, #2575FC);
  border-radius: 3px;
}

/* 输入框样式 */
.form-item {
  margin-bottom: 24px;
}

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
  border-color: #2575FC;
  box-shadow: 0 0 0 3px rgba(37, 117, 252, 0.15);
}

.custom-input.has-icon:deep(.el-input__inner):focus + .input-icon {
  color: #2575FC;
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

/* 安全提示 */
.security-tips {
  margin-top: 40px;
  padding: 20px;
  background: linear-gradient(135deg, rgba(106, 17, 203, 0.05), rgba(37, 117, 252, 0.05));
  border-radius: 12px;
  border: 1px solid rgba(106, 17, 203, 0.1);
}

.security-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.security-header i {
  font-size: 20px;
  color: #FF6B6B;
}

.security-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.security-content {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
}

.security-content p {
  margin: 8px 0;
}

/* 密码强度指示器 */
.password-strength {
  margin-top: 40px;
  padding: 20px;
  background: linear-gradient(135deg, rgba(78, 205, 196, 0.05), rgba(96, 206, 180, 0.05));
  border-radius: 12px;
  border: 1px solid rgba(78, 205, 196, 0.1);
}

.strength-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.strength-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.strength-value {
  font-size: 14px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 12px;
}

.strength-weak {
  color: #FF6B6B;
  background: rgba(255, 107, 107, 0.1);
}

.strength-medium {
  color: #FFD166;
  background: rgba(255, 209, 102, 0.1);
}

.strength-strong {
  color: #06D6A0;
  background: rgba(6, 214, 160, 0.1);
}

.strength-bar {
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 10px;
}

.strength-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.5s ease, background 0.5s ease;
}

.strength-weak.strength-fill {
  background: linear-gradient(90deg, #FF6B6B, #FF8E8E);
}

.strength-medium.strength-fill {
  background: linear-gradient(90deg, #FFD166, #FFE29E);
}

.strength-strong.strength-fill {
  background: linear-gradient(90deg, #06D6A0, #4ECDC4);
}

.strength-criteria {
  font-size: 12px;
  color: #999;
  text-align: center;
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

.update-btn {
  background: linear-gradient(135deg, #2575FC 0%, #6A11CB 100%);
  color: white;
  border: none;
}

.update-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(37, 117, 252, 0.3);
}

.return-btn {
  background: linear-gradient(135deg, #4ECDC4 0%, #45B7D1 100%);
  color: white;
  border: none;
}

.return-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(78, 205, 196, 0.3);
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
  
  .left-section {
    padding-right: 0;
    width: 100%;
  }
  
  .vertical-divider {
    display: none;
  }
  
  .right-section {
    padding-left: 0;
    width: 100%;
  }
  
  .password-card {
    max-width: 500px;
    padding: 30px;
  }
}

@media (max-width: 480px) {
  .password-card {
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
  
  .security-tips,
  .password-strength {
    padding: 15px;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .password-card {
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
  
  .security-tips {
    background: linear-gradient(135deg, rgba(138, 43, 226, 0.1), rgba(0, 191, 255, 0.1));
    border-color: rgba(255, 255, 255, 0.1);
  }
  
  .password-strength {
    background: linear-gradient(135deg, rgba(0, 191, 255, 0.1), rgba(78, 205, 196, 0.1));
    border-color: rgba(255, 255, 255, 0.1);
  }
  
  .divider-text {
    background: rgba(30, 30, 40, 0.95);
    border-color: rgba(255, 255, 255, 0.1);
    color: #aaa;
  }
  
  .security-title,
  .strength-title {
    color: #ddd;
  }
  
  .security-content {
    color: #bbb;
  }
}
</style>