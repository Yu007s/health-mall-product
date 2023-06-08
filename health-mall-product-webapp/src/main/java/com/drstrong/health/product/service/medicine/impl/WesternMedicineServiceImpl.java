package com.drstrong.health.product.service.medicine.impl;

import java.time.LocalDateTime;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.common.utils.DateUtil;
import com.drstrong.health.product.dao.medicine.WesternMedicineMapper;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineVO;
import com.drstrong.health.product.service.medicine.WesternMedicineInstructionsService;
import com.drstrong.health.product.service.medicine.WesternMedicineService;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import com.naiterui.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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


    @Autowired
    private WesternMedicineInstructionsService westernMedicineInstructionsService;

    @Autowired
    private OperationLogSendUtil operationLogSendUtil;


    //旧redis key
    private static final String SERIAL_NUMBER_REDIS_KEY = "naiterui-b2c|product_serial_number";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateMedicine(AddOrUpdateMedicineRequest addOrUpdateMedicineRequest) {
        WesternMedicineEntity westernMedicine = queryByMedicineCode(addOrUpdateMedicineRequest.getMedicineCode());
        WesternMedicineEntity westernMedicineEntity = buildWesternMedicineEntity(addOrUpdateMedicineRequest);
        if (ObjectUtil.isNotNull(westernMedicine)) {
            westernMedicineEntity.setId(westernMedicine.getId());
        }
        //保存修改西药信息
        saveOrUpdate(westernMedicineEntity);
        //保存修改西药说明
        addOrUpdateMedicineRequest.getMedicineInstructions().setMedicineId(westernMedicineEntity.getId());
        westernMedicineInstructionsService.saveOrUpdateInstructions(addOrUpdateMedicineRequest.getMedicineInstructions());
//        operationLogSendUtil.sendOperationLog();
    }

    @Override
    public void queryMedicineDetailInfo(Long id) {

    }

    @Override
    public WesternMedicineEntity queryByMedicineCode(String medicineCode) {
        if (StrUtil.isBlank(medicineCode)) {
            return null;
        }
        LambdaQueryWrapper<WesternMedicineEntity> queryWrapper = new LambdaQueryWrapper<WesternMedicineEntity>()
                .eq(WesternMedicineEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(WesternMedicineEntity::getMedicineCode, medicineCode);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public PageVO<WesternMedicineVO> queryMedicinePageList(WesternMedicineRequest request) {
        return null;
    }


    private WesternMedicineEntity buildWesternMedicineEntity(AddOrUpdateMedicineRequest medicineRequest) {
        medicineRequest.constructFullName();
        WesternMedicineEntity westernMedicine = BeanUtil.copyProperties(medicineRequest, WesternMedicineEntity.class);
        westernMedicine.setMedicineCode(generateMedicineCode());
        westernMedicine.setMedicineClassificationInfo(JSON.toJSONString(medicineRequest.getMedicineClassificationInfo()));
        if (ObjectUtil.isNull(medicineRequest.getId())) {
            westernMedicine.setCreatedAt(LocalDateTime.now());
            westernMedicine.setCreatedBy(medicineRequest.getUserId());
        }
        westernMedicine.setChangedAt(LocalDateTime.now());
        westernMedicine.setChangedBy(medicineRequest.getUserId());
        return westernMedicine;
    }


    private String generateMedicineCode() {
        // 生成规则：药品编码M开头 + 建码日期六位：年后两位+月份+日期（190520）+ 5位顺序码    举例：M19052000001
        StringBuilder number = new StringBuilder();
        number.append("M");
        number.append(DateUtil.formatDate(new Date(), "yyMMdd"));
        long serialNumber = RedisUtil.keyOps().incr(SERIAL_NUMBER_REDIS_KEY);
        number.append(String.format("%05d", serialNumber));
        return number.toString();
    }
}
