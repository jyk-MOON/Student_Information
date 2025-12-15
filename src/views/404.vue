<template>
  <div class="error-page">
    <!-- 装饰性背景元素 -->
    <div class="error-bg-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="shape shape-4"></div>
    </div>

    <div class="error-container">
      <!-- 错误代码展示 -->
      <div class="error-code-display">
        <span class="digit digit-1">4</span>
        <span class="digit digit-2">0</span>
        <span class="digit digit-3">4</span>
        <div class="error-icon">
          <i class="el-icon-warning-outline"></i>
        </div>
      </div>

      <!-- 错误信息 -->
      <div class="error-content">
        <h1 class="error-title">页面不存在</h1>
        <p class="error-description">
          抱歉，您访问的页面可能已被移动、删除或暂时不可用。
        </p>

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
            type="text" 
            icon="el-icon-refresh" 
            class="refresh-btn"
            @click="refreshPage">
            刷新页面
          </el-button>
        </div>

        <!-- 额外帮助 -->
        <div class="extra-help">
          <p>需要帮助？</p>
          <div class="contact-options">
            <el-button 
              type="text" 
              icon="el-icon-service" 
              @click="contactSupport">
              联系客服
            </el-button>
            <el-button 
              type="text" 
              icon="el-icon-document" 
              @click="viewDocumentation">
              查看文档
            </el-button>
          </div>
        </div>

        <!-- 搜索建议 -->
        <div class="search-suggestion">
          <p>或者，您可以搜索需要的内容：</p>
          <div class="search-input">
            <el-input
              v-model="searchQuery"
              placeholder="搜索页面内容..."
              size="large"
              @keyup.enter="handleSearch">
              <template #append>
                <el-button 
                  icon="el-icon-search"
                  @click="handleSearch" />
              </template>
            </el-input>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "NotFound",
  
  data() {
    return {
      searchQuery: '',
      errorId: this.generateErrorId(),
      currentYear: new Date().getFullYear()
    }
  },
  
  methods: {
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
      window.location.reload()
    },
    
    handleSearch() {
      if (this.searchQuery.trim()) {
        // 在实际项目中，这里应该实现搜索功能
        this.$message.info(`搜索: ${this.searchQuery}`)
        // 可以跳转到搜索页面或执行搜索
      }
    },
    
    contactSupport() {
      this.$message.info('联系客服功能')
      // 实际项目中可以打开客服对话框或跳转到客服页面
    },
    
    viewDocumentation() {
      this.$message.info('查看文档功能')
      // 跳转到文档页面
    },
    
    reportError() {
      this.$message.success('问题已报告，感谢您的反馈！')
      // 实际项目中可以发送错误报告到服务器
    },
    
    viewLogs() {
      this.$message.info('查看日志功能')
      // 实际项目中可以跳转到日志页面
    },
    
    clearCache() {
      this.$confirm('确定要清除本地缓存吗？这可能会影响您的使用体验。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.clear()
        this.$message.success('缓存已清除，页面将刷新')
        setTimeout(() => {
          window.location.reload()
        }, 1000)
      })
    },
    
    generateErrorId() {
      // 生成一个唯一的错误ID
      return `ERR-${Date.now().toString(36)}-${Math.random().toString(36).substr(2, 5)}`.toUpperCase()
    }
  },
  
  mounted() {
    // 记录404错误访问
    console.warn(`404 Error - Page not found: ${window.location.pathname}`)
    
    // 添加页面进入动画
    this.$nextTick(() => {
      const digits = document.querySelectorAll('.digit')
      digits.forEach((digit, index) => {
        setTimeout(() => {
          digit.style.opacity = '1'
          digit.style.transform = 'translateY(0)'
        }, index * 200)
      })
    })
  }
}
</script>

<style scoped>
.error-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  position: relative;
  overflow: hidden;
}

.error-bg-shapes {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(64, 158, 255, 0.1);
}

.shape-1 {
  width: 300px;
  height: 300px;
  top: -150px;
  right: -150px;
}

.shape-2 {
  width: 200px;
  height: 200px;
  bottom: -100px;
  left: -100px;
  background: rgba(103, 194, 58, 0.1);
}

.shape-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  left: 10%;
  background: rgba(230, 162, 60, 0.1);
}

.shape-4 {
  width: 100px;
  height: 100px;
  bottom: 20%;
  right: 20%;
  background: rgba(245, 108, 108, 0.1);
}

.error-container {
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

.error-code-display {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40px;
  position: relative;
}

.digit {
  font-size: 160px;
  font-weight: 900;
  color: #409EFF;
  text-shadow: 3px 3px 0 rgba(64, 158, 255, 0.2);
  opacity: 0;
  transform: translateY(-50px);
  transition: all 0.5s ease;
  margin: 0 10px;
}

.digit-1 {
  color: #409EFF;
}

.digit-2 {
  color: #67C23A;
  transition-delay: 0.1s;
}

.digit-3 {
  color: #E6A23C;
  transition-delay: 0.2s;
}

.error-icon {
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
  animation: bounce 2s infinite;
}

.error-icon i {
  font-size: 40px;
  color: #F56C6C;
}

.error-content {
  text-align: center;
  max-width: 800px;
  margin: 0 auto 60px;
}

.error-title {
  font-size: 36px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
  line-height: 1.2;
}

.error-description {
  font-size: 18px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 30px;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.possible-reasons {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin: 30px auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  max-width: 500px;
  text-align: left;
}

.reason-title {
  font-weight: 500;
  color: #303133;
  margin-bottom: 12px;
  font-size: 16px;
}

.reason-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.reason-list li {
  color: #606266;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.reason-list i {
  margin-right: 8px;
  color: #F56C6C;
}

.action-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin: 40px 0;
  flex-wrap: wrap;
}

.home-btn,
.back-btn,
.refresh-btn {
  padding: 12px 28px;
  border-radius: 8px;
  font-size: 16px;
  transition: all 0.3s ease;
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
  color: #409EFF;
}

.extra-help {
  margin: 40px 0;
  padding-top: 30px;
  border-top: 1px solid #e4e7ed;
}

.extra-help p {
  color: #909399;
  margin-bottom: 16px;
}

.contact-options {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.search-suggestion {
  margin: 40px 0;
}

.search-input {
  max-width: 400px;
  margin: 20px auto;
}

.sitemap-links {
  margin: 40px 0;
}

.quick-links {
  display: flex;
  gap: 24px;
  justify-content: center;
  flex-wrap: wrap;
  margin-top: 16px;
}

.link-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #409EFF;
  text-decoration: none;
  padding: 10px 20px;
  border-radius: 8px;
  background: white;
  transition: all 0.3s ease;
}

.link-item:hover {
  background: #409EFF;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
}

.link-item i {
  font-size: 18px;
}

.error-illustration {
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
  color: #909399;
  font-style: italic;
  font-size: 14px;
}

.error-footer {
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

.error-id {
  font-family: monospace;
  background: #f5f7fa;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
}

.footer-links a {
  color: #409EFF;
  text-decoration: none;
  margin: 0 8px;
  transition: color 0.3s ease;
}

.footer-links a:hover {
  color: #67C23A;
  text-decoration: underline;
}

/* 动画 */
@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
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

.error-content > * {
  animation: fadeInUp 0.6s ease forwards;
}

.error-content > *:nth-child(1) { animation-delay: 0.1s; }
.error-content > *:nth-child(2) { animation-delay: 0.2s; }
.error-content > *:nth-child(3) { animation-delay: 0.3s; }
.error-content > *:nth-child(4) { animation-delay: 0.4s; }
.error-content > *:nth-child(5) { animation-delay: 0.5s; }

/* 响应式设计 */
@media (max-width: 768px) {
  .digit {
    font-size: 120px;
  }
  
  .error-title {
    font-size: 28px;
  }
  
  .error-description {
    font-size: 16px;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .home-btn,
  .back-btn,
  .refresh-btn {
    width: 100%;
    max-width: 300px;
  }
  
  .quick-links {
    flex-direction: column;
    align-items: center;
  }
  
  .link-item {
    width: 100%;
    max-width: 250px;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .digit {
    font-size: 80px;
    margin: 0 5px;
  }
  
  .error-icon {
    width: 60px;
    height: 60px;
    top: -10px;
    right: -10px;
  }
  
  .error-icon i {
    font-size: 30px;
  }
  
  .error-title {
    font-size: 24px;
  }
  
  .possible-reasons {
    padding: 16px;
  }
}
</style>