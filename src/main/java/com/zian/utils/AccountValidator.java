package com.zian.utils;
import java.util.regex.Pattern;

/**
 *  账户校验工具
 *  @author Wang Zijian && QWEN
 */
public class AccountValidator {

    // 定义一个正则表达式，只允许字母、数字和下划线
    private static final String ACCOUNT_PATTERN = "^[a-zA-Z0-9_]+$";

    // 编译正则表达式为Pattern对象
    private static final Pattern pattern = Pattern.compile(ACCOUNT_PATTERN);

    /**
     * 校验账户名是否合法
     * @param account 账户名
     * @return 如果账户名合法返回true，否则返回false
     */
    public static boolean isValidAccount(String account) {
        if (account == null || account.isEmpty()) {
            return false;  // 账户名不能为空
        }
        return pattern.matcher(account).matches();
    }
}