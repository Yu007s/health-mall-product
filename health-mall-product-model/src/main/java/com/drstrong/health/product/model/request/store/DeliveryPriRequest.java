package com.drstrong.health.product.model.request.store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author xieYueFeng
 * @Date 2022/08/05/18:59
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("配送优先级返回 某区域对应优先级返回信息")
public class DeliveryPriRequest implements Serializable {
    private static final long serialVersionUID = -1564854894456645741L;

    /**
     * 新接口中,这个字段废弃,为了暂时兼容老逻辑,等供应链三期正式上线后可以去除
     */
    @ApiModelProperty("区域id")
    private List<Long> areaId;

	@ApiModelProperty("区域id")
	private List<AreaInfo> areaInfoList;

    @ApiModelProperty("优先级药店供应商id列表  已排好序")
    private List<Long> supplierIds;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ApiModel("区域信息")
	public static class AreaInfo implements Serializable{
		private static final long serialVersionUID = 676579650324180707L;

		@ApiModelProperty("区域id")
		private Long areaId;

		@ApiModelProperty("父区域id")
		private Long parentAreaId;
	}
}
