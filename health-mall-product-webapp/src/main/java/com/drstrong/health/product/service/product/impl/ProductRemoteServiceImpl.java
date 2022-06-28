package com.drstrong.health.product.service.product.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.drstrong.health.product.constants.CommonConstant;
import com.drstrong.health.product.dao.product.SkuMapper;
import com.drstrong.health.product.model.dto.CommAttributeDTO;
import com.drstrong.health.product.model.entity.product.*;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.response.product.ProductPropertyVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.cms.CmsRemoteProService;
import com.drstrong.health.product.remote.model.*;
import com.drstrong.health.product.remote.model.request.QueryProductRequest;
import com.drstrong.health.product.remote.pro.PharmacyGoodsRemoteProService;
import com.drstrong.health.product.remote.vo.BsUserInfoVO;
import com.drstrong.health.product.remote.vo.SkuVO;
import com.drstrong.health.product.service.category.BackCategoryService;
import com.drstrong.health.product.service.product.*;
import com.drstrong.health.product.service.store.StoreThreeRelevanceService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.naiterui.common.redis.template.IRedisSetTemplate;
import com.naiterui.ehp.bp.bo.b2c.cms.CmsSkuBO;
import com.naiterui.ehp.bp.bo.b2c.cms.ProductBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * 商品远程接口实现类
 *
 * @author liuqiuyi
 * @date 2021/12/22 15:04
 */
@Slf4j
@Service
public class ProductRemoteServiceImpl implements ProductRemoteService {
	@Resource
	ProductSkuService productSkuService;

	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	@Resource
	PharmacyGoodsRemoteProService pharmacyGoodsRemoteProService;

	@Resource
	CmsRemoteProService cmsRemoteProService;

	@Resource
	ProductExtendService productExtendService;

	@Resource
	ProductAttributeService productAttributeService;

	@Resource
	StoreThreeRelevanceService storeThreeRelevanceService;

	@Resource
	ProductSkuRevenueService productSkuRevenueService;

	@Resource
	BackCategoryService backCategoryService;

	@Resource
	private SkuMapper skuMapper;

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	@Resource
	private IRedisSetTemplate redisSetTemplate;
	//中药处方
	public static final int RECOM_TYPE_CHINESE = 2;
	/**
	 * 根据 skuId 查询商品 sku 信息集合
	 *
	 * @param queryProductRequest 请求参数
	 * @return 商品 sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/22 15:03
	 */
	@Override
	public List<ProductSkuInfoDTO> getSkuInfoBySkuIds(QueryProductRequest queryProductRequest) {
		log.info("ProductRemoteServiceImpl.getSkuInfoBySkuIds param:{}", queryProductRequest);
		List<ProductSkuEntity> productSkuEntityList = getSkuList(queryProductRequest);
		if (CollectionUtils.isEmpty(productSkuEntityList)) {
			return Lists.newArrayList();
		}
		return buildProductSkuInfoDTOList(productSkuEntityList);
	}

	/**
	 * 根据 skuId 集合,获取 sku 信息(包含已删除的数据)
	 * <p> 包含 delFlag 为 1 的数据 </>
	 *
	 * @param queryProductRequest 查询参数
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2022/1/10 16:54
	 */
	@Override
	public List<ProductSkuInfoDTO> getSkuInfoBySkuIdsContainDel(QueryProductRequest queryProductRequest) {
		List<ProductSkuEntity> productSkuEntityList = productSkuService.queryBySkuIdOrCodeContainDel(queryProductRequest.getSkuIdList(), queryProductRequest.getSkuCodeList(), queryProductRequest.getUpOffStatus());
		if (CollectionUtils.isEmpty(productSkuEntityList)) {
			return Lists.newArrayList();
		}
		return buildProductSkuInfoDTOList(productSkuEntityList);
	}

	private List<ProductSkuEntity> getSkuList(QueryProductRequest queryProductRequest) {
		if (Objects.isNull(queryProductRequest) || (CollectionUtils.isEmpty(queryProductRequest.getSkuIdList()) && CollectionUtils.isEmpty(queryProductRequest.getSkuCodeList()))) {
			return Lists.newArrayList();
		}
		// 1.查询 sku 信息是否存在
		return productSkuService.queryBySkuIdOrCode(queryProductRequest.getSkuIdList(), queryProductRequest.getSkuCodeList(), UpOffEnum.getEnumByCode(queryProductRequest.getUpOffStatus()));
	}

	private List<ProductSkuInfoDTO> buildProductSkuInfoDTOList(List<ProductSkuEntity> productSkuEntityList) {
		// 2.查询商品信息
		Set<Long> productIdList = Sets.newHashSetWithExpectedSize(productSkuEntityList.size());
		Set<Long> skuIds = Sets.newHashSetWithExpectedSize(productSkuEntityList.size());
		productSkuEntityList.forEach(productSkuEntity -> {
			productIdList.add(productSkuEntity.getProductId());
			skuIds.add(productSkuEntity.getId());
		});
		QuerySpuRequest querySpuRequest = new QuerySpuRequest();
		querySpuRequest.setProductIdList(productIdList);
		Map<Long, ProductBasicsInfoEntity> productIdInfoMap = productBasicsInfoService.queryProductByParamToMap(querySpuRequest);
		// 3.查询库存信息
		Map<Long, Integer> skuIdStockNumMap = pharmacyGoodsRemoteProService.getSkuStockNumToMap(skuIds);
		// 4.查询商品属性,从 cms 接口中获取
		Map<Integer, CommAttributeDTO> commAttributeByIdListToMap = cmsRemoteProService.getCommAttributeByIdListToMap();
		// 5.查询三方进货价
		Map<Long, BigDecimal> threadPriceSkuIdMap = storeThreeRelevanceService.getThreadPriceBySkuIdsToMap(skuIds);
		// 6.组装返回值
		return buildSkuInfoResult(productSkuEntityList, productIdInfoMap, skuIdStockNumMap, commAttributeByIdListToMap, threadPriceSkuIdMap);
	}

	/**
	 * 分页查询搜索的内容,只返回商品名称
	 *
	 * @param content 搜索条件
	 * @param count   返回的个数
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/17 15:49
	 */
	@Override
	public List<SearchNameResultDTO> searchSpuNameByName(String content, Integer count) {
		log.info("invoke ProductRemoteServiceImpl.searchSpuNameByName param:{},{}", content, count);
		List<ProductBasicsInfoEntity> infoEntityList = productBasicsInfoService.searchTitleAndBrandName(content, count);
		if (CollectionUtils.isEmpty(infoEntityList)) {
			log.info("invoke ProductRemoteServiceImpl.searchSpuNameByName result is null, param:{},{}", content, count);
			return Lists.newArrayList();
		}
		List<SearchNameResultDTO> resultDTOList = Lists.newArrayListWithCapacity(infoEntityList.size());
		for (ProductBasicsInfoEntity infoEntity : infoEntityList) {
			SearchNameResultDTO resultDTO = new SearchNameResultDTO();
			// 和之前的老业务保持一致
			resultDTO.setName(infoEntity.getBrandName() + CommonConstant.SPACE + infoEntity.getTitle());
			resultDTO.setCommonName(infoEntity.getTitle());
			resultDTOList.add(resultDTO);
		}
		return resultDTOList;
	}

	/**
	 * 根据名称模糊查询 sku 信息
	 *
	 * @param content 商品 sku 名称
	 * @return sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/23 21:12
	 */
	@Override
	public List<ProductSkuInfoDTO> searchSkuDetail(String content) {
		log.info("invoke ProductRemoteServiceImpl.searchSkuDetail param:{}", content);
		if (StringUtils.isBlank(content)) {
			return Lists.newArrayList();
		}
		// 判断是否含有空格,如果有空格,需要进行截取(主要是为了和之前空中药房搜索方式保持一致),获取真实的商品名称
		if (content.contains(CommonConstant.SPACE)) {
			int index = content.indexOf(CommonConstant.SPACE) + 1;
			content = content.substring(index);
		}
		QuerySkuRequest querySkuRequest = new QuerySkuRequest();
		querySkuRequest.setProductName(content);
		querySkuRequest.setUpOffEnum(UpOffEnum.UP);
		List<ProductSkuEntity> productSkuEntityList = productSkuService.querySkuByParam(querySkuRequest);
		if (CollectionUtils.isEmpty(productSkuEntityList)) {
			log.info("invoke ProductRemoteServiceImpl.searchSkuDetail search result is null. param:{}", content);
			return Lists.newArrayList();
		}
		return buildProductSkuInfoDTOList(productSkuEntityList);
	}

	/**
	 * 根据后台分类 id 查询商品信息
	 *
	 * @param categoryId 分类 id
	 * @return 商品详细信息
	 * @author liuqiuyi
	 * @date 2021/12/24 13:54
	 */
	@Override
	public List<ProductSkuInfoDTO> getSkuInfoByCategoryId(Long categoryId) {
		log.info("invoke ProductRemoteServiceImpl.getSkuInfoByCategoryId param:{}", categoryId);
		if (Objects.isNull(categoryId)) {
			return Lists.newArrayList();
		}
		// 1.获取后台分类的子分类
		Set<Long> backCategoryIds = backCategoryService.getCategoryIdsByOneId(categoryId);
		// 2.根据分类 id 查询 spu 信息
		QuerySpuRequest querySpuRequest = new QuerySpuRequest();
		querySpuRequest.setBackCategoryIdList(backCategoryIds);
		querySpuRequest.setUpOffEnum(UpOffEnum.UP);
		List<ProductBasicsInfoEntity> basicsInfoEntityList = productBasicsInfoService.queryProductByParam(querySpuRequest);
		if (CollectionUtils.isEmpty(basicsInfoEntityList)) {
			return Lists.newArrayList();
		}
		// 3.获取商品 id,查询 sku 信息
		Set<Long> productIdList = basicsInfoEntityList.stream().map(ProductBasicsInfoEntity::getId).collect(Collectors.toSet());
		QuerySkuRequest querySkuRequest = new QuerySkuRequest();
		querySkuRequest.setProductIdList(productIdList);
		querySkuRequest.setUpOffEnum(UpOffEnum.UP);
		List<ProductSkuEntity> productSkuEntityList = productSkuService.querySkuByParam(querySkuRequest);
		if (CollectionUtils.isEmpty(productSkuEntityList)) {
			log.info("invoke ProductRemoteServiceImpl.getSkuInfoByCategoryId search result is null. param:{}", categoryId);
			return Lists.newArrayList();
		}
		return buildProductSkuInfoDTOList(productSkuEntityList);
	}

	/**
	 * skuCode 和 skuId 进行转换,批量接口
	 * <p> 主要用于两者相互查询,两个入参不能同时为空 </>
	 *
	 * @param queryProductRequest 查询参数
	 * @return sku 编码和 id 信息
	 * @author liuqiuyi
	 * @date 2021/12/24 20:07
	 */
	@Override
	public List<SkuIdAndCodeDTO> listSkuIdOrCode(QueryProductRequest queryProductRequest) {
		log.info("ProductRemoteServiceImpl.getSkuInfoBySkuIds param:{}", queryProductRequest);
		List<ProductSkuEntity> productSkuEntityList = getSkuList(queryProductRequest);
		// 组装返回值
		List<SkuIdAndCodeDTO> skuIdAndCodeDTOList = Lists.newArrayListWithCapacity(productSkuEntityList.size());
		productSkuEntityList.forEach(productSkuEntity -> {
			SkuIdAndCodeDTO skuIdAndCodeDTO = new SkuIdAndCodeDTO();
			skuIdAndCodeDTO.setSkuCode(productSkuEntity.getSkuCode());
			skuIdAndCodeDTO.setSkuId(productSkuEntity.getId());
			skuIdAndCodeDTOList.add(skuIdAndCodeDTO);
		});
		return skuIdAndCodeDTOList;
	}

	/**
	 * 根据 skuId 或者 skuCode 查询商品详情
	 * <p> 目前主要提供给空中药房调用 </>
	 * <p> 两个入参任传其一 </>
	 *
	 * @param skuCode sku 编码
	 * @param skuId   skuId
	 * @return 商品的 sku 详情
	 * @author liuqiuyi
	 * @date 2021/12/24 11:30
	 */
	@Override
	@Transactional(readOnly = true)
	public ProductSkuDetailsDTO getSkuDetail(String skuCode, Long skuId) {
		log.info("invoke ProductRemoteServiceImpl.getSkuDetail param :{},{}", skuCode, skuId);
		if (StringUtils.isBlank(skuCode) && Objects.isNull(skuId)) {
			return null;
		}
		// 1.获取 sku 信息
		ProductSkuEntity productSkuEntity = productSkuService.queryBySkuIdOrCode(skuId, skuCode, UpOffEnum.UP);
		if (Objects.isNull(productSkuEntity)) {
			throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST);
		}
		// 2.查询商品扩展信息
		ProductExtendEntity extendEntity = productExtendService.queryByProductId(productSkuEntity.getProductId());
		// 3.查询商品属性
		List<ProductPropertyVO> productPropertyVOList = productAttributeService.getPropertyByCode(null, productSkuEntity.getProductId());
		// 4.组装返回值
		return buildSkuDetailResult(productSkuEntity, extendEntity, productPropertyVOList);
	}

	/**
	 * 根据 skuId 或者 skuCode 集合查询发票所需相关信息
	 *
	 * @param queryProductRequest 查询入参
	 * @return 发票相关信息
	 * @author liuqiuyi
	 * @date 2022/1/10 10:23
	 */
	@Override
	public List<SkuInvoiceDTO> listInvoiceBySkuIds(QueryProductRequest queryProductRequest) {
		if (org.apache.commons.collections.CollectionUtils.isEmpty(queryProductRequest.getSkuIdList()) && org.apache.commons.collections.CollectionUtils.isEmpty(queryProductRequest.getSkuCodeList())) {
			log.error("invoke ProductRemoteController.listInvoiceBySkuIds param is null");
			return Lists.newArrayList();
		}
		log.info("invoke ProductRemoteController.listInvoiceBySkuIds param :{}", queryProductRequest);
		// 1.查询 sku
		List<ProductSkuEntity> productSkuEntityList = productSkuService.queryBySkuIdOrCode(queryProductRequest.getSkuIdList(), queryProductRequest.getSkuCodeList(), UpOffEnum.getEnumByCode(queryProductRequest.getUpOffStatus()));
		if (CollectionUtils.isEmpty(productSkuEntityList)) {
			log.error("invoke ProductRemoteController.listInvoiceBySkuIds,sku does not exist. param:{}", queryProductRequest);
			return Lists.newArrayList();
		}
		// 2.查询税收编码
		List<ProductSkuRevenueEntity> revenueEntityList = productSkuRevenueService.listSkuRevenue(queryProductRequest.getSkuIdList(), queryProductRequest.getSkuCodeList());
		Map<Long, ProductSkuRevenueEntity> skuIdRevenueMap = revenueEntityList.stream().collect(toMap(ProductSkuRevenueEntity::getSkuId, dto -> dto, (v1, v2) -> v1));
		// 3.组装参数,返回
		List<SkuInvoiceDTO> skuInvoiceDTOList = Lists.newArrayListWithCapacity(productSkuEntityList.size());
		productSkuEntityList.forEach(productSkuEntity -> {
			ProductSkuRevenueEntity skuRevenueEntity = skuIdRevenueMap.getOrDefault(productSkuEntity.getId(), new ProductSkuRevenueEntity());
			SkuInvoiceDTO skuInvoiceDTO = SkuInvoiceDTO.builder().skuId(productSkuEntity.getId()).skuCode(productSkuEntity.getSkuCode())
					.packName(productSkuEntity.getPackName()).packValue(productSkuEntity.getPackValue())
					.revenueCode(skuRevenueEntity.getRevenueCode()).revenueRate(skuRevenueEntity.getRevenueRate()).build();

			skuInvoiceDTOList.add(skuInvoiceDTO);
		});
		return skuInvoiceDTOList;
	}

	@Override
	public Map<Long, String> getskuNumber(Set<Long> skuIds, Integer recomType) {
		Map<Long,String> skuMap = new HashMap<>();
		if (RECOM_TYPE_CHINESE == recomType){
			List<SkuVO> skuVOS = skuMapper.getskuNumber(skuIds);
			skuVOS.stream().forEach(e->{
				skuMap.put(e.getId(),e.getNumber());
			});
		}else {
			List<Sku>  skuS = skuMapper.selectList(Wrappers.<Sku>lambdaQuery().in(Sku::getId, skuIds));
			skuS.stream().forEach(e->{
				skuMap.put(e.getId(),e.getNumber());
			});
		}
		return skuMap;
	}

	@Override
	public void addErpInfo(CmsSkuBO skuVO) {
		if(skuVO.getHistoryId() == null){
			return;
		}
		log.info("保存修改SKU金额添加推送bd记录: sku_{}_{}" , skuVO.getId() , skuVO.getHistoryId());
		//判断是否有金额修改，如果有，查询出所有在职bd，保存redis集合
		List<BsUserInfoVO> bsUserInfoVOS = skuMapper.selectRepresentInfoList(null);
		List<String> erpList = bsUserInfoVOS.stream().map(BsUserInfoVO::getErpId).collect(Collectors.toList());
		String key = "sku_"+skuVO.getId()+"_"+skuVO.getHistoryId();
		erpList.forEach(e->{
			redisSetTemplate.sadd(key,"\""+e.trim()+"\"");
		});
		redisTemplate.expire(key,7, TimeUnit.DAYS);
	}

	@Override
	public List<ProductBO> getProductListByIds(Set<Long> ids) {
		log.info(" 根据spu id 获取spu列表:{}",ids);
		List<ProductBO> products = this.skuMapper.selectSpuList(ids);
		return products;
	}

	private ProductSkuDetailsDTO buildSkuDetailResult(ProductSkuEntity productSkuEntity, ProductExtendEntity extendEntity, List<ProductPropertyVO> productPropertyVOList) {
		ProductSkuDetailsDTO detailsDTO = new ProductSkuDetailsDTO();
		BeanUtils.copyProperties(productSkuEntity, detailsDTO);
		detailsDTO.setSkuId(productSkuEntity.getId());
		detailsDTO.setPrice(BigDecimalUtil.F2Y(productSkuEntity.getSkuPrice().longValue()));
		detailsDTO.setStoreId(productSkuEntity.getSourceId());
		detailsDTO.setStoreName(productSkuEntity.getSourceName());
		detailsDTO.setImageUrlList(Lists.newArrayList(extendEntity.getImageUrl().split(",")));
		detailsDTO.setDetailUrlList(Lists.newArrayList(extendEntity.getDetailUrl().split(",")));

		List<ProductPropertyDTO> propertyVOList = Lists.newArrayListWithCapacity(productPropertyVOList.size());
		for (ProductPropertyVO productPropertyVO : productPropertyVOList) {
			ProductPropertyDTO propertyDTO = new ProductPropertyDTO();
			BeanUtils.copyProperties(productPropertyVO, propertyDTO);
			propertyVOList.add(propertyDTO);
		}
		detailsDTO.setPropertyVOList(propertyVOList);
		return detailsDTO;
	}

	private List<ProductSkuInfoDTO> buildSkuInfoResult(List<ProductSkuEntity> productSkuEntityList, Map<Long, ProductBasicsInfoEntity> productIdInfoMap,
													   Map<Long, Integer> skuIdStockNumMap, Map<Integer, CommAttributeDTO> commAttributeByIdListToMap, Map<Long, BigDecimal> threadPriceSkuIdMap) {

		List<ProductSkuInfoDTO> resultSkuInfoList = Lists.newArrayListWithCapacity(productSkuEntityList.size());
		for (ProductSkuEntity productSkuEntity : productSkuEntityList) {
			ProductBasicsInfoEntity basicsInfoEntity = productIdInfoMap.getOrDefault(productSkuEntity.getProductId(), new ProductBasicsInfoEntity());
			Integer stockNum = skuIdStockNumMap.getOrDefault(productSkuEntity.getId(), 0);
			// 商品属性
			CommAttributeDTO commAttributeDTO = commAttributeByIdListToMap.getOrDefault(productSkuEntity.getCommAttribute(), new CommAttributeDTO());

			ProductSkuInfoDTO infoDTO = new ProductSkuInfoDTO();
			infoDTO.setProductCategoryId(basicsInfoEntity.getCategoryId());
			infoDTO.setProductId(productSkuEntity.getProductId());
			infoDTO.setSpuCode(basicsInfoEntity.getSpuCode());
			infoDTO.setMasterImageUrl(basicsInfoEntity.getMasterImageUrl());
			infoDTO.setBrandName(basicsInfoEntity.getBrandName());
			infoDTO.setProductName(basicsInfoEntity.getTitle());
			infoDTO.setSkuId(productSkuEntity.getId());
			infoDTO.setSkuCode(productSkuEntity.getSkuCode());
			infoDTO.setSkuName(productSkuEntity.getSkuName());
			infoDTO.setPackValue(productSkuEntity.getPackValue());
			infoDTO.setProductAmount(BigDecimalUtil.F2Y(productSkuEntity.getSkuPrice().longValue()));
			infoDTO.setPurchasePrice(threadPriceSkuIdMap.get(productSkuEntity.getId()));
			infoDTO.setStoreId(productSkuEntity.getSourceId());
			infoDTO.setStoreName(productSkuEntity.getSourceName());
			infoDTO.setUpOffStatus(productSkuEntity.getState());
			infoDTO.setStockNum(stockNum);
			infoDTO.setCommAttribute(commAttributeDTO.getCommAttribute());
			infoDTO.setCommAttributeName(commAttributeDTO.getCommAttributeName());
			infoDTO.setCommAttributeIcon(commAttributeDTO.getCommAttributeIcon());
			infoDTO.setDelFlag(productSkuEntity.getDelFlag());
			resultSkuInfoList.add(infoDTO);
		}
		return resultSkuInfoList;
	}
}
