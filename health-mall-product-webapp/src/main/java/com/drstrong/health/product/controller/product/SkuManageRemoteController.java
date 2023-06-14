package com.drstrong.health.product.controller.product;

import com.drstrong.health.product.facade.sku.SkuManageFacade;
import com.drstrong.health.product.model.dto.product.StoreSkuDetailDTO;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.request.product.v3.SaveOrUpdateStoreSkuRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.v3.AgreementSkuInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.product.SkuManageRemoteApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
	@Resource
	SkuManageFacade skuManageFacade;

	@Override
	public ResultVO<PageVO<AgreementSkuInfoVO>> pageQuerySkuManageInfo(ProductManageQueryRequest productManageQueryRequest) {
		return ResultVO.success(skuManageFacade.querySkuManageInfo(productManageQueryRequest));
	}

	@Override
	public ResultVO<List<AgreementSkuInfoVO>> listSkuManageInfo(ProductManageQueryRequest productManageQueryRequest) {
		return ResultVO.success(skuManageFacade.listSkuManageInfo(productManageQueryRequest));
	}

	@Override
	public ResultVO<Void> saveOrUpdateStoreProduct(SaveOrUpdateStoreSkuRequest saveOrUpdateStoreSkuRequest) {
		skuManageFacade.saveOrUpdateStoreProduct(saveOrUpdateStoreSkuRequest);
		return ResultVO.success();
	}

	@Override
	public ResultVO<StoreSkuDetailDTO> queryDetailByCode(String skuCode) {
		return ResultVO.success(skuManageFacade.queryDetailByCode(skuCode));
	}

	@Override
	public ResultVO<Void> updateSkuStatus(UpdateSkuStateRequest updateSkuStateRequest) {
		skuManageFacade.updateSkuStatus(updateSkuStateRequest);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Void> scheduledSkuUpDown(ScheduledSkuUpDownRequest scheduledSkuUpDownRequest) {
		return ResultVO.success();
	}
}
