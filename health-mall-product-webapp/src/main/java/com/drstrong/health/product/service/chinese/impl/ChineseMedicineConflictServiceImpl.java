package com.drstrong.health.product.service.chinese.impl;

import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.drstrong.health.product.dao.chinese.ChineseMedicineConflictMapper;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineConflictEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 中药材相反药材表服务
 *
 * @Author xieYueFeng
 * @Date 2022/07/27/19:32
 */
@Service
public class ChineseMedicineConflictServiceImpl extends CustomServiceImpl<ChineseMedicineConflictMapper, ChineseMedicineConflictEntity>
        implements ChineseMedicineConflictService {
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
}
