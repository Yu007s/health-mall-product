package com.drstrong.health.product.controller.product;

import com.drstrong.health.product.model.dto.product.SaveOrUpdateStoreSkuDTO;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.v3.AgreementSkuInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.product.SkuManageRemoteApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * sku 管理 controller
 * <p>
 * 提供给 cms 管理端使用
 * </>
 *
 * @author liuqiuyi
 * @date 2023/6/5 11:04
 */
@RestController
@RequestMapping("/inner/sku/manage")
public class SkuManageRemoteController implements SkuManageRemoteApi {
	@Override
	public ResultVO<PageVO<AgreementSkuInfoVO>> queryAgreementManageInfo(ProductManageQueryRequest productManageQueryRequest) {
		return ResultVO.success();
	}

	@Override
	public ResultVO<Void> saveOrUpdateStoreProduct(SaveOrUpdateStoreSkuDTO saveOrUpdateStoreProductRequest) {
		return ResultVO.success();
	}

	@Override
	public ResultVO<SaveOrUpdateStoreSkuDTO> queryDetailByCode(String skuCode) {
		return ResultVO.success();
	}

	@Override
	public ResultVO<Void> updateSkuStatus(UpdateSkuStateRequest updateSkuStateRequest) {
		return ResultVO.success();
	}

	@Override
	public ResultVO<Void> scheduledSkuUpDown(ScheduledSkuUpDownRequest scheduledSkuUpDownRequest) {
		return ResultVO.success();
	}
}
