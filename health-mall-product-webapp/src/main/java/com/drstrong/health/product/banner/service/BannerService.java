package com.drstrong.health.product.banner.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.banner.Banner;
import com.drstrong.health.product.model.request.banner.BannerListRequest;
import com.drstrong.health.product.model.request.banner.BannerRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.banner.BannerListResponse;
import com.drstrong.health.product.model.response.banner.BannerResponse;

import java.util.List;

/**
 * <p>
 * 轮播图 服务类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2021-12-14
 */
public interface BannerService extends IService<Banner> {

    /**
     * 获取可展示的轮播图
     * @param location 展示位置
     * @param pageSize 展示条目数
     * @return 可展示的轮播图
     */
    List<BannerResponse> get (Integer location, Integer pageSize);

    /**
     * 添加/修改 轮播图
     * @param request 请求
     * @return 成功/失败
     */
    Boolean addOrUpdate(BannerRequest request);

    /**
     * 轮询更新轮播图状态
     * @return 更新条目数量
     */
    Integer pollingStatus();

    /**
     * 分页 条件 查询 轮播图列表
     * @param request 查询请求
     * @return 轮播图
     */
    PageVO<BannerListResponse> queryList(BannerListRequest request);
}
