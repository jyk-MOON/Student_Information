package com.example.controller;

import com.example.common.Result;
import com.example.entity.Book;
import com.example.entity.Borrow;
import com.example.service.BookService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookService bookService;

    // 图书增删改查
    @PostMapping("/add")
    public Result add(@RequestBody Book book) {
        bookService.add(book);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        bookService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        bookService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Book book) {
        bookService.updateById(book);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Book book = bookService.selectById(id);
        return Result.success(book);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Book book) {
        List<Book> list = bookService.selectAll(book);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Book book,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Book> page = bookService.selectPage(book, pageNum, pageSize);
        return Result.success(page);
    }

    // 借阅归还功能
    @PostMapping("/borrow")
    public Result borrow(@RequestParam Integer bookId,
                         @RequestParam Integer userId,
                         @RequestParam Integer adminId) {
        bookService.borrow(bookId, userId, adminId);
        return Result.success();
    }

    @PostMapping("/return/{borrowId}")
    public Result returnBook(@PathVariable Integer borrowId,
                             @RequestParam Integer adminId) {
        bookService.returnBook(borrowId, adminId);
        return Result.success();
    }

    // 库存管理功能
    @GetMapping("/lowStock")
    public Result lowStock(@RequestParam(required = false) Integer threshold) {
        List<Book> list = bookService.selectLowStock(threshold);
        return Result.success(list);
    }

    // 借阅记录与历史
    @GetMapping("/borrow/selectAll")
    public Result selectBorrowAll(Borrow borrow) {
        List<Borrow> list = bookService.selectBorrowAll(borrow);
        return Result.success(list);
    }

    @GetMapping("/borrow/selectPage")
    public Result selectBorrowPage(Borrow borrow,
                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Borrow> page = bookService.selectBorrowPage(borrow, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/borrow/user/{userId}")
    public Result selectBorrowByUserId(@PathVariable Integer userId) {
        List<Borrow> list = bookService.selectBorrowByUserId(userId);
        return Result.success(list);
    }
}