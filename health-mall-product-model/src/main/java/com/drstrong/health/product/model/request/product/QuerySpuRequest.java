package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 查询 spu 的入参
 *
 * @author liuqiuyi
 * @date 2021/12/6 19:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("查询 spu 的入参")
public class QuerySpuRequest extends PageRequest implements Serializable {
	private static final long serialVersionUID = 1883541500871286680L;

	@ApiModelProperty(value = "商品 id", hidden = true)
	private Long productId;

	@ApiModelProperty("spu 编码")
	private String spuCode;

	@ApiModelProperty("商品名称")
	private String productName;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createStart;

	@ApiModelProperty("结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createEnd;

	@ApiModelProperty(value = "上架状态", hidden = true)
	private UpOffEnum upOffEnum;

	@ApiModelProperty(value = "后台分类集合", hidden = true)
	private Set<Long> backCategoryIdList;
}
