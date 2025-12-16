<template>
  <div class="not-found-container">
    <!-- 背景装饰元素 -->
    <div class="decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <div class="content-wrapper">
      <!-- 404数字展示 -->
      <div class="error-number">
        <span class="digit">4</span>
        <span class="digit zero">0</span>
        <span class="digit">4</span>
      </div>
      
      <!-- 错误信息 -->
      <div class="error-content">
        <h1 class="error-title">页面迷路了</h1>
        <p class="error-description">
          我们找不到您要访问的页面，它可能已被移动、删除或暂时不可用
        </p>
        
        <!-- 返回首页按钮 -->
        <div class="action-section">
          <router-link to="/" class="home-button">
            <span class="button-text">返回首页</span>
            <span class="button-icon">→</span>
          </router-link>
          
          <!-- 额外提示 -->
          <p class="helper-text">
            或者尝试刷新页面，检查网址是否正确
          </p>
        </div>
      </div>
      
      <!-- 装饰性图标 -->
      <div class="decoration-icons">
        <div class="icon">🔍</div>
        <div class="icon">📄</div>
        <div class="icon">🌐</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "404",
  data() {
    return {}
  },
  created() {

  },
  methods: {}
}
</script>

<style scoped>
.not-found-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
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
  background: linear-gradient(45deg, rgba(255,255,255,0.8), rgba(255,255,255,0.2));
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
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  max-width: 800px;
  margin: 0 20px;
  backdrop-filter: blur(10px);
}

/* 404数字样式 */
.error-number {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30px;
}

.digit {
  font-size: 180px;
  font-weight: 900;
  color: #2c3e50;
  text-shadow: 8px 8px 16px rgba(0, 0, 0, 0.1);
  line-height: 1;
  transition: transform 0.3s ease;
}

.digit:hover {
  transform: translateY(-5px);
}

.digit.zero {
  color: #3498db;
  position: relative;
  margin: 0 20px;
}

.digit.zero::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(52, 152, 219, 0.2) 30%, transparent 70%);
  border-radius: 50%;
  animation: pulse 2s infinite;
}

/* 错误信息样式 */
.error-content {
  max-width: 600px;
}

.error-title {
  font-size: 36px;
  color: #2c3e50;
  margin-bottom: 16px;
  font-weight: 700;
}

.error-description {
  font-size: 18px;
  color: #7f8c8d;
  line-height: 1.6;
  margin-bottom: 40px;
  max-width: 500px;
}

/* 按钮样式 */
.action-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.home-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 16px 40px;
  background: linear-gradient(45deg, #3498db, #2980b9);
  color: white;
  text-decoration: none;
  border-radius: 50px;
  font-size: 18px;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 8px 25px rgba(52, 152, 219, 0.3);
  border: none;
  cursor: pointer;
  position: relative;
  overflow: hidden;
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

.home-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 30px rgba(52, 152, 219, 0.4);
}

.home-button:active {
  transform: translateY(-1px);
}

.button-text {
  margin-right: 10px;
}

.button-icon {
  font-size: 24px;
  transition: transform 0.3s ease;
}

.home-button:hover .button-icon {
  transform: translateX(5px);
}

.helper-text {
  font-size: 14px;
  color: #95a5a6;
  margin-top: 10px;
}

/* 装饰图标 */
.decoration-icons {
  display: flex;
  gap: 30px;
  margin-top: 40px;
  opacity: 0.6;
}

.icon {
  font-size: 32px;
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
    padding: 0 20px;
  }
  
  .content-wrapper {
    padding: 30px 20px;
    margin: 0 15px;
  }
}

@media (max-width: 480px) {
  .digit {
    font-size: 80px;
  }
  
  .error-number {
    margin-bottom: 20px;
  }
  
  .error-title {
    font-size: 24px;
  }
  
  .home-button {
    padding: 14px 30px;
    font-size: 16px;
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