package cn.dm.service.impl;

import cn.dm.client.RestDmCinemaSeatClient;
import cn.dm.client.RestDmItemClient;
import cn.dm.client.RestDmSchedulerClient;
import cn.dm.client.RestDmSchedulerSeatClient;
import cn.dm.pojo.DmCinemaSeat;
import cn.dm.pojo.DmItem;
import cn.dm.pojo.DmScheduler;
import cn.dm.pojo.DmSchedulerSeat;
import cn.dm.service.TempSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TempSchedulerServiceImpl implements TempSchedulerService {

    @Autowired
    private RestDmSchedulerClient restDmSchedulerClient;

    @Autowired
    private RestDmSchedulerSeatClient restDmSchedulerSeatClient;

    @Autowired
    private RestDmCinemaSeatClient restDmCinemaSeatClient;

    @Autowired
    private RestDmItemClient restDmItemClient;

    @Override
    public void generateScheduleSeatData() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        //先获取所有的排期
        List<DmScheduler> dmSchedulerList = restDmSchedulerClient.getDmSchedulerListByMap(param);
        //为每个排期初始化座位信息
        for (int i = 0; i < dmSchedulerList.size(); i++) {
            DmScheduler dmScheduler = dmSchedulerList.get(i);
            //获取排期对应的剧院信息
            DmItem dmItem = restDmItemClient.getDmItemById(dmScheduler.getItemId());
            //根据剧院信息拿到具体对应的座位信息列表
            Map<String, Object> paramCinemaSeat = new HashMap<String, Object>();
            paramCinemaSeat.put("cinemaId", dmItem.getCinemaId());
            List<DmCinemaSeat> dmCinemaSeatList = restDmCinemaSeatClient.getDmCinemaSeatListByMap(paramCinemaSeat);
            //把这些座位信息插入到排期座位表中
            for (int j = 0; j < dmCinemaSeatList.size(); j++) {
                DmCinemaSeat dmCinemaSeat = dmCinemaSeatList.get(j);
                DmSchedulerSeat dmSchedulerSeat = new DmSchedulerSeat();
                dmSchedulerSeat.setX(dmCinemaSeat.getX());
                dmSchedulerSeat.setY(dmCinemaSeat.getY());
                dmSchedulerSeat.setAreaLevel(dmCinemaSeat.getAreaLevel());
                dmSchedulerSeat.setScheduleId(dmScheduler.getId());
//                dmSchedulerSeat.setUserId(); 初始不插入用户信息
                dmSchedulerSeat.setStatus(0);
                dmSchedulerSeat.setSort(j + 1);
                dmSchedulerSeat.setCreatedTime(new Date());
                restDmSchedulerSeatClient.qdtxAddDmSchedulerSeat(dmSchedulerSeat);
            }
        }
    }

    @Override
    public void addScheduler(DmScheduler dmScheduler) throws Exception {
        restDmSchedulerClient.qdtxAddDmScheduler(dmScheduler);
    }
}
