package com.drstrong.health.product.model.entity.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 老的桐庐邮费
 * FIXME 仅作为数据修复使用,不要用于其它用途
 *
 * @author lsx
 * @date 2022/8/5 14:48
 */
@Data
@TableName("freight_pharmacy_area")
public class OldAreaFreight implements Serializable {
	private static final long serialVersionUID = 6721808163120768686L;

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 医院自有药房ID
	 */
	private Long warehouseId;

	/**
	 * 第三方药店ID
	 */
	private Long pharmacyId;

	/**
	 * 邮费区域ID
	 */
	private Integer areaId;

	/**
	 * 邮费区域名称
	 */
	private String areaName;

	/**
	 * 地区邮费（分）
	 */
	private Integer freightPrice;

	
	/**
	 * 创建时间
	 */
	private Date createdAt;

	/**
	 * 创建人
	 */
	private String createdBy;

	/**
	 * 修改时间
	 */
	private Date changedAt;

	/**
	 * 修改人
	 */
	private String changedBy;

	/**
	 * 乐观锁
	 */
	private Integer version;

	/**
	 * 是否删除 0未删除，1删除
	 */
	private Integer delFlag;
}
