// src/api/book.js
// 图书管理相关API，风格与您现有的请求方式保持一致

export default {
    // ==================== 分类管理 ====================
    // 查询全部分类
    selectAllCategory() {
        return this.$request.get('/category/selectAll');
    },
    // 添加分类
    addCategory(data) {
        return this.$request.post('/category/add', data);
    },
    // 修改分类
    updateCategory(data) {
        return this.$request.put('/category/update', data);
    },
    // 删除分类
    deleteCategory(id) {
        return this.$request.delete('/category/delete/' + id);
    },

    // ==================== 图书管理 ====================
    // 分页查询图书
    selectBookPage(params) {
        return this.$request.get('/book/selectPage', { params: params });
    },
    // 添加图书
    addBook(data) {
        return this.$request.post('/book/add', data);
    },
    // 修改图书
    updateBook(data) {
        return this.$request.put('/book/update', data);
    },
    // 删除图书
    deleteBook(id) {
        return this.$request.delete('/book/delete/' + id);
    },
    // 查询低库存图书
    selectLowStock() {
        return this.$request.get('/book/lowStock');
    },

    // ==================== 借阅管理 ====================
    // 办理借阅
    borrowBook(bookId, userId, adminId) {
        return this.$request.post('/book/borrow', null, {
            params: { bookId, userId, adminId }
        });
    },
    // 办理归还
    returnBook(borrowId, adminId) {
        return this.$request.post('/book/return/' + borrowId, null, {
            params: { adminId }
        });
    },
    // 分页查询借阅记录
    selectBorrowPage(params) {
        return this.$request.get('/book/borrow/selectPage', { params: params });
    },
    // 按学生查询借阅记录
    selectBorrowByUser(userId) {
        return this.$request.get('/book/borrow/user/' + userId);
    }
}