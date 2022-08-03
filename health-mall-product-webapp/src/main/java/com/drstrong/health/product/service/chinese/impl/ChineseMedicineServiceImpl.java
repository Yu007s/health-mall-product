package com.drstrong.health.product.service.chinese.impl;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.drstrong.health.product.model.response.chinese.ChineseMedicineResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineVO;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
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

    @Resource
    ChineseMedicineMapper chineseMedicineMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ChineseMedicineVO chineseMedicineVO,Long userId) throws Exception {
        String medicineCode = chineseMedicineVO.getMedicineCode();
        ChineseMedicineEntity chineseMedicineEntity = new ChineseMedicineEntity();
        if (StringUtils.isNotBlank(medicineCode)) {
            //编辑药材
            ChineseMedicineEntity byMedicineCode = getByMedicineCode(medicineCode);
            Long medicineId = byMedicineCode.getId();
            if (medicineId ==  null) {
                throw new Exception("编辑药材失败，未找到该药材");
            }
            chineseMedicineEntity.setId(medicineId);
            chineseMedicineEntity.setMedicineCode(chineseMedicineVO.getMedicineCode());
        }
        else{
            //检验药材是否重名
            LambdaQueryWrapper<ChineseMedicineEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ChineseMedicineEntity::getMedicineName,chineseMedicineVO.getName());
            ChineseMedicineEntity one = getOne(lambdaQueryWrapper);
            if(one != null){
                throw new Exception("新增药材失败，已有同名药材");
            }
            //新增药材
            String nextMedicineCode = UniqueCodeUtils.getNextMedicineCode(chineseMedicineVO.getName());
            chineseMedicineEntity.setMedicineCode(nextMedicineCode);
        }
        chineseMedicineEntity.setMedicineName(chineseMedicineVO.getName());
        chineseMedicineEntity.setMaxDosage(chineseMedicineVO.getMaxDosage());
        List<String> aliNames = chineseMedicineVO.getAliNames();
        StringBuilder aliNameSb = new StringBuilder(),aliNamePinSb = new StringBuilder();
        aliNames.forEach( a -> {
            String firstLetter = PinyinUtil.getFirstLetter(a, "");
            aliNamePinSb.append(firstLetter).append(',');
            aliNameSb.append(a).append(',');
        });
        chineseMedicineEntity.setMedicineAlias(aliNameSb.toString());
        //别名转换为拼音
        chineseMedicineEntity.setAliasPinyin(aliNamePinSb.toString());
        //将名字转化为拼音
        chineseMedicineEntity.setMedicinePinyin(PinyinUtil.getFirstLetter(chineseMedicineVO.getName(),""));
        chineseMedicineEntity.setChangedBy(userId);
        //更新相反药材名表
        List<String> conflictMedicineCodes = chineseMedicineVO.getConflictMedicineCodes();
        ChineseMedicineConflictEntity chineseMedicineConflictEntity = new ChineseMedicineConflictEntity();
        StringBuilder stringBuilder = new StringBuilder();
        conflictMedicineCodes.forEach( conflictMedicineCode -> stringBuilder.append(conflictMedicineCode).append(","));
        chineseMedicineConflictEntity.setMedicineCode(medicineCode);
        chineseMedicineConflictEntity.setMedicineConflictCodes(stringBuilder.toString());
        chineseMedicineConflictService.saveUpdate(chineseMedicineConflictEntity,userId);
        return super.saveOrUpdate(chineseMedicineEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByCode(String medicineCode,Long userId) {
        if (chineseSkuInfoService.checkHasChineseByMedicineCode(medicineCode)) {
            return false;
        }
        //逻辑删除药材
        LambdaUpdateWrapper<ChineseMedicineEntity> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(ChineseMedicineEntity::getMedicineCode, medicineCode)
                .set(ChineseMedicineEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode())
                .set(ChineseMedicineEntity::getChangedBy,userId);
        //逻辑删除相反药材
        LambdaUpdateWrapper<ChineseMedicineConflictEntity> conflictUpdateWrapper = new LambdaUpdateWrapper<>();
        conflictUpdateWrapper.eq(ChineseMedicineConflictEntity::getMedicineCode, medicineCode)
                .set(true, ChineseMedicineConflictEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode())
                .set(ChineseMedicineConflictEntity::getChangedBy,userId);
        chineseMedicineConflictService.update(conflictUpdateWrapper);
        return true;
    }

    @Override
    public List<ChineseMedicineInfoResponse> queryAll(ChineseMedicineRequest chineseMedicineRequest) {
        return null;
    }

    @Override
    public List<ChineseMedicineResponse> queryPage(ChineseMedicineRequest chineseMedicineRequest) {
        QueryWrapper<ChineseMedicineEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().select(ChineseMedicineEntity::getMedicineCode, ChineseMedicineEntity::getMedicineName,
                ChineseMedicineEntity::getMedicineAlias, ChineseMedicineEntity::getMaxDosage);
//        medicineWrapper.eq(ChineseMedicineEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        if (StringUtils.isNotBlank(chineseMedicineRequest.getMedicineCode())) {
            wrapper.lambda().eq(ChineseMedicineEntity::getMedicineCode, chineseMedicineRequest.getMedicineCode());
        }
        if (StringUtils.isNotBlank(chineseMedicineRequest.getMedicineName()) ) {
            wrapper.lambda().like(ChineseMedicineEntity::getMedicineName, chineseMedicineRequest.getMedicineName());
        }
        Page<ChineseMedicineEntity> page = new Page<>(chineseMedicineRequest.getPageNo(), chineseMedicineRequest.getPageSize());
        List<ChineseMedicineEntity> records = page(page, wrapper).getRecords();
        return buildChineseMedicineResponse(records);
    }

    @Override
    public List<ChineseMedicineResponse> queryPage(List<String> medicineCodes, Integer pageNo, Integer pageSize) {
        LambdaQueryWrapper<ChineseMedicineEntity> medicineWrapper = new LambdaQueryWrapper<>();
        medicineWrapper.select(ChineseMedicineEntity::getMedicineCode, ChineseMedicineEntity::getMedicineName,
                        ChineseMedicineEntity::getMedicineAlias, ChineseMedicineEntity::getMaxDosage)
//                .in(ChineseMedicineEntity::getMedicineCode,medicineCodes)
                .eq(ChineseMedicineEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        medicineWrapper.and((wrapper) -> {
            wrapper.in(ChineseMedicineEntity::getMedicineCode, medicineCodes);
        });
        Page<ChineseMedicineEntity> page = new Page<>(pageNo, pageSize);
        List<ChineseMedicineEntity> records = page(page, medicineWrapper).getRecords();
        return buildChineseMedicineResponse(records);
    }

    private List<ChineseMedicineResponse> buildChineseMedicineResponse(List<ChineseMedicineEntity> chineseMedicineEntities) {
        return chineseMedicineEntities.stream().map(chineseMedicineEntity -> {
            ChineseMedicineResponse response = new ChineseMedicineResponse();
            response.setMedicineCode(chineseMedicineEntity.getMedicineCode());
            response.setName(chineseMedicineEntity.getMedicineName());
            List<String> strings = Arrays.asList(chineseMedicineEntity.getMedicineAlias().split(","));
            response.setAliNames(strings);
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
}
