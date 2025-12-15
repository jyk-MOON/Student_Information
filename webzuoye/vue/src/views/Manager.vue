<template>
  <div :class="{'manager-container':!isChange,'manager-container1':isChange}">
    <!--  头部  -->
    <div :class="{'manager-header1':isChange,'manager-header':!isChange}" >
      <div class="manager-header-left">
        <div class="title">web系统</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div>
        <el-dropdown @command="logout">
          <span style="align-items: center;font-size: 25px;cursor: pointer">
            {{user?.name}}用户<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="2">日历</el-dropdown-item>
            <el-dropdown-item command="1">退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>

    </div>

    <!--  主体  -->
    <div class="manager-main">
      <!--  侧边栏  -->
      <div class="manager-main-left">
        <el-menu :default-openeds="['info', 'user']" router style="border: none" :default-active="$route.path">
          <el-menu-item :class="{'el-menu-item2':!isChange,'el-menu-item1 ':isChange}" index="/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-submenu index="info">
            <template slot="title">
              <i class="el-icon-menu"></i><span>功能管理</span>
            </template>
            <el-menu-item :class="{'el-menu-item2':!isChange,'el-menu-item1 ':isChange}" index="/user">用户基本信息</el-menu-item>
            <el-menu-item :class="{'el-menu-item2':!isChange,'el-menu-item1 ':isChange}" index="/user">待修改功能</el-menu-item>
          </el-submenu>
          <el-submenu index="info">
            <template slot="title">
              <i class="el-icon-menu"></i><span>背景切换</span>
            </template>
            <el-menu-item :class="{'el-menu-item2':!isChange,'el-menu-item1 ':isChange}" @click="backgroundChange(0)">背景1</el-menu-item>
            <el-menu-item :class="{'el-menu-item2':!isChange,'el-menu-item1 ':isChange}" @click="backgroundChange(1)">背景2</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>

  </div>
</template>

<script>

export default {
  name: "Manager",
  data() {
    return {
      isChange:false,
      theme:{
        color:"orange"
      },
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
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    logout(command) {
      localStorage.removeItem('xm-user')
      this.$router.push('/login')
    },
    backgroundChange(num){
      this.isChange = num;
    },

  }
}
</script>

<style scoped>
.manager-header1 {
  display: flex;
  background-color: deepskyblue;
  height: 50px;
}
.manager-container1 {
  background-color:powderblue !important;
  min-height: 100vh;
}
.el-menu-item1 {
  background-color: powderblue !important;
  color: blue;
}
.manager-container {
  background-color:#f9ece6 !important;
  min-height: 100vh;
}

.manager-header {
  display: flex;
  background-color: #f96332;
  height: 50px;
}

.manager-header-left {
  width: 250px;
  height: 50px;
  padding-left: 10px;
  display: flex;
  align-items: center
}

.manager-header-left img {
  width: 35px;
  height: 35px;
}

.manager-header-left .title {
  flex: 1;
  margin-left: 5px;
  font-size: 18px;
  font-weight: bold;
  color: #fff
}

.manager-header-center {
  flex: 1;
  width: 0;
  display: flex;
  align-items: center
}

.manager-header-right .avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 5px
}

.manager-main {
  display: flex;
}

.manager-main-left {
  width: 200px;
  min-height: calc(100vh - 60px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, .1);
}

.manager-main-right {
  flex: 1;
  width: 0;
  padding: 10px;
}


.el-menu-item2 {
  background-color: #f9ece6 !important;
  color: #f96332;
}
@media screen and (max-width: 800px) {
  .manager-header{display:none}
  .manager-main-left{display: none}
}
</style>