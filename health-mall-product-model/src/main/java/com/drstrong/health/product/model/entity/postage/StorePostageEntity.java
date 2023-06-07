package com.drstrong.health.product.model.entity.postage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.handler.mybatis.SupplierAreaPostageInfoTypeHandler;
import com.drstrong.health.product.handler.mybatis.SupplierFreePostageInfoTypeHandler;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/6 11:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "pms_store_postage", autoResultMap = true)
public class StorePostageEntity extends BaseStandardEntity implements Serializable {
	private static final long serialVersionUID = 1989063757616927698L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 店铺 id
	 */
	private Long storeId;
	/**
	 * 店铺的包邮金额,单位:分
	 */
	private Long freePostage;
	/**
	 * 店铺供应商包邮金额信息,json字段存储
	 */
	@TableField(value = "supplier_free_postage_info", typeHandler = SupplierFreePostageInfoTypeHandler.class)
	private List<SupplierFreePostageInfo> supplierFreePostageInfo;
	/**
	 * 店铺供应商区域邮费设置,json字段存储
	 */
	@TableField(value = "supplier_area_postage_info", typeHandler = SupplierAreaPostageInfoTypeHandler.class)
	private List<SupplierAreaPostageInfo> supplierAreaPostageInfo;

	public static StorePostageEntity buildDefaultEntity(Long operatorId) {
		StorePostageEntity storePostageEntity = new StorePostageEntity();
		storePostageEntity.setSupplierFreePostageInfo(new ArrayList<>());
		storePostageEntity.setSupplierAreaPostageInfo(new ArrayList<>());
		storePostageEntity.setVersion(0);
		storePostageEntity.setDelFlag(DelFlagEnum.UN_DELETED.getCode());
		storePostageEntity.setCreatedBy(operatorId);
		storePostageEntity.setCreatedAt(LocalDateTime.now());
		storePostageEntity.setChangedBy(operatorId);
		storePostageEntity.setChangedAt(LocalDateTime.now());
		return storePostageEntity;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ApiModel("店铺供应商包邮金额信息")
	public static class SupplierFreePostageInfo implements Serializable {
		private static final long serialVersionUID = -6856380478121860116L;
		/**
		 * 供应商 id
		 */
		private Long supplierId;
		/**
		 * 包邮金额,单位:分
		 */
		private Long freePostage;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ApiModel("店铺供应商区域邮费信息")
	public static class SupplierAreaPostageInfo implements Serializable {
		private static final long serialVersionUID = -6856380478121860116L;
		/**
		 * 供应商 id
		 */
		private Long supplierId;
		/**
		 * 区域 id
		 */
		private Long areaId;
		/**
		 * 邮费,金额:分
		 */
		private Long postage;
	}
}
