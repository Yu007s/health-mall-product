package com.drstrong.health.product.service.chinese.impl;

import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.drstrong.health.product.dao.chinese.ChineseMedicineConflictMapper;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineConflictEntity;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import org.springframework.stereotype.Service;

/**
 * 中药材相反药材表服务
 * @Author xieYueFeng
 * @Date 2022/07/27/19:32
 */
@Service
public class ChineseMedicineConflictServiceImpl  extends CustomServiceImpl<ChineseMedicineConflictMapper, ChineseMedicineConflictEntity> implements ChineseMedicineConflictService {
}
