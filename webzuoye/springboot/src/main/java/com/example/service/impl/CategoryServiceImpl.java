package com.example.service.impl;

import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import com.example.service.CategoryService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Category add(Category category) {  // 修改返回类型
        categoryMapper.insert(category);
        // 这里category对象已经通过MyBatis的useGeneratedKeys="true"获得了自增ID
        return category;  // 返回包含ID的category对象
    }

    @Override
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public void updateById(Category category) {
        categoryMapper.updateById(category);
    }

    @Override
    public List<Category> selectAll() {
        return categoryMapper.selectAll(new Category());
    }
}