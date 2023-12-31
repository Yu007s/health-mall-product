package com.drstrong.health.product.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * mq topic 配置类
 *
 * @author liuqiuyi
 * @date 2021/12/30 11:39
 */
@Data
@Configuration
public class MqTopicConfig {

	/**
	 * 店铺信息变更 topic
	 */
	@Value("${store.change.topic}")
	private String storeChangeTopic;

	/**
	 * 店铺信息变更 tag
	 */
	@Value("${store.change.tag}")
	private String storeChangeTag;

	/**
	 * sku 上下架 topic
	 */
	@Value("${sku.state.stock.topic}")
	private String skuStateStockTopic;

	/**
	 * sku 上下架 tag
	 */
	@Value("${sku.state.stock.tag}")
	private String skuStateStockTag;

	@Value("${health.log.topic}")
	private String logTopic;

	@Value("${health.log.tag}")
	private String logTag;

	@Value("${product.change.topic}")
	private String productChangeTopic;

	@Value("${product.change.tag}")
	private String productChangeTag;
}
