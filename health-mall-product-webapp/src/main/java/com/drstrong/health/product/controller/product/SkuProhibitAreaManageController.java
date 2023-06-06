package com.drstrong.health.product.controller.product;

import com.drstrong.health.product.model.dto.product.SkuProhibitAreaInfoDTO;
import com.drstrong.health.product.model.request.product.v3.SaveOrUpdateSkuProhibitAreaRequest;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.product.SkuProhibitAreaManageRemoteApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * sku 禁售区域
 * <p>
 * 	提供给 cms 管理端使用
 * </>
 *
 * @author liuqiuyi
 * @date 2023/6/5 11:06
 */
@RestController
@RequestMapping("/inner/prohibit/area/manage")
public class SkuProhibitAreaManageController implements SkuProhibitAreaManageRemoteApi {
	@Override
	public ResultVO<List<SkuProhibitAreaInfoDTO>> queryBySkuCode(String skuCode) {
		return ResultVO.success();
	}

	@Override
	public ResultVO<Void> saveOrUpdateArea(SaveOrUpdateSkuProhibitAreaRequest saveOrUpdateSkuProhibitAreaRequest) {
		return ResultVO.success();
	}
}
