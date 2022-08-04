package com.drstrong.health.product.service.chinese;

import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.request.chinese.ChineseMedicineRequest;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineVO;

import java.util.List;
import java.util.Set;

/**
 * @Author xieYueFeng
 * @Date 2022/07/27/16:36
 */
public interface ChineseMedicineService {
    /**
     * 新增中药材
     * @param chineseMedicineVO 中药材提交的相应信息
     * @param userId 用户id
     * @return 成功与否
     * @throws Exception 失败返回相应异常信息
     */
    boolean save(ChineseMedicineVO chineseMedicineVO,Long userId) throws Exception;

    /**
     * 查询中药材分页展示
     * @param chineseMedicineRequest  查询请求信息
     * @return 查询所得列表
     */
    List<ChineseMedicineResponse> queryPage(ChineseMedicineRequest chineseMedicineRequest);

    /**
     * 查询中药材分页展示  根据药材编码列表查找  带分页参数
     * @param medicineCode 药材编码  根据此编码查询相反药材列表
     * @param pageNo 分页起始页
     * @param pageSize 每页大小
     * @return 查询所得中药材列表
     */
    List<ChineseMedicineResponse> queryPageForConflict(String medicineCode, Integer pageNo, Integer pageSize);

    /**
     * 逻辑删除药材
     * @param medicineCode 药材编码
     * @param userId 当前操作用户id
     * @return 是否成功删除
     */
     boolean removeByCode(String medicineCode,Long userId) ;

    /**
     * 条件查询所有的药材信息
     * @param medicineName 药材名字
     * @param medicineCode 药材编码
     * @return 条件查询所有的药材信息
     */
    List<ChineseMedicineInfoResponse> queryAll(String medicineName,String medicineCode);
    /**
     * 根据药材code获取中药材信息
     *
     * @param medicineCode 药材code
     * @author liuqiuyi
     * @date 2022/8/2 21:38
     */
    ChineseMedicineEntity getByMedicineCode(String medicineCode);

	/**
	 * 根据药材code获取中药材信息
	 *
	 * @param medicineCodes 药材code
	 * @return 药材信息
	 * @author liuqiuyi
	 * @date 2022/8/4 15:32
	 */
	List<ChineseMedicineEntity> getByMedicineCode(Set<String> medicineCodes);

	/**
	 * 根据关键字模糊搜索
	 * <p>
	 * 支持药材名称、别名、药材拼音、别名拼音  搜索
	 * </>
	 *
	 * @param keyword 关键字
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2022/8/3 20:47
	 */
	List<ChineseMedicineEntity> likeQueryByKeyword(String keyword);
}
