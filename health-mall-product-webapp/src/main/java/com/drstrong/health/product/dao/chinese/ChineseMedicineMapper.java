package com.drstrong.health.product.dao.chinese;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 中药材库 Mapper 接口
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Mapper
public interface ChineseMedicineMapper extends BaseMapper<ChineseMedicineEntity> {

	List<ChineseMedicineEntity> likeQueryByKeyword(@Param("keyword") String keyword);

	List<ChineseMedicineEntity> queryByMedicineIds(@Param("medicineIds") Set<Long> medicineIds);

	/**
	 * 批量插入  数据迁移
	 * @param chineseMedicines 需要迁移的新数据
	 */
	void insertBatch(@Param("chineseMedicines") List<ChineseMedicineEntity> chineseMedicines);

	ChineseMedicineEntity getByMedicineCodeIgnoreDel(@Param("medicineCode") String medicineCode);

    Page<ChineseMedicineEntity> pageQueryByRequest(Page<ChineseSkuInfoEntity> entityPage, @Param("queryParam") MedicineWarehouseQueryRequest queryParam);
}
