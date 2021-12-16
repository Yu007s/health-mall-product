package com.drstrong.health.product.banner.service.impl;


import cn.strong.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.constans.banner.BannerConstants;
import com.drstrong.health.product.banner.dao.mybatis.BannerMapper;
import com.drstrong.health.product.model.entity.banner.Banner;
import com.drstrong.health.product.model.request.banner.BannerListRequest;
import com.drstrong.health.product.model.request.banner.BannerRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.banner.BannerListResponse;
import com.drstrong.health.product.model.response.banner.BannerResponse;
import com.drstrong.health.product.banner.service.BannerService;
import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.drstrong.health.product.service.IRedisService;
import com.drstrong.health.redis.utils.RedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 轮播图 服务实现类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2021-12-14
 */
@Service
public class BannerServiceImpl extends CustomServiceImpl<BannerMapper, Banner> implements BannerService {

    @Resource
    BannerMapper bannerDao;
    @Resource
    IRedisService redisService;
    private static RedisUtils redisUtils;
    @Value("${banner.default.photo-url}")
    private String DEFAULT_PHOTO_URL;
    @Value("${banner.default.link}")
    private String DEFAULT_LINK;

    @Autowired
    public BannerServiceImpl(RedisUtils redisUtils) {
        BannerServiceImpl.redisUtils = redisUtils;
    }

    @Override
    public List<BannerResponse> get(Integer location, Integer pageSize) {
        List<BannerResponse> bannerResponses = new ArrayList<>();
        if (location == BannerConstants.LOCATION_INDEX) {
            bannerResponses = redisService.lrange(BannerConstants.BANNER_INDEX_KEY, BannerConstants.BANNER_INDEX_KEY_EXPIRE, 0, -1,
                    () -> bannerDao.getByLocation(location,location));
        }
        // 限制条目数量
        if (bannerResponses.size() > pageSize) {
            bannerResponses.stream().limit(pageSize);
        }
        // 数量不足，展示占位图
        if (bannerResponses.size() < pageSize) {
            do {
                BannerResponse banner = new BannerResponse(DEFAULT_PHOTO_URL, DEFAULT_LINK, 1, "");
                bannerResponses.add(banner);
            } while (bannerResponses.size() < pageSize);
        }
        return bannerResponses;
    }


    @Override
    public Boolean addOrUpdate(BannerRequest request) {
        Banner banner = new Banner();
        BeanUtils.copyProperties(request,banner);
        //判断状态： 1.开始时间大于当前：未上架
        //         2.当前日期大于开始时间，小于结束时间，已上架
        //         3.当前日期大于结束时间，已下架
        setBannerShowStatus(banner);
        if (this.bannerDao.getCount(banner.getSort())>0){
            this.bannerDao.updateFollowUp(banner.getSort());
        }
        if (banner.getId() == null) {
            this.save(banner);
        } else {
            this.updateById(banner);
        }
        // 如果 该轮播图上架 清空缓存
        if (banner.getShowStatus() == BannerConstants.ON_SALE){
            redisUtils.del(BannerConstants.BANNER_INDEX_KEY);
        }
        return true;
    }

    @Override
    public Integer pollingStatus() {
        List<Banner> list = this.list();
        List<Long> notOnSale = new ArrayList<>();
        List<Long> onSale = new ArrayList<>();
        List<Long> offOnSale = new ArrayList<>();
        for (Banner banner : list){
            Integer status=banner.getShowStatus();
            setBannerShowStatus(banner);
            if (!status.equals(banner.getShowStatus())){
                if (status == BannerConstants.NOT_ON_SALE) {notOnSale.add(banner.getId());}
                if (status == BannerConstants.ON_SALE) {onSale.add(banner.getId());}
                if (status == BannerConstants.OFF_ON_SHELF) {offOnSale.add(banner.getId());}
            }
        }
        Integer count = 0;
        if (CollectionUtils.isNotEmpty(notOnSale)){
            count += bannerDao.updateBannerStatus(onSale, BannerConstants.NOT_ON_SALE);}
        if (CollectionUtils.isNotEmpty(onSale)){
            count += bannerDao.updateBannerStatus(onSale, BannerConstants.ON_SALE);}
        if (CollectionUtils.isNotEmpty(offOnSale)){
            count += bannerDao.updateBannerStatus(onSale, BannerConstants.OFF_ON_SHELF);}
        return count;
    }

    private void setBannerShowStatus(Banner banner) {
        if (banner.getStartTime().after(new Date())) {
            banner.setShowStatus(BannerConstants.NOT_ON_SALE);
        } else if (banner.getStartTime().before(new Date()) && banner.getEndTime().after(new Date())) {
            banner.setShowStatus(BannerConstants.ON_SALE);
        } else if (banner.getEndTime().before(new Date())) {
            banner.setShowStatus(BannerConstants.OFF_ON_SHELF);
        }
    }

    @Override
    public PageVO<BannerListResponse> queryList(BannerListRequest request) {
        Page<BannerListResponse>  page = new Page<BannerListResponse> (request.getPageNo(),request.getPageSize());
        bannerDao.queryList( request.getBannerName(),request.getShowStatus(), page);
        List<BannerListResponse> list = page.getRecords();
        if (CollectionUtils.isNotEmpty(list)){
            list.forEach(i->{
                if (i.getLinkType() != 2){
                    i.setMasterImageUrl(null);
                }
            });
            return PageVO.toPageVo(list,page.getTotal(),page.getCurrent(),page.getTotal());
        }else {
            return PageVO.emptyPageVo(request.getPageNo(),request.getPageSize());
        }
    }
}
