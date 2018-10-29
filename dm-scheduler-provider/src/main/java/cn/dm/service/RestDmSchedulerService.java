package cn.dm.service;

import cn.dm.common.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dm.mapper.DmSchedulerMapper;
import cn.dm.pojo.DmScheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zezhong.shang on 18-5-15.
 */
@RestController
public class RestDmSchedulerService {

    @Autowired
    private DmSchedulerMapper dmSchedulerMapper;

    @RequestMapping(value = "/getDmSchedulerById", method = RequestMethod.POST)
    public DmScheduler getDmSchedulerById(@RequestParam("id") Long id) throws Exception {
        return dmSchedulerMapper.getDmSchedulerById(id);
    }

    @RequestMapping(value = "/getDmSchedulerListByMap", method = RequestMethod.POST)
    public List<DmScheduler> getDmSchedulerListByMap(@RequestBody Map<String, Object> param) throws Exception {
        return dmSchedulerMapper.getDmSchedulerListByMap(param);
    }

    @RequestMapping(value = "/getDmSchedulerByItemId", method = RequestMethod.POST)
    public DmScheduler getDmSchedulerByItemId(@RequestParam("itemId") Long itemId) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("itemId", itemId);
        return EmptyUtils.isEmpty(dmSchedulerMapper.getDmSchedulerListByMap(param)) ? null : dmSchedulerMapper.getDmSchedulerListByMap(param).get(0);
    }

    @RequestMapping(value = "/getDmSchedulerCountByMap", method = RequestMethod.POST)
    public Integer getDmSchedulerCountByMap(@RequestBody Map<String, Object> param) throws Exception {
        return dmSchedulerMapper.getDmSchedulerCountByMap(param);
    }

    @RequestMapping(value = "/qdtxAddDmScheduler", method = RequestMethod.POST)
    public Integer qdtxAddDmScheduler(@RequestBody DmScheduler dmScheduler) throws Exception {
        dmScheduler.setCreatedTime(new Date());
        return dmSchedulerMapper.insertDmScheduler(dmScheduler);
    }

    @RequestMapping(value = "/qdtxModifyDmScheduler", method = RequestMethod.POST)
    public Integer qdtxModifyDmScheduler(@RequestBody DmScheduler dmScheduler) throws Exception {
        dmScheduler.setUpdatedTime(new Date());
        return dmSchedulerMapper.updateDmScheduler(dmScheduler);
    }
}
