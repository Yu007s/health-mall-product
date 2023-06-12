package com.drstrong.health.product.remote.cms;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.drstrong.health.product.constants.CommonConstant;
import com.drstrong.health.product.model.dto.CommAttributeDTO;
import com.drstrong.health.product.model.response.cms.DictVO;
import com.drstrong.health.product.model.response.cms.SkuProhibitAreaVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.model.DictResponse;
import com.drstrong.health.user.vo.SimpleUcUserVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
	private static final String CODE = "0";

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

	/**
	 * 根据入参获取省市区信息
	 *
	 * @author liuqiuyi
	 * @date 2023/6/10 16:32
	 */
	public List<SkuProhibitAreaVO> querySkuProhibitAreaByIds(List<Long> areaIds) {
		List<SkuProhibitAreaVO> skuProhibitAreaVOList = listSkuProhibitArea();
		if (CollectionUtils.isEmpty(areaIds) || CollectionUtils.isEmpty(skuProhibitAreaVOList)) {
			return Lists.newArrayList();
		}
		return skuProhibitAreaVOList.stream().filter(skuProhibitAreaVO -> areaIds.contains(skuProhibitAreaVO.getId())).collect(Collectors.toList());
	}

	/**
	 * 获取省市区区域信息
	 *
	 * @author liuqiuyi
	 * @date 2023/6/10 16:29
	 */
	public List<SkuProhibitAreaVO> listSkuProhibitArea() {
		try {
			log.info("invoke cmsFeignClient.list param:{}", 1);
			ResultVO<List<SkuProhibitAreaVO>> resultVO = cmsFeignClient.list(1);
			if (Objects.isNull(resultVO) || !resultVO.isSuccess()) {
				log.error("invoke cmsFeignClient.list return failed. result:{}", JSONUtil.toJsonStr(resultVO));
				return Lists.newArrayList();
			}
			return resultVO.getData();
		} catch (Exception e) {
			log.error("invoke cmsFeignClient.list an error occurred,param:{}", 1, e);
			return Lists.newArrayList();
		}
	}

	/**
	 * 根据 cms id 获取名称
	 *
	 * @author liuqiuyi
	 * @date 2023/6/10 16:29
	 */
	public List<SimpleUcUserVO> queryByIds(List<Long> idList) {
		if (CollectionUtils.isEmpty(idList)) {
			log.info("invoke cmsFeignClient.simpleInfoByIdList param is null");
			return Lists.newArrayList();
		}
		try {
			log.info("invoke cmsFeignClient.simpleInfoByIdList param:{}", idList);
			ResultVO<List<SimpleUcUserVO>> resultVO = cmsFeignClient.simpleInfoByIdList(idList);
			if (Objects.isNull(resultVO) || !Objects.equals(resultVO.getCode(), CODE)) {
				log.error("invoke cmsFeignClient.simpleInfoByIdList return failed. result:{}", JSONUtil.toJsonStr(resultVO));
				return Lists.newArrayList();
			}
			return resultVO.getData();
		} catch (Exception e) {
			log.error("invoke cmsFeignClient.simpleInfoByIdList an error occurred,param:{}", idList, e);
			return Lists.newArrayList();
		}
	}
}
