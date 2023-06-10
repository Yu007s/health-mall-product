package com.drstrong.health.product.service.medicine.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.constants.MedicineConstant;
import com.drstrong.health.product.dao.medicine.WesternMedicineSpecificationsMapper;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineSpecRequest;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSpecInfoVO;
import com.drstrong.health.product.service.medicine.MedicineUsageService;
import com.drstrong.health.product.service.medicine.WesternMedicineService;
import com.drstrong.health.product.service.medicine.WesternMedicineSpecificationsService;
import com.naiterui.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 西药药品规格 服务实现类
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@Service
public class WesternMedicineSpecificationsServiceImpl extends ServiceImpl<WesternMedicineSpecificationsMapper, WesternMedicineSpecificationsEntity> implements WesternMedicineSpecificationsService {

    @Autowired
    private WesternMedicineService westernMedicineService;

    @Autowired
    private MedicineUsageService medicineUsageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdateMedicineSpec(AddOrUpdateMedicineSpecRequest specRequest) {
        WesternMedicineEntity westernMedicine = westernMedicineService.queryByMedicineCode(specRequest.getMedicineCode());
        Assert.notNull(westernMedicine, "西药不存在");
        WesternMedicineSpecificationsEntity specifications = BeanUtil.copyProperties(specRequest, WesternMedicineSpecificationsEntity.class);
        specifications.setSpecImageInfo(JSONUtil.parse(specRequest.getImageInfoList()).toString());
        if (ObjectUtil.isNull(specRequest.getId())) {
            //新增
            specifications.setSpecCode(generateMedicineSpecCode(westernMedicine.getId(), westernMedicine.getMedicineCode()));
        }
        saveOrUpdate(specifications);
        medicineUsageService.saveOrUpdateUsage(specRequest);
        return specifications.getId();
    }

    @Override
    public WesternMedicineSpecInfoVO queryMedicineSpecDetailInfo(Long id) {
        WesternMedicineSpecInfoVO vo = new WesternMedicineSpecInfoVO();
        LambdaQueryWrapper<WesternMedicineSpecificationsEntity> queryWrapper = new LambdaQueryWrapper<WesternMedicineSpecificationsEntity>()
                .eq(WesternMedicineSpecificationsEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(WesternMedicineSpecificationsEntity::getId, id);
        WesternMedicineSpecificationsEntity specifications = baseMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNotNull(specifications)) {
            vo = BeanUtil.copyProperties(specifications, WesternMedicineSpecInfoVO.class);
        }
        return vo;
    }

    private String generateMedicineSpecCode(Long medicineId, String medicineCode) {
        // 生成规则：药品编码M开头 + 建码日期六位：年后两位+月份+日期（190520）+ 5位顺序码    举例：M19052000001
        long serialNumber = RedisUtil.keyOps().incr(MedicineConstant.SPEC_SERIAL_NUMBER_REDIS_KEY + medicineId);
        return medicineCode + "-" + serialNumber;
    }

}
