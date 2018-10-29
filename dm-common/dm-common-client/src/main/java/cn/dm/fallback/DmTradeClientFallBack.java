package cn.dm.fallback;

import cn.dm.pojo.DmTrade;

import cn.dm.client.RestDmTradeClient;
import cn.dm.vo.DmItemMessageVo;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
@Component
public class DmTradeClientFallBack implements RestDmTradeClient {

    @Override
    public Integer insertTrade(DmItemMessageVo dmItemMessageVo) throws Exception {
        return null;
    }
}
