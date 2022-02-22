package com.drstrong.health.product.service;

import com.drstrong.health.product.SpringBootTests;
import com.drstrong.health.product.remote.pro.ShopCartRemoteProService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author liuqiuyi
 * @date 2022/1/10 15:38
 */
public class ShopCartRemoteProServiceTest extends SpringBootTests {
	@Resource
	ShopCartRemoteProService shopCartRemoteProService;

	@Test
	public void checkHasProduct() {
		Boolean aBoolean = shopCartRemoteProService.checkHasProduct(6007951L);
		System.out.println(aBoolean);
	}
}
