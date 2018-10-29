package cn.dm.fallback;

import cn.dm.common.Page;
import cn.dm.pojo.DmItem;

import cn.dm.client.RestDmItemClient;
import cn.dm.vo.DmFloorItems;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Component
public class DmItemClientFallBack implements RestDmItemClient {


    @Override
    public DmItem getDmItemById(Long id)throws Exception{
        return null;
    }

    @Override
    public List<DmItem>	getDmItemListByMap(Map<String,Object> param)throws Exception{
        return null;
    }

    @Override
    public List<DmItem> getHotDmItemList(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Integer getDmItemCountByMap(Map<String,Object> param)throws Exception{
        return null;
    }

    @Override
    public Integer qdtxAddDmItem(DmItem dmItem)throws Exception{
        return null;
    }

    @Override
    public Integer qdtxModifyDmItem(DmItem dmItem)throws Exception{
        return null;
    }

    @Override
    public List<DmFloorItems> queryItemByFloor() throws Exception {
        return null;
    }

    @Override
    public List<DmItem> queryItemByMonth(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public List<DmItem> getDmItemListByMapForEs(@RequestParam Map<String, Object> param) throws Exception{
        return null;
    }

    @Override
    public Page<DmItem> queryDmItemPage(@RequestParam  Map<String, Object> param) throws Exception {
        return null;
    }

}
