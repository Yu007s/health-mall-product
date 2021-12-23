package com.drstrong.health.product.remote.cms;

import com.drstrong.health.product.model.response.cms.DictVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * cms 的远程接口,之前的项目没有打 jar 包...
 *
 * @author liuqiuyi
 * @date 2021/12/22 16:55
 */
@FeignClient(name = "cms", url = "sys/dict")
public interface ICMSFeignClient {

	@GetMapping("/vue/list/{type}")
	ResponseEntity<List<DictVO>> vueList(@PathVariable("type") String type);
}
