package cn.dm.controller;

import cn.dm.common.Constants;
import cn.dm.common.Dto;
import cn.dm.common.DtoUtil;
import cn.dm.common.MD5;
import cn.dm.exception.UserErrorCode;
import cn.dm.pojo.DmUser;
import cn.dm.service.DmLoginService;
import cn.dm.vo.TokenVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户注册控制器
 */
@Controller
@RequestMapping(value = "/api")
public class DmUserController {

    @Resource
    private DmLoginService dmLoginService;

    /**
     * 账号密码登录
     *
     * @param dmUser 封装 用户注册邮箱 用户密码
     * @return 登录结果，登录成功时包含用户信息和token信息
     * @throws Exception
     */
    @RequestMapping(value = "/p/login", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto loginByPassword(@RequestBody DmUser dmUser) throws Exception {
        dmUser.setPassword(MD5.getMd5(dmUser.getPassword(), 32));
        Object[] results = dmLoginService.login(dmUser);
        if (results == null) {
            return DtoUtil.returnFail(UserErrorCode.USER_LOGIN_FAILED.getErrorMessage(),
                    UserErrorCode.USER_LOGIN_FAILED.getErrorCode());
        } else {
            return DtoUtil.returnSuccess("登录成功", results);
        }

    }

    /**
     * 置换token
     *
     * @param token 旧的token
     * @return 新token，TokenVO格式
     * @throws Exception 未登录异常
     */
    @RequestMapping(value = "/v/replaceToken", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto replaceToken(@RequestHeader String token) throws Exception {
        TokenVO tokenVO = new TokenVO(dmLoginService.replace(token), Constants.Redis_Expire.SESSION_TIMEOUT, new Date().getTime());
        return DtoUtil.returnSuccess("置换成功", tokenVO);
    }

    /**
     * 根据token加载当前登录用户
     *
     * @param token
     * @return DmUserVO格式的用户信息
     * @throws Exception 未登录异常
     */
    @RequestMapping(value = "/v/loadCurrentUserByToken", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto loadCurrentUserByToken(@RequestHeader String token) throws Exception {
        return DtoUtil.returnSuccess("查询成功", dmLoginService.loadCurrentUserByTokenAsDmUserVo(token));
    }

}