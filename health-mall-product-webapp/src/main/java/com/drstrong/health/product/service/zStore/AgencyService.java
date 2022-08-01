package com.drstrong.health.product.service.zStore;

/**
 * 关联互联网医院id 名字映射
 * @Author xieYueFeng
 * @Date 2022/08/01/9:19
 */

public interface AgencyService {
    /**
     * 根据互联网医院id查找互联网医院名字
     * @param id 互联网医院id
     * @return 互联网医院名字
     */
    String  id2name(Long id);
}
