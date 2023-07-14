package com.drstrong.health.product.model.request.store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2022/8/8 19:36
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("店铺和互联网 id 的对应关系")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgencyStoreVO implements Serializable {
	private static final long serialVersionUID = 116643248551824963L;

	@ApiModelProperty("互联网医院 id")
	private Long agencyId;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("店铺类型：0-互联网医院 1-其它")
	private Integer storeType;
}
