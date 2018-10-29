package cn.dm.service.impl;

import cn.dm.client.RestDmLinkUserClient;
import cn.dm.common.BaseException;
import cn.dm.common.Dto;
import cn.dm.common.DtoUtil;
import cn.dm.exception.LinkUserErrorCode;
import cn.dm.pojo.DmLinkUser;
import cn.dm.service.DmLinkUserService;
import cn.dm.vo.QueryLinkUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-5-21.
 */
@Service
public class DmLinkUserServiceImpl implements DmLinkUserService{

    private static final Logger logger = LoggerFactory.getLogger(DmLinkUserServiceImpl.class);

    @Resource
    private RestDmLinkUserClient restDmLinkUserClient;

    @Override
    public List<QueryLinkUserVo> findLinkUserByUserId(Long userId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        List<DmLinkUser> dmLinkUsers = restDmLinkUserClient.getDmLinkUserListByMap(params);
        List<QueryLinkUserVo> queryLinkUserVos = new ArrayList<QueryLinkUserVo>();
         if (dmLinkUsers.size() >0){
             logger.info("[findLinkUserByUserId]" + "获取到的常用联系人的数目为："+dmLinkUsers.size());
             //将查询到的结果放到vo中返回
            for(DmLinkUser dmLinkUser:dmLinkUsers){
                QueryLinkUserVo queryLinkUserVo = new QueryLinkUserVo();
                BeanUtils.copyProperties(dmLinkUser, queryLinkUserVo);
                queryLinkUserVos.add(queryLinkUserVo);
            }
            return queryLinkUserVos;
        }else{
            throw new BaseException(LinkUserErrorCode.LINKUSER_NO_DATA);
        }
    }

    @Override
    public Integer createLinkUser(DmLinkUser dmLinkUser) throws Exception {
        int i = restDmLinkUserClient.qdtxAddDmLinkUser(dmLinkUser);
        if(i>0){
            logger.info("[createLinkUser]" + "添加常用联系人成功");
            return i;
        }else{
            throw new BaseException(LinkUserErrorCode.LINKUSER_ADD_FAIL);
        }
    }

    @Override
    public boolean isExistsLinkUser(String idCard) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("idCard", idCard);
        List<DmLinkUser> dmLinkUsers = restDmLinkUserClient.getDmLinkUserListByMap(param);
        if (dmLinkUsers.size() == 0){
            return true;
        }else{
            throw new BaseException(LinkUserErrorCode.LINKUSER_NO_EXIST);
        }
    }

    @Override
    public Integer deleteLinkUser(Long id) throws Exception {
        int i = restDmLinkUserClient.deleteDmLinkUserById(id);
        if(i<=0){
            throw new BaseException(LinkUserErrorCode.LINKUSER_DEL_FAIL);
        }
        logger.info("[createLinkUser]" + "删除常用联系人成功");
        return i;
    }

    @Override
    public DmLinkUser queryLinkUserByIdCard(String idCard) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idCard", idCard);
        List<DmLinkUser> dmLinkUsers = restDmLinkUserClient.getDmLinkUserListByMap(params);
        if(dmLinkUsers.size() == 1){
            return dmLinkUsers.get(0);
        }else{
            throw  new BaseException(LinkUserErrorCode.COMMON_Exception);
        }
    }
}
