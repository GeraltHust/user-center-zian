package com.zian.constant;

public enum BaseResponseCode {
    SUCCESS(200, "OK"),

    PARAM_ERR(400, "参数错误"),
    NO_AUTH(403, "禁止访问"),

    SYSTEM_ERR(500, "服务器内部异常"),
    CONTENT_EMPTY(501, "数据为空")

    ;
    private int code;
    private String desc;
    BaseResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
