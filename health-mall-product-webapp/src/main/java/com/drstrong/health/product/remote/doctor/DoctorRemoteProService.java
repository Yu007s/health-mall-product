package com.drstrong.health.product.remote.doctor;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import com.drstrong.health.product.model.dto.RecordHospitalDTO;
import com.drstrong.health.user.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author liuqiuyi
 * @date 2022/12/8 17:15
 */
@Slf4j
@Component
public class DoctorRemoteProService {
	@Resource
	IDoctorFeignClient doctorFeignClient;

	/**
	 * 根据老的医生 id,获取医生的互联网医院 id
	 *
	 * @author liuqiuyi
	 * @date 2022/12/8 17:30
	 */
	public Long queryDoctorAgencyId(Long doctorId) {
		if (Objects.isNull(doctorId)) {
			return null;
		}
		try {
			ResponseVO<RecordHospitalDTO> hospitalDTOResponseVO = doctorFeignClient.getRecordHospitalByDoctor(doctorId);
			if (Objects.isNull(hospitalDTOResponseVO) || ObjectUtil.notEqual(hospitalDTOResponseVO.getCode(), 0)) {
				log.info("invoke doctorFeignClient.getRecordHospitalByDoctor return null,param:{}", doctorId);
				return null;
			}
			return Opt.ofNullable(hospitalDTOResponseVO.getData()).map(recordHospitalDTOS -> recordHospitalDTOS.get(0).getId().longValue()).orElse(null);
		} catch (Exception e) {
			log.error("invoke doctorFeignClient.getRecordHospitalByDoctor an error occurred,param:{}", doctorId, e);
			return null;
		}
	}
}
