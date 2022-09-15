package com.drstrong.health.product.dao.store;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.constants.DsName;
import com.drstrong.health.product.model.entity.store.OldAreaFreight;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author lsx
 * @Date 2022/07/26/9:26
 */
@DS(DsName.SLAVE_I)
@Mapper
public interface OldFreightPostageMapper extends BaseMapper<OldAreaFreight> {

    @Select(" SELECT a.*,b.name as areaName" +
            " FROM `freight_pharmacy_area` a JOIN p_city b ON a.area_id = b.id" +
            " WHERE a.warehouse_id = 90 and a.del_flag = 0")
    List<OldAreaFreight> searchOldTLPostage();
}
