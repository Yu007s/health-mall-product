package com.drstrong.health.product.facade.sku.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.product.facade.sku.SkuBusinessBaseFacade;
import com.drstrong.health.product.facade.sku.SkuManageFacade;
import com.drstrong.health.product.model.dto.sku.SkuInfoSummaryDTO;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.request.sku.SkuQueryRequest;
import com.drstrong.health.product.model.response.product.v3.AgreementSkuInfoVO;
import com.drstrong.health.product.remote.pro.StockRemoteProService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 西药 facade （具体到规格）
 *
 * @author liuqiuyi
 * @date 2023/6/20 11:27
 */
@Slf4j
@Service
public class WesternSkuBusinessFacadeImpl implements SkuBusinessBaseFacade {

    @Resource
    SkuManageFacade skuManageFacade;

    @Resource
    StockRemoteProService stockRemoteProService;

    /**
     * 返回每个处理类的商品类型
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:41
     */
    @Override
    public ProductTypeEnum getProductType() {
        return ProductTypeEnum.MEDICINE;
    }

    @Override
    public SkuInfoSummaryDTO querySkuByParam(SkuQueryRequest skuQueryRequest) {
        SkuInfoSummaryDTO skuBaseVO = SkuInfoSummaryDTO.builder()
                .productType(getProductType().getCode())
                .productTypeName(getProductType().getValue()).build();
        // 1.查询数据
        ProductManageQueryRequest queryRequest = BeanUtil.copyProperties(skuQueryRequest, ProductManageQueryRequest.class);
        List<AgreementSkuInfoVO> westernSkuInfoVOList = skuManageFacade.listSkuManageInfo(queryRequest);
        if (CollectionUtil.isEmpty(westernSkuInfoVOList)) {
            log.info("根据条件查询西药列表，返回值为空，查询参数为：{}", JSONUtil.toJsonStr(skuQueryRequest));
            skuBaseVO.setWesternSkuInfoVoList(Lists.newArrayList());
            return skuBaseVO;
        }
        skuBaseVO.setWesternSkuInfoVoList(westernSkuInfoVOList);
        if (ObjectUtil.equal(Boolean.TRUE, skuQueryRequest.getNeedQueryInventory())) {
            // 2.调用库存接口
            List<String> skuCodes = westernSkuInfoVOList.stream().map(AgreementSkuInfoVO::getSkuCode).collect(Collectors.toList());
            skuBaseVO.setSkuCanStockList(stockRemoteProService.getStockToMap(skuCodes));
        }
        return skuBaseVO;
    }

    @Override
    public SkuInfoSummaryDTO queryBySkuCode(String skuCode) {
        SkuQueryRequest skuQueryRequest = SkuQueryRequest.builder().skuCode(skuCode).build();
        return SpringUtil.getBean(WesternSkuBusinessFacadeImpl.class).querySkuByParam(skuQueryRequest);
    }
}
