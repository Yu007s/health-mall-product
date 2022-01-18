package com.drstrong.health.product.service.category.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.constants.CommonConstant;
import com.drstrong.health.product.dao.category.FrontCategoryMapper;
import com.drstrong.health.product.model.BaseTree;
import com.drstrong.health.product.model.entity.category.CategoryRelationEntity;
import com.drstrong.health.product.model.entity.category.FrontCategoryEntity;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.LevelEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.category.AddOrUpdateFrontCategoryRequest;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.request.category.PageCategoryIdRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.category.CategoryProductVO;
import com.drstrong.health.product.model.response.category.FrontCategoryVO;
import com.drstrong.health.product.model.response.category.HomeCategoryVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.pro.PharmacyGoodsRemoteProService;
import com.drstrong.health.product.service.category.BackCategoryService;
import com.drstrong.health.product.service.category.CategoryRelationService;
import com.drstrong.health.product.service.category.FrontCategoryService;
import com.drstrong.health.product.service.product.ProductBasicsInfoService;
import com.drstrong.health.product.service.product.ProductSkuService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
	 * 默认的前台分类图标地址
	 */
	private static final String DEFAULT_CATEGORY_ICON = "https://file.drstrong.cn/mall/product/category_icon.png";

	@Resource
	FrontCategoryMapper frontCategoryMapper;

	@Resource
	CategoryRelationService categoryRelationService;

	@Resource
	BackCategoryService backCategoryService;

	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	@Resource
	ProductSkuService productSkuService;

	@Resource
	PharmacyGoodsRemoteProService pharmacyGoodsRemoteProService;

	/**
	 * 公共的查询方法
	 *
	 * @author liuqiuyi
	 * @date 2021/12/23 10:35
	 */
	@Override
	public List<FrontCategoryEntity> queryByParam(CategoryQueryRequest categoryQueryRequest) {
		LambdaQueryWrapper<FrontCategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(FrontCategoryEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (StringUtils.isNotBlank(categoryQueryRequest.getCategoryName())) {
			queryWrapper.like(FrontCategoryEntity::getName, categoryQueryRequest.getCategoryName());
		}
		if (StringUtils.isNotBlank(categoryQueryRequest.getCompareName())) {
			queryWrapper.eq(FrontCategoryEntity::getName, categoryQueryRequest.getCompareName());
		}
		if (Objects.nonNull(categoryQueryRequest.getState())) {
			queryWrapper.eq(FrontCategoryEntity::getState, categoryQueryRequest.getState());
		}
		if (Objects.nonNull(categoryQueryRequest.getLevel())) {
			queryWrapper.eq(FrontCategoryEntity::getLevel, categoryQueryRequest.getLevel());
		}
		if (!CollectionUtils.isEmpty(categoryQueryRequest.getCategoryIdList())) {
			queryWrapper.in(FrontCategoryEntity::getId, categoryQueryRequest.getCategoryIdList());
		}
		return frontCategoryMapper.selectList(queryWrapper);
	}

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
		List<FrontCategoryEntity> frontCategoryEntityList = queryByParam(categoryQueryRequest);
		if (CollectionUtils.isEmpty(frontCategoryEntityList)) {
			return Lists.newArrayList();
		}
		// 组装返回值的树形结构
		List<FrontCategoryVO> frontCategoryVOList = buildResponse(frontCategoryEntityList);
		// 2.获取前台分类关联的后台分类
		Set<Long> frontCategoryIdList = frontCategoryEntityList.stream().map(FrontCategoryEntity::getId).collect(Collectors.toSet());
		// 前台分类关联的后台分类 id
		Map<Long, Set<Long>> frontIdBackIdMap = categoryRelationService.getFrontAndBackCategoryToMap(frontCategoryIdList);
		// 获取前台分类对应的商品数量
		Map<Long, Integer> frontIdProductCountMap = getFrontIdProductCountMap(frontCategoryIdList);
		// 6.设置返回值
		frontCategoryVOList.forEach(frontCategoryVO -> buildResponseProductNum(frontCategoryVO, frontIdProductCountMap, frontIdBackIdMap));
		return frontCategoryVOList;
	}

	/**
	 * 获取前台分类对应的商品数量
	 * <p> 如果是二级分类,二级分类的商品数量 = 二级分类的数量 + 一级分类的数量 </>
	 *
	 * @param frontCategoryIdList 前台分类 id
	 * @return 前台分类对应的商品数量  key=前台分类 id,value = 商品数量
	 * @author liuqiuyi
	 * @date 2021/12/23 10:23
	 */
	@Override
	public Map<Long, Integer> getFrontIdProductCountMap(Set<Long> frontCategoryIdList) {
		// 获取前台分类信息
		List<FrontCategoryEntity> frontCategoryEntityList = queryByIdList(frontCategoryIdList);
		if (CollectionUtils.isEmpty(frontCategoryEntityList)) {
			return Maps.newHashMap();
		}
		// 获取关联的后台 id
		Map<Long, Set<Long>> frontIdBackIdListMap = categoryRelationService.getFrontAndBackCategoryToMap(frontCategoryIdList);
		if (CollectionUtils.isEmpty(frontIdBackIdListMap)) {
			return Maps.newHashMap();
		}
		Set<Long> backCategoryIdSet = frontIdBackIdListMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
		// 获取后台分类和对应的商品数量
		Map<Long, Integer> backIdProductNumMap = backCategoryService.getBackIdProductNumMap(backCategoryIdSet);
		// 组装每个分类的商品数量
		Map<Long, Integer> frontIdProductCountMap = Maps.newHashMapWithExpectedSize(frontCategoryEntityList.size());
		for (FrontCategoryEntity categoryEntity : frontCategoryEntityList) {
			Set<Long> backIdSet = frontIdBackIdListMap.getOrDefault(categoryEntity.getId(), Sets.newHashSet());
			if (Objects.equals(CommonConstant.ONE_LEVEL, categoryEntity.getLevel())) {
				Integer productNum = getProductNum(backIdSet, backIdProductNumMap);
				frontIdProductCountMap.put(categoryEntity.getId(), productNum);
			} else {
				// 二级分类的商品数量 = 一级分类商品数量 + 二级分类商品数
				Set<Long> parentBackIdSet = frontIdBackIdListMap.getOrDefault(categoryEntity.getParentId(), Sets.newHashSet());
				backIdSet.addAll(parentBackIdSet);
				Integer productNum = getProductNum(backIdSet, backIdProductNumMap);
				frontIdProductCountMap.put(categoryEntity.getId(), productNum);
			}
		}
		// 组装返回值:一级分类的数量 = 一级分类的自己的商品数 + 二级分类的商品数
		BaseTree.listToTree(frontCategoryEntityList);
		for (FrontCategoryEntity categoryEntity : frontCategoryEntityList) {
			// 不是一级分类,不修改
			if (!Objects.equals(CommonConstant.ONE_LEVEL, categoryEntity.getLevel())) {
				continue;
			}
			// 获取一级分类的商品数量
			Integer parentProductNum = frontIdProductCountMap.getOrDefault(categoryEntity.getId(), 0);
			// 获取一级分类下二级分类的商品数量
			List<? super BaseTree> childrenList = categoryEntity.getChildren();
			for (Object obj : childrenList) {
				FrontCategoryEntity children = (FrontCategoryEntity) obj;
				Integer childNum = frontIdProductCountMap.getOrDefault(children.getId(), 0);
				parentProductNum = parentProductNum + childNum;
			}
			frontIdProductCountMap.put(categoryEntity.getId(), parentProductNum);
		}
		return frontIdProductCountMap;
	}

	private Integer getProductNum(Set<Long> backIdList, Map<Long, Integer> backIdProductNumMap) {
		if (CollectionUtils.isEmpty(backIdList)) {
			return 0;
		}
		int count = 0;
		for (Long categoryId : backIdList) {
			count = count + backIdProductNumMap.getOrDefault(categoryId, 0);
		}
		return count;
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
	 * 根据分类 id 查询分类信息
	 *
	 * @param parentCategoryId 父类 id
	 * @return 分类信息
	 * @author liuqiuyi
	 * @date 2021/12/23 00:10
	 */
	@Override
	public List<FrontCategoryEntity> queryByParentId(Long parentCategoryId) {
		if (Objects.isNull(parentCategoryId)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<FrontCategoryEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(FrontCategoryEntity::getParentId, parentCategoryId).eq(FrontCategoryEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return frontCategoryMapper.selectList(wrapper);
	}

	/**
	 * 校验分类是否存在
	 *
	 * @param categoryId 分类 id
	 * @author liuqiuyi
	 * @date 2021/12/26 15:40
	 */
	@Override
	public void checkCategoryIsExist(Long categoryId) {
		FrontCategoryEntity categoryEntity = queryById(categoryId);
		if (Objects.isNull(categoryEntity)) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
	}

	/**
	 * 校验分类名称是否重复
	 *
	 * @param name     名称
	 * @param parentId 父类 id
	 * @author liuqiuyi
	 * @date 2021/12/26 15:40
	 */
	@Override
	public void checkNameIsRepeat(String name, Long parentId) {
		if (StringUtils.isBlank(name)) {
			throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
		}
		// 如果父类 id 为空,说明添加的是一级分类
		if (Objects.isNull(parentId) || Objects.equals(0L, parentId)) {
			CategoryQueryRequest queryRequest = new CategoryQueryRequest();
			queryRequest.setLevel(1);
			queryRequest.setCompareName(name);
			List<FrontCategoryEntity> entityList = queryByParam(queryRequest);
			if (!CollectionUtils.isEmpty(entityList)) {
				throw new BusinessException(ErrorEnums.CATEGORY_NAME_IS_EXIST);
			}
		} else {
			// 如果存在 parentId,说明添加的是二级分类,查询父类所有的二级分类,校验名称是否重复
			List<FrontCategoryEntity> frontCategoryEntities = queryByParentId(parentId);
			if (!CollectionUtils.isEmpty(frontCategoryEntities)) {
				boolean flag = frontCategoryEntities.stream().anyMatch(frontCategoryEntity -> Objects.equals(name, frontCategoryEntity.getName()));
				if (flag) {
					throw new BusinessException(ErrorEnums.CATEGORY_NAME_IS_EXIST);
				}
			}
		}
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
		boolean isTwoLevel = Objects.nonNull(categoryRequest.getParentId()) && !Objects.equals(0L, categoryRequest.getParentId());
		// 添加的不是一级分类,校验上级分类是否存在
		if (isTwoLevel) {
			checkCategoryIsExist(categoryRequest.getParentId());
		}
		// 如果关联的后台 id 不为空,进行校验
		if (!CollectionUtils.isEmpty(categoryRequest.getBackCategoryIdList())) {
			backCategoryService.checkCategoryIsExist(categoryRequest.getBackCategoryIdList());
		}
		// 校验名称是否重复
		checkNameIsRepeat(categoryRequest.getCategoryName(), categoryRequest.getParentId());
		// 1.保存前台分类信息
		FrontCategoryEntity categoryEntity = new FrontCategoryEntity();
		categoryEntity.setName(categoryRequest.getCategoryName());
		categoryEntity.setIcon(StringUtils.isBlank(categoryRequest.getIconUrl()) ? DEFAULT_CATEGORY_ICON : categoryRequest.getIconUrl());
		categoryEntity.setLevel(isTwoLevel ? 2 : 1);
		categoryEntity.setParentId(isTwoLevel ? categoryRequest.getParentId() : 0);
		categoryEntity.setSort(categoryRequest.getSort());
		categoryEntity.setCreatedBy(categoryRequest.getUserId());
		categoryEntity.setChangedBy(categoryRequest.getUserId());
		categoryEntity.setState(0);
		frontCategoryMapper.insert(categoryEntity);
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
		// 校验分类 id 是否存在
		FrontCategoryEntity categoryEntity = queryById(updateFrontCategoryRequest.getCategoryId());
		if (Objects.isNull(categoryEntity)) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
		// 如果名称不一致,需要校验名称是否重复
		if (!Objects.equals(categoryEntity.getName(), updateFrontCategoryRequest.getCategoryName())) {
			checkNameIsRepeat(updateFrontCategoryRequest.getCategoryName(), categoryEntity.getParentId());
		}
		categoryEntity.setId(updateFrontCategoryRequest.getCategoryId());
		categoryEntity.setName(updateFrontCategoryRequest.getCategoryName());
		categoryEntity.setSort(updateFrontCategoryRequest.getSort());
		categoryEntity.setIcon(updateFrontCategoryRequest.getIconUrl());
		categoryEntity.setChangedAt(LocalDateTime.now());
		categoryEntity.setChangedBy(updateFrontCategoryRequest.getUserId());
		frontCategoryMapper.updateById(categoryEntity);
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
	public void updateStateFront(Long categoryId, String userId) {
		if (Objects.isNull(categoryId) || Objects.isNull(userId)) {
			log.error("FrontCategoryServiceImpl#updateStateFront() param is null");
			return;
		}
		FrontCategoryEntity categoryEntity = queryById(categoryId);
		if (Objects.isNull(categoryEntity)) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
		Integer state = Objects.equals(0, categoryEntity.getState()) ? 1 : 0;

		Set<Long> categoryIdList = Sets.newHashSet(categoryId);
		// 如果更新的是一级分类
		if (Objects.equals(1, categoryEntity.getLevel())) {
			// 获取子分类id
			List<FrontCategoryEntity> frontCategoryEntities = queryByParentId(categoryId);
			if (!CollectionUtils.isEmpty(frontCategoryEntities)) {
				Set<Long> childList = frontCategoryEntities.stream().map(FrontCategoryEntity::getId).collect(Collectors.toSet());
				categoryIdList.addAll(childList);
			}
		}
		frontCategoryMapper.updateStateByIdList(categoryIdList, state, userId);
		categoryRelationService.updateStateByFrontCategoryIdList(categoryIdList, state, userId);
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
	public void deleteFrontCategoryById(Long categoryId, String userId) {
		if (Objects.isNull(categoryId) || Objects.isNull(userId)) {
			log.error("FrontCategoryServiceImpl#updateStateFront() param is null");
			return;
		}
		FrontCategoryEntity categoryEntity = queryById(categoryId);
		if (Objects.isNull(categoryEntity)) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
		boolean isOnLevel = Objects.equals(1, categoryEntity.getLevel());

		Set<Long> categoryIdList = Sets.newHashSet(categoryId);
		// 如果删除的是一级分类
		if (isOnLevel) {
			// 获取子分类id
			List<FrontCategoryEntity> frontCategoryEntities = queryByParentId(categoryId);
			if (!CollectionUtils.isEmpty(frontCategoryEntities)) {
				Set<Long> childList = frontCategoryEntities.stream().map(FrontCategoryEntity::getId).collect(Collectors.toSet());
				categoryIdList.addAll(childList);
			}
		}
		// 获取分类下的商品数量
		Map<Long, Integer> idProductCountMap = getFrontIdProductCountMap(categoryIdList);
		IntSummaryStatistics summaryStatistics = idProductCountMap.values().stream().mapToInt((s) -> s).summaryStatistics();
		if (summaryStatistics.getSum() > 0) {
			throw new BusinessException(ErrorEnums.CATEGORY_DELETED_ERROR);
		}
		frontCategoryMapper.deleteByIdList(categoryIdList, userId);
		categoryRelationService.deletedByFrontCategoryIdList(categoryIdList, userId);
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
		queryWrapper.eq(FrontCategoryEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode()).eq(FrontCategoryEntity::getState, 1);
		if (Objects.isNull(level)) {
			queryWrapper.eq(FrontCategoryEntity::getLevel, 1);
		} else {
			queryWrapper.le(FrontCategoryEntity::getLevel, level);
		}
		// 查询分类信息
		List<HomeCategoryVO> homeCategoryVOList = buildCategoryVOList(queryWrapper);
		if (CollectionUtils.isEmpty(homeCategoryVOList)) {
			return Lists.newArrayList();
		}
		// 按照优先级排序
		homeCategoryVOList.sort(Comparator.comparing(HomeCategoryVO::getSort));
		// 封装返回值
		if (Objects.isNull(level) || Objects.equals(level, 1)) {
			// 一级分类,进行返回值大小裁剪,并添加最后的全部分类
			if (homeCategoryVOList.size() > FIRST_CATEGORY_SIZE) {
				homeCategoryVOList = homeCategoryVOList.subList(0, FIRST_CATEGORY_SIZE);
			}
			homeCategoryVOList.add(HomeCategoryVO.buildDefault(ALL_CATEGORY, ""));
		} else {
			homeCategoryVOList = BaseTree.listToTree(homeCategoryVOList);
		}
		return homeCategoryVOList;
	}

	/**
	 * 根据一级类 id 查询二级分类的商品信息
	 *
	 * @param oneLevelId 一级分类 id
	 * @return 二级分类的信息
	 * @author liuqiuyi
	 * @date 2021/12/15 20:33
	 */
	@Override
	public List<HomeCategoryVO> getLevelTwoById(Long oneLevelId) {
		if (Objects.isNull(oneLevelId)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<FrontCategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(FrontCategoryEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(FrontCategoryEntity::getLevel, 2)
				.eq(FrontCategoryEntity::getState, 1)
				.eq(FrontCategoryEntity::getParentId, oneLevelId);
		return buildCategoryVOList(queryWrapper);
	}

	private List<HomeCategoryVO> buildCategoryVOList(LambdaQueryWrapper<FrontCategoryEntity> queryWrapper) {
		List<FrontCategoryEntity> categoryEntityList = frontCategoryMapper.selectList(queryWrapper);
		if (CollectionUtils.isEmpty(categoryEntityList)) {
			return Lists.newArrayList();
		}
		List<HomeCategoryVO> categoryVOList = Lists.newArrayListWithCapacity(categoryEntityList.size());
		for (FrontCategoryEntity categoryEntity : categoryEntityList) {
			HomeCategoryVO categoryVO = new HomeCategoryVO();
			BeanUtils.copyProperties(categoryEntity, categoryVO);
			categoryVO.setCategoryName(categoryEntity.getName());
			categoryVOList.add(categoryVO);
		}
		return categoryVOList;
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
		if (Objects.equals(CommonConstant.TWO_LEVEL, categoryEntity.getLevel())) {
			frontCategoryIdList.add(categoryEntity.getParentId());
		}
		// 3.查询前后台关联关系
		List<CategoryRelationEntity> relationByFrontCategoryIds = categoryRelationService.getRelationByFrontCategoryIds(frontCategoryIdList);
		if (CollectionUtils.isEmpty(relationByFrontCategoryIds)) {
			return PageVO.newBuilder().pageNo(pageCategoryIdRequest.getPageNo()).pageSize(pageCategoryIdRequest.getPageSize()).totalCount(0).result(Lists.newArrayList()).build();
		}
		// 4.获取后台 id
		Set<Long> backIdList = relationByFrontCategoryIds.stream().map(CategoryRelationEntity::getBackCategoryId).collect(Collectors.toSet());
		// 5.根据后台 id 集合,分页查询商品 spu 表
		QuerySpuRequest querySpuRequest = new QuerySpuRequest();
		querySpuRequest.setUpOffEnum(UpOffEnum.UP);
		querySpuRequest.setBackCategoryIdList(backIdList);
		Page<ProductBasicsInfoEntity> productBasicsInfoEntityPage = productBasicsInfoService.pageQueryProductByParam(querySpuRequest);
		Map<Long, List<ProductSkuEntity>> productIdSkusMap = productBasicsInfoService.buildSkuMap(productBasicsInfoEntityPage.getRecords());
		// 6.查询库存信息
		Set<Long> skuIdList = productIdSkusMap.values().stream().flatMap(Collection::stream).map(ProductSkuEntity::getId).collect(Collectors.toSet());
		Map<Long, Integer> skuHasStockToMap = pharmacyGoodsRemoteProService.getSkuStockNumToMap(skuIdList);
		// 7.封装返回值
		List<CategoryProductVO> productVOList = Lists.newArrayListWithCapacity(productBasicsInfoEntityPage.getRecords().size());
		for (ProductBasicsInfoEntity record : productBasicsInfoEntityPage.getRecords()) {
			CategoryProductVO categoryProductVO = new CategoryProductVO();
			categoryProductVO.setSpuCode(record.getSpuCode());
			categoryProductVO.setProductName(record.getTitle());
			categoryProductVO.setMasterImageUrl(record.getMasterImageUrl());
			List<ProductSkuEntity> skuEntityList = productIdSkusMap.get(record.getId());
			Map<String, BigDecimal> priceSectionMap = productSkuService.getPriceSectionMap(skuEntityList);
			categoryProductVO.setLowPrice(priceSectionMap.get("lowPrice"));
			categoryProductVO.setHasInventory(checkProductHasInventory(skuEntityList, skuHasStockToMap));
			productVOList.add(categoryProductVO);
		}
		return PageVO.newBuilder().pageNo(pageCategoryIdRequest.getPageNo()).pageSize(pageCategoryIdRequest.getPageSize()).totalCount((int) productBasicsInfoEntityPage.getTotal()).result(productVOList).build();
	}

	private Boolean checkProductHasInventory(List<ProductSkuEntity> skuEntityList, Map<Long, Integer> skuHasStockToMap) {
		int stockNum = 0;
		for (ProductSkuEntity productSkuEntity : skuEntityList) {
			stockNum = stockNum + skuHasStockToMap.getOrDefault(productSkuEntity.getId(), 0);
		}
		return stockNum > 0;
	}

	/**
	 * 设置返回值中,前台分类对应的商品数量
	 */
	private void buildResponseProductNum(FrontCategoryVO frontCategoryVO, Map<Long, Integer> frontIdProductCountMap, Map<Long, Set<Long>> frontIdBackIdMap) {
		frontCategoryVO.setProductCount(frontIdProductCountMap.getOrDefault(frontCategoryVO.getId(), 0));
		frontCategoryVO.setBackCategoryIdList(frontIdBackIdMap.getOrDefault(frontCategoryVO.getId(), Sets.newHashSet()));
		if (!CollectionUtils.isEmpty(frontCategoryVO.getChildren())) {
			for (Object child : frontCategoryVO.getChildren()) {
				FrontCategoryVO childResponse = (FrontCategoryVO) child;
				buildResponseProductNum(childResponse, frontIdProductCountMap, frontIdBackIdMap);
			}
		}
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
		frontCategoryVOList.sort(Comparator.comparing(FrontCategoryVO::getSort));
		return BaseTree.listToTree(frontCategoryVOList);
	}
}
