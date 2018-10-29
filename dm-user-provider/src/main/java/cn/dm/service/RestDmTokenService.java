package cn.dm.service;

import cn.dm.common.EmptyUtils;
import cn.dm.common.MD5;
import cn.dm.pojo.DmUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Token管理接口相关业务服务实现
 *
 * @author hduser
 */
@RestController
public class RestDmTokenService {

    private Logger logger = LoggerFactory.getLogger(RestDmTokenService.class);
    private String tokenPrefix = "token:";//统一加入 token前缀标识

    /***
     * @param user 用户信息
     * @return Token格式<br/>
     * 	PC：“前缀PC-USERCODE-USERID-CREATIONDATE-RONDEM[6位]”
     *  <br/>
     *  Android：“前缀ANDROID-USERCODE-USERID-CREATIONDATE-RONDEM[6位]”
     */
    @RequestMapping(value = "/generateToken", method = RequestMethod.POST)
    public String generateToken(@RequestBody DmUser user) {
        StringBuilder sb = new StringBuilder();
        sb.append(tokenPrefix);//统一前缀
        // 设备，暂时固定为PC
        sb.append("PC-");
        // usercode
        sb.append(MD5.getMd5((EmptyUtils.isEmpty(user.getPhone()) ? user.getWxUserId().toString() : user.getPhone()), 32) + "-");
        // userid
        sb.append(user.getId() + "-");
        // creation date
        sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-");
        // 6位random，暂无特定含义或用途
        sb.append(UUID.randomUUID().toString().substring(0, 6));
        return sb.toString();
    }

}
