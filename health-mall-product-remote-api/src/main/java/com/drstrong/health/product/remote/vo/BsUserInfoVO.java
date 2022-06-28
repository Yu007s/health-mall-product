package com.drstrong.health.product.remote.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: llq
 * @Date: 2022/06/27
 * @Description:
 */
@Data
public class BsUserInfoVO implements Serializable{

    private static final long serialVersionUID = 951114342535868541L;
    
    /**
     * 代表Id
     */
    private Long representId;
    
    private String userName;
    
    private String erpId;
    /**
     * 部门Id
     */
    private Long departmentId;

    /**
     * 职位Id
     */
    private Long titleId;

    /**
     * 账户状态，0在职，1离职
     */
    private Integer accountStatus;

    /**
     * 用户中心ID
     */
    private Long ucUserId;

    /**
     * 修改人
     */
    private String changedBy;

    /**
     * 修改时间
     */
    private Date changedAt;
    

    public BsUserInfoVO(Long representId, String changedBy, Date changedAt) {
        this.representId = representId;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
    }

    public BsUserInfoVO() {
    }
}