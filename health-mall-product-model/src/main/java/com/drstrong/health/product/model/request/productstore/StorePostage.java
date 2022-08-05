package com.drstrong.health.product.model.request.productstore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 店铺的配送费 response
 *
 * @author liuqiuyi
 * @date 2021/12/7 10:33
 */
@Data
@ApiModel("店铺的配送费信息")
public class StorePostage implements Serializable {
	private static final long serialVersionUID = 6078954806565513548L;

    @ApiModelProperty("店铺ID")
    private Long storeId;

	@ApiModelProperty("店铺包邮额度")
	private BigDecimal freePostage;

	@ApiModelProperty("店铺各地区邮费")
	private List<AreaPostage> areaPostageList;
}
