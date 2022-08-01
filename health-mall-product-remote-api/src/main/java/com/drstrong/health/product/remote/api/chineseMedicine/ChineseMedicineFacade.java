package com.drstrong.health.product.remote.api.chineseMedicine;

import com.drstrong.health.product.model.request.ChineseMedicine.ChineseMedicineRequest;
import com.drstrong.health.product.model.response.chineseMedicine.ChineseMedicineVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/27/10:22
 */
@FeignClient(value = "health-mall-product", path = "/product/chineseMedicine")
public interface ChineseMedicineFacade {

}
