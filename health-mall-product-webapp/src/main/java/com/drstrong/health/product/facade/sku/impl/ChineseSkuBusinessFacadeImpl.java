package com.drstrong.health.product.facade.sku.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.product.facade.ChineseManagerFacade;
import com.drstrong.health.product.facade.sku.SkuBusinessBaseFacade;
import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.dto.sku.SkuInfoSummaryDTO;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.request.sku.SkuQueryRequest;
import com.drstrong.health.product.model.response.chinese.ChineseManagerSkuVO;
import com.drstrong.health.product.remote.pro.StockRemoteProService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liuqiuyi
 * @date 2023/7/7 17:53
 */
@Slf4j
@Service
public class ChineseSkuBusinessFacadeImpl implements SkuBusinessBaseFacade {
    @Resource
    ChineseManagerFacade chineseManagerFacade;

    @Resource
    StockRemoteProService stockRemoteProService;

    @Override
    public ProductTypeEnum getProductType() {
        return ProductTypeEnum.CHINESE;
    }

    @Override
    public SkuInfoSummaryDTO querySkuByParam(SkuQueryRequest skuQueryRequest) {
        SkuInfoSummaryDTO skuBaseVO = SkuInfoSummaryDTO.builder()
                .productType(getProductType().getCode())
                .productTypeName(getProductType().getValue()).build();
        // 1.调用查询接口
        ChineseManagerSkuRequest chineseManagerSkuRequest = new ChineseManagerSkuRequest();
        chineseManagerSkuRequest.setSkuCode(skuQueryRequest.getSkuCode());
        chineseManagerSkuRequest.setSkuName(skuQueryRequest.getSkuName());
        chineseManagerSkuRequest.setStoreId(skuQueryRequest.getStoreId());
        chineseManagerSkuRequest.setSkuState(skuQueryRequest.getSkuStatus());
        List<ChineseManagerSkuVO> managerSkuVOList = chineseManagerFacade.listChineseManagerSkuExport(chineseManagerSkuRequest);
        if (CollectionUtil.isEmpty(managerSkuVOList)) {
            log.info("根据条件查询中药列表，返回值为空，查询参数为：{}", JSONUtil.toJsonStr(skuQueryRequest));
            skuBaseVO.setChineseManagerSkuVoList(Lists.newArrayList());
            return skuBaseVO;
        }
        skuBaseVO.setChineseManagerSkuVoList(managerSkuVOList);
        if (ObjectUtil.equal(Boolean.TRUE, skuQueryRequest.getNeedQueryInventory())) {
            // 2.调用库存接口
            List<String> skuCodes = managerSkuVOList.stream().map(ChineseManagerSkuVO::getSkuCode).collect(Collectors.toList());
            skuBaseVO.setSkuCanStockList(stockRemoteProService.getStockToMap(skuCodes));
        }
        return skuBaseVO;
    }

    @Override
    public SkuInfoSummaryDTO queryBySkuCode(String skuCode) {
        SkuQueryRequest skuQueryRequest = SkuQueryRequest.builder().skuCode(skuCode).build();
        return SpringUtil.getBean(ChineseSkuBusinessFacadeImpl.class).querySkuByParam(skuQueryRequest);
    }

    /**
     * 根据 productType 过滤skuCode
     *
     * @param skuCodes
     * @author liuqiuyi
     * @date 2023/7/17 14:41
     */
    @Override
    public Set<String> filterSkuCodesByProductType(Set<String> skuCodes) {
        if (CollectionUtil.isEmpty(skuCodes)) {
            return Sets.newHashSet();
        }
        return skuCodes.stream().filter(skuCode -> StrUtil.isNotBlank(skuCode) && skuCode.startsWith(getProductType().getMark())).collect(Collectors.toSet());
    }

    @Override
    public List<MedicineUsageDTO> queryMedicineUsageBySkuCode(Set<String> skuCodes) {
        log.info("中药暂无用法用量，不处理");
        return Lists.newArrayList();
    }
}
