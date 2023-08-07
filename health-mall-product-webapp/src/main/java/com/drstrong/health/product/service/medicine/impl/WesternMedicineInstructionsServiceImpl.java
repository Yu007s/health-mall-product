package com.drstrong.health.product.service.medicine.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.medicine.WesternMedicineInstructionsMapper;
import com.drstrong.health.product.model.entity.medication.WesternMedicineInstructionsEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.MedicineInstructionsRequest;
import com.drstrong.health.product.service.medicine.WesternMedicineInstructionsService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 西/成药品说明 服务实现类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2023-06-07
 */
@Service
public class WesternMedicineInstructionsServiceImpl extends ServiceImpl<WesternMedicineInstructionsMapper, WesternMedicineInstructionsEntity> implements WesternMedicineInstructionsService {


    @Override
    public void saveOrUpdateInstructions(AddOrUpdateMedicineRequest medicineRequest) {
        MedicineInstructionsRequest instructionsRequest = medicineRequest.getMedicineInstructions();
        WesternMedicineInstructionsEntity medicineInstructions = BeanUtil.copyProperties(instructionsRequest, WesternMedicineInstructionsEntity.class);
        WesternMedicineInstructionsEntity instructions = queryByMedicineId(instructionsRequest.getMedicineId());
        medicineInstructions.setChangedBy(medicineRequest.getUserId());
        medicineInstructions.setChangedAt(LocalDateTime.now());
        if (ObjectUtil.isNotNull(instructions)) {
            medicineInstructions.setId(instructions.getId());
        } else {
            medicineInstructions.setCreatedBy(medicineRequest.getUserId());
            medicineInstructions.setCreatedAt(LocalDateTime.now());
        }
        //保存修改西药说明
        saveOrUpdate(medicineInstructions);
    }

    @Override
    public WesternMedicineInstructionsEntity queryByMedicineId(Long medicineId) {
        if (ObjectUtil.isNull(medicineId)) {
            return null;
        }
        LambdaQueryWrapper<WesternMedicineInstructionsEntity> queryWrapper = new LambdaQueryWrapper<WesternMedicineInstructionsEntity>()
                .eq(WesternMedicineInstructionsEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(WesternMedicineInstructionsEntity::getMedicineId, medicineId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<WesternMedicineInstructionsEntity> queryByMedicineIdList(List<Long> medicineIdList) {
        if (CollectionUtil.isEmpty(medicineIdList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<WesternMedicineInstructionsEntity> queryWrapper = new LambdaQueryWrapper<WesternMedicineInstructionsEntity>()
                .eq(WesternMedicineInstructionsEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(WesternMedicineInstructionsEntity::getMedicineId, medicineIdList);
        return baseMapper.selectList(queryWrapper);
    }
}
