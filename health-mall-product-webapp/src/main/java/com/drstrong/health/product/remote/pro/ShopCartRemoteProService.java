package com.drstrong.health.product.remote.pro;

import com.drstrong.health.order.remote.api.cart.ShopCartRemoteApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 购物车远程接口
 *
 * @author liuqiuyi
 * @date 2021/12/29 16:57
 */
@Service
@Slf4j
public class ShopCartRemoteProService {
	@Resource
	ShopCartRemoteApi shopCartRemoteApi;


	/**
	 * 判断购物车中是否存在未购买商品
	 *
	 * @param userId 用户 id
	 * @return 是否有未购买商品
	 * @author liuqiuyi
	 * @date 2021/12/22 20:53
	 */
	public Boolean checkHasProduct(Long userId) {
		try {
			log.info("invoke shopCartRemoteApi.checkHasProduct param:{}", userId);
			Boolean hasProduct = shopCartRemoteApi.checkHasProduct(userId);
			log.info("invoke shopCartRemoteApi.checkHasProduct result:{}", hasProduct);
			if (Objects.isNull(hasProduct)) {
				log.info("invoke shopCartRemoteApi.checkHasProduct return null,param:{}", userId);
				return Boolean.FALSE;
			}
			return hasProduct;
		} catch (Exception e) {
			log.error("invoke shopCartRemoteApi.checkHasProduct an error occurred,param:{}", userId, e);
			throw e;
		}
	}
}
