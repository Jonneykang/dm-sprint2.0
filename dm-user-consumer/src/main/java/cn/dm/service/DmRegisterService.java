package cn.dm.service;

import cn.dm.pojo.DmUser;

/**
 *
 */
public interface DmRegisterService {
    /**
     * 通过邮件发送注册验证码
     * @param mailTo 需要发送验证码的email地址
     * @throws Exception
     */
    void sendVerificationCodeByEmail(String mailTo) throws Exception;

    /**
     * 检查邮箱是否已被注册
     * @param email
     * @return 注册状态，true表示已被注册，false表示未被注册
     * @throws Exception
     */
    boolean checkEmailRegistered(String email) throws Exception;

    /**
     * 使用邮箱注册用户
     * @param dmUser 用户信息
     * @param vcode 用户输入的注册验证码
     * @throws Exception
     */
    void registerUserByEmail(DmUser dmUser, String vcode) throws Exception;
}
