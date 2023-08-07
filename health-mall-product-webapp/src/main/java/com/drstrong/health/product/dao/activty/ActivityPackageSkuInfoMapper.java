package com.drstrong.health.product.dao.activty;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.activty.ActivityPackageSkuInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * huangpeng
 * 2023/7/7 13:53
 */
@Mapper
public interface ActivityPackageSkuInfoMapper extends BaseMapper<ActivityPackageSkuInfoEntity> {
}
