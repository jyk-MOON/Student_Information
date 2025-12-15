<template>
  <div class="front-container">
    <!-- 公告栏 -->
    <div class="announcement-bar">
      <div class="announcement-content">
        <i class="el-icon-message-solid announcement-icon"></i>
        <span class="announcement-text">{{ currentNotice }}</span>
      </div>
    </div>

    <!-- 头部导航 -->
    <header class="app-header">
      <div class="header-container">
        <!-- Logo区域 -->
        <div class="logo-section">
          <div class="logo-wrapper">
            <img src="@/assets/imgs/logo.png" alt="网站Logo" class="logo-img">
            <span class="logo-text">项目前台</span>
          </div>
        </div>

        <!-- 导航菜单 -->
        <nav class="nav-section">
          <div class="nav-menu">
            <el-menu 
              :default-active="$route.path" 
              mode="horizontal" 
              router
              class="custom-menu"
              background-color="transparent"
              text-color="#333"
              active-text-color="#409EFF">
              <el-menu-item index="/front/home" class="nav-item">
                <i class="el-icon-house"></i>
                <span>首页</span>
              </el-menu-item>
              <el-menu-item index="/front/person" class="nav-item">
                <i class="el-icon-user"></i>
                <span>个人中心</span>
              </el-menu-item>
            </el-menu>
          </div>
        </nav>

        <!-- 用户操作区 -->
        <div class="user-section">
          <div v-if="!user.username" class="auth-buttons">
            <el-button 
              type="text" 
              class="login-btn"
              @click="$router.push('/login')">
              登录
            </el-button>
            <el-button 
              type="primary" 
              size="small"
              class="register-btn"
              @click="$router.push('/register')">
              注册
            </el-button>
          </div>
          <div v-else class="user-profile">
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar 
                  :src="user.avatar" 
                  :size="36"
                  class="user-avatar">
                  {{ user.name?.charAt(0) }}
                </el-avatar>
                <span class="user-name">{{ user.name }}</span>
                <i class="el-icon-arrow-down arrow-icon"></i>
              </div>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="profile">
                  <i class="el-icon-user"></i>个人资料
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <i class="el-icon-setting"></i>设置
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <i class="el-icon-switch-button"></i>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
      </div>
    </header>

    <!-- 主体内容 -->
    <main class="main-content">
      <div class="content-container">
        <router-view 
          ref="child" 
          @update:user="updateUser" 
          class="router-view" />
      </div>
    </main>
  </div>
</template>

<script>
export default {
  name: "FrontLayout",

  data() {
    return {
      currentNotice: '',
      noticeList: [],
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
      noticeIndex: 0,
      noticeInterval: null
    }
  },

  mounted() {
    this.loadNotice()
  },

  beforeDestroy() {
    if (this.noticeInterval) {
      clearInterval(this.noticeInterval)
    }
  },

  methods: {
    async loadNotice() {
      try {
        const res = await this.$request.get('/notice/selectAll')
        this.noticeList = res.data || []
        
        if (this.noticeList.length > 0) {
          this.currentNotice = this.noticeList[0].content
          
          // 清除之前的定时器
          if (this.noticeInterval) {
            clearInterval(this.noticeInterval)
          }
          
          // 设置轮播
          this.noticeIndex = 0
          this.noticeInterval = setInterval(() => {
            this.noticeIndex = (this.noticeIndex + 1) % this.noticeList.length
            this.currentNotice = this.noticeList[this.noticeIndex].content
          }, 3000)
        }
      } catch (error) {
        console.error('加载公告失败:', error)
      }
    },

    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
    },

    handleCommand(command) {
      switch (command) {
        case 'logout':
          this.logout()
          break
        case 'profile':
          this.$router.push('/front/person')
          break
        case 'settings':
          // 跳转到设置页面
          this.$message.info('设置功能开发中')
          break
      }
    },

    logout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem("xm-user")
        this.$message.success('退出成功')
        this.$router.push("/login")
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.front-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

/* 公告栏样式 */
.announcement-bar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 8px 0;
  font-size: 14px;
  position: relative;
  overflow: hidden;
}

.announcement-content {
  display: flex;
  align-items: center;
  justify-content: center;
  animation: slideIn 0.5s ease;
}

.announcement-icon {
  margin-right: 8px;
  font-size: 16px;
}

.announcement-text {
  font-weight: 500;
  text-shadow: 0 1px 2px rgba(0,0,0,0.1);
}

/* 头部样式 */
.app-header {
  background: white;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo-section {
  flex: 0 0 auto;
}

.logo-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.logo-img {
  height: 36px;
  width: auto;
  transition: transform 0.3s ease;
}

.logo-img:hover {
  transform: scale(1.05);
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  letter-spacing: 1px;
}

/* 导航菜单 */
.nav-section {
  flex: 1;
  display: flex;
  justify-content: center;
}

.custom-menu {
  border-bottom: none !important;
}

.nav-item {
  margin: 0 16px !important;
  font-size: 16px !important;
  transition: all 0.3s ease !important;
}

.nav-item:hover {
  background-color: #f5f7fa !important;
  border-radius: 4px !important;
}

.nav-item i {
  margin-right: 6px !important;
}

/* 用户区域 */
.user-section {
  flex: 0 0 auto;
}

.auth-buttons {
  display: flex;
  align-items: center;
  gap: 12px;
}

.login-btn {
  color: #666;
  font-size: 14px;
  padding: 6px 16px;
}

.login-btn:hover {
  color: #409EFF;
}

.register-btn {
  padding: 8px 20px;
  border-radius: 20px;
}

.user-profile {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 8px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.user-info:hover {
  background: #f5f7fa;
}

.user-avatar {
  border: 2px solid #e4e7ed;
}

.user-name {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.arrow-icon {
  color: #909399;
  font-size: 12px;
  transition: transform 0.3s ease;
}

.user-info:hover .arrow-icon {
  transform: rotate(180deg);
}

/* 主体内容 */
.main-content {
  flex: 1;
  padding: 24px;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
  padding: 24px;
  min-height: calc(100vh - 200px);
}

.router-view {
  animation: fadeIn 0.5s ease;
}

/* 底部 */
.app-footer {
  background: white;
  border-top: 1px solid #e4e7ed;
  padding: 20px 0;
  margin-top: 40px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

/* 动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-container {
    padding: 0 16px;
    height: 56px;
  }

  .logo-text {
    font-size: 18px;
  }

  .nav-item {
    margin: 0 8px !important;
    font-size: 14px !important;
  }

  .user-name {
    display: none;
  }

  .main-content {
    padding: 16px;
  }

  .content-container {
    padding: 16px;
    border-radius: 6px;
  }

  .announcement-bar {
    font-size: 12px;
    padding: 6px 0;
  }
}

@media (max-width: 480px) {
  .logo-text {
    display: none;
  }

  .nav-section {
    justify-content: flex-end;
  }

  .auth-buttons {
    gap: 8px;
  }
}
</style>