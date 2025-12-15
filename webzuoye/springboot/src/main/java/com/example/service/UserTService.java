package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.UserT;
import com.example.exception.CustomException;
import com.example.mapper.UserTMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员业务处理
 **/
@Service
public class UserTService {

    @Resource
    private UserTMapper userTMapper;

    /**
     * 新增
     */
    public void add(UserT userT) {
        UserT dbUserT = userTMapper.selectByName(userT.getName());
        if (ObjectUtil.isNotNull(dbUserT)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(userT.getPassword())) {
            userT.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(userT.getName())) {
            userT.setName(userT.getName());
        }
        userT.setRole(RoleEnum.USERT.name());
        userTMapper.insert(userT);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        userTMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            userTMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(UserT userT) {
        userTMapper.updateById(userT);
    }

    /**
     * 根据ID查询
     */
    public UserT selectById(Integer id) {
        return userTMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<UserT> selectAll(UserT userT) {
        return userTMapper.selectAll(userT);
    }

    /**
     * 分页查询
     */
    public PageInfo<UserT> selectPage(UserT userT, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserT> list = userTMapper.selectAll(userT);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Account dbUserT = userTMapper.selectByName(account.getName());
        if (ObjectUtil.isNull(dbUserT)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUserT.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbUserT.getId() + "-" + RoleEnum.USERT.name();
        String token = TokenUtils.createToken(tokenData, dbUserT.getPassword());
        dbUserT.setToken(token);
        return dbUserT;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        UserT userT = new UserT();
        BeanUtils.copyProperties(account, userT);
        add(userT);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        UserT dbUserT = userTMapper.selectByName(account.getName());
        if (ObjectUtil.isNull(dbUserT)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUserT.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUserT.setPassword(account.getNewPassword());
        userTMapper.updateById(dbUserT);
    }

}