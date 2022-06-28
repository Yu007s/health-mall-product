package com.drstrong.health.product.dao.product;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.constants.DsName;
import com.drstrong.health.product.model.entity.product.Sku;
import com.drstrong.health.product.remote.vo.BsUserInfoVO;
import com.drstrong.health.product.remote.vo.SkuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@DS(DsName.SLAVE_I)
@Mapper
public interface SkuMapper extends BaseMapper<Sku> {


    List<SkuVO> getskuNumber(@Param("ids") Set<Long> skuIds);

    List<BsUserInfoVO> selectRepresentInfoList(Object o);
}
