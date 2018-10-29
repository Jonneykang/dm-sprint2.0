package cn.dm.controller;
import cn.dm.client.RestDmAreaClient;
import cn.dm.common.Dto;
import cn.dm.common.DtoUtil;
import cn.dm.pojo.DmArea;
import cn.dm.service.BaseService;
import cn.dm.vo.DmAreaVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.geom.Area;
import java.util.List;

/****
 *  基础Controller
 */
@Controller
@RequestMapping("/api/p/area")
public class AreaController {

    static Logger logger = Logger.getLogger(AreaController.class);

    @Autowired
    private BaseService baseService;
    /***
     * 查询所有的市区列表
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryCityList")
    @ResponseBody
    public Dto<List<DmAreaVo>> queryCityList()throws Exception{
       List<DmAreaVo> dmAreaVoList=baseService.queryCityList();
       return DtoUtil.returnDataSuccess(dmAreaVoList);
    }
}
