package com.drstrong.health.product.dao.chineseMedicine;

import cn.strong.mybatis.plus.extend.CustomBaseMapper;
import com.drstrong.health.product.model.entity.ChineseMedicine.ChineseMedicineConflictEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 中药材库相反药材表 Mapper 接口
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Mapper
public interface ChineseMedicineConflictMapper extends CustomBaseMapper<ChineseMedicineConflictEntity> {

}
