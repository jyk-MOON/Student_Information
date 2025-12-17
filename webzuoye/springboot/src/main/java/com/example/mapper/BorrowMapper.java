package com.example.mapper;

import com.example.entity.Borrow;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

public interface BorrowMapper {
    int insert(Borrow borrow);
    int updateById(Borrow borrow);
    Borrow selectById(Integer id);
    List<Borrow> selectAll(Borrow borrow);
    List<Borrow> selectByUserId(Integer userId);
    List<Borrow> selectOverdue(@Param("date") Date date);
    int updateStatusToOverdue(@Param("date") Date date);
}