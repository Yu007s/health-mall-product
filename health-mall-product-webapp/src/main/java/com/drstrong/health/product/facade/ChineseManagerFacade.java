package com.drstrong.health.product.facade;

import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.chinese.ChineseManagerSkuVO;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;
import com.drstrong.health.product.model.response.chinese.SupplierChineseManagerSkuVO;

import java.util.List;

/**
 * 中药管理门面层，用于组合所有的业务操作
 *
 * @author liuqiuyi
 * @date 2022/8/1 11:14
 */
public interface ChineseManagerFacade {
    /**
     * 中药管理页面，列表查询
     *
     * @param skuRequest 分页查询的入参
     * @return 中药管理列表
     * @author liuqiuyi
     * @date 2022/8/1 11:16
     */
    PageVO<ChineseManagerSkuVO> pageChineseManagerSku(ChineseManagerSkuRequest skuRequest);


    /**
     * 中药管理页面，列表查询，导出数据使用
     *
     * @param skuRequest 分页查询的入参
     * @return 中药管理列表
     * @author liuqiuyi
     * @date 2022/8/1 11:16
     */
    List<ChineseManagerSkuVO> listChineseManagerSkuExport(ChineseManagerSkuRequest skuRequest);

    /**
     * 保存sku信息
     *
     * @param saveOrUpdateSkuVO 接口入参
     * @author liuqiuyi
     * @date 2022/8/1 15:44
     */
    void saveOrUpdateSku(SaveOrUpdateSkuVO saveOrUpdateSkuVO);

    /**
     * 根据 skuCode 获取详情
     *
     * @param skuCode sku 编码
     * @return sku 详细信息，包含供应商等信息
     * @author liuqiuyi
     * @date 2022/8/2 11:50
     */
    SaveOrUpdateSkuVO getSkuByCode(String skuCode);

    /**
     * 批量更新sku上下架状态
     *
     * @param updateSkuStateRequest 入参
     * @author liuqiuyi
     * @date 2022/8/2 14:21
     */
    void listUpdateSkuState(UpdateSkuStateRequest updateSkuStateRequest);

    /**
     * 供应商中药库存页面，列表查询接口,提供给供应商远程调用
     *
     * @author liuqiuyi
     * @date 2022/8/5 10:17
     */
    PageVO<SupplierChineseManagerSkuVO> pageSupplierChineseManagerSku(ChineseManagerSkuRequest skuRequest);

    /**
     * 供应商中药库存页面，列表查询导出,提供给供应商远程调用
     *
     * @author liuqiuyi
     * @date 2022/8/5 10:17
     */
    List<SupplierChineseManagerSkuVO> listSupplierChineseManagerSkuExport(ChineseManagerSkuRequest skuRequest);

    /**
     * 店铺数据初始化,将中药材默认上架到所有店铺,关联天江供应商
     *
     * @param supplierId 供应商 id
     * @author liuqiuyi
     * @date 2022/8/5 14:23
     */
    void storeDataInitialize(Long supplierId);
}
