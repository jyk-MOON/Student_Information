package com.example.controller;

import com.example.common.Result;
import com.example.entity.UserT;
import com.example.service.UserService;
import com.example.service.UserTService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员前端操作接口
 **/
@RestController
@RequestMapping("/userT")
public class UserTController {

    @Resource
    private UserTService userTService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody UserT userT) {
        userTService.add(userT);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        userTService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userTService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody UserT userT) {
        userTService.updateById(userT);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        UserT userT = userTService.selectById(id);
        return Result.success(userT);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(UserT userT ) {
        List<UserT> list = userTService.selectAll(userT);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(UserT userT,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<UserT> page = userTService.selectPage(userT, pageNum, pageSize);
        return Result.success(page);
    }

}