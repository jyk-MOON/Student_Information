<template>
  <div>
    <!-- 1. 搜索区 -->
    <div class="search">
      <el-input placeholder="请输入图书名称" style="width: 200px" v-model="name"></el-input>
      <el-input placeholder="请输入作者" style="width: 200px; margin-left: 10px" v-model="author"></el-input>
      <el-select v-model="categoryId" placeholder="请选择分类" clearable style="width: 200px; margin-left: 10px">
        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <!-- 2. 操作按钮区 -->
    <div class="operation" style="margin-top: 15px; margin-bottom: 10px">
      <el-button type="primary" plain @click="handleAdd">新增图书</el-button>
      <el-button type="success" plain @click="handleBorrow">办理借阅</el-button>
      <el-button type="warning" plain @click="showLowStock">查看低库存</el-button>
      <el-button plain @click="showCategoryManager">管理分类</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <!-- 3. 表格数据区 -->
    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="ID" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="图书名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="author" label="作者" width="120"></el-table-column>
        <el-table-column prop="categoryName" label="分类" width="120"></el-table-column>
        <el-table-column prop="isbn" label="ISBN" width="160"></el-table-column>
        <el-table-column label="库存" width="100" align="center">
          <template v-slot="scope">
            <span :style="{color: scope.row.available < 3 ? '#F56C6C' : '#67C23A', fontWeight: 'bold'}">
              {{ scope.row.available }}/{{ scope.row.total }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格(元)" width="100" align="center">
          <template v-slot="scope">￥{{ scope.row.price?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="location" label="位置" width="120"></el-table-column>

        <el-table-column label="操作" width="200" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-button plain type="success" @click="handleOneBorrow(scope.row)" size="mini">借阅</el-button>
            <el-button plain type="danger" size="mini" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination" style="margin-top: 15px">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <!-- 4. 新增/编辑图书的弹窗 -->
    <el-dialog title="图书信息" :visible.sync="bookFormVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="name" label="图书名称">
          <el-input v-model="form.name" autocomplete="off" placeholder="如：Java编程思想"></el-input>
        </el-form-item>
        <el-form-item prop="author" label="作者">
          <el-input v-model="form.author" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="isbn" label="ISBN">
          <el-input v-model="form.isbn" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="categoryId" label="分类" required>
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="publisher" label="出版社">
          <el-input v-model="form.publisher" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="price" label="价格">
          <el-input v-model="form.price" autocomplete="off" type="number" min="0">
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="total" label="总数量" required>
          <el-input-number v-model="form.total" :min="1" :max="999"></el-input-number>
          <span style="margin-left: 10px; color: #888;">可借数量将自动同步</span>
        </el-form-item>
        <el-form-item prop="location" label="馆藏位置">
          <el-input v-model="form.location" autocomplete="off" placeholder="如：A区3排2架"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="bookFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveBook">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 5. 办理借阅的弹窗 -->
    <el-dialog title="办理借阅" :visible.sync="borrowFormVisible" width="35%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px">
        <el-form-item label="图书" required>
          <div style="border: 1px solid #ddd; padding: 10px; border-radius: 4px; background-color: #f9f9f9">
            <div><strong>名称：</strong>{{ currentBook.name }}</div>
            <div style="margin-top: 5px"><strong>作者：</strong>{{ currentBook.author }}</div>
            <div style="margin-top: 5px"><strong>可借：</strong>
              <span :style="{color: currentBook.available < 1 ? '#F56C6C' : ''}">{{ currentBook.available }} 本</span>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="学生ID" required>
          <el-input v-model="borrowForm.userId" placeholder="请输入学生ID（编号）" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="管理员ID">
          <el-input v-model="borrowForm.adminId" placeholder="自动获取" autocomplete="off" disabled></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="borrowFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveBorrow" :disabled="currentBook.available < 1">确认借出</el-button>
      </div>
    </el-dialog>

    <!-- 6. 嵌入分类管理组件 -->
    <CategoryManager :visible.sync="categoryManagerVisible" @refresh="loadCategories"/>
  </div>
</template>

<script>
// 引入封装好的API方法和分类管理组件
import bookApi from '/src/api/book'
import CategoryManager from './CategoryManager.vue'

export default {
  name: "Book",
  components: { CategoryManager },
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: null,
      author: null,
      categoryId: null,

      bookFormVisible: false,
      borrowFormVisible: false,
      categoryManagerVisible: false,

      form: {},
      categories: [], // 分类下拉框数据
      currentBook: {}, // 当前要操作（借阅）的图书
      borrowForm: {
        userId: '',
        adminId: JSON.parse(localStorage.getItem('xm-user') || '{}').id // 从登录信息获取管理员ID
      },
      rules: {
        name: [
          {required: true, message: '请输入图书名称', trigger: 'blur'},
        ],
        author: [
          {required: true, message: '请输入作者', trigger: 'blur'},
        ],
        categoryId: [
          {required: true, message: '请选择分类', trigger: 'change'},
        ],
        total: [
          {required: true, message: '请输入总数量', trigger: 'blur'},
          {type: 'number', min: 1, message: '数量至少为1', trigger: 'blur'}
        ]
      },
      ids: []
    }
  },
  created() {
    // 页面加载时，获取图书列表和分类下拉数据
    this.load(1)
    this.loadCategories()
  },
  methods: {
    // 加载图书列表
    async load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.loading = true

      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.name || undefined,
        author: this.author || undefined,
        categoryId: this.categoryId || undefined
      }

      try {
        const res = await this.$request.get('/book/selectPage', { params })
        if (res.code === '200') {
          this.tableData = res.data?.list || []
          this.total = res.data?.total || 0

          // 为每本图书匹配分类名称
          this.tableData.forEach(book => {
            const cat = this.categories.find(c => c.id === book.categoryId)
            book.categoryName = cat ? cat.name : '未知'
          })
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('加载图书列表出错:', error)
        this.$message.error('加载失败')
      } finally {
        this.loading = false
      }
    },
    // 加载分类数据（用于下拉框）
    async loadCategories() {
      try {
        const res = await this.$request.get('/category/selectAll')
        if (res.code === '200') {
          this.categories = res.data || []
        }
      } catch (error) {
        console.error('获取分类失败:', error)
      }
    },
    // 新增图书
    handleAdd() {
      this.form = {
        total: 1,
        available: 1 // 默认可借数量等于总数
      }
      this.bookFormVisible = true
    },
    // 编辑图书
    handleEdit(row) {
      // 深拷贝当前行数据到表单
      this.form = JSON.parse(JSON.stringify(row))
      this.bookFormVisible = true
    },
    // 保存图书（新增或修改）
    async saveBook() {
      try {
        const valid = await this.$refs.formRef.validate()
        if (!valid) return

        // 确保新增时，可借数量等于总数量
        if (!this.form.id) {
          this.form.available = this.form.total
        }

        const url = this.form.id ? '/book/update' : '/book/add'
        const method = this.form.id ? 'put' : 'post'

        const res = await this.$request({
          url: url,
          method: method,
          data: this.form
        })

        if (res.code === '200') {
          this.$message.success('保存成功')
          this.load(1)
          this.bookFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('保存图书出错:', error)
      }
    },
    // 删除单本图书
    del(id) {
      this.$confirm('确定删除这本图书吗？', '确认删除', {type: "warning"}).then(() => {
        bookApi.deleteBook(id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    // 表格多选
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)
    },
    // 批量删除
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择要删除的图书')
        return
      }
      this.$confirm(`确定批量删除这 ${this.ids.length} 本图书吗？`, '确认删除', {type: "warning"}).then(() => {
        // 注意：您的后端可能没有批量删除接口，这里需要循环调用单个删除
        // 这里提供前端循环删除的示例（在实际生产中，建议后端提供批量接口）
        let successCount = 0
        const deleteNext = (index) => {
          if (index >= this.ids.length) {
            this.$message.success(`成功删除 ${successCount} 本图书`)
            this.load(1)
            this.ids = []
            return
          }
          bookApi.deleteBook(this.ids[index]).then(res => {
            if (res.code === '200') successCount++
            deleteNext(index + 1)
          })
        }
        deleteNext(0)
      }).catch(() => {})
    },
    // 重置搜索条件
    reset() {
      this.name = null
      this.author = null
      this.categoryId = null
      this.load(1)
    },
    // 分页切换
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },

    // ========== 借阅相关方法 ==========
    // 打开“办理借阅”弹窗（从表格操作列）
    handleOneBorrow(row) {
      if (row.available < 1) {
        this.$message.warning('该图书暂无库存可借')
        return
      }
      this.currentBook = row
      this.borrowForm.userId = '' // 清空之前输入的学生ID
      this.borrowFormVisible = true
    },
    // 顶部“办理借阅”按钮（需先选择图书）
    handleBorrow() {
      if (this.ids.length !== 1) {
        this.$message.warning('请先选择一本要借阅的图书')
        return
      }
      const selectedBook = this.tableData.find(book => book.id === this.ids[0])
      this.handleOneBorrow(selectedBook)
    },
    // 执行借阅操作
    async saveBorrow() {
      if (!this.borrowForm.userId) {
        this.$message.warning('请输入学生ID')
        return
      }

      try {
        const res = await this.$request.post('/book/borrow', null, {
          params: {
            bookId: this.currentBook.id,
            userId: this.borrowForm.userId,
            adminId: this.borrowForm.adminId
          }
        })

        if (res.code === '200') {
          this.$message.success('借阅成功')
          this.borrowFormVisible = false
          this.load(1) // 刷新列表，更新库存
        } else {
          this.$message.error(res.msg || '借阅失败')
        }
      } catch (error) {
        console.error('借阅出错:', error)
        this.$message.error('借阅失败')
      }
    },

    // ========== 其他功能 ==========
    // 查看低库存图书
    showLowStock() {
      bookApi.selectLowStock().then(res => {
        if (res.code === '200' && res.data.length) {
          this.$alert(`共有 ${res.data.length} 本图书库存紧张，请及时补充。`, '低库存提醒', {
            confirmButtonText: '知道了'
          })
        } else {
          this.$message.info('当前没有低库存图书')
        }
      })
    },
    // 打开分类管理弹窗
    showCategoryManager() {
      this.categoryManagerVisible = true
    }
  }
}
</script>

<style scoped>
/* 样式与User.vue保持一致即可 */
.search, .operation {
  margin-bottom: 15px;
}
</style>