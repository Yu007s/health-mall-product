package com.drstrong.health.product.service.chinese.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.chinese.ChineseMedicineMapper;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineConflictEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.chinese.ChineseMedicineRequest;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineVO;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    ChineseMedicineConflictService chineseMedicineConflictService;

    @Resource
    ChineseSkuInfoService chineseSkuInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ChineseMedicineVO chineseMedicineVO) {
        ChineseMedicineEntity chineseMedicineEntity = new ChineseMedicineEntity();
        chineseMedicineEntity.setMedicineName(chineseMedicineVO.getName());
        chineseMedicineEntity.setMaxDosage(chineseMedicineVO.getMaxDosage());
        //将名字转化为拼音  暂未实现
        chineseMedicineEntity.setPinyin(null);
        return super.save(chineseMedicineEntity);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByCode(String medicineCode) {
        if (chineseSkuInfoService.checkHasChineseByMedicineCode(medicineCode)) {
            return false;
        }
        //逻辑删除药材
        LambdaUpdateWrapper<ChineseMedicineEntity> lambdaQueryWrapper =  new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(ChineseMedicineEntity::getMedicineCode,medicineCode)
                           .set(ChineseMedicineEntity::getDelFlag,DelFlagEnum.IS_DELETED.getCode());
        //逻辑删除相反药材
        LambdaUpdateWrapper<ChineseMedicineConflictEntity> conflictUpdateWrapper = new LambdaUpdateWrapper<>();
        conflictUpdateWrapper.eq(ChineseMedicineConflictEntity::getChineseMedicineCode, medicineCode)
                .set(true, ChineseMedicineConflictEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode());
        chineseMedicineConflictService.update(conflictUpdateWrapper);
        return true;
    }

    @Override
    public List<ChineseMedicineInfoResponse> queryAll(ChineseMedicineRequest chineseMedicineRequest) {
        return null;
    }

    @Override
    public List<ChineseMedicineVO> queryPage(ChineseMedicineRequest chineseMedicineRequest) {
//        chineseMedicineMapper.
        List<ChineseMedicineEntity> records = new ArrayList<>();
        LambdaQueryWrapper<ChineseMedicineEntity> medicineWrapper = new LambdaQueryWrapper<>();
        if (chineseMedicineRequest.getMedicineCode() != null) {
            medicineWrapper.eq(ChineseMedicineEntity::getId, chineseMedicineRequest.getMedicineCode()).eq(ChineseMedicineEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
            Page<ChineseMedicineEntity> page = new Page<>(chineseMedicineRequest.getPageNo(), chineseMedicineRequest.getPageSize());
            records = page(page, medicineWrapper).getRecords();
//            records.
        }
        List<String> alisName = new ArrayList<>();
        alisName.add("还原靓靓拳");
        alisName.add("还我漂亮拳");
        return records.stream().map(chineseMedicineEntity -> {
            ChineseMedicineVO chineseMedicineVO = new ChineseMedicineVO();
            chineseMedicineVO.setId(chineseMedicineEntity.getId());
            chineseMedicineVO.setName(chineseMedicineEntity.getMedicineName());
            chineseMedicineVO.setAliName(alisName);
            chineseMedicineVO.setMaxDosage(chineseMedicineEntity.getMaxDosage());
            return chineseMedicineVO;
        }).collect(Collectors.toList());
    }

    /**
     * 根据药材code获取中药材信息
     *
     * @param medicineCode 药材code
     * @author liuqiuyi
     * @date 2022/8/2 21:38
     */
    @Override
    public ChineseMedicineEntity getByMedicineCode(String medicineCode) {
        if (StringUtils.isBlank(medicineCode)) {
            return null;
        }
        LambdaQueryWrapper<ChineseMedicineEntity> queryWrapper = Wrappers.<ChineseMedicineEntity>lambdaQuery()
                .eq(ChineseMedicineEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ChineseMedicineEntity::getMedicineCode, medicineCode)
                .last("limit 1");
        return getOne(queryWrapper);
    }
}
