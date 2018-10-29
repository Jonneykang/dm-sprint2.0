package cn.dm.mapper;

import cn.dm.pojo.DmItem;
import cn.dm.vo.DmFloorItems;
import cn.dm.vo.ItemSearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DmItemSearchMapper {
    /***
     * 根据最后更新时间查询ItemSearchVo
     * @param lastUpdatedTime
     * @return
     * @throws Exception
     */
    public List<ItemSearchVo> getDmItemList(String lastUpdatedTime) throws Exception;

}
