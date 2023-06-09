package com.drstrong.health.product.service.dict.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.dao.dict.DictMapper;
import com.drstrong.health.product.enums.DictTypeEnum;
import com.drstrong.health.product.model.entity.dict.DictEntity;
import com.drstrong.health.product.service.dict.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * huangpeng
 * 2023/6/9 14:25
 */
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<DictEntity> getDictByType(String dictType) {
        LambdaQueryWrapper<DictEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictEntity::getDictType, DictTypeEnum.getValueByCode(dictType).getCode());
        List<DictEntity> dictEntities = dictMapper.selectList(queryWrapper);
        return dictEntities;
    }
}
