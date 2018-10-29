package cn.dm.service.impl;

import cn.dm.client.RestDmRegisterClient;
import cn.dm.client.RestDmUserClient;
import cn.dm.common.BaseException;
import cn.dm.common.RedisUtils;
import cn.dm.exception.RegisterErrorCode;
import cn.dm.exception.UserErrorCode;
import cn.dm.pojo.DmUser;
import cn.dm.service.DmRegisterService;
import cn.dm.utils.SimpleMailServiceUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class DmRegisterServiceImpl implements DmRegisterService {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private SimpleMailServiceUtil mailServiceUtil;
    @Resource
    private RestDmRegisterClient restDmRegisterClient;
    @Resource
    private RestDmUserClient restDmUserClient;

    private final String PREFIX = "Verification:";
    private final String SUFFIX = "_frz";

    @Override
    public void sendVerificationCodeByEmail(String mailTo) throws Exception {
        String vfCodeKey = PREFIX + mailTo;
        String vfFrzKey = PREFIX + mailTo + SUFFIX;
        // 验证是否仍在冻结时间内
        if (redisUtils.exist(vfFrzKey))
            throw new BaseException(RegisterErrorCode.USER_REGISTER_FRZ);
        // 验证数据库中邮箱是否已注册
        if (this.checkEmailRegistered(mailTo))
            throw new BaseException(RegisterErrorCode.USER_REGISTER_EMAIL_EXIST);

        String verificationCode = restDmRegisterClient.generateVerificationCode();
        try {
            mailServiceUtil.sendMail(mailTo, verificationCode);
        } catch (Exception e) {
            throw new BaseException(RegisterErrorCode.USER_REGISTER_VFCODE_SEND_FAILED);
        }
        redisUtils.set(vfFrzKey, 5 * 60, "true");
        redisUtils.set(vfCodeKey, 30 * 60, verificationCode);

    }

    @Override
    public boolean checkEmailRegistered(String email) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("phone", email);
        Integer count = restDmUserClient.getDmUserCountByMap(params);
        return (count != null && count > 0);
    }

    @Override
    public void registerUserByEmail(DmUser dmUser, String vcode) throws Exception {
        String vfCodeKey = PREFIX + dmUser.getPhone();
        String vfCode = (String) redisUtils.get(vfCodeKey);
        // 判断验证码是否正确
        if (vfCode == null || (! vfCode.equals(vcode)))
            throw new BaseException(UserErrorCode.USER_VFCODE_INVALID);
        // 验证数据库中邮箱是否已注册
        if (this.checkEmailRegistered(dmUser.getPhone()))
            throw new BaseException(RegisterErrorCode.USER_REGISTER_EMAIL_EXIST);
        restDmUserClient.qdtxAddDmUser(dmUser);
    }
}
