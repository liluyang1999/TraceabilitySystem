package com.example.server_framework.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

/**
 * Framework: "com.google.code.kaptcha"
 * Captcha Configuration
 */
@Configuration
public class CaptchaConfig {

    @Bean(name = "textCaptchaProducer")
    public DefaultKaptcha getTextCaptchaProducer() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(KAPTCHA_BORDER, "yes");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "150");
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "40");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean(name = "mathCaptchaProducer")
    public DefaultKaptcha getMathCaptchaProducer() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(KAPTCHA_BORDER, "yes");
        properties.setProperty(KAPTCHA_BORDER_COLOR, "105,179,90");

        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "com.example.server_framework.captcha.CaptchaTextCreator");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");

        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "150");
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "40");
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
