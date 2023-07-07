package com.drstrong.health.product.service.medicine;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.medication.MedicineClassificationEntity;
import com.drstrong.health.product.model.response.medicine.FixedClassificationVO;
import com.drstrong.health.product.model.response.medicine.MedicineClassificationVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 药品基础分类 服务类
 * </p>
 *
 * @author zzw
 * @since 2023-06-06
 */
public interface MedicineClassificationService extends IService<MedicineClassificationEntity> {

    /**
     * 根据类型获取药品分类
     *
     * @param classificationType
     * @return
     */
    List<MedicineClassificationVO> getListByType(Integer classificationType);

    /**
     * 获取所有药品基础分类
     *
     * @return
     */
    Map<Integer, List<MedicineClassificationVO>> getAllClassification();

}
