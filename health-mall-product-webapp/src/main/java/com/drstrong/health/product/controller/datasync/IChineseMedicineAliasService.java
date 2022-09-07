package com.drstrong.health.product.controller.datasync;


import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.controller.datasync.model.ChineseMedicineAlias;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 中药材别名表 服务类
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Deprecated
public interface IChineseMedicineAliasService extends IService<ChineseMedicineAlias> {
    /**
     * 根据主键id 获取中药材别名
     * @param id 中药材id
     * @return 返回hashMap 中药材id -> 别名实体
     */
    HashMap<Long, List<ChineseMedicineAlias>> getAlias(Long id);
}
