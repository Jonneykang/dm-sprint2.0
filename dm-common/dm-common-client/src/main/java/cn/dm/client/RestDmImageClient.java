package cn.dm.client;
import cn.dm.fallback.DmImageClientFallBack;
import cn.dm.pojo.DmImage;

import java.util.HashMap;
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
@FeignClient(name = "dm-base-provider", configuration = DmConfiguration.class, fallback = DmImageClientFallBack.class)
public interface RestDmImageClient {

    @RequestMapping(value = "/getDmImageById",method = RequestMethod.POST)
    public DmImage getDmImageById(@RequestParam("id") Long id)throws Exception;

    @RequestMapping(value = "/getDmImageListByMap",method = RequestMethod.POST)
    public List<DmImage>	getDmImageListByMap(@RequestBody Map<String, Object> param)throws Exception;

    @RequestMapping(value = "/getDmImageCountByMap",method = RequestMethod.POST)
    public Integer getDmImageCountByMap(@RequestBody Map<String, Object> param)throws Exception;

    @RequestMapping(value = "/qdtxAddDmImage",method = RequestMethod.POST)
    public Integer qdtxAddDmImage(@RequestBody DmImage dmImage)throws Exception;

    @RequestMapping(value = "/qdtxModifyDmImage",method = RequestMethod.POST)
    public Integer qdtxModifyDmImage(@RequestBody DmImage dmImage)throws Exception;

    @RequestMapping(value = "/queryDmImageList",method = RequestMethod.POST)
    public List<DmImage> queryDmImageList(@RequestParam("targetId") Long targetId,
                                          @RequestParam(value = "type", required = false) Integer type,
                                          @RequestParam(value = "category", required = false) Integer category);
}
