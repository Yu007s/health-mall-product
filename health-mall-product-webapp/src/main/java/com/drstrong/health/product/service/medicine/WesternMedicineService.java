package com.drstrong.health.product.service.medicine;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.category.CategoryProductVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineExcelVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineInfoVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineLogVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineVO;

import java.util.List;

/**
 * <p>
 * 西/成药品库 服务类
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
public interface WesternMedicineService extends IService<WesternMedicineEntity> {

    /**
     * 保存/修改西药
     *
     * @param addOrUpdateMedicineRequest
     */
    Long saveOrUpdateMedicine(AddOrUpdateMedicineRequest addOrUpdateMedicineRequest);


    /**
     * 查询西药详情信息
     *
     * @param id
     */
    WesternMedicineInfoVO queryMedicineDetailInfo(Long id);


    /**
     * 根据 medicineCode查询药品
     *
     * @param medicineCode
     * @return
     */
    WesternMedicineEntity queryByMedicineCode(String medicineCode);


    /**
     * 根据 medicineId查询药品
     *
     * @param medicineId
     * @return
     */
    WesternMedicineEntity queryByMedicineId(Long medicineId);


    /**
     * 西药分页列表
     *
     * @param request
     * @return
     */
    PageVO<WesternMedicineVO> queryMedicinePageList(WesternMedicineRequest request);


    /**
     * 西药操作日志分页列表
     *
     * @param westernMedicineRequest
     * @return
     */
    PageVO<WesternMedicineLogVO> queryMedicineOperationLogByPage(WesternMedicineRequest westernMedicineRequest);


    /**
     * 西药excel导出数据
     * @param request
     * @return
     */
    List<WesternMedicineExcelVO> queryMedicineExcelData(WesternMedicineRequest request);

    List<WesternMedicineEntity> queryByMedicineCodeList(List<String> medicineCodes);
}
