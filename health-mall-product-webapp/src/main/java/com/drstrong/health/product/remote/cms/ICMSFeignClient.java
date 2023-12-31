package com.drstrong.health.product.remote.cms;

import com.drstrong.health.product.model.response.cms.DictVO;
import com.drstrong.health.product.model.response.cms.SkuProhibitAreaVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.model.DictResponse;
import com.drstrong.health.user.vo.SimpleUcUserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * cms 的远程接口,之前的项目没有打 jar 包...
 *
 * @author liuqiuyi
 * @date 2021/12/22 16:55
 */
@FeignClient(value = "health-cms")
public interface ICMSFeignClient {

	@GetMapping("/sys/dict/vue/query/{type}")
	DictResponse<List<DictVO>> vueQuery(@PathVariable("type") String type);

	@GetMapping("/inner/city/skuProhibitArea/list")
	ResultVO<List<SkuProhibitAreaVO>> list(@RequestParam("type") Integer type);

	@PostMapping("/inner/sys/user/simple/info/batch")
	ResultVO<List<SimpleUcUserVO>> simpleInfoByIdList(@RequestBody List<Long> idList);
}
