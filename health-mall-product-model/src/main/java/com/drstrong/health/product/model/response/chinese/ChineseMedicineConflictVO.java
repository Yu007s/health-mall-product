package com.drstrong.health.product.model.response.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 中药材相反信息,和之前的出参保持一致
 * <p> 此处为了兼容老业务,新业务建议使用 code </>
 *
 * @author liuqiuyi
 * @date 2022/8/8 11:39
 */
@Data
@ApiModel("中药材相反信息,和之前的出参保持一致")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChineseMedicineConflictVO implements Serializable {
	private static final long serialVersionUID = 713733319205888686L;

	@ApiModelProperty("老的中药材 id")
	private Long id;

	@ApiModelProperty("相反药材的老中药材 id集合")
	private List<Long> conflictIdList;
}
