package com.drstrong.health.product.facade.medicine.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.facade.medicine.MedicineWarehouseBaseFacade;
import com.drstrong.health.product.model.dto.medicine.AgreementPrescriptionMedicineBaseDTO;
import com.drstrong.health.product.model.dto.medicine.MedicineWarehouseBaseDTO;
import com.drstrong.health.product.model.entity.medication.AgreementPrescriptionMedicineEntity;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.service.medicine.AgreementPrescriptionMedicineService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 协定方 facade
 *
 * @author liuqiuyi
 * @date 2023/6/20 11:27
 */
@Slf4j
@Service
public class AgreementPrescriptionMedicineFacadeImpl implements MedicineWarehouseBaseFacade {
    @Resource
    AgreementPrescriptionMedicineService agreementPrescriptionMedicineService;

    /**
     * 返回每个处理类的商品类型
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:41
     */
    @Override
    public ProductTypeEnum queryProductType() {
        return ProductTypeEnum.AGREEMENT;
    }

    /**
     * 根据入参分页查询药材库信息
     *
     * @param medicineWarehouseQueryRequest
     * @author liuqiuyi
     * @date 2023/6/20 10:29
     */
    @Override
    public <T extends MedicineWarehouseBaseDTO> PageVO<T> pageQuery(MedicineWarehouseQueryRequest medicineWarehouseQueryRequest) {
        // 1.调用接口查询
        Page<AgreementPrescriptionMedicineEntity> agreementPrescriptionMedicineEntityPage = agreementPrescriptionMedicineService.pageQueryByRequest(medicineWarehouseQueryRequest);
        // 2.组装结果返回
        if (Objects.isNull(agreementPrescriptionMedicineEntityPage) || CollectionUtil.isEmpty(agreementPrescriptionMedicineEntityPage.getRecords())) {
            log.info("未查询到协定方信息，查询的参数为：{}", JSONUtil.toJsonStr(medicineWarehouseQueryRequest));
            return PageVO.newBuilder().result(Lists.newArrayList()).totalCount(0).pageNo(medicineWarehouseQueryRequest.getPageNo()).pageSize(medicineWarehouseQueryRequest.getPageSize()).build();
        }
        List<AgreementPrescriptionMedicineBaseDTO> agreementPrescriptionMedicineBaseDTOList = agreementPrescriptionMedicineEntityPage.getRecords().stream().map(this::buildAgreementPrescriptionMedicineBaseDTO).collect(Collectors.toList());

        return PageVO.newBuilder().result(agreementPrescriptionMedicineBaseDTOList)
                .totalCount((int) agreementPrescriptionMedicineEntityPage.getTotal())
                .pageNo(medicineWarehouseQueryRequest.getPageNo())
                .pageSize(medicineWarehouseQueryRequest.getPageSize())
                .build();
    }

    /**
     * 根据code查询
     *
     * @param code
     * @author liuqiuyi
     * @date 2023/6/20 14:55
     */
    @Override
    public MedicineWarehouseBaseDTO queryByCode(String code) {
        AgreementPrescriptionMedicineEntity agreementPrescriptionMedicineEntity = agreementPrescriptionMedicineService.queryByCode(code);
        if (ObjectUtil.isNull(agreementPrescriptionMedicineEntity)) {
            log.info("未查询到协定方信息，查询的参数为：{}", code);
            return null;
        }
        return buildAgreementPrescriptionMedicineBaseDTO(agreementPrescriptionMedicineEntity);
    }

    private AgreementPrescriptionMedicineBaseDTO buildAgreementPrescriptionMedicineBaseDTO(AgreementPrescriptionMedicineEntity agreementPrescriptionMedicineEntity) {
        AgreementPrescriptionMedicineBaseDTO agreementPrescriptionMedicineBaseDTO = AgreementPrescriptionMedicineBaseDTO.builder()
                .medicineId(agreementPrescriptionMedicineEntity.getId())
                .medicineCode(agreementPrescriptionMedicineEntity.getMedicineCode())
                .medicineName(agreementPrescriptionMedicineEntity.getMedicineName())
                .fullName(agreementPrescriptionMedicineEntity.getFullName())
                .build();
        agreementPrescriptionMedicineBaseDTO.setProductType(queryProductType().getCode());
        agreementPrescriptionMedicineBaseDTO.setProductTypeName(queryProductType().getValue());
        return agreementPrescriptionMedicineBaseDTO;
    }
}
