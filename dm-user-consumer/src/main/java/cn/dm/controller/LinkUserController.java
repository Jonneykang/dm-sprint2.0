package cn.dm.controller;

import cn.dm.common.BaseException;
import cn.dm.common.Dto;
import cn.dm.common.DtoUtil;
import cn.dm.common.EmptyUtils;
import cn.dm.exception.LinkUserErrorCode;
import cn.dm.pojo.DmLinkUser;
import cn.dm.pojo.DmUser;
import cn.dm.service.DmLinkUserService;
import cn.dm.service.DmLoginService;
import cn.dm.vo.DmUserVO;
import cn.dm.vo.LinkUserVo;
import cn.dm.vo.QueryLinkUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-5-21.
 */
@Controller
@RequestMapping("/api/v")
public class LinkUserController {
    private static final Logger logger = LoggerFactory.getLogger(LinkUserController.class);
    @Resource
    private DmLinkUserService dmLinkUserService;
    @Resource
    private DmLoginService dmLoginService;

    /**
     * 根据当前登录人查询常用联系人
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/queryLinkUser", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto queryLinkUserByUserId(HttpServletRequest request) throws Exception {
        //获取token
        String tokenString = request.getHeader("token");
        //根据token获取当前登录人的信息
        DmUserVO dmUserVO = dmLoginService.loadCurrentUserByTokenAsDmUserVo(tokenString);
        //根据当前登录用户的id获取常用购票人的信息
        logger.info("[queryLinkUserByUserId]" + "当前登录人的id为：" + dmUserVO.getUserId());
        List<QueryLinkUserVo> queryLinkUserVos = dmLinkUserService.findLinkUserByUserId(dmUserVO.getUserId());
        return DtoUtil.returnDataSuccess(queryLinkUserVos);
    }

    /**
     * 新增常用联系人
     *
     * @param linkUserVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/addLinkUser", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto addLinkUser(@RequestBody LinkUserVo linkUserVo, HttpServletRequest request) throws Exception {
        //获取token
        String tokenString = request.getHeader("token");
        //根据token获取当前登录人的信息
        DmUserVO dmUserVO = dmLoginService.loadCurrentUserByTokenAsDmUserVo(tokenString);
        DmLinkUser dmLinkUser = new DmLinkUser();
        dmLinkUser.setCardType(linkUserVo.getCardType());
        dmLinkUser.setIdCard(linkUserVo.getIdCard());
        dmLinkUser.setName(linkUserVo.getName());
        //根据当前登录人的信息获取当前登录人的id
        dmLinkUser.setUserId(dmUserVO.getUserId());
        Integer i = dmLinkUserService.createLinkUser(dmLinkUser);
        return DtoUtil.returnDataSuccess(i);
    }

    /**
     * 根据身份证查询常用联系人是否存在
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/queryLinkUserByIdCard", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto queryLinkUserByIdCard(@RequestBody Map<String, String> param) throws Exception {
        String idCard = param.get("idCard");
        logger.info("[queryLinkUserByIdCard]" + "身份证号为：" + idCard);
        boolean flag = dmLinkUserService.isExistsLinkUser(idCard);
        return DtoUtil.returnDataSuccess(flag);
    }

    /**
     * 根据id删除常用联系人
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/deleteLinkUserById", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto deleteLinkUserById(@RequestBody Map<String, String> param) throws Exception {
        String id = param.get("id");
        logger.info("[deleteLinkUserById]" + "要删除的常用联系人的id为：" + id);
        Integer i = dmLinkUserService.deleteLinkUser(Long.parseLong(id));
        return DtoUtil.returnDataSuccess(i);
    }
}
