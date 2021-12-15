package com.drstrong.health.product.model.request.banner;

import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @description: 获取轮播图列表-请求
 * @Author: JiaoYuSheng
 * @Date: 2021-12-15 15:09
 * @program health-mall-product
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("获取轮播图列表-请求")
public class BannerListRequest extends PageRequest implements Serializable {

    @Size(max = 20)
    @ApiModelProperty("轮播图名称")
    private String bannerName;
    @ApiModelProperty("上架状态，0:待上架，1:已上架 2:已过期")
    private Integer showStatus;
}
