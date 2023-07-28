package com.drstrong.health.product.facade.label.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.drstrong.health.product.facade.label.LabelInfoManageFacade;
import com.drstrong.health.product.model.dto.label.LabelExtendDTO;
import com.drstrong.health.product.model.entity.label.LabelInfoEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.label.SaveLabelRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.cms.CmsRemoteProService;
import com.drstrong.health.product.service.label.LabelInfoService;
import com.drstrong.health.user.vo.SimpleUcUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author liuqiuyi
 * @date 2023/6/7 11:37
 */
@Slf4j
@Service
public class LabelInfoManageFacadeImpl implements LabelInfoManageFacade {
	@Resource
	LabelInfoService labelInfoService;

	@Resource
	CmsRemoteProService cmsRemoteProService;


	/**
	 * 根据店铺 id 和标签类型查询
	 *
	 * @param storeId
	 * @param labelType
	 * @author liuqiuyi
	 * @date 2023/6/7 11:29
	 */
	@Override
	public List<LabelExtendDTO> listByStoreIdAndType(Long storeId, Integer labelType) {
		// 1.查询所有标签列表
		List<LabelInfoEntity> labelInfoEntityList = labelInfoService.listByStoreIdAndType(storeId, labelType);
		List<LabelExtendDTO> labelExtendDTOList = BeanUtil.copyToList(labelInfoEntityList, LabelExtendDTO.class);
		// 2.根据 id 查询创建人名称
		List<Long> cmsIdList = labelExtendDTOList.stream().map(LabelExtendDTO::getCreatedBy).distinct().collect(Collectors.toList());
		Map<Long, SimpleUcUserVO> cmsIdUserVoMap = cmsRemoteProService.queryByIds(cmsIdList).stream().collect(Collectors.toMap(SimpleUcUserVO::getId, dto -> dto, (v1, v2) -> v1));
		// 3.设置名称
		labelExtendDTOList.forEach(labelExtendDTO -> labelExtendDTO.setCreatedName(cmsIdUserVoMap.getOrDefault(labelExtendDTO.getCreatedBy(), new SimpleUcUserVO()).getRealName()));
		return labelExtendDTOList;
	}

	/**
	 * 保存或者更新店铺标签信息
	 *
	 * @param saveLabelRequest
	 * @author liuqiuyi
	 * @date 2023/6/7 11:42
	 */
	@Override
	public void saveOrUpdate(SaveLabelRequest saveLabelRequest) {
		// 1.校验同一个店铺下的标签名称不能重复
		LabelInfoEntity labelInfoEntity = labelInfoService.queryByStoreIdAndNameAndType(saveLabelRequest.getStoreId(), saveLabelRequest.getLabelName(), saveLabelRequest.getLabelType());
		if (Objects.nonNull(labelInfoEntity) && ObjectUtil.notEqual(saveLabelRequest.getId(), labelInfoEntity.getId())) {
			throw new BusinessException(ErrorEnums.STORE_LABEL_REPEAT);
		}
		// 2.判断是新增还是更新
		boolean saveFlag = Objects.isNull(saveLabelRequest.getId());
		if (saveFlag) {
			LabelInfoEntity saveParam = LabelInfoEntity.buildDefaultEntity(saveLabelRequest.getOperatorId());
			saveParam.setStoreId(saveLabelRequest.getStoreId());
			saveParam.setLabelName(saveLabelRequest.getLabelName());
			saveParam.setLabelType(saveLabelRequest.getLabelType());
			labelInfoService.save(saveParam);
		} else {
			LabelInfoEntity updateParam = labelInfoService.queryById(saveLabelRequest.getId());
			if (Objects.isNull(updateParam)) {
				throw new BusinessException(ErrorEnums.STORE_LABEL_NOT_EXIST);
			}
			updateParam.setLabelName(saveLabelRequest.getLabelName());
			updateParam.setChangedBy(saveLabelRequest.getOperatorId());
			updateParam.setChangedAt(LocalDateTime.now());
			labelInfoService.updateById(updateParam);
		}
	}
}
