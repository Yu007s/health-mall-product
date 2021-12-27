package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.BackCategoryMapper;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.BackCategoryManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 后台分类的增删改方法,直接拷贝的之前的代码,未改动
 *
 * @author liuqiuyi
 * @date 2021/12/27 17:48
 */
@Service
@Slf4j
public class BackCategoryManageServiceImpl extends ServiceImpl<BackCategoryMapper, BackCategoryEntity> implements BackCategoryManageService {

	private static String toPath(String parentPath, String item) {
		StringBuilder path = new StringBuilder();
		if (parentPath != null) {
			path.append(parentPath).append(",");
		}
		path.append(item);
		return path.toString();
	}

	private BackCategoryEntity getCategory(Long categoryId) throws BusinessException {
		BackCategoryEntity category = baseMapper.selectById(categoryId);
		if (category == null) {
			throw new BusinessException("分类不存在,id=" + categoryId);
		}
		return category;
	}

	private void checkName(String name) throws BusinessException {
		QueryWrapper<BackCategoryEntity> query = Wrappers.<BackCategoryEntity>query().eq("name", name);
		if (baseMapper.selectCount(query) > 0) {
			throw new BusinessException("分类名称已存在");
		}
	}

	private static boolean isParentIdChanged(Long parentIdParam, Long parentId) {
		if (parentIdParam == null && !BackCategoryEntity.HEALTH_PRODUCT_TOP_PARENT_ID.equals(parentId)) {
			return true;
		}
		return parentIdParam != null && !parentIdParam.equals(parentId);
	}

	private static boolean isLeafCategory(String[] idPathItems) {
		return idPathItems.length == BackCategoryEntity.MAX_LEVEL;
	}

	private BackCategoryEntity doAdd(BackCategoryEntity entity) {
		BackCategoryEntity parent = baseMapper.selectById(entity.getParentId());
		boolean isTopCategory = parent == null;
		entity.setParentId(isTopCategory ? BackCategoryEntity.HEALTH_PRODUCT_TOP_PARENT_ID : parent.getId());
		//默认启用
		entity.setStatus(BackCategoryEntity.STATUS_ENABLE);

		//先保存分类，以获取分类ID
		baseMapper.insert(entity);

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

	private void doRemove(BackCategoryEntity entity) throws BusinessException {
		String[] idPathItems = StringUtils.commaDelimitedListToStringArray(entity.getIdPath());
		if (isLeafCategory(idPathItems)) {
			Integer pNumber = entity.getPNumber();
			if (pNumber != null && pNumber > 0) {
				throw new BusinessException("删除失败：" + entity.getName() + "分类下存在关联商品");
			}
			baseMapper.deleteById(entity.getId());
			updateLeafCount(idPathItems, -1);
		} else {
			//判断是否存在品类
			Integer leafCount = entity.getLeafCount();
			if (leafCount != null && leafCount > 0) {
				throw new BusinessException("删除失败：" + entity.getName() + "分类存在品类");
			}
			//判断是否存在子分类
			QueryWrapper<BackCategoryEntity> query = Wrappers.<BackCategoryEntity>query().eq("parent_id", entity.getId());
			if (baseMapper.selectCount(query) > 0) {
				throw new BusinessException("删除失败：" + entity.getName() + "分类存在子分类");
			}
			baseMapper.deleteById(entity.getId());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BackCategoryEntity saveEntity(BackCategoryEntity entity) throws BusinessException {
		Assert.notNull(entity, "BackCategoryEntity must not be null.");
		String name = entity.getName();
		Assert.hasText(name, "分类名称不能为空");

		Long categoryId = entity.getId();
		if (categoryId == null) {
			checkName(name);
			doAdd(entity);
			return entity;
		} else {
			BackCategoryEntity category = getCategory(categoryId);
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
				doRemove(category);
				category.setParentId(parentIdParam);
				doAdd(category);
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
	public BackCategoryEntity deleteEntity(Long categoryId) throws BusinessException {
		BackCategoryEntity category = getCategory(categoryId);
		doRemove(category);
		return category;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateStatus(Long categoryId, Integer status) {
		baseMapper.updateStatusById(categoryId, status);
	}

}
