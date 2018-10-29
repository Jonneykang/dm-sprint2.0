package cn.dm.exception;

import cn.dm.common.IErrorCode;

public enum BaseErrorCode implements IErrorCode {
    /**
     * 节目项目异常
     **/
    Illegal_Upload("6001", "非法的上传请求"),
    File_isEmpty("6002", "非法的上传请求"),
    File_Name_isEmpty("6003", "文件名不能为空"),
    ;
    private String errorCode;
    private String errorMessage;

    private BaseErrorCode(String errorCode, String errorMessage) {
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
