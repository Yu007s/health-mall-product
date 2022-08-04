package com.drstrong.health.product.service.chinese;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 中药 sku 信息表 服务类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
public interface ChineseSkuInfoService extends IService<ChineseSkuInfoEntity> {
    /**
     * 根据条件分页查询 sku 信息
     *
     * @param queryParam 查询的条件
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/1 14:20
     */
    Page<ChineseSkuInfoEntity> pageQuerySkuByRequest(ChineseManagerSkuRequest queryParam);

    /**
     * 根据条件查询 sku 信息，供导出使用
     *
     * @param queryParam 查询的条件
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/1 14:20
     */
    List<ChineseSkuInfoEntity> listQuerySkuByRequest(ChineseManagerSkuRequest queryParam);

    /**
     * 根据店铺id和药材code获取sku信息
     *
     * @param medicineCode 药材 code
     * @param storeId      店铺id
     * @return sku信息
     * @author liuqiuyi
     * @date 2022/8/1 15:56
     */
    ChineseSkuInfoEntity getByMedicineCodeAndStoreId(String medicineCode, Long storeId);

	/**
	 * 根据药材 code 集合和店铺id，获取中药信息
	 *
	 * @param medicineCodes 中药编码集合
	 * @param storeId       店铺 id
	 * @return 中药材信息集合
	 * @author liuqiuyi
	 * @date 2022/8/3 19:39
	 */
	List<ChineseSkuInfoEntity> listByMedicineCodeAndStoreId(Set<String> medicineCodes, Long storeId);

    /**
     * 根据 skuCode 查询 sku 信息
     *
     * @param skuCode sku 编码
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/1 16:15
     */
    ChineseSkuInfoEntity getBySkuCode(String skuCode);

    /**
     * 根据 skuCode 集合查询 sku 信息
     *
     * @param skuCodes sku 编码
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/1 16:15
     */
    List<ChineseSkuInfoEntity> listBySkuCode(Set<String> skuCodes);

    /**
     * 更新sku信息
     *
     * @param saveOrUpdateSkuVO 接口入参
     * @author liuqiuyi
     * @date 2022/8/1 15:44
     */
    void updateSku(SaveOrUpdateSkuVO saveOrUpdateSkuVO);

    /**
     * 保存sku信息
     *
     * @param saveOrUpdateSkuVO 接口入参
     * @author liuqiuyi
     * @date 2022/8/1 15:44
     */
    void saveSku(SaveOrUpdateSkuVO saveOrUpdateSkuVO);

    /**
     * 根据 skuCode 批量更新sku状态
     *
     * @param updateSkuStateRequest 参数
     * @author liuqiuyi
     * @date 2022/8/2 14:41
     */
    void updateSkuStatue(UpdateSkuStateRequest updateSkuStateRequest);

    /**
     * 根据药材code判断是否有关联某个sku
     *
     * @param medicineCode 药材编码
     * @return boolean 类型，true-存在，false-不存在
     * @author liuqiuyi
     * @date 2022/8/2 14:48
     */
    Boolean checkHasChineseByMedicineCode(String medicineCode);

	/**
	 * 根据 skuCode 或者 药材 id,查询中药 sku 信息
	 * <p> 仅支持查询同一店铺的 sku 信息 </>
	 *
	 * @param skuCodeList sku编码信息
	 * @param medicineIdList 药材 id
	 * @param storeId 店铺 id
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2022/8/4 14:42
	 */
	List<ChineseSkuInfoEntity> queryStoreSkuByCodesOrMedicineIds(Set<String> skuCodeList, Set<Long> medicineIdList, Long storeId);
}
