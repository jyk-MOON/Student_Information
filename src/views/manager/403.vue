<template>
  <div class="forbidden-page">
    <!-- 背景装饰 -->
    <div class="forbidden-bg-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="shape shape-4"></div>
    </div>

    <!-- 主内容区域 -->
    <div class="forbidden-container">
      <!-- 错误代码展示 -->
      <div class="forbidden-code-display">
        <span class="digit digit-1">4</span>
        <span class="digit digit-2">0</span>
        <span class="digit digit-3">3</span>
        <div class="forbidden-icon">
          <i class="el-icon-lock"></i>
        </div>
      </div>

      <!-- 错误信息 -->
      <div class="forbidden-content">
        <h1 class="forbidden-title">访问被拒绝</h1>
        <p class="forbidden-description">
          抱歉，您没有权限访问此页面。这可能是由于以下原因：
        </p>
        
        <!-- 原因分析 -->
        <div class="reason-analysis">
          <div class="reason-item">
            <div class="reason-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="reason-text">
              <h4>权限不足</h4>
              <p>您的账户权限不足以访问此资源</p>
            </div>
          </div>
          
          <div class="reason-item">
            <div class="reason-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="reason-text">
              <h4>访问限制</h4>
              <p>此页面设置了访问限制，需要特定权限</p>
            </div>
          </div>
          
          <div class="reason-item">
            <div class="reason-icon">
              <i class="el-icon-time"></i>
            </div>
            <div class="reason-text">
              <h4>会话过期</h4>
              <p>您的登录会话可能已过期，请重新登录</p>
            </div>
          </div>
        </div>

        <!-- 用户权限信息 -->
        <div class="permission-info">
          <div class="permission-card">
            <h3 class="permission-title">
              <i class="el-icon-user"></i> 当前账户信息
            </h3>
            <div class="user-info-details">
              <div class="info-row">
                <span class="info-label">用户名：</span>
                <span class="info-value">{{ currentUser.name || '未登录' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">角 色：</span>
                <span class="info-value">{{ currentUser.role || '普通用户' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">权限等级：</span>
                <span class="info-value">{{ permissionLevel }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <el-button 
            type="primary" 
            icon="el-icon-house" 
            class="home-btn"
            @click="goHome">
            返回首页
          </el-button>
          
          <el-button 
            icon="el-icon-back" 
            class="back-btn"
            @click="goBack">
            返回上一页
          </el-button>
          
          <el-button 
            type="warning" 
            icon="el-icon-refresh" 
            class="refresh-btn"
            @click="refreshPage">
            刷新权限
          </el-button>
          
          <el-button 
            v-if="!isLoggedIn"
            type="success" 
            icon="el-icon-key" 
            class="login-btn"
            @click="goToLogin">
            重新登录
          </el-button>
        </div>

        <!-- 权限申请 -->
        <div class="permission-request" v-if="isLoggedIn">
          <h3 class="request-title">
            <i class="el-icon-s-opportunity"></i> 需要访问权限？
          </h3>
          <p class="request-description">
            如果您认为应该有权限访问此页面，可以申请权限提升
          </p>
          <div class="request-actions">
            <el-button 
              type="primary" 
              icon="el-icon-s-promotion"
              @click="requestPermission">
              申请权限
            </el-button>
            <el-button 
              type="text" 
              icon="el-icon-chat-line-round"
              @click="contactAdmin">
              联系管理员
            </el-button>
          </div>
        </div>

        <!-- 快速导航 -->
        <div class="quick-navigation">
          <h3 class="navigation-title">
            <i class="el-icon-guide"></i> 您可以访问的页面
          </h3>
          <div class="navigation-links">
            <div 
              v-for="page in accessiblePages" 
              :key="page.path"
              class="nav-link"
              @click="goToPage(page.path)">
              <div class="link-icon" :style="{backgroundColor: page.color}">
                <i :class="page.icon"></i>
              </div>
              <div class="link-info">
                <span class="link-title">{{ page.title }}</span>
                <span class="link-desc">{{ page.desc }}</span>
              </div>
              <i class="el-icon-arrow-right link-arrow"></i>
            </div>
          </div>
        </div>

        <!-- 帮助信息 -->
        <div class="help-section">
          <div class="help-item">
            <i class="el-icon-question"></i>
            <div class="help-content">
              <h4>如何获取访问权限？</h4>
              <p>联系系统管理员或具有更高权限的用户为您分配权限</p>
            </div>
          </div>
          <div class="help-item">
            <i class="el-icon-info"></i>
            <div class="help-content">
              <h4>权限错误报告</h4>
              <p>如果您发现权限设置错误，请及时报告</p>
            </div>
          </div>
        </div>

        <!-- 系统信息 -->
        <div class="system-info">
          <p>错误代码：403 Forbidden</p>
          <p>错误ID：{{ errorId }}</p>
          <p>时间：{{ currentTime }}</p>
        </div>
      </div>

      <!-- 插画区域 -->
      <div class="forbidden-illustration">
        <div class="illustration-wrapper">
          <div class="illustration">
            <!-- 锁的SVG插图 -->
            <svg class="illustration-svg" viewBox="0 0 400 300" fill="none">
              <!-- 锁体 -->
              <rect x="150" y="100" width="100" height="120" rx="10" fill="#FFE8E8" stroke="#F56C6C" stroke-width="3"/>
              <!-- 锁孔 -->
              <circle cx="200" cy="140" r="10" fill="#F56C6C"/>
              <!-- 锁杆 -->
              <rect x="130" y="100" width="140" height="20" rx="10" fill="#F0F0F0" stroke="#909399" stroke-width="2"/>
              <!-- 钥匙孔 -->
              <rect x="195" y="150" width="10" height="40" rx="5" fill="#909399"/>
              <!-- 禁止图标 -->
              <circle cx="320" cy="180" r="40" fill="#F56C6C" opacity="0.8"/>
              <line x1="290" y1="150" x2="350" y2="210" stroke="white" stroke-width="8"/>
            </svg>
          </div>
          <p class="illustration-text">您被阻止访问此内容</p>
        </div>
      </div>
    </div>

    <!-- 页面底部 -->
    <footer class="forbidden-footer">
      <div class="footer-content">
        <p>© {{ currentYear }} 项目名称 · 权限控制系统</p>
        <div class="footer-links">
          <a href="#" @click.prevent="viewSecurityGuide">安全指南</a>
          <span class="separator">·</span>
          <a href="#" @click.prevent="viewPrivacyPolicy">隐私政策</a>
          <span class="separator">·</span>
          <a href="#" @click.prevent="clearBrowserCache">清除缓存</a>
        </div>
        <div class="security-level">
          <span class="security-badge">
            <i class="el-icon-lock"></i> 当前安全等级：{{ securityLevel }}
          </span>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: "Forbidden",
  
  data() {
    return {
      currentUser: {},
      errorId: this.generateErrorId(),
      currentTime: '',
      currentYear: new Date().getFullYear(),
      securityLevel: '中等',
      permissionLevel: '标准用户',
      accessiblePages: [
        {
          path: '/home',
          icon: 'el-icon-house',
          title: '控制台',
          desc: '返回主控制面板',
          color: '#409EFF'
        },
        {
          path: '/profile',
          icon: 'el-icon-user',
          title: '个人中心',
          desc: '查看和编辑个人信息',
          color: '#67C23A'
        },
        {
          path: '/settings',
          icon: 'el-icon-setting',
          title: '设置',
          desc: '系统设置和偏好',
          color: '#909399'
        },
        {
          path: '/help',
          icon: 'el-icon-question',
          title: '帮助中心',
          desc: '获取使用帮助',
          color: '#E6A23C'
        }
      ]
    }
  },
  
  computed: {
    isLoggedIn() {
      return !!this.currentUser.id
    }
  },
  
  created() {
    this.loadUserInfo()
    this.updateTime()
    
    // 定时更新时间
    setInterval(() => {
      this.updateTime()
    }, 1000)
  },
  
  mounted() {
    // 添加数字动画
    this.$nextTick(() => {
      const digits = document.querySelectorAll('.digit')
      digits.forEach((digit, index) => {
        setTimeout(() => {
          digit.style.opacity = '1'
          digit.style.transform = 'translateY(0)'
        }, index * 200)
      })
    })
  },
  
  methods: {
    loadUserInfo() {
      const user = JSON.parse(localStorage.getItem('xm-user') || '{}')
      this.currentUser = user
      this.determinePermissionLevel()
    },
    
    determinePermissionLevel() {
      // 根据用户角色确定权限等级
      const role = this.currentUser.role || ''
      if (role.includes('admin')) {
        this.permissionLevel = '系统管理员'
      } else if (role.includes('manager')) {
        this.permissionLevel = '经理'
      } else {
        this.permissionLevel = '标准用户'
      }
    },
    
    updateTime() {
      const now = new Date()
      this.currentTime = now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
      })
    },
    
    generateErrorId() {
      return `FORBIDDEN-${Date.now().toString(36)}-${Math.random().toString(36).substr(2, 5)}`.toUpperCase()
    },
    
    goHome() {
      this.$router.push('/')
    },
    
    goBack() {
      if (window.history.length > 1) {
        this.$router.go(-1)
      } else {
        this.goHome()
      }
    },
    
    refreshPage() {
      // 刷新页面并重新检查权限
      this.$confirm('确定要刷新权限吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        window.location.reload()
      })
    },
    
    goToLogin() {
      this.$router.push('/login')
    },
    
    requestPermission() {
      this.$prompt('请输入申请权限的理由', '权限申请', {
        confirmButtonText: '提交申请',
        cancelButtonText: '取消',
        inputPlaceholder: '请详细说明为什么需要访问此页面...',
        inputType: 'textarea',
        inputValidator: (value) => {
          if (!value || value.length < 10) {
            return '申请理由至少需要10个字符'
          }
          return true
        }
      }).then(({ value }) => {
        this.$message.success('权限申请已提交，管理员会尽快处理')
        // 在实际项目中，这里应该发送申请到服务器
        console.log('权限申请理由:', value)
      }).catch(() => {})
    },
    
    contactAdmin() {
      this.$message.info('联系管理员功能开发中')
      // 实际项目中可以打开聊天窗口或发送邮件
    },
    
    goToPage(path) {
      this.$router.push(path)
    },
    
    viewSecurityGuide() {
      this.$message.info('查看安全指南功能开发中')
    },
    
    viewPrivacyPolicy() {
      this.$message.info('查看隐私政策功能开发中')
    },
    
    clearBrowserCache() {
      this.$confirm('清除浏览器缓存可能会影响您的使用体验，确定要继续吗？', '清除缓存', {
        confirmButtonText: '确定清除',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.clear()
        sessionStorage.clear()
        this.$message.success('缓存已清除，页面将刷新')
        setTimeout(() => {
          window.location.reload()
        }, 1000)
      })
    }
  }
}
</script>

<style scoped>
.forbidden-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  position: relative;
  overflow: hidden;
}

.forbidden-bg-shapes {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(245, 108, 108, 0.1);
}

.shape-1 {
  width: 300px;
  height: 300px;
  top: -150px;
  left: -150px;
}

.shape-2 {
  width: 200px;
  height: 200px;
  bottom: -100px;
  right: -100px;
  background: rgba(230, 162, 60, 0.1);
}

.shape-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  right: 10%;
  background: rgba(64, 158, 255, 0.1);
}

.shape-4 {
  width: 100px;
  height: 100px;
  bottom: 30%;
  left: 20%;
  background: rgba(103, 194, 58, 0.1);
}

.forbidden-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.forbidden-code-display {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40px;
  position: relative;
}

.digit {
  font-size: 160px;
  font-weight: 900;
  color: #F56C6C;
  text-shadow: 3px 3px 0 rgba(245, 108, 108, 0.2);
  opacity: 0;
  transform: translateY(-50px);
  transition: all 0.5s ease;
  margin: 0 10px;
}

.digit-1 {
  color: #F56C6C;
}

.digit-2 {
  color: #E6A23C;
  transition-delay: 0.1s;
}

.digit-3 {
  color: #409EFF;
  transition-delay: 0.2s;
}

.forbidden-icon {
  position: absolute;
  top: -20px;
  right: -20px;
  background: white;
  border-radius: 50%;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  animation: pulse 2s infinite;
}

.forbidden-icon i {
  font-size: 40px;
  color: #F56C6C;
}

.forbidden-content {
  text-align: center;
  max-width: 800px;
  margin: 0 auto 60px;
}

.forbidden-title {
  font-size: 36px;
  font-weight: 600;
  color: #F56C6C;
  margin-bottom: 20px;
  line-height: 1.2;
}

.forbidden-description {
  font-size: 18px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 30px;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

/* 原因分析 */
.reason-analysis {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin: 40px 0;
}

.reason-item {
  background: white;
  border-radius: 12px;
  padding: 24px;
  text-align: left;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
}

.reason-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
}

.reason-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #F56C6C 0%, #ff9a9e 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
}

.reason-icon i {
  font-size: 24px;
  color: white;
}

.reason-text h4 {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}

.reason-text p {
  font-size: 14px;
  color: #606266;
  margin: 0;
  line-height: 1.5;
}

/* 权限信息 */
.permission-info {
  margin: 40px 0;
}

.permission-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  text-align: left;
}

.permission-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.permission-title i {
  color: #409EFF;
}

.user-info-details {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.info-row {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-label {
  font-size: 14px;
  color: #909399;
}

.info-value {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin: 40px 0;
  flex-wrap: wrap;
}

.home-btn,
.back-btn,
.refresh-btn,
.login-btn {
  padding: 12px 28px;
  border-radius: 8px;
  font-size: 16px;
  transition: all 0.3s ease;
  min-width: 140px;
}

.home-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(64, 158, 255, 0.3);
}

.back-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(144, 147, 153, 0.2);
}

.refresh-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(230, 162, 60, 0.3);
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(103, 194, 58, 0.3);
}

/* 权限申请 */
.permission-request {
  background: linear-gradient(135deg, #fdfcfb 0%, #e2d1c3 100%);
  border-radius: 12px;
  padding: 30px;
  margin: 40px 0;
  border-left: 4px solid #E6A23C;
}

.request-title {
  font-size: 20px;
  font-weight: 600;
  color: #E6A23C;
  margin: 0 0 12px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.request-description {
  color: #606266;
  font-size: 16px;
  margin: 0 0 24px 0;
}

.request-actions {
  display: flex;
  gap: 16px;
}

/* 快速导航 */
.quick-navigation {
  margin: 40px 0;
}

.navigation-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.navigation-title i {
  color: #409EFF;
}

.navigation-links {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.nav-link {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #e4e7ed;
}

.nav-link:hover {
  background-color: #f5f7fa;
  border-color: #409EFF;
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

.link-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.link-icon i {
  font-size: 24px;
  color: white;
}

.link-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
  text-align: left;
}

.link-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.link-desc {
  font-size: 12px;
  color: #909399;
}

.link-arrow {
  color: #909399;
  font-size: 14px;
}

/* 帮助信息 */
.help-section {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  margin: 40px 0;
}

.help-item {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  gap: 16px;
  text-align: left;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.help-item i {
  font-size: 24px;
  color: #409EFF;
  margin-top: 4px;
}

.help-content h4 {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}

.help-content p {
  font-size: 14px;
  color: #606266;
  margin: 0;
  line-height: 1.5;
}

/* 系统信息 */
.system-info {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  padding: 16px;
  margin-top: 40px;
  font-family: 'Courier New', monospace;
}

.system-info p {
  margin: 4px 0;
  color: #606266;
  font-size: 14px;
}

/* 插画区域 */
.forbidden-illustration {
  margin-top: 40px;
}

.illustration-wrapper {
  text-align: center;
}

.illustration {
  width: 200px;
  height: 150px;
  margin: 0 auto 20px;
  opacity: 0.8;
}

.illustration-text {
  color: #F56C6C;
  font-weight: 500;
  font-size: 14px;
}

/* 页面底部 */
.forbidden-footer {
  margin-top: auto;
  padding: 20px;
  text-align: center;
  color: #909399;
  font-size: 14px;
  border-top: 1px solid #e4e7ed;
  background: white;
}

.footer-content p {
  margin: 5px 0;
}

.footer-links {
  margin: 10px 0;
}

.footer-links a {
  color: #409EFF;
  text-decoration: none;
  margin: 0 8px;
  transition: color 0.3s ease;
}

.footer-links a:hover {
  color: #F56C6C;
  text-decoration: underline;
}

.separator {
  margin: 0 8px;
  color: #dcdfe6;
}

.security-level {
  margin-top: 10px;
}

.security-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: #f5f7fa;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  color: #606266;
}

.security-badge i {
  color: #67C23A;
}

/* 动画 */
@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.forbidden-content > * {
  animation: fadeInUp 0.6s ease forwards;
}

.forbidden-content > *:nth-child(1) { animation-delay: 0.1s; }
.forbidden-content > *:nth-child(2) { animation-delay: 0.2s; }
.forbidden-content > *:nth-child(3) { animation-delay: 0.3s; }
.forbidden-content > *:nth-child(4) { animation-delay: 0.4s; }

/* 响应式设计 */
@media (max-width: 768px) {
  .digit {
    font-size: 120px;
  }
  
  .forbidden-title {
    font-size: 28px;
  }
  
  .forbidden-description {
    font-size: 16px;
  }
  
  .reason-analysis {
    grid-template-columns: 1fr;
  }
  
  .user-info-details {
    grid-template-columns: 1fr;
  }
  
  .navigation-links {
    grid-template-columns: 1fr;
  }
  
  .help-section {
    grid-template-columns: 1fr;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .home-btn,
  .back-btn,
  .refresh-btn,
  .login-btn {
    width: 100%;
    max-width: 300px;
  }
}

@media (max-width: 480px) {
  .digit {
    font-size: 80px;
    margin: 0 5px;
  }
  
  .forbidden-icon {
    width: 60px;
    height: 60px;
    top: -10px;
    right: -10px;
  }
  
  .forbidden-icon i {
    font-size: 30px;
  }
  
  .forbidden-title {
    font-size: 24px;
  }
  
  .request-actions {
    flex-direction: column;
  }
}
</style>