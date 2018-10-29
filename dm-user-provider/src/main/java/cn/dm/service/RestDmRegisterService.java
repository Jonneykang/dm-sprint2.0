package cn.dm.service;

import cn.dm.common.MD5;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
public class RestDmRegisterService {

    @RequestMapping(value = "/generateVerificationCode", method = RequestMethod.POST)
    /**
     * 生成注册验证码
     */
    public String generateVerificationCode() throws Exception {
        String code = String.valueOf(MD5.getRandomCode());
        return code;
    }
}
