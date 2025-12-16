<template>
  <div class="front-container">
    <!-- 公告栏 -->
    <div class="notice-bar">
      <div class="notice-content">
        <i class="el-icon-bell notice-icon"></i>
        <div class="notice-text">
          <span class="notice-label">公告：</span>
          <span class="notice-message">{{ top }}</span>
        </div>
      </div>
    </div>

    <!-- 头部导航 -->
    <header class="front-header">
      <div class="header-container">
        <!-- Logo区域 -->
        <div class="header-left">
          <div class="logo-container">
            <img src="@/assets/imgs/logo.png" alt="网站Logo" class="logo-img">
            <div class="logo-text">项目前台</div>
          </div>
        </div>

        <!-- 导航菜单 -->
        <nav class="header-center">
          <div class="nav-menu">
            <el-menu 
              :default-active="$route.path" 
              mode="horizontal" 
              router
              class="custom-menu"
              active-text-color="#409EFF"
              text-color="#606266">
              <el-menu-item index="/front/home" class="nav-item">
                <i class="el-icon-house"></i>
                <span class="nav-text">首页</span>
              </el-menu-item>
              <el-menu-item index="/front/person" class="nav-item">
                <i class="el-icon-user"></i>
                <span class="nav-text">个人中心</span>
              </el-menu-item>
            </el-menu>
          </div>
        </nav>

        <!-- 用户操作区域 -->
        <div class="header-right">
          <div v-if="!user.username" class="auth-buttons">
            <el-button 
              type="primary" 
              plain 
              @click="$router.push('/login')"
              class="auth-btn login-btn">
              登录
            </el-button>
            <el-button 
              type="primary" 
              @click="$router.push('/register')"
              class="auth-btn register-btn">
              注册
            </el-button>
          </div>
          <div v-else class="user-info">
            <el-dropdown placement="bottom-end" class="user-dropdown">
              <div class="user-profile">
                <div class="avatar-container">
                  <img :src="user.avatar" alt="用户头像" class="user-avatar">
                  <div class="avatar-badge"></div>
                </div>
                <div class="user-details">
                  <span class="user-name">{{ user.name }}</span>
                  <i class="el-icon-arrow-down dropdown-icon"></i>
                </div>
              </div>
              <el-dropdown-menu slot="dropdown" class="user-dropdown-menu">
                <el-dropdown-item class="dropdown-item">
                  <div class="dropdown-content" @click="logout">
                    <i class="el-icon-switch-button"></i>
                    <span>退出登录</span>
                  </div>
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
        <router-view ref="child" @update:user="updateUser" />
      </div>
    </main>
  </div>
</template>

<script>
export default {
  name: "FrontLayout",

  data () {
    return {
      top: '',
      notice: [],
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
    }
  },

  mounted() {
    this.loadNotice()
  },
  methods: {
    loadNotice() {
      this.$request.get('/notice/selectAll').then(res => {
        this.notice = res.data
        let i = 0
        if (this.notice && this.notice.length) {
          this.top = this.notice[0].content
          setInterval(() => {
            this.top = this.notice[i].content
            i++
            if (i === this.notice.length) {
              i = 0
            }
          }, 2500)
        }
      })
    },
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
    },
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
  }
}
</script>

<style scoped>
.front-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

/* 公告栏样式 */
.notice-bar {
  background: linear-gradient(90deg, #ff6b6b, #ff8e53);
  color: white;
  padding: 8px 0;
  position: relative;
  overflow: hidden;
}

.notice-bar::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
  animation: shimmer 2s infinite;
}

@keyframes shimmer {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.notice-content {
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.notice-icon {
  font-size: 18px;
  margin-right: 12px;
  animation: bell-ring 2s infinite;
}

@keyframes bell-ring {
  0%, 100% { transform: rotate(0); }
  10%, 30%, 50%, 70%, 90% { transform: rotate(-10deg); }
  20%, 40%, 60%, 80% { transform: rotate(10deg); }
}

.notice-text {
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
}

.notice-label {
  font-weight: bold;
  margin-right: 8px;
  text-shadow: 0 1px 2px rgba(0,0,0,0.2);
}

.notice-message {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 头部导航样式 */
.front-header {
  background: white;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 1000;
  transition: all 0.3s ease;
}

.front-header:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 70px;
}

/* Logo区域 */
.header-left {
  flex: 0 0 auto;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.logo-container:hover {
  transform: translateY(-2px);
}

.logo-img {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  object-fit: cover;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.logo-text {
  font-size: 22px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: 1px;
}

/* 导航菜单 */
.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
}

.nav-menu {
  max-width: 400px;
}

.custom-menu {
  border-bottom: none !important;
  background: transparent !important;
}

.custom-menu >>> .el-menu-item {
  padding: 0 25px !important;
  height: 50px !important;
  line-height: 50px !important;
  margin: 0 5px;
  border-radius: 25px;
  transition: all 0.3s ease;
  font-weight: 500;
}

.custom-menu >>> .el-menu-item:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white !important;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.custom-menu >>> .el-menu-item.is-active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white !important;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-text {
  font-size: 16px;
}

/* 用户操作区域 */
.header-right {
  flex: 0 0 auto;
}

.auth-buttons {
  display: flex;
  gap: 12px;
}

.auth-btn {
  padding: 10px 24px !important;
  border-radius: 25px !important;
  font-weight: 500 !important;
  transition: all 0.3s ease !important;
}

.login-btn {
  border: 2px solid #409EFF !important;
}

.login-btn:hover {
  background: #409EFF !important;
  color: white !important;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.4);
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.4);
}

/* 用户信息 */
.user-info {
  position: relative;
}

.user-dropdown {
  cursor: pointer;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  border-radius: 25px;
  transition: all 0.3s ease;
  background: rgba(245, 247, 250, 0.8);
}

.user-profile:hover {
  background: white;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.avatar-container {
  position: relative;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s ease;
}

.user-profile:hover .user-avatar {
  transform: scale(1.1);
}

.avatar-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 10px;
  height: 10px;
  background: #52c41a;
  border-radius: 50%;
  border: 2px solid white;
}

.user-details {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-name {
  font-weight: 600;
  color: #333;
  font-size: 15px;
}

.dropdown-icon {
  color: #999;
  font-size: 12px;
  transition: transform 0.3s ease;
}

.user-dropdown:hover .dropdown-icon {
  transform: rotate(180deg);
}

.user-dropdown-menu {
  border-radius: 12px !important;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15) !important;
  border: 1px solid #f0f0f0 !important;
  padding: 8px 0 !important;
}

.dropdown-item {
  padding: 0 !important;
}

.dropdown-content {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 20px;
  color: #ff4d4f;
  font-weight: 500;
  transition: all 0.3s ease;
}

.dropdown-content:hover {
  background: #fff2f0;
  color: #ff7875;
}

.dropdown-content i {
  font-size: 16px;
}

/* 主体内容样式 */
.main-content {
  padding: 30px 0;
  min-height: calc(100vh - 160px);
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-container {
    padding: 0 15px;
    height: 60px;
  }
  
  .logo-text {
    font-size: 18px;
  }
  
  .custom-menu >>> .el-menu-item {
    padding: 0 15px !important;
    font-size: 14px;
  }
  
  .nav-text {
    display: none;
  }
  
  .auth-btn {
    padding: 8px 16px !important;
    font-size: 14px !important;
  }
  
  .user-name {
    display: none;
  }
  
  .content-container {
    padding: 0 15px;
  }
}
</style>