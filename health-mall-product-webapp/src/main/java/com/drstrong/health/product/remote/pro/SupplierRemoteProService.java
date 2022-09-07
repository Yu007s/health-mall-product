package com.drstrong.health.product.remote.pro;

import com.alibaba.fastjson.JSON;
import com.drstrong.health.ware.model.response.SupplierInfoDTO;
import com.drstrong.health.ware.model.result.ResultVO;
import com.drstrong.health.ware.remote.api.SupplierManageRemoteApi;
import com.drstrong.health.ware.remote.api.SupplierSkuRemoteApi;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author liuqiuyi
 * @date 2022/8/5 11:25
 */
@Slf4j
@Service
public class SupplierRemoteProService {
	@Resource
	SupplierManageRemoteApi supplierManageRemoteApi;

	@Resource
	SupplierSkuRemoteApi supplierSkuRemoteApi;

	/**
	 * 根据供应商 id 获取供应商信息,并转换成 map
	 *
	 * @author liuqiuyi
	 * @date 2022/8/5 13:52
	 */
	public Map<Long, String> getSupplierNameToMap(List<Long> supplierIds) {
		List<SupplierInfoDTO> supplierInfoList = getSupplierNameByIds(supplierIds);
		return Optional.ofNullable(supplierInfoList).orElse(Lists.newArrayList()).stream()
				.collect(Collectors.toMap(SupplierInfoDTO::getSupplierId, SupplierInfoDTO::getSupplierName, (v1, v2) -> v1));
	}

	/**
	 * 根据供应商 id 获取供应商信息
	 *
	 * @author liuqiuyi
	 * @date 2022/8/5 13:52
	 */
	public List<SupplierInfoDTO> getSupplierNameByIds(List<Long> supplierIds) {
		if (CollectionUtils.isEmpty(supplierIds)) {
			return Lists.newArrayList();
		}
		log.info("invoke supplierManageRemoteApi.queryBySupplierIds() param:{}", JSON.toJSONString(supplierIds));
		try {
			ResultVO<List<SupplierInfoDTO>> listResult = supplierManageRemoteApi.queryBySupplierIds(supplierIds);
			if (Objects.isNull(listResult) || !listResult.isSuccess()) {
				log.error("invoke supplierManageRemoteApi.queryBySupplierIds() result is error! result:{}", JSON.toJSONString(listResult));
				return Lists.newArrayList();
			}
			return listResult.getData();
		} catch (Exception e) {
			log.error("invoke supplierManageRemoteApi.queryBySupplierIds() an error occurred!", e);
			return Lists.newArrayList();
		}
	}

	/**
	 * 根据供应商 id 获取供应商信息
	 *
	 * @author liuqiuyi
	 * @date 2022/8/5 13:52
	 */
	public List<SupplierInfoDTO> searchSupplierByCode(String medicineCode) {
		if (StringUtils.isBlank(medicineCode)) {
			return Lists.newArrayList();
		}
		log.info("invoke supplierSkuRemoteApi.searchSupplierByDicCode() param:{}", medicineCode);
		try {
			ResultVO<List<SupplierInfoDTO>> listResultVO = supplierSkuRemoteApi.searchSupplierByDicCode(medicineCode);
			if (Objects.isNull(listResultVO) || !listResultVO.isSuccess()) {
				log.error("invoke supplierSkuRemoteApi.searchSupplierByDicCode() result is error! result:{}", JSON.toJSONString(listResultVO));
				return Lists.newArrayList();
			}
			return listResultVO.getData();
		} catch (Exception e) {
			log.error("invoke supplierSkuRemoteApi.searchSupplierByDicCode() an error occurred!", e);
			return Lists.newArrayList();
		}
	}
}
