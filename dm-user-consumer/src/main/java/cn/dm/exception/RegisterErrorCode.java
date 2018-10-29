package cn.dm.exception;

import cn.dm.common.IErrorCode;

public enum RegisterErrorCode implements IErrorCode {
    /**
     * 通用异常
     **/
    COMMON_NO_LOGIN("0001", "用户未登录"),
    COMMON_Exception("0002", "系统异常"),
    /**用户项目注册功能异常**/
    USER_REGISTER_FRZ("1002", "冻结时间未到"),
    USER_REGISTER_EMAIL_EXIST("1003", "邮箱已注册"),
    USER_REGISTER_VFCODE_SEND_FAILED("1007", "验证码发送失败")
    ;

    private String errorCode;
    private String errorMessage;

    private RegisterErrorCode(String errorCode, String errorMessage) {
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
