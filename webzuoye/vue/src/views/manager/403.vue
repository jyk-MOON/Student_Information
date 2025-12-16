<template>
  <div class="access-denied-container">
    <!-- 背景装饰元素 -->
    <div class="decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <div class="content-wrapper">
      <!-- 错误代码和图标 -->
      <div class="error-header">
        <div class="error-icon">🔒</div>
        <div class="error-number">
          <span class="digit">4</span>
          <span class="digit zero">0</span>
          <span class="digit">3</span>
        </div>
      </div>
      
      <!-- 错误信息 -->
      <div class="error-content">
        <h1 class="error-title">访问被拒绝</h1>
        <p class="error-description">
          您没有权限访问此页面，这可能是因为：
        </p>
        
        <!-- 原因列表 -->
        <div class="reason-list">
          <div class="reason-item">
            <span class="reason-icon">👤</span>
            <span class="reason-text">当前账户权限不足</span>
          </div>
          <div class="reason-item">
            <span class="reason-icon">🛡️</span>
            <span class="reason-text">资源需要特殊授权</span>
          </div>
          <div class="reason-item">
            <span class="reason-icon">🔐</span>
            <span class="reason-text">页面受到保护</span>
          </div>
        </div>
        
        <!-- 操作按钮 -->
        <div class="action-section">
          <div class="button-group">
            <router-link to="/" class="home-button primary-button">
              <span class="button-icon">🏠</span>
              <span class="button-text">返回首页</span>
            </router-link>
            <button class="home-button secondary-button" @click="goBack">
              <span class="button-icon">↩️</span>
              <span class="button-text">返回上一页</span>
            </button>
          </div>
          
          <!-- 额外提示 -->
          <div class="helper-section">
            <p class="helper-text">
              如果您认为这是一个错误，请联系系统管理员
            </p>
            <div class="contact-info">
              <span class="contact-item">📧 support@example.com</span>
              <span class="contact-item">📞 400-123-4567</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 装饰性图标 -->
      <div class="decoration-icons">
        <div class="icon">🛑</div>
        <div class="icon">🚫</div>
        <div class="icon">⚠️</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Auth",
  data() {
    return {}
  },
  created() {

  },
  methods: {
    goBack() {
      this.$router.go(-1);
    }
  }
}
</script>

<style scoped>
.access-denied-container {
  height: calc(100vh - 80px);
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #ffeaa7 0%, #fab1a0 100%);
  position: relative;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 背景装饰圆圈 */
.decoration {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(45deg, rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0.1));
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  right: -100px;
  animation: float 20s infinite linear;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: 50px;
  left: -50px;
  animation: float 15s infinite linear reverse;
}

.circle-3 {
  width: 150px;
  height: 150px;
  bottom: 150px;
  right: 20%;
  animation: float 25s infinite linear;
}

.content-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  z-index: 1;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  max-width: 800px;
  margin: 0 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.5);
}

/* 错误头部样式 */
.error-header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30px;
}

.error-icon {
  font-size: 80px;
  margin-right: 30px;
  animation: shake 3s infinite;
}

/* 403数字样式 */
.error-number {
  display: flex;
  align-items: center;
  justify-content: center;
}

.digit {
  font-size: 140px;
  font-weight: 900;
  color: #e74c3c;
  text-shadow: 6px 6px 12px rgba(0, 0, 0, 0.15);
  line-height: 1;
  transition: transform 0.3s ease;
}

.digit:hover {
  transform: translateY(-5px);
}

.digit.zero {
  color: #f39c12;
  position: relative;
  margin: 0 15px;
}

.digit.zero::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(243, 156, 18, 0.2) 30%, transparent 70%);
  border-radius: 50%;
  animation: pulse 2s infinite;
}

/* 错误信息样式 */
.error-content {
  max-width: 700px;
}

.error-title {
  font-size: 42px;
  color: #2c3e50;
  margin-bottom: 16px;
  font-weight: 800;
  position: relative;
  display: inline-block;
}

.error-title::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 4px;
  background: linear-gradient(90deg, #e74c3c, #f39c12);
  border-radius: 2px;
}

.error-description {
  font-size: 20px;
  color: #34495e;
  line-height: 1.6;
  margin: 30px 0;
  font-weight: 500;
}

/* 原因列表样式 */
.reason-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  margin: 30px 0 40px;
}

.reason-item {
  display: flex;
  align-items: center;
  background: rgba(236, 240, 241, 0.6);
  padding: 15px 25px;
  border-radius: 12px;
  transition: all 0.3s ease;
  width: 100%;
  max-width: 400px;
  border-left: 4px solid #e74c3c;
}

.reason-item:hover {
  transform: translateX(5px);
  background: rgba(236, 240, 241, 0.9);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.reason-icon {
  font-size: 24px;
  margin-right: 15px;
  width: 40px;
}

.reason-text {
  font-size: 17px;
  color: #2c3e50;
  font-weight: 500;
  text-align: left;
}

/* 按钮样式 */
.action-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
  margin-top: 30px;
}

.button-group {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  justify-content: center;
}

.home-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 16px 35px;
  border-radius: 50px;
  font-size: 18px;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s ease;
  border: none;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  min-width: 180px;
}

.home-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
  transition: left 0.5s ease;
}

.home-button:hover::before {
  left: 100%;
}

.primary-button {
  background: linear-gradient(45deg, #e74c3c, #c0392b);
  color: white;
  box-shadow: 0 8px 25px rgba(231, 76, 60, 0.3);
}

.primary-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 30px rgba(231, 76, 60, 0.4);
}

.secondary-button {
  background: linear-gradient(45deg, #3498db, #2980b9);
  color: white;
  box-shadow: 0 8px 25px rgba(52, 152, 219, 0.3);
}

.secondary-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 30px rgba(52, 152, 219, 0.4);
}

.button-icon {
  font-size: 22px;
  margin-right: 12px;
  transition: transform 0.3s ease;
}

.home-button:hover .button-icon {
  transform: scale(1.1);
}

/* 帮助信息样式 */
.helper-section {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid rgba(189, 195, 199, 0.3);
  width: 100%;
  max-width: 500px;
}

.helper-text {
  font-size: 16px;
  color: #7f8c8d;
  margin-bottom: 15px;
}

.contact-info {
  display: flex;
  justify-content: center;
  gap: 25px;
  flex-wrap: wrap;
}

.contact-item {
  font-size: 14px;
  color: #95a5a6;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 装饰图标 */
.decoration-icons {
  display: flex;
  gap: 30px;
  margin-top: 40px;
  opacity: 0.7;
}

.icon {
  font-size: 36px;
  animation: bounce 3s infinite ease-in-out;
}

.icon:nth-child(1) {
  animation-delay: 0s;
}

.icon:nth-child(2) {
  animation-delay: 0.5s;
}

.icon:nth-child(3) {
  animation-delay: 1s;
}

/* 动画定义 */
@keyframes float {
  0% {
    transform: translate(0, 0) rotate(0deg);
  }
  100% {
    transform: translate(30px, -20px) rotate(360deg);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 0.2;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.3;
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-15px);
  }
}

@keyframes shake {
  0%, 100% {
    transform: rotate(0deg);
  }
  10%, 30%, 50%, 70%, 90% {
    transform: rotate(-5deg);
  }
  20%, 40%, 60%, 80% {
    transform: rotate(5deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .error-header {
    flex-direction: column;
  }
  
  .error-icon {
    margin-right: 0;
    margin-bottom: 20px;
  }
  
  .digit {
    font-size: 100px;
  }
  
  .error-title {
    font-size: 32px;
  }
  
  .error-description {
    font-size: 18px;
    padding: 0 10px;
  }
  
  .content-wrapper {
    padding: 30px 20px;
    margin: 0 15px;
  }
  
  .button-group {
    flex-direction: column;
    align-items: center;
    width: 100%;
  }
  
  .home-button {
    width: 100%;
    max-width: 300px;
  }
  
  .contact-info {
    flex-direction: column;
    gap: 10px;
  }
}

@media (max-width: 480px) {
  .digit {
    font-size: 70px;
  }
  
  .error-icon {
    font-size: 60px;
  }
  
  .error-number {
    margin-bottom: 15px;
  }
  
  .error-title {
    font-size: 26px;
  }
  
  .home-button {
    padding: 14px 25px;
    font-size: 16px;
  }
  
  .reason-item {
    padding: 12px 20px;
  }
  
  .reason-text {
    font-size: 15px;
  }
  
  .circle {
    display: none;
  }
  
  .circle-1 {
    display: block;
    width: 200px;
    height: 200px;
  }
}
</style>