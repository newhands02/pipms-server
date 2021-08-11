package com.pipms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pipms.entity.ProjectInfo;
import com.pipms.entity.UserRole;
import com.pipms.entity.view.ProjectList;
import com.pipms.entity.view.ProjectResultView;
import com.pipms.service.ProjectListService;
import com.pipms.service.impl.ProjectInfoServiceImpl;
import com.pipms.shiro.utils.ShiroUtils;
import com.pipms.utils.EmailUtils;
import com.pipms.utils.R;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ClassName projectController
 * @Description 项目信息控制层
 * @Author 661595
 * @Date 2021/6/2116:20
 * @Version 1.0
 **/
@RestController
@RequestMapping("project")
public class ProjectController {
    @Autowired
    private ProjectInfoServiceImpl projectInfoService;
    @Autowired
    private ProjectListService projectListService;


    /**
     *@Description 获取项目初始化信息
     * **/
    @RequiresRoles(value = {"super_admin","sponsor"},logical = Logical.OR)
    @RequiresPermissions("project:create")
    @GetMapping("getInit")
    public R getInitProject(){
        UserRole sysUser = ShiroUtils.getSysUser();
        String userName = sysUser.getUserName();
        String unit = sysUser.getUnit();
        ProjectInfo projectInfo = projectInfoService.initProject(userName, unit);
        return projectInfo==null?
                R.error("获取初始项目信息失败"):R.ok().put("project",projectInfo);
    }

    /**
     *@Description 保存项目
     * **/
    @RequiresRoles(value = {"sponsor","super_admin"},logical = Logical.OR)
    @RequiresPermissions("project:create")
    @PostMapping("save")
    public R saveProject(@RequestBody ProjectInfo projectInfo){
        return projectInfoService.saveProject(projectInfo)?R.ok("保存成功"):R.error("保存失败 ");
    }

    /**
     *@Description 根据账号返回对应范围数据
     * **/
    @RequiresPermissions("project:find")
    @GetMapping("getProjectByAccount")
    public R getProjectByAccount(int currentPage,int pageSize){
        UserRole sysUser = ShiroUtils.getSysUser();
        IPage<ProjectInfo> listPage = projectInfoService.getProjectListByAccount(sysUser, currentPage, pageSize);
        return R.ok().put("list",listPage.getRecords()).put("total",listPage.getTotal());
    }
    @RequiresPermissions("project:find")
    @GetMapping("getProjectListByAccount")
    public R getProjectListByAccount(int currentPage,int pageSize){
        UserRole sysUser = ShiroUtils.getSysUser();
        IPage<ProjectList> listPage = projectListService.listByAccount(sysUser, currentPage, pageSize);
        return R.ok().put("list",listPage.getRecords()).put("total",listPage.getTotal());
    }


    /**
     *@Description 根据项目编号返回项目信息
     * **/
    @RequiresPermissions("project:find")
    @GetMapping("getProjectByNumber")
    public R getProjectByNumber(String projectNumber){
        ProjectInfo project = projectInfoService.getProjectByNumber(projectNumber);
        List<Map> beforeVideoList = projectInfoService.getVideoList(projectNumber, project.getImprovementBeforeVideo());
        List<Map> afterVideoList = projectInfoService.getVideoList(projectNumber, project.getImprovementAfterVideo());
        List<Map> relateFileList = projectInfoService.getRelateFileList(projectNumber, project.getFileRelateOne(), project.getFileRelateTwo());
        return R.ok().put("project",project).put("beforeVideoList",beforeVideoList)
                .put("afterVideoList",afterVideoList).put("relateFileList",relateFileList);
    }

    /**
     *@Description 提交项目
     * **/
    @RequiresRoles(value = {"sponsor","super_admin"},logical = Logical.OR)
    @RequiresPermissions("project:create")
    @PostMapping("submitProject")
    public R submitProject(@RequestBody ProjectInfo projectInfo, HttpServletRequest request){
        boolean b = projectInfoService.submitProject(projectInfo, EmailUtils.getApprovePageUrl(request,"/approvePage"));
        return b?R.ok():R.error();
    }
    @PostMapping("getList")
    public R getListProject(@RequestBody Map<String,String> object,int currentPage,int pageSize){
        UserRole sysUser = ShiroUtils.getSysUser();
        IPage<ProjectInfo> projectByQuery = projectInfoService.getProjectByQuery(sysUser,object, currentPage, pageSize);
        return R.ok().put("data",projectByQuery.getRecords()).put("total",projectByQuery.getTotal());
    }

    @PostMapping("getSummaryResults")
    public R getSummaryResults(@RequestBody Map<String,String> queryMap){
        UserRole sysUser = ShiroUtils.getSysUser();
        List<ProjectResultView> resultSummary = projectInfoService.getResultSummary(sysUser, queryMap, 0, 0);
        return R.ok().put("list",resultSummary);
    }
}
