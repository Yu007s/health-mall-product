package com.drstrong.health.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.drstrong.health.product.SpringBootTests;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.remote.pro.ShopCartRemoteProService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2022/1/10 15:38
 */
public class ShopCartRemoteProServiceTest extends SpringBootTests {
	@Resource
	ShopCartRemoteProService shopCartRemoteProService;

	@Resource
	ChineseSkuInfoService chineseSkuInfoService;


	@Test
	public void checkHasProduct() {
		Boolean aBoolean = shopCartRemoteProService.checkHasProduct(6007951L);
		System.out.println(aBoolean);
	}

	@Test
	public void test1(){
		List<Long> storeIds = Lists.newArrayList(1L);
		// 将商品修改为上架状态
		ChineseSkuInfoEntity chineseSkuInfoEntity = ChineseSkuInfoEntity.builder().build();
		chineseSkuInfoEntity.setSkuStatus(ProductStateEnum.HAS_PUT.getCode());
		chineseSkuInfoEntity.setChangedAt(LocalDateTime.now());
		LambdaQueryWrapper<ChineseSkuInfoEntity> updateWrapper = Wrappers.<ChineseSkuInfoEntity>lambdaQuery().in(ChineseSkuInfoEntity::getStoreId, storeIds);
		chineseSkuInfoService.update(chineseSkuInfoEntity, updateWrapper);

		System.out.println("1");
	}
}
