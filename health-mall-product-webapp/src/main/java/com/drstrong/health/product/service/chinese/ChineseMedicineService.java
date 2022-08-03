package com.drstrong.health.product.service.chinese;

import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.request.chinese.ChineseMedicineRequest;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineVO;

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

    /**
     * 查询中药材分页展示
     * @param chineseMedicineRequest  查询请求信息
     * @return 查询所得列表
     */
    List<ChineseMedicineVO> queryPage(ChineseMedicineRequest chineseMedicineRequest);

    /**
     * 逻辑删除药材
     * @param medicineCode 药材编码
     * @return 是否成功删除
     */
    public boolean removeByCode(String medicineCode) ;

    /**
     * 条件查询所有的药材信息
     * @param chineseMedicineRequest 查询信息
     * @return 条件查询所有的药材信息
     */
    List<ChineseMedicineInfoResponse> queryAll(ChineseMedicineRequest chineseMedicineRequest);
    /**
     * 根据药材code获取中药材信息
     *
     * @param medicineCode 药材code
     * @author liuqiuyi
     * @date 2022/8/2 21:38
     */
    ChineseMedicineEntity getByMedicineCode(String medicineCode);
}
