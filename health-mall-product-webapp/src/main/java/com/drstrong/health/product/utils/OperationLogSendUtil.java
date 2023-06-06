package com.drstrong.health.product.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.common.log.HealthLog;
import com.drstrong.health.common.mq.BaseMessage;
import com.drstrong.health.common.utils.IdWorker;
import com.drstrong.health.product.config.MqTopicConfig;
import com.drstrong.health.product.model.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuqiuyi
 * @date 2022/11/1 11:24
 */
@Slf4j
@Component
public class OperationLogSendUtil extends MqMessageUtil {
	@Resource
	MqTopicConfig mqTopicConfig;

	/**
	 * 发送 product 操作日志
	 *
	 * @author liuqiuyi
	 * @date 2022/11/1 11:53
	 */
	public void sendOperationLog(OperationLog operationLog) {
		try {
			Map<String, String> contentMaps = new HashMap<>(8);
			contentMaps.put("operateContentType", operationLog.getOperationType());
			contentMaps.put("operateContent", operationLog.getOperateContent());
			contentMaps.put("changeBeforeData", operationLog.getChangeBeforeData());
			contentMaps.put("changeAfterData", operationLog.getChangeAfterData());

			HealthLog healthLog = HealthLog.builder()
					.source(UUID.randomUUID().toString())
					.tag(mqTopicConfig.getLogTag())
					.businessId(operationLog.getBusinessId())
					.createdBy(StrUtil.blankToDefault(operationLog.getOperationUserName(), CharSequenceUtil.EMPTY))
					.createdById(ObjectUtil.isNull(operationLog.getOperationUserId()) ? "" : operationLog.getOperationUserId().toString())
					.uploaderType(operationLog.getOperationUserType())
					.createdAt(new Date())
					.contentMaps(contentMaps)
					.build();
			String content = JSONUtil.toJsonStr(healthLog);

			BaseMessage baseMessage = new BaseMessage();
			baseMessage.setContent(content);
			baseMessage.setMessageId(IdWorker.get32UUID());

			super.sendMsg(mqTopicConfig.getLogTopic(), mqTopicConfig.getLogTag(), baseMessage);
		} catch (Throwable e) {
			log.error("发送商品操作日志失败,参数为:{},异常信息为:{}", JSONUtil.toJsonStr(operationLog), e);
		}
	}
}
