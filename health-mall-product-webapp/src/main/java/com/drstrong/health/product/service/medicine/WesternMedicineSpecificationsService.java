package com.drstrong.health.product.service.medicine;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineSpecRequest;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSimpleInfoVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSpecInfoVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSpecVO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 西药药品规格 服务类
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
public interface WesternMedicineSpecificationsService extends IService<WesternMedicineSpecificationsEntity> {

    /**
     * 保存/修改西药
     *
     * @param specRequest
     */
    Long saveOrUpdateMedicineSpec(AddOrUpdateMedicineSpecRequest specRequest);

    /**
     * 规格详情
     *
     * @param id
     * @return
     */
    WesternMedicineSpecInfoVO queryMedicineSpecDetailInfo(Long id);

    /**
     * 分页查询规格
     *
     * @param request
     * @return
     */
    WesternMedicineSimpleInfoVO queryMedicineSpecByPage(WesternMedicineRequest request);


    /**
     * 规格分页查询
     *
     * @param request
     * @return
     */
    PageVO<WesternMedicineSpecVO> queryMedicineSpecInfoPage(WesternMedicineRequest request);

    /**
     * 按条件分页查询规格信息，返回基本信息
     *
     * @author liuqiuyi
     * @date 2023/6/20 14:05
     */
    Page<WesternMedicineSpecificationsEntity> pageQueryByRequest(MedicineWarehouseQueryRequest medicineWarehouseQueryRequest);

    /**
     * 根据code查询西药规格
     *
     * @author liuqiuyi
     * @date 2023/6/20 19:18
     */
    WesternMedicineSpecificationsEntity queryByCode(String code);

    /**
     * 根据code查询西药规格
     *
     * @author liuqiuyi
     * @date 2023/6/20 19:18
     */
    List<WesternMedicineSpecificationsEntity> queryByCodeList(List<String> codeList);

    /**
     * 根据规格编码，查询用法用量信息
     *
     * @author liuqiuyi
     * @date 2023/7/11 11:12
     */
    List<MedicineUsageDTO> queryMedicineUsageBySpecCodes(Set<String> specCodes);
}
