package com.drstrong.health.product.service.chinese;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.chinese.ChineseSpuInfoEntity;

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
     * @return 中药的 spu 信息
     * @author liuqiuyi
     * @date 2022/8/1 22:07
     */
    ChineseSpuInfoEntity getByMedicineCode(String medicineCode);

    /**
     * 生成中药的 spu 信息
     *
     * @param medicineCode 中药材code
     * @return spuCode
     * @author liuqiuyi
     * @date 2022/8/1 22:12
     */
    String saveChineseSpu(String medicineCode, String name, String createdBy);
}
