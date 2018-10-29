package cn.dm.client;
import cn.dm.fallback.DmKeywordSortClientFallBack;
import cn.dm.pojo.DmKeywordSort;


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
@FeignClient(name = "dm-base-provider", configuration = DmConfiguration.class, fallback = DmKeywordSortClientFallBack.class)
public interface RestDmKeywordSortClient {

    @RequestMapping(value = "/getDmKeywordSortById",method = RequestMethod.POST)
    public DmKeywordSort getDmKeywordSortById(@RequestParam("id") Long id)throws Exception;

    @RequestMapping(value = "/getDmKeywordSortListByMap",method = RequestMethod.POST)
    public List<DmKeywordSort>	getDmKeywordSortListByMap(@RequestBody Map<String, Object> param)throws Exception;

    @RequestMapping(value = "/getDmKeywordSortCountByMap",method = RequestMethod.POST)
    public Integer getDmKeywordSortCountByMap(@RequestBody Map<String, Object> param)throws Exception;

    @RequestMapping(value = "/qdtxAddDmKeywordSort",method = RequestMethod.POST)
    public Integer qdtxAddDmKeywordSort(@RequestBody DmKeywordSort dmKeywordSort)throws Exception;

    @RequestMapping(value = "/qdtxModifyDmKeywordSort",method = RequestMethod.POST)
    public Integer qdtxModifyDmKeywordSort(@RequestBody DmKeywordSort dmKeywordSort)throws Exception;
}
