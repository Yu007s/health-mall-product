package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.store.StorePostageAreaEntity;

import java.util.List;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺邮费service
 * @createTime 2021/12/13 11:24
 */
public interface StorePostageAreaService {

   List<StorePostageAreaEntity> queryByStoreId(Long storeId);
   
   void deleteByStoreId(Long storeId,String userId);
}
