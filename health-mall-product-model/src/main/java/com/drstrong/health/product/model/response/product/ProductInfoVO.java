package com.drstrong.health.product.model.response.product;

import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoExtendVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2022/8/8 16:20
 */
@Data
@ApiModel("商品信息的返回值")
public class ProductInfoVO implements Serializable {
	private static final long serialVersionUID = -747362472766957432L;

	@ApiModelProperty("商品类型。0-商品，1-药品，2-中药")
	private Integer productType;

	@ApiModelProperty("商品类型名称")
	private String productTypeName;

	@ApiModelProperty("中药基础信息")
	private List<ChineseSkuInfoExtendVO> chineseSkuInfoExtendVOList;

	@ApiModelProperty("店铺的中药配送优先级")
	private List<Long> storeChineseDeliveryInfoList;
}
