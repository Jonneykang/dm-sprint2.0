package cn.dm.client;
import cn.dm.fallback.DmCinemaClientFallBack;
import cn.dm.pojo.DmCinema;


import java.util.List;
import java.util.Map;

import cn.dm.config.DmConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
/**
* Created by shang-pc on 2018/5/15.
*/
@FeignClient(name = "dm-item-provider", configuration = DmConfiguration.class, fallback = DmCinemaClientFallBack.class)
public interface RestDmCinemaClient {

@RequestMapping(value = "/getDmCinemaById",method = RequestMethod.POST)
public DmCinema getDmCinemaById(@RequestParam("id") Long id)throws Exception;

@RequestMapping(value = "/getDmCinemaListByMap",method = RequestMethod.POST)
public List<DmCinema>	getDmCinemaListByMap(@RequestBody Map<String, Object> param)throws Exception;

@RequestMapping(value = "/getDmCinemaCountByMap",method = RequestMethod.POST)
public Integer getDmCinemaCountByMap(@RequestBody Map<String, Object> param)throws Exception;

@RequestMapping(value = "/qdtxAddDmCinema",method = RequestMethod.POST)
public Integer qdtxAddDmCinema(@RequestBody DmCinema dmCinema)throws Exception;

@RequestMapping(value = "/qdtxModifyDmCinema",method = RequestMethod.POST)
public Integer qdtxModifyDmCinema(@RequestBody DmCinema dmCinema)throws Exception;
}
