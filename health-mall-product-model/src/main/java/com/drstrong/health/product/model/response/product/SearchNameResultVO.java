package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 商品名称搜索,结果返回值对象
 *
 * @author liuqiuyi
 * @date 2021/12/18 17:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("名称搜索的返回值")
public class SearchNameResultVO implements Serializable {
	private static final long serialVersionUID = 7411819759618181151L;

	@ApiModelProperty("商品名称集合")
	private List<String> productNameList;
}
