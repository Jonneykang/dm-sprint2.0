package cn.dm.service;

import cn.dm.client.RestDmUserClient;
import cn.dm.pojo.DmUser;
import cn.dm.vo.QueryUserVo;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2018-5-24.
 */
public interface DmUserInfoService {

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     * @throws Exception
     */
    public QueryUserVo queryUserInfoById(Long id) throws Exception;

    /**
     * 修改用户信息——初版
     * @param queryUserVo
     * @return
     * @throws Exception
     */
    public Integer qdtxModifyDmUser(QueryUserVo queryUserVo) throws Exception;
}
