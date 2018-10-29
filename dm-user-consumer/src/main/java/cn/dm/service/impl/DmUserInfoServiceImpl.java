package cn.dm.service.impl;

import cn.dm.client.RestDmImageClient;
import cn.dm.client.RestDmUserClient;
import cn.dm.common.BaseException;
import cn.dm.common.Constants;
import cn.dm.common.EmptyUtils;
import cn.dm.common.RedisUtils;
import cn.dm.exception.UserInfoErrorCode;
import cn.dm.pojo.DmImage;
import cn.dm.pojo.DmUser;
import cn.dm.service.DmUserInfoService;
import cn.dm.vo.DmUserVO;
import cn.dm.vo.QueryUserVo;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-5-24.
 */
@Service
public class DmUserInfoServiceImpl implements DmUserInfoService {
    private static final Logger logger = LoggerFactory.getLogger(DmUserInfoServiceImpl.class);
    @Resource
    private RestDmUserClient restDmUserClient;
    @Resource
    private RestDmImageClient restDmImageClient;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public QueryUserVo queryUserInfoById(Long id) throws Exception {
        //根据用户id查询用户信息
        DmUser dmUser = restDmUserClient.getDmUserById(id);
        //如果用户不存在，则抛出异常
        if (EmptyUtils.isEmpty(dmUser)) {
            logger.info("[queryUserInfoById]" + "获取用户信息失败，用户的id为：" + id);
            throw new BaseException(UserInfoErrorCode.USER_NO_EXISTS);
        }

        //获取用户头像图片地址
        List<DmImage> dmImages = null;
        QueryUserVo queryUserVo = new QueryUserVo();
        //先从redis缓存中获取
        String key = Constants.IMAGE_TOKEN_PREFIX + id +
                "_" + Constants.Image.ImageType.normal +
                "_" + Constants.Image.ImageCategory.user;
        String imgUrl = (String) redisUtils.get(key);
        logger.info("[queryUserInfoById]" + "获取到的用户的头像图片为：" + imgUrl);
        //如果从reids缓存中没有获取到用户头像则从数据库中查询
        if (EmptyUtils.isEmpty(imgUrl)) {
            logger.info("[queryUserInfoById]" + "redis缓存中没有用户图片，用户id为：" + id);
            dmImages = restDmImageClient.queryDmImageList(id, Constants.Image.ImageType.normal, Constants.Image.ImageCategory.user);
            //如果用户有头像
            if (dmImages.size() != 0) {
                imgUrl = dmImages.get(0).getImgUrl();
                queryUserVo.setImgUrl(imgUrl);
                //此时需要将此用户头像实时地放到redis缓存中
                redisUtils.set(key, imgUrl);
            }
        } else {
            queryUserVo.setImgUrl(imgUrl);
        }
        BeanUtils.copyProperties(dmUser, queryUserVo);
        return queryUserVo;
    }

    @Override
    public Integer qdtxModifyDmUser(QueryUserVo queryUserVo) throws Exception {
        DmUser dmUser = new DmUser();
        DmImage dmImage = new DmImage();
        BeanUtils.copyProperties(queryUserVo, dmUser);
        //查询dmImage
        List<DmImage> dmImages = null;
        //根据用户id查询用户信息
        DmUser dmUserFlag = restDmUserClient.getDmUserById(dmUser.getId());
        //如果用户不存在，则抛出异常
        if (EmptyUtils.isEmpty(dmUserFlag)) {
            logger.info("[qdtxModifyDmUser]" + "用户信息不存在：" + queryUserVo.getId());
            throw new BaseException(UserInfoErrorCode.USER_NO_EXISTS);
        }
        //修改用户信息
        int i = restDmUserClient.qdtxModifyDmUser(dmUser);
        //判断用户是否修改了头像
        if (EmptyUtils.isNotEmpty(queryUserVo.getImgUrl()) && !(Constants.FILE_PRE + Constants.DEFAULT_USER).equals(queryUserVo.getImgUrl())) {
            logger.info("[qdtxModifyDmUser]" + "用户已经设置过头像：" + queryUserVo.getImgUrl());
            //先从redis缓存中查询
            String key = Constants.IMAGE_TOKEN_PREFIX + dmUser.getId() +
                    "_" + Constants.Image.ImageType.normal +
                    "_" + Constants.Image.ImageCategory.user;
            dmImages = restDmImageClient.queryDmImageList(dmUser.getId(), Constants.Image.ImageType.normal, Constants.Image.ImageCategory.user);
            if (dmImages.size() == 0 || null == dmImages.get(0).getId()) {//如果该用户没有头像,不仅要更新用户表的头像图片，还要将头像添加到dm_image表
                dmImage.setTargetId(queryUserVo.getId().intValue());
                dmImage.setType(0);
                dmImage.setImgUrl(queryUserVo.getImgUrl());
                dmImage.setCategory(0);
                restDmImageClient.qdtxAddDmImage(dmImage);
            } else {//修改头像信息
                dmImage = dmImages.get(0);
                dmImage.setImgUrl(queryUserVo.getImgUrl());
                restDmImageClient.qdtxModifyDmImage(dmImage);
            }
            //修改redis缓存
            redisUtils.set(key, queryUserVo.getImgUrl());
        }
        return i;
    }


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

    }

    public void save(String token, DmUserVO user) throws Exception {
        String tokenKey = Constants.USER_TOKEN_PREFIX + user.getUserId();
        String tokenValue = null;
        // 检查是否为会话有效期内的重复登录，若是则首先删除之前缓存的用户数据和token，原登录失效
        if ((tokenValue = (String) redisUtils.get(tokenKey)) != null) {
            redisUtils.delete(tokenValue);
        }
        // 缓存用户token
        redisUtils.set(tokenKey, Constants.Redis_Expire.SESSION_TIMEOUT, token);
        // 缓存用户信息
        redisUtils.set(token, Constants.Redis_Expire.SESSION_TIMEOUT, JSON.toJSONString(user));
    }
}
