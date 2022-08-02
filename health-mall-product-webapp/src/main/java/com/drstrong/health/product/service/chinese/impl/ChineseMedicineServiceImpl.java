package com.drstrong.health.product.service.chinese.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.chinese.ChineseMedicineMapper;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineAliasEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineConflictEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.chinese.ChineseMedicineRequest;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineVO;
import com.drstrong.health.product.service.chinese.ChineseMedicineAliasService;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 中药材表相关服务
 *
 * @Author xieYueFeng
 * @Date 2022/07/27/16:37
 */
@Service
@Slf4j
public class ChineseMedicineServiceImpl extends ServiceImpl<ChineseMedicineMapper, ChineseMedicineEntity> implements ChineseMedicineService {

    @Resource
    ChineseMedicineAliasService chineseMedicineAliasService;

    @Resource
    ChineseMedicineConflictService chineseMedicineConflictService;

    @Resource
    ChineseMedicineMapper chineseMedicineMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ChineseMedicineVO chineseMedicineVO) {
        ChineseMedicineEntity chineseMedicineEntity = new ChineseMedicineEntity();
        chineseMedicineEntity.setMedicineName(chineseMedicineVO.getName());
        chineseMedicineEntity.setMaxDosage(chineseMedicineVO.getMaxDosage());
        //将名字转化为拼音  暂未实现
        chineseMedicineEntity.setPinyin(null);
        boolean save = super.save(chineseMedicineEntity);
        if (!save) {
            return false;
        }
        Long medicineCode = chineseMedicineEntity.getMedicineCode();
        //更新相反药材表
        List<Long> medicineConflictIds = chineseMedicineVO.getMedicineConflictIds();
        List<ChineseMedicineConflictEntity> conflictList = medicineConflictIds.stream().map(conflictId -> {
            ChineseMedicineConflictEntity chineseMedicineConflictEntity = new ChineseMedicineConflictEntity();
            chineseMedicineConflictEntity.setChineseMedicineCode(medicineCode);
            chineseMedicineConflictEntity.setChineseMedicineConflictCode(conflictId);
            return chineseMedicineConflictEntity;
        }).collect(Collectors.toList());
        chineseMedicineConflictService.saveBatch(conflictList);
        //更新药材别名表
        List<String> aliName = chineseMedicineVO.getAliName();
        List<ChineseMedicineAliasEntity> aliasList = aliName.stream().map(name -> {
            ChineseMedicineAliasEntity chineseMedicineAliasEntity = new ChineseMedicineAliasEntity();
            chineseMedicineAliasEntity.setMedicineId(medicineCode);
            chineseMedicineAliasEntity.setAliasName(name);
            return chineseMedicineAliasEntity;
        }).collect(Collectors.toList());
        chineseMedicineAliasService.saveBatch(aliasList);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long medicineId) {
        ChineseMedicineEntity chineseMedicineEntity = new ChineseMedicineEntity();
        chineseMedicineEntity.setId(medicineId);
        //逻辑删除
        chineseMedicineEntity.setDelFlag(1);
        updateById(chineseMedicineEntity);
        //逻辑删除相反药材
        LambdaUpdateWrapper<ChineseMedicineConflictEntity> conflictUpdateWrapper = new LambdaUpdateWrapper<>();
        conflictUpdateWrapper.eq(ChineseMedicineConflictEntity::getId, medicineId).set(true, ChineseMedicineConflictEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        chineseMedicineConflictService.update(conflictUpdateWrapper);
        LambdaUpdateWrapper<ChineseMedicineAliasEntity> aliasUpdateWrapper = new LambdaUpdateWrapper<>();
        aliasUpdateWrapper.eq(ChineseMedicineAliasEntity::getMedicineId, medicineId).set(true, ChineseMedicineAliasEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        chineseMedicineAliasService.update(aliasUpdateWrapper);
        return true;
    }

    public List<ChineseMedicineVO> queryPage(ChineseMedicineRequest chineseMedicineRequest) {
//        chineseMedicineMapper.
        List<ChineseMedicineEntity> records = new ArrayList<>();
        LambdaQueryWrapper<ChineseMedicineEntity> medicineWrapper = new LambdaQueryWrapper<>();
        if (chineseMedicineRequest.getMedicineId() != null) {
            medicineWrapper.eq(ChineseMedicineEntity::getId, chineseMedicineRequest.getMedicineId()).eq(ChineseMedicineEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
            Page<ChineseMedicineEntity> page = new Page<>(chineseMedicineRequest.getPageNo(), chineseMedicineRequest.getPageSize());
            records = page(page, medicineWrapper).getRecords();
//            records.
        }
        List<String> alisName = new ArrayList<>();
        alisName.add("还原靓靓拳");
        alisName.add("还我漂亮拳");
        return  records.stream().map( chineseMedicineEntity -> {
            ChineseMedicineVO ChineseMedicineVO = new ChineseMedicineVO();
            ChineseMedicineVO.setId(chineseMedicineEntity.getId());
            ChineseMedicineVO.setName(chineseMedicineEntity.getMedicineName());
            ChineseMedicineVO.setAliName(alisName);
            ChineseMedicineVO.setMaxDosage(chineseMedicineEntity.getMaxDosage());
            return ChineseMedicineVO;
        }).collect(Collectors.toList());
    }
}
