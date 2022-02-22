package com.drstrong.health.product.utils;

import com.drstrong.health.common.exception.BusinessException;
import com.yomahub.tlog.core.mq.TLogMqWrapBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc mq消息发送
 * @createTime 2021/12/26 16:22
 * @since TODO
 */
@Slf4j
@Component
public class MqMessageUtil {

    private static final Long PRODUCER_TIME_OUT = 3000L;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送普通mq消息
     *
     * @param topic
     * @param tag
     * @param o
     */
    public void sendMsg(String topic, String tag, Object o) {
        TLogMqWrapBean tLogMqWrapBean = new TLogMqWrapBean(o);
        Message<TLogMqWrapBean> message = MessageBuilder.withPayload(tLogMqWrapBean).build();
        String destination = "";
        if (StringUtils.isNotEmpty(tag)) {
            destination = String.format("%s:%s", topic, tag);
        } else {
            destination = topic;
        }
        SendResult sendResult = rocketMQTemplate.syncSend(destination, message, PRODUCER_TIME_OUT);
        if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.info("send message to mq success,topic:[{}], tag:[{}], content:[{}]", topic, tag, message);
        } else {
            log.error("send message to mq failed,topic:[{}], tag:[{}], content:[{}], rocketMq sendResult:[{}]", topic, tag, message, sendResult);
            throw new BusinessException("发送mq消息失败");
        }
    }


    /**
     * 发送延时消息
     * RocketMQ不支持任意时间的延时，默认支持以下几个固定的延时等级，如1m就用5表示，10s用3表示
     * 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     * {@link com.drstrong.health.common.enums.MqDelayLevelEnum}
     *
     * @param topic
     * @param tag
     * @param delayLevel
     * @param o
     */
    public void sendDelayMsg(String topic, String tag, int delayLevel, Object o) {
        Message<Object> message = MessageBuilder.withPayload(o).build();
        String destination = "";
        if (StringUtils.isNotEmpty(tag)) {
            destination = String.format("%s:%s", topic, tag);
        } else {
            destination = topic;
        }
        SendResult sendResult = rocketMQTemplate.syncSend(destination, message, PRODUCER_TIME_OUT, delayLevel);
        if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
            log.info("send delay message to mq success,topic:[{}], tag:[{}], delayLevel:[{}], content:[{}]", topic, tag, delayLevel, message);
        } else {
            log.error("send delay message to mq failed,topic:[{}], tag:[{}], delayLevel:[{}], content:[{}], rocketMq sendResult:[{}]", topic, tag, delayLevel, message, sendResult);
            throw new BusinessException("发送mq消息失败");
        }
    }

    /**
     * 发送异步消息 在SendCallback中可处理相关成功失败时的逻辑
     */
    public void sendAsyncMsg(String topic,String tag,Object o){
        Message<Object> message = MessageBuilder.withPayload(o).build();
        String destination = "";
        if (StringUtils.isNotEmpty(tag)) {
            destination = String.format("%s:%s", topic, tag);
        } else {
            destination = topic;
        }
        rocketMQTemplate.asyncSend(destination,message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("send async message to mq success,topic:[{}], tag:[{}], content:[{}]", topic, tag, message);
            }
            @Override
            public void onException(Throwable e) {
                log.error(e.getMessage());
                throw new BusinessException("异步消息发送失败");
            }
        });
    }
}
