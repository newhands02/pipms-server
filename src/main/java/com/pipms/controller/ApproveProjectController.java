package com.pipms.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pipms.entity.ProjectInfo;
import com.pipms.service.impl.ApproveProjectServiceImpl;
import com.pipms.shiro.utils.ShiroUtils;
import com.pipms.utils.EmailUtils;
import com.pipms.utils.R;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ApproveProjectController
 * @Description 接受签审操作请求的控制层
 * @Author 661595
 * @Date 2021/7/1410:18
 * @Version 1.0
 **/
@RequestMapping("approve")
@RestController
public class ApproveProjectController {
    @Autowired
    private ApproveProjectServiceImpl approveProjectService;

    /**
     *@Description 获取单位管理员首次签审列表
     * **/
    @RequiresRoles(value = {"super_admin","unit_admin"},logical = Logical.OR)
    @RequiresPermissions("project:find")
    @GetMapping("getAdminCheckList")
    public R getApproveList(int currentPage, int pageSize){
        String loginName = ShiroUtils.getLoginName();
        IPage<ProjectInfo> pages = approveProjectService.getProjectListCheck(loginName, currentPage, pageSize);
        return R.ok().put("list",pages.getRecords()).put("total",pages.getTotal());
    }
    /**
     *@Description 单位管理员首次签审通过
     * **/
    @RequiresRoles(value = {"super_admin","unit_admin"},logical = Logical.OR)
    @RequiresPermissions("project:approve")
    @PostMapping("adminAdopted")
    public R adminAdopted(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        ProjectInfo projectInfo=jsonObject.getObject("projectInfo",ProjectInfo.class);
        String comment=(String) jsonObject.get("comment");
        String account=ShiroUtils.getLoginAccount();
        String url = EmailUtils.getApprovePageUrl(request,"/conclusionPage");
        boolean b = approveProjectService.adoptProject(projectInfo, comment, account, url);
        return b?R.ok():R.error();
    }
    /**
     *@Description 单位管理员首次签审驳回
     * **/
    @RequiresRoles(value = {"super_admin","unit_admin"},logical = Logical.OR)
    @RequiresPermissions("project:approve")
    @PostMapping("adminReject")
    public R adminReject(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        ProjectInfo projectInfo=jsonObject.getObject("projectInfo",ProjectInfo.class);
        String comment=(String) jsonObject.get("comment");
        String account=ShiroUtils.getLoginAccount();
        String url = EmailUtils.getApprovePageUrl(request,"/createProject");
        boolean b = approveProjectService.rejectProject(projectInfo, comment, account, url);
        return b?R.ok():R.error();
    }

    /**
     *@Description 项目推进人获取数据列表
     * **/
    @RequiresRoles(value = {"super_admin","promoter"},logical = Logical.OR)
    @RequiresPermissions("project:find")
    @GetMapping("getPrompterCheckList")
    public R getPrompterList(int currentPage, int pageSize){
        String loginName = ShiroUtils.getLoginName();
        IPage<ProjectInfo> pages = approveProjectService.getProjectListConclusion(loginName, currentPage, pageSize);
        return R.ok().put("list",pages.getRecords()).put("total",pages.getTotal());
    }
    /**
     *@Description 项目推进人结题
     * **/
    @RequiresRoles(value = {"super_admin","promoter"},logical = Logical.OR)
    @RequiresPermissions("project:conclusion")
    @PostMapping("promoterConclusion")
    public R promoterConclusion(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        ProjectInfo projectInfo=jsonObject.getObject("projectInfo",ProjectInfo.class);
        String comment=(String) jsonObject.get("comment");
        String account=ShiroUtils.getLoginAccount();
        String url = EmailUtils.getApprovePageUrl(request,"/recheckPage");
        boolean b = approveProjectService.promoterAdopt(projectInfo, comment, account, url);
        return b?R.ok():R.error();
    }
    /**
     *@Description 获取单位管理员复核列表
     * **/
    @RequiresRoles(value = {"super_admin","unit_admin"},logical = Logical.OR)
    @RequiresPermissions("project:find")
    @GetMapping("getAdminRecheckList")
    public R getAdminRecheckList(String adminName, int currentPage, int pageSize){
        String loginName = ShiroUtils.getLoginName();
        IPage<ProjectInfo> pages = approveProjectService.getProjectListRecheck(loginName, currentPage, pageSize);
        return R.ok().put("list",pages.getRecords()).put("total",pages.getTotal());
    }
    /**
     *@Description 单位管理员复核通过
     * **/
    @RequiresRoles(value = {"super_admin","unit_admin"},logical = Logical.OR)
    @RequiresPermissions("project:approve")
    @PostMapping("adminRecheckAdopt")
    public R adminRecheckAdopt(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        ProjectInfo projectInfo=jsonObject.getObject("projectInfo",ProjectInfo.class);
        String comment=(String) jsonObject.get("comment");
        String account=ShiroUtils.getLoginAccount();
        String url = EmailUtils.getApprovePageUrl(request,"/auditorCheckPage");
        boolean b = approveProjectService.ReadoptProject(projectInfo, comment, account, url);
        return b?R.ok():R.error();
    }
    /**
     *@Description 单位管理员复核驳回
     * **/
    @RequiresRoles(value = {"super_admin","unit_admin"},logical = Logical.OR)
    @RequiresPermissions("project:approve")
    @PostMapping("adminRecheckReject")
    public R adminRecheckReject(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        ProjectInfo projectInfo=jsonObject.getObject("projectInfo",ProjectInfo.class);
        String comment=(String) jsonObject.get("comment");
        String account=ShiroUtils.getLoginAccount();
        String url = EmailUtils.getApprovePageUrl(request,"/conclusionPage");
        boolean b = approveProjectService.rejectProjectAgain(projectInfo, comment, account, url);
        return b?R.ok():R.error();
    }

    /**
     *@Description 单位审核员数据列表
     * **/
    @RequiresRoles(value = {"super_admin","unit_auditor"},logical = Logical.OR)
    @RequiresPermissions("project:find")
    @GetMapping("getAuditorCheckList")
    public R getAuditorCheckList(String adminName, int currentPage, int pageSize){
        String loginName = ShiroUtils.getLoginName();
        IPage<ProjectInfo> pages = approveProjectService.getProjectLisFinalCheck(loginName, currentPage, pageSize);
        return R.ok().put("list",pages.getRecords()).put("total",pages.getTotal());
    }

    /**
     *@Description 单位审核员复核通过
     * **/
    @RequiresRoles(value = {"super_admin","unit_auditor"},logical = Logical.OR)
    @RequiresPermissions("project:approve")
    @PostMapping("auditorCheckAdopt")
    public R auditorCheckAdopt(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        ProjectInfo projectInfo=jsonObject.getObject("projectInfo",ProjectInfo.class);
        String comment=(String) jsonObject.get("comment");
        String account=ShiroUtils.getLoginAccount();
        String url = EmailUtils.getApprovePageUrl(request,"/");
        boolean b = approveProjectService.auditorAdopt(projectInfo, comment, account, url);
        return b?R.ok():R.error();
    }
    /**
     *@Description 单位审核员复核驳回至推进人
     * **/
    @RequiresRoles(value = {"super_admin","unit_auditor"},logical = Logical.OR)
    @RequiresPermissions("project:approve")
    @PostMapping("auditorRejectToPromoter")
    public R auditorRejectToPromoter(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        ProjectInfo projectInfo=jsonObject.getObject("projectInfo",ProjectInfo.class);
        String comment=(String) jsonObject.get("comment");
        String account=ShiroUtils.getLoginAccount();
        String url = EmailUtils.getApprovePageUrl(request,"/conclusionPage");
        boolean b = approveProjectService.auditorRejectToPromoter(projectInfo, comment, account, url);
        return b?R.ok():R.error();
    }

    /**
     *@Description 单位审核员驳回至管理员
     * **/
    @RequiresRoles(value = {"super_admin","unit_auditor"},logical = Logical.OR)
    @RequiresPermissions("project:approve")
    @PostMapping("auditorRejectToAdmin")
    public R auditorRejectToAdmin(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        ProjectInfo projectInfo=jsonObject.getObject("projectInfo",ProjectInfo.class);
        String comment=(String) jsonObject.get("comment");
        String account=ShiroUtils.getLoginAccount();
        String url = EmailUtils.getApprovePageUrl(request,"/recheckPage");
        boolean b = approveProjectService.auditorRejectToAdmin(projectInfo, comment, account, url);
        return b?R.ok():R.error();
    }
}


