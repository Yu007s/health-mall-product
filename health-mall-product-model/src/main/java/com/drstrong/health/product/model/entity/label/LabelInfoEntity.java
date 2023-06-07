package com.drstrong.health.product.model.entity.label;

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
 * @date 2023/6/7 11:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "pms_label_info", autoResultMap = true)
public class LabelInfoEntity extends BaseStandardEntity implements Serializable {
	private static final long serialVersionUID = 6065408216020699574L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 店铺 id，对应 pms_store 表的主键
	 */
	private Long storeId;

	/**
	 * 标签类型，和sku的类型字段一致 0-商品，1-药品，2-中药，3-协定方
	 * {@link com.drstrong.health.product.model.enums.ProductTypeEnum}
	 */
	private int labelType;

	/**
	 * 标签名称
	 */
	private String labelName;

	public static LabelInfoEntity buildDefaultEntity(Long operatorId) {
		LabelInfoEntity labelInfoEntity = new LabelInfoEntity();
		labelInfoEntity.setVersion(1);
		labelInfoEntity.setDelFlag(DelFlagEnum.UN_DELETED.getCode());
		labelInfoEntity.setCreatedBy(operatorId);
		labelInfoEntity.setChangedAt(LocalDateTime.now());
		labelInfoEntity.setChangedAt(LocalDateTime.now());
		labelInfoEntity.setChangedBy(operatorId);
		return labelInfoEntity;
	}
}
