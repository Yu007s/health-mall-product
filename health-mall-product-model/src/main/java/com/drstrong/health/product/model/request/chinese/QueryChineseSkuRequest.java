package com.drstrong.health.product.model.request.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * 对外查询接口-查询中药材的的入参
 *
 * @author liuqiuyi
 * @date 2022/8/4 10:13
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("中药管理页面请求入参")
public class QueryChineseSkuRequest implements Serializable {
	private static final long serialVersionUID = -3646638224917709159L;

	@ApiModelProperty("sku编码")
	@Size(max = 200, message = "一次查询的入参大小不能超过 200")
	private Set<String> skuCodeList;

	@ApiModelProperty("药材 id，主要是兼容老业务，新业务请统一使用 skuCode 查询")
	@Size(max = 200, message = "一次查询的入参大小不能超过 200")
	private Set<Long> medicineIdList;

	@ApiModelProperty("店铺id，配合 medicineIdList 字段使用，和 agencyId 任传其一")
	private Long storeId;

	@ApiModelProperty("互联网医院id，配合 medicineIdList 字段使用，和 shopId 任传其一")
	private Long agencyId;

	@ApiModelProperty("是否需要查询库存")
	private Boolean needQueryStock;

	@ApiModelProperty("是否需要查询供应商配送优先级")
	private Boolean needQueryPriority;
}
