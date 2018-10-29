package cn.dm.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 发送简单邮件的实现类
 */
@Component
public class SimpleMailServiceUtil implements Runnable {
    @Resource
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;
    private static BlockingQueue<SimpleMailMessage> messages = new LinkedBlockingQueue<SimpleMailMessage>(500);

    public SimpleMailServiceUtil() {
        new Thread(this).start();
    }

    public void sendMail(String mailTo, String verificationCode) throws Exception {
        SimpleMailMessage verificationMailMessage = new SimpleMailMessage();
        verificationMailMessage.setFrom(from);
        verificationMailMessage.setTo(mailTo);
        verificationMailMessage.setSubject("【大麦网】请激活您的账户");
        verificationMailMessage.setText("注册邮箱：" + mailTo + "  激活码：" + verificationCode);
        messages.put(verificationMailMessage);
    }

    @Override
    public void run() {
        while (true) {
            try {
                SimpleMailMessage verificationMailMessage = messages.take();
                mailSender.send(verificationMailMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
