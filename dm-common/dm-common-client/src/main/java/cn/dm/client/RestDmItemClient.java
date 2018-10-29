package cn.dm.client;
import cn.dm.common.Page;
import cn.dm.fallback.DmItemClientFallBack;
import cn.dm.pojo.DmItem;


import java.util.List;
import java.util.Map;

import cn.dm.config.DmConfiguration;
import cn.dm.vo.DmFloorItems;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
/**
* Created by shang-pc on 2018/5/15.
*/
@FeignClient(name = "dm-item-provider", configuration = DmConfiguration.class, fallback = DmItemClientFallBack.class)
public interface RestDmItemClient {

    @RequestMapping(value = "/getDmItemById",method = RequestMethod.POST)
    public DmItem getDmItemById(@RequestParam("id") Long id)throws Exception;

    @RequestMapping(value = "/getDmItemListByMap",method = RequestMethod.POST)
    public List<DmItem>	getDmItemListByMap(@RequestBody Map<String, Object> param)throws Exception;

    @RequestMapping(value = "/getHotDmItemList", method = RequestMethod.POST)
    public List<DmItem> getHotDmItemList(@RequestBody Map<String, Object> param) throws Exception;

    @RequestMapping(value = "/getDmItemCountByMap",method = RequestMethod.POST)
    public Integer getDmItemCountByMap(@RequestBody Map<String, Object> param)throws Exception;

    @RequestMapping(value = "/qdtxAddDmItem",method = RequestMethod.POST)
    public Integer qdtxAddDmItem(@RequestBody DmItem dmItem)throws Exception;

    @RequestMapping(value = "/qdtxModifyDmItem",method = RequestMethod.POST)
    public Integer qdtxModifyDmItem(@RequestBody DmItem dmItem)throws Exception;

    @RequestMapping(value = "/queryItemByFloor",method = RequestMethod.POST)
    public List<DmFloorItems> queryItemByFloor()throws Exception;

    @RequestMapping(value = "/queryItemByMonth",method = RequestMethod.POST)
    public List<DmItem> queryItemByMonth(@RequestParam Map<String, Object> param)throws Exception;

    @RequestMapping(value = "/getDmItemListByMapForEs",method = RequestMethod.POST)
    public List<DmItem> getDmItemListByMapForEs(@RequestParam Map<String, Object> param) throws Exception;

    @RequestMapping(value = "/queryDmItemPage",method = RequestMethod.POST)
    public Page<DmItem> queryDmItemPage(@RequestParam Map<String, Object> param) throws Exception;

}
