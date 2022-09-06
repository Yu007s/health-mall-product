package com.drstrong.health.product.controller.datasync;


import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.controller.datasync.model.ChineseMedicineConflict;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 中药材冲反库 服务类
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Deprecated
public interface IChineseMedicineConflictService extends IService<ChineseMedicineConflict> {
    /**
     * 根据中药材id  获取相反药材实体
     * @param id  中药材id
     * @return id -> 相反药材表实体类
     */
    HashMap<Long, List<ChineseMedicineConflict>> getConflicts(Long id);
}
