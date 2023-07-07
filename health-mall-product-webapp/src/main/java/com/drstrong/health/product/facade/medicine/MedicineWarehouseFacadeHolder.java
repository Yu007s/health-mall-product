package com.drstrong.health.product.facade.medicine;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrFormatter;
import com.drstrong.health.common.exception.BusinessException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 药材库 facade 的持有器，业务中使用时，注入这个对象
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:43
 */
@Slf4j
@Service
public class MedicineWarehouseFacadeHolder {
    /**
     * 药材库实现类的映射
     */
    private static final Map<Integer, MedicineWarehouseBaseFacade> ENUM_OPERATE_MAP = Maps.newHashMapWithExpectedSize(6);

    @Resource
    List<MedicineWarehouseBaseFacade> medicineWarehouseBaseFacadeList;

    @PostConstruct
    public void postConstruct() {
        if (CollectionUtil.isEmpty(medicineWarehouseBaseFacadeList)) {
            log.info("未声明任何药材库的处理类！");
            return;
        }
        for (MedicineWarehouseBaseFacade medicineWarehouseBaseFacade : medicineWarehouseBaseFacadeList) {
            Integer code = Optional.ofNullable(medicineWarehouseBaseFacade.queryProductType())
                    .orElseThrow(() -> new BusinessException(StrFormatter.format("该处理类{}未声明商品类型，请检查代码!!", medicineWarehouseBaseFacade.getClass())))
                    .getCode();
            ENUM_OPERATE_MAP.put(code, medicineWarehouseBaseFacade);
        }
    }

    /**
     * 根据类型获取具体实现类
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:53
     */
    public MedicineWarehouseBaseFacade getMedicineWarehouseFacade(Integer productTypeCode) {
        return Optional.ofNullable(ENUM_OPERATE_MAP.get(productTypeCode)).orElseThrow(() -> new BusinessException(StrFormatter.format("未找到code为{}的药材库处理类!", productTypeCode)));
    }
}
