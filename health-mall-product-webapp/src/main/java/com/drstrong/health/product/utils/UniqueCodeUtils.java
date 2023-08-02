package com.drstrong.health.product.utils;

import cn.hutool.extra.pinyin.PinyinUtil;
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

    private static final String HAN_ZI = "[^\\u4e00-\\u9fa5]";

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

    public static String getNextMedicineCode(String medicineName){
        // 生成编码的时候，去除特殊字符，仅保留汉字
        String replaceName = medicineName.replaceAll(HAN_ZI, "");
        String firstLetter = PinyinUtil.getFirstLetter(replaceName.substring(0, Math.min(2, replaceName.length())), "");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Z");
        stringBuilder.append(firstLetter);
        stringBuilder.append(DateUtil.formatDate(new Date(), "MMddyy"));
        IRedisService redisService = ApplicationContextHolder.getInstance().getBean(IRedisService.class);
        long serialNumber = redisService.incr(RedisKeyUtils.getMedicineCodeNum());
        stringBuilder.append(String.format("%05d", serialNumber));
        return stringBuilder.toString();
    }
}
