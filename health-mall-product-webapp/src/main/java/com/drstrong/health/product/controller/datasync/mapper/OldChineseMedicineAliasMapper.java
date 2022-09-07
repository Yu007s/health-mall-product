package com.drstrong.health.product.controller.datasync.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.constants.DsName;
import com.drstrong.health.product.controller.datasync.model.ChineseMedicineAlias;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 中药材别名表 Mapper 接口
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Mapper
@DS(DsName.SLAVE_I)
@Deprecated
public interface OldChineseMedicineAliasMapper extends BaseMapper<ChineseMedicineAlias> {

}
