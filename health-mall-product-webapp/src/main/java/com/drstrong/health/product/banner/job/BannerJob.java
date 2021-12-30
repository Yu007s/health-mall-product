package com.drstrong.health.product.banner.job;


import com.drstrong.health.common.utils.SpringContextHolder;
import com.drstrong.health.product.banner.service.BannerService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;


/**
 * @Author: 焦宇盛
 * @Date: 2021/6/28 15:42
 */
@Slf4j
public class BannerJob {

    @Resource
    private BannerService bannerService;

    /**
     * 更新轮播图上下架状态
     */
    @XxlJob("pollingBannerStatus")
    public ReturnT<String> changeAllActivityTimeStatus(String param){
        System.out.println("task running pollingBannerStatus");
        XxlJobLogger.log("xxl-job, pollingBannerStatus start.");
        log.info("task running pollingBannerStatus");
        Integer count = 0;
        try {
            count =bannerService.pollingStatus();
        } catch (Exception e) {
            log.info("changeAllActivityTimeStatus fail", e);
            return ReturnT.FAIL;
        }
        log.info("定时任务，更新轮播图上下架状态-----------修改行数:{}",count);
        XxlJobLogger.log("xxl-job, pollingBannerStatus end.");
        return ReturnT.SUCCESS;
    }

}
