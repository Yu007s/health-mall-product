package com.drstrong.health.product.model.entity.sku;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.handler.mybatis.RecommendDetailInfoTypeHandler;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/8 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "pms_store_sku_recommend", autoResultMap = true)
public class StoreSkuRecommendEntity extends BaseStandardEntity implements Serializable {
    private static final long serialVersionUID = -5946977622740078345L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * sku编码 商品-p开头，药品-m开头，中药-z开头，协定方-x开头
     */
    private String skuCode;

    /**
     * 推荐的详细信息(包含搜索关键字和关键字的拼音)，json存储
     */
    @TableField(value = "recommend_detail_info", typeHandler = RecommendDetailInfoTypeHandler.class)
    private List<RecommendDetailInfoEntity> recommendDetailInfoList;

    /**
     * 虚拟列，存储所有的拼音信息
     */
    private List<String> recommendDetailInfoPinyinArray;

    /**
     * 虚拟列，存储所有的搜索关键字信息
     */
    private List<String> recommendDetailInfoKeywordArray;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class RecommendDetailInfoEntity implements Serializable {
        private static final long serialVersionUID = -6565240040631179495L;

        /**
         * 拼音简写
         */
        private String pinyin;

        /**
         * 搜索关键字
         */
        private String keyword;
    }

    public static StoreSkuRecommendEntity buildDefault(Long operatorId) {
        StoreSkuRecommendEntity storeSkuRecommendEntity = new StoreSkuRecommendEntity();
        storeSkuRecommendEntity.setRecommendDetailInfoList(new ArrayList<>());
        storeSkuRecommendEntity.setVersion(1);
        storeSkuRecommendEntity.setDelFlag(DelFlagEnum.UN_DELETED.getCode());
        storeSkuRecommendEntity.setCreatedBy(operatorId);
        storeSkuRecommendEntity.setChangedAt(LocalDateTime.now());
        storeSkuRecommendEntity.setChangedAt(LocalDateTime.now());
        storeSkuRecommendEntity.setChangedBy(operatorId);
        return storeSkuRecommendEntity;
    }
}
