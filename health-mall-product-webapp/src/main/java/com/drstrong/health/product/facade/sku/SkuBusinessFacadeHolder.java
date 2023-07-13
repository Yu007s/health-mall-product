package com.drstrong.health.product.facade.sku;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrFormatter;
import com.drstrong.health.common.exception.BusinessException;
import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * sku facade 的持有器，业务中使用时，注入这个对象
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:43
 */
@Slf4j
@Service
public class SkuBusinessFacadeHolder {
    /**
     * sku业务实现类的映射
     */
    private static final Map<Integer, SkuBusinessBaseFacade> ENUM_OPERATE_MAP = Maps.newHashMapWithExpectedSize(6);

    @Resource
    List<SkuBusinessBaseFacade> skuBusinessFacadeList;

    @PostConstruct
    public void postConstruct() {
        if (CollectionUtil.isEmpty(skuBusinessFacadeList)) {
            log.info("未声明任何sku业务处理类！");
            return;
        }
        for (SkuBusinessBaseFacade skuBusinessBaseFacade : skuBusinessFacadeList) {
            Integer code = Optional.ofNullable(skuBusinessBaseFacade.getProductType())
                    .orElseThrow(() -> new BusinessException(StrFormatter.format("该处理类{}未声明商品类型，请检查代码!!", skuBusinessBaseFacade.getClass())))
                    .getCode();
            ENUM_OPERATE_MAP.put(code, skuBusinessBaseFacade);
        }
    }

    /**
     * 根据类型获取具体实现类
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:53
     */
    public SkuBusinessBaseFacade getSkuBusinessBaseFacade(Integer productTypeCode) {
        return Optional.ofNullable(ENUM_OPERATE_MAP.get(productTypeCode)).orElseThrow(() -> new BusinessException(StrFormatter.format("未找到code为{}的sku处理类!", productTypeCode)));
    }

    /**
     * 根据 skuCode 查询用法用量
     *
     * @author liuqiuyi
     * @date 2023/7/11 17:30
     */
    public List<MedicineUsageDTO> queryMedicineUsageBySkuCode(Set<String> skuCodes ) {
        List<MedicineUsageDTO> medicineUsageDTOList = Lists.newArrayListWithCapacity(skuCodes.size());
        skuBusinessFacadeList.forEach(skuBusinessBaseFacade -> medicineUsageDTOList.addAll(skuBusinessBaseFacade.queryMedicineUsageBySkuCode(skuCodes)));
        return medicineUsageDTOList;
    }
}
