package com.drstrong.health.product.remote.doctor;

import com.drstrong.health.common.constant.ServiceNameConstants;
import com.drstrong.health.product.model.dto.RecordHospitalDTO;
import com.drstrong.health.user.vo.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = ServiceNameConstants.DOCTOR)
public interface IDoctorFeignClient {
	/**
	 * 获取医生互联网医院信息
	 *
	 * @param doctorId
	 * @return
	 */
	@GetMapping("/inner/recordHospital/info/byDoctor")
	ResponseVO<RecordHospitalDTO> getRecordHospitalByDoctor(@RequestParam("doctorId") Long doctorId);
}
