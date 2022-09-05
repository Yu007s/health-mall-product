package com.drstrong.health.product.controller.datasync.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.controller.datasync.IChineseMedicineService;
import com.drstrong.health.product.dao.chinese.OldChineseMedicineMapper;
import com.drstrong.health.product.model.entity.chinese.OldChineseMedicine;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 中药材库 服务实现类
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Service
public class OldChineseMedicineServiceImpl extends ServiceImpl<OldChineseMedicineMapper, OldChineseMedicine> implements IChineseMedicineService {
    @Override
    public List<OldChineseMedicine> getMedicines(Long id) {
        LambdaQueryWrapper<OldChineseMedicine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(OldChineseMedicine::getId,id).orderBy(true,true,OldChineseMedicine::getId).last("limit 100");
        return list(queryWrapper);
    }
}
