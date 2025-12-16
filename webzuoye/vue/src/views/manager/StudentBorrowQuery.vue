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
                placeholder="请输入学生学号、姓名或手机号进行查询"
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
                <el-option label="手机号" value="phone"></el-option>
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

        <!-- 高级搜索（折叠面板） -->
        <el-collapse v-model="activeCollapse" style="margin-top: 15px;">
          <el-collapse-item title="高级搜索" name="1">
            <el-form :inline="true" label-width="80px">
              <el-form-item label="学院">
                <el-input v-model="advancedSearch.college" placeholder="请输入学院名称"></el-input>
              </el-form-item>
              <el-form-item label="专业">
                <el-input v-model="advancedSearch.major" placeholder="请输入专业名称"></el-input>
              </el-form-item>
              <el-form-item label="班级">
                <el-input v-model="advancedSearch.className" placeholder="请输入班级"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleAdvancedSearch">搜索</el-button>
              </el-form-item>
            </el-form>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-card>

    <!-- 2. 学生信息展示区 -->
    <el-card v-if="currentStudent.id" shadow="hover" style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; color: #67C23A;">
          <i class="el-icon-user-solid"></i> 学生信息
        </span>
        <el-button
            style="float: right; padding: 3px 0"
            type="text"
            @click="viewStudentDetail">
          查看详情 <i class="el-icon-arrow-right"></i>
        </el-button>
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
                <div class="info-label">性别</div>
                <div class="info-value">{{ currentStudent.gender || '未填写' }}</div>
              </div>
              <div class="info-item">
                <div class="info-label">学院</div>
                <div class="info-value">{{ currentStudent.college || '未填写' }}</div>
              </div>
              <div class="info-item">
                <div class="info-label">专业</div>
                <div class="info-value">{{ currentStudent.major || '未填写' }}</div>
              </div>
              <div class="info-item">
                <div class="info-label">班级</div>
                <div class="info-value">{{ currentStudent.className || '未填写' }}</div>
              </div>
              <div class="info-item">
                <div class="info-label">联系方式</div>
                <div class="info-value">{{ currentStudent.phone || '未填写' }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 借阅统计卡片 -->
        <div style="display: flex; gap: 15px; margin-top: 10px;">
          <el-card shadow="never" style="flex: 1; text-align: center; background-color: #f0f9ff;">
            <div style="color: #409EFF; font-size: 24px; font-weight: bold;">
              {{ borrowStats.totalBorrowed || 0 }}
            </div>
            <div style="color: #606266; font-size: 14px; margin-top: 5px;">总借阅量</div>
          </el-card>
          <el-card shadow="never" style="flex: 1; text-align: center; background-color: #f0f9ff;">
            <div style="color: #E6A23C; font-size: 24px; font-weight: bold;">
              {{ borrowStats.currentBorrowing || 0 }}
            </div>
            <div style="color: #606266; font-size: 14px; margin-top: 5px;">当前借阅</div>
          </el-card>
          <el-card shadow="never" style="flex: 1; text-align: center; background-color: #f0f9ff;">
            <div :style="{color: borrowStats.overdue > 0 ? '#F56C6C' : '#67C23A',
                         fontSize: '24px', fontWeight: 'bold'}">
              {{ borrowStats.overdue || 0 }}
            </div>
            <div style="color: #606266; font-size: 14px; margin-top: 5px;">超期未还</div>
          </el-card>
          <el-card shadow="never" style="flex: 1; text-align: center; background-color: #f0f9ff;">
            <div style="color: #909399; font-size: 24px; font-weight: bold;">
              {{ borrowStats.favorites || 0 }}
            </div>
            <div style="color: #606266; font-size: 14px; margin-top: 5px;">收藏图书</div>
          </el-card>
        </div>
      </div>
    </el-card>

    <!-- 3. 当前借阅列表 -->
    <el-card v-if="currentStudent.id" shadow="hover" style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; color: #E6A23C;">
          <i class="el-icon-reading"></i> 当前借阅 ({{ currentBorrowList.length }})
        </span>
        <div style="float: right;">
          <el-button
              type="success"
              size="mini"
              plain
              :disabled="selectedCurrentBorrows.length === 0"
              @click="handleBatchReturn">
            批量归还 ({{ selectedCurrentBorrows.length }})
          </el-button>
          <el-button
              type="primary"
              size="mini"
              plain
              @click="loadBorrowData">
            <i class="el-icon-refresh"></i> 刷新
          </el-button>
        </div>
      </div>

      <div v-if="currentBorrowList.length > 0">
        <el-table
            :data="currentBorrowList"
            stripe
            @selection-change="handleCurrentBorrowSelection"
            v-loading="loadingCurrent">

          <el-table-column type="selection" width="55" align="center"></el-table-column>

          <el-table-column label="图书信息" min-width="200">
            <template v-slot="scope">
              <div>
                <div style="font-weight: 500; color: #409EFF; cursor: pointer;"
                     @click="viewBookDetail(scope.row.bookId)">
                  {{ scope.row.bookName }}
                </div>
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

          <el-table-column label="剩余天数" width="100" align="center">
            <template v-slot="scope">
              <el-tag
                  :type="getRemainingDaysTagType(scope.row)"
                  size="small"
                  effect="plain">
                {{ calculateRemainingDays(scope.row) }}天
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="超期状态" width="100" align="center">
            <template v-slot="scope">
              <el-tag
                  v-if="isOverdue(scope.row)"
                  type="danger"
                  size="small"
                  effect="dark">
                超期 {{ calculateOverdueDays(scope.row) }}天
              </el-tag>
              <el-tag
                  v-else
                  type="success"
                  size="small"
                  effect="plain">
                正常
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="150" align="center" fixed="right">
            <template v-slot="scope">
              <el-button
                  type="success"
                  size="mini"
                  @click="handleSingleReturn(scope.row)">
                归还
              </el-button>
              <el-button
                  type="warning"
                  size="mini"
                  plain
                  @click="handleRenew(scope.row)"
                  :disabled="scope.row.renewed || isOverdue(scope.row)">
                续借
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div v-else style="text-align: center; padding: 40px; color: #909399;">
        <i class="el-icon-notebook-2" style="font-size: 60px; margin-bottom: 15px;"></i>
        <div style="font-size: 16px;">该学生当前没有借阅中的图书</div>
      </div>
    </el-card>

    <!-- 4. 历史借阅记录 -->
    <el-card v-if="currentStudent.id" shadow="hover">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; color: #909399;">
          <i class="el-icon-timer"></i> 历史借阅记录
        </span>
        <div style="float: right;">
          <el-button
              type="info"
              size="mini"
              plain
              @click="exportHistory">
            <i class="el-icon-download"></i> 导出记录
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

          <el-table-column label="归还时间" width="150" align="center">
            <template v-slot="scope">
              {{ formatDate(scope.row.returnTime) || '--' }}
            </template>
          </el-table-column>

          <el-table-column label="借阅状态" width="100" align="center">
            <template v-slot="scope">
              <el-tag
                  :type="scope.row.returnTime ? 'success' : 'warning'"
                  size="small">
                {{ scope.row.returnTime ? '已归还' : '借阅中' }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="借阅时长" width="120" align="center">
            <template v-slot="scope">
              {{ calculateBorrowDuration(scope.row) }}
            </template>
          </el-table-column>

          <el-table-column label="操作管理员" width="120">
            <template v-slot="scope">
              {{ scope.row.adminName || '系统' }}
            </template>
          </el-table-column>

          <el-table-column label="操作" width="100" align="center">
            <template v-slot="scope">
              <el-button
                  type="text"
                  size="mini"
                  @click="viewBorrowDetail(scope.row)">
                详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 历史记录分页 -->
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
        <div style="font-size: 16px;">暂无历史借阅记录</div>
      </div>
    </el-card>

    <!-- 5. 学生选择对话框 -->
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
        <el-table-column prop="gender" label="性别" width="80" align="center">
          <template v-slot="scope">
            {{ scope.row.gender === 'M' ? '男' : scope.row.gender === 'F' ? '女' : '未填写' }}
          </template>
        </el-table-column>
        <el-table-column prop="college" label="学院"></el-table-column>
        <el-table-column prop="major" label="专业"></el-table-column>
        <el-table-column prop="className" label="班级" width="100"></el-table-column>
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
import bookApi from '@/api/book'

export default {
  name: "StudentBorrowQuery",
  data() {
    return {
      // 搜索相关
      searchInput: '',
      searchType: 'studentId',
      activeCollapse: [],
      advancedSearch: {
        college: '',
        major: '',
        className: ''
      },

      // 当前学生信息
      currentStudent: {},

      // 借阅统计
      borrowStats: {
        totalBorrowed: 0,
        currentBorrowing: 0,
        overdue: 0,
        favorites: 0
      },

      // 当前借阅列表
      currentBorrowList: [],
      loadingCurrent: false,
      selectedCurrentBorrows: [],

      // 历史借阅记录
      historyBorrowList: [],
      loadingHistory: false,
      historyPageNum: 1,
      historyPageSize: 10,
      historyTotal: 0,

      // 学生列表对话框
      studentListVisible: false,
      studentListSearch: '',
      studentList: [],

      // 当前登录管理员信息
      currentAdmin: JSON.parse(localStorage.getItem('xm-user') || '{}')
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
          (student.phone && student.phone.includes(search)) ||
          (student.college && student.college.toLowerCase().includes(search))
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

      // 直接使用用户信息接口来搜索学生
      this.$request.get('/user/selectPage', {
        params: {
          pageNum: 1,
          pageSize: 10,
          name: this.searchType === 'name' ? this.searchInput : undefined,
          studentId: this.searchType === 'studentId' ? this.searchInput : undefined,
          phone: this.searchType === 'phone' ? this.searchInput : undefined
        }
      }).then(res => {
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

    // 高级搜索
    handleAdvancedSearch() {
      // 实现高级搜索逻辑
      this.$message.info('高级搜索功能开发中...')
    },

    // 切换搜索类型
    handleSearchTypeChange() {
      this.searchInput = ''
      this.$message.info(`已切换为按${this.searchType === 'studentId' ? '学号' :
          this.searchType === 'name' ? '姓名' : '手机号'}搜索`)
    },

    // 显示学生列表
    showStudentList() {
      // 加载所有学生
      this.$request.get('/user/selectPage', {
        params: {
          pageNum: 1,
          pageSize: 100,
          role: 'STUDENT'
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
      this.currentBorrowList = []
      this.historyBorrowList = []
      this.borrowStats = {
        totalBorrowed: 0,
        currentBorrowing: 0,
        overdue: 0,
        favorites: 0
      }
    },

    // 选择学生
    selectStudent(student) {
      this.currentStudent = student
      this.studentListVisible = false

      // 加载该学生的借阅数据
      this.loadBorrowData()
    },

    // 加载借阅数据
    loadBorrowData() {
      if (!this.currentStudent.id) {
        this.$message.warning('请先选择学生')
        return
      }

      this.loadCurrentBorrows()
      this.loadHistoryBorrows()
      this.calculateBorrowStats()
    },

    // 加载当前借阅
    async loadCurrentBorrows() {
      this.loadingCurrent = true
      try {
        const res = await this.$request.get(`/book/borrow/user/${this.currentStudent.id}`)
        if (res.code === '200') {
          // 过滤出未归还的记录
          this.currentBorrowList = (res.data || []).filter(record => !record.returnTime)
          this.selectedCurrentBorrows = []
        } else {
          this.$message.error(res.msg || '获取当前借阅失败')
        }
      } catch (error) {
        console.error('获取当前借阅出错:', error)
        this.$message.error('获取当前借阅失败')
      } finally {
        this.loadingCurrent = false
      }
    },

    // 加载历史借阅记录
    loadHistoryBorrows() {
      this.loadingHistory = true

      // 调用分页查询接口
      bookApi.selectBorrowPage({
        pageNum: this.historyPageNum,
        pageSize: this.historyPageSize,
        userId: this.currentStudent.id
      }).then(res => {
        this.loadingHistory = false
        if (res.code === '200') {
          this.historyBorrowList = res.data?.list || []
          this.historyTotal = res.data?.total || 0
        } else {
          this.$message.error(res.msg || '获取历史记录失败')
        }
      }).catch(err => {
        this.loadingHistory = false
        console.error('获取历史记录出错:', err)
      })
    },

    // 计算借阅统计
    calculateBorrowStats() {
      // 这里可以根据实际数据计算统计信息
      this.borrowStats = {
        totalBorrowed: this.historyTotal,
        currentBorrowing: this.currentBorrowList.length,
        overdue: this.currentBorrowList.filter(record => this.isOverdue(record)).length,
        favorites: 0 // 需要额外的收藏接口
      }
    },

    // 当前借阅多选
    handleCurrentBorrowSelection(selection) {
      this.selectedCurrentBorrows = selection
    },

    // 单本归还
    handleSingleReturn(record) {
      this.$confirm(`确认归还图书《${record.bookName}》吗？`, '办理归还', {
        confirmButtonText: '确认归还',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const adminId = this.currentAdmin.id
        if (!adminId) {
          this.$message.error('无法获取管理员信息')
          return
        }

        bookApi.returnBook(record.id, adminId).then(res => {
          if (res.code === '200') {
            this.$message.success('归还成功')
            this.loadBorrowData() // 重新加载数据
          } else {
            this.$message.error(res.msg || '归还失败')
          }
        })
      }).catch(() => {})
    },

    // 批量归还
    handleBatchReturn() {
      if (this.selectedCurrentBorrows.length === 0) {
        this.$message.warning('请选择要归还的图书')
        return
      }

      const bookNames = this.selectedCurrentBorrows.map(item => item.bookName).join('、')
      this.$confirm(`确认批量归还 ${this.selectedCurrentBorrows.length} 本图书吗？\n\n${bookNames}`, '批量归还', {
        confirmButtonText: '确认归还',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const adminId = this.currentAdmin.id
        if (!adminId) {
          this.$message.error('无法获取管理员信息')
          return
        }

        // 批量处理
        let successCount = 0
        const processNext = (index) => {
          if (index >= this.selectedCurrentBorrows.length) {
            this.$message.success(`成功归还 ${successCount} 本图书`)
            this.loadBorrowData()
            return
          }

          const record = this.selectedCurrentBorrows[index]
          bookApi.returnBook(record.id, adminId).then(res => {
            if (res.code === '200') successCount++
            processNext(index + 1)
          }).catch(() => {
            processNext(index + 1)
          })
        }

        processNext(0)
      }).catch(() => {})
    },

    // 续借
    handleRenew(record) {
      this.$confirm(`确认续借图书《${record.bookName}》吗？`, '办理续借', {
        confirmButtonText: '确认续借',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        // 这里需要调用续借接口，假设为/book/renew/{borrowId}
        this.$request.post(`/book/renew/${record.id}`).then(res => {
          if (res.code === '200') {
            this.$message.success('续借成功')
            record.renewed = true
            // 更新应还时间
            if (res.data && res.data.newDueTime) {
              record.dueTime = res.data.newDueTime
            }
          } else {
            this.$message.error(res.msg || '续借失败')
          }
        })
      }).catch(() => {})
    },

    // 导出历史记录
    exportHistory() {
      this.$message.info('导出功能开发中...')
      // 实际开发中可以调用导出接口
    },

    // 查看学生详情
    viewStudentDetail() {
      // 跳转到学生详情页面或显示详情对话框
      this.$message.info('查看学生详情功能开发中...')
    },

    // 查看图书详情
    viewBookDetail(bookId) {
      // 跳转到图书详情页面
      this.$message.info(`查看图书详情: ${bookId}`)
    },

    // 查看借阅记录详情
    viewBorrowDetail(record) {
      // 显示借阅记录详情
      this.$alert(
          `借阅记录详情：\n\n` +
          `图书：${record.bookName}\n` +
          `作者：${record.bookAuthor}\n` +
          `ISBN：${record.bookIsbn}\n` +
          `借阅时间：${this.formatDate(record.borrowTime)}\n` +
          `应还时间：${this.formatDate(record.dueTime)}\n` +
          `归还时间：${this.formatDate(record.returnTime) || '尚未归还'}\n` +
          `操作员：${record.adminName || '系统'}`,
          '借阅详情',
          { confirmButtonText: '知道了' }
      )
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
      if (!record.dueTime) return false

      const dueDate = new Date(record.dueTime)
      const now = new Date()
      return now > dueDate
    },

    // 计算超期天数
    calculateOverdueDays(record) {
      if (!this.isOverdue(record)) return 0

      const dueDate = new Date(record.dueTime)
      const now = new Date()
      const diffTime = now - dueDate
      return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    },

    // 计算剩余天数
    calculateRemainingDays(record) {
      if (!record.dueTime || this.isOverdue(record)) return 0

      const dueDate = new Date(record.dueTime)
      const now = new Date()
      const diffTime = dueDate - now
      return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    },

    // 获取剩余天数标签类型
    getRemainingDaysTagType(record) {
      const days = this.calculateRemainingDays(record)
      if (days <= 0) return 'danger'
      if (days <= 3) return 'warning'
      return 'success'
    },

    // 计算借阅时长
    calculateBorrowDuration(record) {
      if (!record.borrowTime) return '--'

      const borrowDate = new Date(record.borrowTime)
      const returnDate = record.returnTime ? new Date(record.returnTime) : new Date()
      const diffTime = returnDate - borrowDate
      const days = Math.floor(diffTime / (1000 * 60 * 60 * 24))

      return days > 0 ? `${days}天` : '当天归还'
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
  background-color: #f5f7fa;
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