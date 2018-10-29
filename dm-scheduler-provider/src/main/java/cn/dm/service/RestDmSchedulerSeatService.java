package cn.dm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dm.mapper.DmSchedulerSeatMapper;
import cn.dm.pojo.DmSchedulerSeat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zezhong.shang on 18-5-15.
 */
@RestController
public class RestDmSchedulerSeatService {

    @Autowired
    private DmSchedulerSeatMapper dmSchedulerSeatMapper;

    @RequestMapping(value = "/getDmSchedulerSeatById", method = RequestMethod.POST)
    public DmSchedulerSeat getDmSchedulerSeatById(@RequestParam("id") Long id) throws Exception {
        return dmSchedulerSeatMapper.getDmSchedulerSeatById(id);
    }

    @RequestMapping(value = "/getDmSchedulerSeatListByMap", method = RequestMethod.POST)
    public List<DmSchedulerSeat> getDmSchedulerSeatListByMap(@RequestBody Map<String, Object> param) throws Exception {
        return dmSchedulerSeatMapper.getDmSchedulerSeatListByMap(param);
    }

    @RequestMapping(value = "/getDmSchedulerSeatCountByMap", method = RequestMethod.POST)
    public Integer getDmSchedulerSeatCountByMap(@RequestBody Map<String, Object> param) throws Exception {
        return dmSchedulerSeatMapper.getDmSchedulerSeatCountByMap(param);
    }

    @RequestMapping(value = "/qdtxAddDmSchedulerSeat", method = RequestMethod.POST)
    public Integer qdtxAddDmSchedulerSeat(@RequestBody DmSchedulerSeat dmSchedulerSeat) throws Exception {
        dmSchedulerSeat.setCreatedTime(new Date());
        return dmSchedulerSeatMapper.insertDmSchedulerSeat(dmSchedulerSeat);
    }

    @RequestMapping(value = "/qdtxModifyDmSchedulerSeat", method = RequestMethod.POST)
    public Integer qdtxModifyDmSchedulerSeat(@RequestBody DmSchedulerSeat dmSchedulerSeat) throws Exception {
        dmSchedulerSeat.setUpdatedTime(new Date());
        return dmSchedulerSeatMapper.updateDmSchedulerSeat(dmSchedulerSeat);
    }

    @RequestMapping(value = "/getDmSchedulerSeatByOrder", method = RequestMethod.POST)
    public DmSchedulerSeat getDmSchedulerSeatByOrder(@RequestParam("scheduleId") Long scheduleId,
                                                     @RequestParam("x") Integer x,
                                                     @RequestParam("y") Integer y) throws Exception {
        DmSchedulerSeat dmSchedulerSeat = dmSchedulerSeatMapper.getDmSchedulerSeatByOrder(scheduleId, x, y);
        return dmSchedulerSeat;
    }
}
