package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 商品搜索的入参
 *
 * @author liuqiuyi
 * @date 2021/12/6 20:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("商品搜索参数")
public class ProductSearchRequest extends PageRequest implements Serializable {
	private static final long serialVersionUID = 586525841318457288L;

	@ApiModelProperty("查询内容")
	@NotEmpty(message = "搜索内容不能为空")
	private String content;
}
