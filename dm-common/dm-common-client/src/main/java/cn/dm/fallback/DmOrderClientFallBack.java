package cn.dm.fallback;

import cn.dm.pojo.DmOrder;

import cn.dm.client.RestDmOrderClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DmOrderClientFallBack implements RestDmOrderClient {


    @Override
    public DmOrder getDmOrderById(Long id) throws Exception {
        return null;
    }

    @Override
    public List<DmOrder> getDmOrderListByMap(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Integer getDmOrderCountByMap(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Long qdtxAddDmOrder(DmOrder dmOrder) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxModifyDmOrder(DmOrder dmOrder) throws Exception {
        return null;
    }

    @Override
    public List<DmOrder> getDmOrderListByOrderNoOrDate(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public DmOrder getDmOrderByOrderNo(String orderNo) throws Exception {
        return null;
    }

    @Override
    public Integer deleteDmOrderById(Long id) throws Exception {
        return null;
    }

    public boolean flushCancelOrderType() throws Exception {
        return false;
    }

    @Override
    public List<DmOrder> getDmOrderByOrderTypeAndTime() throws Exception {
        return null;
    }

    @Override
    public boolean processed(String orderNo, Integer flag) throws Exception {
        return false;
    }
}
