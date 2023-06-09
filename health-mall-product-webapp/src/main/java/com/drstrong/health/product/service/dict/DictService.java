package com.drstrong.health.product.service.dict;

import com.drstrong.health.product.model.entity.dict.DictEntity;

import java.util.List;

/**
 * huangpeng
 * 2023/6/9 14:25
 */
public interface DictService {

    List<DictEntity> getDictByType(String dictType);

}
