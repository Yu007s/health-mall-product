package com.drstrong.health.product.model.response.banner;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @description: 轮播图列表
 * @Author: JiaoYuSheng
 * @Date: 2021-12-14 17:34
 * @program health-mall-postsale
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("轮播图列表")
public class BannerListResponse {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("轮播图名称")
    private String bannerName;

    @ApiModelProperty("图片url")
    private String photoUrl;

    @ApiModelProperty("链接地址")
    private String linkAddress;

    @ApiModelProperty(value = "轮播图显示位置，1:首页" )
    private Integer location = 1;

    @ApiModelProperty("1:跳转链接 2:跳转商品")
    private Integer linkType;

    @ApiModelProperty("商品SPU_Sn")
    private String productSpuSn;

    @ApiModelProperty("序列号")
    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("展示状态，0:待上架，1:已上架 2:已过期")
    private Integer showStatus;

    @ApiModelProperty("商品主图")
    private String masterImageUrl;
}
