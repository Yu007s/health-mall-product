package com.drstrong.health.product.service;

import com.drstrong.health.product.model.response.area.AreaInfoResponse;

import java.util.List;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 地区service
 * @createTime 2021/12/13 11:24
 */
public interface AreaService {

    List<AreaInfoResponse> queryAllProvince();
}
