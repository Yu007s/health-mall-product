package com.drstrong.health.product.service.medicine.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.medicine.MedicineClassificationMapper;
import com.drstrong.health.product.model.BaseTree;
import com.drstrong.health.product.model.entity.medication.MedicineClassificationEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.MedicineClassificationEnum;
import com.drstrong.health.product.model.response.medicine.FixedClassificationVO;
import com.drstrong.health.product.model.response.medicine.MedicineClassificationVO;
import com.drstrong.health.product.service.medicine.MedicineClassificationService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 药品基础分类 服务实现类
 * </p>
 *
 * @author zzw
 * @since 2023-06-06
 */
@Slf4j
@Service
public class MedicineClassificationServiceImpl extends ServiceImpl<MedicineClassificationMapper, MedicineClassificationEntity> implements MedicineClassificationService {

    @Override
    public List<MedicineClassificationVO> getListByType(Integer classificationType) {
        LambdaQueryWrapper<MedicineClassificationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MedicineClassificationEntity::getClassificationType, classificationType)
                .eq(MedicineClassificationEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<MedicineClassificationEntity> medicineClassificationList = list(queryWrapper);
        if (CollectionUtils.isEmpty(medicineClassificationList)) {
            return Lists.newArrayList();
        }
        List<MedicineClassificationVO> result = buildClassificationTree(medicineClassificationList);
        return result;
    }

    @Override
    public List<FixedClassificationVO> getAllClassification() {
        List<MedicineClassificationEntity> medicineClassificationList = list(new LambdaQueryWrapper<MedicineClassificationEntity>().eq(MedicineClassificationEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode()));
        List<MedicineClassificationVO> result = buildClassificationTree(medicineClassificationList);
        Map<Integer, List<MedicineClassificationVO>> collect = result.stream().collect(Collectors.groupingBy(MedicineClassificationVO::getClassificationType));
        List<FixedClassificationVO> voList = collect.entrySet().stream()
                .map(entry -> FixedClassificationVO.builder()
                        .classificationType(entry.getKey())
                        .classificationName(MedicineClassificationEnum.getValueByCode(entry.getKey()))
                        .medicineClassificationList(entry.getValue())
                        .build())
                .collect(Collectors.toList());
        return voList;
    }

    public List<MedicineClassificationVO> buildClassificationTree(List<MedicineClassificationEntity> medicineClassificationList) {
        List<MedicineClassificationVO> result = BeanUtil.copyToList(Optional.ofNullable(medicineClassificationList).orElse(Collections.emptyList()), MedicineClassificationVO.class);
        BaseTree.listToTree(result);
        return result;
    }
}
