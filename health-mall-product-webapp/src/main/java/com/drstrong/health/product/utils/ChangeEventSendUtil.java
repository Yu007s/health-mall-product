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
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.mq.model.change.MedicineWarehouseChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品、药材库、sku 等变动事件的统一推送入口
 *
 * @author liuqiuyi
 * @date 2022/11/1 11:24
 */
@Slf4j
@Component
public class ChangeEventSendUtil extends MqMessageUtil {
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
					.source(operationLog.getOperationType())
					.tag(mqTopicConfig.getLogTag())
					.businessId(operationLog.getBusinessId())
					.createdBy(StrUtil.blankToDefault(operationLog.getOperationUserName(), CharSequenceUtil.EMPTY))
					.createdById(ObjectUtil.isNull(operationLog.getOperationUserId()) ? "" : operationLog.getOperationUserId().toString())
					.uploaderType(operationLog.getOperationUserType())
					.createdAt(new Date())
					.contentMaps(contentMaps)
					.objectId(UUID.randomUUID().toString())
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

    /**
     * 发送 药材库名称 变动事件
	 * <p> 由于ware服务冗余了药材名称，所以需要在信息变更后发送MQ告知修改 </>
     *
     * @author liuqiuyi
     * @date 2023/8/3 10:42
     */
    public void sendMedicineWarehouseChangeEvent(String uniqueCode, ProductTypeEnum productTypeEnum) {
		MedicineWarehouseChangeEvent medicineWarehouseChangeEvent = MedicineWarehouseChangeEvent.builder()
				.medicineCode(uniqueCode)
				.productType(productTypeEnum.getCode())
				.build();
		try {
			super.sendMsg(mqTopicConfig.getProductChangeTopic(), mqTopicConfig.getProductChangeTag(), medicineWarehouseChangeEvent);
        } catch (Throwable e) {
            log.error("发送药材库变更操作失败,参数为:{},异常信息为:{}", JSONUtil.toJsonStr(medicineWarehouseChangeEvent), e);
        }
    }
}
