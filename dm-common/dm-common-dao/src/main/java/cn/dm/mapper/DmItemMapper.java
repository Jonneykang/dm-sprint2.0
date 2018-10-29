package cn.dm.mapper;

import cn.dm.pojo.DmItem;
import cn.dm.vo.DmFloorItems;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DmItemMapper {

    public DmItem getDmItemById(@Param(value = "id") Long id) throws Exception;

    public List<DmItem> getDmItemListByMap(Map<String, Object> param) throws Exception;

    public Integer getDmItemCountByMap(Map<String, Object> param) throws Exception;

    public Integer insertDmItem(DmItem dmItem) throws Exception;

    public Integer updateDmItem(DmItem dmItem) throws Exception;

    public Integer deleteDmItemById(@Param(value = "id") Long id) throws Exception;

    public Integer batchDeleteDmItem(Map<String, List<String>> params);

    public List<DmFloorItems> queryItemByFloor() throws Exception;

    public List<DmItem> getHotDmItemList(Map<String, Object> param) throws Exception;

    public List<DmItem> getItemByMonth(Map<String, Object> params) throws Exception;

    public List<DmItem> getHotDmItemListForEs(Map<String, Object> param) throws Exception;

}
