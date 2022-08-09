package com.drstrong.health.product.service.area.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.dao.area.AreaMapper;
import com.drstrong.health.product.model.entity.productstore.AreaEntity;
import com.drstrong.health.product.model.enums.AreaTypeEnum;
import com.drstrong.health.product.model.response.area.AreaInfoResponse;
import com.drstrong.health.product.model.response.area.ProvinceAreaInfo;
import com.drstrong.health.product.model.response.area.StoreAreaInfo;
import com.drstrong.health.product.service.area.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Override
    public List<ProvinceAreaInfo> queryAll() {
        LambdaQueryWrapper<AreaEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(AreaEntity::getName,AreaEntity::getId,AreaEntity::getParentId,AreaEntity::getType).eq(AreaEntity::getAvailable,1)
                .in(AreaEntity::getType, AreaTypeEnum.COUNTRY.ordinal(),AreaTypeEnum.PROVINCE.ordinal(),AreaTypeEnum.CITY.ordinal()).orderByAsc(AreaEntity::getId);
        //完成所有省市的嵌套
        List<AreaEntity> allAreaEntities = areaMapper.selectList(queryWrapper);
        int chinaId = -1;
        HashMap<Integer,List<AreaEntity>> areaEntityHashMap = new HashMap<>(32);
        for (AreaEntity areaEntity : allAreaEntities) {
            if ( AreaTypeEnum.COUNTRY.ordinal() == areaEntity.getType() ) {
                chinaId = Math.toIntExact(areaEntity.getId());
            }
            Integer parentId = areaEntity.getParentId();
            List<AreaEntity> areaEntities = areaEntityHashMap.computeIfAbsent(parentId, k -> new ArrayList<>(16));
            areaEntities.add(areaEntity);
        }
        List<ProvinceAreaInfo> ans = new ArrayList<>(32);
        List<AreaEntity> provinces = areaEntityHashMap.get(chinaId);
        for (AreaEntity province : provinces) {
            int id = Math.toIntExact(province.getId());
            List<AreaEntity> areaEntities = areaEntityHashMap.get(id);
            List<StoreAreaInfo> collect = areaEntities.stream().map(areaEntity -> {
                StoreAreaInfo storeAreaInfo = new StoreAreaInfo();
                storeAreaInfo.setValue(areaEntity.getId().toString());
                storeAreaInfo.setLabel(areaEntity.getName());
                return storeAreaInfo;
            }).collect(Collectors.toList());
            ProvinceAreaInfo provinceAreaInfo = new ProvinceAreaInfo();
            provinceAreaInfo.setValue(province.getId().toString());
            provinceAreaInfo.setLabel(province.getName());
            provinceAreaInfo.setChildren(collect);
            ans.add(provinceAreaInfo);
        }
        return ans;
    }



    @Override
    public List<AreaEntity> queryFatherAreaById(Long areaId) {
        LambdaQueryWrapper<AreaEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AreaEntity::getId, areaId).eq(AreaEntity::getAvailable, 1);
        AreaEntity areaEntity = areaMapper.selectOne(lambdaQueryWrapper);
        Long queryId = areaId;
        //如果是区级id  则转换为市级查询  否则直接查询
        if (areaEntity.getType() == AreaTypeEnum.DISTRICT.ordinal()) {
            queryId = (long)areaEntity.getParentId();
        }
        return areaMapper.queryFatherAreaById(queryId);
    }


}