package com.drstrong.health.product;

import cn.strong.mybatis.config.StrongMybatisPlusExtendConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2021/6/8 15:39.
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan({"com.drstrong.health.product","com.drstrong.health.redis"})
public class ProductSpringBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductSpringBootApplication.class, args);
	}
}
