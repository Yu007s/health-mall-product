package com.drstrong.health.product.service.category.v3.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.category.v3.CategoryMapper;
import com.drstrong.health.product.model.entity.category.v3.CategoryEntity;
import com.drstrong.health.product.model.enums.CategoryStatusEnum;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.category.v3.OldCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 老的 cms 中的代码,直接迁移过来的
 *
 * @author liuqiuyi
 * @date 2023/6/16 16:24
 */
@Service
@Slf4j
public class OldCategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements OldCategoryService {

	private static String toPath(String parentPath, String item) {
		StringBuilder path = new StringBuilder();
		if (parentPath != null) {
			path.append(parentPath).append(",");
		}
		path.append(item);
		return path.toString();
	}

	private CategoryEntity getCategory(Long categoryId) throws BusinessException {
		LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<CategoryEntity>()
				.ne(CategoryEntity::getStatus, CategoryStatusEnum.DELETE.getCode())
				.eq(CategoryEntity::getId, categoryId);
		CategoryEntity category = baseMapper.selectOne(queryWrapper);
		if (category == null) {
			throw new BusinessException("分类不存在,id=" + categoryId);
		}
		return category;
	}

	private void checkName(String name) throws BusinessException {
		QueryWrapper<CategoryEntity> query = Wrappers.<CategoryEntity>query().eq("name", name).ne("status", CategoryStatusEnum.DELETE.getCode());
		if (baseMapper.selectCount(query) > 0) {
			throw new BusinessException("分类名称已存在");
		}
	}

	private static boolean isParentIdChanged(Long parentIdParam, Long parentId) {
		if (parentIdParam == null && !CategoryEntity.HEALTH_PRODUCT_TOP_PARENT_ID.equals(parentId)) {
			return true;
		}
		return parentIdParam != null && !parentIdParam.equals(parentId);
	}

	private static boolean isLeafCategory(String[] idPathItems) {
		return idPathItems.length == CategoryEntity.MAX_LEVEL;
	}

	private CategoryEntity doAdd(CategoryEntity entity, Integer productType) {
		CategoryEntity parent = baseMapper.selectById(entity.getParentId());
		boolean isTopCategory = parent == null;
		// 中西药的parentId默认是0，健康用品的默认id是-2  按照之前的老逻辑来
		Long parentId = CategoryEntity.HEALTH_PRODUCT_TOP_PARENT_ID;
		if (isTopCategory && Objects.equals(ProductTypeEnum.MEDICINE.getCode(), productType)) {
			parentId = CategoryEntity.MEDICINE_TOP_PARENT_ID_NEW;
		} else if (!isTopCategory){
			parentId = parent.getId();
		}
		entity.setParentId(parentId);
		//默认启用
		entity.setStatus(CategoryEntity.STATUS_ENABLE);

		//先保存分类，以获取分类ID
		Date date = new Date();
		entity.setCreatedAt(date);
		entity.setCreatedBy(entity.getChangedBy());
		entity.setChangedAt(date);
		entity.setChangedBy(entity.getChangedBy());
		baseMapper.insert(entity);
		if (Objects.equals(parentId, CategoryEntity.HEALTH_PRODUCT_TOP_PARENT_ID) || Objects.equals(parentId, CategoryEntity.MEDICINE_TOP_PARENT_ID_NEW)) {
			return entity;
		}

		String parentNamePath = isTopCategory ? null : parent.getNamePath();
		String parentIdPath = isTopCategory ? null : parent.getIdPath();
		//设置分类的namePath, idPath
		entity.setNamePath(toPath(parentNamePath, entity.getName()));
		entity.setIdPath(toPath(parentIdPath, String.valueOf(entity.getId())));
		String[] idPathItems = StringUtils.commaDelimitedListToStringArray(entity.getIdPath());
		//设置分类级别
		entity.setLevel(idPathItems.length);
		baseMapper.updateById(entity);

		//更新父分类叶子节点数量
		updateLeafCount(idPathItems, 1);
		return entity;
	}

	/**
	 * 更新父分类的叶子节点数
	 *
	 * @param idPathItems 分类全路径
	 * @param offset      叶子节点数偏移量
	 */
	private void updateLeafCount(String[] idPathItems, int offset) {
		if (isLeafCategory(idPathItems)) {
			Set<Long> parentIds = new HashSet<>();
			for (int i = 0; i < idPathItems.length - 1; i++) {
				Long parentCategoryId = Long.parseLong(idPathItems[i]);
				parentIds.add(parentCategoryId);
			}
			baseMapper.updateLeafCountByIds(parentIds, offset);
		}
	}

	private void doRemove(CategoryEntity entity, String changeByName) throws BusinessException {
		String[] idPathItems = StringUtils.commaDelimitedListToStringArray(entity.getIdPath());
		if (isLeafCategory(idPathItems)) {
			Integer pNumber = entity.getPNumber();
			if (pNumber != null && pNumber > 0) {
				throw new BusinessException("删除失败：" + entity.getName() + "分类下存在关联商品");
			}
			baseMapper.updateStatusToDeleteById(entity.getId(), changeByName);
			updateLeafCount(idPathItems, -1);
		} else {
			//判断是否存在品类
			Integer leafCount = entity.getLeafCount();
			if (leafCount != null && leafCount > 0) {
				throw new BusinessException("删除失败：" + entity.getName() + "分类存在品类");
			}
			//判断是否存在子分类
			QueryWrapper<CategoryEntity> query = Wrappers.<CategoryEntity>query().eq("parent_id", entity.getId());
			if (baseMapper.selectCount(query) > 0) {
				throw new BusinessException("删除失败：" + entity.getName() + "分类存在子分类");
			}
			baseMapper.updateStatusToDeleteById(entity.getId(), changeByName);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public CategoryEntity saveEntity(CategoryEntity entity, Integer productType) throws BusinessException {
		Assert.notNull(entity, "Category must not be null.");
		String name = entity.getName();
		Assert.hasText(name, "分类名称不能为空");

		Long categoryId = entity.getId();
		if (categoryId == null) {
			checkName(name);
			doAdd(entity,productType);
			return entity;
		} else {
			CategoryEntity category = getCategory(categoryId);
			if (!name.equals(category.getName())) {
				checkName(name);
				category.setName(name);
				//更新路径中的名称
				String oldNamePath = category.getNamePath();
				String newNamePath = generateNewNamePath(name, oldNamePath);
				category.setNamePath(newNamePath);
				//更新该分类及其子分类中的路径名称
				baseMapper.updateChildrenNamePath(category.getIdPath(), oldNamePath, newNamePath);
			}
			category.setIcon(entity.getIcon());
			category.setDescription(entity.getDescription());
			category.setOrderNumber(entity.getOrderNumber());
			baseMapper.updateById(category);

			Long parentIdParam = entity.getParentId();
			Long parentId = category.getParentId();
			if (isParentIdChanged(parentIdParam, parentId)) {
				doRemove(category, entity.getChangedBy());
				category.setParentId(parentIdParam);
				doAdd(category, productType);
			}
		}
		return entity;
	}

	private static String generateNewNamePath(String newName, String oldNamePath) {
		List<String> newNameItems = new ArrayList<>();
		String[] oldNameItems = StringUtils.commaDelimitedListToStringArray(oldNamePath);
		if (oldNameItems.length > 0) {
			//保留所有上级分类名称
			for (int i = 0; i < oldNameItems.length - 1; i++) {
				newNameItems.add(oldNameItems[i]);
			}
		}
		//添加新名称
		newNameItems.add(newName);
		return StringUtils.collectionToCommaDelimitedString(newNameItems);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public CategoryEntity deleteEntity(Long categoryId, String changedName) throws BusinessException {
		CategoryEntity category = getCategory(categoryId);
		doRemove(category, changedName);
		return category;
	}
}
