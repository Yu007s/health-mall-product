package com.drstrong.health.product.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 操作日志类
 *
 * @author liuqiuyi
 * @date 2022/11/1 10:46
 */
@ApiModel("操作日志记录类")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationLog implements Serializable {
	private static final long serialVersionUID = 1317126816639537373L;

	@ApiModelProperty("业务 id,例如:sku 编码,中药材编码等等")
	private String businessId;

	@ApiModelProperty("操作类型,例如:sku 变动")
	private String operationType;

	@ApiModelProperty("操作内容,具体的操作步骤 例如:sku 上架,中药材删除 等等")
	private String operateContent;

	@ApiModelProperty("修改前的数据")
	private String changeBeforeData;

	@ApiModelProperty("修改后的数据")
	private String changeAfterData;

	@ApiModelProperty("操作人id")
	private Long operationUserId;

	@ApiModelProperty("操作人类型,请见 common 包的 OperateTypeEnum 类")
	private Integer operationUserType;
}
