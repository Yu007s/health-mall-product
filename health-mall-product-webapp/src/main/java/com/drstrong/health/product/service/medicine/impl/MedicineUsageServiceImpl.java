package com.drstrong.health.product.service.medicine.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.constants.MedicineConstant;
import com.drstrong.health.product.dao.medicine.MedicineUsageMapper;
import com.drstrong.health.product.model.entity.medication.MedicineUsageEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineSpecRequest;
import com.drstrong.health.product.model.request.medicine.MedicineUsageRequest;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSpecInfoVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultStatus;
import com.drstrong.health.product.service.medicine.MedicineUsageService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MedicineUsageServiceImpl extends ServiceImpl<MedicineUsageMapper, MedicineUsageEntity> implements MedicineUsageService {


    @Override
    public void saveOrUpdateUsage(MedicineUsageRequest medicineUsageRequest) {
        log.info("invoke saveOrUpdateUsage() param:{}", JSONUtil.toJsonStr(medicineUsageRequest));
        Assert.isTrue(ObjectUtil.hasEmpty(medicineUsageRequest.getRelationId(),
                medicineUsageRequest.getRelationType()),
                () -> new BusinessException(ErrorEnums.PARAM_TYPE_IS_ERROR));
        Integer useUsageDosage = medicineUsageRequest.getUseUsageDosage();
        //未设置用法用量则删除
        if (ObjectUtil.isNotNull(useUsageDosage) && ObjectUtil.equals(useUsageDosage, MedicineConstant.NO_USE_USAGE_DOSAGE)) {
            cancelMedicineUsage(medicineUsageRequest.getRelationId(), medicineUsageRequest.getRelationType());
            return;
        }
        checkParam(medicineUsageRequest);
        MedicineUsageEntity medicineUsage = getMedicineUsageBySpecId(medicineUsageRequest.getRelationId(), medicineUsageRequest.getRelationType());
        MedicineUsageEntity medicineUsageEntity = BeanUtil.copyProperties(medicineUsageRequest, MedicineUsageEntity.class);
        medicineUsageEntity.setRelationType(medicineUsageRequest.getRelationType());
        medicineUsageEntity.setChangedAt(LocalDateTime.now());
        medicineUsageEntity.setChangedBy(medicineUsageRequest.getUserId());
        if (ObjectUtil.isNull(medicineUsage)) {
            //新增
            medicineUsageEntity.setCreatedAt(LocalDateTime.now());
            medicineUsageEntity.setCreatedBy(medicineUsageRequest.getUserId());
        } else {
            //修改
            medicineUsageEntity.setId(medicineUsage.getId());
        }
        saveOrUpdate(medicineUsageEntity);
    }

    @Override
    public void cancelMedicineUsage(Long relationId, Integer relationType) {
        MedicineUsageEntity medicineUsage = getMedicineUsageBySpecId(relationId, relationType);
        if (ObjectUtil.isNull(medicineUsage)) {
            return;
        }
        LambdaUpdateWrapper<MedicineUsageEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MedicineUsageEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(MedicineUsageEntity::getRelationId, relationId)
                .eq(MedicineUsageEntity::getRelationType, relationType).set(MedicineUsageEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode());
        this.update(updateWrapper);
    }

    @Override
    public MedicineUsageEntity getMedicineUsageBySpecId(Long relationId, Integer relationType) {
        LambdaQueryWrapper<MedicineUsageEntity> queryWrapper = new LambdaQueryWrapper<MedicineUsageEntity>()
                .eq(MedicineUsageEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(MedicineUsageEntity::getRelationId, relationId)
                .eq(MedicineUsageEntity::getRelationType, relationType);
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
