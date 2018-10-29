package cn.dm.service;

import cn.dm.common.Constants;
import cn.dm.common.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.dm.mapper.DmOrderMapper;
import cn.dm.pojo.DmOrder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zezhong.shang on 18-5-15.
 */
@RestController
public class RestDmOrderService {

    @Autowired
    private DmOrderMapper dmOrderMapper;

    @RequestMapping(value = "/getDmOrderById", method = RequestMethod.POST)
    public DmOrder getDmOrderById(@RequestParam("id") Long id) throws Exception {
        return dmOrderMapper.getDmOrderById(id);
    }

    @RequestMapping(value = "/deleteDmOrderById", method = RequestMethod.POST)
    public Integer deleteDmOrderById(@RequestParam("id") Long id) throws Exception {
        return dmOrderMapper.deleteDmOrderById(id);
    }

    @RequestMapping(value = "/getDmOrderListByMap", method = RequestMethod.POST)
    public List<DmOrder> getDmOrderListByMap(@RequestBody Map<String, Object> param) throws Exception {
        return dmOrderMapper.getDmOrderListByMap(param);
    }

    @RequestMapping(value = "/getDmOrderCountByMap", method = RequestMethod.POST)
    public Integer getDmOrderCountByMap(@RequestBody Map<String, Object> param) throws Exception {
        return dmOrderMapper.getDmOrderCountByMap(param);
    }

    @RequestMapping(value = "/qdtxAddDmOrder", method = RequestMethod.POST)
    public Long qdtxAddDmOrder(@RequestBody DmOrder dmOrder) throws Exception {
        dmOrder.setCreatedTime(new Date());
        this.dmOrderMapper.insertDmOrder(dmOrder);
        return dmOrder.getId();
    }

    @RequestMapping(value = "/qdtxModifyDmOrder", method = RequestMethod.POST)
    public Integer qdtxModifyDmOrder(@RequestBody DmOrder dmOrder) throws Exception {
        dmOrder.setUpdatedTime(new Date());
        return dmOrderMapper.updateDmOrder(dmOrder);
    }

    @RequestMapping(value = "/getDmOrderListByOrderNoOrDate", method = RequestMethod.POST)
    public List<DmOrder> getDmOrderListByOrderNoOrDate(@RequestBody Map<String, Object> param) throws Exception {
        return dmOrderMapper.getDmOrderListByOrderNoOrDate(param);
    }

    @RequestMapping(value = "/getDmOrderByOrderNo", method = RequestMethod.POST)
    public DmOrder getDmOrderByOrderNo(@RequestParam("orderNo") String orderNo) throws Exception {
        return dmOrderMapper.getDmOrderByOrderNo(orderNo);
    }

    @RequestMapping(value = "/flushCancelOrderType", method = RequestMethod.POST)
    public boolean flushCancelOrderType() throws Exception {
        Integer flag = 0;
        flag = dmOrderMapper.flushCancelOrderType();
        return flag > 0 ? true : false;
    }

    @RequestMapping(value = "/getDmOrderByOrderTypeAndTime", method = RequestMethod.POST)
    public List<DmOrder> getDmOrderByOrderTypeAndTime() throws Exception {
        return dmOrderMapper.getDmOrderByOrderTypeAndTime();
    }

    @RequestMapping(value = "/processed", method = RequestMethod.POST)
    public boolean processed(@RequestParam("orderNo") String orderNo, Integer flag) throws Exception {
        if (EmptyUtils.isEmpty(orderNo) || EmptyUtils.isEmpty(flag)) {
            return false;
        }
        DmOrder dmOrder = this.getDmOrderByOrderNo(orderNo);
        String tradeNo = flag == Constants.PayMethod.WINXI ? dmOrder.getWxTradeNo() : dmOrder.getAliTradeNo();
        return dmOrder.getOrderType().equals(0) && !EmptyUtils.isEmpty(tradeNo);
    }
}
