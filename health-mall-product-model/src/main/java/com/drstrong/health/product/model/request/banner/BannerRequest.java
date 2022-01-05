package com.drstrong.health.product.model.request.banner;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

/**
 * @description: 轮播图-请求
 * @Author: JiaoYuSheng
 * @Date: 2021-12-14 11:44
 * @program health-mall-postsale
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("轮播图-请求")
public class BannerRequest {

    @ApiModelProperty("主键")
    private Long id;

    @Size(max = 20)
    @ApiModelProperty("轮播图名称")
    private String bannerName;

    @NotEmpty(message = "图片URL不得为空")
    @ApiModelProperty("图片url")
    private String photoUrl;

    @ApiModelProperty("链接地址")
    private String linkAddress;

    @ApiModelProperty(value = "轮播图显示位置，1:首页" )
    private Integer location = 1;

    @NotNull
    @ApiModelProperty("1:跳转链接 2:跳转商品")
    private Integer linkType;

    @ApiModelProperty("商品SPU_Sn")
    private String productSpuSn;

    @Min(1)
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

    @ApiModelProperty(value = "操作人 id", hidden = true)
    private Long userId;
}
