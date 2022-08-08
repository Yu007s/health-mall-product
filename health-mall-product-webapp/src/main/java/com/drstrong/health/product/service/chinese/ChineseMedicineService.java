package com.drstrong.health.product.service.chinese;

import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineSearchVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineVO;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    boolean save(ChineseMedicineVO chineseMedicineVO);


	/**
	 *      * 查询中药材分页展示
	 * @param medicineCode
	 * @param medicineName
	 * @param pageNo
	 * @param pageSize
	 * @return 查询所得列表
	 */
    ChineseMedicineSearchVO queryPage(String medicineCode, String medicineName, Integer pageNo, Integer pageSize);

    /**
     * 查询中药材分页展示  根据药材编码列表查找  带分页参数
     * @param medicineCode 药材编码  根据此编码查询相反药材列表
     * @return 查询所得中药材列表
     */
    List<ChineseMedicineResponse> queryPageForConflict(String medicineCode);

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

	/**
	 * 根据老的药材 id 获取药材 code,组成 map
	 *
	 * @author liuqiuyi
	 * @date 2022/8/5 16:41
	 */
	Map<Long, String> getMedicineIdAndMedicineCodeMap(Set<Long> medicineIds);
}
