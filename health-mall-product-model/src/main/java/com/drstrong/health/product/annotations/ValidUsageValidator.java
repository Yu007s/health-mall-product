package com.drstrong.health.product.annotations;

import cn.hutool.core.util.ObjectUtil;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineSpecRequest;
import com.drstrong.health.product.model.request.medicine.MedicineUsageRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidUsageValidator implements ConstraintValidator<ValidUsage, AddOrUpdateMedicineSpecRequest> {
    @Override
    public boolean isValid(AddOrUpdateMedicineSpecRequest request, ConstraintValidatorContext context) {
        MedicineUsageRequest medicineUsage = request.getMedicineUsage();
        if (request == null || medicineUsage == null) {
            return true;
        }
        if (request.getUseUsageDosage() == 1) {
            // 在useUsageDosage为1时校验medicineUsage参数
            if (ObjectUtil.hasEmpty(
                    medicineUsage.getSpecificationsId(),
                    medicineUsage.getMedicineType(),
                    medicineUsage.getMedicationFrequency(),
                    medicineUsage.getEachDosageCount(),
                    medicineUsage.getEachDoseUnit(),
                    medicineUsage.getUsageTime(),
                    medicineUsage.getUsageMethod()
            )) {
                // 添加校验错误信息
                context.buildConstraintViolationWithTemplate("Invalid medicine usage")
                        .addPropertyNode("medicineUsage")
                        .addConstraintViolation();
                return false;
            }
        }
        // 默认通过校验
        return true;
    }
}
