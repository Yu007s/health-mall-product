package com.drstrong.health.product.facade.store.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.product.facade.store.StoreBusinessFacade;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.request.store.AgencyStoreVO;
import com.drstrong.health.product.model.request.store.QueryStoreRequest;
import com.drstrong.health.product.service.store.StoreService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuqiuyi
 * @date 2023/7/14 10:06
 */
@Slf4j
@Service
public class StoreBusinessFacadeImpl implements StoreBusinessFacade {

    @Resource
    StoreService storeService;

    /**
     * 根据条件查询店铺信息
     *
     * @param queryStoreRequest
     * @author liuqiuyi
     * @date 2023/7/14 10:07
     */
    @Override
    public List<AgencyStoreVO> queryStoreByParam(QueryStoreRequest queryStoreRequest) {
        log.info("invoke queryStoreByParam() param:{}", JSONUtil.toJsonStr(queryStoreRequest));
        // 1.查询数据库
        List<StoreEntity> storeEntityList = storeService.queryStoreByParam(queryStoreRequest);
        if (CollectionUtil.isEmpty(storeEntityList)) {
            log.info("未查询到任何店铺信息，请求的入参为：{}", JSONUtil.toJsonStr(queryStoreRequest));
            return Lists.newArrayList();
        }
        // 2.组装返回值
        return storeEntityList.stream().map(storeEntity -> {
            AgencyStoreVO agencyStoreVO = BeanUtil.copyProperties(storeEntity, AgencyStoreVO.class);
            agencyStoreVO.setStoreId(storeEntity.getId());
            return agencyStoreVO;
        }).collect(Collectors.toList());
    }
}
