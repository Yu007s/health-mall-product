package com.drstrong.health.product.controller.label;

import com.drstrong.health.product.facade.label.LabelInfoManageFacade;
import com.drstrong.health.product.model.dto.label.LabelExtendDTO;
import com.drstrong.health.product.model.request.label.SaveLabelRequest;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.label.LabelManageRemoteApi;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/7 11:19
 */
@RestController
@RequestMapping("/inner/label/manage")
public class LabelManageController implements LabelManageRemoteApi {
	@Resource
	LabelInfoManageFacade labelInfoManageFacade;

	@Override
	public ResultVO<List<LabelExtendDTO>> queryAll(Long storeId, Integer labelType) {
		return ResultVO.success(labelInfoManageFacade.listByStoreIdAndType(storeId, labelType));
	}

	@Override
	public ResultVO<Void> saveOrUpdate(@RequestBody @Valid SaveLabelRequest saveLabelRequest) {
		labelInfoManageFacade.saveOrUpdate(saveLabelRequest);
		return ResultVO.success();
	}
}
