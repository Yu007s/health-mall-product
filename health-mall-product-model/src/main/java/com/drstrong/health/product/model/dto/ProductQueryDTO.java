package com.drstrong.health.product.model.dto;

import com.drstrong.health.product.model.request.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 商品查询的公共对象
 *
 * @author liuqiuyi
 * @date 2021/12/15 21:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductQueryDTO extends PageRequest implements Serializable {
	private static final long serialVersionUID = -8597026007474387709L;

	/**
	 * 商品 id
	 */
	private Long productId;
	/**
	 * spu 编码
	 */
	private String spuCode;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 店铺 id
	 */
	private String storeId;
	/**
	 * 开始时间
	 */
	private LocalDateTime createStart;
	/**
	 * 结束时间
	 */
	private LocalDateTime createEnd;
	/**
	 * 后台分类 id 集合
	 */
	private Set<Long> backCategoryIdList;
}
