package com.zian.controller;

import com.zian.dao.entity.User;
import com.zian.model.request.UserLoginRequest;
import com.zian.model.request.UserRegisterRequest;
import com.zian.service.user.UserService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zian.constant.UserConstant.ADMIN_ROLE;
import static com.zian.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping(value = "/register")
    public long userRegister (@RequestBody UserRegisterRequest req) {
        if(req == null) {
            return -1;
        }
        return userService.userRegister(req.getUserAccount(), req.getPassword(), req.getCheckpwd());
    }

    @PostMapping("/login")
    public User userLogin (@RequestBody UserLoginRequest req, HttpServletRequest request) {
        if(req == null) {
            return null;
        }
        return userService.userLogin(req.getUsername(), req.getPassword(), request);
    }

    @GetMapping("/search")
    public List<User> searchUserByIds (@RequestBody List<Long> ids, HttpServletRequest request) {
        if(ids == null || ids.isEmpty()) {
            return null;
        }
        if(!isAdmin(request)) {
            return null;
        }
        return userService.searchUser(ids).stream()
                .map(user -> userService.anonymize(user))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteUserById(@PathVariable Long id, HttpServletRequest request) {
        if(id == null) {
            return false;
        }
        if(!isAdmin(request)) {
            return false;
        }
        userService.getBaseMapper().deleteById(id);
        return true;
    }

    /**
     * 鉴权
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
