package cn.dm.controller;

import cn.dm.common.Dto;
import cn.dm.common.DtoUtil;
import cn.dm.pojo.DmUser;
import cn.dm.service.DmLoginService;
import cn.dm.service.DmUserInfoService;
import cn.dm.vo.DmUserVO;
import cn.dm.vo.QueryUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Administrator on 2018-5-24.
 */
@Controller
@RequestMapping("/api/v")
public class DmUserInfoController {
    private static final Logger logger = LoggerFactory.getLogger(LinkUserController.class);
    @Resource
    private DmUserInfoService dmUserInfoService;

    @Resource
    private DmLoginService dmLoginService;

    /**
     * 根据用户的id查询用户信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryUserInfoByToken", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto queryUserInfoById(@RequestHeader String token) throws Exception {
        DmUserVO dmUserVO=dmLoginService.loadCurrentUserByTokenAsDmUserVo(token);
        logger.info("[queryUserInfoById]" + "获取用户信息成功，用户名为："+dmUserVO.getNickName());
        QueryUserVo queryUserVo = dmUserInfoService.queryUserInfoById(dmUserVO.getUserId());
        return DtoUtil.returnDataSuccess(queryUserVo);
    }

    /**
     * 修改用户信息-初版
     * @param queryUserVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/modifyUserInfo", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto modifyUserInfo(@RequestBody QueryUserVo queryUserVo,@RequestHeader String token) throws Exception{
        DmUserVO dmUserVO=dmLoginService.loadCurrentUserByTokenAsDmUserVo(token);
        queryUserVo.setId(dmUserVO.getUserId());
        logger.info("[modifyUserInfo]" + "修改用户信息，用户id为："+dmUserVO.getUserId());
        Integer i = dmUserInfoService.qdtxModifyDmUser(queryUserVo);
        return DtoUtil.returnSuccess();
    }



}
