package com.example.service;

import com.example.entity.Category;
import java.util.List;

public interface CategoryService {
    Category add(Category category);  // 改为返回Category对象，而不是void
    void deleteById(Integer id);
    void updateById(Category category);
    List<Category> selectAll();
}