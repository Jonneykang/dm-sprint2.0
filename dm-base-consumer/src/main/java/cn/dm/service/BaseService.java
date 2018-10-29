package cn.dm.service;

import cn.dm.pojo.DmArea;
import cn.dm.vo.DmAreaVo;

import java.util.List;

public interface BaseService {
    /***
     * 查询所有的城市
     * @return
     */
    public List<DmAreaVo> queryCityList() throws Exception;

}
