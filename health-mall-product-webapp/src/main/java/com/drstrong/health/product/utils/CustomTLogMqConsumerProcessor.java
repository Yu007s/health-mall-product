package com.drstrong.health.product.utils;

import com.yomahub.tlog.core.mq.TLogMqWrapBean;
import com.yomahub.tlog.core.rpc.TLogRPCHandler;

import java.util.function.Consumer;

/**
 * 自定义tlog的mq消费者处理器
 * @author lsx
 * @date 2022-1-10
 */
public class CustomTLogMqConsumerProcessor {

    private static TLogRPCHandler tLogRPCHandler = new TLogRPCHandler();

    public static void process(TLogMqWrapBean o, Consumer<TLogMqWrapBean> consume) {
       tLogRPCHandler.processProviderSide(o);
       try {
           consume.accept(o);
       }finally {
           tLogRPCHandler.cleanThreadLocal();
       }
    }
}
