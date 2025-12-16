<template>
  <el-dialog title="分类管理" :visible.sync="dialogVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
    <!-- 搜索和操作区 -->
    <div style="margin-bottom: 20px;">
      <el-input v-model="searchName" placeholder="请输入分类名称" style="width: 200px;" clearable @clear="load(1)" @keyup.enter.native="load(1)"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">搜索</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="resetSearch">重置</el-button>
      <el-button type="primary" plain style="margin-left: 20px" @click="handleAdd">新增分类</el-button>
    </div>

    <!-- 分类数据表格 -->
    <el-table v-loading="loading" :data="tableData" border stripe height="350">
      <el-table-column prop="id" label="ID" width="80" align="center" sortable></el-table-column>
      <el-table-column prop="name" label="分类名称"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template v-slot="scope">
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row.id)">删除</el-button>
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
import bookApi from '@/api/book'

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
        this.load(1) // 每次打开弹窗都重新加载数据
      } else {
        this.resetSearch() // 关闭时重置内部状态
      }
    }
  },
  methods: {
    // 加载分类列表
    async load() {
      this.loading = true
      try {
        const res = await this.$request.get('/category/selectAll')
        if (res.code === '200') {
          let allData = res.data || []

          // 前端搜索筛选
          if (this.searchName) {
            allData = allData.filter(item =>
                item.name && item.name.toLowerCase().includes(this.searchName.toLowerCase())
            )
          }

          this.total = allData.length

          // 前端分页
          const start = (this.pageNum - 1) * this.pageSize
          const end = start + this.pageSize
          this.tableData = allData.slice(start, end)
        } else {
          this.$message.error(res.msg || '获取分类失败')
        }
      } catch (error) {
        console.error('获取分类失败:', error)
        this.$message.error('获取分类失败')
      } finally {
        this.loading = false
      }
    },

    // 重置搜索
    resetSearch() {
      this.searchName = ''
      this.pageNum = 1
      // 注意：这里不自动调用load，由watch触发
    },

    // 处理新增
    handleAdd() {
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
      try {
        const valid = await this.$refs.formRef.validate()
        if (!valid) return

        const url = this.form.id ? '/category/update' : '/category/add'
        const method = this.form.id ? 'put' : 'post'

        const res = await this.$request({
          url: url,
          method: method,
          data: this.form
        })

        if (res.code === '200') {
          this.$message.success(this.form.id ? '修改成功' : '添加成功')
          this.innerDialogVisible = false
          this.load() // 刷新列表
          this.$emit('refresh') // 通知父组件刷新下拉框
        } else {
          this.$message.error(res.msg || '操作失败')
        }
      } catch (error) {
        console.error('保存分类出错:', error)
      }
    },

    // 删除分类
    handleDelete(id) {
      // 删除前的检查：这里可以添加检查该分类下是否有图书的逻辑
      // 假设我们直接删除，实际应用中可能需要先检查
      this.$confirm('确定要删除这个分类吗？如果该分类下有图书，删除可能会失败。', '确认删除', {
        type: 'warning',
        confirmButtonText: '确定删除',
        cancelButtonText: '再想想'
      }).then(() => {
        bookApi.deleteCategory(id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load(1) // 刷新列表
            this.$emit('refresh') // 通知父组件刷新下拉框
          } else {
            this.$message.error(res.msg || '删除失败，可能该分类下仍有图书')
          }
        })
      }).catch(() => {})
    },

    // 分页切换
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    }
  }
}
</script>

<style scoped>
/* 可以添加一些自定义样式 */
.el-table {
  margin-top: 10px;
}
</style>