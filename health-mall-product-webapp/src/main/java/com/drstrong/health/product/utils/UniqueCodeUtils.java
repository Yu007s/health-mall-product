package com.drstrong.health.product.utils;

import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.redis.IRedisService;
import com.drstrong.health.product.util.DateUtil;
import com.drstrong.health.product.util.RedisKeyUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * 唯一键生成工具类
 *
 * @author liuqiuyi
 * @date 2022/8/1 21:37
 */
public class UniqueCodeUtils {

    /**
     * 获取 spuCode
     * <p> 参照之前的写法 </>
     *
     * @author liuqiuyi
     * @date 2022/8/1 21:41
     */
    public static String getNextSpuCode(ProductTypeEnum productTypeEnum) {
        if (Objects.isNull(productTypeEnum)) {
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        IRedisService redisService = ApplicationContextHolder.getInstance().getBean(IRedisService.class);
        // 生成规则：药品编码M开头 + 建码日期六位：年后两位+月份+日期（190520）+ 5位顺序码    举例：M19052000001
        StringBuilder number = new StringBuilder();
        number.append(productTypeEnum.getMark());
        number.append(DateUtil.formatDate(new Date(), "yyMMdd"));
        long serialNumber = redisService.incr(RedisKeyUtils.getSerialNum());
        number.append(String.format("%05d", serialNumber));
        return number.toString();
    }

    /**
     * 获取 skuCode
     * <p> 参照之前的写法 </>
     *
     * @author liuqiuyi
     * @date 2022/8/1 21:44
     */
    public static String getNextSkuCode(String spuCode, Long productId) {
        if (Objects.isNull(productId) || StringUtils.isBlank(spuCode)) {
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        IRedisService redisService = ApplicationContextHolder.getInstance().getBean(IRedisService.class);
        long serialNumber = redisService.incr(RedisKeyUtils.getSkuNum(productId));
        return spuCode + "-" + serialNumber;
    }
}
