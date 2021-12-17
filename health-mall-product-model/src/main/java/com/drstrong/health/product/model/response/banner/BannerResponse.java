package com.drstrong.health.product.model.response.banner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 轮播图返回
 * @Author: JiaoYuSheng
 * @Date: 2021-12-14 09:38
 * @program health-mall-postsale
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("轮播图返回")
public class BannerResponse implements Serializable {

    @ApiModelProperty("图片url")
    private String photoUrl;

    @ApiModelProperty("轮播图名称")
    private String bannerName;

    @ApiModelProperty("链接地址")
    private String linkAddress;

    @ApiModelProperty("1:跳转链接 2:跳转商品")
    private Integer linkType;

    @ApiModelProperty("商品SPU_Sn")
    private String productSpuSn;

}
