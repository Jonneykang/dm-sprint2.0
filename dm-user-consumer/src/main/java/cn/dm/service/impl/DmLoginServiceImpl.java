package cn.dm.service.impl;

import cn.dm.client.RestDmImageClient;
import cn.dm.client.RestDmUserClient;
import cn.dm.common.*;
import cn.dm.exception.UserErrorCode;
import cn.dm.pojo.DmImage;
import cn.dm.pojo.DmUser;
import cn.dm.service.DmLoginService;
import cn.dm.vo.DmUserVO;
import cn.dm.vo.TokenVO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/1/8.
 */
@Service
public class DmLoginServiceImpl implements DmLoginService {
    private Logger logger = LoggerFactory.getLogger(DmLoginServiceImpl.class);
    @Resource
    private RestDmUserClient restDmUserClient;
    @Resource
    private RestDmImageClient restDmImageClient;
    @Resource
    private RedisUtils redisUtils;


    // private final String SUFFIX = "_frz";

    @Override
    public Object[] login(DmUser user) throws Exception {
        DmUser dmUser = restDmUserClient.checkLoginByPassword(user);
        // 账号或密码错误
        if (dmUser == null)
            return null;
        // 拷贝用户信息到DTO
        DmUserVO dmUserVO = new DmUserVO();
        BeanUtils.copyProperties(dmUser, dmUserVO);
        dmUserVO.setUserId(dmUser.getId());
        // 查询用户头像
        String key = Constants.IMAGE_TOKEN_PREFIX + dmUser.getId() +
                "_" + Constants.Image.ImageType.normal +
                "_" + Constants.Image.ImageCategory.user;
        String userImg = (String) redisUtils.get(key);
        logger.info("[login]" + "用户的头像为：" + userImg);
        List<DmImage> logo = null;
        if (EmptyUtils.isEmpty(userImg)) {
            logger.info("[login]" + "没有从redis缓存中获取到用户头像");
            logo = restDmImageClient.queryDmImageList(dmUser.getId(),
                    Constants.Image.ImageType.normal,
                    Constants.Image.ImageCategory.user);
            // 如果用户指定了头像，拷贝用户头像信息到DTO
            if (EmptyUtils.isNotEmpty(logo)) {
                userImg = logo.get(0).getImgUrl();
                if (!(Constants.DEFAULT_USER).equals(userImg)) {
                    dmUserVO.setImageId(logo.get(0).getId());
                    dmUserVO.setImgUrl(userImg);
                    //将数据库中的用户头像缓存到redis中
                    redisUtils.set(key, userImg);
                }
            }
        }

        // 生成token
        String token = this.generateToken(dmUser);
        // 拷贝用户信息到DTO
        logger.info("[login]" + "成功生成token：" + token);
        TokenVO tokenVO = new TokenVO(token, Constants.Redis_Expire.SESSION_TIMEOUT, new Date().getTime());
        // dmUserVO.setToken(token);
        // dmUserVO.setExtTime(Constants.Redis_Expire.SESSION_TIMEOUT);
        // dmUserVO.setGenTime(new Date());
        // 保存token到redis缓存
        this.save(token, dmUserVO);

        return new Object[]{dmUserVO, tokenVO};
    }

    @Override
    public String generateToken(DmUser user) throws Exception {
        return restDmUserClient.generateToken(user);
    }

    @Override
    public void save(String token, DmUserVO user) throws Exception {
        String tokenKey = Constants.USER_TOKEN_PREFIX + user.getUserId();
        String tokenValue = null;
        // 检查是否为会话有效期内的重复登录，若是则首先删除之前缓存的用户数据和token，原登录失效
        if ((tokenValue = (String) redisUtils.get(tokenKey)) != null) {
            logger.info("[save]" + "用户在会话有效期内重复登录：" + tokenKey);
            redisUtils.delete(tokenValue);
        }
        // 缓存用户token
        logger.info("[save]" + "缓存用户token");
        redisUtils.set(tokenKey, Constants.Redis_Expire.SESSION_TIMEOUT, token);
        // 缓存用户信息
        logger.info("[save]" + "缓存用户信息");
        redisUtils.set(token, Constants.Redis_Expire.SESSION_TIMEOUT, JSON.toJSONString(user));
    }

    @Override
    public String replace(String token) throws Exception {
        // 置换保护期
        Long expireTime = redisUtils.getExpire(token);
        // if (expireTime == null)
        if (expireTime == -1) {
            // 检查是否大于保护时间
            Date tokenGenTime;// token生成时间
            try {
                String[] tokenDetails = token.split("-");
                SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
                tokenGenTime = formater.parse(tokenDetails[3]);
            } catch (ParseException e) {
                throw new BaseException(UserErrorCode.USER_TOKEN_INVALID_FORMAT);
            }
            long passed = Calendar.getInstance().getTimeInMillis()
                    - tokenGenTime.getTime();// token已产生时间
            if (passed < Constants.Redis_Expire.REPLACEMENT_PROTECTION_TIMEOUT * 1000) // 置换保护期内
                throw new BaseException(UserErrorCode.USER_TOKEN_REPLACE_TIME_NOT_REACH);
        } else if (expireTime == -2 || expireTime == 0)
            throw new BaseException(UserErrorCode.COMMON_NO_LOGIN);
        else if (expireTime > Constants.Redis_Expire.REPLACEABLE_TIME_RANGE)
            throw new BaseException(UserErrorCode.USER_TOKEN_REPLACE_TIME_NOT_REACH);
        // 获取token对应的用户信息，可能抛出未登录异常
        String tokenUser = this.loadCurrentUserByTokenAsJson(token);
        // 从json中截取用户ID
        String idKey = "\"userId\":";
        int begin = tokenUser.indexOf(idKey) + idKey.length();
        int end = tokenUser.indexOf(",", begin);
        if (end == -1) end = tokenUser.indexOf("}");
        String userId = tokenUser.substring(begin, end);
        // 生成新token
        DmUser dmUser = JSON.parseObject(tokenUser, DmUser.class);
        dmUser.setId(Long.parseLong(userId));
        String newToken = restDmUserClient.generateToken(dmUser);
        /* 保存新token到Redis缓存 */
        String tokenKey = Constants.USER_TOKEN_PREFIX + userId;
        // 缓存用户token
        redisUtils.set(tokenKey, newToken);
        // 缓存用户信息
        redisUtils.set(newToken, tokenUser);
        if (expireTime > 0) {
            redisUtils.expire(tokenKey, Constants.Redis_Expire.SESSION_TIMEOUT);
            redisUtils.expire(newToken, Constants.Redis_Expire.SESSION_TIMEOUT);
        }
        redisUtils.expire(token, Constants.Redis_Expire.REPLACEMENT_DELAY);
        return newToken;
    }

    @Override
    public String loadCurrentUserByTokenAsJson(String token) throws Exception {
        String tokenUser = null;
        if ((tokenUser = (String) redisUtils.get(token)) == null)
            throw new BaseException(UserErrorCode.COMMON_NO_LOGIN);

        return tokenUser;
    }

    @Override
    public DmUserVO loadCurrentUserByTokenAsDmUserVo(String token) throws Exception {
        String tokenUser = this.loadCurrentUserByTokenAsJson(token);
        return JSON.parseObject(tokenUser, DmUserVO.class);
    }

    /*@Override
    public DmUser loadCurrentUserByTokenAsDmUser(String token) throws Exception {
        DmUserVO dmUserVO = this.loadCurrentUserByTokenAsDmUserVo(token);
        DmUser dmUser = new DmUser();
        BeanUtils.copyProperties(dmUserVO, dmUser);
        dmUser.setId(dmUserVO.getUserId());
        return dmUser;
    }*/

    @Override
    public void delete(String token) throws Exception {
        if (redisUtils.exist(token))
            redisUtils.delete(token);
    }

    @Override
    public boolean validate(String token) throws Exception {
        return redisUtils.validate(token);
    }

    @Override
    public DmUser findByWxUserId(String wxUserId) throws Exception {
        return restDmUserClient.findByWxUserId(wxUserId);
    }

    @Override
    public Long createDmUser(DmUser dmUser) throws Exception {
        return restDmUserClient.createDmUser(dmUser);
    }

    @Override
    public void flushUserInfo(String token, Long userId) throws Exception {
        DmUser dmUser = restDmUserClient.getDmUserById(userId);
        DmUserVO dmUserVO = new DmUserVO();
        BeanUtils.copyProperties(dmUser, dmUserVO);
        dmUserVO.setUserId(dmUser.getId());
        // 查询用户头像
        List<DmImage> logo = restDmImageClient.queryDmImageList(dmUser.getId(), 0, 0); // 等常量，等常量，等常量，等常量，等常量，等常量，等常量，等常量，等常量，
        // 如果用户指定了头像，拷贝用户头像信息到DTO
        if (EmptyUtils.isNotEmpty(logo)) {
            dmUserVO.setImageId(logo.get(0).getId());
            dmUserVO.setImgUrl(logo.get(0).getImgUrl());
        }
        this.save(token, dmUserVO);
    }

    /**
     * @param userInfoMap 用户信息入库包括openId
     * @param openId
     * @return
     * @throws Exception
     */
    @Override
    public String getWxUserInfo(Map<String, Object> userInfoMap, String openId) throws Exception {
        //用户昵称
        String nickName = userInfoMap.get("nickname").toString();
        //性别
        String sex = userInfoMap.get("sex").toString();

        //验证本地库是否存在该用户
        DmUser user = this.findByWxUserId(openId);
        DmUserVO dmUserVO = new DmUserVO();
        List<DmImage> dmImages = new ArrayList<DmImage>();
        Long userId = null;
        if (user == null) {//如果不存在则添加用户
            user = new DmUser();
            user.setWxUserId(openId);
            user.setSex(Integer.parseInt(sex));
            user.setNickName(nickName);
            userId = this.createDmUser(user);
            user.setId(userId);
        }
        BeanUtils.copyProperties(user, dmUserVO);
        //先从redis缓存中获取
        String key = Constants.IMAGE_TOKEN_PREFIX + user.getId() +
                "_" + Constants.Image.ImageType.normal +
                "_" + Constants.Image.ImageCategory.user;
        String imgUrl = (String) redisUtils.get(key);

        logger.info("[queryUserInfoById]" + "获取到的用户的头像图片为：" + imgUrl);
        //如果从reids缓存中没有获取到用户头像则从数据库中查询
        if (EmptyUtils.isEmpty(imgUrl)) {
            logger.info("[queryUserInfoById]" + "redis缓存中没有用户图片，用户id为：" + user.getId());
            dmImages = restDmImageClient.queryDmImageList(user.getId(),
                    Constants.Image.ImageType.normal,
                    Constants.Image.ImageCategory.user);
            //如果用户有头像
            if (dmImages.size() != 0) {
                imgUrl = dmImages.get(0).getImgUrl();
                dmUserVO.setImgUrl(imgUrl);
                //此时需要将此用户头像实时地放到redis缓存中
                redisUtils.set(key, imgUrl);
            }
        } else {
            dmUserVO.setImgUrl(imgUrl);
        }

        String token = this.generateToken(user);
        dmUserVO.setUserId(user.getId());
        this.save(token, dmUserVO);
        return token;
    }
}
