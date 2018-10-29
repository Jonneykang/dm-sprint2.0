package cn.dm.exception;

import cn.dm.common.IErrorCode;

/**
 * Created by Administrator on 2018-5-21.
 */
public enum  LinkUserErrorCode implements IErrorCode{
    /**通用异常**/
    COMMON_NO_LOGIN("0001","用户未登录"),
    COMMON_Exception("0002","系统异常"),
    /**用户项目异常**/
    LINKUSER_NO_DATA("1021", "没有查询到数据"),
    LINKUSER_NO_EXIST("1020", "常用购票人已存在"),
    LINKUSER_DEL_FAIL("1022", "删除失败"),
    LINKUSER_ADD_FAIL("1024", "添加失败"),
    ;
    private String errorCode;
    private String errorMessage;

    private LinkUserErrorCode(String errorCode, String errorMessage) {
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
