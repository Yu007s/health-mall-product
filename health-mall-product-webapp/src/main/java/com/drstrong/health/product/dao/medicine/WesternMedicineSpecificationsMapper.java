package com.drstrong.health.product.dao.medicine;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSpecVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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

    /**
     * 按条件分页查询药品信息
     *
     * @author liuqiuyi
     * @date 2023/6/20 14:07
     */
    Page<WesternMedicineSpecificationsEntity> pageQueryByRequest(Page<WesternMedicineSpecificationsEntity> entityPage, @Param("queryParam") MedicineWarehouseQueryRequest queryParam);

    /**
     * 根据规格编码，查询用法用量信息
     *
     * @author liuqiuyi
     * @date 2023/7/11 11:12
     */
    List<MedicineUsageDTO> queryMedicineUsageBySpecCodes(@Param("specCodes")Set<String> specCodes);
}
