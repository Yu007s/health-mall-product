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

    List<AreaEntity> queryFatherAreaById(Long areaId);

}
