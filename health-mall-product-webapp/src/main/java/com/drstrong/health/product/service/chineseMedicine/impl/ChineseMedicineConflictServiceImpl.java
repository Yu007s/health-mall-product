package com.drstrong.health.product.service.chineseMedicine.impl;

import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.drstrong.health.product.dao.chineseMedicine.ChineseMedicineConflictMapper;
import com.drstrong.health.product.model.entity.ChineseMedicine.ChineseMedicineConflictEntity;
import com.drstrong.health.product.service.chineseMedicine.ChineseMedicineConflictService;
import org.springframework.stereotype.Service;

/**
 * 中药材相反药材表服务
 * @Author xieYueFeng
 * @Date 2022/07/27/19:32
 */
@Service
public class ChineseMedicineConflictServiceImpl  extends CustomServiceImpl<ChineseMedicineConflictMapper, ChineseMedicineConflictEntity> implements ChineseMedicineConflictService {
}
