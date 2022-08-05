package com.drstrong.health.product.service.chinese;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.chinese.ChineseSpuInfoEntity;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;

/**
 * <p>
 * 中药 spu 信息 服务类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
public interface ChineseSpuInfoService extends IService<ChineseSpuInfoEntity> {

    /**
     * 根据药材 Code 获取 spu 信息
     *
     * @param medicineCode 药材code
     * @param storeId 店铺 id
     * @return 中药的 spu 信息
     * @author liuqiuyi
     * @date 2022/8/1 22:07
     */
    ChineseSpuInfoEntity getByMedicineCode(String medicineCode, Long storeId);

    /**
     * 生成中药的 spu 信息
     *
     * @param saveOrUpdateSkuVO 保存的入参
     * @return spuCode
     * @author liuqiuyi
     * @date 2022/8/1 22:12
     */
    String saveChineseSpu(SaveOrUpdateSkuVO saveOrUpdateSkuVO);

    /**
     * 更新中药材code
     *
     * @param spuCode    spu编码
     * @param saveOrUpdateSkuVO 入参
     * @author liuqiuyi
     * @date 2022/8/2 11:36
     */
    void updateMedicineCodeBySpuCode(String spuCode, SaveOrUpdateSkuVO saveOrUpdateSkuVO);
}
