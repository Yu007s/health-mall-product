package com.drstrong.health.product.model.entity.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 地区
 * @createTime 2021/12/13 10:37
 */
@Data
@TableName("product_city")
public class AreaEntity implements Serializable {

    private static final long serialVersionUID = 2286983794183915722L;
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 省 市 区  名称
     */
    private String name;

    /**
     * 省市区全名
     */
    private String fullName;

    /**
     * 类型:0:全国 1:省 2;市 3:区
     */
    private Integer type;

    /**
     * 父类ID
     */
    private Integer parentId;

    /**
     * 0无效，1有效
     */
    private Integer available;

    /**
     * 父类ID名称
     */
    private String parentName;

    /**
     * 直辖市  0：不是；1：是
     */
    private Integer municipality;

    /**
     * 权重 0：默认权重
     */
    private Integer weight;

    /**
     * 是否热门城市 0：否 1：是 用于代发货项目
     */
    private Integer hot;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 修改时间
     */
    private LocalDateTime changedAt;

    /**
     * 修改人
     */
    private String changedBy;
    
}
