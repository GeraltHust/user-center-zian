package com.zian.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 *  用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 2L;

    private String username;

    private String password;

}
