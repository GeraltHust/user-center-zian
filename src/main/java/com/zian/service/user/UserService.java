package com.zian.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zian.dao.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends IService<User> {


    /**
     * 用户注册
     * @param userAccount 账户
     * @param password 密码
     * @param checkPwd 确认密码
     * @return
     */
    long userRegister(String userAccount, String password, String checkPwd);

    /**
     * 用户登录
     * @param userAccount
     * @param password
     * @return
     */
    User userLogin(String userAccount, String password, HttpServletRequest request);

    /**
     * 根据id查询用户
     * @param ids
     * @return
     */
    List<User> searchUser(List<Long> ids);

    /**
     * 用户信息脱敏
     * @param user
     * @return
     */
    User anonymize(User user);

    /**
     * 获取当前登录的用户
     * @param request
     * @return
     */
    User getCurrentUser(HttpServletRequest request);

    /**
     * 检测是否是管理员用户
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);
}
