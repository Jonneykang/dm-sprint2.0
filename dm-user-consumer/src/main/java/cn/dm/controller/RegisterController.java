package cn.dm.controller;

import cn.dm.common.Dto;
import cn.dm.common.DtoUtil;
import cn.dm.common.MD5;
import cn.dm.exception.RegisterErrorCode;
import cn.dm.pojo.DmUser;
import cn.dm.service.DmRegisterService;
import cn.dm.vo.DmUserRegisterVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户注册控制器
 */
@Controller
@RequestMapping(value = "/api/p")
public class RegisterController {

    @Resource
    private DmRegisterService dmRegisterService;

    /**
     * 向指定邮箱发送验证码
     *
     * @param phone 目标邮箱
     * @return 发送结果
     * @throws Exception
     */
    @RequestMapping(value = "/code", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto sendVerificationCodeByEmail(@RequestBody Map<String, String> phone) throws Exception {
        dmRegisterService.sendVerificationCodeByEmail(phone.get("phone"));
        return DtoUtil.returnSuccess("验证码已发送");
    }

    /**
     * 验证邮箱是否已被注册
     *
     * @param phone 待验证的邮箱
     * @return 验证结果
     * @throws Exception
     */
    @RequestMapping(value = "/checkEmail", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto checkEmail(@RequestBody Map<String, String> phone) throws Exception {
        boolean result = dmRegisterService.checkEmailRegistered(phone.get("phone"));
        if (result) {
            return DtoUtil.returnFail(RegisterErrorCode.USER_REGISTER_EMAIL_EXIST.getErrorMessage(),
                    RegisterErrorCode.USER_REGISTER_EMAIL_EXIST.getErrorCode());
        } else {
            return DtoUtil.returnSuccess("邮箱未被注册");
        }
    }

    /**
     * 使用邮箱注册用户
     *
     * @param registerInfo 用户输入的注册信息，包括验证码
     * @return 注册结果
     * @throws Exception
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public Dto registerUserByEmail(@RequestBody DmUserRegisterVO registerInfo) throws Exception {
        DmUser dmUser = new DmUser();
        BeanUtils.copyProperties(registerInfo, dmUser);
        dmUser.setPassword(MD5.getMd5(dmUser.getPassword(), 32));
        dmRegisterService.registerUserByEmail(dmUser, registerInfo.getVcode());
        return DtoUtil.returnSuccess("注册成功");
    }
}

