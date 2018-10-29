package cn.dm.client;
import cn.dm.fallback.DmAreaClientFallBack;
import cn.dm.pojo.DmArea;
import java.util.List;
import java.util.Map;

import cn.dm.config.DmConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
/**
* Created by shang-pc on 2018/5/15.
*/
@FeignClient(name = "dm-base-provider", configuration = DmConfiguration.class, fallback = DmAreaClientFallBack.class)
public interface RestDmAreaClient {

    @RequestMapping(value = "/getDmAreaById",method = RequestMethod.POST)
    public DmArea getDmAreaById(@RequestParam("id") Long id)throws Exception;

    @RequestMapping(value = "/getDmAreaListByMap",method = RequestMethod.POST)
    public List<DmArea>	getDmAreaListByMap(@RequestBody Map<String, Object> param)throws Exception;

    @RequestMapping(value = "/getDmAreaCountByMap",method = RequestMethod.POST)
    public Integer getDmAreaCountByMap(@RequestBody Map<String, Object> param)throws Exception;

    @RequestMapping(value = "/qdtxAddDmArea",method = RequestMethod.POST)
    public Integer qdtxAddDmArea(@RequestBody DmArea dmArea)throws Exception;

    @RequestMapping(value = "/qdtxModifyDmArea",method = RequestMethod.POST)
    public Integer qdtxModifyDmArea(@RequestBody DmArea dmArea)throws Exception;
}
