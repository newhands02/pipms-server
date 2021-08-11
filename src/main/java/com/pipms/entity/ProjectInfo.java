package com.pipms.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pipms.component.ByteConvert;
import com.pipms.component.LocalDateTimeConvert;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 项目信息表
 * </p>
 *
 * @author xyj
 * @since 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ContentRowHeight(15)
@HeadFontStyle(fontHeightInPoints = 10,bold = true)
@TableName("project_info")
public class ProjectInfo extends ParentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属版块
     */
    @ExcelProperty(index = 1,value = "所属版块")
    private String section;

    /**
     * 落地单位
     */
    @ExcelProperty(index = 2,value = "落地单位")
    private String unit;

    /**
     * 单位代号+年月+4位数字
     */
    @ExcelProperty(index = 3,value = "项目编号")
    private String projectNumber;

    /**
     * 项目名称
     */
    @ExcelProperty(index = 4,value = "项目编号")
    private String projectTitle;

    /**
     * 详细落地位置，车间+具体岗位
     */
    @ExcelProperty(index = 5,value = "详细落地位置")
    private String projectAddress;

    /**
     * 项目提出人
     */
    @ExcelProperty(index = 6,value = "项目提出人")
    private String projectSponsor;
    /**
     * 项目提出人邮箱
     */
    @ExcelIgnore
    private String sponsorMail;

    /**
     * 项目推进人
     */
    @ExcelProperty(index = 7,value = "项目推进人")
    private String projectPromoter;
    /**
     * 项目推进人邮箱
     */
    @ExcelIgnore
    private String promoterMail;
    /**
     * 项目成员
     */
    @ExcelProperty(index = 8,value = "项目成员")
    private String projectMember;

    /**
     * 单位管理员
     */
    @ExcelProperty(index = 9,value = "单位管理员")
    private String unitAdministrator;
    /**
     * 单位管理员邮箱
     */
    @ExcelIgnore
    private String administratorMail;

    /**
     * 单位审核员
     */
    @ExcelProperty(index = 10,value = "单位审核员")
    private String unitAuditor;
    /**
     * 单位审核员邮箱
     */
    @ExcelIgnore
    private String auditorMail;

    /**
     * 项目类型，固定的几种类型
     */
    @ExcelProperty(index = 11,value = "项目类型")
    private String projectType;

    /**
     * 是否自动化
     */
    @ExcelProperty(index = 12,value = "是否自动化",converter = ByteConvert.class)
    private Boolean automaticFlag;

    /**
     * 项目改善前描述
     */
    @ExcelProperty(index = 13,value = "项目改善前描述")
    private String projectImprovementBefore;

    /**
     * 改善方案
     */
    @ExcelProperty(index = 14,value = "改善方案")
    private String improvePlan;

    /**
     * 项目改善后描述
     */
    @ExcelProperty(index = 15,value = "项目改善后描述")
    private String projectImprovementAfter;

    /**
     * 涉及产品类型
     */
    @ExcelProperty(index = 16,value = "涉及产品类型")
    private String projectTypeInvolved;

    /**
     * 项目控员成果，保留一位小数
     */
    @ExcelProperty(index = 17,value = "项目控员成果")
    private BigDecimal projectResult;

    /**
     * 成果类型，两种
     */
    @ExcelProperty(index = 18,value = "成果类型")
    private String resultType;

    /**
     * 改善前视频路径
     */
    @ExcelIgnore
    private String improvementBeforeVideo;

    /**
     * 改善后视频路径
     */
    @ExcelIgnore
    private String improvementAfterVideo;

    /**
     * 相关附件1
     */
    @ExcelIgnore
    private String fileRelateOne;

    /**
     * 相关附件2
     */
    @ExcelIgnore
    private String fileRelateTwo;

    /**
     * 填写时间
     */
    @ExcelProperty(index = 19,value = "填写时间")
    private String writeTime;
    /**
     * 创建人账号
     */
    @ExcelIgnore
    private String createAccount;


    /**
     * 结题时间
     */
    @ExcelProperty(index = 20,value = "结题时间")
    private String finishTime;

    /**
     * 当前状态
     */
    @ExcelProperty(index = 21,value = "当前状态")
    private String currentState;
    /**
     * 当前状态标识1-3
     */
    @ExcelIgnore
    private Integer currentStateFlag;

    /**
     * 创建时间
     */
    @ExcelProperty(index = 22,value = "创建时间",converter = LocalDateTimeConvert.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ExcelProperty(index = 23,value = "更新时间",converter = LocalDateTimeConvert.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 逻辑删除
     */
    @ExcelIgnore
    @TableLogic
    private Integer deleted;

    /**
     * 乐观锁
     */
    @ExcelIgnore
    @Version
    private Integer version;


    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProjectNumber() {
        return projectNumber;
    }
    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getProjectSponsor() {
        return projectSponsor;
    }

    public void setProjectSponsor(String projectSponsor) {
        this.projectSponsor = projectSponsor;
    }

    public String getProjectPromoter() {
        return projectPromoter;
    }

    public void setProjectPromoter(String projectPromoter) {
        this.projectPromoter = projectPromoter;
    }

    public String getProjectMember() {
        return projectMember;
    }

    public void setProjectMember(String projectMember) {
        this.projectMember = projectMember;
    }

    public String getUnitAdministrator() {
        return unitAdministrator;
    }

    public void setUnitAdministrator(String unitAdministrator) {
        this.unitAdministrator = unitAdministrator;
    }

    public String getUnitAuditor() {
        return unitAuditor;
    }

    public void setUnitAuditor(String unitAuditor) {
        this.unitAuditor = unitAuditor;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Boolean getAutomaticFlag() {
        return automaticFlag;
    }

    public void setAutomaticFlag(Boolean automaticFlag) {
        this.automaticFlag = automaticFlag;
    }

    public String getProjectImprovementBefore() {
        return projectImprovementBefore;
    }

    public void setProjectImprovementBefore(String projectImprovementBefore) {
        this.projectImprovementBefore = projectImprovementBefore;
    }

    public String getImprovePlan() {
        return improvePlan;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getSponsorMail() {
        return sponsorMail;
    }

    public void setSponsorMail(String sponsorMail) {
        this.sponsorMail = sponsorMail;
    }

    public String getPromoterMail() {
        return promoterMail;
    }

    public void setPromoterMail(String promoterMail) {
        this.promoterMail = promoterMail;
    }

    public String getAdministratorMail() {
        return administratorMail;
    }

    public void setAdministratorMail(String administratorMail) {
        this.administratorMail = administratorMail;
    }

    public String getAuditorMail() {
        return auditorMail;
    }

    public void setAuditorMail(String auditorMail) {
        this.auditorMail = auditorMail;
    }

    public void setImprovePlan(String improvePlan) {
        this.improvePlan = improvePlan;
    }

    public String getProjectImprovementAfter() {
        return projectImprovementAfter;
    }

    public void setProjectImprovementAfter(String projectImprovementAfter) {
        this.projectImprovementAfter = projectImprovementAfter;
    }

    public String getProjectTypeInvolved() {
        return projectTypeInvolved;
    }

    public void setProjectTypeInvolved(String projectTypeInvolved) {
        this.projectTypeInvolved = projectTypeInvolved;
    }

    public BigDecimal getProjectResult() {
        return projectResult;
    }

    public void setProjectResult(BigDecimal projectResult) {
        this.projectResult = projectResult;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getImprovementBeforeVideo() {
        return improvementBeforeVideo;
    }

    public void setImprovementBeforeVideo(String improvementBeforeVideo) {
        this.improvementBeforeVideo = improvementBeforeVideo;
    }

    public String getImprovementAfterVideo() {
        return improvementAfterVideo;
    }

    public void setImprovementAfterVideo(String improvementAfterVideo) {
        this.improvementAfterVideo = improvementAfterVideo;
    }

    public String getFileRelateOne() {
        return fileRelateOne;
    }

    public void setFileRelateOne(String fileRelateOne) {
        this.fileRelateOne = fileRelateOne;
    }

    public String getFileRelateTwo() {
        return fileRelateTwo;
    }

    public void setFileRelateTwo(String fileRelateTwo) {
        this.fileRelateTwo = fileRelateTwo;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public Integer getCurrentStateFlag() {
        return currentStateFlag;
    }

    public void setCurrentStateFlag(Integer currentStateFlag) {
        this.currentStateFlag = currentStateFlag;
    }
}
