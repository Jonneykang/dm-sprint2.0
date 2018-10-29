package cn.dm.exception;

import cn.dm.common.IErrorCode;

/**
 * Created by Administrator on 2018-5-24.
 */
public enum UserInfoErrorCode implements IErrorCode {
    /**
     * 通用异常
     **/
    COMMON_NO_LOGIN("0001", "用户未登录"),
    COMMON_Exception("0002", "系统异常"),
    /**
     * 项目异常
     **/
    USER_IMAGE_NO_EXISTS("1022", "用户头像不存在"),
    USER_NO_EXISTS("1023", "用户不存在"),
    ;
    private String errorCode;
    private String errorMessage;

    private UserInfoErrorCode(String errorCode, String errorMessage) {
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
        return errorMessage ;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
