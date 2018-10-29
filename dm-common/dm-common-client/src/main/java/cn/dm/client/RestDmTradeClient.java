package cn.dm.client;
import cn.dm.pojo.DmTrade;

import cn.dm.fallback.DmTradeClientFallBack;
import java.util.List;
import java.util.Map;

import cn.dm.config.DmConfiguration;
import cn.dm.vo.DmItemMessageVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * Created by shang-pc on 2018/5/15.
 */
@FeignClient(name = "dm-pay-provider", configuration = DmConfiguration.class, fallback = DmTradeClientFallBack.class)
public interface RestDmTradeClient {

@RequestMapping(value = "/insertTrade",method = RequestMethod.POST)
public Integer insertTrade(@RequestBody DmItemMessageVo dmItemMessageVo)throws Exception;
}
