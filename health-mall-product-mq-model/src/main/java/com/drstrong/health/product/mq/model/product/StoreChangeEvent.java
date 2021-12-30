package com.drstrong.health.product.mq.model.product;

import lombok.Data;

import java.io.Serializable;

/**
 * 店铺变更事件
 *
 * @author liuqiuyi
 * @date 2021/12/30 11:22
 */
@Data
public class StoreChangeEvent implements Serializable {
	private static final long serialVersionUID = 1186736574622921863L;

	/**
	 * 店铺 id
	 */
	private Long storeId;

	/**
	 * 操作人 id
	 */
	private String operatorId;
}
