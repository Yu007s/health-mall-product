package com.drstrong.health.product.remote.pro;

import com.alibaba.fastjson.JSON;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.ware.model.request.SkuStockAddOrUpdateRequest;
import com.drstrong.health.ware.model.response.SkuStockResponse;
import com.drstrong.health.ware.model.result.ResultVO;
import com.drstrong.health.ware.remote.api.StockManageRemoteApi;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 库存远程接口的封装类
 *
 * @author liuqiuyi
 * @date 2022/8/6 19:37
 */
@Slf4j
@Service
public class StockRemoteProService {
	@Resource
	StockManageRemoteApi stockManageRemoteApi;

	/**
	 * 根据 skuCode 获取供应商库存设置信息
	 *
	 * @author liuqiuyi
	 * @date 2022/8/6 19:49
	 */
	public List<SkuStockResponse> getSkuInfoBySkuCode(String skuCode) {
		if (StringUtils.isBlank(skuCode)) {
			return Lists.newArrayList();
		}
		log.info("invoke stockManageRemoteApi.get() param:{}", skuCode);
		try {
			ResultVO<List<SkuStockResponse>> listResultVO = stockManageRemoteApi.get(skuCode);
			if (Objects.isNull(listResultVO) || !listResultVO.isSuccess()) {
				log.error("invoke stockManageRemoteApi.get() result is error!, result:{}", JSON.toJSONString(listResultVO));
				return Lists.newArrayList();
			}
			return listResultVO.getData();
		} catch (Exception e) {
			log.error("invoke stockManageRemoteApi.get() an error occurred!", e);
			return Lists.newArrayList();
		}
	}

	/**
	 * 保存或者更新供应商库存设置
	 *
	 * @author liuqiuyi
	 * @date 2022/8/6 20:01
	 */
	public void saveOrUpdateStockInfo(String skuCode, SaveOrUpdateSkuVO saveOrUpdateSkuVO) {
		if (Objects.isNull(saveOrUpdateSkuVO) || StringUtils.isBlank(skuCode)) {
			throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
		}
		// 1.组装参数
		SkuStockAddOrUpdateRequest addOrUpdateRequest = new SkuStockAddOrUpdateRequest();
		addOrUpdateRequest.setStoreId(saveOrUpdateSkuVO.getStoreId());
		addOrUpdateRequest.setSkuCode(skuCode);
		addOrUpdateRequest.setProductDicCode(saveOrUpdateSkuVO.getMedicineCode());

		List<SkuStockResponse> skuStockResponses = Lists.newArrayListWithCapacity(saveOrUpdateSkuVO.getSupplierInfoList().size());
		saveOrUpdateSkuVO.getSupplierInfoList().forEach(supplierInfo -> {
			SkuStockResponse skuStockResponse = new SkuStockResponse();
			BeanUtils.copyProperties(supplierInfo, skuStockResponse);
			skuStockResponses.add(skuStockResponse);
		});
		addOrUpdateRequest.setSkuStockResponses(skuStockResponses);
		// 2.调用远程接口
		log.info("invoke stockManageRemoteApi.saveOrUpdate() param:{}", JSON.toJSONString(addOrUpdateRequest));
		try {
			ResultVO<Object> objectResultVO = stockManageRemoteApi.saveOrUpdate(addOrUpdateRequest, saveOrUpdateSkuVO.getOperatorId());
			if (Objects.isNull(objectResultVO) || !objectResultVO.isSuccess()) {
				log.error("invoke stockManageRemoteApi.saveOrUpdate() result is error!, result:{}", JSON.toJSONString(objectResultVO));
				throw new BusinessException(ErrorEnums.SAVE_IS_NULL);
			}
		} catch (Exception e) {
			log.error("invoke stockManageRemoteApi.saveOrUpdate() an error occurred!", e);
			throw new BusinessException(ErrorEnums.SAVE_IS_NULL);
		}
	}
}
