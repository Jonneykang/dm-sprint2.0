package cn.dm.service;

import cn.dm.common.Dto;
import cn.dm.pojo.DmLinkUser;
import cn.dm.vo.LinkUserVo;
import cn.dm.vo.QueryLinkUserVo;

import java.util.List;

/**
 * Created by Administrator on 2018-5-21.
 */
public interface DmLinkUserService {
    public List<QueryLinkUserVo> findLinkUserByUserId(Long userId) throws Exception;
    public Integer createLinkUser(DmLinkUser dmLinkUser) throws Exception;
    public boolean isExistsLinkUser(String idCard) throws Exception;
    public Integer deleteLinkUser(Long id) throws Exception;
    public DmLinkUser queryLinkUserByIdCard(String idCard) throws Exception;
}
