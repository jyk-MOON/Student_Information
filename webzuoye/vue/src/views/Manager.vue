<template>
  <div :class="['manager-container', {'theme-dark': isChange, 'theme-light': !isChange}]">
    <!-- 头部导航栏 -->
    <header :class="['manager-header', {'theme-header-dark': isChange, 'theme-header-light': !isChange}]">
      <div class="header-content">
        <!-- Logo和标题 -->
        <div class="header-left">
          <div class="logo-section">
            <div class="logo-icon">
              <i class="el-icon-s-platform"></i>
            </div>
            <h1 class="logo-title">Web系统</h1>
          </div>
        </div>

        <!-- 面包屑导航 -->
        <div class="header-center">
          <div class="breadcrumb-container">
            <el-breadcrumb separator-class="el-icon-arrow-right">
              <el-breadcrumb-item :to="{ path: '/' }">
                <i class="el-icon-s-home"></i>
                <span>首页</span>
              </el-breadcrumb-item>
              <el-breadcrumb-item :to="{ path: $route.path }">
                <i class="el-icon-location"></i>
                <span class="current-page">{{ $route.meta.name }}</span>
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </div>

        <!-- 用户操作区域 -->
        <div class="header-right">
          <div class="user-section">
            <el-dropdown 
              trigger="click" 
              placement="bottom-end" 
              @command="handleCommand"
              class="user-dropdown">
              <div class="user-profile">
                <div class="avatar-container">
                  <div class="avatar-placeholder">
                    <i class="el-icon-user-solid"></i>
                  </div>
                  <div class="user-status"></div>
                </div>
                <div class="user-info">
                  <span class="user-name">{{ user?.name }}</span>
                  <span class="user-role">管理员</span>
                </div>
                <i class="el-icon-arrow-down dropdown-arrow"></i>
              </div>
              <el-dropdown-menu slot="dropdown" class="user-menu">
                <el-dropdown-item class="menu-item" command="profile">
                  <i class="el-icon-user"></i>
                  <span>个人资料</span>
                </el-dropdown-item>
                <el-dropdown-item class="menu-item" command="calendar">
                  <i class="el-icon-date"></i>
                  <span>日历</span>
                </el-dropdown-item>
                <el-dropdown-item class="menu-item" divided command="settings">
                  <i class="el-icon-setting"></i>
                  <span>系统设置</span>
                </el-dropdown-item>
                <el-dropdown-item class="menu-item" command="logout">
                  <i class="el-icon-switch-button"></i>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
      </div>
    </header>

    <!-- 主体内容 -->
    <main class="manager-main">
      <!-- 侧边栏 -->
      <aside :class="['sidebar', {'sidebar-dark': isChange, 'sidebar-light': !isChange}]">
        <div class="sidebar-header">
          <div class="sidebar-title">
            <i class="el-icon-menu"></i>
            <span>系统菜单</span>
          </div>
          <div class="theme-toggle">
            <span class="theme-label">主题</span>
            <el-switch
              v-model="isChange"
              active-text="深色"
              inactive-text="浅色"
              active-color="#2c3e50"
              inactive-color="#f96332"
              @change="toggleTheme">
            </el-switch>
          </div>
        </div>

        <div class="sidebar-content">
          <el-menu
            :default-active="$route.path"
            :default-openeds="['function', 'theme']"
            router
            :class="['custom-menu', {'menu-dark': isChange, 'menu-light': !isChange}]"
            :collapse-transition="false">
            
            <el-menu-item index="/home" class="menu-item">
              <div class="menu-item-content">
                <i class="el-icon-s-home"></i>
                <span class="menu-text">首页</span>
              </div>
              <div class="menu-badge"></div>
            </el-menu-item>

            <el-submenu index="function" class="menu-group">
              <template slot="title">
                <div class="menu-group-title">
                  <i class="el-icon-s-operation"></i>
                  <span>功能管理</span>
                </div>
              </template>
              <el-menu-item index="/user" class="submenu-item">
                <div class="submenu-content">
                  <i class="el-icon-user"></i>
                  <span>用户基本信息</span>
                </div>
              </el-menu-item>
              <el-menu-item index="/user" class="submenu-item">
                <div class="submenu-content">
                  <i class="el-icon-edit"></i>
                  <span>待修改功能</span>
                </div>
              </el-menu-item>
            </el-submenu>

            <el-submenu index="theme" class="menu-group">
              <template slot="title">
                <div class="menu-group-title">
                  <i class="el-icon-picture-outline"></i>
                  <span>背景切换</span>
                </div>
              </template>
              <el-menu-item class="submenu-item" @click="switchTheme(0)">
                <div class="submenu-content">
                  <div class="theme-preview theme-1"></div>
                  <span>现代简约</span>
                </div>
                <i v-if="!isChange" class="el-icon-check theme-check"></i>
              </el-menu-item>
              <el-menu-item class="submenu-item" @click="switchTheme(1)">
                <div class="submenu-content">
                  <div class="theme-preview theme-2"></div>
                  <span>科技深色</span>
                </div>
                <i v-if="isChange" class="el-icon-check theme-check"></i>
              </el-menu-item>
            </el-submenu>

            <div class="menu-footer">
              <div class="system-info">
                <i class="el-icon-info"></i>
                <span>系统版本: v2.1.0</span>
              </div>
            </div>
          </el-menu>
        </div>
      </aside>

      <!-- 主内容区 -->
      <section class="main-content">
        <div class="content-wrapper">
          <div class="content-header">
            <h2 class="page-title">
              <i class="el-icon-s-order"></i>
              <span>{{ $route.meta.name || '工作台' }}</span>
            </h2>
            <div class="page-tools">
              <el-button type="primary" icon="el-icon-refresh">刷新</el-button>
              <el-button type="success" icon="el-icon-download">导出</el-button>
            </div>
          </div>
          
          <div class="content-body">
            <router-view @update:user="updateUser" />
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      isChange: false,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    if (!this.user.id) {
      this.$router.push('/login')
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
    },
    handleCommand(command) {
      if (command === 'logout') {
        this.logout();
      } else if (command === 'calendar') {
        this.$message.info('日历功能开发中...');
      } else if (command === 'profile') {
        this.$router.push('/profile');
      } else if (command === 'settings') {
        this.$message.info('系统设置开发中...');
      }
    },
    logout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        customClass: 'logout-confirm'
      }).then(() => {
        localStorage.removeItem('xm-user');
        this.$router.push('/login');
        this.$message.success('已成功退出登录');
      }).catch(() => {});
    },
    switchTheme(num) {
      this.isChange = num === 1;
    },
    toggleTheme() {
      // 切换主题
    },
  }
}
</script>

<style scoped>
/* 全局容器 */
.manager-container {
  min-height: 100vh;
  transition: all 0.3s ease;
}

/* 主题样式 */
.theme-light {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

.theme-dark {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
  color: #ecf0f1;
}

/* 头部导航栏 */
.manager-header {
  height: 64px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 1000;
  transition: all 0.3s ease;
}

.theme-header-light {
  background: linear-gradient(90deg, #ffffff 0%, #f8f9fa 100%);
  border-bottom: 1px solid #e9ecef;
}

.theme-header-dark {
  background: linear-gradient(90deg, #2c3e50 0%, #34495e 100%);
  border-bottom: 1px solid #1a252f;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 24px;
  max-width: 100%;
  margin: 0 auto;
}

/* Logo区域 */
.header-left {
  flex: 0 0 auto;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.logo-section:hover {
  transform: translateY(-2px);
}

.logo-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.theme-header-light .logo-icon {
  background: linear-gradient(135deg, #f96332 0%, #ff7b4a 100%);
  color: white;
}

.theme-header-dark .logo-icon {
  background: linear-gradient(135deg, #00b4db 0%, #0083b0 100%);
  color: white;
}

.logo-title {
  font-size: 20px;
  font-weight: 700;
  margin: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: 1px;
}

.theme-header-dark .logo-title {
  background: linear-gradient(135deg, #00b4db 0%, #0083b0 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* 面包屑导航 */
.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
  padding: 0 20px;
}

.breadcrumb-container {
  max-width: 600px;
  width: 100%;
}

::v-deep .el-breadcrumb {
  font-size: 14px;
}

::v-deep .el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #409eff;
  font-weight: 600;
}

.theme-header-dark ::v-deep .el-breadcrumb__inner {
  color: #bdc3c7;
}

.theme-header-dark ::v-deep .el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #00b4db;
}

::v-deep .el-breadcrumb__separator {
  color: #c0c4cc;
}

.current-page {
  font-weight: 600;
}

/* 用户区域 */
.header-right {
  flex: 0 0 auto;
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
}

.theme-header-light .user-profile:hover {
  background: #f8f9fa;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.theme-header-dark .user-profile:hover {
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.avatar-container {
  position: relative;
}

.avatar-placeholder {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.theme-header-light .avatar-placeholder {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.theme-header-dark .avatar-placeholder {
  background: linear-gradient(135deg, #00b4db 0%, #0083b0 100%);
  color: white;
}

.user-status {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 10px;
  height: 10px;
  background: #52c41a;
  border-radius: 50%;
  border: 2px solid white;
  box-shadow: 0 0 0 2px #52c41a33;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.user-name {
  font-weight: 600;
  font-size: 14px;
}

.theme-header-light .user-name {
  color: #333;
}

.theme-header-dark .user-name {
  color: white;
}

.user-role {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

.dropdown-arrow {
  color: #909399;
  font-size: 12px;
  transition: transform 0.3s ease;
}

.user-dropdown:hover .dropdown-arrow {
  transform: rotate(180deg);
}

.user-menu {
  border-radius: 12px !important;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15) !important;
  border: 1px solid #f0f0f0 !important;
  padding: 8px 0 !important;
}

.menu-item {
  padding: 12px 20px !important;
  display: flex !important;
  align-items: center !important;
  gap: 12px !important;
  transition: all 0.3s ease !important;
  border-radius: 8px !important;
  margin: 4px 8px !important;
}

.menu-item:hover {
  background: #f5f7fa !important;
  transform: translateX(4px) !important;
}

/* 侧边栏 */
.manager-main {
  display: flex;
  min-height: calc(100vh - 64px);
}

.sidebar {
  width: 240px;
  transition: all 0.3s ease;
  position: relative;
  z-index: 100;
}

.sidebar-light {
  background: white;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.05);
  border-right: 1px solid #e9ecef;
}

.sidebar-dark {
  background: linear-gradient(135deg, #1a252f 0%, #2c3e50 100%);
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.2);
  border-right: 1px solid #34495e;
}

.sidebar-header {
  padding: 20px 16px;
  border-bottom: 1px solid;
}

.sidebar-light .sidebar-header {
  border-color: #e9ecef;
}

.sidebar-dark .sidebar-header {
  border-color: #34495e;
}

.sidebar-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
}

.sidebar-light .sidebar-title {
  color: #333;
}

.sidebar-dark .sidebar-title {
  color: white;
}

.sidebar-title i {
  font-size: 18px;
}

.theme-toggle {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f8f9fa;
  padding: 8px 12px;
  border-radius: 8px;
}

.sidebar-dark .theme-toggle {
  background: rgba(255, 255, 255, 0.1);
}

.theme-label {
  font-size: 12px;
  color: #666;
}

.sidebar-dark .theme-label {
  color: #bdc3c7;
}

::v-deep .el-switch__label {
  color: #666 !important;
}

.sidebar-dark ::v-deep .el-switch__label {
  color: #bdc3c7 !important;
}

::v-deep .el-switch__label.is-active {
  color: #409eff !important;
}

.sidebar-content {
  height: calc(100vh - 180px);
  overflow-y: auto;
  padding: 16px 0;
}

.custom-menu {
  border-right: none !important;
}

.menu-light {
  background: white !important;
}

.menu-dark {
  background: transparent !important;
}

.menu-item {
  height: 48px !important;
  line-height: 48px !important;
  margin: 4px 8px !important;
  border-radius: 8px !important;
  transition: all 0.3s ease !important;
}

.menu-light .menu-item:hover {
  background: linear-gradient(90deg, #f8f9fa 0%, #e9ecef 100%) !important;
  color: #f96332 !important;
}

.menu-dark .menu-item:hover {
  background: rgba(255, 255, 255, 0.1) !important;
  color: #00b4db !important;
}

.menu-item-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.menu-text {
  font-weight: 500;
}

.menu-badge {
  width: 8px;
  height: 8px;
  background: #ff4757;
  border-radius: 50%;
  margin-left: auto;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { opacity: 1; }
  50% { opacity: 0.3; }
  100% { opacity: 1; }
}

.menu-group-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 600;
}

.menu-light .menu-group-title {
  color: #333;
}

.menu-dark .menu-group-title {
  color: #ecf0f1;
}

.submenu-item {
  height: 40px !important;
  line-height: 40px !important;
  margin: 2px 8px 2px 32px !important;
  border-radius: 6px !important;
}

.menu-light .submenu-item:hover {
  background: #f8f9fa !important;
  color: #f96332 !important;
}

.menu-dark .submenu-item:hover {
  background: rgba(255, 255, 255, 0.05) !important;
  color: #00b4db !important;
}

.submenu-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.theme-preview {
  width: 20px;
  height: 20px;
  border-radius: 4px;
  border: 2px solid #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.theme-1 {
  background: linear-gradient(135deg, #f96332 0%, #ff7b4a 100%);
}

.theme-2 {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
}

.theme-check {
  color: #52c41a;
  font-size: 16px;
  margin-left: auto;
}

.menu-footer {
  margin-top: auto;
  padding: 20px 16px;
  border-top: 1px solid;
}

.menu-light .menu-footer {
  border-color: #e9ecef;
}

.menu-dark .menu-footer {
  border-color: #34495e;
}

.system-info {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 12px;
  color: #909399;
}

.menu-dark .system-info {
  color: #95a5a6;
}

/* 主内容区 */
.main-content {
  flex: 1;
  padding: 24px;
  overflow: auto;
}

.content-wrapper {
  max-width: 1400px;
  margin: 0 auto;
}

.content-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
}

.theme-dark .content-header {
  border-color: #34495e;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.theme-light .page-title {
  color: #333;
}

.theme-dark .page-title {
  color: white;
}

.page-tools {
  display: flex;
  gap: 12px;
}

.content-body {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 24px;
  min-height: 600px;
}

.theme-dark .content-body {
  background: rgba(255, 255, 255, 0.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .header-content {
    padding: 0 16px;
  }
  
  .logo-title {
    font-size: 16px;
  }
  
  .header-center {
    display: none;
  }
  
  .sidebar {
    position: fixed;
    left: -240px;
    top: 64px;
    bottom: 0;
    transition: left 0.3s ease;
    z-index: 1000;
  }
  
  .sidebar.active {
    left: 0;
  }
  
  .main-content {
    padding: 16px;
  }
  
  .content-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .page-tools {
    width: 100%;
    justify-content: flex-end;
  }
}

/* 自定义确认框样式 */
.logout-confirm {
  border-radius: 12px !important;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15) !important;
}

.logout-confirm .el-message-box__header {
  background: linear-gradient(135deg, #f96332 0%, #ff7b4a 100%);
  color: white;
  border-radius: 12px 12px 0 0;
}

.logout-confirm .el-message-box__title {
  color: white;
}

.logout-confirm .el-message-box__content {
  padding: 24px;
}

.logout-confirm .el-message-box__btns {
  padding: 16px 24px;
  border-top: 1px solid #e9ecef;
}
</style>