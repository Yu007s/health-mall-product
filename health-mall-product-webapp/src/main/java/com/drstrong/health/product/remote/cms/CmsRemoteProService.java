package com.drstrong.health.product.remote.cms;

import com.alibaba.fastjson.JSON;
import com.drstrong.health.product.constants.CommonConstant;
import com.drstrong.health.product.model.dto.CommAttributeDTO;
import com.drstrong.health.product.model.response.cms.DictVO;
import com.drstrong.health.product.remote.model.DictResponse;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

/**
 * cms 远程接口封装
 *
 * @author liuqiuyi
 * @date 2021/12/22 17:08
 */
@Slf4j
@Service
public class CmsRemoteProService {
	private static final String SUCCESS = "success";

	@Resource
	ICMSFeignClient cmsFeignClient;

	/**
	 * 获取商品属性,转成 map
	 *
	 * @author liuqiuyi
	 * @date 2021/12/22 17:40
	 */
	public Map<Integer, CommAttributeDTO> getCommAttributeByIdListToMap() {
		String dictCommAttribute = CommonConstant.DICT_COMM_ATTRIBUTE;
		try {
			log.info("invoke ICMSFeignClient.vueQuery param:{}", dictCommAttribute);
			DictResponse<List<DictVO>> listResponseEntity = cmsFeignClient.vueQuery(dictCommAttribute);
			log.info("invoke ICMSFeignClient.vueQuery result:{}", listResponseEntity);
			if (Objects.isNull(listResponseEntity) || !Objects.equals(SUCCESS, listResponseEntity.getMsg())) {
				log.info("invoke ICMSFeignClient.vueQuery return failed. param:{}", dictCommAttribute);
				return Maps.newHashMap();
			}
			List<DictVO> dictVOList = listResponseEntity.getData();
			if (CollectionUtils.isEmpty(dictVOList)) {
				log.info("invoke ICMSFeignClient.vueQuery return null. param:{},result:{}", dictCommAttribute, JSON.toJSONString(listResponseEntity));
				return Maps.newHashMap();
			}
			return dictVOList.stream().collect(toMap(vo -> Integer.valueOf(vo.getCode()), vo -> {
				CommAttributeDTO commAttributeDTO = new CommAttributeDTO();
				commAttributeDTO.setCommAttribute(Integer.valueOf(vo.getCode()));
				commAttributeDTO.setCommAttributeName(vo.getValue());
				commAttributeDTO.setCommAttributeIcon(vo.getUrl());
				return commAttributeDTO;
			}, (o, n) -> o));
		} catch (Exception e) {
			log.error("invoke ICMSFeignClient.vueQuery an error occurred,param:{}", dictCommAttribute, e);
			throw e;
		}
	}
}
