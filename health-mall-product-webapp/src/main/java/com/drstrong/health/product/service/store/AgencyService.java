package com.drstrong.health.product.service.store;

import java.util.List;

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

    /**
     * 根据name转换为互联医院id
     * @param name 互联网医院名字
     * @return 互联网医院id
     */
    Long name2Id(String name);

    List<String> getAllName();
}
