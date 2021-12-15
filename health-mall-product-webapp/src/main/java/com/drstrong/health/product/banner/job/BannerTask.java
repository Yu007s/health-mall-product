package com.drstrong.health.product.banner.job;


import com.drstrong.health.product.banner.service.BannerService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @Author: 焦宇盛
 * @Date: 2021/6/28 15:42
 */
@Component
@Slf4j
public class BannerTask {

    @Resource
    private BannerService bannerService;

    /**
     * 更新轮播图上下架状态
     */
    @XxlJob("pollingBannerStatus")
    public ReturnT<String> changeAllActivityTimeStatus(){
        log.info("task running pollingBannerStatus");
        Integer count = bannerService.pollingStatus();
        log.info("定时任务，更新轮播图上下架状态-----------修改行数:{}",count);
        return ReturnT.SUCCESS;
    }

}
