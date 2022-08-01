package com.drstrong.health.product.service.chineseMedicine;

import com.drstrong.health.product.model.request.ChineseMedicine.ChineseMedicineRequest;
import com.drstrong.health.product.model.response.chineseMedicine.ChineseMedicineVO;

import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/27/16:36
 */
public interface ChineseMedicineService {
    /**
     * 新增中药材
     * @param chineseMedicineVO 中药材提交的相应信息
     * @return 成功与否
     */
    boolean save(ChineseMedicineVO chineseMedicineVO) ;
    List<ChineseMedicineVO> queryPage(ChineseMedicineRequest chineseMedicineRequest);

}
