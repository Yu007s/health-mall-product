package com.drstrong.health.product.remote.pro;

import com.drstrong.health.common.model.Result;
import com.drstrong.health.product.remote.doctor.DoctorRemoteProService;
import com.drstrong.health.user.api.UcUserFacade;
import com.drstrong.health.user.dto.UcUserIdDTO;
import com.drstrong.health.user.vo.BusinessUserIdVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author liuqiuyi
 * @date 2022/12/8 17:09
 */
@Slf4j
@Component
public class UserRemoteProService {
	@Resource
	UcUserFacade ucUserFacade;

	@Resource
	DoctorRemoteProService doctorRemoteProService;

	/**
	 * 根据医生 id 获取互联网医院 id
	 *
	 * @author liuqiuyi
	 * @date 2022/12/8 17:32
	 */
	public Long getDoctorAgencyId(Long ucDoctorId) {
		return doctorRemoteProService.queryDoctorAgencyId(getDoctorIdByUcDoctorId(ucDoctorId));
	}

	/**
	 * 根据 ucDoctorId 获取老的医生 id
	 *
	 * @author liuqiuyi
	 * @date 2022/12/8 17:13
	 */
	public Long getDoctorIdByUcDoctorId(Long ucDoctorId) {
		if (Objects.isNull(ucDoctorId)) {
			return null;
		}
		try {
			Result<BusinessUserIdVO> result = ucUserFacade.ucId2businessId(UcUserIdDTO.builder().ucDoctorId(ucDoctorId).build());
			Long doctorId = result.getData().getDoctorId();
			return Objects.isNull(doctorId) ? 0L : doctorId;
		} catch (Exception e) {
			log.error("invoke ucUserFacade.ucId2businessId出现异常!请求的参数为:{}", ucDoctorId, e);
			return null;
		}
	}
}
