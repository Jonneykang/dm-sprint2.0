package cn.dm.mapper;

import cn.dm.pojo.DmOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface DmOrderMapper {

    public DmOrder getDmOrderById(@Param(value = "id") Long id) throws Exception;

    public List<DmOrder> getDmOrderListByMap(Map<String, Object> param) throws Exception;

    public Integer getDmOrderCountByMap(Map<String, Object> param) throws Exception;

    public Long insertDmOrder(DmOrder dmOrder) throws Exception;

    public Integer updateDmOrder(DmOrder dmOrder) throws Exception;

    public Integer deleteDmOrderById(@Param(value = "id") Long id) throws Exception;

    public Integer batchDeleteDmOrder(Map<String, List<String>> params);

    public List<DmOrder> getDmOrderListByOrderNoOrDate(Map<String, Object> param);

    public Integer flushCancelOrderType() throws Exception;

    public List<DmOrder> getDmOrderByOrderTypeAndTime() throws Exception;

    public DmOrder getDmOrderByOrderNo(@Param(value = "orderNo") String orderNo) throws Exception;
}
