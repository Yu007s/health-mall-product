package com.drstrong.health.product.config;

import com.drstrong.health.product.utils.ApplicationContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 公共配置类
 *
 * @author liuqiuyi
 * @date 2022/8/1 21:35
 */
@Configuration
public class CommonConfig {

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }
}
