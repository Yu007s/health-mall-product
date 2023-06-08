package com.drstrong.health.product.service.medicine.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.medicine.WesternMedicineInstructionsMapper;
import com.drstrong.health.product.model.entity.medication.WesternMedicineInstructionsEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.MedicineInstructionsRequest;
import com.drstrong.health.product.service.medicine.WesternMedicineInstructionsService;
import org.springframework.stereotype.Service;

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
    public void saveOrUpdateInstructions(MedicineInstructionsRequest medicineInstructionsRequest) {
        WesternMedicineInstructionsEntity medicineInstructions = BeanUtil.copyProperties(medicineInstructionsRequest, WesternMedicineInstructionsEntity.class);
        WesternMedicineInstructionsEntity instructions = queryByMedicineId(medicineInstructionsRequest.getMedicineId());
        if (ObjectUtil.isNotNull(instructions)) {
            medicineInstructions.setId(instructions.getId());
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
}
