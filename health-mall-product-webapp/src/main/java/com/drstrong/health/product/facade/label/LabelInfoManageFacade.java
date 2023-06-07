package com.drstrong.health.product.facade.label;

import com.drstrong.health.product.model.dto.label.LabelExtendDTO;
import com.drstrong.health.product.model.request.label.SaveLabelRequest;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/7 11:37
 */
public interface LabelInfoManageFacade {
	/**
	 * 根据店铺 id 和标签类型查询
	 *
	 * @author liuqiuyi
	 * @date 2023/6/7 11:29
	 */
	List<LabelExtendDTO> listByStoreIdAndType(Long storeId, Integer labelType);

	/**
	 * 保存或者更新店铺标签信息
	 *
	 * @author liuqiuyi
	 * @date 2023/6/7 11:42
	 */
	void saveOrUpdate(SaveLabelRequest saveLabelRequest);
}
