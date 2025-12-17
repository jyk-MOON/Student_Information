<template>
  <div>
    <!-- 1. 搜索区 -->
    <div class="search">
      <el-input placeholder="请输入图书名称" style="width: 200px" v-model="searchBookName"></el-input>
      <el-input placeholder="请输入学生姓名/ID" style="width: 200px; margin-left: 10px" v-model="searchStudent"></el-input>
      <el-select v-model="searchStatus" placeholder="借阅状态" clearable style="width: 120px; margin-left: 10px">
        <el-option label="全部" value=""></el-option>
        <el-option label="借阅中" value="BORROWED"></el-option>
        <el-option label="已归还" value="RETURNED"></el-option>
        <el-option label="已超期" value="OVERDUE"></el-option>
      </el-select>

      <!-- 日期范围选择 -->
      <el-date-picker
          v-model="searchDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          style="width: 300px; margin-left: 10px">
      </el-date-picker>

      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="resetSearch">重置</el-button>
    </div>

    <!-- 2. 操作按钮区 -->
    <div class="operation" style="margin-top: 15px; margin-bottom: 10px">
      <el-button type="primary" plain @click="load(1)" icon="el-icon-refresh">刷新</el-button>
      <el-button type="success" plain @click="handleBatchReturn" :disabled="selectedIds.length === 0">
        批量归还({{ selectedIds.length }})
      </el-button>
      <el-button type="warning" plain @click="exportData">导出数据</el-button>
    </div>

    <!-- 3. 借阅记录表格 -->
    <div class="table">
      <el-table
          :data="tableData"
          stripe
          @selection-change="handleSelectionChange"
          v-loading="loading">

        <el-table-column type="selection" width="55" align="center"></el-table-column>

        <el-table-column prop="id" label="记录ID" width="90" align="center" sortable></el-table-column>

        <!-- 图书信息列 -->
        <el-table-column label="图书信息" min-width="180">
          <template v-slot="scope">
            <div style="display: flex; align-items: center;">
              <div>
                <div style="font-weight: 500;">{{ scope.row.bookName }}</div>
                <div style="font-size: 12px; color: #909399;">
                  {{ scope.row.bookAuthor }} | ISBN: {{ scope.row.bookIsbn }}
                </div>
              </div>
            </div>
          </template>
        </el-table-column>

        <!-- 学生信息列 -->
        <el-table-column label="借阅学生" width="150">
          <template v-slot="scope">
            <div>
              <div>{{ scope.row.userName }}</div>
              <div style="font-size: 12px; color: #909399;">ID: {{ scope.row.userId }}</div>
            </div>
          </template>
        </el-table-column>

        <!-- 时间信息列 -->
        <el-table-column label="借阅时间" width="160" align="center">
          <template v-slot="scope">
            <div>
              <div>{{ formatDate(scope.row.borrowTime) }}</div>
              <div style="font-size: 12px; color: #909399;">
                应还: {{ formatDate(scope.row.dueTime) }}
              </div>
            </div>
          </template>
        </el-table-column>

        <!-- 归还时间列 -->
        <el-table-column label="归还时间" width="160" align="center">
          <template v-slot="scope">
            <div v-if="scope.row.returnTime">
              {{ formatDate(scope.row.returnTime) }}
            </div>
            <div v-else style="color: #F56C6C;">--</div>
          </template>
        </el-table-column>

        <!-- 状态列 -->
        <el-table-column label="状态" width="100" align="center">
          <template v-slot="scope">
            <el-tag
                :type="getStatusTagType(scope.row)"
                size="small"
                effect="dark">
              {{ getStatusText(scope.row) }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 管理员列 -->
        <el-table-column label="操作员" width="120">
          <template v-slot="scope">
            <div>
              <div>{{ scope.row.adminName || '--' }}</div>
              <div style="font-size: 12px; color: #909399;">ID: {{ scope.row.adminId }}</div>
            </div>
          </template>
        </el-table-column>

        <!-- 操作列 -->
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template v-slot="scope">
            <el-button
                v-if="!scope.row.returnTime"
                type="success"
                size="mini"
                plain
                @click="handleReturn(scope.row)"
                :loading="scope.row.returnLoading">
              办理归还
            </el-button>
            <span v-else style="color: #909399;">已归还</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination" style="margin-top: 15px">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
            :current-page="pageNum"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <!-- 4. 借阅详情对话框 -->
    <el-dialog
        title="借阅记录详情"
        :visible.sync="detailDialogVisible"
        width="50%"
        :close-on-click-modal="false">

      <div v-if="currentRecord" style="line-height: 1.8;">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="借阅编号">{{ currentRecord.id }}</el-descriptions-item>
          <el-descriptions-item label="借阅状态">
            <el-tag :type="getStatusTagType(currentRecord)" size="small">
              {{ getStatusText(currentRecord) }}
            </el-tag>
          </el-descriptions-item>

          <el-descriptions-item label="图书名称">{{ currentRecord.bookName }}</el-descriptions-item>
          <el-descriptions-item label="图书作者">{{ currentRecord.bookAuthor }}</el-descriptions-item>

          <el-descriptions-item label="ISBN">{{ currentRecord.bookIsbn }}</el-descriptions-item>

          <el-descriptions-item label="借阅学生">{{ currentRecord.userName }}</el-descriptions-item>
          <el-descriptions-item label="学生ID">{{ currentRecord.userId }}</el-descriptions-item>

          <el-descriptions-item label="借阅时间">{{ formatDate(currentRecord.borrowTime) }}</el-descriptions-item>
          <el-descriptions-item label="应还时间">{{ formatDate(currentRecord.dueTime) }}</el-descriptions-item>

          <el-descriptions-item label="实际归还时间" v-if="currentRecord.returnTime">
            {{ formatDate(currentRecord.returnTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="实际归还时间" v-else>--</el-descriptions-item>

          <el-descriptions-item label="超期天数" v-if="isOverdue(currentRecord)">
            <span style="color: #F56C6C; font-weight: bold;">{{ calculateOverdueDays(currentRecord) }} 天</span>
          </el-descriptions-item>
          <el-descriptions-item label="超期天数" v-else>0 天</el-descriptions-item>

          <el-descriptions-item label="操作管理员">{{ currentRecord.adminName || '--' }}</el-descriptions-item>
          <el-descriptions-item label="管理员ID">{{ currentRecord.adminId }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button
            v-if="currentRecord && !currentRecord.returnTime"
            type="primary"
            @click="handleReturn(currentRecord)">
          办理归还
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "BorrowRecord",
  data() {
    return {
      // 表格数据
      tableData: [],
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,

      // 搜索条件
      searchBookName: '',
      searchStudent: '',
      searchStatus: '',
      searchDateRange: [],

      // 选中项
      selectedIds: [],

      // 对话框
      detailDialogVisible: false,
      currentRecord: null,

      // 当前登录管理员信息
      currentAdmin: JSON.parse(localStorage.getItem('xm-user') || '{}')
    }
  },
  created() {
    // 页面加载时，获取借阅记录列表
    this.load(1)
  },
  methods: {
    // 加载借阅记录列表
    async load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.loading = true

      // 构建查询参数
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      }

      // 添加可选查询条件
      if (this.searchBookName) params.bookName = this.searchBookName
      if (this.searchStudent) params.studentInfo = this.searchStudent
      if (this.searchStatus) params.status = this.searchStatus

      // 添加日期范围参数
      if (this.searchDateRange && this.searchDateRange.length === 2) {
        params.startDate = this.searchDateRange[0]
        params.endDate = this.searchDateRange[1]
      }

      try {
        const res = await this.$request.get('/book/borrow/selectPage', { params })
        console.log('借阅记录列表响应:', res)

        if (res.code === '200') {
          this.tableData = res.data?.list || []
          this.total = res.data?.total || 0

          // 为每条记录添加加载状态
          this.tableData.forEach(record => {
            record.returnLoading = false
          })
        } else {
          this.$message.error(res.msg || '获取借阅记录失败')
        }
      } catch (error) {
        console.error('加载借阅记录出错:', error)
        this.$message.error('请求失败，请检查网络连接')
      } finally {
        this.loading = false
      }
    },

    // 重置搜索条件
    resetSearch() {
      this.searchBookName = ''
      this.searchStudent = ''
      this.searchStatus = ''
      this.searchDateRange = []
      this.load(1)
    },

    // 表格多选
    handleSelectionChange(rows) {
      // 只选择未归还的记录
      this.selectedIds = rows
          .filter(row => !row.returnTime)
          .map(row => row.id)
    },

    // 分页切换
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },

    // 每页条数切换
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load(1)
    },

    // 办理归还（单条）
    async handleReturn(record) {
      if (!record.id) {
        this.$message.warning('无效的记录ID')
        return
      }

      try {
        // 设置加载状态
        record.returnLoading = true

        await this.$confirm(`确认归还图书《${record.bookName}》吗？`, '办理归还', {
          confirmButtonText: '确认归还',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 获取当前管理员ID
        const adminId = this.currentAdmin.id
        if (!adminId) {
          this.$message.error('无法获取管理员信息，请重新登录')
          record.returnLoading = false
          return
        }

        console.log('归还参数:', {
          borrowId: record.id,
          adminId: adminId
        })

        // 调用归还接口
        const res = await this.$request.post(`/book/return/${record.id}`, null, {
          params: { adminId }
        })

        console.log('归还响应:', res)

        if (res.code === '200') {
          this.$message.success('归还成功')
          // 刷新列表
          this.load(1)
          // 关闭详情对话框
          if (this.detailDialogVisible) {
            this.detailDialogVisible = false
          }
        } else {
          this.$message.error(res.msg || '归还失败')
          record.returnLoading = false
        }
      } catch (error) {
        console.log('取消操作或操作失败:', error)
        record.returnLoading = false

        // 如果是用户取消操作，不做处理
        if (error !== 'cancel' && error.message !== 'cancel') {
          this.$message.error('操作失败: ' + (error.message || '未知错误'))
        }
      }
    },

    // 批量归还
    async handleBatchReturn() {
      if (this.selectedIds.length === 0) {
        this.$message.warning('请选择要归还的记录')
        return
      }

      try {
        await this.$confirm(`确认批量归还 ${this.selectedIds.length} 条借阅记录吗？`, '批量归还', {
          confirmButtonText: '确认归还',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const adminId = this.currentAdmin.id
        if (!adminId) {
          this.$message.error('无法获取管理员信息，请重新登录')
          return
        }

        let successCount = 0
        let failCount = 0

        // 为选中的记录设置加载状态
        const selectedRecords = this.tableData.filter(record => this.selectedIds.includes(record.id))
        selectedRecords.forEach(record => {
          record.returnLoading = true
        })

        // 逐个处理归还
        for (const borrowId of this.selectedIds) {
          try {
            const res = await this.$request.post(`/book/return/${borrowId}`, null, {
              params: { adminId }
            })

            if (res.code === '200') {
              successCount++
            } else {
              failCount++
              console.error(`归还记录 ${borrowId} 失败:`, res.msg)
            }
          } catch (error) {
            failCount++
            console.error(`归还记录 ${borrowId} 异常:`, error)
          }
        }

        // 清除加载状态
        selectedRecords.forEach(record => {
          record.returnLoading = false
        })

        // 显示处理结果
        let message = ''
        if (successCount > 0) {
          message += `成功归还 ${successCount} 条记录`
        }
        if (failCount > 0) {
          message += `，失败 ${failCount} 条记录`
        }

        if (message) {
          this.$message.success(message)
        }

        // 刷新数据
        this.load(1)
        this.selectedIds = []

      } catch (error) {
        console.log('取消批量归还操作')
      }
    },

    // 查看详情
    viewDetails(record) {
      this.currentRecord = JSON.parse(JSON.stringify(record))
      this.detailDialogVisible = true
    },

    // 导出数据
    exportData() {
      this.$message.info('导出功能开发中...')
    },

    // 工具方法：格式化日期
    formatDate(dateStr) {
      if (!dateStr) return '--'
      try {
        const date = new Date(dateStr)
        return date.toLocaleString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit'
        }).replace(/\//g, '-')
      } catch (e) {
        return dateStr
      }
    },

    // 工具方法：获取状态标签类型
    getStatusTagType(record) {
      if (record.returnTime) {
        return 'success'  // 已归还
      } else if (this.isOverdue(record)) {
        return 'danger'   // 已超期
      } else {
        return 'warning'  // 借阅中
      }
    },

    // 工具方法：获取状态文本
    getStatusText(record) {
      if (record.returnTime) {
        return '已归还'
      } else if (this.isOverdue(record)) {
        return '已超期'
      } else {
        return '借阅中'
      }
    },

    // 判断是否超期
    isOverdue(record) {
      if (!record.dueTime || record.returnTime) return false

      const dueDate = new Date(record.dueTime)
      const now = new Date()
      return now > dueDate
    },

    // 计算超期天数
    calculateOverdueDays(record) {
      if (!this.isOverdue(record)) return 0

      const dueDate = new Date(record.dueTime)
      const now = new Date()
      const diffTime = Math.abs(now - dueDate)
      return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    }
  }
}
</script>

<style scoped>
.search, .operation {
  margin-bottom: 15px;
}

/* 表格行悬停效果 */
.el-table >>> .hover-row {
  cursor: pointer;
  transition: background-color 0.3s;
}

.el-table >>> .hover-row:hover {
  background-color: #f5f7fa !important;
}
</style>