package cn.dm.controller;

import cn.dm.common.*;
import cn.dm.pojo.DmUser;
import cn.dm.service.DmLoginService;
import cn.dm.vo.DmUserVO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 第三方登录控制器
 *
 * @author hduser
 */
@Controller
@RequestMapping(value = "/api/p/vendors")
public class VendorsController {
    private static Logger logger = LoggerFactory.getLogger(VendorsController.class);
    @Resource
    private DmLoginService dmLoginService;

    /**
     * 微信登录——第一步：获取code
     *
     * @param response
     */
    @RequestMapping(value = "/wechat/login")
    public void wechatLogin(HttpServletResponse response) throws Exception {
        StringBuilder qrconnect = new StringBuilder("https://open.weixin.qq.com/connect/qrconnect");
        qrconnect.append("?appid=wx9168f76f000a0d4c");
        qrconnect.append("&redirect_uri=http%3a%2f%2fj19h691179.iok.la%2f");
        qrconnect.append("api%2fp%2fvendors%2fwechat%2fcallBack");
        qrconnect.append("&response_type=code");
        qrconnect.append("&scope=snsapi_login");
        qrconnect.append("&state=STATE#wechat_redirect");
        response.sendRedirect(qrconnect.toString());
    }

    /**
     * 微信登录——第二步：通过code换取access_token
     *
     * @param code
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/wechat/callBack")
    public void wechatCallback(@RequestParam String code,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        //定义通过code获取access_token的url地址
        StringBuilder accessTokenUrl = new StringBuilder();
        accessTokenUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token");
        accessTokenUrl.append("?appid=wx9168f76f000a0d4c");
        accessTokenUrl.append("&secret=8ba69d5639242c3bd3a69dffe84336c1");
        accessTokenUrl.append("&code=" + code);
        accessTokenUrl.append("&grant_type=authorization_code");
        response.setContentType("text/html;charset=utf-8");
        String json = UrlUtils.loadURL(accessTokenUrl.toString());
        Map<String, Object> wechatToken = JSON.parseObject(json, Map.class);

        //通过access_token获取用户信息
        String access_token = wechatToken.get("access_token").toString();
        //带着access_token去请求微信官方地址获取用户信息
        StringBuilder userInfoUrl = new StringBuilder("https://api.weixin.qq.com/sns/userinfo");
        userInfoUrl.append("?access_token=" + access_token);
        userInfoUrl.append("&openid=" + wechatToken.get("openid").toString());
        String userInfoJson = UrlUtils.loadURL(userInfoUrl.toString());
        Map<String, Object> userInfoMap = JSON.parseObject(userInfoJson, Map.class);

        //获取token并将用户信息缓存到redis
        String token = dmLoginService.getWxUserInfo(userInfoMap, wechatToken.get("openid").toString());

        //返回前端处理
        String loginPage = "http://192.168.9.151:8888/#/?token="+token;
        response.sendRedirect(loginPage.toString());
    }

    /**
     * 获取微信用户信息
     * @param accessToken 微信会话凭据
     * @param openid 微信用户唯一标识
     * @return
     */
//    @RequestMapping(value = "/wechat/user/info", method = RequestMethod.GET,produces= "application/json")
//    public @ResponseBody
//    Dto wechatUserInfo(
//            @RequestParam String accessToken,
//            @RequestParam String openid){
//        try {
//            //加载用户信息
//            String userInfoJson=UrlUtils.loadURL("https://api.weixin.qq.com/sns/userinfo?access_token="
//                    +accessToken
//                    +"&openid="+ openid
//            );
//            Map<String,Object> userInfo=JSON.parseObject(userInfoJson, Map.class);
//            return DtoUtil.returnDataSuccess(userInfo);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DtoUtil.returnFail(e.getMessage(), "授权失败");
//        }
//    }
}

