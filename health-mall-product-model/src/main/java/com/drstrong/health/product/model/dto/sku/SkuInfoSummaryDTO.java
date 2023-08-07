package com.drstrong.health.product.model.dto.sku;

import com.drstrong.health.product.model.dto.stock.SkuCanStockDTO;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.response.chinese.ChineseManagerSkuVO;
import com.drstrong.health.product.model.response.product.v3.AgreementSkuInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author liuqiuyi
 * @date 2023/7/7 17:30
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("sku基础信息")
public class SkuInfoSummaryDTO implements Serializable {
    private static final long serialVersionUID = -9104438426550300646L;

    /**
     * @see ProductTypeEnum
     */
    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("商品类型 名称")
    private String productTypeName;

    @ApiModelProperty("中药sku的信息")
    private List<ChineseManagerSkuVO> chineseManagerSkuVoList;

    @ApiModelProperty("西药/中成药 的sku信息")
    private List<AgreementSkuInfoVO> westernSkuInfoVoList;

    @ApiModelProperty("协定方 的sku信息")
    private List<AgreementSkuInfoVO> agreementSkuInfoVoList;

    @ApiModelProperty("sku对应的库存信息")
    private Map<String, List<SkuCanStockDTO>> skuCanStockList;
}
