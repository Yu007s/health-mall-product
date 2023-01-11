package com.drstrong.health.product.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 医生备案医院
 *
 * @Author zkp
 * @Date 2022/7/29 17:25
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordHospitalDTO {
	/**
	 * id
	 */
	private Integer id;
	/**
	 * 医院名
	 */
	private String name;
}
