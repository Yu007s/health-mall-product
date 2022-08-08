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

    List<AreaInfoResponse> queryAllProvince();

    List<ProvinceAreaInfo> queryAll();

    AreaInfoResponse querySingle(Long areaId);

    /**
     * 根据区域id 查询其父级区域id  只会查找市、省、国家级
     * @param areaId 区域id
     * @return  查询所得国家、省、市级区域id
     */
    List<AreaEntity> queryFatherAreaById(Long areaId);

}
