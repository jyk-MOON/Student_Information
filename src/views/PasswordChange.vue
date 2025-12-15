<template>
  <div class="password-change-container">
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
    
    <!-- 密码修改卡片 -->
    <div class="password-card">
      <!-- 卡片头部 -->
      <div class="card-header">
        <div class="logo">
          <i class="el-icon-lock"></i>
        </div>
        <h1 class="title">重置密码</h1>
        <p class="subtitle">Secure Your Account</p>
      </div>

      <!-- 安全提示 -->
      <div class="security-tips">
        <div class="tip-item">
          <i class="el-icon-info tip-icon"></i>
          <span class="tip-text">为了您的账户安全，请定期更换密码</span>
        </div>
      </div>

      <!-- 表单区域 -->
      <el-form :model="form" :rules="rules" ref="formRef" class="password-form">
        <!-- 用户名 -->
        <el-form-item prop="name" class="form-item">
          <div class="input-with-icon">
            <i class="el-icon-user input-icon"></i>
            <el-input 
              v-model="form.name"
              placeholder="请输入用户名"
              size="large"
              class="custom-input"
              @keyup.enter="focusNext('password')"
            >
            </el-input>
          </div>
        </el-form-item>

        <!-- 旧密码 -->
        <el-form-item prop="password" class="form-item">
          <div class="input-with-icon">
            <i class="el-icon-lock input-icon"></i>
            <el-input 
              v-model="form.password"
              type="password"
              placeholder="请输入旧密码"
              size="large"
              show-password
              ref="passwordInput"
              class="custom-input"
              @keyup.enter="focusNext('newPassword')"
            >
            </el-input>
          </div>
        </el-form-item>

        <!-- 新密码 -->
        <el-form-item prop="newPassword" class="form-item">
          <div class="input-with-icon">
            <i class="el-icon-key input-icon"></i>
            <el-input 
              v-model="form.newPassword"
              type="password"
              placeholder="请输入新密码 (6-20位)"
              size="large"
              show-password
              ref="newPasswordInput"
              class="custom-input"
              @keyup.enter="focusNext('confirmPassword')"
            >
            </el-input>
          </div>
          <!-- 密码强度指示器 -->
          <div v-if="form.newPassword" class="password-strength">
            <div class="strength-bar" :class="getPasswordStrength.class"></div>
            <span class="strength-text">{{ getPasswordStrength.text }}</span>
          </div>
        </el-form-item>

        <!-- 确认密码 -->
        <el-form-item prop="confirmPassword" class="form-item">
          <div class="input-with-icon">
            <i class="el-icon-check input-icon"></i>
            <el-input 
              v-model="form.confirmPassword"
              type="password"
              placeholder="请确认新密码"
              size="large"
              show-password
              ref="confirmPasswordInput"
              class="custom-input"
              @keyup.enter="update"
            >
            </el-input>
          </div>
        </el-form-item>

        <!-- 密码要求提示 -->
        <div class="password-requirements">
          <h4 class="requirements-title">密码要求：</h4>
          <ul class="requirements-list">
            <li :class="{ 'requirement-met': form.newPassword && form.newPassword.length >= 6 }">
              <i class="el-icon-check requirement-icon"></i>
              至少6个字符
            </li>
            <li :class="{ 'requirement-met': form.newPassword && form.newPassword.length <= 20 }">
              <i class="el-icon-check requirement-icon"></i>
              最多20个字符
            </li>
            <li :class="{ 'requirement-met': form.newPassword && /[A-Z]/.test(form.newPassword) }">
              <i class="el-icon-check requirement-icon"></i>
              包含大写字母
            </li>
            <li :class="{ 'requirement-met': form.newPassword && /[0-9]/.test(form.newPassword) }">
              <i class="el-icon-check requirement-icon"></i>
              包含数字
            </li>
            <li :class="{ 'requirement-met': form.newPassword && /[^A-Za-z0-9]/.test(form.newPassword) }">
              <i class="el-icon-check requirement-icon"></i>
              包含特殊字符
            </li>
          </ul>
        </div>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <el-button 
            type="primary" 
            @click="update" 
            :loading="loading"
            size="large"
            class="update-btn"
          >
            <i class="el-icon-refresh update-btn-icon"></i>
            {{ loading ? '修改中...' : '确认修改' }}
          </el-button>
          
          <el-button 
            @click="login" 
            size="large"
            class="back-btn"
          >
            <i class="el-icon-back back-btn-icon"></i>
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
    // 密码强度检查
    const checkPasswordStrength = (password) => {
      if (!password) return { score: 0, class: '', text: '' }
      
      let score = 0
      if (password.length >= 6) score++
      if (password.length >= 8) score++
      if (/[A-Z]/.test(password)) score++
      if (/[0-9]/.test(password)) score++
      if (/[^A-Za-z0-9]/.test(password)) score++
      
      if (score >= 4) {
        return { score, class: 'strong', text: '强' }
      } else if (score >= 3) {
        return { score, class: 'medium', text: '中' }
      } else {
        return { score, class: 'weak', text: '弱' }
      }
    }
    
    // 密码验证规则
    const validateNewPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else if (value.length < 6 || value.length > 20) {
        callback(new Error('密码长度在6-20个字符之间'))
      } else if (value === this.form.password) {
        callback(new Error('新密码不能与旧密码相同'))
      } else {
        callback()
      }
    }
    
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认新密码'))
      } else if (value !== this.form.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }

    return {
      form: {
        name: '',
        password: '',
        newPassword: '',
        confirmPassword: '',
        role: 'USER'
      },
      userT: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      loading: false,
      passwordStrength: { score: 0, class: '', text: '' },
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在3-20个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入旧密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, validator: validateNewPassword, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    getPasswordStrength() {
      if (!this.form.newPassword) {
        return { class: '', text: '' }
      }
      
      let score = 0
      if (this.form.newPassword.length >= 6) score++
      if (this.form.newPassword.length >= 8) score++
      if (/[A-Z]/.test(this.form.newPassword)) score++
      if (/[0-9]/.test(this.form.newPassword)) score++
      if (/[^A-Za-z0-9]/.test(this.form.newPassword)) score++
      
      if (score >= 4) {
        return { class: 'strong', text: '密码强度：强' }
      } else if (score >= 3) {
        return { class: 'medium', text: '密码强度：中' }
      } else {
        return { class: 'weak', text: '密码强度：弱' }
      }
    }
  },
  created() {
    // 如果已登录，自动填充用户名
    if (this.userT && this.userT.name) {
      this.form.name = this.userT.name
    }
  },
  methods: {
    focusNext(field) {
      const nextInput = this.$refs[`${field}Input`]
      if (nextInput && nextInput.focus) {
        nextInput.focus()
      }
    },
    
    async update() {
      try {
        const valid = await this.$refs.formRef.validate()
        if (!valid) return
        
        this.loading = true
        
        // 密码强度检查提醒
        const strength = this.getPasswordStrength
        if (strength.class === 'weak') {
          const confirm = await this.$confirm(
            '您设置的密码强度较弱，建议包含大写字母、数字和特殊字符以提高安全性，是否继续？',
            '密码强度提醒',
            {
              confirmButtonText: '继续修改',
              cancelButtonText: '重新设置',
              type: 'warning'
            }
          ).catch(() => false)
          
          if (!confirm) {
            this.loading = false
            return
          }
        }
        
        const res = await this.$request.put('/updatePassword', this.form)
        
        if (res.code === '200') {
          this.$message.success({
            message: '密码修改成功！请使用新密码重新登录',
            duration: 2000,
            showClose: true
          })
          
          // 清空本地存储的用户信息
          localStorage.removeItem('xm-user')
          
          // 延迟跳转到登录页面
          setTimeout(() => {
            this.$router.push('/login')
          }, 1500)
        } else {
          this.$message.error({
            message: res.msg || '密码修改失败',
            showClose: true
          })
        }
      } catch (error) {
        console.error('密码修改错误:', error)
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
    }
  }
}
</script>

<style scoped>
/* 基础容器 - 与登录页面保持一致 */
.password-change-container {
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

/* 密码修改卡片 */
.password-card {
  width: 100%;
  max-width: 480px;
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
    #FF6B6B, 
    #4ECDC4, 
    #45B7D1, 
    #96CEB4);
  animation: gradientFlow 3s ease infinite;
  background-size: 200% 100%;
}

/* 卡片头部 */
.card-header {
  text-align: center;
  margin-bottom: 20px;
}

.logo {
  width: 70px;
  height: 70px;
  margin: 0 auto 15px;
  background: linear-gradient(135deg, #FF6B6B 0%, #4ECDC4 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 
    0 10px 30px rgba(255, 107, 107, 0.3),
    0 0 0 8px rgba(255, 107, 107, 0.1);
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
  background: linear-gradient(90deg, #FF6B6B, #4ECDC4);
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

/* 安全提示 */
.security-tips {
  background: linear-gradient(135deg, #FFF5F5 0%, #E6F7FF 100%);
  border-radius: 12px;
  padding: 12px 16px;
  margin-bottom: 20px;
  border-left: 4px solid #FF6B6B;
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.tip-icon {
  color: #FF6B6B;
  font-size: 18px;
}

.tip-text {
  color: #666;
  font-size: 14px;
  font-weight: 500;
}

/* 表单样式 */
.password-form {
  margin-top: 20px;
}

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
  border-color: #4ECDC4;
  box-shadow: 0 0 0 3px rgba(78, 205, 196, 0.15);
}

.custom-input:deep(.el-input__inner):focus + .input-icon {
  color: #4ECDC4;
}

/* 密码强度指示器 */
.password-strength {
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.strength-bar {
  flex: 1;
  height: 6px;
  border-radius: 3px;
  background: #e1e5e9;
  overflow: hidden;
  position: relative;
}

.strength-bar::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 0;
  border-radius: 3px;
  transition: all 0.3s ease;
}

.strength-bar.weak::after {
  width: 33%;
  background: #FF6B6B;
}

.strength-bar.medium::after {
  width: 66%;
  background: #FFD166;
}

.strength-bar.strong::after {
  width: 100%;
  background: #4ECDC4;
}

.strength-text {
  font-size: 12px;
  color: #888;
  min-width: 80px;
}

/* 密码要求提示 */
.password-requirements {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 16px;
  margin: 20px 0;
  border: 1px solid #e1e5e9;
}

.requirements-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.requirements-title::before {
  content: '🔐';
  font-size: 16px;
}

.requirements-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
}

@media (max-width: 480px) {
  .requirements-list {
    grid-template-columns: 1fr;
  }
}

.requirements-list li {
  font-size: 12px;
  color: #888;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.3s ease;
}

.requirements-list li.requirement-met {
  color: #4ECDC4;
}

.requirement-icon {
  font-size: 12px;
  transition: all 0.3s ease;
}

.requirements-list li.requirement-met .requirement-icon {
  color: #4ECDC4;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 16px;
  margin-top: 30px;
}

.update-btn {
  flex: 1;
  height: 50px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #FF6B6B 0%, #4ECDC4 100%);
  border: none;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.update-btn:hover {
  transform: translateY(-2px);
  box-shadow: 
    0 15px 30px rgba(255, 107, 107, 0.3),
    0 0 0 1px rgba(255, 255, 255, 0.1);
}

.update-btn:active {
  transform: translateY(0);
}

.update-btn-icon {
  margin-right: 8px;
  font-size: 18px;
}

.back-btn {
  flex: 1;
  height: 50px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 500;
  background: rgba(255, 255, 255, 0.9);
  border: 2px solid #e1e5e9;
  color: #666;
  transition: all 0.3s ease;
}

.back-btn:hover {
  border-color: #4ECDC4;
  color: #4ECDC4;
  background: rgba(78, 205, 196, 0.05);
  transform: translateY(-2px);
}

.back-btn:active {
  transform: translateY(0);
}

.back-btn-icon {
  margin-right: 8px;
}

/* 安全建议 */
.security-suggestions {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid rgba(240, 240, 240, 0.5);
}

.suggestions-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.suggestions-title::before {
  content: '⚠️';
  font-size: 16px;
}

.suggestion-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  margin-bottom: 10px;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.suggestion-item:hover {
  background: rgba(255, 107, 107, 0.05);
}

.suggestion-icon {
  color: #FF6B6B;
  font-size: 16px;
  margin-top: 2px;
  flex-shrink: 0;
}

.suggestion-item span {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
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
  .password-card {
    padding: 30px 24px;
    margin: 20px;
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
  
  .action-buttons {
    flex-direction: column;
    gap: 12px;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .password-card {
    background: rgba(30, 30, 40, 0.95);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .custom-input:deep(.el-input__inner) {
    background: rgba(40, 40, 50, 0.8);
    border-color: rgba(255, 255, 255, 0.1);
    color: #fff;
  }
  
  .title {
    background: linear-gradient(90deg, #FF6B6B, #4ECDC4);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
  
  .security-tips {
    background: rgba(255, 107, 107, 0.1);
    border-left-color: #FF6B6B;
  }
  
  .tip-text {
    color: #bbb;
  }
  
  .password-requirements {
    background: rgba(40, 40, 50, 0.8);
    border-color: rgba(255, 255, 255, 0.1);
  }
  
  .requirements-title {
    color: #eee;
  }
  
  .requirements-list li {
    color: #aaa;
  }
  
  .back-btn {
    background: rgba(40, 40, 50, 0.8);
    border-color: rgba(255, 255, 255, 0.1);
    color: #bbb;
  }
  
  .suggestion-item span {
    color: #aaa;
  }
}
</style>