package com.drstrong.health.product.service.medicine.impl;

import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.response.medicine.*;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import com.drstrong.health.log.vo.HealthLogQueryVO.Sort;

import java.time.LocalDateTime;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.log.vo.HealthLogPageQueryVO;
import com.drstrong.health.log.vo.HealthLogVO;
import com.drstrong.health.product.constants.MedicineConstant;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.dao.medicine.WesternMedicineMapper;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineInstructionsEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.MedicineInstructionsRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.log.LogApiServicePlus;
import com.drstrong.health.product.service.medicine.WesternMedicineInstructionsService;
import com.drstrong.health.product.service.medicine.WesternMedicineService;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 西/成药品库 服务实现类
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@Slf4j
@Service
public class WesternMedicineServiceImpl extends ServiceImpl<WesternMedicineMapper, WesternMedicineEntity> implements WesternMedicineService {


    @Autowired
    private WesternMedicineInstructionsService westernMedicineInstructionsService;

    @Autowired
    private OperationLogSendUtil operationLogSendUtil;

    @Autowired
    private LogApiServicePlus logApiService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdateMedicine(AddOrUpdateMedicineRequest addOrUpdateMedicineRequest) {
        log.info("invoke saveOrUpdateMedicine() param：{}", JSON.toJSONString(addOrUpdateMedicineRequest));
        boolean updateFlag = ObjectUtil.isNotNull(addOrUpdateMedicineRequest.getId());
        WesternMedicineEntity westernMedicineEntity = buildWesternMedicineEntity(addOrUpdateMedicineRequest);
        String logJsonStr = JSONUtil.toJsonStr(westernMedicineEntity);
        if (updateFlag) {
            WesternMedicineEntity westernMedicine = queryByMedicineId(addOrUpdateMedicineRequest.getId());
            if (ObjectUtil.isNull(westernMedicine)) {
                throw new BusinessException("药品不存在");
            }
            westernMedicineEntity.setId(westernMedicine.getId());
            westernMedicineEntity.setMedicineCode(westernMedicine.getMedicineCode());
            logJsonStr = JSONUtil.toJsonStr(westernMedicine);
        }
        westernMedicineEntity.setDataIntegrity(checkDataIntegrity(addOrUpdateMedicineRequest));
        //保存修改西药信息
        saveOrUpdate(westernMedicineEntity);
        //保存修改西药说明
        addOrUpdateMedicineRequest.getMedicineInstructions().setMedicineId(westernMedicineEntity.getId());
        westernMedicineInstructionsService.saveOrUpdateInstructions(addOrUpdateMedicineRequest.getMedicineInstructions());
        //保存操作日志
        OperationLog operationLog = OperationLog.buildOperationLog(westernMedicineEntity.getMedicineCode(), OperationLogConstant.SAVE_OR_UPDATE_WESTERN_MEDICINE,
                buildOperateContent(updateFlag, westernMedicineEntity.getMedicineCode()), addOrUpdateMedicineRequest.getUserId(), addOrUpdateMedicineRequest.getUserName(),
                OperateTypeEnum.CMS.getCode(), logJsonStr);
        log.info("药品操作日志记录,param：{}", JSON.toJSONString(operationLog));
        operationLogSendUtil.sendOperationLog(operationLog);
        return westernMedicineEntity.getId();
    }


    private Integer checkDataIntegrity(AddOrUpdateMedicineRequest medicine) {
        MedicineInstructionsRequest instructions = medicine.getMedicineInstructions();
        if (ObjectUtil.hasEmpty(
                medicine.getBrandName(),
                medicine.getMedicineName(),
                medicine.getPinyin(),
                medicine.getCommonName(),
                medicine.getChemicalName(),
                medicine.getEnglishName(),
                medicine.getStandardCode(),
                instructions.getIngredients(),
                instructions.getPhenotypicTrait(),
                instructions.getAdverseEffects(),
                instructions.getContraindications(),
                instructions.getMattersNeedingAttention(),
                instructions.getListingLicensee()
        )) {
            return MedicineConstant.DATA_NO_INTEGRITY;
        }
        return MedicineConstant.DATA_INTEGRITY;
    }

    @Override
    public WesternMedicineInfoVO queryMedicineDetailInfo(Long id) {
        WesternMedicineEntity westernMedicine = getById(id);
        Assert.notNull(westernMedicine, "西药不存在");
        WesternMedicineInfoVO vo = BeanUtil.copyProperties(westernMedicine, WesternMedicineInfoVO.class);
        WesternMedicineInstructionsEntity instructions = westernMedicineInstructionsService.queryByMedicineId(id);
        MedicineInstructionsVO medicineInstructionsVO = BeanUtil.copyProperties(instructions, MedicineInstructionsVO.class);
        vo.setMedicineInstructions(medicineInstructionsVO);
        return vo;
    }

    @Override
    public WesternMedicineEntity queryByMedicineCode(String medicineCode) {
        if (StrUtil.isBlank(medicineCode)) {
            return null;
        }
        LambdaQueryWrapper<WesternMedicineEntity> queryWrapper = new LambdaQueryWrapper<WesternMedicineEntity>()
                .eq(WesternMedicineEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(WesternMedicineEntity::getMedicineCode, medicineCode);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public WesternMedicineEntity queryByMedicineId(Long medicineId) {
        if (ObjectUtil.isNull(medicineId)) {
            return null;
        }
        LambdaQueryWrapper<WesternMedicineEntity> queryWrapper = new LambdaQueryWrapper<WesternMedicineEntity>()
                .eq(WesternMedicineEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(WesternMedicineEntity::getId, medicineId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public PageVO<WesternMedicineVO> queryMedicinePageList(WesternMedicineRequest request) {
        Page<WesternMedicineVO> westernMedicineVOPage = baseMapper.queryMedicinePageList(new Page<>(request.getPageNo(), request.getPageSize()), request);
        return PageVO.newBuilder().pageNo(request.getPageNo()).pageSize(request.getPageSize()).totalCount((int) westernMedicineVOPage.getTotal()).result(westernMedicineVOPage.getRecords()).build();
    }

    @Override
    public PageVO<WesternMedicineLogVO> queryMedicineOperationLogByPage(WesternMedicineRequest westernMedicineRequest) {
        if (ObjectUtil.hasEmpty(westernMedicineRequest.getMedicineCode(), westernMedicineRequest.getSearchLogType())) {
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        int pageNo = westernMedicineRequest.getPageNo();
        int pageSize = westernMedicineRequest.getPageSize();
        HealthLogPageQueryVO request = new HealthLogPageQueryVO();
        request.setPage(pageNo);
        request.setPageSize(pageSize);
        request.setIsCount(Boolean.TRUE);
        request.setSource(OperationLogConstant.SAVE_OR_UPDATE_WESTERN_MEDICINE);
        request.setBusinessId(westernMedicineRequest.getMedicineCode());
        request.setOrder(Sort.DESC);
        PageVO<HealthLogVO> pageVO = logApiService.queryWesternMedicineUpdateLog(request);
        List<WesternMedicineLogVO> vo = pageVO.getResult().stream()
                .map(s -> {
                    String operateContent = s.getContentMaps().get("operateContent");
                    return ObjectUtil.isNotNull(operateContent) ? WesternMedicineLogVO.builder()
                            .medicineCode(s.getBusinessId())
                            .operationTime(s.getCreatedAt())
                            .operationAccount(s.getCreatedBy())
                            .operationType(operateContent.startsWith("s") ? 0 : 1)
                            .operationBehavior(getOperationBehavior(westernMedicineRequest.getSearchLogType(), s.getContentMaps()))
                            .build() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return PageVO.newBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalCount(pageVO.getTotalCount())
                .result(vo)
                .build();
    }


    private String buildOperateContent(boolean updateFlag, String medicineCode) {
        String action = updateFlag ? MedicineConstant.UPDATE_WESTERN_MEDICINE : MedicineConstant.SAVE_WESTERN_MEDICINE;
        return String.format("%s_%s", action, medicineCode);
    }

    private String getOperationBehavior(Integer searchLogType, Map<String, String> contentMaps) {
        String operationBehavior = StrUtil.EMPTY;
        if (!ObjectUtil.equals(searchLogType, 1)) {
            return operationBehavior;
        }
        String operateContent = contentMaps.get("operateContent");
        String[] splitContent = operateContent.split("_");
        String behaviorType = splitContent[0];
        String code = splitContent[1];
        switch (behaviorType) {
            case MedicineConstant.SAVE_WESTERN_MEDICINE:
                operationBehavior = String.format("新建药品【%s】", code);
                break;
            case MedicineConstant.UPDATE_WESTERN_MEDICINE:
                operationBehavior = String.format("编辑药品【%s】", code);
                break;
            case MedicineConstant.SAVE_WESTERN_MEDICINE_SPEC:
                operationBehavior = String.format("新建规格【%s】", code);
                break;
            default:
                operationBehavior = String.format("编辑规格【%s】", code);
                break;
        }
        return operationBehavior;
    }

    @Override
    public List<WesternMedicineExcelVO> queryMedicineExcelData(WesternMedicineRequest request) {
        return baseMapper.queryMedicineExcelData(request);
    }

    private WesternMedicineEntity buildWesternMedicineEntity(AddOrUpdateMedicineRequest medicineRequest) {
        medicineRequest.constructFullName();
        WesternMedicineEntity westernMedicine = BeanUtil.copyProperties(medicineRequest, WesternMedicineEntity.class);
        westernMedicine.setMedicineClassificationInfo(JSON.toJSONString(medicineRequest.getMedicineClassificationInfo()));
        if (ObjectUtil.isNull(medicineRequest.getId())) {
            westernMedicine.setCreatedAt(LocalDateTime.now());
            westernMedicine.setCreatedBy(medicineRequest.getUserId());
            westernMedicine.setMedicineCode(UniqueCodeUtils.getNextSpuCode(ProductTypeEnum.MEDICINE));
        }
        westernMedicine.setChangedAt(LocalDateTime.now());
        westernMedicine.setChangedBy(medicineRequest.getUserId());
        return westernMedicine;
    }
}
