package com.drstrong.health.product.service.medicine.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.MedicineConstant;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.dao.medicine.AgreementPrescriptionMedicineMapper;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.entity.medication.AgreementPrescriptionMedicineEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateAgreementRequest;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.MedicineUsageRequest;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionInfoVO;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionSimpleInfoVO;
import com.drstrong.health.product.service.medicine.AgreementPrescriptionMedicineService;
import com.drstrong.health.product.service.medicine.MedicineUsageService;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 协定方(预制) 服务实现类
 * </p>
 *
 * @author zzw
 * @since 2023-06-13
 */
@Slf4j
@Service
public class AgreementPrescriptionMedicineServiceImpl extends ServiceImpl<AgreementPrescriptionMedicineMapper, AgreementPrescriptionMedicineEntity> implements AgreementPrescriptionMedicineService {

    @Autowired
    private MedicineUsageService medicineUsageService;

    @Autowired
    private OperationLogSendUtil operationLogSendUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdateAgreementPrescription(AddOrUpdateAgreementRequest request) {
        AgreementPrescriptionMedicineEntity prescriptionMedicineEntity = buildAgreementPrescriptionEntity(request);
        //保存更新协定方
        saveOrUpdate(prescriptionMedicineEntity);
        MedicineUsageRequest medicineUsage = request.getMedicineUsage();
        if (ObjectUtil.isNull(medicineUsage)) {
            medicineUsage = new MedicineUsageRequest(request.getUseUsageDosage(), prescriptionMedicineEntity.getId(), MedicineConstant.AGREEMENT_PRESCRIPTION_USAGE_DOSAGE);
        } else {
            medicineUsage.setRelationType(MedicineConstant.AGREEMENT_PRESCRIPTION_USAGE_DOSAGE);
            medicineUsage.setRelationId(prescriptionMedicineEntity.getId());
            medicineUsage.setUseUsageDosage(request.getUseUsageDosage());
        }
        medicineUsage.setUserId(request.getUserId());
        medicineUsage.setUserName(request.getUserName());
        //保存更新用法用量
        medicineUsageService.saveOrUpdateUsage(medicineUsage);

        //保存操作日志
        String logJsonStr = JSONUtil.toJsonStr(prescriptionMedicineEntity);
        OperationLog operationLog = OperationLog.buildOperationLog(prescriptionMedicineEntity.getMedicineCode(), OperationLogConstant.SAVE_OR_UPDATE_WESTERN_MEDICINE,
                buildOperateContent(ObjectUtil.isNotNull(request.getId()), prescriptionMedicineEntity.getMedicineCode()), request.getUserId(), request.getUserName(),
                OperateTypeEnum.CMS.getCode(), logJsonStr);
        log.info("协定方操作日志记录,param：{}", JSON.toJSONString(operationLog));
        operationLogSendUtil.sendOperationLog(operationLog);
        return prescriptionMedicineEntity.getId();
    }

    private String buildOperateContent(boolean updateFlag, String medicineCode) {
        String action = updateFlag ? MedicineConstant.UPDATE_AGREEMENT_PRESCRIPTION_MEDICINE : MedicineConstant.SAVE_AGREEMENT_PRESCRIPTION_MEDICINE;
        return String.format("%s_%s", action, medicineCode);
    }

    @Override
    public AgreementPrescriptionInfoVO queryAgreementPrescriptionInfo(Long id) {
        return baseMapper.queryAgreementPrescriptionInfo(id);
    }

    @Override
    public PageVO<AgreementPrescriptionSimpleInfoVO> queryAgreementPrescriptionPageInfo(WesternMedicineRequest request) {
        Page<AgreementPrescriptionSimpleInfoVO> page = baseMapper.queryPageList(new Page<>(request.getPageNo(), request.getPageSize()), request);
        return PageVO.newBuilder().pageNo(request.getPageNo()).pageSize(request.getPageSize()).totalCount((int) page.getTotal()).result(page.getRecords()).build();
    }

    private AgreementPrescriptionMedicineEntity buildAgreementPrescriptionEntity(AddOrUpdateAgreementRequest request) {
        AgreementPrescriptionMedicineEntity prescriptionMedicine = BeanUtil.copyProperties(request, AgreementPrescriptionMedicineEntity.class);
        prescriptionMedicine.setMedicineClassificationInfo(JSON.toJSONString(request.getClassificationInfo()));
        prescriptionMedicine.setImageInfo(JSONUtil.parse(request.getImageInfoList()).toString());
        if (ObjectUtil.isNull(prescriptionMedicine.getId())) {
            prescriptionMedicine.setCreatedAt(LocalDateTime.now());
            prescriptionMedicine.setCreatedBy(request.getUserId());
            prescriptionMedicine.setMedicineCode(UniqueCodeUtils.getNextSpuCode(ProductTypeEnum.AGREEMENT));
        }
        prescriptionMedicine.setChangedAt(LocalDateTime.now());
        prescriptionMedicine.setChangedBy(request.getUserId());
        return prescriptionMedicine;
    }

    /**
     * 根据条件查询协定方规格信息
     *
     * @param medicineWarehouseQueryRequest
     * @author liuqiuyi
     * @date 2023/6/20 13:53
     */
    @Override
    public Page<AgreementPrescriptionMedicineEntity> pageQueryByRequest(MedicineWarehouseQueryRequest medicineWarehouseQueryRequest) {
        Page<AgreementPrescriptionMedicineEntity> entityPage = new Page<>(medicineWarehouseQueryRequest.getPageNo(), medicineWarehouseQueryRequest.getPageSize());
        return baseMapper.pageQueryByRequest(entityPage, medicineWarehouseQueryRequest);
    }
}
