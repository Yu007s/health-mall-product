package com.drstrong.health.product.dao.area;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.productstore.AreaEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 地区mapper
 * @createTime 2021/12/13 11:14
 * @since TODO
 */
@Mapper
public interface AreaMapper extends BaseMapper<AreaEntity> {
    /**
     * 根据区域id 查询父级的区域id
     * @param areaId 区域id
     * @return 查询所得区域
     */
    List<AreaEntity> queryFatherAreaById(Long areaId);

    /**
     * 查询所有省级 国家信息
     * @return 所有省级 国家信息
     */
    List<AreaEntity> queryProvinceAndCountry();
    
}
