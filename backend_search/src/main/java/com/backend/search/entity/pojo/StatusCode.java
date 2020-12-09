package com.backend.search.entity.pojo;

public class StatusCode {
    /**
     * 操作成功
     */
    public static final int OK = 200;
    /**
     * 失败
     */
    public static final int ERROR = 201;
    /**
     * 用户名或密码错误
     */
    public static final int LOGIN_ERROR = 202;
    /**
     * token过期
     */
    public static final int TOKEN_EXPIREE = 203;
    /**
     * 权限不足
     */
    public static final int ACCESS_ERROR = 403;
    /**
     * 远程调用失败
     */
    public static final int REMOTE_ERROR = 204;
    /**
     * 重复操作
     */
    public static final int REP_ERROR = 205;
    /**
     * 业务层错误
     */
    public static final int SERVICE_ERROR = 500;
    /**
     * 资源不存在
     */
    public static final int NOTFOUND = 404;
    /**
     * 信息错误
     */
    public static final int INFORMATION_ERROR = 206;
    /**
     * 验证码错误
     */
    public static final int CODE_ERROR = 207;
}