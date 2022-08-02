package com.drstrong.health.product.model.entity.store;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * @author xieYueFeng
 * @since 2022-07-206 15:59:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_store_invoice")
public class StoreInvoiceEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3583447778043545858L;

    /**
     * 主键id 自增
     **/
    @TableId("id")
    private Long id;

    /**
     * 关联店铺id 此列建立索引
     * **/
    @TableField("store_id")
    private Long storeId;

    /**
     * 企业税号  最长50位  只有英文、数字
     * **/
    @TableField("enterprise_tax_number")
    private String enterpriseTaxNumber;

    /**
     * 企业开户行银行账号
     * **/
    @TableField("enterprise_bank_account")
    private String enterpriseBankAccount;

    /**
     * 企业电话
     * **/
    @TableField("enterprise_tel_number")
    private String enterpriseTelNumber;

    /**
     * 企业地址
     * **/
    @TableField("enterprise_address")
    private String enterpriseAddress;

    /**
     * 开票员
     * **/
    @TableField("drawer")
    private String drawer;

    /**
     * 收款人
     * */
    @TableField("payee")
    private String payee;

    /**
     * 复核人
     * **/
    @TableField("checker")
    private String checker;

    /**
     * 应用密钥
     * **/
    @TableField("app_secret")
    private String appSecret;

    /**
     * 应用key
     * */
    @TableField("app_key")
    private String appKey;

    /**
     * 乐观锁
     */
    private Integer version;

}
