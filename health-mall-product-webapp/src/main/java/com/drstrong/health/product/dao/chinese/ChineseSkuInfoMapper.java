package com.drstrong.health.product.dao.chinese;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import cn.strong.mybatis.plus.extend.CustomBaseMapper;
import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 中药 sku 信息表 Mapper 接口
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
@Mapper
public interface ChineseSkuInfoMapper extends CustomBaseMapper<ChineseSkuInfoEntity> {
    /**
     * 根据查询条件，分页查询 sku 信息
     *
     * @author liuqiuyi
     * @date 2022/8/1 14:19
     */
    Page<ChineseSkuInfoEntity> pageQuerySkuByRequest(Page<ChineseSkuInfoEntity> page, @Param("queryParam") ChineseManagerSkuRequest queryParam);
}
