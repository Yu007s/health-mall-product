package com.drstrong.health.product.model.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品基础信息
 *
 * @author liuqiuyi
 * @date 2021/12/13 15:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_product_basics_info")
public class ProductBasicsInfoEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3825569002454320604L;

	/**
	 * 自增主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * spu编码
	 */
	private String spuCode;

	/**
	 * 商品标题
	 */
	private String title;

	/**
	 * 品牌名称
	 */
	private String brandName;

	/**
	 * 商品别名
	 */
	private String aliasName;

	/**
	 * 主图地址
	 */
	private String masterImageUrl;

	/**
	 * 商品后端分类 id
	 */
	private Long categoryId;

	/**
	 * 商品来源(区分不同的店铺来源)
	 */
	private Long sourceId;

	/**
	 * 商品来源名称(冗余字段)
	 */
	private String sourceName;

	/**
	 * 商品类型(0-商品，1-药品)
	 */
	private Integer productType;

	/**
	 * 排序字段(值越大优先级越高)
	 */
	private Integer sort;

	/**
	 * 上架状态(0-未上架，1-已上架)
	 */
	private Integer state;

	/**
	 * 乐观锁字段
	 */
	@Version
	private Integer version;
}
