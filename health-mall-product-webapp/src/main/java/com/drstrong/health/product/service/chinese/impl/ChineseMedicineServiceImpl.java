package com.drstrong.health.product.service.chinese.impl;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.chinese.ChineseMedicineMapper;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineConflictEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineSearchVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultStatus;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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

    @Resource
    ChineseMedicineMapper chineseMedicineMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ChineseMedicineVO chineseMedicineVO) {
        Long userId = chineseMedicineVO.getUserId();
        String medicineCode = chineseMedicineVO.getMedicineCode();
        ChineseMedicineEntity chineseMedicineEntity = new ChineseMedicineEntity();
        LambdaQueryWrapper<ChineseMedicineEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ChineseMedicineEntity::getMedicineName,
                chineseMedicineVO.getName()).eq(ChineseMedicineEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        ChineseMedicineEntity getOneByName = getOne(lambdaQueryWrapper);
        if (StringUtils.isNotBlank(medicineCode)) {
            //编辑药材
            ChineseMedicineEntity byMedicineCode = getByMedicineCode(medicineCode);
            if (byMedicineCode == null || byMedicineCode.getId() == null) {
                throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "编辑药材失败，未找到该药材");
            }
            if (getOneByName != null && !getOneByName.getMedicineCode().equals(medicineCode)) {
                throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "编辑药材失败，此药材名已经被使用");
            }
            chineseMedicineEntity.setId(byMedicineCode.getId());
            chineseMedicineEntity.setMedicineCode(chineseMedicineVO.getMedicineCode());
        } else {
            //新增药材  检验药材是否重名
            if (getOneByName != null) {
                throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "新增药材失败，已有同名药材");
            }
            //新增药材
            String nextMedicineCode = UniqueCodeUtils.getNextMedicineCode(chineseMedicineVO.getName());
            medicineCode = nextMedicineCode;
            chineseMedicineEntity.setCreatedBy(userId);
            chineseMedicineEntity.setMedicineCode(nextMedicineCode);
        }
        chineseMedicineEntity.setMedicineName(chineseMedicineVO.getName());
        chineseMedicineEntity.setMaxDosage(chineseMedicineVO.getMaxDosage());
        String aliNamesString = chineseMedicineVO.getAliNames();
        String[] split = aliNamesString.split(",");
        Arrays.stream(split).forEach(string -> {
            if (chineseMedicineVO.getName().equals(string)) {
                throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "药材别名与药材名字相同");
            }
        });
        //别名转换为拼音
        String aliNamePingYinS = Arrays.stream(split).map(s -> PinyinUtil.getFirstLetter(s, "")).collect(Collectors.joining(","));
        chineseMedicineEntity.setMedicineAlias(aliNamesString);
        chineseMedicineEntity.setAliasPinyin(aliNamePingYinS);
        //将名字转化为拼音
        chineseMedicineEntity.setMedicinePinyin(PinyinUtil.getFirstLetter(chineseMedicineVO.getName(), ""));
        chineseMedicineEntity.setChangedBy(userId);
        //更新相反药材名表
        List<String> conflictMedicineCodes = chineseMedicineVO.getConflictMedicineCodes();
        Set<String> medicineSet = new HashSet<>(conflictMedicineCodes);
        if (conflictMedicineCodes.size() != 0 && medicineSet.size() != conflictMedicineCodes.size()) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "重复的相反药材");
        }
        if (StringUtils.isNotBlank(chineseMedicineVO.getMedicineCode())) {
            if (medicineSet.contains(chineseMedicineVO.getMedicineCode())) {
                throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "药材相反药材名称不能与自己相同");
            }
        }
        if(medicineSet.size() != 0) {
            List<ChineseMedicineEntity> byMedicineCode = this.getByMedicineCode(medicineSet);
            if (byMedicineCode.size() != medicineSet.size()) {
                throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "相反药材中有不存在的中药材");
            }
        }
        ChineseMedicineConflictEntity chineseMedicineConflictEntity = new ChineseMedicineConflictEntity();
        String collect = String.join(",", conflictMedicineCodes);
        chineseMedicineConflictEntity.setMedicineCode(medicineCode);
        chineseMedicineConflictEntity.setMedicineConflictCodes(collect);
        chineseMedicineConflictService.saveOrUpdate(chineseMedicineConflictEntity, userId);
        return super.saveOrUpdate(chineseMedicineEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByCode(String medicineCode, Long userId) {
        if (chineseSkuInfoService.checkHasChineseByMedicineCode(medicineCode)) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "药材已经关联sku");
        }
        //逻辑删除药材
        LambdaUpdateWrapper<ChineseMedicineEntity> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(ChineseMedicineEntity::getMedicineCode, medicineCode)
                .set(ChineseMedicineEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode())
                .set(ChineseMedicineEntity::getChangedBy, userId);
        super.update(lambdaQueryWrapper);
        //逻辑删除相反药材
        ChineseMedicineConflictEntity chineseMedicineConflictEntity = new ChineseMedicineConflictEntity();
        chineseMedicineConflictEntity.setMedicineCode(medicineCode);
        chineseMedicineConflictService.delete(chineseMedicineConflictEntity, userId);
    }

    @Override
    public List<ChineseMedicineInfoResponse> queryAll(String medicineName, String medicineCode) {
        LambdaQueryWrapper<ChineseMedicineEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ChineseMedicineEntity::getMedicineCode, ChineseMedicineEntity::getMedicineName);
        if (StringUtils.isNotBlank(medicineName)) {
            queryWrapper.like(ChineseMedicineEntity::getMedicineName, medicineName);
        }
        if (StringUtils.isNotBlank(medicineCode)) {
            queryWrapper.eq(ChineseMedicineEntity::getMedicineCode, medicineCode);
        }
        queryWrapper.last("limit 50");
        List<ChineseMedicineEntity> list = super.list(queryWrapper);
        return list.stream().map(chineseMedicineEntity -> {
            ChineseMedicineInfoResponse chineseMedicineInfoResponse = new ChineseMedicineInfoResponse();
            chineseMedicineInfoResponse.setMedicineCode(chineseMedicineEntity.getMedicineCode());
            chineseMedicineInfoResponse.setMedicineName(chineseMedicineEntity.getMedicineName());
            return chineseMedicineInfoResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public ChineseMedicineSearchVO queryPage(String medicineCode, String medicineName, Integer pageNo, Integer pageSize) {
        LambdaQueryWrapper<ChineseMedicineEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ChineseMedicineEntity::getMedicineCode, ChineseMedicineEntity::getMedicineName,
                ChineseMedicineEntity::getMedicineAlias, ChineseMedicineEntity::getMaxDosage);
        if (StringUtils.isNotBlank(medicineCode)) {
            wrapper.eq(ChineseMedicineEntity::getMedicineCode, medicineCode);
        }
        if (StringUtils.isNotBlank(medicineName)) {
            wrapper.like(ChineseMedicineEntity::getMedicineName, medicineName);
        }
        wrapper.orderByDesc(ChineseMedicineEntity::getCreatedAt);
        Page<ChineseMedicineEntity> page = new Page<>(pageNo, pageSize);
        List<ChineseMedicineEntity> records = page(page, wrapper).getRecords();
        long total = page.getTotal();
        List<ChineseMedicineResponse> chineseMedicineResponses = buildChineseMedicineResponse(records);
        ChineseMedicineSearchVO chineseMedicineSearchVO = new ChineseMedicineSearchVO();
        chineseMedicineSearchVO.setPageNo(pageNo);
        chineseMedicineSearchVO.setPageSize(pageSize);
        chineseMedicineSearchVO.setTotal((int) total);
        chineseMedicineSearchVO.setMedicineResponses(chineseMedicineResponses);
        return chineseMedicineSearchVO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChineseMedicineResponse> queryForConflict(String medicineCode) {
        ChineseMedicineConflictEntity chineseMedicineConflictEntity = chineseMedicineConflictService.getByMedicineCode(medicineCode);
        if (chineseMedicineConflictEntity == null ) {
            return new ArrayList<>(0);
        }
        String medicineConflictCodes = chineseMedicineConflictEntity.getMedicineConflictCodes();
        if (StringUtils.isBlank(medicineConflictCodes)) {
            return new ArrayList<>(0);
        }
        List<String> conflictCodes = Arrays.asList(chineseMedicineConflictEntity.getMedicineConflictCodes().split(","));
        LambdaQueryWrapper<ChineseMedicineEntity> medicineWrapper = new LambdaQueryWrapper<>();
        medicineWrapper.select(ChineseMedicineEntity::getMedicineCode, ChineseMedicineEntity::getMedicineName,
                ChineseMedicineEntity::getMedicineAlias, ChineseMedicineEntity::getMaxDosage);
        medicineWrapper.and((wrapper) -> wrapper.in(ChineseMedicineEntity::getMedicineCode, conflictCodes));
        List<ChineseMedicineEntity> records;
        records = list(medicineWrapper);
        return buildChineseMedicineResponse(records);
    }


    private List<ChineseMedicineResponse> buildChineseMedicineResponse(List<ChineseMedicineEntity> chineseMedicineEntities) {
        return chineseMedicineEntities.stream().map(chineseMedicineEntity -> {
            ChineseMedicineResponse response = new ChineseMedicineResponse();
            response.setMedicineCode(chineseMedicineEntity.getMedicineCode());
            response.setName(chineseMedicineEntity.getMedicineName());
            String medicineAlias = chineseMedicineEntity.getMedicineAlias();
            response.setAliNames(medicineAlias);
            response.setMaxDosage(chineseMedicineEntity.getMaxDosage());
            return response;
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

    /**
     * 根据药材code获取中药材信息
     *
     * @param medicineCodes 药材code
     * @return 药材信息
     * @author liuqiuyi
     * @date 2022/8/4 15:32
     */
    @Override
    public List<ChineseMedicineEntity> getByMedicineCode(Set<String> medicineCodes) {
        if (CollectionUtils.isEmpty(medicineCodes)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ChineseMedicineEntity> queryWrapper = Wrappers.<ChineseMedicineEntity>lambdaQuery()
                .eq(ChineseMedicineEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(ChineseMedicineEntity::getMedicineCode, medicineCodes);
        return list(queryWrapper);
    }

    /**
     * 根据关键字模糊搜索
     * <p>
     * 支持药材名称、药材编码、别名、药材拼音、别名拼音  搜索
     * </>
     *
     * @param keyword 关键字
     * @return 搜索结果
     * @author liuqiuyi
     * @date 2022/8/3 20:47
     */
    @Override
    public List<ChineseMedicineEntity> likeQueryByKeyword(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return Lists.newArrayList();
        }
        return chineseMedicineMapper.likeQueryByKeyword(keyword);
    }

    /**
     * 根据老的药材 id 获取药材 code,组成 map
     *
     * @param medicineIds 药材id
     * @author liuqiuyi
     * @date 2022/8/5 16:41
     */
    @Override
    public Map<Long, String> getMedicineIdAndMedicineCodeMap(Set<Long> medicineIds) {
        List<ChineseMedicineEntity> chineseMedicineEntityList = listMedicineByIds(medicineIds);
        return chineseMedicineEntityList.stream().collect(Collectors.toMap(ChineseMedicineEntity::getId, ChineseMedicineEntity::getMedicineCode, (v1, v2) -> v1));
    }

    /**
     * 根据老的药材 id 获取药材信息
     *
     * @param medicineIds 老药材 id 集合
     * @return 药材信息集合
     * @author liuqiuyi
     * @date 2022/8/15 10:18
     */
    @Override
    public List<ChineseMedicineEntity> listMedicineByIds(Set<Long> medicineIds) {
        if (CollectionUtils.isEmpty(medicineIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ChineseMedicineEntity> queryWrapper = Wrappers.<ChineseMedicineEntity>lambdaQuery()
                .eq(ChineseMedicineEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(ChineseMedicineEntity::getId, medicineIds);
        return list(queryWrapper);
    }
}
