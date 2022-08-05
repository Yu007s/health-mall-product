package com.drstrong.health.product.dao.chinese;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.constants.DsName;
import com.drstrong.health.product.model.entity.chinese.OldChineseMedicine;
import org.apache.ibatis.annotations.Mapper;

/**
 * FIXME 仅作为数据修复使用,不要用于其它用途
 *
 * @author liuqiuyi
 * @date 2022/8/5 14:51
 */
@DS(DsName.SLAVE_I)
@Mapper
public interface OldChineseMedicineMapper extends BaseMapper<OldChineseMedicine> {
}
