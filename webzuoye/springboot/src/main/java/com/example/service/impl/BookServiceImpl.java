package com.example.service.impl;

import com.example.entity.Book;
import com.example.entity.Borrow;
import com.example.exception.CustomException;
import com.example.mapper.BookMapper;
import com.example.mapper.BorrowMapper;
import com.example.service.BookService;
import com.example.service.UserTService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Resource
    private BookMapper bookMapper;
    @Resource
    private BorrowMapper borrowMapper;
    @Resource
    private UserTService userTService;

    @Override
    public void add(Book book) {
        if (book.getTotal() == null) book.setTotal(0);
        if (book.getAvailable() == null) book.setAvailable(book.getTotal());
        book.setCreateTime(new Date());
        bookMapper.insert(book);
    }

    @Override
    public void deleteById(Integer id) {
        Borrow query = new Borrow();
        query.setBookId(id);
        query.setStatus("0");
        List<Borrow> list = borrowMapper.selectAll(query);
        if (!list.isEmpty()) {
            throw new CustomException("500", "图书已被借出，无法删除");
        }
        bookMapper.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) deleteById(id);
    }

    @Override
    public void updateById(Book book) {
        Book oldBook = bookMapper.selectById(book.getId());
        if (oldBook == null) throw new CustomException("404", "图书不存在");

        if (book.getTotal() != null && !book.getTotal().equals(oldBook.getTotal())) {
            int borrowed = oldBook.getTotal() - oldBook.getAvailable();
            book.setAvailable(book.getTotal() - borrowed);
            if (book.getAvailable() < 0) throw new CustomException("500", "库存数量设置错误");
        }
        bookMapper.updateById(book);
    }

    @Override
    public Book selectById(Integer id) {
        Book book = bookMapper.selectById(id);
        if (book == null) throw new CustomException("404", "图书不存在");
        return book;
    }

    @Override
    public List<Book> selectAll(Book book) {
        return bookMapper.selectAll(book);
    }

    @Override
    public PageInfo<Book> selectPage(Book book, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Book> list = bookMapper.selectAll(book);
        return PageInfo.of(list);
    }

    @Override
    @Transactional
    public void borrow(Integer bookId, Integer userId, Integer adminId) {
        if (userTService.selectById(adminId) == null) {
            throw new CustomException("404", "操作员不存在");
        }

        Book book = bookMapper.selectById(bookId);
        if (book == null) throw new CustomException("404", "图书不存在");
        if (book.getAvailable() <= 0) throw new CustomException("500", "图书库存不足");

        Borrow borrow = new Borrow();
        borrow.setBookId(bookId);
        borrow.setUserId(userId);
        borrow.setAdminId(adminId);
        borrow.setBorrowTime(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        borrow.setDueTime(calendar.getTime());
        borrow.setStatus("0");
        borrowMapper.insert(borrow);

        int rows = bookMapper.updateAvailable(bookId, -1);
        if (rows == 0) throw new CustomException("500", "更新库存失败");

        log.info("借阅成功：图书ID={}, 用户ID={}", bookId, userId);
    }

    @Override
    @Transactional
    public void returnBook(Integer borrowId, Integer adminId) {
        if (userTService.selectById(adminId) == null) {
            throw new CustomException("404", "操作员不存在");
        }

        Borrow borrow = borrowMapper.selectById(borrowId);
        if (borrow == null || !"0".equals(borrow.getStatus())) {
            throw new CustomException("500", "借阅记录无效");
        }

        borrow.setReturnTime(new Date());
        borrow.setAdminId(adminId);

        Date now = new Date();
        if (now.after(borrow.getDueTime())) {
            long days = (now.getTime() - borrow.getDueTime().getTime()) / (1000 * 60 * 60 * 24);
            borrow.setOverdueDays((int) days);
            borrow.setFine(BigDecimal.valueOf(days * 0.5));
            borrow.setStatus("2");
        } else {
            borrow.setStatus("1");
        }

        borrowMapper.updateById(borrow);
        bookMapper.updateAvailable(borrow.getBookId(), 1);

        log.info("归还成功：借阅记录ID={}", borrowId);
    }

    @Override
    public List<Book> selectLowStock(Integer threshold) {
        Integer actualThreshold = (threshold != null) ? threshold : 5;
        return bookMapper.selectLowStock(actualThreshold);
    }

    @Override
    public List<Borrow> selectBorrowAll(Borrow borrow) {
        return borrowMapper.selectAll(borrow);
    }

    @Override
    public PageInfo<Borrow> selectBorrowPage(Borrow borrow, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Borrow> list = borrowMapper.selectAll(borrow);
        return PageInfo.of(list);
    }

    @Override
    public List<Borrow> selectBorrowByUserId(Integer userId) {
        return borrowMapper.selectByUserId(userId);
    }
}