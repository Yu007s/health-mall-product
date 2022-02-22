package com.drstrong.health.product.service.area.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.dao.area.AreaMapper;
import com.drstrong.health.product.model.entity.store.AreaEntity;
import com.drstrong.health.product.model.response.area.AreaInfoResponse;
import com.drstrong.health.product.service.area.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 地区service
 * @createTime 2021/12/13 11:25
 */
@Service
@Slf4j
public class AreaServiceImpl implements AreaService {


    @Resource
    private AreaMapper areaMapper;

    @Override
    public List<AreaInfoResponse> queryAllProvince() {
        LambdaQueryWrapper<AreaEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AreaEntity::getAvailable,1)
                .eq(AreaEntity::getType,1);
        List<AreaEntity> areaEntities = areaMapper.selectList(queryWrapper);
        List<AreaInfoResponse> areaInfoResponses = areaEntities.stream().map(a -> {
            AreaInfoResponse areaInfoResponse = new AreaInfoResponse();
            areaInfoResponse.setAreaId(a.getId());
            areaInfoResponse.setAreaName(a.getName());
            return areaInfoResponse;
        }).collect(Collectors.toList());
        return areaInfoResponses;
    }
}