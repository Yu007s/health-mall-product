package com.drstrong.health.product.model.entity.incentive;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.handler.mybatis.IncentivePolicyInfoTypeHandler;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.EarningPolicyTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/7 16:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "pms_sku_incentive_policy", autoResultMap = true)
public class SkuIncentivePolicyEntity extends BaseStandardEntity implements Serializable {
	private static final long serialVersionUID = -2508403160360230319L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 店铺 id，对应 pms_store 表的主键
	 */
	private Long storeId;

	/**
	 * sku编码
	 */
	private String skuCode;

	/**
	 * 成本价
	 */
	private Long costPrice;

	/**
	 * sku激励政策信息,json字段存储
	 */
	@TableField(value = "incentive_policy_info", typeHandler = IncentivePolicyInfoTypeHandler.class)
	private List<IncentivePolicyInfo> incentivePolicyInfo;

	public static SkuIncentivePolicyEntity buildDefaultEntity(Long operatorId) {
		SkuIncentivePolicyEntity skuIncentivePolicyEntity = new SkuIncentivePolicyEntity();
		skuIncentivePolicyEntity.setVersion(1);
		skuIncentivePolicyEntity.setIncentivePolicyInfo(new ArrayList<>());
		skuIncentivePolicyEntity.setDelFlag(DelFlagEnum.UN_DELETED.getCode());
		skuIncentivePolicyEntity.setCreatedBy(operatorId);
		skuIncentivePolicyEntity.setChangedAt(LocalDateTime.now());
		skuIncentivePolicyEntity.setChangedAt(LocalDateTime.now());
		skuIncentivePolicyEntity.setChangedBy(operatorId);
		return skuIncentivePolicyEntity;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class IncentivePolicyInfo implements Serializable {
		private static final long serialVersionUID = 3233394834634077751L;

		/**
		 * 配置表id,对应pms_incentive_policy_config表主键
		 */
		private Long policyConfigId;

		/**
		 * 政策类型 1-固定奖励(单位:元),2-比例提成,3-无收益
		 * {@link EarningPolicyTypeEnum}
		 */
		private Integer policyType;

		/**
		 * 政策值
		 */
		private BigDecimal policyValue;
	}
}
