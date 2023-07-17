package com.drstrong.health.product.facade.sku;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.dto.sku.SkuInfoSummaryDTO;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.sku.SkuQueryRequest;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * sku的公共方法入口，根据不同的商品类型交给不同的实现类处理
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:14
 */
public interface SkuBusinessBaseFacade {

    /**
     * 返回每个处理类的商品类型
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:41
     */
    ProductTypeEnum getProductType();

    /**
     * 根据入参查询sku信息
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:29
     */
    SkuInfoSummaryDTO querySkuByParam(SkuQueryRequest skuQueryRequest);

    /**
     * 根据skuCode查询
     *
     * @author liuqiuyi
     * @date 2023/6/20 14:55
     */
    SkuInfoSummaryDTO queryBySkuCode(String skuCode);

    /**
     * 根据 productType 过滤skuCode
     *
     * @author liuqiuyi
     * @date 2023/7/17 14:41
     */
    Set<String> filterSkuCodesByProductType(Set<String> skuCodes);

    /**
     * 根据skuCode查询用法用量
     *
     * @author liuqiuyi
     * @date 2023/7/11 10:03
     */
    List<MedicineUsageDTO> queryMedicineUsageBySkuCode(Set<String> skuCodes);

    /**
     * 组装用法用量的方法值
     *
     * @author liuqiuyi
     * @date 2023/7/11 17:44
     */
    default List<MedicineUsageDTO> buildMedicineCodeUsageDto(List<StoreSkuInfoEntity> storeSkuInfoEntityList, Map<String, MedicineUsageDTO> medicineCodeUsageDtoMap) {
        List<MedicineUsageDTO> medicineUsageDTOList = Lists.newArrayListWithCapacity(medicineCodeUsageDtoMap.size());
        for (StoreSkuInfoEntity storeSkuInfoEntity : storeSkuInfoEntityList) {
            String skuCode = storeSkuInfoEntity.getSkuCode();
            String skuName = storeSkuInfoEntity.getSkuName();
            String medicineCode = storeSkuInfoEntity.getMedicineCode();

            MedicineUsageDTO usageDTO = MedicineUsageDTO.builder()
                    .productType(storeSkuInfoEntity.getSkuType())
                    .skuId(storeSkuInfoEntity.getId())
                    .skuCode(skuCode)
                    .skuName(skuName)
                    .build();
            if (medicineCodeUsageDtoMap.containsKey(medicineCode)) {
                MedicineUsageDTO medicineUsageDTO = medicineCodeUsageDtoMap.get(medicineCode);
                BeanUtil.copyProperties(medicineUsageDTO, usageDTO, new CopyOptions().setIgnoreNullValue(true));
            }
            medicineUsageDTOList.add(usageDTO);
        }
        return medicineUsageDTOList;
    }
}
