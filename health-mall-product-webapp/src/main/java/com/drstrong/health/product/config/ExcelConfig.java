package com.drstrong.health.product.config;

import org.easy.excel.ExcelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc excel导入导出上下文
 * @createTime 2021/12/17 11:09
 * @since TODO
 */
@Configuration
public class ExcelConfig {

    @Bean
    public ExcelContext excelContext(){
        return new ExcelContext("excel-config.xml");
    }
}
