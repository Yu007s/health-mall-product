package com.drstrong.health.product.model.entity.sku;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.enums.ScheduledStatusEnum;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liuqiuyi
 * @date 2023/6/14 16:16
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName(value = "pms_sku_scheduled_config", autoResultMap = true)
public class SkuScheduledConfigEntity extends BaseStandardEntity implements Serializable {
	private static final long serialVersionUID = -6063715523950040985L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 商品sku编码，对应p_sku表number字段
	 */
	private String skuCode;

	/**
	 * 定时配置类型 1-定时上架，2-定时下架
	 * {@link ScheduledStatusEnum}
	 */
	private int scheduledType;

	/**
	 * 配置时间
	 */
	private LocalDateTime scheduledDateTime;

	/**
	 * 0-待处理，1-已处理，2-已取消(例如手动上架了)
	 */
	private int scheduledStatus;
}
