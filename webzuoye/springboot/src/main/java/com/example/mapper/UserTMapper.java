package com.example.mapper;

import com.example.entity.User;
import com.example.entity.UserT;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作userT相关数据接口
*/
public interface UserTMapper {

    /**
      * 新增
    */
    int insert(UserT userT);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(UserT userT);

    /**
      * 根据ID查询
    */
    UserT selectById(Integer id);

    /**
      * 查询所有
    */
    List<UserT> selectAll(UserT userT);


    @Select("select * from userT where name = #{name}")
    UserT selectByName(String name);
}