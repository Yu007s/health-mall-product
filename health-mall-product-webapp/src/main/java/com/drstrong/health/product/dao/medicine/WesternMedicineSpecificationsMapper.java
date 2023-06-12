package com.drstrong.health.product.dao.medicine;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSpecVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 西药药品规格 Mapper 接口
 * </p>
 *
 * @author mybatis plus generator
 * @since 2023-06-07
 */
@Mapper
public interface WesternMedicineSpecificationsMapper extends BaseMapper<WesternMedicineSpecificationsEntity> {


    /**
     * 分页查询药品信息
     *
     * @param specPage
     * @param queryParam
     * @return
     */
    Page<WesternMedicineSpecVO> queryMedicineSpecPageList(Page<WesternMedicineSpecVO> specPage, @Param("queryParam") WesternMedicineRequest queryParam);
}
