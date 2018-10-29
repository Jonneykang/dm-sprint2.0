package cn.dm.fallback;

import cn.dm.pojo.DmUser;

import cn.dm.client.RestDmUserClient;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
@Component
public class DmUserClientFallBack implements RestDmUserClient {


    @Override
    public DmUser checkLoginByPassword(DmUser dmUser) throws Exception {
        return null;
    }

    @Override
    public DmUser getDmUserById(Long id)throws Exception{
        return null;
    }

    @Override
    public List<DmUser>	getDmUserListByMap(Map<String,Object> param)throws Exception{
        return null;
    }

    @Override
    public DmUser findByWxUserId(String wxUserId) throws Exception {
        return null;
    }

    @Override
    public Long createDmUser(DmUser dmUser) throws Exception {
        return null;
    }

    @Override
    public String generateToken(DmUser dmUser) throws Exception {
        return null;
    }

    @Override
    public Integer getDmUserCountByMap(Map<String,Object> param)throws Exception{
        return null;
    }

    @Override
    public Integer qdtxAddDmUser(DmUser dmUser)throws Exception{
        return null;
    }

    @Override
    public Integer qdtxModifyDmUser(DmUser dmUser)throws Exception{
        return null;
    }
}
