package com.drstrong.health.product.service.medicine;

import cn.hutool.core.lang.Pair;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.entity.medication.AgreementPrescriptionMedicineEntity;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateAgreementRequest;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionInfoVO;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionSimpleInfoVO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 协定方(预制) 服务类
 * </p>
 *
 * @author zzw
 * @since 2023-06-13
 */
public interface AgreementPrescriptionMedicineService extends IService<AgreementPrescriptionMedicineEntity> {

    /**
     * 保存/修改协定方
     *
     * @param request
     */
    Pair<Long,String> saveOrUpdateAgreementPrescription(AddOrUpdateAgreementRequest request);


    /**
     * 协定方详情
     *
     * @param id
     * @return
     */
    AgreementPrescriptionInfoVO queryAgreementPrescriptionInfo(Long id);


    /**
     * 协定方分页列表
     *
     * @param request
     * @return
     */
    PageVO<AgreementPrescriptionSimpleInfoVO> queryAgreementPrescriptionPageInfo(WesternMedicineRequest request);

    /**
     * 根据条件查询协定方规格信息
     *
     * @author liuqiuyi
     * @date 2023/6/20 13:53
     */
    Page<AgreementPrescriptionMedicineEntity> pageQueryByRequest(MedicineWarehouseQueryRequest medicineWarehouseQueryRequest);

    /**
     * 根据药材code查询
     *
     * @author liuqiuyi
     * @date 2023/6/20 19:13
     */
    AgreementPrescriptionMedicineEntity queryByCode(String medicineCode);

    /**
     * 根据编码，查询用法用量信息
     *
     * @author liuqiuyi
     * @date 2023/7/11 11:12
     */
    List<MedicineUsageDTO> queryMedicineUsageByMedicineCodes(Set<String> medicineCodes);

    List<AgreementPrescriptionMedicineEntity> queryByCodeList(List<String> medicineCode);
}
