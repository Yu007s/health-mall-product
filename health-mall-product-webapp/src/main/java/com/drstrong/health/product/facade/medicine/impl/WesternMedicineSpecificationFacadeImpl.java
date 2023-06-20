package com.drstrong.health.product.facade.medicine.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.facade.medicine.MedicineWarehouseBaseFacade;
import com.drstrong.health.product.model.dto.medicine.MedicineWarehouseBaseDTO;
import com.drstrong.health.product.model.dto.medicine.WesternMedicineBaseDTO;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.service.medicine.WesternMedicineSpecificationsService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 西药 facade （具体到规格）
 *
 * @author liuqiuyi
 * @date 2023/6/20 11:27
 */
@Slf4j
@Service
public class WesternMedicineSpecificationFacadeImpl implements MedicineWarehouseBaseFacade {
    @Resource
    WesternMedicineSpecificationsService westernMedicineSpecificationsService;

    /**
     * 返回每个处理类的商品类型
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:41
     */
    @Override
    public ProductTypeEnum queryProductType() {
        return ProductTypeEnum.MEDICINE;
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
        Page<WesternMedicineSpecificationsEntity> westernMedicineEntityPage = westernMedicineSpecificationsService.pageQueryByRequest(medicineWarehouseQueryRequest);
        // 2.组装结果返回
        if (Objects.isNull(westernMedicineEntityPage) || CollectionUtil.isEmpty(westernMedicineEntityPage.getRecords())) {
            log.info("未查询到西药信息，查询的参数为：{}", JSONUtil.toJsonStr(medicineWarehouseQueryRequest));
            return PageVO.newBuilder().result(Lists.newArrayList()).totalCount(0).pageNo(medicineWarehouseQueryRequest.getPageNo()).pageSize(medicineWarehouseQueryRequest.getPageSize()).build();
        }
        List<WesternMedicineBaseDTO> westernMedicineBaseDTOList = westernMedicineEntityPage.getRecords().stream().map(westernMedicineSpecificationsEntity -> {
            WesternMedicineBaseDTO westernMedicineBaseDTO = WesternMedicineBaseDTO.builder()
                    .medicineCode(westernMedicineSpecificationsEntity.getSpecCode())
                    .medicineName(westernMedicineSpecificationsEntity.getSpecName())
                    .build();
            westernMedicineBaseDTO.setProductType(queryProductType().getCode());
            westernMedicineBaseDTO.setProductTypeName(queryProductType().getValue());
            return westernMedicineBaseDTO;
        }).collect(Collectors.toList());

        return PageVO.newBuilder().result(westernMedicineBaseDTOList)
                .totalCount((int) westernMedicineEntityPage.getTotal())
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
        return null;
    }
}
