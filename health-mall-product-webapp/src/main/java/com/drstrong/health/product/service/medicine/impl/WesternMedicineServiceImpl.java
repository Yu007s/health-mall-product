package com.drstrong.health.product.service.medicine.impl;

import com.google.common.collect.Lists;
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
import com.drstrong.health.common.utils.DateUtil;
import com.drstrong.health.log.api.LogFacade;
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
import com.drstrong.health.product.model.response.medicine.MedicineInstructionsVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineInfoVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineLogVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.log.LogApiServicePlus;
import com.drstrong.health.product.service.medicine.WesternMedicineInstructionsService;
import com.drstrong.health.product.service.medicine.WesternMedicineService;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import com.naiterui.common.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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
        boolean updateFlag = ObjectUtil.isAllNotEmpty(addOrUpdateMedicineRequest.getId(), addOrUpdateMedicineRequest.getMedicineCode());
        WesternMedicineEntity westernMedicineEntity = buildWesternMedicineEntity(addOrUpdateMedicineRequest);
        String logJsonStr = JSONUtil.toJsonStr(westernMedicineEntity);
        if (updateFlag) {
            WesternMedicineEntity westernMedicine = queryByMedicineCode(addOrUpdateMedicineRequest.getMedicineCode());
            westernMedicineEntity.setId(westernMedicine.getId());
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
                updateFlag ? MedicineConstant.SAVE_WESTERN_MEDICINE : MedicineConstant.UPDATE_WESTERN_MEDICINE, addOrUpdateMedicineRequest.getUserId(), addOrUpdateMedicineRequest.getUserName(),
                OperateTypeEnum.CMS.getCode(), logJsonStr);
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
        Assert.notNull(westernMedicineRequest.getMedicineCode(), () -> new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL));
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
                            .operationType(ObjectUtil.equals(operateContent, MedicineConstant.SAVE_WESTERN_MEDICINE) ? 0 : 1)
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

    private WesternMedicineEntity buildWesternMedicineEntity(AddOrUpdateMedicineRequest medicineRequest) {
        medicineRequest.constructFullName();
        WesternMedicineEntity westernMedicine = BeanUtil.copyProperties(medicineRequest, WesternMedicineEntity.class);
        westernMedicine.setMedicineClassificationInfo(JSON.toJSONString(medicineRequest.getMedicineClassificationInfo()));
        if (ObjectUtil.isNull(medicineRequest.getId())) {
            westernMedicine.setCreatedAt(LocalDateTime.now());
            westernMedicine.setCreatedBy(medicineRequest.getUserId());
            westernMedicine.setMedicineCode(generateMedicineCode());
        }
        westernMedicine.setChangedAt(LocalDateTime.now());
        westernMedicine.setChangedBy(medicineRequest.getUserId());
        return westernMedicine;
    }


    private String generateMedicineCode() {
        // 生成规则：药品编码M开头 + 建码日期六位：年后两位+月份+日期（190520）+ 5位顺序码    举例：M19052000001
        StringBuilder number = new StringBuilder();
        number.append("M");
        number.append(DateUtil.formatDate(new Date(), "yyMMdd"));
        long serialNumber = RedisUtil.keyOps().incr(MedicineConstant.SERIAL_NUMBER_REDIS_KEY);
        number.append(String.format("%05d", serialNumber));
        return number.toString();
    }
}
