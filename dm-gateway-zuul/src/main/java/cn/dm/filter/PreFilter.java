package cn.dm.filter;

import cn.dm.common.DtoUtil;
import cn.dm.common.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreFilter.class);

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

   /* @Value("${tokenValidation}")
    private boolean tokenValidation;*/
   private boolean tokenValidation = true;//默认开启token认证


    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        LOGGER.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());
        //根据线上配置是否需要开启token验证
        if (!tokenValidation) {
            return "pass";
        }
        String accessToken = request.getHeader("token");
        String requestUrl = request.getRequestURI().toString();
        if (requestUrl.split("/")[3].equals("p")) {
            LOGGER.info("此接口不需要token验证，直接通行");
            return "pass";
        }
        if (null == accessToken || !redisUtils.exist(accessToken)) {
            LOGGER.warn("access token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody(JSONObject.toJSON(DtoUtil.returnFail("401! Access without permission, please login first.", "0001")).toString());
            return "Access denied";
        }
        LOGGER.info("access token ok");
        return "pass";
    }
}
