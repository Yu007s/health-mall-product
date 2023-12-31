package com.drstrong.health.product.service.label;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.label.LabelInfoEntity;

import java.util.List;

/**
 * 标签信息 service
 *
 * @author liuqiuyi
 * @date 2023/6/7 11:26
 */
public interface LabelInfoService extends IService<LabelInfoEntity> {
	/**
	 * 根据 Id 查询
	 *
	 * @author liuqiuyi
	 * @date 2023/6/7 12:01
	 */
	LabelInfoEntity queryById(Long id);

	/**
	 * 根据 Ids 查询
	 *
	 * @author liuqiuyi
	 * @date 2023/6/7 12:01
	 */
	List<LabelInfoEntity> queryByIds(List<Long> ids);

	/**
	 * 根据店铺 id 和标签名称查询
	 *
	 * @author liuqiuyi
	 * @date 2023/6/7 11:29
	 */
	LabelInfoEntity queryByStoreIdAndNameAndType(Long storeId, String labelName, Integer labelType);

	/**
	 * 根据店铺 id 和标签类型查询
	 *
	 * @author liuqiuyi
	 * @date 2023/6/7 11:29
	 */
	List<LabelInfoEntity> listByStoreIdAndType(Long storeId, Integer labelType);
}
