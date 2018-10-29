package cn.dm.fallback;

import cn.dm.client.RestDmRegisterClient;
import org.springframework.stereotype.Component;

@Component
public class DmRegisterClientFallBack implements RestDmRegisterClient {

    @Override
    public String generateVerificationCode() throws Exception {
        return null;
    }
}
