<template>
  <div class="user-profile-container">
    <div class="profile-content">
      <!-- 左侧用户信息卡片 -->
      <div class="profile-left">
        <div class="welcome-card">
          <div class="welcome-header">
            <div class="welcome-icon">
              <i class="el-icon-sunny"></i>
            </div>
            <h1 class="welcome-title">您好，<span class="highlight">{{ userT?.name }}</span>！</h1>
            <p class="welcome-subtitle">欢迎回到您的个人空间</p>
          </div>
          
          <div class="avatar-section">
            <div class="avatar-wrapper">
              <div class="avatar-frame">
                <img :src="userT?.avatar || '/default-avatar.png'" alt="用户头像" class="user-avatar">
                <div class="avatar-status">
                  <div class="status-dot"></div>
                  <span class="status-text">在线</span>
                </div>
              </div>
              <div class="avatar-actions">
                <el-button type="primary" icon="el-icon-camera" circle @click="changeAvatar"></el-button>
                <el-button type="success" icon="el-icon-edit" circle @click="editProfile"></el-button>
              </div>
            </div>
            
            <div class="user-quick-info">
              <div class="info-item">
                <div class="info-icon">
                  <i class="el-icon-user"></i>
                </div>
                <div class="info-content">
                  <div class="info-label">用户名</div>
                  <div class="info-value">{{ userT?.name }}</div>
                </div>
              </div>
              <div class="info-item">
                <div class="info-icon">
                  <i class="el-icon-medal"></i>
                </div>
                <div class="info-content">
                  <div class="info-label">会员等级</div>
                  <div class="info-value">VIP会员</div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="stats-section">
            <h3 class="stats-title">今日数据</h3>
            <div class="stats-grid">
              <div class="stat-card stat-primary">
                <div class="stat-icon">
                  <i class="el-icon-s-data"></i>
                </div>
                <div class="stat-info">
                  <div class="stat-value">128</div>
                  <div class="stat-label">今日访问</div>
                </div>
              </div>
              <div class="stat-card stat-success">
                <div class="stat-icon">
                  <i class="el-icon-star-on"></i>
                </div>
                <div class="stat-info">
                  <div class="stat-value">89</div>
                  <div class="stat-label">累计收藏</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧详细信息 -->
      <div class="profile-right">
        <el-card class="detail-card">
          <div slot="header" class="detail-card-header">
            <div class="header-content">
              <i class="el-icon-user-solid header-icon"></i>
              <h2 class="header-title">用户详细信息</h2>
              <div class="header-badge">个人资料</div>
            </div>
          </div>
          
          <div class="detail-content">
            <!-- 基本信息部分 -->
            <div class="detail-section">
              <h3 class="section-title">
                <i class="el-icon-s-opportunity"></i>
                基本信息
              </h3>
              <div class="info-grid">
                <div class="info-item detail">
                  <div class="info-label-wrapper">
                    <div class="info-label-icon">
                      <i class="el-icon-user"></i>
                    </div>
                    <span class="info-label-text">用户名</span>
                  </div>
                  <div class="info-value-wrapper">
                    <span class="info-value">{{ userT?.name }}</span>
                    <span class="info-verified">
                      <i class="el-icon-circle-check"></i>
                      已认证
                    </span>
                  </div>
                </div>
                
                <div class="info-item detail">
                  <div class="info-label-wrapper">
                    <div class="info-label-icon">
                      <i class="el-icon-message"></i>
                    </div>
                    <span class="info-label-text">邮箱地址</span>
                  </div>
                  <div class="info-value-wrapper">
                    <span class="info-value email">{{ userT?.email }}</span>
                    <el-tag type="success" size="small" class="email-status">已验证</el-tag>
                  </div>
                </div>
              </div>
            </div>

            <!-- 财务信息部分 -->
            <div class="detail-section">
              <h3 class="section-title">
                <i class="el-icon-s-finance"></i>
                财务信息
              </h3>
              <div class="info-grid">
                <div class="info-item detail">
                  <div class="info-label-wrapper">
                    <div class="info-label-icon">
                      <i class="el-icon-money"></i>
                    </div>
                    <span class="info-label-text">账户余额</span>
                  </div>
                  <div class="info-value-wrapper">
                    <span class="info-value money">
                      ¥ {{ formatMoney(userT?.money) }}
                    </span>
                    <el-button type="text" class="recharge-btn" @click="showRecharge">
                      <i class="el-icon-plus"></i>
                      充值
                    </el-button>
                  </div>
                </div>
                
                <div class="info-item detail">
                  <div class="info-label-wrapper">
                    <div class="info-label-icon">
                      <i class="el-icon-coin"></i>
                    </div>
                    <span class="info-label-text">积分</span>
                  </div>
                  <div class="info-value-wrapper">
                    <span class="info-value points">1,248 分</span>
                    <el-progress :percentage="65" :show-text="false" class="points-progress"></el-progress>
                  </div>
                </div>
              </div>
            </div>

            <!-- 个人信息部分 -->
            <div class="detail-section">
              <h3 class="section-title">
                <i class="el-icon-s-custom"></i>
                个人信息
              </h3>
              <div class="info-grid">
                <div class="info-item detail">
                  <div class="info-label-wrapper">
                    <div class="info-label-icon">
                      <i class="el-icon-date"></i>
                    </div>
                    <span class="info-label-text">生日</span>
                  </div>
                  <div class="info-value-wrapper">
                    <span class="info-value birthday">{{ formatBirthday(userT?.birthday) }}</span>
                    <div class="birthday-countdown">
                      <i class="el-icon-timer"></i>
                      距离生日还有 45 天
                    </div>
                  </div>
                </div>
                
                <div class="info-item detail">
                  <div class="info-label-wrapper">
                    <div class="info-label-icon">
                      <i class="el-icon-phone"></i>
                    </div>
                    <span class="info-label-text">手机号码</span>
                  </div>
                  <div class="info-value-wrapper">
                    <span class="info-value phone">138****8888</span>
                    <el-button type="text" size="mini" class="bind-btn">绑定手机</el-button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 账户安全部分 -->
            <div class="detail-section">
              <h3 class="section-title">
                <i class="el-icon-lock"></i>
                账户安全
              </h3>
              <div class="security-grid">
                <div class="security-item">
                  <div class="security-status">
                    <i class="el-icon-check security-check"></i>
                    <span class="security-text">密码安全</span>
                  </div>
                  <el-button type="text" size="mini" @click="changePassword">修改</el-button>
                </div>
                
                <div class="security-item">
                  <div class="security-status">
                    <i class="el-icon-check security-check"></i>
                    <span class="security-text">邮箱验证</span>
                  </div>
                  <el-button type="text" size="mini" @click="verifyEmail">查看</el-button>
                </div>
                
                <div class="security-item">
                  <div class="security-status">
                    <i class="el-icon-warning-outline security-warning"></i>
                    <span class="security-text">手机验证</span>
                  </div>
                  <el-button type="text" size="mini" @click="bindPhone">去绑定</el-button>
                </div>
              </div>
            </div>

            <!-- 操作按钮 -->
            <div class="action-section">
              <el-button type="primary" icon="el-icon-edit" @click="editProfile" class="action-btn">
                编辑资料
              </el-button>
              <el-button type="success" icon="el-icon-setting" @click="accountSettings" class="action-btn">
                账户设置
              </el-button>
              <el-button type="info" icon="el-icon-printer" @click="exportData" class="action-btn">
                导出数据
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      userT: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: []
    }
  },
  methods: {
    formatMoney(money) {
      if (!money) return '0.00'
      return parseFloat(money).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    },
    formatBirthday(birthday) {
      if (!birthday) return '未设置'
      return birthday
    },
    changeAvatar() {
      this.$message.info('更换头像功能开发中...')
    },
    editProfile() {
      this.$message.info('编辑资料功能开发中...')
    },
    showRecharge() {
      this.$message.info('充值功能开发中...')
    },
    changePassword() {
      this.$message.info('修改密码功能开发中...')
    },
    verifyEmail() {
      this.$message.info('邮箱验证功能开发中...')
    },
    bindPhone() {
      this.$message.info('绑定手机功能开发中...')
    },
    accountSettings() {
      this.$message.info('账户设置功能开发中...')
    },
    exportData() {
      this.$message.info('数据导出功能开发中...')
    }
  }
}
</script>

<style scoped>
.user-profile-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 30px;
}

.profile-content {
  display: flex;
  gap: 30px;
  max-width: 1400px;
  margin: 0 auto;
}

/* 左侧面板 */
.profile-left {
  flex: 3;
}

.welcome-card {
  background: white;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
  height: 100%;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.welcome-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.12);
}

.welcome-header {
  text-align: center;
  margin-bottom: 40px;
  position: relative;
}

.welcome-header::after {
  content: '';
  position: absolute;
  bottom: -15px;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 4px;
  background: linear-gradient(90deg, #667eea, #764ba2);
  border-radius: 2px;
}

.welcome-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: white;
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.welcome-title {
  font-size: 36px;
  font-weight: 700;
  color: #333;
  margin-bottom: 10px;
  letter-spacing: 1px;
}

.highlight {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: colorShift 3s ease-in-out infinite;
}

@keyframes colorShift {
  0%, 100% { filter: hue-rotate(0deg); }
  50% { filter: hue-rotate(45deg); }
}

.welcome-subtitle {
  font-size: 16px;
  color: #666;
  font-weight: 400;
}

/* 头像区域 */
.avatar-section {
  margin: 40px 0;
}

.avatar-wrapper {
  text-align: center;
  margin-bottom: 30px;
}

.avatar-frame {
  position: relative;
  display: inline-block;
  margin-bottom: 20px;
}

.user-avatar {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  object-fit: cover;
  border: 6px solid white;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
}

.avatar-status {
  position: absolute;
  bottom: 15px;
  right: 15px;
  background: white;
  padding: 6px 12px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.status-dot {
  width: 8px;
  height: 8px;
  background: #52c41a;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.status-text {
  font-size: 12px;
  font-weight: 500;
  color: #333;
}

.avatar-actions {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 20px;
}

.avatar-actions .el-button {
  width: 45px;
  height: 45px;
  font-size: 18px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.avatar-actions .el-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

/* 快速信息 */
.user-quick-info {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 15px;
  padding: 20px;
  margin-top: 30px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background: white;
  border-radius: 12px;
  margin-bottom: 15px;
  transition: transform 0.3s ease;
}

.info-item:hover {
  transform: translateX(5px);
}

.info-item:last-child {
  margin-bottom: 0;
}

.info-icon {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.info-content {
  flex: 1;
}

.info-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
}

.info-value {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

/* 统计数据 */
.stats-section {
  margin-top: 40px;
}

.stats-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.stats-title::before {
  content: '';
  width: 4px;
  height: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.stat-card {
  background: white;
  border-radius: 15px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border-left: 4px solid;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.stat-primary {
  border-left-color: #667eea;
}

.stat-success {
  border-left-color: #52c41a;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
}

.stat-primary .stat-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-success .stat-icon {
  background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

/* 右侧面板 */
.profile-right {
  flex: 7;
}

.detail-card {
  border-radius: 20px;
  border: none;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.detail-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.12);
}

.detail-card-header {
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  padding: 25px 30px;
  border-bottom: none;
}

::v-deep .el-card__header {
  border-bottom: none;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-icon {
  font-size: 28px;
  color: white;
  background: rgba(255, 255, 255, 0.2);
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-title {
  font-size: 24px;
  font-weight: 700;
  color: white;
  margin: 0;
  flex: 1;
}

.header-badge {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.detail-content {
  padding: 30px;
}

/* 详情部分 */
.detail-section {
  margin-bottom: 40px;
  padding-bottom: 30px;
  border-bottom: 2px solid #f0f0f0;
}

.detail-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 25px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.section-title i {
  color: #667eea;
  font-size: 22px;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 25px;
}

.info-item.detail {
  background: #f8f9fa;
  border-radius: 15px;
  padding: 20px;
  border-left: 4px solid #667eea;
  transition: all 0.3s ease;
}

.info-item.detail:hover {
  background: white;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
  border-left-color: #764ba2;
}

.info-label-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 15px;
}

.info-label-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.info-label-text {
  font-size: 16px;
  font-weight: 500;
  color: #666;
}

.info-value-wrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 10px;
}

.info-value {
  font-size: 22px;
  font-weight: 600;
  color: #333;
}

.info-value.email {
  color: #1890ff;
  font-weight: 500;
}

.info-value.money {
  color: #f5222d;
  font-size: 26px;
  font-weight: 700;
}

.info-value.points {
  color: #fa8c16;
}

.info-value.birthday {
  color: #52c41a;
}

.info-value.phone {
  color: #333;
}

.info-verified {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  color: #52c41a;
  font-size: 14px;
  font-weight: 500;
  background: #f6ffed;
  padding: 4px 12px;
  border-radius: 15px;
  border: 1px solid #b7eb8f;
}

.email-status {
  font-size: 12px;
  border-radius: 12px;
}

.recharge-btn {
  color: #1890ff;
  font-weight: 500;
}

.recharge-btn:hover {
  color: #40a9ff;
}

.points-progress {
  width: 120px;
}

.birthday-countdown {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #fa8c16;
  background: #fff7e6;
  padding: 6px 12px;
  border-radius: 15px;
  border: 1px solid #ffd591;
}

.bind-btn {
  color: #666;
}

/* 安全区域 */
.security-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.security-item {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid #f0f0f0;
  transition: all 0.3s ease;
}

.security-item:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  border-color: #e6f7ff;
}

.security-status {
  display: flex;
  align-items: center;
  gap: 10px;
}

.security-check {
  color: #52c41a;
  font-size: 20px;
}

.security-warning {
  color: #faad14;
  font-size: 20px;
}

.security-text {
  font-weight: 500;
  color: #333;
}

/* 操作按钮 */
.action-section {
  margin-top: 40px;
  text-align: center;
  padding-top: 30px;
  border-top: 2px solid #f0f0f0;
}

.action-btn {
  padding: 12px 32px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 25px;
  margin: 0 10px;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .profile-content {
    flex-direction: column;
  }
  
  .info-grid,
  .stats-grid,
  .security-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .user-profile-container {
    padding: 15px;
  }
  
  .welcome-title {
    font-size: 28px;
  }
  
  .user-avatar {
    width: 150px;
    height: 150px;
  }
  
  .detail-content {
    padding: 20px;
  }
  
  .action-btn {
    width: 100%;
    margin: 10px 0;
  }
}
</style>