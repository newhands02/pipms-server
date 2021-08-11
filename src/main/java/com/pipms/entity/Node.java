package com.pipms.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName Node
 * @Description 操作节点信息
 * @Author 661595
 * @Date 2021/6/2815:40
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("project_node")
public class Node implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 项目编号
     */
    private String projectNumber;
    /**
     * 当前节点状态
     */
    private Integer currentState;

    /**
     * 操作
     */
    private String operation;
    /**
     * 操作人账号
     */
    private String account;
    /**
     * 操作人姓名
     */
    private String operator;
    /**
     * 操作人意见
     */
    private String comment;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    private LocalDateTime operateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
    /**
     * 版本号
     */
    @Version
    private Integer version;
    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public Integer getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Integer currentState) {
        this.currentState = currentState;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public LocalDateTime getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

