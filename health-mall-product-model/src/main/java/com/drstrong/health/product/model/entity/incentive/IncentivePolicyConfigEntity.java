package com.drstrong.health.product.model.entity.incentive;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liuqiuyi
 * @date 2023/6/7 14:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "pms_incentive_policy_config", autoResultMap = true)
public class IncentivePolicyConfigEntity extends BaseStandardEntity implements Serializable {
	private static final long serialVersionUID = -2329995554686133026L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 店铺 id，对应 pms_store 表的主键
	 */
	private Long storeId;

	/**
	 * 配置目标类型,和sku的类型字段一致 0-商品，1-药品，2-中药,3-协定方
	 * {@link com.drstrong.health.product.model.enums.ProductTypeEnum}
	 */
	private Integer configGoalType;

	/**
	 * 收益名称,同店铺同类型下不能重复
	 */
	private String earningName;

	public static IncentivePolicyConfigEntity buildDefaultEntity(Long operatorId) {
		IncentivePolicyConfigEntity incentivePolicyConfigEntity = new IncentivePolicyConfigEntity();
		incentivePolicyConfigEntity.setVersion(1);
		incentivePolicyConfigEntity.setDelFlag(DelFlagEnum.UN_DELETED.getCode());
		incentivePolicyConfigEntity.setCreatedBy(operatorId);
		incentivePolicyConfigEntity.setChangedAt(LocalDateTime.now());
		incentivePolicyConfigEntity.setChangedAt(LocalDateTime.now());
		incentivePolicyConfigEntity.setChangedBy(operatorId);
		return incentivePolicyConfigEntity;
	}
}
