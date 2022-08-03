package com.drstrong.health.product.service.chinese;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineConflictEntity;

/**
 * <p>
 * 中药材别名表 服务类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-07-27
 */
public interface ChineseMedicineConflictService extends IService<ChineseMedicineConflictEntity> {
    /**
     * 根据药材编码查询相反药材的编码
     * @param medicineCode  中药材编码
     * @return  查询所得列表
     */
    ChineseMedicineConflictEntity getByMedicineCode(String medicineCode);

    /**
     * 更新中药材相反表一条记录
     * @param conflictEntity 中药材相反实体类
     */
    void saveUpdate(ChineseMedicineConflictEntity conflictEntity,Long userId);
}
