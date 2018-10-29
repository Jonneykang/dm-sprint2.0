package cn.dm.service;


import cn.dm.pojo.DmUser;
import cn.dm.vo.DmUserVO;

import java.util.Map;

/**
 * Created by Administrator on 2018/1/18.
 */
public interface DmLoginService {
    /**
     * 账号密码登录
     * @param user 封装 用户注册邮箱 用户密码
     * @return 登录结果，登录成功时返回包含DmUserVO和TokenVO的数组，登录失败返回null
     * @throws Exception
     */
    public Object[] login(DmUser user) throws Exception;

    /**
     * 生成token
     * @param user 用户信息

     * 		PC：“前缀PC-USERCODE-USERID-CREATIONDATE-RONDEM[6位]”
     *  	<BR/>
     *  	Android：“前缀ANDROID-USERCODE-USERID-CREATIONDATE-RONDEM[6位]”
     */
    public String generateToken(DmUser user) throws Exception;

    /**
     *保存token和用户信息到redis中
     * @param token
     * @param user
     */
    public void save(String token, DmUserVO user) throws Exception;

    /**
     * 置换token到redis中
     * @param token
     * @return 新token
     * @throws Exception 未登录异常
     */
    public String replace(String token) throws Exception;

    /**
     * 删除token
     * @param token
     */
    public void delete(String token) throws Exception;

    /**
     * 验证token的正确性
     * @param token
     * @return
     */
    public boolean validate(String token) throws Exception;

    /**
     * 根据token以JSON字符串形式加载当前对象
     * @param token
     * @return  JSON字符串形式的当前用户信息
     * @throws Exception token无效抛出未登录异常
     */
    public String loadCurrentUserByTokenAsJson(String token) throws Exception;

    /**
     * 根据token以DmUserVO形式加载当前对象
     * @param token
     * @return  DmUserVO形式的当前用户信息
     * @throws Exception token无效抛出未登录异常
     */
    public DmUserVO loadCurrentUserByTokenAsDmUserVo(String token) throws Exception;

    /*
     * 根据token以DmUser形式加载当前对象
     * @param token
     * @return  DmUser形式的当前用户信息
     * @throws Exception token无效抛出未登录异常
     */
    // public DmUser loadCurrentUserByTokenAsDmUser(String token) throws Exception;

    /**
     * 根据wxUserId查询用户
     * @param wxUserId
     * @return
     * @throws Exception
     */
    public DmUser findByWxUserId(String wxUserId) throws Exception;

    /**
     * 创建微信登录用户信息
     * @param dmUser
     * @return
     * @throws Exception
     */
    public Long createDmUser(DmUser dmUser) throws Exception;
    /***
     * 刷新用户信息
     * @param token
     * @param userId
     * @throws Exception
     */
    public void flushUserInfo(String token, Long userId) throws Exception;

    public String getWxUserInfo(Map<String, Object> userInfoMap, String openId) throws Exception;
}
