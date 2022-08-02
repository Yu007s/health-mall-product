package com.drstrong.health.product.service.store.impl;

import com.drstrong.health.product.service.store.AgencyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/01/9:23
 */
@Service
public class AgencyServiceImpl implements AgencyService {
    private static final List<String> NAMES = new ArrayList<>(2);
    static {
        NAMES.add("桐庐互联网医院");
        NAMES.add("海南互联网医院");
    }
    @Override
    public String id2name(Long id) {
        if (id == null || id > NAMES.size() || id < 1) {
            return null;
        }
        return NAMES.get((int)(id-1));
    }
}
