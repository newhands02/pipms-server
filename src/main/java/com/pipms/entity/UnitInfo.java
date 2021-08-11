package com.pipms.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 单位信息表
 * </p>
 *
 * @author xyj
 * @since 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("unit_info")
public class UnitInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 单位对应版块
     */
    private String unitSection;

    /**
     * 单位在系统中名称
     */
    private String unitSysName;

    /**
     * 单位在真实名称
     */
    private String unitRealName;

    /**
     * 单位代码
     */
    private String unitCode;

    /**
     * 单位管理员
     */
    private String unitAdmin;
    /**
     * 管理员邮箱
     */
    private String adminMail;

    /**
     * 单位审核员
     */
    private String unitAuditor;

    /**
     * 审核员邮箱
     */
    private String auditorMail;

    /**
     * 维护人姓名
     */
    private String lastMaintainUser;

    /**
     * 数据更新时间
     */
    private LocalDateTime lastUpdateTime;

    /**
     * 数据创建时间
     */
    private LocalDateTime createTime;

    /**
     * 数据删除时间
     */
    private LocalDateTime deleteTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitSection() {
        return unitSection;
    }

    public void setUnitSection(String unitSection) {
        this.unitSection = unitSection;
    }

    public String getUnitSysName() {
        return unitSysName;
    }

    public void setUnitSysName(String unitSysName) {
        this.unitSysName = unitSysName;
    }

    public String getUnitRealName() {
        return unitRealName;
    }

    public void setUnitRealName(String unitRealName) {
        this.unitRealName = unitRealName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitAdmin() {
        return unitAdmin;
    }

    public void setUnitAdmin(String unitAdmin) {
        this.unitAdmin = unitAdmin;
    }

    public String getAdminMail() {
        return adminMail;
    }

    public void setAdminMail(String adminMail) {
        this.adminMail = adminMail;
    }

    public String getUnitAuditor() {
        return unitAuditor;
    }

    public void setUnitAuditor(String unitAuditor) {
        this.unitAuditor = unitAuditor;
    }

    public String getAuditorMail() {
        return auditorMail;
    }

    public void setAuditorMail(String auditorMail) {
        this.auditorMail = auditorMail;
    }

    public String getLastMaintainUser() {
        return lastMaintainUser;
    }

    public void setLastMaintainUser(String lastMaintainUser) {
        this.lastMaintainUser = lastMaintainUser;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(LocalDateTime deleteTime) {
        this.deleteTime = deleteTime;
    }
}
