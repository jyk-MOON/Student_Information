<template>
  <div>
    <!-- 1. 学生搜索区 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; color: #409EFF;">
          <i class="el-icon-search"></i> 学生查询
        </span>
      </div>

      <div class="search-container">
        <div style="display: flex; align-items: center; gap: 15px;">
          <div style="flex: 1;">
            <el-input
                v-model="searchInput"
                placeholder="请输入学生学号、姓名进行查询"
                style="width: 100%"
                @keyup.enter.native="handleSearch"
                clearable>
              <el-select
                  v-model="searchType"
                  slot="prepend"
                  style="width: 100px"
                  @change="handleSearchTypeChange">
                <el-option label="学号" value="studentId"></el-option>
                <el-option label="姓名" value="name"></el-option>
              </el-select>
              <el-button
                  slot="append"
                  icon="el-icon-search"
                  type="primary"
                  @click="handleSearch">
              </el-button>
            </el-input>
          </div>

          <div>
            <el-button type="info" plain @click="showStudentList">
              <i class="el-icon-user"></i> 学生列表
            </el-button>
            <el-button type="warning" plain @click="resetSearch">
              <i class="el-icon-refresh"></i> 重置
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 2. 学生信息展示区（简化版） -->
    <el-card v-if="currentStudent.id" shadow="hover" style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; color: #67C23A;">
          <i class="el-icon-user-solid"></i> 学生信息
        </span>
      </div>

      <div class="student-info">
        <div style="display: flex; align-items: center; margin-bottom: 15px;">
          <div style="width: 80px; height: 80px; background-color: #f0f9ff; border-radius: 50%;
                     display: flex; align-items: center; justify-content: center; margin-right: 20px;">
            <i class="el-icon-user" style="font-size: 40px; color: #409EFF;"></i>
          </div>

          <div style="flex: 1;">
            <div style="display: flex; flex-wrap: wrap; gap: 30px;">
              <div class="info-item">
                <div class="info-label">学号</div>
                <div class="info-value">{{ currentStudent.studentId || currentStudent.id }}</div>
              </div>
              <div class="info-item">
                <div class="info-label">姓名</div>
                <div class="info-value">{{ currentStudent.name }}</div>
              </div>
              <div class="info-item">
                <div class="info-label">联系方式</div>
                <div class="info-value">{{ currentStudent.phone || '未填写' }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 3. 历史借阅记录 -->
    <el-card v-if="currentStudent.id" shadow="hover">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; color: #909399;">
          <i class="el-icon-timer"></i> 借阅记录
        </span>
        <div style="float: right;">
          <el-button
              type="info"
              size="mini"
              plain
              @click="exportHistory">
            <i class="el-icon-download"></i> 导出记录
          </el-button>
          <el-button
              type="primary"
              size="mini"
              plain
              @click="loadHistoryBorrows">
            <i class="el-icon-refresh"></i> 刷新
          </el-button>
        </div>
      </div>

      <div v-if="historyBorrowList.length > 0">
        <el-table
            :data="historyBorrowList"
            stripe
            v-loading="loadingHistory">

          <el-table-column label="图书信息" min-width="200">
            <template v-slot="scope">
              <div>
                <div style="font-weight: 500;">{{ scope.row.bookName }}</div>
                <div style="font-size: 12px; color: #909399; margin-top: 2px;">
                  {{ scope.row.bookAuthor }} | ISBN: {{ scope.row.bookIsbn }}
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="借阅时间" width="150" align="center">
            <template v-slot="scope">
              {{ formatDate(scope.row.borrowTime) }}
            </template>
          </el-table-column>

          <el-table-column label="应还时间" width="150" align="center">
            <template v-slot="scope">
              <div :style="{color: isOverdue(scope.row) ? '#F56C6C' : '#67C23A'}">
                {{ formatDate(scope.row.dueTime) }}
              </div>
            </template>
          </el-table-column>

          <el-table-column label="归还时间" width="150" align="center">
            <template v-slot="scope">
              {{ formatDate(scope.row.returnTime) || '--' }}
            </template>
          </el-table-column>

          <el-table-column label="状态" width="100" align="center">
            <template v-slot="scope">
              <el-tag
                  :type="scope.row.returnTime ? 'success' : 'warning'"
                  size="small">
                {{ scope.row.returnTime ? '已归还' : '借阅中' }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="超期状态" width="100" align="center">
            <template v-slot="scope">
              <el-tag
                  v-if="!scope.row.returnTime && isOverdue(scope.row)"
                  type="danger"
                  size="small"
                  effect="dark">
                已超期
              </el-tag>
              <span v-else-if="scope.row.returnTime">--</span>
              <span v-else style="color: #67C23A;">正常</span>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="120" align="center">
            <template v-slot="scope">
              <el-button
                  v-if="!scope.row.returnTime"
                  type="success"
                  size="mini"
                  @click="handleReturn(scope.row)"
                  :loading="scope.row.returnLoading">
                归还
              </el-button>
              <span v-else style="color: #909399;">已归还</span>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div style="margin-top: 15px; text-align: center;">
          <el-pagination
              background
              @current-change="handleHistoryPageChange"
              @size-change="handleHistorySizeChange"
              :current-page="historyPageNum"
              :page-sizes="[10, 20, 50]"
              :page-size="historyPageSize"
              layout="total, sizes, prev, pager, next"
              :total="historyTotal">
          </el-pagination>
        </div>
      </div>

      <div v-else style="text-align: center; padding: 40px; color: #909399;">
        <i class="el-icon-document" style="font-size: 60px; margin-bottom: 15px;"></i>
        <div style="font-size: 16px;">该学生暂无借阅记录</div>
      </div>
    </el-card>

    <!-- 4. 学生选择对话框 -->
    <el-dialog
        title="选择学生"
        :visible.sync="studentListVisible"
        width="70%"
        :close-on-click-modal="false">

      <div style="margin-bottom: 15px;">
        <el-input
            v-model="studentListSearch"
            placeholder="搜索学生"
            style="width: 300px"
            clearable>
          <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>
      </div>

      <el-table
          :data="filteredStudentList"
          stripe
          @row-click="handleStudentSelect"
          height="400">

        <el-table-column prop="studentId" label="学号" width="120" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" width="100" align="center"></el-table-column>
        <el-table-column prop="phone" label="联系方式" width="120"></el-table-column>
        <el-table-column label="操作" width="80" align="center">
          <template v-slot="scope">
            <el-button type="text" size="mini" @click.stop="selectStudent(scope.row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="studentListVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "StudentBorrowQuery",
  data() {
    return {
      // 搜索相关
      searchInput: '',
      searchType: 'studentId',

      // 当前学生信息
      currentStudent: {},

      // 历史借阅记录
      historyBorrowList: [],
      loadingHistory: false,
      historyPageNum: 1,
      historyPageSize: 10,
      historyTotal: 0,

      // 学生列表对话框
      studentListVisible: false,
      studentListSearch: '',
      studentList: []
    }
  },
  computed: {
    // 过滤后的学生列表
    filteredStudentList() {
      if (!this.studentListSearch) return this.studentList

      const search = this.studentListSearch.toLowerCase()
      return this.studentList.filter(student =>
          (student.studentId && student.studentId.toLowerCase().includes(search)) ||
          (student.name && student.name.toLowerCase().includes(search)) ||
          (student.phone && student.phone.includes(search))
      )
    }
  },
  methods: {
    // 搜索学生
    handleSearch() {
      if (!this.searchInput.trim()) {
        this.$message.warning('请输入搜索内容')
        return
      }

      console.log('搜索学生，类型:', this.searchType, '关键词:', this.searchInput)

      // 构建查询参数
      const params = {
        pageNum: 1,
        pageSize: 10
      }

      // 根据搜索类型设置参数
      if (this.searchType === 'studentId') {
        params.studentId = this.searchInput
      } else if (this.searchType === 'name') {
        params.name = this.searchInput
      }

      this.$request.get('/user/selectPage', {
        params: params
      }).then(res => {
        console.log('学生搜索响应:', res)
        if (res.code === '200' && res.data?.list?.length > 0) {
          // 如果有多个结果，显示选择对话框
          if (res.data.list.length > 1) {
            this.studentList = res.data.list
            this.studentListVisible = true
          } else {
            // 只有一个结果，直接选择
            this.selectStudent(res.data.list[0])
          }
        } else {
          this.$message.warning('未找到匹配的学生')
        }
      }).catch(err => {
        console.error('搜索学生出错:', err)
        this.$message.error('搜索失败，请检查网络连接')
      })
    },

    // 切换搜索类型
    handleSearchTypeChange() {
      this.searchInput = ''
      this.$message.info(`已切换为按${this.searchType === 'studentId' ? '学号' : '姓名'}搜索`)
    },

    // 显示学生列表
    showStudentList() {
      // 加载所有学生
      this.$request.get('/user/selectPage', {
        params: {
          pageNum: 1,
          pageSize: 100
        }
      }).then(res => {
        if (res.code === '200' && res.data?.list) {
          this.studentList = res.data.list
          this.studentListVisible = true
        } else {
          this.$message.warning('获取学生列表失败')
        }
      })
    },

    // 重置搜索
    resetSearch() {
      this.searchInput = ''
      this.currentStudent = {}
      this.historyBorrowList = []
      this.historyTotal = 0
    },

    // 选择学生
    selectStudent(student) {
      console.log('选择学生:', student)
      this.currentStudent = student
      this.studentListVisible = false

      // 加载该学生的借阅数据
      this.loadHistoryBorrows()
    },

    // 加载历史借阅记录
    loadHistoryBorrows() {
      if (!this.currentStudent.id) {
        this.$message.warning('请先选择学生')
        return
      }

      this.loadingHistory = true
      console.log('加载学生借阅记录，学生ID:', this.currentStudent.id)

      // 调用分页查询接口
      this.$request.get('/book/borrow/selectPage', {
        params: {
          pageNum: this.historyPageNum,
          pageSize: this.historyPageSize,
          userId: this.currentStudent.id
        }
      }).then(res => {
        this.loadingHistory = false
        console.log('借阅记录响应:', res)

        if (res.code === '200') {
          this.historyBorrowList = res.data?.list || []
          this.historyTotal = res.data?.total || 0

          // 为每条记录添加加载状态
          this.historyBorrowList.forEach(record => {
            record.returnLoading = false
          })
        } else {
          this.$message.error(res.msg || '获取借阅记录失败')
        }
      }).catch(err => {
        this.loadingHistory = false
        console.error('获取借阅记录出错:', err)
        this.$message.error('获取借阅记录失败')
      })
    },

    // 学生列表行点击
    handleStudentSelect(row) {
      this.selectStudent(row)
    },

    // 历史记录分页切换
    handleHistoryPageChange(pageNum) {
      this.historyPageNum = pageNum
      this.loadHistoryBorrows()
    },

    // 历史记录每页条数切换
    handleHistorySizeChange(pageSize) {
      this.historyPageSize = pageSize
      this.historyPageNum = 1
      this.loadHistoryBorrows()
    },

    // 归还图书
    async handleReturn(record) {
      console.log('归还图书，记录:', record)

      try {
        // 设置加载状态
        record.returnLoading = true

        await this.$confirm(`确认归还图书《${record.bookName}》吗？`, '办理归还', {
          confirmButtonText: '确认归还',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 获取当前管理员ID（从登录信息获取）
        const user = JSON.parse(localStorage.getItem("xm-user") || '{}')
        const adminId = user.id

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
          // 重新加载借阅记录
          this.loadHistoryBorrows()
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

    // 导出历史记录
    exportHistory() {
      this.$message.info('导出功能开发中...')
      // 实际开发中可以调用导出接口
    },

    // 工具方法：格式化日期
    formatDate(dateStr) {
      if (!dateStr) return '--'
      try {
        const date = new Date(dateStr)
        return date.toLocaleDateString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit'
        }).replace(/\//g, '-')
      } catch (e) {
        return dateStr
      }
    },

    // 判断是否超期
    isOverdue(record) {
      if (!record.dueTime || record.returnTime) return false

      const dueDate = new Date(record.dueTime)
      const now = new Date()
      return now > dueDate
    }
  }
}
</script>

<style scoped>
.search-container {
  padding: 10px;
}

.student-info {
  padding: 10px;
}

.info-item {
  margin-bottom: 10px;
  min-width: 150px;
}

.info-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.info-value {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

/* 表格行悬停效果 */
.el-table >>> tbody tr:hover {
  cursor: pointer;
}

/* 卡片标题样式 */
.el-card__header {
  background-color: #fafafa;
  border-bottom: 1px solid #ebeef5;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .search-container > div {
    flex-direction: column;
  }

  .info-item {
    min-width: 100%;
  }
}
</style>