package com.drstrong.health.product.banner.dao.mybatis;

import cn.strong.mybatis.plus.extend.CustomBaseMapper;
import com.drstrong.health.product.model.entity.banner.Banner;
import com.drstrong.health.product.model.response.banner.BannerResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 轮播图 Mapper 接口
 * </p>
 *
 * @author mybatis plus generator
 * @since 2021-12-14
 */
@Mapper
public interface BannerMapper extends CustomBaseMapper<Banner> {

    /**
     * 获取轮播图
     * @param location 轮播图显示位置，1:首页
     * @return 轮播图
     */
    List<BannerResponse> getByLocation (@Param("location") Integer location, @Param("showStatus") Integer showStatus);

    /**
     * 新增数据,将比序号大的全部往后加一
     * @param sort 序列号
     */
    void updateFollowUp(@Param("sort") Integer sort);

    /**
     * 查找是否存在 大于当前 sort 的条目
     * @param sort 序列号
     * @return 条目数
     */
    @Transactional(readOnly = true)
    Integer getCount(@Param("sort") Integer sort);

    /**
     * 批量更新 展示状态
     * @param list list
     * @param showStatus
     * @return
     */
    Integer updateBannerStatus(@Param("list") List<Long> list, @Param("showStatus") Integer showStatus);
}
