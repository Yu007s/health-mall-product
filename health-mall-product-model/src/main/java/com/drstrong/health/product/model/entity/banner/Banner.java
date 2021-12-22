package com.drstrong.health.product.model.entity.banner;

import cn.strong.mybatis.model.MyBatisPlusBaseModel;
import com.baomidou.mybatisplus.annotation.*;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 轮播图
 * </p>
 *
 * @author mybatis plus generator
 * @since 2021-12-13
 */
@Data
@Accessors(chain = true)
@TableName("mall_banner")
public class Banner   implements Serializable  {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 轮播图名称
     */
    private String bannerName;

    /**
     * 图片url
     */
    private String photoUrl;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 链接地址
     */
    private String linkAddress;

    /**
     * 轮播图显示位置，1:首页
     */
    private Integer location;

    /**
     * 1:跳转链接 2:跳转商品
     */
    private Integer linkType;

    /**
     * 商品SPU_Sn
     */
    private String productSpuSn;

    /**
     * 序列号
     */
    private Integer sort;

    /**
     * 展示状态，0:待上架，1:已上架 2:已过期
     */
    private Integer showStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 修改时间
     */
    private LocalDateTime changedAt;

    /**
     * 修改人
     */
    private Long changedBy;

    /**
     * 是否删除 0：正常 1：删除
     */
    private Integer delFlag;

}
