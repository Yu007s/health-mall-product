package com.drstrong.health.product.service.medicine.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.constants.MedicineConstant;
import com.drstrong.health.product.dao.medicine.MedicineUsageMapper;
import com.drstrong.health.product.model.entity.medication.MedicineUsageEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineSpecRequest;
import com.drstrong.health.product.model.request.medicine.MedicineUsageRequest;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSpecInfoVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultStatus;
import com.drstrong.health.product.service.medicine.MedicineUsageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 药品规格用法用量 服务实现类
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@Service
public class MedicineUsageServiceImpl extends ServiceImpl<MedicineUsageMapper, MedicineUsageEntity> implements MedicineUsageService {


    @Override
    public void saveOrUpdateUsage(AddOrUpdateMedicineSpecRequest request) {
        Integer useUsageDosage = request.getUseUsageDosage();
        MedicineUsageRequest usageRequest = request.getMedicineUsage();
        if (ObjectUtil.isNotNull(request.getId())) {
            usageRequest.setSpecificationsId(request.getId());
        }
        //未设置用法用量则删除
        if (ObjectUtil.isNotNull(useUsageDosage) && ObjectUtil.equals(useUsageDosage, MedicineConstant.NO_USE_USAGE_DOSAGE)) {
            cancelMedicineUsage(usageRequest.getSpecificationsId(), usageRequest.getMedicineType());
            return;
        }
        checkParam(request.getMedicineUsage());
        MedicineUsageEntity medicineUsage = getMedicineUsageBySpecId(usageRequest.getSpecificationsId(), usageRequest.getMedicineType());
        MedicineUsageEntity medicineUsageEntity = BeanUtil.copyProperties(request.getMedicineUsage(), MedicineUsageEntity.class);
        medicineUsageEntity.setMedicineType(1);
        medicineUsageEntity.setChangedAt(LocalDateTime.now());
        medicineUsageEntity.setChangedBy(request.getUserId());
        if (ObjectUtil.isNull(medicineUsage)) {
            //新增
            medicineUsageEntity.setCreatedAt(LocalDateTime.now());
            medicineUsageEntity.setCreatedBy(request.getUserId());
        } else {
            //修改
            medicineUsageEntity.setId(medicineUsage.getId());
        }
        saveOrUpdate(medicineUsageEntity);
    }

    @Override
    public void cancelMedicineUsage(Long specificationsId, Integer medicineType) {
        MedicineUsageEntity medicineUsage = getMedicineUsageBySpecId(specificationsId, medicineType);
        if (ObjectUtil.isNull(medicineUsage)) {
            return;
        }
        LambdaUpdateWrapper<MedicineUsageEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MedicineUsageEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(MedicineUsageEntity::getSpecificationsId, specificationsId)
                .eq(MedicineUsageEntity::getMedicineType, medicineType).set(MedicineUsageEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode());
        this.update(updateWrapper);
    }

    @Override
    public MedicineUsageEntity getMedicineUsageBySpecId(Long specificationsId, Integer medicineType) {
        LambdaQueryWrapper<MedicineUsageEntity> queryWrapper = new LambdaQueryWrapper<MedicineUsageEntity>()
                .eq(MedicineUsageEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(MedicineUsageEntity::getSpecificationsId, specificationsId)
                .eq(MedicineUsageEntity::getMedicineType, medicineType);
        MedicineUsageEntity medicineUsage = this.getOne(queryWrapper);
        return medicineUsage;
    }

    public void checkParam(MedicineUsageRequest medicineUsage) {
        if (ObjectUtil.hasEmpty(
                medicineUsage.getMedicationFrequency(),
                medicineUsage.getEachDosageCount(),
                medicineUsage.getEachDoseUnit(),
                medicineUsage.getUsageTime(),
                medicineUsage.getUsageMethod()
        )) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "规格用法用量参数不能为空！");
        }

    }
}
