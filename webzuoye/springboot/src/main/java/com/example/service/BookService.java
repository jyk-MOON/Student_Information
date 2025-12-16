package com.example.service;

import com.example.entity.Book;
import com.example.entity.Borrow;
import com.github.pagehelper.PageInfo;
import java.util.List;

public interface BookService {
    // 基础CRUD
    void add(Book book);
    void deleteById(Integer id);
    void deleteBatch(List<Integer> ids);
    void updateById(Book book);
    Book selectById(Integer id);
    List<Book> selectAll(Book book);
    PageInfo<Book> selectPage(Book book, Integer pageNum, Integer pageSize);

    // 核心业务
    void borrow(Integer bookId, Integer userId, Integer adminId);
    void returnBook(Integer borrowId, Integer adminId);

    // 库存管理
    List<Book> selectLowStock(Integer threshold);

    // 借阅记录
    List<Borrow> selectBorrowAll(Borrow borrow);
    PageInfo<Borrow> selectBorrowPage(Borrow borrow, Integer pageNum, Integer pageSize);
    List<Borrow> selectBorrowByUserId(Integer userId);
}