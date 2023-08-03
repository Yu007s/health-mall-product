package com.drstrong.health.product.service.chinese.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.controller.datasync.model.ChineseMedicineAlias;
import com.drstrong.health.product.dao.chinese.ChineseMedicineMapper;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineConflictEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.entity.chinese.OldChineseMedicine;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineSearchVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultStatus;
import com.drstrong.health.product.remote.pro.SupplierRemoteProService;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.utils.ChangeEventSendUtil;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import com.drstrong.health.ware.model.response.SupplierInfoDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Resource
    SupplierRemoteProService supplierRemoteProService;

    @Resource
    ChangeEventSendUtil changeEventSendUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ChineseMedicineVO chineseMedicineVO) {
        OperationLog operationLog = new OperationLog();

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
            operationLog.setChangeBeforeData(JSONUtil.toJsonStr(byMedicineCode));
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
        boolean saveOrUpdate = super.saveOrUpdate(chineseMedicineEntity);
        // 组装操作日志
        operationLog.setBusinessId(chineseMedicineEntity.getMedicineCode());
        operationLog.setOperationType(OperationLogConstant.MALL_PRODUCT_CHINESE_MEDICINE_CHANGE);
        operationLog.setOperateContent(OperationLogConstant.SAVE_OR_UPDATE_CHINESE_MEDICINE);
        operationLog.setChangeAfterData(JSONUtil.toJsonStr(chineseMedicineEntity));
        operationLog.setOperationUserId(userId);
        operationLog.setOperationUserType(OperateTypeEnum.CMS.getCode());
        changeEventSendUtil.sendOperationLog(operationLog);
        return saveOrUpdate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByCode(String medicineCode, Long userId) {
        ChineseMedicineEntity chineseMedicineEntity = getByMedicineCode(medicineCode);
        if (Objects.isNull(chineseMedicineEntity)) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "药材信息不存在!");
        }
        // 1.校验是否关联了 sku
        if (chineseSkuInfoService.checkHasChineseByMedicineCode(medicineCode)) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "药材已经关联sku,不允许删除!");
        }
        // 2.校验是否关联了供应商
        List<SupplierInfoDTO> supplierInfoDTOList = supplierRemoteProService.searchSupplierByCodeIsThrowError(medicineCode);
        if (CollectionUtil.isNotEmpty(supplierInfoDTOList)) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "药材已经关联了供应商,不允许删除!");
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
        // 组装并发送操作日志
        sendOperationLog(medicineCode, userId, chineseMedicineEntity);
    }

    private void sendOperationLog(String medicineCode, Long userId, ChineseMedicineEntity changeBeforeData) {
        ChineseMedicineEntity afterChineseMedicineEntity = getByMedicineCode(medicineCode);
        OperationLog operationLog = OperationLog.builder()
                .businessId(medicineCode)
                .operationType(OperationLogConstant.MALL_PRODUCT_CHINESE_MEDICINE_CHANGE)
                .operateContent(OperationLogConstant.DELETE_CHINESE_MEDICINE)
                .changeBeforeData(JSONUtil.toJsonStr(changeBeforeData))
                .changeAfterData(JSONUtil.toJsonStr(afterChineseMedicineEntity))
                .operationUserId(userId)
                .operationUserType(OperateTypeEnum.CMS.getCode())
                .build();
        changeEventSendUtil.sendOperationLog(operationLog);
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
        ChineseMedicineSearchVO chineseMedicineSearchVO = new ChineseMedicineSearchVO();
        chineseMedicineSearchVO.setMedicineResponses(Lists.newArrayList());
        chineseMedicineSearchVO.setPageNo(pageNo);
        chineseMedicineSearchVO.setPageSize(pageSize);
        chineseMedicineSearchVO.setTotal(0L);
        // 1.根据条件分页查询中药材库
        LambdaQueryWrapper<ChineseMedicineEntity> wrapper = new LambdaQueryWrapper<ChineseMedicineEntity>()
                .select(ChineseMedicineEntity::getMedicineCode, ChineseMedicineEntity::getMedicineName, ChineseMedicineEntity::getMedicineAlias, ChineseMedicineEntity::getMaxDosage)
                .eq(StringUtils.isNotBlank(medicineCode), ChineseMedicineEntity::getMedicineCode, medicineCode)
                .like(StringUtils.isNotBlank(medicineName), ChineseMedicineEntity::getMedicineName, medicineName)
                .orderByDesc(ChineseMedicineEntity::getId);
        Page<ChineseMedicineEntity> chineseMedicineEntityPage = page(new Page<>(pageNo, pageSize), wrapper);
        List<ChineseMedicineEntity> records = chineseMedicineEntityPage.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            log.info("未查询到任何药材信息，查询参数为：{},{}", medicineCode, medicineName);
            return chineseMedicineSearchVO;
        }
        // 2.组装参数返回
        List<ChineseMedicineResponse> chineseMedicineResponses = buildChineseResponse(records);
        chineseMedicineSearchVO.setTotal(chineseMedicineEntityPage.getTotal());
        chineseMedicineSearchVO.setMedicineResponses(chineseMedicineResponses);
        return chineseMedicineSearchVO;
    }

    /**
     * 查询所有药材并导出
     *
     * @param medicineCode
     * @param medicineName
     * @author liuqiuyi
     * @date 2023/8/2 16:54
     */
    @Override
    public List<ChineseMedicineResponse> queryMedicineExport(String medicineCode, String medicineName) {
        // 1.查询所有药材信息
        LambdaQueryWrapper<ChineseMedicineEntity> wrapper = new LambdaQueryWrapper<ChineseMedicineEntity>()
                .select(ChineseMedicineEntity::getMedicineCode, ChineseMedicineEntity::getMedicineName, ChineseMedicineEntity::getMedicineAlias, ChineseMedicineEntity::getMaxDosage)
                .eq(StringUtils.isNotBlank(medicineCode), ChineseMedicineEntity::getMedicineCode, medicineCode)
                .like(StringUtils.isNotBlank(medicineName), ChineseMedicineEntity::getMedicineName, medicineName)
                .orderByDesc(ChineseMedicineEntity::getId);
        List<ChineseMedicineEntity> chineseMedicineEntityList = baseMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(chineseMedicineEntityList)) {
            log.info("未查询到任何药材信息，查询参数为：{},{}", medicineCode, medicineName);
            return Lists.newArrayList();
        }
        // 2.组装参数后返回
        return buildChineseResponse(chineseMedicineEntityList);
    }

    private List<ChineseMedicineResponse> buildChineseResponse(List<ChineseMedicineEntity> records) {
        // 2.查询所有的相反药材
        Map<String, List<String>> medicineCodeMedicineConflictCodesMap = chineseMedicineConflictService.listAllConflictEntity().stream()
                .filter(chineseMedicineConflictEntity -> StrUtil.isNotBlank(chineseMedicineConflictEntity.getMedicineConflictCodes()))
                .collect(Collectors.toMap(ChineseMedicineConflictEntity::getMedicineCode, dto -> Lists.newArrayList(dto.getMedicineConflictCodes().split(",")), (v1, v2) -> v1));
        // 3.根据相反药材的code，查询药材表
        Set<String> conflictCodes = medicineCodeMedicineConflictCodesMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        Map<String, String> conflictCodeNameMap = getByMedicineCode(conflictCodes).stream().collect(Collectors.toMap(ChineseMedicineEntity::getMedicineCode, ChineseMedicineEntity::getMedicineName, (v1, v2) -> v1));
        // 4.组装最终的结果
        return records.stream().map(chineseMedicineEntity -> {
            ChineseMedicineResponse chineseMedicineResponse = ChineseMedicineResponse.builder()
                    .medicineId(chineseMedicineEntity.getId())
                    .medicineCode(chineseMedicineEntity.getMedicineCode())
                    .name(chineseMedicineEntity.getMedicineName())
                    .aliNames(chineseMedicineEntity.getMedicineAlias())
                    .maxDosage(chineseMedicineEntity.getMaxDosage())
                    .build();
            // 设置相反药材
            if (medicineCodeMedicineConflictCodesMap.containsKey(chineseMedicineEntity.getMedicineCode())) {
                List<ChineseMedicineResponse.ChineseConflictMedicine> chineseConflictMedicineList = medicineCodeMedicineConflictCodesMap.get(chineseMedicineEntity.getMedicineCode()).stream().map(conflictCode ->
                        ChineseMedicineResponse.ChineseConflictMedicine.builder()
                                .medicineCode(conflictCode)
                                .name(conflictCodeNameMap.get(conflictCode))
                                .build()).collect(Collectors.toList());
                chineseMedicineResponse.setChineseConflictMedicineList(chineseConflictMedicineList);
            }
            return chineseMedicineResponse;
        }).collect(Collectors.toList());
    }

    /**
     * 根据条件分页查询
     *
     * @param warehouseQueryRequest
     * @author liuqiuyi
     * @date 2023/6/20 11:31
     */
    @Override
    public Page<ChineseMedicineEntity> pageQueryByRequest(MedicineWarehouseQueryRequest warehouseQueryRequest) {
        Page<ChineseSkuInfoEntity> entityPage = new Page<>(warehouseQueryRequest.getPageNo(), warehouseQueryRequest.getPageSize());
        return baseMapper.pageQueryByRequest(entityPage, warehouseQueryRequest);
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
     * 根据药材code获取中药材信息,并忽略是否删除
     *
     * @param medicineCode 药材code
     * @author liuqiuyi
     * @date 2022/9/29 11:34
     */
    @Override
    public ChineseMedicineEntity getByMedicineCodeIgnoreDel(String medicineCode) {
        if (StringUtils.isBlank(medicineCode)) {
            return null;
        }
        return chineseMedicineMapper.getByMedicineCodeIgnoreDel(medicineCode);
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
     * <p> 注意:包含了删除的数据 </>
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
     * <p> 注意:包含了删除的数据 </>
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
        return chineseMedicineMapper.queryByMedicineIds(medicineIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBatch(List<OldChineseMedicine> chineseMedicines) {
        List<ChineseMedicineEntity> chineseMedicineEntities = new ArrayList<>(50);
        for (OldChineseMedicine chineseMedicine : chineseMedicines) {
            BigDecimal maxDosage = chineseMedicine.getMaxDosage();
            String name = chineseMedicine.getName();
            String pinyin = chineseMedicine.getPinyin();
            Integer invalid = chineseMedicine.getInvalid();
            ChineseMedicineEntity chineseMedicineEntity = new ChineseMedicineEntity();
            if (pinyin.length() > 200) {
                pinyin = pinyin.substring(0, 200);
            }
            chineseMedicineEntity.setMedicinePinyin(pinyin);
            chineseMedicineEntity.setMaxDosage(maxDosage);
            chineseMedicineEntity.setDelFlag(invalid);
            if (name.length() > 200) {
                name = name.substring(0, 200);
            }
            chineseMedicineEntity.setMedicineName(name);
            chineseMedicineEntity.setId(chineseMedicine.getId());
            String nextMedicineCode = UniqueCodeUtils.getNextMedicineCode(name);
            chineseMedicineEntity.setMedicineCode(nextMedicineCode);
            chineseMedicineEntity.setAliasPinyin(" ");
            chineseMedicineEntity.setVersion(1);
            chineseMedicineEntities.add(chineseMedicineEntity);
        }
        if (chineseMedicineEntities.size() != 0) {
            chineseMedicineMapper.insertBatch(chineseMedicineEntities);
        }
    }

    @Override
    public void updateAlias(HashMap<Long, List<ChineseMedicineAlias>> hashMap) {
        List<ChineseMedicineEntity> updateList = new ArrayList<>();
        for (Map.Entry<Long, List<ChineseMedicineAlias>> longListEntry : hashMap.entrySet()) {
            Long key = longListEntry.getKey();
            List<ChineseMedicineAlias> value = longListEntry.getValue();
            String pinYins = value.stream().map(ChineseMedicineAlias::getPinyin).collect(Collectors.joining(","));
            String alias = value.stream().map(ChineseMedicineAlias::getName).collect(Collectors.joining(","));
            ChineseMedicineEntity chineseMedicineEntity = new ChineseMedicineEntity();
            chineseMedicineEntity.setAliasPinyin(pinYins);
            chineseMedicineEntity.setMedicineAlias(alias);
            chineseMedicineEntity.setId(key);
            updateList.add(chineseMedicineEntity);
        }
        updateBatchById(updateList);
    }

    @Override
    public List<ChineseMedicineEntity> getByIds(List<Long> ids) {
        if (ids == null || ids.size() == 0 ){
            return new ArrayList<>(0);
        }
        return super.listByIds(ids);
    }
}
