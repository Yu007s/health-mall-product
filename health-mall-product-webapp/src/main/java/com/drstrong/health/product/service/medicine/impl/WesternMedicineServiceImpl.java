package com.drstrong.health.product.service.medicine.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.medicine.WesternMedicineMapper;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;
import com.drstrong.health.product.service.medicine.WesternMedicineService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 西/成药品库 服务实现类
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@Service
public class WesternMedicineServiceImpl extends ServiceImpl<WesternMedicineMapper, WesternMedicineEntity> implements WesternMedicineService {

    @Override
    public void saveOrUpdateMedicine() {

    }
}
