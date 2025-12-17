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
      <el-table v-loading="loading" :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="ID" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="图书名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="author" label="作者" width="120"></el-table-column>
        <el-table-column label="分类" width="120">
          <template v-slot="scope">
            {{ getCategoryName(scope.row.categoryId) }}
          </template>
        </el-table-column>
        <el-table-column prop="isbn" label="ISBN" width="160"></el-table-column>
        <el-table-column label="库存" width="100" align="center">
          <template v-slot="scope">
            <span :style="{color: scope.row.available < 3 ? '#F56C6C' : '#67C23A', fontWeight: 'bold'}">
              {{ scope.row.available }}/{{ scope.row.total }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="价格(元)" width="100" align="center">
          <template v-slot="scope">￥{{ parseFloat(scope.row.price || 0).toFixed(2) }}</template>
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
          <el-input v-model="form.price" autocomplete="off" type="number" min="0" step="0.01">
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
    <!-- 价格控制 -->
    <el-form-item prop="price" label="价格">
      <el-input
          v-model="form.price"
          autocomplete="off"
          placeholder="请输入价格"
          @input="handlePriceInput">
        <template slot="append">元</template>
      </el-input>
    </el-form-item>

    <!-- 6. 嵌入分类管理组件 -->
    <CategoryManager :visible.sync="categoryManagerVisible" @refresh="loadCategories"/>
  </div>
</template>

<script>
// 引入分类管理组件
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
      name: '',
      author: '',
      categoryId: null,
      loading: false,

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
        ],
        price: [
          {required: true, message: '请输入价格', trigger: 'blur'},
          {validator: this.validatePrice, trigger: 'blur'}
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
        console.log('图书列表响应:', res)

        if (res.code === '200') {
          this.tableData = res.data?.list || []
          this.total = res.data?.total || 0
        } else {
          this.$message.error(res.msg || '获取图书列表失败')
        }
      } catch (error) {
        console.error('加载图书列表出错:', error)
        this.$message.error('加载失败，请检查网络连接')
      } finally {
        this.loading = false
      }
    },

    // 加载分类数据
    async loadCategories() {
      console.log('开始加载分类数据...')
      try {
        const res = await this.$request.get('/category/selectAll')
        console.log('分类接口响应:', res)

        if (res.code === '200') {
          this.categories = res.data || []
          console.log('分类数据已加载:', this.categories.length, '条')
        } else {
          console.error('获取分类失败:', res.msg)
          this.$message.error('获取分类失败: ' + (res.msg || '未知错误'))
        }
      } catch (error) {
        console.error('获取分类异常:', error)
        this.$message.error('获取分类失败，请检查网络连接')
      }
    },

    // 根据ID获取分类名称
    getCategoryName(categoryId) {
      if (!categoryId) return '未分类'

      // 确保比较的是相同类型
      const catIdStr = String(categoryId)
      const cat = this.categories.find(item => String(item.id) === catIdStr)

      return cat ? cat.name : '未知'
    },

    // 搜索
    handleFilter() {
      console.log('执行搜索，参数:', {name: this.name, author: this.author, categoryId: this.categoryId})
      this.load(1)
    },

    // 重置
    reset() {
      this.name = ''
      this.author = ''
      this.categoryId = null
      this.load(1)
    },

    // 新增图书
    handleAdd() {
      console.log('点击新增图书')
      this.form = {
        total: 1,
        available: 1,
        price: ''  // 初始化为空字符串，让用户输入
      }
      this.bookFormVisible = true
      // 确保清空表单验证状态
      this.$nextTick(() => {
        if (this.$refs.formRef) {
          this.$refs.formRef.clearValidate()
        }
      })
    },

    // 编辑图书
    handleEdit(row) {
      console.log('编辑图书:', row)
      this.form = JSON.parse(JSON.stringify(row))

      // 确保价格字段是字符串类型（Element UI 的 el-input 期望字符串）
      if (this.form.price !== null && this.form.price !== undefined) {
        this.form.price = this.form.price.toString()
      }

      this.bookFormVisible = true
      this.$nextTick(() => {
        if (this.$refs.formRef) {
          this.$refs.formRef.clearValidate()
        }
      })
    },

    // 保存图书（新增或修改）
    async saveBook() {
      console.log('保存图书，表单数据:', this.form)
      try {
        // 验证表单
        const valid = await this.$refs.formRef.validate()
        if (!valid) {
          console.log('表单验证失败')
          return
        }

        // 处理价格字段：确保发送的是数字
        const formData = JSON.parse(JSON.stringify(this.form))
        if (formData.price !== '' && formData.price !== null && formData.price !== undefined) {
          formData.price = parseFloat(formData.price)
        }

        // 确保新增时，可借数量等于总数量
        if (!formData.id) {
          formData.available = formData.total
        }

        // 根据是否有ID决定是更新还是新增
        const url = formData.id ? '/book/update' : '/book/add'
        const method = formData.id ? 'put' : 'post'

        console.log('发送请求:', url, method, formData)
        const res = await this.$request({
          url: url,
          method: method,
          data: formData
        })

        console.log('保存响应:', res)
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.load(1) // 重新加载列表
          this.bookFormVisible = false
        } else {
          this.$message.error(res.msg || '保存失败')
        }
      } catch (error) {
        console.error('保存图书出错:', error)
        this.$message.error('保存失败: ' + (error.message || '未知错误'))
      }
    },

    // 删除单本图书
    async del(id) {
      console.log('开始删除图书，ID:', id)

      try {
        // 显示确认对话框
        await this.$confirm('确定删除这本图书吗？如果该图书有借阅记录，删除可能会失败。', '确认删除', {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'warning'
        })

        console.log('用户确认删除，调用API...')

        // 调用删除接口
        const res = await this.$request.delete('/book/delete/' + id)
        console.log('删除接口响应:', res)

        if (res.code === '200') {
          this.$message.success('删除成功')
          // 从当前列表中移除已删除的图书
          this.tableData = this.tableData.filter(item => item.id !== id)
          this.total--

          // 如果当前页没有数据了，且不是第一页，则返回上一页
          if (this.tableData.length === 0 && this.pageNum > 1) {
            this.pageNum--
            this.load(this.pageNum)
          }
        } else {
          // 显示具体的错误信息
          let errorMsg = res.msg || '删除失败'

          // 根据错误信息提供更友好的提示
          if (errorMsg.includes('关联') || errorMsg.includes('借阅') || errorMsg.includes('外键')) {
            errorMsg = '删除失败：该图书存在借阅记录，请先处理相关借阅信息'
          } else if (errorMsg.includes('系统异常')) {
            errorMsg = '删除失败：服务器处理异常，请稍后重试'
          }

          this.$message.error(errorMsg)
          console.error('删除失败详细原因:', res)
        }
      } catch (error) {
        console.log('删除操作取消或异常:', error)

        // 如果是用户取消操作，不显示错误信息
        if (error !== 'cancel' && error.message !== 'cancel') {
          this.$message.error('删除失败：' + (error.message || '未知错误'))
        }
      }
    },

    // 表格多选
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)
      console.log('选中图书IDs:', this.ids)
    },

    // 批量删除
    async delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择要删除的图书')
        return
      }

      console.log('批量删除IDs:', this.ids)
      try {
        await this.$confirm(`确定批量删除这 ${this.ids.length} 本图书吗？`, '确认删除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 逐个删除
        let successCount = 0
        const deleteNext = async (index) => {
          if (index >= this.ids.length) {
            this.$message.success(`成功删除 ${successCount} 本图书`)
            this.load(1)
            this.ids = []
            return
          }

          try {
            const res = await this.$request.delete('/book/delete/' + this.ids[index])
            if (res.code === '200') successCount++
            await deleteNext(index + 1)
          } catch (error) {
            console.error('删除单个图书失败:', error)
            await deleteNext(index + 1)
          }
        }

        await deleteNext(0)
      } catch (error) {
        console.log('取消批量删除操作')
      }
    },

    // 低库存查询功能
    async showLowStock() {
      console.log('查询低库存图书')
      try {
        const res = await this.$request.get('/book/lowStock')
        console.log('低库存响应:', res)

        if (res.code === '200' && res.data && res.data.length > 0) {
          const bookNames = res.data.map(book => book.name).join('、')
          this.$alert(`发现${res.data.length}本低库存图书：\n\n${bookNames}`, '低库存提醒', {
            confirmButtonText: '知道了'
          })
        } else {
          this.$message.info('当前没有低库存图书')
        }
      } catch (error) {
        console.error('查询低库存出错:', error)
        this.$message.error('查询失败')
      }
    },
    // 自定义价格验证方法
    validatePrice(rule, value, callback) {
      if (value === '' || value === null || value === undefined) {
        callback(new Error('请输入价格'))
        return
      }

      // 将值转换为数字
      const numValue = parseFloat(value)

      // 检查是否为有效的数字
      if (isNaN(numValue)) {
        callback(new Error('请输入有效的数字'))
        return
      }

      // 检查是否为正数
      if (numValue < 0) {
        callback(new Error('价格不能为负数'))
        return
      }

      // 检查小数位数（可选）
      if (value.toString().split('.')[1] && value.toString().split('.')[1].length > 2) {
        callback(new Error('价格最多保留两位小数'))
        return
      }

      callback()
    },

    // 显示分类管理弹窗
    showCategoryManager() {
      console.log('显示分类管理')
      this.categoryManagerVisible = true
    },

    // 分页切换
    handleCurrentChange(pageNum) {
      console.log('分页切换:', pageNum)
      this.load(pageNum)
    },

    // 处理借阅（从表格操作列）
    handleOneBorrow(row) {
      if (row.available < 1) {
        this.$message.warning('该图书暂无库存可借')
        return
      }
      console.log('借阅图书:', row)
      this.currentBook = row
      this.borrowForm.userId = '' // 清空之前输入的学生ID
      this.borrowFormVisible = true
    },

    // 顶部"办理借阅"按钮（需先选择图书）
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

      console.log('执行借阅操作:', {
        bookId: this.currentBook.id,
        userId: this.borrowForm.userId,
        adminId: this.borrowForm.adminId
      })

      try {
        const res = await this.$request.post('/book/borrow', null, {
          params: {
            bookId: this.currentBook.id,
            userId: this.borrowForm.userId,
            adminId: this.borrowForm.adminId
          }
        })

        console.log('借阅响应:', res)
        if (res.code === '200') {
          this.$message.success('借阅成功')
          this.borrowFormVisible = false
          this.load(1) // 刷新列表，更新库存
        } else {
          this.$message.error(res.msg || '借阅失败')
        }
      } catch (error) {
        console.error('借阅出错:', error)
        this.$message.error('借阅失败: ' + (error.message || '未知错误'))
      }
    }
  }
}
</script>

<style scoped>
.search, .operation {
  margin-bottom: 15px;
}
</style>