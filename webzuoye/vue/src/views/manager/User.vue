<template>
  <div class="user-management-container">
    <!-- 搜索和操作区域 -->
    <div class="management-header">
      <div class="header-content">
        <div class="header-title">
          <i class="el-icon-user-solid"></i>
          <h2>用户管理</h2>
          <span class="user-count">共 {{ total }} 位用户</span>
        </div>
        
        <div class="search-area">
          <div class="search-wrapper">
            <el-input
              placeholder="请输入姓名查询"
              v-model="name"
              class="search-input"
              prefix-icon="el-icon-search"
              clearable>
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
                type="info" 
                icon="el-icon-refresh" 
                @click="reset"
                class="reset-btn">
                重置
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-area">
      <div class="action-buttons">
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
          @click="delBatch"
          :disabled="!ids.length"
          class="batch-delete-btn">
          批量删除 ({{ ids.length }})
        </el-button>
        <div class="action-tools">
          <el-button 
            type="info" 
            icon="el-icon-download" 
            plain
            @click="exportData">
            导出数据
          </el-button>
          <el-button 
            type="info" 
            icon="el-icon-printer" 
            plain
            @click="printData">
            打印
          </el-button>
        </div>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-card class="table-card">
        <div class="table-wrapper">
          <el-table 
            :data="tableData" 
            stripe
            style="width: 100%"
            @selection-change="handleSelectionChange"
            :header-cell-style="{background: '#f5f7fa', color: '#333'}"
            class="custom-table">
            
            <el-table-column 
              type="selection" 
              width="55" 
              align="center"
              fixed>
            </el-table-column>
            
            <el-table-column 
              prop="id" 
              label="用户编号" 
              width="120" 
              align="center"
              sortable>
              <template slot-scope="scope">
                <span class="user-id">{{ scope.row.id }}</span>
              </template>
            </el-table-column>
            
            <el-table-column 
              prop="name" 
              label="姓名" 
              width="120"
              align="center">
              <template slot-scope="scope">
                <div class="user-name-cell">
                  <div class="user-avatar-mini">
                    <span>{{ scope.row.name?.charAt(0) || 'U' }}</span>
                  </div>
                  <span class="name-text">{{ scope.row.name }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column 
              prop="date" 
              label="注册时间" 
              width="180"
              align="center"
              sortable>
              <template slot-scope="scope">
                <div class="date-cell">
                  <i class="el-icon-time date-icon"></i>
                  <span>{{ scope.row.date }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column 
              prop="province" 
              label="省份" 
              width="120"
              align="center">
              <template slot-scope="scope">
                <el-tag type="info" size="small">{{ scope.row.province }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column 
              prop="city" 
              label="城市" 
              width="120"
              align="center">
              <template slot-scope="scope">
                <el-tag type="success" size="small">{{ scope.row.city }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column 
              prop="address" 
              label="详细地址"
              min-width="200">
              <template slot-scope="scope">
                <div class="address-cell">
                  <i class="el-icon-location-outline"></i>
                  <span class="address-text">{{ scope.row.address }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column 
              label="操作" 
              width="200" 
              align="center"
              fixed="right">
              <template slot-scope="scope">
                <div class="operation-cell">
                  <el-button 
                    type="primary" 
                    icon="el-icon-edit" 
                    circle
                    size="mini"
                    @click="handleEdit(scope.row)"
                    class="edit-btn">
                  </el-button>
                  <el-button 
                    type="danger" 
                    icon="el-icon-delete" 
                    circle
                    size="mini"
                    @click="del(scope.row.id)"
                    class="delete-btn">
                  </el-button>
                  <el-button 
                    type="info" 
                    icon="el-icon-view" 
                    circle
                    size="mini"
                    @click="viewDetail(scope.row)"
                    class="view-btn">
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 分页 -->
        <div class="pagination-container">
          <div class="pagination-info">
            显示第 {{ (pageNum - 1) * pageSize + 1 }} 到 {{ Math.min(pageNum * pageSize, total) }} 条记录，共 {{ total }} 条记录
          </div>
          <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20, 50]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            class="custom-pagination">
          </el-pagination>
        </div>
      </el-card>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog 
      :title="form.id ? '编辑用户' : '新增用户'" 
      :visible.sync="fromVisible" 
      width="500px"
      :close-on-click-modal="false"
      destroy-on-close
      custom-class="user-form-dialog">
      
      <div class="dialog-content">
        <el-form 
          ref="formRef"
          :model="form" 
          :rules="rules" 
          label-width="80px"
          class="user-form">
          
          <div class="form-section">
            <div class="section-title">
              <i class="el-icon-user"></i>
              <span>基本信息</span>
            </div>
            
            <el-form-item 
              prop="name" 
              label="姓名"
              class="form-item">
              <el-input 
                v-model="form.name" 
                placeholder="请输入用户姓名"
                prefix-icon="el-icon-user"
                clearable>
              </el-input>
            </el-form-item>
          </div>

          <div class="form-section">
            <div class="section-title">
              <i class="el-icon-location"></i>
              <span>地址信息</span>
            </div>
            
            <el-form-item 
              prop="province" 
              label="省份"
              class="form-item">
              <el-input 
                v-model="form.province" 
                placeholder="请输入省份"
                prefix-icon="el-icon-map-location"
                clearable>
              </el-input>
            </el-form-item>
            
            <el-form-item 
              prop="city" 
              label="城市"
              class="form-item">
              <el-input 
                v-model="form.city" 
                placeholder="请输入城市"
                prefix-icon="el-icon-location"
                clearable>
              </el-input>
            </el-form-item>
            
            <el-form-item 
              prop="address" 
              label="详细地址"
              class="form-item">
              <el-input 
                v-model="form.address" 
                type="textarea"
                :rows="3"
                placeholder="请输入详细地址"
                maxlength="200"
                show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </el-form>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button 
          @click="fromVisible = false"
          class="cancel-btn">
          取消
        </el-button>
        <el-button 
          type="primary" 
          @click="save"
          class="submit-btn">
          {{ form.id ? '更新' : '创建' }}
        </el-button>
      </div>
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
      id: null,
      name: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
        ],
        province: [
          {required: true, message: '请输入省份', trigger: 'blur'},
        ],
        city: [
          {required: true, message: '请输入城市', trigger: 'blur'},
        ],
        address: [
          {required: true, message: '请输入地址', trigger: 'blur'},
        ]
      },
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    handleAdd() {
      this.form = {}
      this.fromVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.fromVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/user/update' : '/user/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    del(id) {
      this.$confirm('确定要删除该用户吗？', '删除确认', {
        type: "warning",
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        customClass: 'delete-confirm'
      }).then(response => {
        this.$request.delete('/user/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load(1)
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
      this.$confirm(`确定要批量删除这 ${this.ids.length} 条记录吗？`, '批量删除确认', {
        type: "warning",
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        customClass: 'batch-delete-confirm'
      }).then(response => {
        this.$request.delete('/user/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {
            this.$message.success('批量删除成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/user/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    exportData() {
      this.$message.info('导出功能开发中...')
    },
    printData() {
      this.$message.info('打印功能开发中...')
    },
    viewDetail(row) {
      this.$message.info(`查看用户 ${row.name} 的详细信息`)
    }
  }
}
</script>

<style scoped>
.user-management-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  padding: 24px;
}

/* 头部区域 */
.management-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.3);
  position: relative;
  overflow: hidden;
}

.management-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.1), transparent);
  animation: shimmer 3s infinite;
}

@keyframes shimmer {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
  z-index: 1;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-title i {
  font-size: 32px;
  color: white;
  background: rgba(255, 255, 255, 0.2);
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-title h2 {
  margin: 0;
  font-size: 24px;
  color: white;
  font-weight: 600;
  letter-spacing: 1px;
}

.user-count {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

/* 搜索区域 */
.search-area {
  flex: 1;
  max-width: 600px;
}

.search-wrapper {
  display: flex;
  gap: 12px;
  align-items: center;
  justify-content: flex-end;
}

.search-input {
  width: 300px;
}

.search-input ::v-deep .el-input__inner {
  border-radius: 25px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  background: rgba(255, 255, 255, 0.1);
  color: white;
  height: 40px;
  line-height: 40px;
  backdrop-filter: blur(10px);
}

.search-input ::v-deep .el-input__inner::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.search-input ::v-deep .el-input__prefix {
  color: white;
  left: 12px;
}

.search-buttons {
  display: flex;
  gap: 8px;
}

.search-btn,
.reset-btn {
  border-radius: 20px;
  padding: 10px 24px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.search-btn {
  background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
  border: none;
  color: white;
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(82, 196, 26, 0.4);
}

.reset-btn:hover {
  transform: translateY(-2px);
  background: white;
  color: #666;
  border-color: white;
}

/* 操作区域 */
.action-area {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.add-btn {
  background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
  border: none;
  color: white;
  border-radius: 25px;
  padding: 12px 28px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.add-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(24, 144, 255, 0.4);
}

.batch-delete-btn {
  border-radius: 25px;
  padding: 12px 28px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.batch-delete-btn:not(.is-disabled):hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(245, 34, 45, 0.3);
}

.batch-delete-btn.is-disabled {
  opacity: 0.6;
}

.action-tools {
  display: flex;
  gap: 12px;
}

/* 表格容器 */
.table-container {
  margin-top: 20px;
}

.table-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.table-wrapper {
  padding: 20px;
}

.custom-table {
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #f0f0f0;
}

.custom-table ::v-deep .el-table__header-wrapper {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

.custom-table ::v-deep .el-table__row:hover {
  background-color: #f5f7fa;
  transform: scale(1.002);
  transition: all 0.2s ease;
}

/* 表格单元格样式 */
.user-id {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-weight: 600;
  font-family: 'Courier New', monospace;
}

.user-name-cell {
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: center;
}

.user-avatar-mini {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
}

.name-text {
  font-weight: 500;
  color: #333;
}

.date-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: center;
  color: #666;
}

.date-icon {
  color: #1890ff;
}

.address-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.address-cell i {
  color: #52c41a;
}

.address-text {
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.operation-cell {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.edit-btn,
.delete-btn,
.view-btn {
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.edit-btn:hover {
  background: #1890ff;
  transform: translateY(-2px) scale(1.1);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
}

.delete-btn:hover {
  background: #f5222d;
  transform: translateY(-2px) scale(1.1);
  box-shadow: 0 4px 12px rgba(245, 34, 45, 0.3);
}

.view-btn:hover {
  background: #666;
  transform: translateY(-2px) scale(1.1);
  box-shadow: 0 4px 12px rgba(102, 102, 102, 0.3);
}

/* 分页样式 */
.pagination-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  border-top: 1px solid #f0f0f0;
  background: #fafafa;
}

.pagination-info {
  color: #666;
  font-size: 14px;
}

.custom-pagination {
  margin: 0;
}

.custom-pagination ::v-deep .el-pagination.is-background .btn-next,
.custom-pagination ::v-deep .el-pagination.is-background .btn-prev,
.custom-pagination ::v-deep .el-pagination.is-background .el-pager li {
  border-radius: 8px;
  margin: 0 4px;
  transition: all 0.3s ease;
}

.custom-pagination ::v-deep .el-pagination.is-background .btn-next:hover,
.custom-pagination ::v-deep .el-pagination.is-background .btn-prev:hover,
.custom-pagination ::v-deep .el-pagination.is-background .el-pager li:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

.custom-pagination ::v-deep .el-pagination.is-background .el-pager li.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

/* 对话框样式 */
.user-form-dialog {
  border-radius: 20px;
  overflow: hidden;
}

.user-form-dialog ::v-deep .el-dialog__header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  border-bottom: none;
}

.user-form-dialog ::v-deep .el-dialog__title {
  color: white;
  font-size: 20px;
  font-weight: 600;
}

.user-form-dialog ::v-deep .el-dialog__close {
  color: white;
  font-size: 18px;
}

.user-form-dialog ::v-deep .el-dialog__close:hover {
  color: #f0f0f0;
}

.dialog-content {
  padding: 20px;
}

.form-section {
  margin-bottom: 30px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #f0f0f0;
  color: #333;
  font-size: 16px;
  font-weight: 600;
}

.section-title i {
  color: #667eea;
  font-size: 18px;
}

.form-item ::v-deep .el-form-item__label {
  color: #666;
  font-weight: 500;
}

.form-item ::v-deep .el-input__inner,
.form-item ::v-deep .el-textarea__inner {
  border-radius: 10px;
  border: 2px solid #e8e8e8;
  transition: all 0.3s ease;
  padding-left: 12px;
}

.form-item ::v-deep .el-input__inner:focus,
.form-item ::v-deep .el-textarea__inner:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.dialog-footer {
  padding: 20px;
  text-align: center;
  border-top: 1px solid #f0f0f0;
}

.cancel-btn {
  border-radius: 25px;
  padding: 12px 32px;
  font-weight: 500;
  border: 2px solid #dcdfe6;
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  border-color: #1890ff;
  color: #1890ff;
  transform: translateY(-2px);
}

.submit-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  border-radius: 25px;
  padding: 12px 32px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

/* 确认对话框样式 */
.delete-confirm,
.batch-delete-confirm {
  border-radius: 16px;
}

.delete-confirm ::v-deep .el-message-box__header,
.batch-delete-confirm ::v-deep .el-message-box__header {
  background: linear-gradient(135deg, #f5222d 0%, #ff4d4f 100%);
  color: white;
  border-radius: 16px 16px 0 0;
  padding: 20px;
}

.delete-confirm ::v-deep .el-message-box__title,
.batch-delete-confirm ::v-deep .el-message-box__title {
  color: white;
}

.delete-confirm ::v-deep .el-message-box__content,
.batch-delete-confirm ::v-deep .el-message-box__content {
  padding: 30px 20px;
  color: #333;
}

.delete-confirm ::v-deep .el-message-box__btns,
.batch-delete-confirm ::v-deep .el-message-box__btns {
  padding: 20px;
  border-top: 1px solid #f0f0f0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
  }
  
  .search-area {
    width: 100%;
  }
  
  .search-wrapper {
    justify-content: flex-start;
  }
}

@media (max-width: 768px) {
  .user-management-container {
    padding: 16px;
  }
  
  .management-header {
    padding: 16px;
  }
  
  .header-title h2 {
    font-size: 20px;
  }
  
  .search-wrapper {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .search-input {
    width: 100%;
  }
  
  .search-buttons {
    width: 100%;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .action-tools {
    width: 100%;
    justify-content: flex-start;
  }
  
  .pagination-container {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .user-form-dialog {
    width: 90% !important;
  }
}
</style>