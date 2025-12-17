<template>
  <el-dialog title="分类管理" :visible.sync="dialogVisible" width="60%" :close-on-click-modal="false" @open="onDialogOpen">
    <!-- 搜索和操作区 -->
    <div style="margin-bottom: 20px;">
      <el-input v-model="searchName" placeholder="请输入分类名称" style="width: 200px;" clearable @keyup.enter.native="loadCategories"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="loadCategories">搜索</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="resetSearch">重置</el-button>
      <el-button type="primary" plain style="margin-left: 20px" @click="handleAdd">新增分类</el-button>
    </div>

    <!-- 分类数据表格 -->
    <el-table v-loading="loading" :data="tableData" border stripe style="width: 100%; max-height: 400px; overflow-y: auto;">
      <el-table-column prop="id" label="ID" width="80" align="center" sortable></el-table-column>
      <el-table-column prop="name" label="分类名称"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template v-slot="scope">
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
              type="danger"
              size="mini"
              @click="handleDelete(scope.row.id, scope.row.name)"
              :loading="deletingId === scope.row.id">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div style="margin-top: 15px; text-align: center;">
      <el-pagination
          background
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

    <!-- 新增/编辑分类的对话框 -->
    <el-dialog :title="formTitle" :visible.sync="innerDialogVisible" width="30%" append-to-body :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="例如：计算机科学、文学小说"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="form.description" placeholder="请输入分类描述（可选）" :rows="3"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="innerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCategory">确定</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
export default {
  name: 'CategoryManager',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      // 主表格数据
      tableData: [],
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      searchName: '',
      deletingId: null, // 正在删除的分类ID

      // 内层表单对话框
      innerDialogVisible: false,
      formTitle: '',
      form: {
        id: null,
        name: '',
        description: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
          { max: 20, message: '长度不超过20个字符', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        console.log('分类管理对话框打开')
        // 延迟一下，确保DOM渲染完成
        this.$nextTick(() => {
          this.loadCategories()
        })
      } else {
        console.log('分类管理对话框关闭')
        this.resetSearch()
      }
    }
  },
  methods: {
    // 对话框打开时的回调
    onDialogOpen() {
      console.log('onDialogOpen 被调用')
      this.loadCategories()
    },

    // 加载分类列表
    async loadCategories() {
      console.log('开始加载分类列表...')
      this.loading = true
      try {
        const res = await this.$request.get('/category/selectAll')
        console.log('分类接口响应:', res)

        if (res.code === '200') {
          let allData = res.data || []
          console.log('原始分类数据:', allData)

          // 前端搜索筛选
          if (this.searchName) {
            allData = allData.filter(item =>
                item.name && item.name.toLowerCase().includes(this.searchName.toLowerCase())
            )
            console.log('搜索后数据:', allData)
          }

          this.total = allData.length

          // 前端分页
          const start = (this.pageNum - 1) * this.pageSize
          const end = start + this.pageSize
          this.tableData = allData.slice(start, end)
          console.log('分页后表格数据:', this.tableData)

          if (this.tableData.length === 0) {
            console.log('表格数据为空，检查数据源:', allData)
          }
        } else {
          console.error('获取分类失败，返回码:', res.code, '消息:', res.msg)
          this.$message.error(res.msg || '获取分类失败')
        }
      } catch (error) {
        console.error('获取分类异常:', error)
        this.$message.error('获取分类失败，请检查网络连接')
      } finally {
        this.loading = false
      }
    },

    // 重置搜索
    resetSearch() {
      this.searchName = ''
      this.pageNum = 1
      // 这里不自动调用load，由watch或按钮触发
    },

    // 处理新增
    handleAdd() {
      console.log('处理新增分类')
      this.form = { id: null, name: '', description: '' }
      this.formTitle = '新增分类'
      this.innerDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.formRef) {
          this.$refs.formRef.clearValidate()
        }
      })
    },

    // 处理编辑
    handleEdit(row) {
      console.log('编辑分类:', row)
      this.form = JSON.parse(JSON.stringify(row))
      this.formTitle = '编辑分类'
      this.innerDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.formRef) {
          this.$refs.formRef.clearValidate()
        }
      })
    },

    // 保存分类（新增或修改）
    async saveCategory() {
      console.log('保存分类，表单数据:', this.form)
      try {
        const valid = await this.$refs.formRef.validate()
        if (!valid) {
          console.log('表单验证失败')
          return
        }

        const url = this.form.id ? '/category/update' : '/category/add'
        const method = this.form.id ? 'put' : 'post'

        console.log('发送请求:', url, method, this.form)
        const res = await this.$request({
          url: url,
          method: method,
          data: this.form
        })

        console.log('保存分类响应:', res)
        if (res.code === '200') {
          this.$message.success(this.form.id ? '修改成功' : '添加成功')
          this.innerDialogVisible = false
          this.loadCategories() // 刷新列表
          this.$emit('refresh') // 通知父组件刷新下拉框
        } else {
          this.$message.error(res.msg || '操作失败')
        }
      } catch (error) {
        console.error('保存分类出错:', error)
        this.$message.error('保存失败: ' + (error.message || '未知错误'))
      }
    },

    // 删除分类
    async handleDelete(id, name) {
      console.log('删除分类，ID:', id, '名称:', name)

      try {
        await this.$confirm(
            `确定要删除分类 "${name}" 吗？如果该分类下有图书，删除可能会失败。`,
            '确认删除',
            {
              confirmButtonText: '确定删除',
              cancelButtonText: '再想想',
              type: 'warning'
            }
        )

        // 设置删除状态
        this.deletingId = id

        const res = await this.$request.delete('/category/delete/' + id)
        console.log('删除分类响应:', res)

        if (res.code === '200') {
          this.$message.success('删除成功')
          this.loadCategories() // 刷新列表
          this.$emit('refresh') // 通知父组件刷新下拉框
        } else {
          let errorMsg = res.msg || '删除失败'
          if (errorMsg.includes('关联') || errorMsg.includes('图书')) {
            errorMsg = `删除失败：分类"${name}"下还有图书，请先处理这些图书`
          }
          this.$message.error(errorMsg)
        }
      } catch (error) {
        console.log('取消删除或删除失败:', error)
        // 如果是用户取消操作，不做处理
        if (error !== 'cancel' && error.message !== 'cancel') {
          this.$message.error('删除失败: ' + (error.message || '未知错误'))
        }
      } finally {
        this.deletingId = null
      }
    },

    // 分页切换
    handleCurrentChange(pageNum) {
      console.log('分页切换:', pageNum)
      this.pageNum = pageNum
      this.loadCategories()
    }
  }
}
</script>

<style scoped>
.el-table {
  margin-top: 10px;
}
</style>