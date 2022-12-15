package com.example.server_interface;

import com.example.common_resource.constant.CacheConstant;
import com.example.common_resource.exception.BaseException;
import com.example.server_framework.cache.RedisCache;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RequestMapping("/server/captcha")
@Controller
public class CaptchaController {

    @Resource(name = "mathCaptchaProducer")
    private Producer mathCaptchaProducer;

    @Resource(name = "textCaptchaProducer")
    private Producer textCaptchaProducer;

    @Autowired
    private RedisCache redisCache;

    @Value("${captcha-type}")
    private String captchaType;

    @RequestMapping("/getImage")
    public void getImage(HttpServletResponse response) {
        String captchaStr, code = null;
        BufferedImage image = null;

        if ("text".equals(captchaType)) {
            captchaStr = code = textCaptchaProducer.createText();
            image = textCaptchaProducer.createImage(captchaStr);
        } else if ("math".equals(captchaType)) {
            String captchaText = mathCaptchaProducer.createText();
            captchaStr = captchaText.substring(0, captchaText.lastIndexOf("@"));
            code = captchaText.substring(captchaText.lastIndexOf("@") + 1);
            image = mathCaptchaProducer.createImage(captchaStr);
        }

        try {
            redisCache.setCacheObject(CacheConstant.CAPTCHA_CODE_KEY, code,
                    CacheConstant.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
            response.setContentType("image/jpeg");
            ServletOutputStream outputStream = response.getOutputStream();
            assert image != null;
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new BaseException("Error occurred when generating captcha");
        }
    }
}
