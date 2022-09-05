package com.drstrong.health.product.service.chinese.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.controller.datasync.model.ChineseMedicineConflict;
import com.drstrong.health.product.dao.chinese.ChineseMedicineConflictMapper;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineConflictEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 中药材相反药材表服务
 *
 * @Author xieYueFeng
 * @Date 2022/07/27/19:32
 */
@Service
public class ChineseMedicineConflictServiceImpl extends ServiceImpl<ChineseMedicineConflictMapper, ChineseMedicineConflictEntity>
        implements ChineseMedicineConflictService {

    @Resource
    ChineseMedicineService chineseMedicineService;
    @Override
    public ChineseMedicineConflictEntity getByMedicineCode(String medicineCode) {
        LambdaQueryWrapper<ChineseMedicineConflictEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(ChineseMedicineConflictEntity::getMedicineConflictCodes)
                .eq(ChineseMedicineConflictEntity::getMedicineCode, medicineCode).eq(ChineseMedicineConflictEntity::getDelFlag,DelFlagEnum.UN_DELETED.getCode());
        return getOne(lambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(ChineseMedicineConflictEntity conflictEntity, Long userId) {
        LambdaQueryWrapper<ChineseMedicineConflictEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChineseMedicineConflictEntity::getMedicineCode, conflictEntity.getMedicineCode())
                .eq(ChineseMedicineConflictEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        ChineseMedicineConflictEntity one = getOne(queryWrapper);
        if (one != null) {
            conflictEntity.setId(one.getId());
        } else {
            conflictEntity.setCreatedBy(userId);
        }
        conflictEntity.setChangedBy(userId);
        String medicineConflictCodes = conflictEntity.getMedicineConflictCodes();
        if(StringUtils.isBlank(medicineConflictCodes)) {
            conflictEntity.setDelFlag(DelFlagEnum.IS_DELETED.getCode());
        }
        LambdaQueryWrapper<ChineseMedicineConflictEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChineseMedicineConflictEntity::getMedicineCode, conflictEntity.getMedicineCode())
                .eq(ChineseMedicineConflictEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        super.saveOrUpdate(conflictEntity, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ChineseMedicineConflictEntity conflictEntity, Long userId) {
        if (conflictEntity.getMedicineCode() == null) {
            return;
        }
        LambdaUpdateWrapper<ChineseMedicineConflictEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ChineseMedicineConflictEntity::getMedicineCode, conflictEntity.getMedicineCode())
                .set(ChineseMedicineConflictEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode());
        super.update(updateWrapper);
    }

    /**
     * 获取所有的相反药材
     *
     * @return 相反药材信息
     * @author liuqiuyi
     * @date 2022/8/8 11:34
     */
    @Override
    public List<ChineseMedicineConflictEntity> listAllConflictEntity() {
        LambdaQueryWrapper<ChineseMedicineConflictEntity> queryWrapper = Wrappers.<ChineseMedicineConflictEntity>lambdaQuery()
                .eq(ChineseMedicineConflictEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        return list(queryWrapper);
    }

    @Override
    public void updateFromOld(HashMap<Long, List<ChineseMedicineConflict>> hashMap) {
        HashMap<Long,String> codeMedicineCodeMap = new HashMap<>(32);
        HashSet<Long> hashSet = new HashSet<>();
        hashMap.forEach((key, value) -> {
            List<Long> collect = value.stream().map(ChineseMedicineConflict::getChineseMedicineConflictId).collect(Collectors.toList());
            hashSet.addAll(collect);
            hashSet.add(key);
        });
        //查询上面所有id对应的药材编码
        List<Long> ids = new ArrayList<>(hashSet);
        if (ids.size() != 0) {
            List<ChineseMedicineConflictEntity> chineseMedicineConflicts = new ArrayList<>();
            List<ChineseMedicineEntity> chineseMedicineEntities = chineseMedicineService.getByIds(ids);
            chineseMedicineEntities.forEach( chineseMedicineEntity ->
                    codeMedicineCodeMap.putIfAbsent(chineseMedicineEntity.getId(),chineseMedicineEntity.getMedicineCode()));
            hashMap.forEach( (key, value) -> {
                String medicineCode = codeMedicineCodeMap.get(key);
                ChineseMedicineConflictEntity productChineseMedicineConflict = new ChineseMedicineConflictEntity();
                productChineseMedicineConflict.setMedicineCode(medicineCode);
                String collect = value.stream().map(a -> {
                    Long id = a.getChineseMedicineConflictId();
                    return codeMedicineCodeMap.get(id);
                }).collect(Collectors.joining(","));
                productChineseMedicineConflict.setMedicineConflictCodes(collect);
                chineseMedicineConflicts.add(productChineseMedicineConflict);
            });
            super.saveBatch(chineseMedicineConflicts);
        }

    }

}
