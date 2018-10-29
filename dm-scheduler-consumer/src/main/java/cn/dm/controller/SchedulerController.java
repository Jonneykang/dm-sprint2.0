package cn.dm.controller;
import cn.dm.client.RestDmCinemaSeatClient;
import cn.dm.client.RestDmSchedulerClient;
import cn.dm.client.RestDmSchedulerSeatClient;
import cn.dm.client.RestDmSchedulerSeatPriceClient;
import cn.dm.common.Dto;
import cn.dm.common.DtoUtil;
import cn.dm.common.EmptyUtils;
import cn.dm.param.QuerySeatParam;
import cn.dm.pojo.DmScheduler;
import cn.dm.pojo.DmSchedulerSeat;
import cn.dm.pojo.DmSchedulerSeatPrice;
import cn.dm.vo.ScheduleSeatVo;
import cn.dm.vo.SeatInfoVo;
import cn.dm.vo.SeatPriceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 排期
 */
@RestController
@RequestMapping("/api/p/")
public class SchedulerController {

    @Autowired
    private RestDmSchedulerSeatClient restDmSchedulerSeatClient;

    @Autowired
    private RestDmSchedulerClient restDmSchedulerClient;

    @Autowired
    private RestDmCinemaSeatClient restDmCinemaSeatClient;

    @Autowired
    private RestDmSchedulerSeatPriceClient restDmSchedulerSeatPriceClient;

    /***
     * 查询剧场原始座位
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryOriginalCinemaSeatArray")
    public Dto<List<Map<String,Object>>> queryCinemaSeatArray(@RequestBody Map<String,Object> params) throws Exception {
        Map<String,Object> result=new HashMap<String,Object>();
        List<String> seatArray=restDmCinemaSeatClient.queryCinemaSeatArray(params);
        result.put("cinemaId",params.get("cinemaId"));
        result.put("seatArray",seatArray);
        //查询座位以数组的形式返回
        return DtoUtil.returnDataSuccess(result);
    }
    /***
     * 根据排期查询做为价格
     * @return
     */
    @RequestMapping("/queryCinemaSeatArrayByScheduleId")
    public Dto<ScheduleSeatVo> queryCinemaSeatArrayByScheduleId(@RequestBody Map<String,Object> params) throws Exception {
        ScheduleSeatVo scheduleSeatVo=new ScheduleSeatVo();
        String scheduleIdStr=params.get("scheduleId").toString();
        Long scheduleId=Long.parseLong(scheduleIdStr);
        DmScheduler dmScheduler=restDmSchedulerClient.getDmSchedulerById(scheduleId);
        Long cinemaId=dmScheduler.getCinemaId();
        List<SeatPriceVo>  seatPriceVos=new ArrayList<SeatPriceVo>();
        List<SeatInfoVo>  seatInfoVos=new ArrayList<SeatInfoVo>();
        scheduleSeatVo.setCinemaId(cinemaId);
        scheduleSeatVo.setScheduleId(scheduleId);
        Map<String,Object> seatPriceParam=new HashMap<String,Object>();
        seatPriceParam.put("scheduleId",scheduleId);
        List<DmSchedulerSeatPrice> dmSchedulerSeatPrices=restDmSchedulerSeatPriceClient.getDmSchedulerSeatPriceListByMap(seatPriceParam);
        List<DmSchedulerSeat> dmSchedulerSeatList=restDmSchedulerSeatClient.getDmSchedulerSeatListByMap(seatPriceParam);
        if(EmptyUtils.isEmpty(dmSchedulerSeatPrices) || EmptyUtils.isEmpty(dmSchedulerSeatPrices)){
            return null;
        }
        //添加座位价格
        for (DmSchedulerSeatPrice dmSchedulerSeatPrice:dmSchedulerSeatPrices){
            SeatPriceVo seatPriceVo=new SeatPriceVo();
            seatPriceVo.setAreaLevel(dmSchedulerSeatPrice.getAreaLevel());
            seatPriceVo.setPrice(dmSchedulerSeatPrice.getPrice());
            seatPriceVos.add(seatPriceVo);
        }
        //添加座位信息
        for (DmSchedulerSeat dmSchedulerSeat:dmSchedulerSeatList){
            SeatInfoVo seatInfoVo=new SeatInfoVo();
            seatInfoVo.setAreaLevel(dmSchedulerSeat.getAreaLevel());
            seatInfoVo.setCinemaId(dmScheduler.getCinemaId());
            seatInfoVo.setId(dmSchedulerSeat.getId());
            seatInfoVo.setSort(dmSchedulerSeat.getSort());
            seatInfoVo.setStatus(dmSchedulerSeat.getStatus());
            seatInfoVo.setX(dmSchedulerSeat.getX());
            seatInfoVo.setY(dmSchedulerSeat.getY());
            seatInfoVos.add(seatInfoVo);
        }
        scheduleSeatVo.setSeatInfoList(seatInfoVos);
        scheduleSeatVo.setSeatPriceList(seatPriceVos);
        return DtoUtil.returnDataSuccess(scheduleSeatVo);
    }
}
