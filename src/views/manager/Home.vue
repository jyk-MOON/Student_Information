<template>
  <div class="home-container">
    <!-- 顶部欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="greeting">
          <h1 class="greeting-title">
            <span class="greeting-text">您好，</span>
            <span class="user-name">{{ user.name }}</span>
            <span class="greeting-emoji">👋</span>
          </h1>
          <p class="greeting-subtitle">欢迎回到您的个人空间</p>
          <p class="greeting-time">{{ currentTime }}</p>
        </div>
        
        <div class="quick-stats">
          <div class="stats-item">
            <div class="stats-icon">
              <i class="el-icon-date"></i>
            </div>
            <div class="stats-info">
              <span class="stats-label">注册时间</span>
              <span class="stats-value">{{ formatDate(user.createTime) }}</span>
            </div>
          </div>
          <div class="stats-item">
            <div class="stats-icon">
              <i class="el-icon-star-off"></i>
            </div>
            <div class="stats-info">
              <span class="stats-label">账户等级</span>
              <span class="stats-value">{{ userLevel }}</span>
            </div>
          </div>
          <div class="stats-item">
            <div class="stats-icon">
              <i class="el-icon-medal"></i>
            </div>
            <div class="stats-info">
              <span class="stats-label">账户状态</span>
              <span class="stats-value status-active">正常</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <div class="profile-overview">
        <!-- 左侧：用户信息卡片 -->
        <div class="profile-card-wrapper">
          <el-card class="profile-card" shadow="hover">
            <div class="avatar-section">
              <div class="avatar-container">
                <el-avatar 
                  :size="160" 
                  :src="user.avatar" 
                  class="user-avatar">
                  {{ user.name?.charAt(0) }}
                </el-avatar>
                <div class="avatar-status" :class="{'status-online': isOnline}"></div>
              </div>
              <div class="avatar-actions">
                <el-button 
                  type="text" 
                  icon="el-icon-camera" 
                  @click="changeAvatar">
                  更换头像
                </el-button>
              </div>
            </div>
            
            <div class="user-basic-info">
              <h2 class="user-fullname">{{ user.name }}</h2>
              <p class="user-bio" v-if="user.bio">{{ user.bio }}</p>
              <p class="user-bio-placeholder" v-else>这个人很懒，什么都没留下</p>
              
              <div class="user-tags">
                <el-tag 
                  v-for="tag in user.tags" 
                  :key="tag"
                  size="small"
                  class="user-tag">
                  {{ tag }}
                </el-tag>
                <el-tag 
                  v-if="!user.tags?.length"
                  size="small"
                  type="info">
                  未设置标签
                </el-tag>
              </div>
            </div>
            
            <div class="social-links">
              <el-button 
                type="text" 
                icon="el-icon-message" 
                class="social-btn"
                @click="sendMessage">
                发送消息
              </el-button>
              <el-button 
                type="text" 
                icon="el-icon-share" 
                class="social-btn"
                @click="shareProfile">
                分享资料
              </el-button>
              <el-button 
                type="text" 
                icon="el-icon-edit" 
                class="social-btn"
                @click="editProfile">
                编辑资料
              </el-button>
            </div>
          </el-card>
        </div>

        <!-- 右侧：详细信息区域 -->
        <div class="details-section">
          <!-- 用户信息卡片 -->
          <el-card class="info-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <h3 class="card-title">
                  <i class="el-icon-user card-title-icon"></i>
                  个人信息
                </h3>
                <el-button 
                  type="text" 
                  icon="el-icon-edit" 
                  @click="editProfile"
                  class="edit-btn">
                  编辑
                </el-button>
              </div>
            </template>
            
            <div class="info-grid">
              <div class="info-item">
                <div class="info-icon">
                  <i class="el-icon-user"></i>
                </div>
                <div class="info-content">
                  <span class="info-label">用户名</span>
                  <span class="info-value">{{ user.name || '未设置' }}</span>
                </div>
              </div>
              
              <div class="info-item">
                <div class="info-icon">
                  <i class="el-icon-message"></i>
                </div>
                <div class="info-content">
                  <span class="info-label">邮箱</span>
                  <span class="info-value">{{ user.email || '未设置' }}</span>
                  <el-tag 
                    v-if="user.emailVerified" 
                    size="mini" 
                    type="success"
                    class="verified-tag">
                    已验证
                  </el-tag>
                </div>
              </div>
              
              <div class="info-item">
                <div class="info-icon">
                  <i class="el-icon-phone"></i>
                </div>
                <div class="info-content">
                  <span class="info-label">手机号</span>
                  <span class="info-value">{{ user.phone || '未绑定' }}</span>
                </div>
              </div>
              
              <div class="info-item">
                <div class="info-icon">
                  <i class="el-icon-date"></i>
                </div>
                <div class="info-content">
                  <span class="info-label">生日</span>
                  <span class="info-value">{{ formatBirthday(user.birthday) }}</span>
                </div>
              </div>
              
              <div class="info-item">
                <div class="info-icon">
                  <i class="el-icon-location-information"></i>
                </div>
                <div class="info-content">
                  <span class="info-label">所在地</span>
                  <span class="info-value">{{ user.location || '未设置' }}</span>
                </div>
              </div>
              
              <div class="info-item">
                <div class="info-icon">
                  <i class="el-icon-office-building"></i>
                </div>
                <div class="info-content">
                  <span class="info-label">职业</span>
                  <span class="info-value">{{ user.job || '未设置' }}</span>
                </div>
              </div>
            </div>
          </el-card>

          <!-- 账户信息卡片 -->
          <el-card class="info-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <h3 class="card-title">
                  <i class="el-icon-wallet card-title-icon"></i>
                  账户信息
                </h3>
                <el-button 
                  type="text" 
                  icon="el-icon-refresh" 
                  @click="refreshBalance"
                  class="refresh-btn">
                  刷新
                </el-button>
              </div>
            </template>
            
            <div class="balance-section">
              <div class="balance-display">
                <div class="balance-label">账户余额</div>
                <div class="balance-amount">
                  <span class="currency">¥</span>
                  <span class="amount">{{ formatMoney(user.money) }}</span>
                </div>
                <div class="balance-change">
                  <span class="change-label">今日变动</span>
                  <span class="change-value positive">+ ¥0.00</span>
                </div>
              </div>
              
              <div class="balance-actions">
                <el-button 
                  type="primary" 
                  icon="el-icon-plus" 
                  class="action-btn"
                  @click="recharge">
                  充值
                </el-button>
                <el-button 
                  type="success" 
                  icon="el-icon-download" 
                  class="action-btn"
                  @click="withdraw">
                  提现
                </el-button>
                <el-button 
                  type="info" 
                  icon="el-icon-document" 
                  class="action-btn"
                  @click="viewRecords">
                  明细
                </el-button>
              </div>
            </div>
            
            <div class="account-stats">
              <div class="stat-item">
                <div class="stat-value">0</div>
                <div class="stat-label">待处理订单</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">0</div>
                <div class="stat-label">优惠券</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">0</div>
                <div class="stat-label">积分</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">100%</div>
                <div class="stat-label">完成度</div>
              </div>
            </div>
          </el-card>

          <!-- 快捷操作卡片 -->
          <el-card class="info-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <h3 class="card-title">
                  <i class="el-icon-s-operation card-title-icon"></i>
                  快捷操作
                </h3>
              </div>
            </template>
            
            <div class="quick-actions-grid">
              <div 
                v-for="action in quickActions" 
                :key="action.id"
                class="quick-action-item"
                @click="action.handler">
                <div class="action-icon" :style="{backgroundColor: action.color}">
                  <i :class="action.icon"></i>
                </div>
                <div class="action-info">
                  <span class="action-title">{{ action.title }}</span>
                  <span class="action-desc">{{ action.desc }}</span>
                </div>
                <i class="el-icon-arrow-right action-arrow"></i>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>

    <!-- 页面底部 -->
    <div class="page-footer">
      <p class="footer-text">最后更新：{{ lastUpdated }}</p>
      <p class="footer-hint">
        <i class="el-icon-info"></i>
        您的个人信息受到严格保护，仅用于为您提供更好的服务
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      currentTime: '',
      lastUpdated: '',
      isOnline: true,
      userLevel: 'VIP 1',
      quickActions: [
        {
          id: 1,
          icon: 'el-icon-setting',
          title: '账户设置',
          desc: '修改密码和安全设置',
          color: '#409EFF',
          handler: () => this.goToSettings()
        },
        {
          id: 2,
          icon: 'el-icon-document',
          title: '我的订单',
          desc: '查看和管理订单',
          color: '#67C23A',
          handler: () => this.viewOrders()
        },
        {
          id: 3,
          icon: 'el-icon-star-off',
          title: '收藏夹',
          desc: '查看收藏的内容',
          color: '#E6A23C',
          handler: () => this.viewFavorites()
        },
        {
          id: 4,
          icon: 'el-icon-chat-dot-round',
          title: '消息中心',
          desc: '查看未读消息',
          color: '#F56C6C',
          handler: () => this.viewMessages()
        },
        {
          id: 5,
          icon: 'el-icon-tickets',
          title: '浏览记录',
          desc: '查看最近访问',
          color: '#909399',
          handler: () => this.viewHistory()
        },
        {
          id: 6,
          icon: 'el-icon-headset',
          title: '帮助中心',
          desc: '获取帮助和支持',
          color: '#8A2BE2',
          handler: () => this.viewHelp()
        }
      ]
    }
  },
  
  mounted() {
    this.updateTime()
    this.setLastUpdated()
    
    // 更新时间每秒钟更新一次
    setInterval(() => {
      this.updateTime()
    }, 1000)
  },
  
  methods: {
    updateTime() {
      const now = new Date()
      this.currentTime = now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
      })
    },
    
    setLastUpdated() {
      const now = new Date()
      this.lastUpdated = now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    },
    
    formatDate(date) {
      if (!date) return '未知'
      return new Date(date).toLocaleDateString('zh-CN')
    },
    
    formatBirthday(birthday) {
      if (!birthday) return '未设置'
      return new Date(birthday).toLocaleDateString('zh-CN')
    },
    
    formatMoney(amount) {
      if (!amount && amount !== 0) return '0.00'
      return parseFloat(amount).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    },
    
    changeAvatar() {
      this.$message.info('更换头像功能开发中')
    },
    
    editProfile() {
      this.$message.info('编辑资料功能开发中')
    },
    
    sendMessage() {
      this.$message.info('发送消息功能开发中')
    },
    
    shareProfile() {
      this.$message.info('分享资料功能开发中')
    },
    
    recharge() {
      this.$message.info('充值功能开发中')
    },
    
    withdraw() {
      this.$message.info('提现功能开发中')
    },
    
    viewRecords() {
      this.$message.info('查看明细功能开发中')
    },
    
    refreshBalance() {
      this.$message.success('余额已刷新')
    },
    
    goToSettings() {
      this.$router.push('/settings')
    },
    
    viewOrders() {
      this.$router.push('/orders')
    },
    
    viewFavorites() {
      this.$router.push('/favorites')
    },
    
    viewMessages() {
      this.$router.push('/messages')
    },
    
    viewHistory() {
      this.$router.push('/history')
    },
    
    viewHelp() {
      this.$router.push('/help')
    }
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  padding: 20px;
}

/* 欢迎区域 */
.welcome-section {
  margin-bottom: 30px;
}

.welcome-content {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 30px;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.greeting-title {
  font-size: 36px;
  font-weight: 600;
  margin: 0 0 10px 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.greeting-text {
  opacity: 0.9;
}

.user-name {
  color: #ffd700;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.greeting-emoji {
  font-size: 32px;
}

.greeting-subtitle {
  font-size: 16px;
  opacity: 0.8;
  margin: 0 0 8px 0;
}

.greeting-time {
  font-size: 14px;
  opacity: 0.7;
  margin: 0;
}

.quick-stats {
  display: flex;
  gap: 30px;
}

.stats-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.stats-icon {
  width: 48px;
  height: 48px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stats-icon i {
  font-size: 24px;
  color: white;
}

.stats-info {
  display: flex;
  flex-direction: column;
}

.stats-label {
  font-size: 12px;
  opacity: 0.8;
  margin-bottom: 4px;
}

.stats-value {
  font-size: 16px;
  font-weight: 600;
}

.status-active {
  color: #67C23A;
}

/* 主要内容区域 */
.profile-overview {
  display: flex;
  gap: 24px;
  margin-bottom: 30px;
}

.profile-card-wrapper {
  flex: 3;
}

.details-section {
  flex: 7;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 个人信息卡片 */
.profile-card {
  border-radius: 12px;
  overflow: hidden;
}

.avatar-section {
  text-align: center;
  margin-bottom: 24px;
  position: relative;
}

.avatar-container {
  position: relative;
  display: inline-block;
}

.user-avatar {
  border: 4px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  margin-bottom: 16px;
}

.avatar-status {
  position: absolute;
  bottom: 20px;
  right: 20px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  border: 3px solid white;
  background: #909399;
}

.status-online {
  background: #67C23A;
}

.avatar-actions {
  margin-top: 12px;
}

.user-basic-info {
  text-align: center;
  margin-bottom: 24px;
}

.user-fullname {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 12px 0;
}

.user-bio {
  color: #606266;
  font-size: 14px;
  line-height: 1.6;
  margin: 0 0 16px 0;
  max-width: 300px;
  margin-left: auto;
  margin-right: auto;
}

.user-bio-placeholder {
  color: #909399;
  font-style: italic;
  font-size: 14px;
  margin: 0 0 16px 0;
}

.user-tags {
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.social-links {
  display: flex;
  justify-content: center;
  gap: 16px;
  border-top: 1px solid #e4e7ed;
  padding-top: 20px;
}

.social-btn {
  flex: 1;
  max-width: 100px;
}

/* 详细信息卡片 */
.info-card {
  border-radius: 12px;
  border: none;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 !important;
  border-bottom: none !important;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.card-title-icon {
  color: #409EFF;
  font-size: 20px;
}

.edit-btn, .refresh-btn {
  color: #409EFF;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}

.info-item:hover {
  background-color: #f5f7fa;
}

.info-icon {
  width: 40px;
  height: 40px;
  background: rgba(64, 158, 255, 0.1);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.info-icon i {
  font-size: 20px;
  color: #409EFF;
}

.info-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-label {
  font-size: 12px;
  color: #909399;
}

.info-value {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.verified-tag {
  margin-left: 8px;
}

/* 账户信息卡片 */
.balance-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.balance-display {
  margin-bottom: 20px;
}

.balance-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.balance-amount {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 12px;
}

.currency {
  font-size: 24px;
  color: #303133;
  font-weight: 500;
}

.amount {
  font-size: 40px;
  font-weight: 700;
  color: #E6A23C;
}

.balance-change {
  display: flex;
  align-items: center;
  gap: 12px;
}

.change-label {
  font-size: 14px;
  color: #909399;
}

.change-value {
  font-size: 16px;
  font-weight: 600;
}

.change-value.positive {
  color: #67C23A;
}

.balance-actions {
  display: flex;
  gap: 12px;
}

.action-btn {
  flex: 1;
}

.account-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.stat-item {
  text-align: center;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
  transition: transform 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-2px);
  background: #ebf1f7;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #409EFF;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

/* 快捷操作卡片 */
.quick-actions-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.quick-action-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  cursor: pointer;
  transition: all 0.3s ease;
}

.quick-action-item:hover {
  background-color: #f5f7fa;
  border-color: #409EFF;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.action-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-icon i {
  font-size: 24px;
  color: white;
}

.action-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.action-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.action-desc {
  font-size: 12px;
  color: #909399;
}

.action-arrow {
  color: #909399;
  font-size: 14px;
}

/* 页面底部 */
.page-footer {
  text-align: center;
  padding: 20px;
  margin-top: 30px;
  border-top: 1px solid #e4e7ed;
}

.footer-text {
  color: #909399;
  font-size: 14px;
  margin: 0 0 8px 0;
}

.footer-hint {
  color: #606266;
  font-size: 12px;
  margin: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.footer-hint i {
  color: #409EFF;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .profile-overview {
    flex-direction: column;
  }
  
  .quick-stats {
    flex-direction: column;
    gap: 20px;
  }
}

@media (max-width: 768px) {
  .welcome-content {
    flex-direction: column;
    gap: 24px;
  }
  
  .greeting-title {
    font-size: 28px;
  }
  
  .info-grid,
  .account-stats,
  .quick-actions-grid {
    grid-template-columns: 1fr;
  }
  
  .balance-actions {
    flex-direction: column;
  }
  
  .home-container {
    padding: 12px;
  }
}

@media (max-width: 480px) {
  .greeting-title {
    font-size: 24px;
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .user-avatar {
    width: 120px !important;
    height: 120px !important;
  }
  
  .amount {
    font-size: 32px;
  }
}
</style>