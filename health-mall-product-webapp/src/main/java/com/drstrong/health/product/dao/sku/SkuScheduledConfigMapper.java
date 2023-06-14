package com.drstrong.health.product.dao.sku;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.sku.SkuScheduledConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/6/14 16:19
 */
@Mapper
public interface SkuScheduledConfigMapper extends BaseMapper<SkuScheduledConfigEntity> {

	void batchUpdateScheduledStatusByCodes(@Param("skuCodeList") Set<String> skuCodeList, @Param("scheduledStatus") Integer scheduledStatus, @Param("operatorId") Long operatorId);
}
