package com.zian.service.user.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zian.common.exception.BusinessException;
import com.zian.constant.UserConstant;
import com.zian.dao.entity.User;
import com.zian.dao.mapper.UserMapper;
import com.zian.service.user.UserService;
import com.zian.utils.AccountValidator;
import com.zian.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import static com.zian.constant.BaseResponseCode.*;
import static com.zian.constant.UserConstant.ADMIN_ROLE;
import static com.zian.constant.UserConstant.USER_LOGIN_STATE;

/**
 *  用户服务实现类
 *  @author Wang Zijian
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public long userRegister(String userAccount, String password, String checkPwd) {
        // 校验逻辑
        if (StringUtils.isAnyBlank(userAccount, password, checkPwd)){
            throw new BusinessException(PARAM_ERR, "参数为空");
        }
        if (userAccount.length() < 4 || (password.length() < 8 || checkPwd.length() < 8)) {
            throw new BusinessException(PARAM_ERR, "账号或密码太短");
        }
        if (!checkPwd.equals(password)) {
            throw new BusinessException(PARAM_ERR, "两次输入密码不一致");
        }
        // 账户不能包含特殊字符
        if(!AccountValidator.isValidAccount(userAccount)) {
            throw new BusinessException(PARAM_ERR, "账号包含特殊字符");
        }

        // 账户不能重复
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("user_account", userAccount);
        if (this.count(qw) >= 1) {
            throw new BusinessException(PARAM_ERR, "账号重复");
        }

        // 加密密码
        String newPwd = MD5Util.md5WithSalt(password, MD5Util.SALT);

        // 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(newPwd);
        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(SYSTEM_ERR, "保存用户信息失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String password, HttpServletRequest request) {
        // 校验逻辑
        if (StringUtils.isAnyBlank(userAccount, password)){
            throw new BusinessException(PARAM_ERR, "参数为空");
        }
        if (userAccount.length() < 4 || password.length() < 8){
            throw new BusinessException(PARAM_ERR, "账号或密码太短");
        }
        if(!AccountValidator.isValidAccount(userAccount)) {
            throw new BusinessException(NO_AUTH, "权限不足");
        }
        String newPwd = MD5Util.md5WithSalt(password, MD5Util.SALT);
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("user_account", userAccount);
        qw.eq("password", newPwd);
        User user = this.baseMapper.selectOne(qw);
        if (user == null) {
            throw new BusinessException(PARAM_ERR, "账号或密码错误");
        }
        // 脱敏
        User newUser = anonymize(user);
        // 记录用户登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, newUser);
        return newUser;
    }

    @Override
    public User anonymize(User user) {
        if(user == null) {
            throw new BusinessException(PARAM_ERR, "请求参数为空");
        }
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setUserRole(user.getUserRole());
        newUser.setUserAccount(user.getUserAccount());
        newUser.setUserName(user.getUserName());
        newUser.setGender(user.getGender());
        newUser.setAge(user.getAge());
        newUser.setAvartarUrl(user.getAvartarUrl());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setEmail(user.getEmail());
        newUser.setUserStatus(user.getUserStatus());
        return newUser;
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        User curUser = (User)request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(curUser == null) {
            throw new BusinessException(CONTENT_EMPTY, "当前用户为空");
        }
        // todo 用户合法性检验
        long curId = curUser.getId();
        return anonymize(getById(curId));
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    @Override
    public void userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }


    @Override
    public List<User> searchUser(List<Long> ids) {
        if(ids == null || ids.size() == 0){
            return Collections.emptyList();
        }
        List<User> users = this.listByIds(ids);
        return users;
    }
}
