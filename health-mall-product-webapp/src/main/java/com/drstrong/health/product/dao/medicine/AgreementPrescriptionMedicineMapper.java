package com.drstrong.health.product.dao.medicine;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.entity.medication.AgreementPrescriptionMedicineEntity;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionInfoVO;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionSimpleInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 协定方(预制) Mapper 接口
 * </p>
 *
 * @author zzw
 * @since 2023-06-13
 */
@Mapper
public interface AgreementPrescriptionMedicineMapper extends BaseMapper<AgreementPrescriptionMedicineEntity> {

    /**
     * 协定方详情
     *
     * @param id
     * @return
     */
    AgreementPrescriptionInfoVO queryAgreementPrescriptionInfo(Long id);


    /**
     * 分页查询
     *
     * @param medicinePage
     * @param queryParam
     * @return
     */
    Page<AgreementPrescriptionSimpleInfoVO> queryPageList(Page<AgreementPrescriptionSimpleInfoVO> medicinePage, @Param("queryParam") WesternMedicineRequest queryParam);

    Page<AgreementPrescriptionMedicineEntity> pageQueryByRequest(Page<AgreementPrescriptionMedicineEntity> entityPage, @Param("queryParam") MedicineWarehouseQueryRequest queryParam);


    /**
     * 根据编码，查询用法用量信息
     *
     * @author liuqiuyi
     * @date 2023/7/11 11:12
     */
    List<MedicineUsageDTO> queryMedicineUsageByMedicineCodes(@Param("medicineCodes") Set<String> medicineCodes);
}
