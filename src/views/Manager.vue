<template>
  <div class="admin-container" :class="{'theme-light': theme === 'light', 'theme-dark': theme === 'dark'}">
    <!-- 顶部导航栏 -->
    <header class="admin-header">
      <div class="header-content">
        <!-- Logo区域 -->
        <div class="logo-area">
          <div class="logo-wrapper">
            <i class="el-icon-s-platform logo-icon"></i>
            <h1 class="system-title">Web管理系统</h1>
          </div>
        </div>

        <!-- 面包屑导航 -->
        <div class="breadcrumb-area">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/home' }">
              <i class="el-icon-s-home"></i>首页
            </el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentRouteName }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <!-- 用户操作区 -->
        <div class="user-actions">
          <!-- 主题切换 -->
          <el-tooltip content="切换主题" placement="bottom">
            <el-switch
              v-model="theme"
              active-value="dark"
              inactive-value="light"
              active-color="#2c3e50"
              inactive-color="#409EFF"
              @change="toggleTheme">
            </el-switch>
          </el-tooltip>

          <!-- 用户信息 -->
          <el-dropdown @command="handleCommand" trigger="click">
            <div class="user-profile">
              <el-avatar :size="36" :src="user.avatar" class="user-avatar">
                {{ user.name?.charAt(0) }}
              </el-avatar>
              <div class="user-info">
                <span class="user-name">{{ user.name }}</span>
                <span class="user-role">管理员</span>
              </div>
              <i class="el-icon-arrow-down arrow-down"></i>
            </div>
            
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">
                <i class="el-icon-user"></i>个人资料
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <i class="el-icon-setting"></i>系统设置
              </el-dropdown-item>
              <el-dropdown-item divided command="calendar">
                <i class="el-icon-date"></i>日历视图
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <i class="el-icon-switch-button"></i>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </header>

    <!-- 主体内容区域 -->
    <div class="admin-main">
      <!-- 侧边栏 -->
      <aside class="admin-sidebar">
        <div class="sidebar-header">
          <h3><i class="el-icon-menu"></i> 导航菜单</h3>
        </div>
        
        <el-menu
          :default-active="activeMenu"
          router
          class="sidebar-menu"
          background-color="transparent"
          text-color="var(--text-color)"
          active-text-color="var(--primary-color)">
          
          <!-- 首页 -->
          <el-menu-item index="/home">
            <i class="el-icon-s-home"></i>
            <span>控制台</span>
          </el-menu-item>

          <!-- 功能管理 -->
          <el-submenu index="function">
            <template slot="title">
              <i class="el-icon-s-operation"></i>
              <span>功能管理</span>
            </template>
            <el-menu-item index="/user">
              <i class="el-icon-user"></i>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/department">
              <i class="el-icon-office-building"></i>
              <span>部门管理</span>
            </el-menu-item>
            <el-menu-item index="/permission">
              <i class="el-icon-lock"></i>
              <span>权限管理</span>
            </el-menu-item>
          </el-submenu>

          <!-- 系统设置 -->
          <el-submenu index="system">
            <template slot="title">
              <i class="el-icon-s-tools"></i>
              <span>系统设置</span>
            </template>
            <el-menu-item index="/config">
              <i class="el-icon-setting"></i>
              <span>系统配置</span>
            </el-menu-item>
            <el-menu-item index="/theme">
              <i class="el-icon-picture-outline"></i>
              <span>主题设置</span>
            </el-menu-item>
            <el-menu-item index="/backup">
              <i class="el-icon-folder-checked"></i>
              <span>数据备份</span>
            </el-menu-item>
          </el-submenu>

          <!-- 快捷操作 -->
          <div class="quick-actions">
            <div class="section-title">快捷操作</div>
            <el-button 
              type="primary" 
              icon="el-icon-plus" 
              class="quick-btn"
              @click="handleQuickAction('add')">
              新建
            </el-button>
            <el-button 
              type="success" 
              icon="el-icon-refresh" 
              class="quick-btn"
              @click="handleQuickAction('refresh')">
              刷新
            </el-button>
          </div>
        </el-menu>

        <!-- 底部信息 -->
        <div class="sidebar-footer">
          <p>© 2024 Web系统</p>
          <p>版本 v2.0.0</p>
        </div>
      </aside>

      <!-- 内容区域 -->
      <main class="admin-content">
        <div class="content-wrapper">
          <!-- 页面标题 -->
          <div class="page-header">
            <h2 class="page-title">{{ currentRouteName }}</h2>
            <div class="page-actions">
              <el-button 
                v-if="$route.path !== '/home'"
                type="primary" 
                icon="el-icon-plus"
                @click="handleCreate">
                新建
              </el-button>
              <el-button 
                icon="el-icon-download"
                @click="handleExport">
                导出
              </el-button>
            </div>
          </div>

          <!-- 页面内容 -->
          <div class="page-content">
            <router-view @update:user="updateUser" />
          </div>
        </div>
      </main>
    </div>

    <!-- 全局提示 -->
    <div class="global-notice" v-if="showNotice">
      <i class="el-icon-info"></i>
      <span>系统将于今晚24:00进行维护，请提前保存工作</span>
      <i class="el-icon-close close-notice" @click="showNotice = false"></i>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminLayout",
  
  data() {
    return {
      theme: 'light',
      activeMenu: '/home',
      showNotice: true,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}')
    }
  },
  
  computed: {
    currentRouteName() {
      return this.$route.meta?.name || this.$route.name || '控制台'
    }
  },
  
  created() {
    if (!this.user.id) {
      this.$router.push('/login')
    }
    this.setTheme()
  },
  
  watch: {
    '$route.path': {
      immediate: true,
      handler(newPath) {
        this.activeMenu = newPath
      }
    }
  },
  
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
    },
    
    toggleTheme() {
      const theme = this.theme
      localStorage.setItem('theme', theme)
      this.setTheme()
    },
    
    setTheme() {
      const savedTheme = localStorage.getItem('theme') || 'light'
      this.theme = savedTheme
      document.documentElement.setAttribute('data-theme', savedTheme)
    },
    
    handleCommand(command) {
      switch (command) {
        case 'logout':
          this.logout()
          break
        case 'profile':
          this.$router.push('/profile')
          break
        case 'settings':
          this.$router.push('/settings')
          break
        case 'calendar':
          this.$router.push('/calendar')
          break
      }
    },
    
    logout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('xm-user')
        this.$message.success('退出成功')
        this.$router.push('/login')
      }).catch(() => {})
    },
    
    handleCreate() {
      this.$emit('create')
    },
    
    handleExport() {
      this.$emit('export')
    },
    
    handleQuickAction(action) {
      if (action === 'add') {
        this.handleCreate()
      } else if (action === 'refresh') {
        window.location.reload()
      }
    }
  }
}
</script>

<style scoped>
:root {
  --primary-color: #409EFF;
  --secondary-color: #67C23A;
  --danger-color: #F56C6C;
  --warning-color: #E6A23C;
  --info-color: #909399;
  
  --bg-color: #f5f7fa;
  --sidebar-bg: #ffffff;
  --header-bg: #ffffff;
  --card-bg: #ffffff;
  
  --text-color: #303133;
  --text-secondary: #606266;
  --border-color: #e4e7ed;
  
  --shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  --shadow-light: 0 1px 3px rgba(0, 0, 0, 0.12);
}

[data-theme="dark"] {
  --primary-color: #409EFF;
  --secondary-color: #67C23A;
  
  --bg-color: #141414;
  --sidebar-bg: #1f1f1f;
  --header-bg: #1f1f1f;
  --card-bg: #2d2d2d;
  
  --text-color: #e5e5e5;
  --text-secondary: #b0b0b0;
  --border-color: #434343;
  
  --shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.3);
  --shadow-light: 0 1px 3px rgba(0, 0, 0, 0.25);
}

.admin-container {
  min-height: 100vh;
  background-color: var(--bg-color);
  transition: all 0.3s ease;
}

/* 顶部导航栏 */
.admin-header {
  background-color: var(--header-bg);
  box-shadow: var(--shadow-light);
  height: 64px;
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  max-width: 100%;
  height: 100%;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo-area {
  flex: 0 0 200px;
}

.logo-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 28px;
  color: var(--primary-color);
}

.system-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-color);
  margin: 0;
}

.breadcrumb-area {
  flex: 1;
  display: flex;
  justify-content: center;
}

.user-actions {
  flex: 0 0 200px;
  display: flex;
  align-items: center;
  gap: 20px;
  justify-content: flex-end;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 12px;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.user-profile:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

[data-theme="dark"] .user-profile:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-color);
}

.user-role {
  font-size: 12px;
  color: var(--text-secondary);
}

.arrow-down {
  color: var(--text-secondary);
  font-size: 12px;
  transition: transform 0.3s ease;
}

.user-profile:hover .arrow-down {
  transform: rotate(180deg);
}

/* 主体布局 */
.admin-main {
  display: flex;
  min-height: calc(100vh - 64px);
}

/* 侧边栏 */
.admin-sidebar {
  width: 240px;
  background-color: var(--sidebar-bg);
  box-shadow: var(--shadow-light);
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
}

.sidebar-header {
  padding: 20px 24px;
  border-bottom: 1px solid var(--border-color);
}

.sidebar-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-color);
  display: flex;
  align-items: center;
  gap: 8px;
}

.sidebar-menu {
  flex: 1;
  border-right: none !important;
  padding: 16px 0;
}

.sidebar-menu .el-menu-item,
.sidebar-menu .el-submenu__title {
  height: 48px;
  line-height: 48px;
  margin: 4px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.sidebar-menu .el-menu-item:hover,
.sidebar-menu .el-submenu__title:hover {
  background-color: rgba(64, 158, 255, 0.1) !important;
}

.sidebar-menu .el-menu-item.is-active {
  background-color: rgba(64, 158, 255, 0.15) !important;
}

.sidebar-menu i {
  font-size: 18px;
  margin-right: 12px;
}

.quick-actions {
  padding: 20px 24px;
  border-top: 1px solid var(--border-color);
}

.section-title {
  font-size: 12px;
  color: var(--text-secondary);
  margin-bottom: 12px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.quick-btn {
  width: 100%;
  margin-bottom: 8px;
}

.sidebar-footer {
  padding: 16px 24px;
  border-top: 1px solid var(--border-color);
  text-align: center;
  font-size: 12px;
  color: var(--text-secondary);
}

.sidebar-footer p {
  margin: 4px 0;
}

/* 内容区域 */
.admin-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}

.content-wrapper {
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border-color);
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: var(--text-color);
}

.page-actions {
  display: flex;
  gap: 12px;
}

.page-content {
  background-color: var(--card-bg);
  border-radius: 8px;
  box-shadow: var(--shadow);
  padding: 24px;
  min-height: 600px;
}

/* 全局通知 */
.global-notice {
  position: fixed;
  bottom: 24px;
  left: 50%;
  transform: translateX(-50%);
  background-color: var(--primary-color);
  color: white;
  padding: 12px 24px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: var(--shadow);
  z-index: 1001;
  animation: slideUp 0.5s ease;
}

.global-notice i {
  font-size: 18px;
}

.close-notice {
  cursor: pointer;
  opacity: 0.8;
  transition: opacity 0.3s ease;
}

.close-notice:hover {
  opacity: 1;
}

/* 动画 */
@keyframes slideUp {
  from {
    transform: translateX(-50%) translateY(100%);
    opacity: 0;
  }
  to {
    transform: translateX(-50%) translateY(0);
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 992px) {
  .admin-sidebar {
    width: 200px;
  }
  
  .logo-area {
    flex: 0 0 160px;
  }
  
  .user-actions {
    flex: 0 0 160px;
  }
}

@media (max-width: 768px) {
  .admin-sidebar {
    position: fixed;
    left: -240px;
    top: 64px;
    bottom: 0;
    z-index: 999;
  }
  
  .admin-sidebar.active {
    left: 0;
  }
  
  .header-content {
    padding: 0 16px;
  }
  
  .system-title {
    font-size: 16px;
  }
  
  .user-name {
    display: none;
  }
  
  .user-role {
    display: none;
  }
  
  .admin-content {
    padding: 16px;
  }
  
  .page-title {
    font-size: 20px;
  }
}

@media (max-width: 576px) {
  .logo-icon {
    font-size: 24px;
  }
  
  .system-title {
    display: none;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .page-actions {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>