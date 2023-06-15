package com.drstrong.health.product.dao.medicine;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.medicine.WesternMedicineExcelVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSimpleInfoVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 西/成药品库 Mapper 接口
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@Mapper
public interface WesternMedicineMapper extends BaseMapper<WesternMedicineEntity> {


    /**
     * 分页查询药品信息
     *
     * @param medicinePage
     * @param queryParam
     * @return
     */
    Page<WesternMedicineVO> queryMedicinePageList(Page<WesternMedicineVO> medicinePage, @Param("queryParam") WesternMedicineRequest queryParam);

    /**
     * 查询西药信息
     *
     * @param id
     * @return
     */
    WesternMedicineSimpleInfoVO queryMedicineSimpleInfo(@Param("id") Long id);


    /**
     * 西药excel数据查询
     *
     * @param request
     * @return
     */
    List<WesternMedicineExcelVO> queryMedicineExcelData(@Param("queryParam") WesternMedicineRequest request);
}


