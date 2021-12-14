package com.drstrong.health.product.model.constans.banner;

/**
 * @description: 轮播图相关常量
 * @Author: JiaoYuSheng
 * @Date: 2021-12-14 10:21
 * @program health-mall-postsale
 */
public class BannerConstants {
    /**
     * 轮播图显示位置，1:首页
     */
    public static final int LOCATION_INDEX = 1;
    /**
     * 状态：未上架
     */
    public static final int NOT_ON_SALE = 0;
    /**
     * 状态：已上架
     */
    public static final int ON_SALE = 1;
    /**
     * 状态：已下架
     */
    public static final int OFF_ON_SHELF = 2;

    /*以下是redis相关*/

    /**
     * 轮播图-商城首页-redis key
     */
    public static final String BANNER_INDEX_KEY = "mall:banner:index";

    /**
     * 轮播图-商城首页-redis 失效时间
     */
    public static final int BANNER_INDEX_KEY_EXPIRE = 5 * 60;

}
