package cn.dm.client;

import cn.dm.config.DmConfiguration;
import cn.dm.fallback.DmRegisterClientFallBack;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 */
@FeignClient(name = "dm-user-provider", configuration = DmConfiguration.class, fallback = DmRegisterClientFallBack.class)
public interface RestDmRegisterClient {
    /**
     * 生成注册验证码
     * @return 注册验证码
     * @throws Exception
     */
    @RequestMapping(value = "/generateVerificationCode", method = RequestMethod.POST)
    public String generateVerificationCode() throws Exception;
}

