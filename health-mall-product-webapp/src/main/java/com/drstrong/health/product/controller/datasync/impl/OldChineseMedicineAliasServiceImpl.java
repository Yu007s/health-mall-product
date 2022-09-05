package com.drstrong.health.product.controller.datasync.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.constants.DsName;
import com.drstrong.health.product.controller.datasync.IChineseMedicineAliasService;
import com.drstrong.health.product.controller.datasync.mapper.OldChineseMedicineAliasMapper;
import com.drstrong.health.product.controller.datasync.model.ChineseMedicineAlias;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 中药材别名表 服务实现类
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Service
@DS(DsName.SLAVE_I)
public class OldChineseMedicineAliasServiceImpl extends ServiceImpl<OldChineseMedicineAliasMapper, ChineseMedicineAlias> implements IChineseMedicineAliasService {

    @Override
    public HashMap<Long, List<ChineseMedicineAlias>> getAlias(Long id) {
        LambdaQueryWrapper<ChineseMedicineAlias> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.ge(ChineseMedicineAlias::getMedicineId,id).orderBy(true,true,ChineseMedicineAlias::getMedicineId).last("limit 20");
        lambdaQueryWrapper.groupBy(ChineseMedicineAlias::getMedicineId);
        List<ChineseMedicineAlias> list = list(lambdaQueryWrapper);
        List<Long> collect = list.stream().map(ChineseMedicineAlias::getMedicineId).distinct().collect(Collectors.toList());
        if (collect.size() == 0) {
            return new HashMap<>(0);
        }
        LambdaQueryWrapper<ChineseMedicineAlias> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ChineseMedicineAlias::getMedicineId,collect);
        List<ChineseMedicineAlias> list1 = list(queryWrapper);
        //将list合并
        HashMap<Long,List<ChineseMedicineAlias>> hashMap = new HashMap<>(32);
        for (ChineseMedicineAlias chineseMedicineAlias : list1) {
            List<ChineseMedicineAlias> alias = hashMap.computeIfAbsent(chineseMedicineAlias.getMedicineId(), k -> new ArrayList<>());
            alias.add(chineseMedicineAlias);
        }
        return hashMap;
    }
}
