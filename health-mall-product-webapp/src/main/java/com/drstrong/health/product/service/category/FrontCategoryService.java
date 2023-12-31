package com.drstrong.health.product.service.category;

import com.drstrong.health.product.model.entity.category.FrontCategoryEntity;
import com.drstrong.health.product.model.request.category.AddOrUpdateFrontCategoryRequest;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.request.category.PageCategoryIdRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.category.CategoryProductVO;
import com.drstrong.health.product.model.response.category.FrontCategoryVO;
import com.drstrong.health.product.model.response.category.HomeCategoryVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 前台分类 service 接口
 *
 * @author liuqiuyi
 * @date 2021/12/7 20:02
 */
public interface FrontCategoryService {
	List<FrontCategoryEntity> queryByParam(CategoryQueryRequest categoryQueryRequest);

	/**
	 * 查询所有的前台分类,并组装树形结构
	 *
	 * @param categoryQueryRequest 查询的参数信息
	 * @return 前台分类的集合
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	List<FrontCategoryVO> queryByParamToTree(CategoryQueryRequest categoryQueryRequest);

	Map<Long, Integer> getFrontIdProductCountMap(Set<Long> frontIdCategoryIdList);

	/**
	 * 根据分类 id 集合查询分类信息
	 *
	 * @param categoryIdList 分类 id 集合
	 * @return 前台分类集合信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:41
	 */
	List<FrontCategoryEntity> queryByIdList(Set<Long> categoryIdList);

	/**
	 * 根据分类 id 查询分类信息
	 *
	 * @param categoryId 分类 id
	 * @return 前台分类信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:41
	 */
	FrontCategoryEntity queryById(Long categoryId);

	/**
	 * 根据分类 id 查询分类信息
	 *
	 * @param parentCategoryId 父类 id
	 * @return 分类信息
	 * @author liuqiuyi
	 * @date 2021/12/23 00:10
	 */
	List<FrontCategoryEntity> queryByParentId(Long parentCategoryId);

	/**
	 * 校验分类是否存在
	 *
	 * @param categoryId 分类 id
	 * @author liuqiuyi
	 * @date 2021/12/26 15:40
	 */
	void checkCategoryIsExist(Long categoryId);

	/**
	 * 校验分类名称是否重复
	 *
	 * @param name     名称
	 * @param parentId 父类 id
	 * @author liuqiuyi
	 * @date 2021/12/26 15:40
	 */
	void checkNameIsRepeat(String name, Long parentId);

	/**
	 * 添加前台分类
	 *
	 * @param categoryRequest 入参信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:19
	 */
	void add(AddOrUpdateFrontCategoryRequest categoryRequest);

	/**
	 * 更新前台分类信息
	 *
	 * @param updateFrontCategoryRequest 更新前台分类的参数
	 * @author liuqiuyi
	 * @date 2021/12/13 10:14
	 */
	void update(AddOrUpdateFrontCategoryRequest updateFrontCategoryRequest);

	/**
	 * 更新前台分类状态
	 *
	 * @param categoryId 前台分类 id
	 * @param userId     用户 id
	 * @author liuqiuyi
	 * @date 2021/12/13 11:05
	 */
	void updateStateFront(Long categoryId, String userId);

	/**
	 * 逻辑删除前台分类状态
	 *
	 * @param categoryId 前台分类 id
	 * @param userId     用户 id
	 * @author liuqiuyi
	 * @date 2021/12/13 11:05
	 */
	void deleteFrontCategoryById(Long categoryId, String userId);

	/**
	 * 获取首页的分类信息
	 *
	 * @param level 查询的前台分类层级,1-表示查询一级分类,2-表示查询一级分类和二级分类,不传默认查询一级分类
	 * @return 分类信息
	 * @author liuqiuyi
	 * @date 2021/12/15 16:24
	 */
	List<HomeCategoryVO> getHomeCategory(Integer level);

	/**
	 * 根据分类 id 查询分类的商品信息(分页)
	 *
	 * @param pageCategoryIdRequest 查询参数
	 * @return 分类商品信息
	 * @author liuqiuyi
	 * @date 2021/12/15 20:33
	 */
	PageVO<CategoryProductVO> pageCategoryProduct(PageCategoryIdRequest pageCategoryIdRequest);


	/**
	 * 根据一级类 id 查询二级分类的商品信息
	 *
	 * @param oneLevelId 一级分类 id
	 * @return 二级分类的信息
	 * @author liuqiuyi
	 * @date 2021/12/15 20:33
	 */
	List<HomeCategoryVO> getLevelTwoById(Long oneLevelId);
}
