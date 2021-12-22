package com.drstrong.health.product.model.entity.category;

import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.BaseTree;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 后台分类
 *
 * @author liuqiuyi
 * @date 2021/12/9 15:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_product_back_category")
public class BackCategoryEntity extends BaseTree implements Serializable {
	private static final long serialVersionUID = -1581848198754072917L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 叶子节点分类数量
	 */
	private Integer leafCount;

	/**
	 * 路径id列表，以逗号分隔
	 */
	private String idPath;

	/**
	 * 路径名称列表，以逗号分隔
	 */
	private String namePath;

	/**
	 * 分类级别
	 */
	private Integer level;

	/**
	 * 排序号
	 */
	private Integer orderNumber;

	/**
	 * 状态：1-启用，0-禁用， -1-删除
	 */
	private Integer status;

	/**
	 * icon图标
	 */
	private String icon;

	/**
	 * 已关联商品数量
	 */
	private Integer pNumber;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 乐观锁字段
	 */
	private Integer version;

	/**
	 * 创建时间
	 */
	private LocalDateTime createdAt;

	/**
	 * 创建人
	 */
	private String createdBy;

	/**
	 * 修改时间
	 */
	private LocalDateTime changedAt;

	/**
	 * 修改人
	 */
	private String changedBy;

	/**
	 * 是否删除 0：正常 1：删除
	 */
	private Integer delFlag;

	/**
	 * 封装各层级分类的商品数量
	 * <p>
	 * 注意:
	 * 商品挂载在三级分类下,但是分类有继承关系,例如:一级分类的商品数量 = 挂载二级分类的商品数量 + 挂载的二级分类下三级分类的商品数量
	 * </>
	 *
	 * @param categoryEntityList 后台分类列表
	 * @param needToTree         是否需要转为树形结构
	 * @author liuqiuyi
	 * @date 2021/12/10 15:35
	 */
	public static Map<Long, Integer> buildCategoryProductCount(List<BackCategoryEntity> categoryEntityList, boolean needToTree) {
		if (CollectionUtils.isEmpty(categoryEntityList)) {
			return Maps.newHashMap();
		}
		// 是否需要转为树形结构
		if (needToTree) {
			categoryEntityList = BaseTree.listToTree(categoryEntityList);
		}
		Map<Long, Integer> categoryIdProductCountMap = Maps.newHashMapWithExpectedSize(categoryEntityList.size());
		categoryEntityList.forEach(entity -> getProductCount(entity, entity.getChildren(), categoryIdProductCountMap));
		return categoryIdProductCountMap;
	}

	private static Integer getProductCount(BackCategoryEntity entity, List<? super BaseTree> childrenList, Map<Long, Integer> categoryIdProductCountMap) {
		// 没有子分类,返回当前分类商品数量
		int productCount = 0;
		if (CollectionUtils.isEmpty(childrenList)) {
			productCount = Objects.isNull(entity.getPNumber()) ? 0 : entity.getPNumber();
			categoryIdProductCountMap.put(entity.getId(), productCount);
			return entity.getPNumber();
		}
		// 有子分类,获取子分类的商品数量
		for (Object object : childrenList) {
			BackCategoryEntity backCategoryEntity = (BackCategoryEntity) object;
			productCount += getProductCount(backCategoryEntity, backCategoryEntity.getChildren(), categoryIdProductCountMap);
		}
		// 当前分类的商品数量总数 = 自身分类的商品数 + 子分类的商品数
		productCount = entity.getPNumber() + productCount;
		categoryIdProductCountMap.put(entity.getId(), productCount);
		return productCount;
	}
}
