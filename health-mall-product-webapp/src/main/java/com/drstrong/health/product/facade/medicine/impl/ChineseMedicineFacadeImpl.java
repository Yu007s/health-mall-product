package com.drstrong.health.product.facade.medicine.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.facade.medicine.MedicineWarehouseBaseFacade;
import com.drstrong.health.product.model.dto.medicine.ChineseMedicineBaseDTO;
import com.drstrong.health.product.model.dto.medicine.MedicineWarehouseBaseDTO;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.medicine.MedicineCodeRequest;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuqiuyi
 * @date 2023/6/20 10:30
 */
@Slf4j
@Service
public class ChineseMedicineFacadeImpl implements MedicineWarehouseBaseFacade {
    @Resource
    ChineseMedicineService chineseMedicineService;

    /**
     * 返回每个处理类的商品类型
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:41
     */
    @Override
    public ProductTypeEnum queryProductType() {
        return ProductTypeEnum.CHINESE;
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
        // 1.调用原有方法，查询数据
        Page<ChineseMedicineEntity> chineseMedicineEntityPage = chineseMedicineService.pageQueryByRequest(medicineWarehouseQueryRequest);
        if (ObjectUtil.isNull(chineseMedicineEntityPage) || CollectionUtil.isEmpty(chineseMedicineEntityPage.getRecords())) {
            log.info("未查询到中药材信息，查询的参数为：{}", JSONUtil.toJsonStr(medicineWarehouseQueryRequest));
            return PageVO.newBuilder().result(Lists.newArrayList()).totalCount(0).pageNo(medicineWarehouseQueryRequest.getPageNo()).pageSize(medicineWarehouseQueryRequest.getPageSize()).build();
        }
        // 2.转换并返回
        List<ChineseMedicineBaseDTO> chineseMedicineBaseDTOList = chineseMedicineEntityPage.getRecords().stream().map(this::buildChineseMedicineBaseDTO).collect(Collectors.toList());
        return PageVO.newBuilder().result(chineseMedicineBaseDTOList).totalCount((int) chineseMedicineEntityPage.getTotal()).pageNo(medicineWarehouseQueryRequest.getPageNo()).pageSize(medicineWarehouseQueryRequest.getPageSize()).build();
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
        ChineseMedicineEntity chineseMedicineEntity = chineseMedicineService.getByMedicineCode(code);
        if (ObjectUtil.isNull(chineseMedicineEntity)) {
            log.info("未查询到中药材信息，查询的参数为：{}", code);
            return null;
        }
        return buildChineseMedicineBaseDTO(chineseMedicineEntity);
    }

    /**
     * 根据code参数查询
     *
     * @param medicineCodeRequest
     * @author liuqiuyi
     * @date 2023/8/3 16:29
     */
    @Override
    public com.drstrong.health.product.model.dto.medicine.v2.MedicineWarehouseBaseDTO queryBaseDtoByTypeAndCode(MedicineCodeRequest medicineCodeRequest) {
        com.drstrong.health.product.model.dto.medicine.v2.MedicineWarehouseBaseDTO medicineWarehouseBaseDTO = new com.drstrong.health.product.model.dto.medicine.v2.MedicineWarehouseBaseDTO();
        medicineWarehouseBaseDTO.setProductType(queryProductType().getCode());
        medicineWarehouseBaseDTO.setProductTypeName(queryProductType().getValue());
        ChineseMedicineEntity chineseMedicineEntity = chineseMedicineService.getByMedicineCode(medicineCodeRequest.getMedicineCode());
        if (ObjectUtil.isNull(chineseMedicineEntity)) {
            log.info("未查询到中药材信息，查询的参数为：{}", JSONUtil.toJsonStr(medicineCodeRequest));
            return medicineWarehouseBaseDTO;
        }
        com.drstrong.health.product.model.dto.medicine.v2.ChineseMedicineBaseDTO chineseMedicineBaseDTO = new com.drstrong.health.product.model.dto.medicine.v2.ChineseMedicineBaseDTO();
        chineseMedicineBaseDTO.setMedicineId(chineseMedicineEntity.getId());
        chineseMedicineBaseDTO.setMedicineCode(chineseMedicineEntity.getMedicineCode());
        chineseMedicineBaseDTO.setMedicineName(chineseMedicineEntity.getMedicineName());
        chineseMedicineBaseDTO.setAliNames(chineseMedicineEntity.getMedicineAlias());
        chineseMedicineBaseDTO.setMaxDosage(chineseMedicineEntity.getMaxDosage());
        medicineWarehouseBaseDTO.setChineseMedicineBaseDTO(chineseMedicineBaseDTO);
        return medicineWarehouseBaseDTO;
    }

    /**
     * 组装返回值
     */
    private ChineseMedicineBaseDTO buildChineseMedicineBaseDTO(ChineseMedicineEntity chineseMedicineEntity) {
        ChineseMedicineBaseDTO chineseMedicineBaseDTO = ChineseMedicineBaseDTO.builder()
                .medicineId(chineseMedicineEntity.getId())
                .medicineCode(chineseMedicineEntity.getMedicineCode())
                .medicineName(chineseMedicineEntity.getMedicineName())
                .aliNames(chineseMedicineEntity.getMedicineAlias())
                .maxDosage(chineseMedicineEntity.getMaxDosage())
                .build();
        chineseMedicineBaseDTO.setProductType(queryProductType().getCode());
        chineseMedicineBaseDTO.setProductTypeName(queryProductType().getValue());
        return chineseMedicineBaseDTO;
    }
}
