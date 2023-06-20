package com.drstrong.health.product.facade.medicine;


import com.drstrong.health.product.model.dto.medicine.MedicineWarehouseBaseDTO;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.response.PageVO;

/**
 * 药材库的公共方法入口，根据不同的商品类型交给不同的实现类处理
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:14
 */
public interface MedicineWarehouseBaseFacade {

    /**
     * 返回每个处理类的商品类型
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:41
     */
    ProductTypeEnum queryProductType();

    /**
     * 根据入参分页查询药材库信息
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:29
     */
    PageVO<MedicineWarehouseBaseDTO> pageQuery(MedicineWarehouseQueryRequest medicineWarehouseQueryRequest);
}
