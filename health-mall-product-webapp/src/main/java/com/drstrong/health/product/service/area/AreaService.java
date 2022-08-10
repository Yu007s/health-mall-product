package com.drstrong.health.product.service.area;

import com.drstrong.health.product.model.entity.productstore.AreaEntity;
import com.drstrong.health.product.model.response.area.AreaInfoResponse;
import com.drstrong.health.product.model.response.area.ProvinceAreaInfo;

import java.util.List;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 地区service
 * @createTime 2021/12/13 11:24
 */
public interface AreaService {

    /**
     * 查询所有省份信息
     * @return 所有省份信息
     */

    List<AreaInfoResponse> queryAllProvince();

    /**
     * 查询所有省级区域以及下属市的信息
     * @return  省级区域列表
     */
    List<ProvinceAreaInfo> queryAll();

    /**
     * 根据区域id 查询其父级区域和自身、全国
     * @param areaId 区域id
     * @return  查询所得区域信息
     */
    List<AreaEntity> queryFatherAreaById(Long areaId);

    /**
     * 根据区域id  返回省id
     * @param areaId 区域id
     * @return  省份信息
     */
    AreaInfoResponse queryProvinceByAreaId(Long areaId);

    /**
     * 查询所有的省份信息以及国家id
     * 国家id在列表最后
     * @return 查询所得
     */

    List<AreaInfoResponse> queryProvinceAndCountry();

}
