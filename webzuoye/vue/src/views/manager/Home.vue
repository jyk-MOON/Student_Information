<template>

  <div style="display: flex">
    <div style="flex: 3">
      <div style="font-size: 50px;text-align: center">您好，{{ userT?.name }}！</div>
      <div style="margin: 20px 50px 50px 50px">
        <img :src="userT?.avatar" style="width: 100%"/>
      </div>
    </div>
    <div style="flex: 7">
      <el-card>
        <div slot="header" style="font-size: 50px;text-align: center">
          <span>用户基本信息</span>
        </div>
        <div style="font-size: 50px;font-family:NSimSun">用户名 {{ userT?.name }}</div>
        <div style="font-size: 50px;font-family:NSimSun">邮箱 {{ userT?.email }}</div>
        <div style="font-size: 50px;font-family:NSimSun">余额 {{ userT?.money }}</div>
        <div style="font-size: 50px;font-family:NSimSun">生日 {{ userT?.birthday }}</div>
      </el-card>
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
}
</script>
