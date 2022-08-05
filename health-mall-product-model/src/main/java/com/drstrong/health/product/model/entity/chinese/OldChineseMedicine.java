package com.drstrong.health.product.model.entity.chinese;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 老的中药材表
 * FIXME 仅作为数据修复使用,不要用于其它用途
 *
 * @author liuqiuyi
 * @date 2022/8/5 14:48
 */
@Data
public class OldChineseMedicine implements Serializable {
	private static final long serialVersionUID = 6721808163120768686L;

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 物料编号
	 */
	private String itemNo;

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 药材名称
	 */
	private String name;

	/**
	 * 相克药品名
	 */
	private String conflictName;

	/**
	 * 汉语简拼
	 */
	private String pinyin;

	/**
	 * 分组id
	 */
	private Long groupId;

	/**
	 * 分组名称
	 */
	private String groupName;

	/**
	 * 颗粒/饮片转化系数
	 */
	private BigDecimal coefficient;

	/**
	 * 最大剂量
	 */
	private BigDecimal maxDosage;

	/**
	 * 每克药价
	 */
	private BigDecimal gramPrice;

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
	 * 作废状态：0 正常 1 作废
	 */
	private Integer invalid;
}
