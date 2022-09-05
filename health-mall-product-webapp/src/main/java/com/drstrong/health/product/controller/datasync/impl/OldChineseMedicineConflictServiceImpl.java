package com.drstrong.health.product.controller.datasync.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.constants.DsName;
import com.drstrong.health.product.controller.datasync.IChineseMedicineConflictService;
import com.drstrong.health.product.controller.datasync.mapper.OldChineseMedicineConflictMapper;
import com.drstrong.health.product.controller.datasync.model.ChineseMedicineConflict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 中药材冲反库 服务实现类
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Service
@DS(DsName.SLAVE_I)
public class OldChineseMedicineConflictServiceImpl extends ServiceImpl<OldChineseMedicineConflictMapper, ChineseMedicineConflict> implements IChineseMedicineConflictService {

    @Override
    public HashMap<Long, List<ChineseMedicineConflict>> getConflicts(Long id) {
        HashMap<Long, List<ChineseMedicineConflict>> hashMap = new HashMap<>(36);
        LambdaQueryWrapper<ChineseMedicineConflict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.ge(ChineseMedicineConflict::getId,id).orderBy(true,true, ChineseMedicineConflict::getId).last("limit 20");
        lambdaQueryWrapper.groupBy(ChineseMedicineConflict::getChineseMedicineId);
        List<ChineseMedicineConflict> list = list(lambdaQueryWrapper);
        if (list.size() != 0) {
            List<Long> collect = list.stream().map(ChineseMedicineConflict::getChineseMedicineId).distinct().collect(Collectors.toList());
            LambdaQueryWrapper<ChineseMedicineConflict> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ChineseMedicineConflict::getChineseMedicineId,collect);
            List<ChineseMedicineConflict> list1 = list(queryWrapper);
            //将list合并
            for (ChineseMedicineConflict chineseMedicineConflict : list1) {
                List<ChineseMedicineConflict> alias = hashMap.computeIfAbsent(chineseMedicineConflict.getChineseMedicineId(), k -> new ArrayList<>());
                alias.add(chineseMedicineConflict);
            }
        }
        return hashMap;
    }
}
