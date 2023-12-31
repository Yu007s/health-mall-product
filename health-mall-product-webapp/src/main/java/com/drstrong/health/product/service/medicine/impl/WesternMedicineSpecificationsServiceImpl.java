package com.drstrong.health.product.service.medicine.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.MedicineConstant;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.dao.medicine.WesternMedicineMapper;
import com.drstrong.health.product.dao.medicine.WesternMedicineSpecificationsMapper;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.entity.medication.MedicineUsageEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineSpecRequest;
import com.drstrong.health.product.model.request.medicine.MedicineUsageRequest;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.medicine.MedicineUsageVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSimpleInfoVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSpecInfoVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSpecVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultStatus;
import com.drstrong.health.product.service.medicine.MedicineUsageService;
import com.drstrong.health.product.service.medicine.WesternMedicineService;
import com.drstrong.health.product.service.medicine.WesternMedicineSpecificationsService;
import com.drstrong.health.product.util.RedisKeyUtils;
import com.drstrong.health.product.utils.ChangeEventSendUtil;
import com.google.common.collect.Lists;
import com.naiterui.common.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
 * <p>
 * 西药药品规格 服务实现类
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@Slf4j
@Service
public class WesternMedicineSpecificationsServiceImpl extends ServiceImpl<WesternMedicineSpecificationsMapper, WesternMedicineSpecificationsEntity> implements WesternMedicineSpecificationsService {

    @Autowired
    private WesternMedicineService westernMedicineService;

    @Autowired
    private WesternMedicineMapper westernMedicineMapper;

    @Autowired
    private MedicineUsageService medicineUsageService;

    @Autowired
    private ChangeEventSendUtil changeEventSendUtil;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pair<Long, String> saveOrUpdateMedicineSpec(AddOrUpdateMedicineSpecRequest specRequest) {
        WesternMedicineEntity westernMedicine = westernMedicineService.queryByMedicineId(specRequest.getMedicineId());
        Assert.notNull(westernMedicine, "西药不存在");
        WesternMedicineSpecificationsEntity specifications = BeanUtil.copyProperties(specRequest, WesternMedicineSpecificationsEntity.class);
        specifications.setSpecImageInfo(JSONUtil.parse(specRequest.getImageInfoList()).toString());
        specifications.setChangedBy(specRequest.getUserId());
        if (ObjectUtil.isNull(specRequest.getId())) {
            //新增
            specifications.setSpecCode(generateMedicineSpecCode(westernMedicine.getId(), westernMedicine.getMedicineCode()));
            specifications.setCreatedBy(specRequest.getUserId());
        } else {
            WesternMedicineSpecificationsEntity specificationsEntity = this.getById(specRequest.getId());
            if (ObjectUtil.isNull(specificationsEntity)) {
                Assert.notNull(specificationsEntity, "药品规格不存在");
            }
            specifications.setSpecCode(specificationsEntity.getSpecCode());
        }
        // 保存或更新药物规格
        saveOrUpdate(specifications);
        MedicineUsageRequest medicineUsage = specRequest.getMedicineUsage();
        if (ObjectUtil.isNull(specRequest.getMedicineUsage())) {
            medicineUsage = new MedicineUsageRequest(specRequest.getUseUsageDosage(), specifications.getId(), MedicineConstant.MEDICINE_SPECIFICATIONS_USAGE_DOSAGE);
        } else {
            medicineUsage.setRelationType(MedicineConstant.MEDICINE_SPECIFICATIONS_USAGE_DOSAGE);
            medicineUsage.setRelationId(specifications.getId());
            medicineUsage.setUseUsageDosage(specRequest.getUseUsageDosage());
        }
        medicineUsage.setUserId(specRequest.getUserId());
        medicineUsage.setUserName(specRequest.getUserName());
        //保存-更新用法用量
        medicineUsageService.saveOrUpdateUsage(medicineUsage);
        //保存规格操作日志
        String logJsonStr = JSONUtil.toJsonStr(specifications);
        OperationLog operationLog = OperationLog.buildOperationLog(westernMedicine.getMedicineCode(), OperationLogConstant.SAVE_OR_UPDATE_WESTERN_MEDICINE,
                buildOperateContent(ObjectUtil.isNotNull(specRequest.getId()), specifications.getSpecCode()), specRequest.getUserId(), specRequest.getUserName(),
                OperateTypeEnum.CMS.getCode(), logJsonStr);
        log.info("药品规格操作日志记录,param：{}", JSON.toJSONString(operationLog));
        changeEventSendUtil.sendOperationLog(operationLog);
        return new Pair<>(specifications.getId(), specifications.getSpecCode());
    }

    @Override
    public WesternMedicineSpecInfoVO queryMedicineSpecDetailInfo(Long id) {
        WesternMedicineSpecInfoVO vo = new WesternMedicineSpecInfoVO();
        LambdaQueryWrapper<WesternMedicineSpecificationsEntity> queryWrapper = new LambdaQueryWrapper<WesternMedicineSpecificationsEntity>()
                .eq(WesternMedicineSpecificationsEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(WesternMedicineSpecificationsEntity::getId, id);
        WesternMedicineSpecificationsEntity specifications = baseMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNotNull(specifications)) {
            vo = BeanUtil.copyProperties(specifications, WesternMedicineSpecInfoVO.class);
            MedicineUsageEntity medicineUsage = medicineUsageService.getMedicineUsageBySpecId(specifications.getId(), 1);
            vo.setUseUsageDosage(0);
            if (ObjectUtil.isNotNull(medicineUsage)) {
                MedicineUsageVO medicineUsageVO = BeanUtil.copyProperties(medicineUsage, MedicineUsageVO.class);
                vo.setUseUsageDosage(1);
                vo.setMedicineUsage(medicineUsageVO);
            }
        }
        return vo;
    }

    @Override
    public WesternMedicineSimpleInfoVO queryMedicineSpecByPage(WesternMedicineRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "药品id不能为空");
        }
        WesternMedicineSimpleInfoVO vo = westernMedicineMapper.queryMedicineSimpleInfo(request.getId());
        vo.setSpecPageList(getMedicineSpecVOPage(request));
        return vo;
    }

    @Override
    public PageVO<WesternMedicineSpecVO> queryMedicineSpecInfoPage(WesternMedicineRequest request) {
        return getMedicineSpecVOPage(request);
    }

    public PageVO<WesternMedicineSpecVO> getMedicineSpecVOPage(WesternMedicineRequest request) {
        Page<WesternMedicineSpecVO> westernMedicineSpecVOPage = baseMapper.queryMedicineSpecPageList(new Page<>(request.getPageNo(), request.getPageSize()), request);
        return PageVO.newBuilder()
                .pageNo(request.getPageNo())
                .pageSize(request.getPageSize())
                .totalCount((int) westernMedicineSpecVOPage.getTotal())
                .result(westernMedicineSpecVOPage.getRecords()).build();
    }

    private String generateMedicineSpecCode(Long medicineId, String medicineCode) {
        // 生成规则：药品编码M开头 + 建码日期六位：年后两位+月份+日期（190520）+ 5位顺序码    举例：M19052000001
        long serialNumber = RedisUtil.keyOps().incr(RedisKeyUtils.getSkuNum(medicineId));
        return medicineCode + "-" + serialNumber;
    }

    private String buildOperateContent(boolean updateFlag, String medicineCode) {
        String action = updateFlag ? MedicineConstant.UPDATE_WESTERN_MEDICINE_SPEC : MedicineConstant.SAVE_WESTERN_MEDICINE_SPEC;
        return String.format("%s_%s", action, medicineCode);
    }

    /**
     * 按条件分页查询规格信息，返回基本信息
     *
     * @param medicineWarehouseQueryRequest
     * @author liuqiuyi
     * @date 2023/6/20 14:05
     */
    @Override
    public Page<WesternMedicineSpecificationsEntity> pageQueryByRequest(MedicineWarehouseQueryRequest medicineWarehouseQueryRequest) {
        Page<WesternMedicineSpecificationsEntity> entityPage = new Page<>(medicineWarehouseQueryRequest.getPageNo(), medicineWarehouseQueryRequest.getPageSize());
        return baseMapper.pageQueryByRequest(entityPage, medicineWarehouseQueryRequest);
    }

    /**
     * 根据code查询西药规格
     *
     * @param code
     * @author liuqiuyi
     * @date 2023/6/20 19:18
     */
    @Override
    public WesternMedicineSpecificationsEntity queryByCode(String code) {
        LambdaQueryWrapper<WesternMedicineSpecificationsEntity> queryWrapper = new LambdaQueryWrapper<WesternMedicineSpecificationsEntity>()
                .eq(WesternMedicineSpecificationsEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(WesternMedicineSpecificationsEntity::getSpecCode, code);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据code查询西药规格
     *
     * @param codeList
     * @author liuqiuyi
     * @date 2023/6/20 19:18
     */
    @Override
    public List<WesternMedicineSpecificationsEntity> queryByCodeList(List<String> codeList) {
        LambdaQueryWrapper<WesternMedicineSpecificationsEntity> queryWrapper = new LambdaQueryWrapper<WesternMedicineSpecificationsEntity>()
                .eq(WesternMedicineSpecificationsEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(WesternMedicineSpecificationsEntity::getSpecCode, codeList);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<MedicineUsageDTO> queryMedicineUsageBySpecCodes(Set<String> specCodes) {
        if (CollectionUtil.isEmpty(specCodes)) {
            return Lists.newArrayList();
        }
        return baseMapper.queryMedicineUsageBySpecCodes(specCodes);
    }
}
