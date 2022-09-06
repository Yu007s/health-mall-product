package com.drstrong.health.product.controller.datasync;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.constants.DsName;
import com.drstrong.health.product.model.entity.chinese.OldChineseMedicine;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 中药材库 服务类
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Deprecated
public interface IChineseMedicineService extends IService<OldChineseMedicine> {
    /**
     * 根据药材id获取药材  老数据库中
     * @param id  药材id
     * @return 老药材表中数据
     */
    List<OldChineseMedicine> getMedicines(Long id);

}
