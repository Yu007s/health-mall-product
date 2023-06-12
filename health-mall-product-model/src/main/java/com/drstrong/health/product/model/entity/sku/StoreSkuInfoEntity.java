package com.drstrong.health.product.model.entity.sku;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.handler.mybatis.LongListTypeHandler;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/8 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "pms_store_sku_info", autoResultMap = true)
public class StoreSkuInfoEntity extends BaseStandardEntity implements Serializable {
	private static final long serialVersionUID = -5946977622740078345L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * sku类型 0-商品，1-药品，2-中药，3-协定方
	 */
	private int skuType;

	/**
	 * sku编码 商品-p开头，药品-m开头，中药-z开头，协定方-x开头
	 */
	private String skuCode;

	/**
	 * sku名称
	 */
	private String skuName;

	/**
	 * 安全用药库的 code
	 */
	private String medicineCode;

	/**
	 * 店铺id
	 */
	private Long storeId;

	/**
	 * 价格，单位：分
	 */
	private Long price;

	/**
	 * sku上下架状态；0-未上架，1-已上架
	 */
	private int skuStatus;

	/**
	 * sku关联的供应商信息，json存储
	 */
	@TableField(value = "supplier_info", typeHandler = LongListTypeHandler.class)
	private List<Long> supplierInfo;

	/**
	 * 展示的分类信息,json 存储
	 */
	@TableField(value = "category_info", typeHandler = LongListTypeHandler.class)
	private List<Long> categoryInfo;

	/**
	 * sku标签信息，json存储
	 */
	@TableField(value = "label_info", typeHandler = LongListTypeHandler.class)
	private List<Long> labelInfo;

	/**
	 * sku禁售区域信息，json存储
	 */
	@TableField(value = "prohibit_area_info", typeHandler = LongListTypeHandler.class)
	private List<Long> prohibitAreaInfo;

	public static StoreSkuInfoEntity buildDefault(Long operatorId) {
		StoreSkuInfoEntity storeSkuInfoEntity = new StoreSkuInfoEntity();
		storeSkuInfoEntity.setSupplierInfo(Lists.newArrayList());
		storeSkuInfoEntity.setLabelInfo(Lists.newArrayList());
		storeSkuInfoEntity.setProhibitAreaInfo(Lists.newArrayList());
		storeSkuInfoEntity.setVersion(1);
		storeSkuInfoEntity.setDelFlag(DelFlagEnum.UN_DELETED.getCode());
		storeSkuInfoEntity.setCreatedBy(operatorId);
		storeSkuInfoEntity.setChangedAt(LocalDateTime.now());
		storeSkuInfoEntity.setChangedAt(LocalDateTime.now());
		storeSkuInfoEntity.setChangedBy(operatorId);
		return storeSkuInfoEntity;
	}
}
