package com.example.mapper;

import com.example.entity.Book;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface BookMapper {
    int insert(Book book);
    int deleteById(Integer id);
    int deleteBatch(List<Integer> ids);
    int updateById(Book book);
    Book selectById(Integer id);
    List<Book> selectAll(Book book);
    int updateAvailable(@Param("id") Integer id, @Param("num") Integer num);
    List<Book> selectLowStock(@Param("threshold") Integer threshold);
}