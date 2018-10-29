package cn.dm.exception;

import cn.dm.common.IErrorCode;

public enum UserErrorCode implements IErrorCode {
    /**
     * 通用异常
     **/
    COMMON_NO_LOGIN("0001", "用户未登录"),
    COMMON_Exception("0002", "系统异常"),
    /**
     * 项目异常
     **/
    USER_EMAIL_INVALID_FORMAT("1001", "邮箱格式错误"),
    USER_VFCODE_INVALID("1004", "验证码错误"),
    USER_LOGIN_EMAIL_NOT_EXIST("1005", "邮箱未注册"),
    USER_LOGIN_FAILED("1006", "账号或密码错误"),
    USER_LOGIN_VFCODE_SEND_FAILED("1008", "验证码发送失败"),
    USER_TOKEN_INVALID_FORMAT("1009", "无效的Token格式"),
    USER_TOKEN_REPLACE_TIME_NOT_REACH("1010", "Token置换周期未到");


    private String errorCode;
    private String errorMessage;

    private UserErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
