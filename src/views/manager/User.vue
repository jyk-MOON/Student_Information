<template>
  <div class="user-management-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-user"></i>
          用户管理
        </h1>
        <p class="page-subtitle">管理您的用户数据，支持增删改查操作</p>
      </div>
      
      <!-- 统计卡片 -->
      <div class="stats-cards">
        <div class="stat-card">
          <div class="stat-icon user-icon">
            <i class="el-icon-user-solid"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon new-icon">
            <i class="el-icon-circle-plus"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">+ 0</div>
            <div class="stat-label">今日新增</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-card">
            <div class="stat-icon active-icon">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">100%</div>
              <div class="stat-label">活跃度</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 操作栏 -->
    <div class="operation-bar">
      <div class="left-actions">
        <!-- 搜索区域 -->
        <div class="search-section">
          <el-input
            v-model="name"
            placeholder="请输入姓名查询"
            clearable
            size="medium"
            class="search-input"
            @keyup.enter="load(1)"
            @clear="reset">
            <template #prefix>
              <i class="el-icon-search"></i>
            </template>
          </el-input>
          
          <div class="search-buttons">
            <el-button 
              type="primary" 
              icon="el-icon-search" 
              @click="load(1)"
              class="search-btn">
              查询
            </el-button>
            <el-button 
              icon="el-icon-refresh" 
              @click="reset"
              class="reset-btn">
              重置
            </el-button>
          </div>
        </div>

        <!-- 高级筛选 -->
        <div class="filter-section">
          <el-button 
            type="text" 
            icon="el-icon-s-operation"
            @click="showAdvancedFilter = !showAdvancedFilter">
            高级筛选 {{ showAdvancedFilter ? '收起' : '展开' }}
          </el-button>
        </div>
        
        <!-- 高级筛选面板 -->
        <div v-if="showAdvancedFilter" class="advanced-filter-panel">
          <div class="filter-row">
            <div class="filter-item">
              <span class="filter-label">省份:</span>
              <el-select
                v-model="filter.province"
                placeholder="请选择省份"
                clearable
                size="small"
                class="filter-select">
                <el-option
                  v-for="item in provinceOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
            
            <div class="filter-item">
              <span class="filter-label">城市:</span>
              <el-select
                v-model="filter.city"
                placeholder="请选择城市"
                clearable
                size="small"
                class="filter-select">
                <el-option
                  v-for="item in cityOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
            
            <div class="filter-item">
              <span class="filter-label">注册时间:</span>
              <el-date-picker
                v-model="filter.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                size="small"
                class="filter-date">
              </el-date-picker>
            </div>
            
            <el-button 
              type="primary" 
              icon="el-icon-search" 
              size="small"
              @click="load(1)"
              class="filter-search-btn">
              筛选
            </el-button>
          </div>
        </div>
      </div>

      <div class="right-actions">
        <el-button 
          type="primary" 
          icon="el-icon-plus" 
          @click="handleAdd"
          class="add-btn">
          新增用户
        </el-button>
        <el-button 
          type="danger" 
          icon="el-icon-delete" 
          :disabled="!ids.length"
          @click="delBatch"
          class="batch-delete-btn">
          批量删除
        </el-button>
        <el-button 
          icon="el-icon-download" 
          @click="exportData"
          class="export-btn">
          导出数据
        </el-button>
        <el-button 
          type="text" 
          icon="el-icon-setting"
          class="settings-btn">
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-card class="table-card" shadow="never">
        <el-table 
          :data="tableData" 
          stripe 
          @selection-change="handleSelectionChange"
          v-loading="loading"
          element-loading-text="数据加载中..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(255, 255, 255, 0.8)"
          class="user-table"
          style="width: 100%">
          
          <el-table-column 
            type="selection" 
            width="55" 
            align="center">
          </el-table-column>
          
          <el-table-column 
            prop="id" 
            label="编号" 
            width="120" 
            align="center"
            sortable>
            <template #header>
              <div class="table-header">
                <span>编号</span>
                <i class="el-icon-sort"></i>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column 
            prop="name" 
            label="姓名" 
            width="150"
            align="center">
            <template #header>
              <div class="table-header">
                <span>姓名</span>
              </div>
            </template>
            <template #default="scope">
              <div class="user-name-cell">
                <el-avatar 
                  :size="32" 
                  :src="scope.row.avatar" 
                  class="user-avatar">
                  {{ scope.row.name?.charAt(0) }}
                </el-avatar>
                <span class="user-name">{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column 
            prop="date" 
            label="注册时间" 
            width="180"
            align="center"
            sortable>
            <template #default="scope">
              <span class="date-cell">{{ formatDate(scope.row.date) }}</span>
            </template>
          </el-table-column>
          
          <el-table-column 
            prop="province" 
            label="省份" 
            width="120"
            align="center">
            <template #default="scope">
              <el-tag size="small" class="province-tag">
                {{ scope.row.province }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column 
            prop="city" 
            label="城市" 
            width="120"
            align="center">
            <template #default="scope">
              <span class="city-cell">{{ scope.row.city }}</span>
            </template>
          </el-table-column>
          
          <el-table-column 
            prop="address" 
            label="地址" 
            min-width="200"
            show-overflow-tooltip>
            <template #default="scope">
              <div class="address-cell">
                <i class="el-icon-location-information"></i>
                <span>{{ scope.row.address }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column 
            prop="status" 
            label="状态" 
            width="100"
            align="center">
            <template #default="scope">
              <el-tag 
                :type="getStatusType(scope.row.status)"
                size="small"
                class="status-tag">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column 
            label="操作" 
            width="200" 
            align="center"
            fixed="right">
            <template #default="scope">
              <div class="action-buttons">
                <el-button 
                  type="text" 
                  icon="el-icon-view"
                  size="small"
                  @click="viewDetail(scope.row)"
                  class="view-btn">
                  查看
                </el-button>
                <el-button 
                  type="text" 
                  icon="el-icon-edit"
                  size="small"
                  @click="handleEdit(scope.row)"
                  class="edit-btn">
                  编辑
                </el-button>
                <el-button 
                  type="text" 
                  icon="el-icon-delete"
                  size="small"
                  @click="del(scope.row.id)"
                  class="delete-btn">
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-section">
          <div class="page-info">
            <span class="total-text">共 {{ total }} 条记录</span>
            <span class="selected-text" v-if="ids.length">已选择 {{ ids.length }} 条</span>
          </div>
          
          <el-pagination
            background
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
            :current-page="pageNum"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            layout="sizes, prev, pager, next, jumper"
            :total="total"
            class="custom-pagination">
          </el-pagination>
        </div>
      </el-card>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog 
      :title="form.id ? '编辑用户' : '新增用户'" 
      :visible.sync="fromVisible" 
      width="600px"
      :close-on-click-modal="false"
      @closed="handleDialogClosed"
      class="user-form-dialog">
      
      <div class="dialog-content">
        <el-form 
          label-width="100px" 
          :model="form" 
          :rules="rules" 
          ref="formRef"
          class="user-form">
          
          <div class="form-section">
            <h3 class="section-title">基本信息</h3>
            <el-form-item prop="name" label="姓名">
              <el-input 
                v-model="form.name" 
                placeholder="请输入用户姓名"
                clearable
                class="form-input">
                <template #prefix>
                  <i class="el-icon-user"></i>
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item prop="province" label="省份">
              <el-select 
                v-model="form.province" 
                placeholder="请选择省份"
                clearable
                class="form-select"
                @change="handleProvinceChange">
                <el-option
                  v-for="item in provinceOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            
            <el-form-item prop="city" label="城市">
              <el-select 
                v-model="form.city" 
                placeholder="请选择城市"
                clearable
                class="form-select">
                <el-option
                  v-for="item in cityOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            
            <el-form-item prop="address" label="详细地址">
              <el-input 
                v-model="form.address" 
                placeholder="请输入详细地址"
                type="textarea"
                :rows="3"
                maxlength="200"
                show-word-limit
                class="form-textarea">
              </el-input>
            </el-form-item>
          </div>
          
          <div class="form-section" v-if="form.id">
            <h3 class="section-title">其他信息</h3>
            <el-form-item label="注册时间">
              <el-date-picker
                v-model="form.date"
                type="datetime"
                placeholder="选择注册时间"
                disabled
                class="form-date">
              </el-date-picker>
            </el-form-item>
            
            <el-form-item label="用户状态">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </div>
        </el-form>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="fromVisible = false" class="cancel-btn">取 消</el-button>
          <el-button 
            type="primary" 
            @click="save" 
            :loading="saving"
            class="save-btn">
            {{ saving ? '保存中...' : '确 定' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      saving: false,
      name: '',
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      ids: [],
      showAdvancedFilter: false,
      filter: {
        province: '',
        city: '',
        dateRange: []
      },
      provinceOptions: [
        { label: '北京', value: '北京' },
        { label: '上海', value: '上海' },
        { label: '广东', value: '广东' },
        { label: '浙江', value: '浙江' },
        { label: '江苏', value: '江苏' }
      ],
      cityOptions: [],
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        province: [
          { required: true, message: '请选择省份', trigger: 'change' }
        ],
        city: [
          { required: true, message: '请选择城市', trigger: 'change' }
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' },
          { min: 5, max: 200, message: '长度在 5 到 200 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    handleAdd() {
      this.form = { status: 1 }
      this.fromVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.fromVisible = true
    },
    async save() {
      try {
        await this.$refs.formRef.validate()
        
        this.saving = true
        
        const res = await this.$request({
          url: this.form.id ? '/user/update' : '/user/add',
          method: this.form.id ? 'PUT' : 'POST',
          data: this.form
        })
        
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.load(this.pageNum)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('保存失败:', error)
      } finally {
        this.saving = false
      }
    },
    del(id) {
      this.$confirm('确定删除该用户吗？删除后数据将无法恢复。', '确认删除', {
        type: "warning",
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        confirmButtonClass: 'confirm-delete-btn',
        cancelButtonClass: 'cancel-delete-btn'
      }).then(() => {
        this.$request.delete('/user/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load(this.pageNum)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      
      this.$confirm(`确定批量删除选中的 ${this.ids.length} 条数据吗？`, '批量删除确认', {
        type: "warning",
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        confirmButtonClass: 'confirm-delete-btn',
        cancelButtonClass: 'cancel-delete-btn'
      }).then(() => {
        this.$request.delete('/user/delete/batch', { data: this.ids }).then(res => {
          if (res.code === '200') {
            this.$message.success('批量删除成功')
            this.load(1)
            this.ids = []
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      
      this.loading = true
      
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.name,
        province: this.filter.province,
        city: this.filter.city
      }
      
      this.$request.get('/user/selectPage', { params })
        .then(res => {
          this.tableData = res.data?.list || []
          this.total = res.data?.total || 0
          
          // 模拟添加一些字段用于展示
          this.tableData.forEach(item => {
            item.avatar = item.avatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${item.name}`
            item.status = item.status || 1
          })
        })
        .catch(error => {
          console.error('加载数据失败:', error)
          this.$message.error('数据加载失败')
        })
        .finally(() => {
          this.loading = false
        })
    },
    reset() {
      this.name = ''
      this.filter = {
        province: '',
        city: '',
        dateRange: []
      }
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load(1)
    },
    formatDate(date) {
      if (!date) return '未知'
      return new Date(date).toLocaleString('zh-CN')
    },
    getStatusType(status) {
      return status === 1 ? 'success' : 'danger'
    },
    getStatusText(status) {
      return status === 1 ? '启用' : '禁用'
    },
    handleProvinceChange(province) {
      if (province) {
        // 这里应该根据省份获取城市数据，这里简单模拟
        const cityMap = {
          '北京': ['东城区', '西城区', '朝阳区', '海淀区'],
          '上海': ['黄浦区', '徐汇区', '长宁区', '静安区'],
          '广东': ['广州', '深圳', '珠海', '东莞'],
          '浙江': ['杭州', '宁波', '温州', '绍兴'],
          '江苏': ['南京', '苏州', '无锡', '常州']
        }
        this.cityOptions = cityMap[province]?.map(city => ({ label: city, value: city })) || []
      } else {
        this.cityOptions = []
        this.form.city = ''
      }
    },
    handleDialogClosed() {
      this.$refs.formRef?.clearValidate()
    },
    viewDetail(row) {
      this.$message.info('查看详情功能开发中')
    },
    exportData() {
      this.$message.info('数据导出功能开发中')
    }
  }
}
</script>

<style scoped>
.user-management-container {
  padding: 24px;
  background: #f5f7fa;
  min-height: 100vh;
}

/* 页面标题区域 */
.page-header {
  margin-bottom: 24px;
}

.header-content {
  margin-bottom: 24px;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-title i {
  color: #409EFF;
}

.page-subtitle {
  color: #606266;
  font-size: 14px;
  margin: 0;
}

/* 统计卡片 */
.stats-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon i {
  font-size: 28px;
  color: white;
}

.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.new-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.active-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

/* 操作栏 */
.operation-bar {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.left-actions {
  flex: 1;
}

.search-section {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.search-input {
  width: 300px;
}

.search-input :deep(.el-input__inner) {
  border-radius: 8px;
  border-color: #e4e7ed;
  transition: border-color 0.3s ease;
}

.search-input :deep(.el-input__inner):focus {
  border-color: #409EFF;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.search-buttons {
  display: flex;
  gap: 8px;
}

.search-btn {
  padding: 10px 20px;
  border-radius: 8px;
}

.reset-btn {
  padding: 10px 20px;
  border-radius: 8px;
}

.filter-section {
  margin-bottom: 16px;
}

.advanced-filter-panel {
  background: #f9fafc;
  border-radius: 8px;
  padding: 16px;
  margin-top: 16px;
}

.filter-row {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  font-size: 14px;
  color: #606266;
  white-space: nowrap;
}

.filter-select {
  width: 120px;
}

.filter-date {
  width: 250px;
}

.filter-search-btn {
  margin-left: auto;
}

.right-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.add-btn {
  padding: 10px 20px;
  border-radius: 8px;
}

.batch-delete-btn {
  padding: 10px 20px;
  border-radius: 8px;
}

.export-btn {
  padding: 10px 20px;
  border-radius: 8px;
}

.settings-btn {
  padding: 10px;
  border-radius: 8px;
}

/* 表格区域 */
.table-section {
  margin-bottom: 24px;
}

.table-card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05) !important;
}

.user-table {
  border-radius: 8px;
  overflow: hidden;
}

.user-table :deep(.el-table__header) th {
  background-color: #f5f7fa !important;
  color: #303133;
  font-weight: 600;
  border-bottom: 2px solid #e4e7ed;
}

.user-table :deep(.el-table__body) tr:hover {
  background-color: #f5f7fa !important;
}

.table-header {
  display: flex;
  align-items: center;
  gap: 4px;
}

.user-name-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  background: #409EFF;
  color: white;
  font-weight: 600;
}

.user-name {
  font-weight: 500;
  color: #303133;
}

.date-cell {
  color: #606266;
  font-size: 13px;
}

.province-tag {
  background: rgba(64, 158, 255, 0.1);
  color: #409EFF;
  border: none;
}

.city-cell {
  color: #606266;
}

.address-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
}

.address-cell i {
  color: #909399;
}

.status-tag {
  border: none;
  font-weight: 500;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.view-btn,
.edit-btn,
.delete-btn {
  padding: 6px 12px;
  border-radius: 6px;
}

.view-btn:hover {
  background-color: rgba(64, 158, 255, 0.1);
}

.edit-btn:hover {
  background-color: rgba(103, 194, 58, 0.1);
}

.delete-btn:hover {
  background-color: rgba(245, 108, 108, 0.1);
}

/* 分页区域 */
.pagination-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-top: 1px solid #e4e7ed;
  margin-top: 20px;
}

.page-info {
  display: flex;
  gap: 16px;
  align-items: center;
}

.total-text {
  color: #606266;
  font-size: 14px;
}

.selected-text {
  color: #409EFF;
  font-size: 14px;
  font-weight: 500;
}

.custom-pagination {
  margin: 0;
}

.custom-pagination :deep(.el-pagination__total) {
  margin-right: 16px;
}

/* 弹窗样式 */
.user-form-dialog :deep(.el-dialog) {
  border-radius: 12px;
  overflow: hidden;
}

.user-form-dialog :deep(.el-dialog__header) {
  padding: 20px 24px;
  border-bottom: 1px solid #e4e7ed;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
}

.user-form-dialog :deep(.el-dialog__title) {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.dialog-content {
  padding: 24px;
}

.form-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 16px 0;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
}

.user-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.user-form :deep(.el-form-item__label) {
  color: #606266;
  font-weight: 500;
}

.form-input :deep(.el-input__inner),
.form-select :deep(.el-input__inner),
.form-date :deep(.el-input__inner) {
  border-radius: 8px;
  border-color: #e4e7ed;
  transition: border-color 0.3s ease;
}

.form-input :deep(.el-input__inner):focus,
.form-select :deep(.el-input__inner):focus,
.form-date :deep(.el-input__inner):focus {
  border-color: #409EFF;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.form-textarea :deep(.el-textarea__inner) {
  border-radius: 8px;
  border-color: #e4e7ed;
  transition: border-color 0.3s ease;
}

.form-textarea :deep(.el-textarea__inner):focus {
  border-color: #409EFF;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px 24px;
  border-top: 1px solid #e4e7ed;
}

.cancel-btn {
  padding: 10px 24px;
  border-radius: 8px;
}

.save-btn {
  padding: 10px 24px;
  border-radius: 8px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .stats-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 992px) {
  .operation-bar {
    flex-direction: column;
    gap: 20px;
  }
  
  .right-actions {
    width: 100%;
    justify-content: flex-end;
  }
}

@media (max-width: 768px) {
  .user-management-container {
    padding: 16px;
  }
  
  .stats-cards {
    grid-template-columns: 1fr;
  }
  
  .search-section {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-input {
    width: 100%;
  }
  
  .filter-row {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-item {
    width: 100%;
  }
  
  .filter-select,
  .filter-date {
    width: 100%;
  }
  
  .right-actions {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .pagination-section {
    flex-direction: column;
    gap: 16px;
  }
}
</style>