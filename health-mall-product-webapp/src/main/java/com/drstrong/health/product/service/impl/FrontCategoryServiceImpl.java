package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.dao.FrontCategoryMapper;
import com.drstrong.health.product.model.BaseTree;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.category.CategoryRelationEntity;
import com.drstrong.health.product.model.entity.category.FrontCategoryEntity;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.LevelEnum;
import com.drstrong.health.product.model.request.category.AddOrUpdateFrontCategoryRequest;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.request.category.PageCategoryIdRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.category.CategoryProductVO;
import com.drstrong.health.product.model.response.category.FrontCategoryVO;
import com.drstrong.health.product.model.response.category.HomeCategoryVO;
import com.drstrong.health.product.model.response.product.ProductSpuVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.BackCategoryService;
import com.drstrong.health.product.service.CategoryRelationService;
import com.drstrong.health.product.service.FrontCategoryService;
import com.drstrong.health.product.service.ProductBasicsInfoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 前台分类 service
 *
 * @author liuqiuyi
 * @date 2021/12/7 20:49
 */
@Service
@Slf4j
public class FrontCategoryServiceImpl implements FrontCategoryService {

	/**
	 * 一级分类展示的条数
	 */
	public static final int FIRST_CATEGORY_SIZE = 7;

	private static final String ALL_CATEGORY = "全部分类";
	/**
	 * 全部分类的 icon 地址
	 */
	private static final String ALL_CATEGORY_ICON = "";

	@Resource
	FrontCategoryMapper frontCategoryMapper;

	@Resource
	CategoryRelationService categoryRelationService;

	@Resource
	BackCategoryService backCategoryService;

	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	/**
	 * 查询所有的前台分类,并组装树形结构
	 *
	 * @param categoryQueryRequest 查询的参数信息
	 * @return 前台分类的集合
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	@Override
	public List<FrontCategoryVO> queryByParamToTree(CategoryQueryRequest categoryQueryRequest) {
		// 1.获取所有的前台分类
		List<FrontCategoryEntity> frontCategoryEntityList = frontCategoryMapper.queryByParam(categoryQueryRequest);
		if (CollectionUtils.isEmpty(frontCategoryEntityList)) {
			return Lists.newArrayList();
		}
		// 组装返回值的树形结构
		List<FrontCategoryVO> frontCategoryVOList = buildResponse(frontCategoryEntityList);
		// 2.获取前台分类关联的后台分类
		Set<Long> frontCategoryIdList = frontCategoryEntityList.stream().map(FrontCategoryEntity::getId).collect(Collectors.toSet());
		Map<Long, List<Long>> frontIdBackIdMap = categoryRelationService.getFrontAndBackCategoryToMap(frontCategoryIdList);
		if (CollectionUtils.isEmpty(frontIdBackIdMap)) {
			return frontCategoryVOList;
		}
		// 3.获取关联的后台分类信息
		Set<Long> backCategoryIdList = frontIdBackIdMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
		List<BackCategoryEntity> backCategoryEntityList = backCategoryService.queryByIdList(backCategoryIdList);
		// 4.组装后台分类的商品数量
		Map<Long, Integer> backIdProductNumMap = BackCategoryEntity.buildCategoryProductCount(backCategoryEntityList, true);
		// 5.组装前台分类对应的商品数量
		Map<Long, Integer> frontIdProductCountMap = buildFrontCategoryProductNumMap(frontIdBackIdMap, backIdProductNumMap);
		// 6.设置返回值
		frontCategoryVOList.forEach(frontCategoryVO -> buildResponseProductNum(frontCategoryVO, frontIdProductCountMap, frontIdBackIdMap));
		return frontCategoryVOList;
	}

	/**
	 * 根据分类 id 集合查询分类信息
	 *
	 * @param categoryIdList 分类 id 集合
	 * @return 前台分类集合信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:41
	 */
	@Override
	public List<FrontCategoryEntity> queryByIdList(Set<Long> categoryIdList) {
		if (CollectionUtils.isEmpty(categoryIdList)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<FrontCategoryEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(FrontCategoryEntity::getId, categoryIdList).eq(FrontCategoryEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return frontCategoryMapper.selectList(wrapper);
	}

	/**
	 * 根据分类 id 查询分类信息
	 *
	 * @param categoryId 分类 id
	 * @return 前台分类信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:41
	 */
	@Override
	public FrontCategoryEntity queryById(Long categoryId) {
		if (Objects.isNull(categoryId)) {
			return null;
		}
		LambdaQueryWrapper<FrontCategoryEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(FrontCategoryEntity::getId, categoryId).eq(FrontCategoryEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return frontCategoryMapper.selectOne(wrapper);
	}


	/**
	 * 添加前台分类
	 *
	 * @param categoryRequest 入参信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:19
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void add(AddOrUpdateFrontCategoryRequest categoryRequest) {
		FrontCategoryEntity parentCategoryEntity = null;
		if (!Objects.isNull(categoryRequest.getParentId()) && !Objects.equals(0L, categoryRequest.getParentId())) {
			// 添加的不是一级分类,校验上级分类是否存在
			parentCategoryEntity = queryById(categoryRequest.getParentId());
			if (Objects.isNull(parentCategoryEntity)) {
				throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
			}
		}
		// 如果关联的后台 id 不为空,进行校验
		if (!CollectionUtils.isEmpty(categoryRequest.getBackCategoryIdList())) {
			List<BackCategoryEntity> backCategoryEntityList = backCategoryService.queryByIdList(categoryRequest.getBackCategoryIdList());
			if (CollectionUtils.isEmpty(backCategoryEntityList) || !Objects.equals(backCategoryEntityList.size(), categoryRequest.getBackCategoryIdList().size())) {
				throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
			}
		}
		// 1.保存前台分类信息
		FrontCategoryEntity categoryEntity = new FrontCategoryEntity();
		categoryEntity.setName(categoryRequest.getCategoryName());
		categoryEntity.setIcon(categoryRequest.getIconUrl());
		categoryEntity.setLevel(Objects.isNull(parentCategoryEntity) ? 1 : parentCategoryEntity.getLevel() + 1);
		categoryEntity.setParentId(Objects.isNull(parentCategoryEntity) ? 0 : parentCategoryEntity.getId());
		categoryEntity.setSort(categoryRequest.getSort());
		categoryEntity.setCreatedBy(categoryRequest.getUserId());
		categoryEntity.setChangedBy(categoryRequest.getUserId());
		categoryEntity.setState(0);
		try {
			frontCategoryMapper.insert(categoryEntity);
		} catch (DuplicateKeyException e) {
			throw new BusinessException(ErrorEnums.CATEGORY_NAME_IS_EXIST);
		}
		if (CollectionUtils.isEmpty(categoryRequest.getBackCategoryIdList())) {
			return;
		}
		// 2.保存前后台分类的关联信息
		batchSaveRelation(categoryRequest, categoryEntity.getId());
	}

	private void batchSaveRelation(AddOrUpdateFrontCategoryRequest categoryRequest, Long categoryId) {
		List<CategoryRelationEntity> saveRelationList = Lists.newArrayListWithCapacity(categoryRequest.getBackCategoryIdList().size());
		for (Long backCategoryId : categoryRequest.getBackCategoryIdList()) {
			CategoryRelationEntity entity = new CategoryRelationEntity();
			entity.setFrontCategoryId(categoryId);
			entity.setBackCategoryId(backCategoryId);
			entity.setCreatedBy(categoryRequest.getUserId());
			entity.setChangedBy(categoryRequest.getUserId());
			saveRelationList.add(entity);
		}
		categoryRelationService.batchSave(saveRelationList);
	}

	/**
	 * 更新前台分类信息
	 *
	 * @param updateFrontCategoryRequest 更新前台分类的参数
	 * @author liuqiuyi
	 * @date 2021/12/13 10:14
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(AddOrUpdateFrontCategoryRequest updateFrontCategoryRequest) {
		FrontCategoryEntity frontEntity = new FrontCategoryEntity();
		frontEntity.setId(updateFrontCategoryRequest.getCategoryId());
		frontEntity.setName(updateFrontCategoryRequest.getCategoryName());
		frontEntity.setSort(updateFrontCategoryRequest.getSort());
		frontEntity.setIcon(updateFrontCategoryRequest.getIconUrl());
		int updateNum = 0;
		try {
			updateNum = frontCategoryMapper.updateById(frontEntity);
		} catch (DuplicateKeyException e) {
			throw new BusinessException(ErrorEnums.CATEGORY_NAME_IS_EXIST);
		}
		if (updateNum <= 0) {
			throw new BusinessException(ErrorEnums.SAVE_UPDATE_NOT_EXIST);
		}
		if (CollectionUtils.isEmpty(updateFrontCategoryRequest.getBackCategoryIdList())) {
			return;
		}
		// 判断是添加分类关系还是更新分类关系
		List<CategoryRelationEntity> relationEntityList = categoryRelationService.getRelationByFrontCategoryId(updateFrontCategoryRequest.getCategoryId());
		if (!CollectionUtils.isEmpty(relationEntityList)) {
			// 如果不为空,先删除,在更新(可能存在之前只关联了一个,更新时关联多个的情况)
			Set<Long> relationIdList = relationEntityList.stream().map(CategoryRelationEntity::getId).collect(Collectors.toSet());
			categoryRelationService.deletedByIdList(relationIdList);
		}
		batchSaveRelation(updateFrontCategoryRequest, updateFrontCategoryRequest.getCategoryId());
	}

	/**
	 * 更新前台分类状态
	 *
	 * @param categoryId 前台分类 id
	 * @author liuqiuyi
	 * @date 2021/12/13 11:05
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateStateFront(Long categoryId, Long userId) {
		if (Objects.isNull(categoryId) || Objects.isNull(userId)) {
			log.error("com.drstrong.health.product.service.impl.FrontCategoryServiceImpl#updateStateFront() param is null");
			return;
		}
		FrontCategoryEntity categoryEntity = queryById(categoryId);
		if (Objects.isNull(categoryEntity)) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
		if (Objects.equals(0, categoryEntity.getState())) {
			// 更新为启用
			categoryEntity.setState(1);
		} else {
			categoryEntity.setState(0);
		}
		categoryEntity.setChangedBy(userId);
		categoryEntity.setChangedAt(LocalDateTime.now());
		frontCategoryMapper.updateById(categoryEntity);
		categoryRelationService.updateStateByFrontCategoryId(categoryId, categoryEntity.getState(), userId);
	}

	/**
	 * 逻辑删除前台分类状态
	 *
	 * @param categoryId 前台分类 id
	 * @param userId     用户 id
	 * @author liuqiuyi
	 * @date 2021/12/13 11:05
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteFrontCategoryById(Long categoryId, Long userId) {
		if (Objects.isNull(categoryId) || Objects.isNull(userId)) {
			log.error("com.drstrong.health.product.service.impl.FrontCategoryServiceImpl#updateStateFront() param is null");
			return;
		}
		FrontCategoryEntity categoryEntity = queryById(categoryId);
		if (Objects.isNull(categoryEntity)) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
		categoryEntity.setChangedAt(LocalDateTime.now());
		categoryEntity.setChangedBy(userId);
		categoryEntity.setDelFlag(DelFlagEnum.IS_DELETED.getCode());
		frontCategoryMapper.updateById(categoryEntity);
		categoryRelationService.deletedByFrontCategoryId(categoryId, userId);
	}

	/**
	 * 获取首页的分类信息
	 *
	 * @param level 查询的前台分类层级,1-表示查询一级分类,2-表示查询一级分类和二级分类,不传默认查询一级分类
	 * @return 分类信息
	 * @author liuqiuyi
	 * @date 2021/12/15 16:24
	 */
	@Override
	public List<HomeCategoryVO> getHomeCategory(Integer level) {
		LambdaQueryWrapper<FrontCategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(FrontCategoryEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (Objects.isNull(level)) {
			queryWrapper.eq(FrontCategoryEntity::getLevel, 1);
		} else {
			queryWrapper.le(FrontCategoryEntity::getLevel, level);
		}
		List<FrontCategoryEntity> categoryEntityList = frontCategoryMapper.selectList(queryWrapper);
		if (CollectionUtils.isEmpty(categoryEntityList)) {
			return Lists.newArrayList();
		}
		// 封装返回值
		List<HomeCategoryVO> homeCategoryVOList = Lists.newArrayListWithCapacity(categoryEntityList.size());
		for (FrontCategoryEntity categoryEntity : categoryEntityList) {
			HomeCategoryVO categoryVO = new HomeCategoryVO();
			BeanUtils.copyProperties(categoryEntity, categoryVO);
			categoryVO.setCategoryName(categoryEntity.getName());
			homeCategoryVOList.add(categoryVO);
		}
		if (Objects.isNull(level) || Objects.equals(level, 1)) {
			// 一级分类,进行返回值大小裁剪,并添加最后的全部分类
			homeCategoryVOList = homeCategoryVOList.subList(0, FIRST_CATEGORY_SIZE);
			homeCategoryVOList.add(HomeCategoryVO.buildDefault(ALL_CATEGORY, ALL_CATEGORY_ICON));
		} else {
			homeCategoryVOList = BaseTree.listToTree(homeCategoryVOList);
		}
		return homeCategoryVOList;
	}

	/**
	 * 根据分类 id 查询分类的商品信息(分页)
	 *
	 * @param pageCategoryIdRequest 查询参数
	 * @return 分类商品信息
	 * @author liuqiuyi
	 * @date 2021/12/15 20:33
	 */
	@Override
	public PageVO<CategoryProductVO> pageCategoryProduct(PageCategoryIdRequest pageCategoryIdRequest) {
		// 1.校验传入的二级分类是否存在
		FrontCategoryEntity categoryEntity = queryById(pageCategoryIdRequest.getCategoryId());
		if (Objects.isNull(categoryEntity)) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
		// 2.获取前台分类 id (如果是二级分类,还需要拿到父类的 id,然后查询关联信息)
		Set<Long> frontCategoryIdList = Sets.newHashSetWithExpectedSize(4);
		frontCategoryIdList.add(categoryEntity.getId());
		if (Objects.equals(2, categoryEntity.getLevel())) {
			frontCategoryIdList.add(categoryEntity.getParentId());
		}
		// 3.查询前后台关联关系
		List<CategoryRelationEntity> relationByFrontCategoryIds = categoryRelationService.getRelationByFrontCategoryIds(frontCategoryIdList);
		if (CollectionUtils.isEmpty(relationByFrontCategoryIds)) {
			return PageVO.emptyPageVo(pageCategoryIdRequest.getPageNo(), pageCategoryIdRequest.getPageSize());
		}
		// 4.获取后台 id
		Set<Long> backIdList = relationByFrontCategoryIds.stream().map(CategoryRelationEntity::getBackCategoryId).collect(Collectors.toSet());
		// 5.根据后台 id 集合,分页查询商品 spu 表
		QuerySpuRequest querySpuRequest = new QuerySpuRequest();
		querySpuRequest.setState(1);
		querySpuRequest.setBackCategoryIdList(backIdList);
		Page<ProductBasicsInfoEntity> productBasicsInfoEntityPage = productBasicsInfoService.pageQueryProductByParam(querySpuRequest);
		Map<Long, List<ProductSkuEntity>> productIdSkusMap = productBasicsInfoService.buildSkuMap(productBasicsInfoEntityPage.getRecords());
		// 6.封装返回值
		List<CategoryProductVO> productVOList = Lists.newArrayListWithCapacity(productBasicsInfoEntityPage.getRecords().size());
		for (ProductBasicsInfoEntity record : productBasicsInfoEntityPage.getRecords()) {
			CategoryProductVO categoryProductVO = new CategoryProductVO();
			categoryProductVO.setProductId(record.getId());
			categoryProductVO.setProductName(record.getTitle());
			categoryProductVO.setMasterImageUrl(record.getMasterImageUrl());
			Map<String, BigDecimal> priceSectionMap = getPriceSectionMap(productIdSkusMap.get(record.getId()));
			categoryProductVO.setLowPrice(priceSectionMap.get("lowPrice"));
			productVOList.add(categoryProductVO);
		}
		return PageVO.toPageVo(productVOList, productBasicsInfoEntityPage.getTotal(), pageCategoryIdRequest.getPageNo(), pageCategoryIdRequest.getPageSize());
	}

	private Map<String, BigDecimal> getPriceSectionMap(List<ProductSkuEntity> productSkuEntities){
		Map<String, BigDecimal> priceMap = Maps.newHashMapWithExpectedSize(4);
		if (CollectionUtils.isEmpty(productSkuEntities)) {
			priceMap.put("lowPrice", new BigDecimal("0"));
			priceMap.put("highPrice", new BigDecimal("0"));
			return priceMap;
		}
		productSkuEntities.sort(Comparator.comparing(ProductSkuEntity::getSkuPrice));
		Integer lowPrice = productSkuEntities.get(0).getSkuPrice();
		Integer highPrice = productSkuEntities.get(productSkuEntities.size() - 1).getSkuPrice();
		priceMap.put("lowPrice", new BigDecimal(lowPrice));
		priceMap.put("highPrice", new BigDecimal(highPrice));
		return priceMap;
	}

	/**
	 * 设置返回值中,前台分类对应的商品数量
	 */
	private void buildResponseProductNum(FrontCategoryVO frontCategoryVO, Map<Long, Integer> frontIdProductCountMap, Map<Long, List<Long>> frontIdBackIdMap) {
		if (CollectionUtils.isEmpty(frontCategoryVO.getChildren())) {
			frontCategoryVO.setProductCount(frontIdProductCountMap.getOrDefault(frontCategoryVO.getId(), 0));
			frontCategoryVO.setBackCategoryIdList(frontIdBackIdMap.getOrDefault(frontCategoryVO.getId(), Lists.newArrayList()));
		} else {
			for (Object child : frontCategoryVO.getChildren()) {
				FrontCategoryVO childResponse = (FrontCategoryVO) child;
				buildResponseProductNum(childResponse, frontIdProductCountMap, frontIdBackIdMap);
			}
		}
	}

	/**
	 * 组装前台分类对应的商品数量
	 */
	private Map<Long, Integer> buildFrontCategoryProductNumMap(Map<Long, List<Long>> frontIdBackIdMap, Map<Long, Integer> backIdAndNumMap) {
		Map<Long, Integer> frontIdProductCountMap = Maps.newHashMapWithExpectedSize(frontIdBackIdMap.size());
		for (Map.Entry<Long, List<Long>> categoryEntry : frontIdBackIdMap.entrySet()) {
			int productCount = 0;
			for (Long backId : categoryEntry.getValue()) {
				productCount += backIdAndNumMap.getOrDefault(backId, 0);
			}
			frontIdProductCountMap.put(categoryEntry.getKey(), productCount);
		}
		return frontIdProductCountMap;
	}

	/**
	 * 组装返回值的树形结构
	 */
	private List<FrontCategoryVO> buildResponse(List<FrontCategoryEntity> categoryEntityList) {
		List<FrontCategoryVO> frontCategoryVOList = Lists.newArrayListWithCapacity(categoryEntityList.size());
		categoryEntityList.forEach(frontCategoryEntity -> {
			FrontCategoryVO categoryResponse = new FrontCategoryVO();
			BeanUtils.copyProperties(frontCategoryEntity, categoryResponse);
			categoryResponse.setLevelName(LevelEnum.getValueByCode(categoryResponse.getLevel()));
			categoryResponse.setCategoryName(frontCategoryEntity.getName());
			categoryResponse.setProductCount(0);
			categoryResponse.setCategoryStatus(frontCategoryEntity.getState());
			categoryResponse.setCreateTime(frontCategoryEntity.getCreatedAt());

			frontCategoryVOList.add(categoryResponse);
		});
		return BaseTree.listToTree(frontCategoryVOList);
	}
}
