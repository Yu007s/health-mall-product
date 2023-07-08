package com.drstrong.health.product.facade.category.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.drstrong.health.product.facade.category.CategoryFacade;
import com.drstrong.health.product.model.BaseTree;
import com.drstrong.health.product.model.entity.category.v3.CategoryEntity;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.response.category.v3.CategoryVO;
import com.drstrong.health.product.service.category.v3.CategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/6/12 15:58
 */
@Slf4j
@Service
public class CategoryFacadeImpl implements CategoryFacade {
    /**
     * 需要查询 中西药/协定方 分类的商品类型
     */
    private static final Set<Integer> CHINESE_WESTERN_AGREEMENT = Sets.newHashSet(ProductTypeEnum.CHINESE.getCode(),
            ProductTypeEnum.MEDICINE.getCode(),
            ProductTypeEnum.AGREEMENT.getCode());

    /**
     * 西药分类中,需要过滤的id,目前仅有协定方不展示在西药分类中
     */
    @Value("${western.filter.id}")
    private Long westernFilterId;

    @Resource
    CategoryService categoryService;

    /**
     * 根据类型获取分类
     * <p>
     * 中西药分类的返回值中去除了 协定方
     * </>
     *
     * @param productType
     * @author liuqiuyi
     * @date 2023/6/12 16:05
     */
    @Override
    public List<CategoryVO> queryAllCategoryByProductType(Integer productType, Boolean needFilter) {
        // 1.查询 中西药/协定方 分类,并根据条件是否过滤掉协定方
        if (CHINESE_WESTERN_AGREEMENT.contains(productType)) {
            List<CategoryEntity> westernCategoryList = categoryService.queryWesternAll();
            if (Objects.equals(Boolean.TRUE, needFilter)) {
                westernCategoryList.removeIf(categoryEntity -> ObjectUtil.equals(categoryEntity.getId(), westernFilterId));
            }
            return BeanUtil.copyToList(westernCategoryList, CategoryVO.class);
        }
        // 2.查询健康商品
        if (ObjectUtil.equal(ProductTypeEnum.HEALTH.getCode(), productType)) {
            List<CategoryEntity> healthCategoryEntityList = categoryService.queryHealthAll();
            return BaseTree.listToTree(BeanUtil.copyToList(healthCategoryEntityList, CategoryVO.class));
        }
        // 目前就这两种，后续如果有其他类型在扩展....
        return Lists.newArrayList();
    }

    /**
     * 获取药品分类(医生端-空中药房)
     *
     * @param productType
     * @return
     */
    @Override
    public List<CategoryVO> queryCategoryByProductType(Integer productType) {
        //中西药+协定方
        if (CHINESE_WESTERN_AGREEMENT.contains(productType)) {
            List<CategoryEntity> westernCategoryList = categoryService.queryWesternCategory();
            return BeanUtil.copyToList(westernCategoryList, CategoryVO.class);
        }
        //健康用品
        if (ObjectUtil.equal(ProductTypeEnum.HEALTH.getCode(), productType)) {
            List<CategoryEntity> healthCategoryEntityList = categoryService.queryHealthCategory();
            return BaseTree.listToTree(BeanUtil.copyToList(healthCategoryEntityList, CategoryVO.class));
        }
        return Lists.newArrayList();
    }
}
