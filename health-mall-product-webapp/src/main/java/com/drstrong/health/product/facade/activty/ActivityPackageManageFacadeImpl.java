package com.drstrong.health.product.facade.activty;

import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ActivityPackageInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * huangpeng
 * 2023/7/5 17:51
 */
@Slf4j
@Service
public class ActivityPackageManageFacadeImpl implements ActivityPackageManageFacade{


    @Override
    public PageVO<ActivityPackageInfoVO> queryActivityPackageManageInfo(ActivityPackageManageQueryRequest productManageQueryRequest) {
        return null;
    }

    @Override
    public List<ActivityPackageInfoVO> listActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        return null;
    }

    @Override
    public void addLocksaveOrUpdateActivityPackage(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest, String skuCode) {

    }
}
