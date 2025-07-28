package com.zian.controller;

import com.zian.common.response.BaseResponse;
import com.zian.dao.entity.User;
import com.zian.model.request.UserLoginRequest;
import com.zian.model.request.UserRegisterRequest;
import com.zian.service.user.UserService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import com.zian.constant.BaseResponseCode;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping(value = "/register")
    public BaseResponse<Long> userRegister (@RequestBody UserRegisterRequest req) {
        if(req == null) {
            return new BaseResponse<>(BaseResponseCode.PARAM_ERR, null);
        }
        Long res = userService.userRegister(req.getUserAccount(), req.getPassword(), req.getCheckpwd());
        return new BaseResponse<>(BaseResponseCode.SUCCESS, res, "ok");
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin (@RequestBody UserLoginRequest req, HttpServletRequest request) {
        if(req == null) {
            return new BaseResponse<>(BaseResponseCode.PARAM_ERR, null);
        }
        User user = userService.userLogin(req.getUsername(), req.getPassword(), request);
        return new BaseResponse<>(BaseResponseCode.SUCCESS, user, "ok");
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUserByIds (@RequestBody List<Long> ids, HttpServletRequest request) {
        if(ids == null || ids.isEmpty() || request == null) {
            return new BaseResponse<>(BaseResponseCode.PARAM_ERR, null);
        }
        if(!userService.isAdmin(request)) {
            return new BaseResponse<>(BaseResponseCode.NO_AUTH, null);
        }
        List<User> res = userService.searchUser(ids).stream()
                .map(user -> userService.anonymize(user))
                .collect(Collectors.toList());
        return new BaseResponse<>(BaseResponseCode.SUCCESS, res, "ok");
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteUserById(@PathVariable Long id, HttpServletRequest request) {
        if(id == null || request == null) {
            return new BaseResponse<>(BaseResponseCode.PARAM_ERR, null);
        }
        if(!userService.isAdmin(request)) {
            return new BaseResponse<>(BaseResponseCode.NO_AUTH, null);
        }
        userService.getBaseMapper().deleteById(id);
        return new BaseResponse<>(BaseResponseCode.SUCCESS, true, "ok");
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        if(request == null) {
            return null;
        }
        return new BaseResponse<>(BaseResponseCode.SUCCESS,userService.getCurrentUser(request), "ok");
    }

    @GetMapping("/logout")
    public void userLogout (HttpServletRequest request) {
        if(request == null) {
            return;
        }
        userService.userLogout(request);
    }
}
