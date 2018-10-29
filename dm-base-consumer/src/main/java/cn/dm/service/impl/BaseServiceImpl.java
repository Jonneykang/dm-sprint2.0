package cn.dm.service.impl;

import cn.dm.client.RestDmAreaClient;
import cn.dm.pojo.DmArea;
import cn.dm.service.BaseService;
import cn.dm.vo.DmAreaVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BaseServiceImpl implements BaseService{

    @Autowired
    private RestDmAreaClient restDmAreaClient;
    /***
     * 查询所有的城市列表
     * @return
     */
    @Override
    public List<DmAreaVo> queryCityList() throws Exception {
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("level",1);
        List<DmAreaVo> dmAreaVoList=new ArrayList<DmAreaVo>();
        List<DmArea> dmAreaList=restDmAreaClient.getDmAreaListByMap(param);
        for (DmArea dmArea:dmAreaList){
            DmAreaVo dmAreaVo=new DmAreaVo();
            BeanUtils.copyProperties(dmArea,dmAreaVo);
            dmAreaVo.setId(dmArea.getId());
            dmAreaVoList.add(dmAreaVo);
        }
        return dmAreaVoList;
    }
}
