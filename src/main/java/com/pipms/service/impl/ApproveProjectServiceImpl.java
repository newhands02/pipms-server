package com.pipms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pipms.entity.ProjectInfo;
import com.pipms.mapper.ProjectInfoMapper;
import com.pipms.service.IProjectInfoService;
import com.pipms.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ApproveProjectServiceImpl
 * @Description //TODO 这里代码可以进行重构，面向接口编程
 * @Author 661595
 * @Date 2021/7/1411:30
 * @Version 1.0
 **/
@Service
public class ApproveProjectServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements IProjectInfoService {
    @Autowired
    private NodeServiceImpl nodeService;
    @Autowired
    private EmailUtils emailUtils;
    /**
     *@Author xyj
     *@Description 管理员需要审核的项目列表
     *@Param [account, currentPage, pageSize]
     *@return ProjectInfo **/
    public IPage<ProjectInfo> getProjectListCheck(String adminName, int currentPage, int pageSize) {
        return this.getProjectListByState("unit_administrator",adminName,2,currentPage,pageSize);
    }
    public IPage<ProjectInfo> getProjectListConclusion(String promoterName,int currentPage,int pageSize){
        return this.getProjectListByState("project_promoter",promoterName,3,currentPage,pageSize);
    }
    public IPage<ProjectInfo> getProjectListRecheck(String adminName,int currentPage,int pageSize){
        return this.getProjectListByState("unit_administrator",adminName,4,currentPage,pageSize);
    }
    public IPage<ProjectInfo> getProjectLisFinalCheck(String auditorName,int currentPage,int pageSize){
        return this.getProjectListByState("unit_auditor",auditorName,5,currentPage,pageSize);
    }
    private IPage<ProjectInfo> getProjectListByState(String equalColumn,String columnValue,int currentStateFlag,int currentPage,int pageSize){
        Page<ProjectInfo> page=new Page<>(currentPage,pageSize);
        QueryWrapper<ProjectInfo> wrapper=new QueryWrapper<>();
        wrapper.eq(equalColumn,columnValue);
        wrapper.eq("current_state_flag",currentStateFlag);
        return this.page(page,wrapper);
    }
    /**
     *@Author xyj
     *@Description 管理员第一次审核通过，通知推进人结题
     *@Date 9:45 2021/7/16
     *@Param [projectInfo, comment, account, url]
     *@return boolean **/
    public boolean adoptProject(ProjectInfo projectInfo,String comment,String account,String url){
        int currentStateFlag=projectInfo.getCurrentStateFlag()+1;
        projectInfo.setCurrentState("待推进人结题");
        boolean update = this.updateProject(projectInfo.getProjectNumber(), currentStateFlag, "管理员审核通过");
        if (update){
//插入节点操作
            boolean insertNode = nodeService.insertNode(projectInfo.getProjectNumber(), currentStateFlag, "管理员审核通过", account, comment);
//触发邮件通知推进人结题
            String content = emailUtils.drawTable(projectInfo.getProjectPromoter(), projectInfo, url,comment);
            boolean send = emailUtils.sendEmail(projectInfo.getPromoterMail(), "推进人结题", content);
        }

        return update;
    }
    /***
     *@Author xyj
     *@Description 管理员驳回项目，通知提出人进行修改
     *@Date 9:46 2021/7/16
     *@Param [projectInfo, comment, account, url]
     *@return boolean **/
    public boolean rejectProject(ProjectInfo projectInfo,String comment,String account,String url){
        int currentStateFlag=projectInfo.getCurrentStateFlag()-1;
        projectInfo.setCurrentState("管理员驳回");
        boolean update = this.updateProject(projectInfo.getProjectNumber(), currentStateFlag, "管理员驳回");
        if (update){
//插入节点操作
            boolean insertNode = nodeService.insertNode(projectInfo.getProjectNumber(), currentStateFlag, "管理员驳回", account, comment);
//触发邮件通知提出人结题
            String content = emailUtils.drawTable(projectInfo.getProjectSponsor(), projectInfo, url,comment);
            boolean send = emailUtils.sendEmail(projectInfo.getSponsorMail(), "管理员驳回", content);
        }

        return update;
    }
    private boolean updateProject(String projectNumber,int currentStateFlag,String currentState){
        UpdateWrapper<ProjectInfo> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("project_number",projectNumber);
        updateWrapper.set("current_state_flag",currentStateFlag);
        updateWrapper.set("current_state",currentState);
        return this.update(updateWrapper);
    }
    /**
     *@Author xyj
     *@Description 推进人结题
     *@Date 9:49 2021/7/16
     *@Param [projectInfo, comment, account, url]
     *@return boolean **/
    public boolean promoterAdopt(ProjectInfo projectInfo,String comment,String account,String url){
        int currentStateFlag=projectInfo.getCurrentStateFlag()+1;
        projectInfo.setCurrentState("待管理员复核");
        UpdateWrapper<ProjectInfo> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("project_number",projectInfo.getProjectNumber());
        updateWrapper.set("current_state_flag",currentStateFlag);
        updateWrapper.set("project_result",projectInfo.getProjectResult());
        updateWrapper.set("current_state","推进人结题");
        boolean update = this.update(updateWrapper);
        if (update){
//插入节点操作
            boolean insertNode = nodeService.insertNode(projectInfo.getProjectNumber(), currentStateFlag, "推进人结题", account, comment);
//触发邮件通知管理员复核
            String content = emailUtils.drawTable(projectInfo.getUnitAdministrator(), projectInfo, url,comment);

            boolean send = emailUtils.sendEmail(projectInfo.getAdministratorMail(), "管理员复核", content);
        }
        return update;
    }
    /**
     *@Author xyj
     *@Description 管理员管理员复核通过，通知审核员审核
     *@Date 9:45 2021/7/16
     *@Param [projectInfo, comment, account, url]
     *@return boolean **/
    public boolean ReadoptProject(ProjectInfo projectInfo,String comment,String account,String url){
        int currentStateFlag=projectInfo.getCurrentStateFlag()+1;
        projectInfo.setCurrentState("待审核员审核");
        boolean update = this.updateProject(projectInfo.getProjectNumber(), currentStateFlag, "管理员复核通过");
        if (update){
//插入节点操作
            boolean insertNode = nodeService.insertNode(projectInfo.getProjectNumber(), currentStateFlag, "管理员复核通过", account, comment);
//触发邮件通知审核员审核
            String content = emailUtils.drawTable(projectInfo.getUnitAuditor(), projectInfo, url,comment);
            boolean send = emailUtils.sendEmail(projectInfo.getAuditorMail(), "审核员审核", content);
        }

        return update;
    }
    /***
     *@Author xyj
     *@Description 管理员驳回项目，通知推进人进行修改
     *@Date 9:46 2021/7/16
     *@Param [projectInfo, comment, account, url]
     *@return boolean **/
    public boolean rejectProjectAgain(ProjectInfo projectInfo,String comment,String account,String url){
        int currentStateFlag=projectInfo.getCurrentStateFlag()-1;
        projectInfo.setCurrentState("管理员驳回");
        boolean update = this.updateProject(projectInfo.getProjectNumber(), currentStateFlag, "管理员驳回");
        if (update){
//插入节点操作
            boolean insertNode = nodeService.insertNode(projectInfo.getProjectNumber(), currentStateFlag, "管理员驳回", account, comment);
//触发邮件通知推进人结题
            String content = emailUtils.drawTable(projectInfo.getProjectPromoter(), projectInfo, url,comment);
            boolean send = emailUtils.sendEmail(projectInfo.getPromoterMail(), "管理员驳回", content);
        }
        return update;
    }
    /**
     *@Author xyj
     *@Description 审核员审核通过，项目完结
     *@Date 17:50 2021/7/16
     *@Param [projectInfo, comment, account, url]
     *@return boolean **/
    public boolean auditorAdopt(ProjectInfo projectInfo,String comment,String account,String url){
        int currentStateFlag=projectInfo.getCurrentStateFlag()+1;
        projectInfo.setCurrentState("审核员通过");
        boolean update = this.updateProject(projectInfo.getProjectNumber(), currentStateFlag, "审核员通过");
        if (update){
//插入节点操作
            boolean insertNode = nodeService.insertNode(projectInfo.getProjectNumber(), currentStateFlag, "审核员通过", account, comment);
//触发邮件通知提出人项目已完结
            String content = emailUtils.drawTable(projectInfo.getProjectSponsor(), projectInfo, url,comment);
            boolean send = emailUtils.sendEmail(projectInfo.getSponsorMail(), "项目已完结", content);
        }
        return update;
    }
    /**
     *@Author xyj
     *@Description 审核员驳回到推进人结题
     *@Date 17:52 2021/7/16
     *@Param [projectInfo, comment, account, url]
     *@return boolean **/
    public boolean auditorRejectToPromoter(ProjectInfo projectInfo,String comment,String account,String url){
        int currentStateFlag=projectInfo.getCurrentStateFlag()-2;
        projectInfo.setCurrentState("审核员驳回至推进人");
        boolean update = this.updateProject(projectInfo.getProjectNumber(), currentStateFlag, "审核员驳回至推进人");
        if (update){
//插入节点操作
            boolean insertNode = nodeService.insertNode(projectInfo.getProjectNumber(), currentStateFlag, "驳回至推进人", account, comment);
//触发邮件通知推进人结题
            String content = emailUtils.drawTable(projectInfo.getProjectPromoter(), projectInfo, url,comment);
            boolean send = emailUtils.sendEmail(projectInfo.getPromoterMail(), "审核员驳回至推进人", content);
        }
        return update;
    }
    /**
     *@Author xyj
     *@Description 审核员驳回到审核员
     *@Date 17:52 2021/7/16
     *@Param [projectInfo, comment, account, url]
     *@return boolean **/
    public boolean auditorRejectToAdmin(ProjectInfo projectInfo,String comment,String account,String url){
        int currentStateFlag=projectInfo.getCurrentStateFlag()-1;
        projectInfo.setCurrentState("审核员驳回至管理员");
        boolean update = this.updateProject(projectInfo.getProjectNumber(), currentStateFlag, "审核员驳回至管理员");
        if (update){
//插入节点操作
            boolean insertNode = nodeService.insertNode(projectInfo.getProjectNumber(), currentStateFlag, "驳回至管理员", account, comment);
//触发邮件通知推进人结题
            String content = emailUtils.drawTable(projectInfo.getUnitAdministrator(), projectInfo, url,comment);
            boolean send = emailUtils.sendEmail(projectInfo.getAdministratorMail(), "审核员驳回至管理员", content);
        }
        return update;
    }
}


