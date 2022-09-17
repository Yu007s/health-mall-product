package com.drstrong.health.product.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 动态更新log4j2配置：
 * <p>
 *     1、从 namespace=log4j2-spring.xml 中载入log4j2的配置；
 *     2、监听 log4j2-spring.xml 的变化并重新刷新 log4j2的配置；
 * </p>
 * 如果不想使用此功能，请设置jvm启动参数 -Dleke.log4j2.dynamic.enabled=false ，或配置Spring参数 leke.log4j2.dynamic.enabled=false
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2021/5/19 09:11.
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnProperty(prefix = "health.log4j2.dynamic", name = "enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class DynamicLog4j2Configuration {

    @ApolloConfig("log4j2-spring.xml")
    private Config log4j2Config;

    @PostConstruct
    public void init() {
        if (log4j2Config == null) {
            log.warn("log4j2Config not init");
            return;
        }
        String xmlContent = log4j2Config.getProperty("content", null);
        if (Strings.isNullOrEmpty(xmlContent)) {
            return;
        }
        refreshLog4j2Config(xmlContent);
    }


    @ApolloConfigChangeListener(value = "log4j2-spring.xml")
    public void refreshConfig(ConfigChangeEvent event) {
        ConfigChange content = event.getChange("content");
        refreshLog4j2Config(content.getNewValue());
    }

    public static void refreshLog4j2Config(String xmlContent) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        try (InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"))) {
            ConfigurationSource source = new ConfigurationSource(inputStream);
            Configuration configuration = new XmlConfigurationFactory().getConfiguration(context, source);
            context.setConfiguration(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
