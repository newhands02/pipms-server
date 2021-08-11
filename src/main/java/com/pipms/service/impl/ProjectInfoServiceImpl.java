package com.pipms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pipms.annotation.DataScope;
import com.pipms.component.ProjectInfoGenerator;
import com.pipms.entity.ProjectInfo;
import com.pipms.entity.UnitInfo;
import com.pipms.entity.UserRole;
import com.pipms.entity.view.ProjectResultView;
import com.pipms.enums.DataScopeEnum;
import com.pipms.mapper.ProjectInfoMapper;
import com.pipms.service.IProjectInfoService;
import com.pipms.shiro.utils.ShiroUtils;
import com.pipms.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目信息表 服务实现类
 * </p>
 *
 * @author xyj
 * @since 2021-06-21
 */
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements IProjectInfoService {
    @Autowired
    private ProjectInfoGenerator pGenerator;
    @Autowired
    private NodeServiceImpl nodeService;
    @Autowired
    private FileInfoServiceImpl fileInfoService;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    private static byte SET_TRUE=1;
    private static byte SET_FALSE=0;
    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";
    //初始化项目
    public ProjectInfo initProject(String userName,String unitName){
        if (userName==null || unitName==null){
            return null;
        }
        ProjectInfo initProject=new ProjectInfo();
//TODO 这里获取单位的逻辑还要在修改
// String unitSection=pGenerator.getUnitSection(unitName);
        UnitInfo unitInfo = pGenerator.getUnitInfo(unitName);
        if (unitInfo==null){
            return null;
        }
        String loginAccount = ShiroUtils.getLoginAccount();
        initProject.setSection(unitInfo.getUnitSection());
        initProject.setProjectNumber(pGenerator.getFlowCode(unitName));
        initProject.setUnit(unitName);
        initProject.setProjectSponsor(userName);
        initProject.setSponsorMail(loginAccount+"@gree.com.cn");
        initProject.setUnitAdministrator(unitInfo.getUnitAdmin());
        initProject.setAdministratorMail(unitInfo.getAdminMail());
        initProject.setUnitAuditor(unitInfo.getUnitAuditor());
        initProject.setAuditorMail(unitInfo.getAuditorMail());
        initProject.setCreateAccount(loginAccount);
        initProject.setCurrentState("项目创建");
        initProject.setUpdateTime(LocalDateTime.now());
        return initProject;
    }
    //保存项目
    @Transactional(rollbackFor = Exception.class)
    public boolean saveProject(ProjectInfo project){
        project.setCurrentState("保存");
        project.setCurrentStateFlag(1);
// project.setSponsorMail(project.getCreateAccount()+"@gree.com.cn");
        project.setUpdateTime(LocalDateTime.now());
// project.setCreateAccount(ShiroUtils.getLoginAccount());
        UpdateWrapper<ProjectInfo> wrapper=new UpdateWrapper<>();
        wrapper.eq("project_number",project.getProjectNumber());
        boolean b = this.saveOrUpdate(project, wrapper);
        if (b){
            nodeService.insertNode(project.getProjectNumber(),1,"项目保存",project.getCreateAccount(),"项目保存");
        }
        return b;
    }
    public List<Map> getVideoList(String projectNumber, String path) {
        return fileInfoService.getVideoList(projectNumber,path);
    }
    public List<Map> getRelateFileList(String projectNumber, String path,String path2) {
        return fileInfoService.getRelateFileList(projectNumber,path,path2);
    }
    //返回查询结果中第一个项目的编号
    @DataScope
    public String getFistProjectNumberByAccount(UserRole user) {
        QueryWrapper<ProjectInfo> wrapper=new QueryWrapper<>();
        String dataScope = user.getParams().get("dataScope");
        if (DataScopeEnum.DATA_SCOPE_UNIT.getDataScope().equals(dataScope)){
            wrapper.eq("unit",user.getUnit());
        }else if (DataScopeEnum.DATA_SCOPE_ACCOUNT.getDataScope().equals(dataScope)){
            wrapper.eq("create_account",user.getUserAccount());
        }
        ProjectInfo one = this.getOne(wrapper, false);
        return one.getProjectNumber();
    }
    public String getVideoUrlByProjectNumber(String projectNumber, String suffix) {
        QueryWrapper<ProjectInfo> wrapper=new QueryWrapper<>();
        wrapper.eq("project_number", projectNumber);
        ProjectInfo project = this.getOne(wrapper, false);
        String url="";
        if (project==null){
            return url;
        }
        if ("before".equals(suffix)){
            url=project.getImprovementBeforeVideo();
        }else if ("after".equals(suffix)){
            url=project.getImprovementAfterVideo();
        }
        return url;
    }
    @DataScope
    public IPage<ProjectInfo> getProjectListByAccount(UserRole user, int currentPage, int pageSize) {
        QueryWrapper<ProjectInfo> wrapper=new QueryWrapper<>();
        IPage<ProjectInfo> page=new Page<>(currentPage,pageSize);
        wrapper.select("section","unit","project_number","project_sponsor","write_time","update_time","create_time","finish_time","current_state");
        String dataScope = user.getParams().get(DATA_SCOPE);
        if (DataScopeEnum.DATA_SCOPE_UNIT.getDataScope().equals(dataScope)){
            wrapper.eq("unit",user.getUnit());
        }else if (DataScopeEnum.DATA_SCOPE_ACCOUNT.getDataScope().equals(dataScope)){
            wrapper.eq("create_account",user.getUserAccount());
        }
        IPage<ProjectInfo> result = this.page(page,wrapper);
        return result;
    }

    public ProjectInfo getProjectByNumber(String projectNumber) {
        QueryWrapper<ProjectInfo> wrapper=new QueryWrapper<>();
        wrapper.eq("project_number",projectNumber);
        return this.getOne(wrapper,false);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean submitProject(ProjectInfo project,String url) {
        project.setCurrentState("待管理员审核");
        project.setCurrentStateFlag(2);
        project.setUpdateTime(LocalDateTime.now());
// project.setSponsorMail(project.getCreateAccount()+"@gree.com.cn");
// project.setCreateAccount(ShiroUtils.getLoginAccount());
        UpdateWrapper<ProjectInfo> wrapper=new UpdateWrapper<>();
        wrapper.eq("project_number",project.getProjectNumber());
        boolean b = this.saveOrUpdate(project, wrapper);
        if (b){
            String s = emailUtils.drawTable(project.getUnitAdministrator(), project, url,"项目提交");
            emailUtils.sendEmail(project.getAdministratorMail(),"管理员审核通知",s);
            nodeService.insertNode(project.getProjectNumber(),2,"项目提交",project.getCreateAccount(),"项目提交");
        }
        return b;
    }
    public String getFileUrlByProjectNumber(String projectNumber,String suffix) {
        QueryWrapper<ProjectInfo> wrapper=new QueryWrapper<>();
        wrapper.eq("project_number", projectNumber);
        ProjectInfo project = this.getOne(wrapper, false);
        String url="";
        if (project!=null){
            if ("one".equals(suffix)){
                url=project.getFileRelateOne();
            }
            if ("two".equals(suffix)){
                url=project.getFileRelateTwo();
            }
        }
        return url;
    }
    //获取项目清单
    @DataScope
    public IPage<ProjectInfo> getProjectByQuery(UserRole user,Map<String,String> queryMap,int currentPage,int pageSize){
        QueryWrapper<ProjectInfo> wrapper=new QueryWrapper<>();
        if (!queryMap.isEmpty()){
            String section = queryMap.get("section");
            if (section!=null && !"".equals(section)){
                wrapper.eq("section",section);
            }
            String resultType = queryMap.get("resultType");
            if (resultType!=null && !"".equals(resultType)){
                wrapper.eq("result_type",resultType);
            }
            String automaticFlag = queryMap.get("automaticFlag");
            if (automaticFlag!=null){
                byte flag=Boolean.valueOf(queryMap.get("automaticFlag")).booleanValue()?SET_TRUE:SET_FALSE;
                wrapper.eq("automatic_flag",flag);
            }
            String createTime = queryMap.get("createTime");
            if (createTime!=null){
                wrapper.le("create_time",createTime);
            }
            String finishTime = queryMap.get("finishTime");
            if (finishTime!=null){
                wrapper.le("finish_time",finishTime);
            }

        }
        String dataScope = user.getParams().get(DATA_SCOPE);
        if (DataScopeEnum.DATA_SCOPE_UNIT.getDataScope().equals(dataScope)){
            wrapper.eq("unit",user.getUnit());
        }else if (DataScopeEnum.DATA_SCOPE_ACCOUNT.getDataScope().equals(dataScope)){
            wrapper.eq("create_account",user.getUserAccount());
            if (!queryMap.isEmpty()){
                wrapper.eq("unit",queryMap.get("unit"));
            }
        }else {
            if (!queryMap.isEmpty()){
                String unit = queryMap.get("unit");
                if (unit!=null){
                    wrapper.eq("unit",unit);
                }
            }
        }
        Page<ProjectInfo> page=new Page<>(currentPage,pageSize);
        return this.page(page, wrapper);
    }
    //获取项目汇总成果
    @DataScope
    public List<ProjectResultView> getResultSummary(UserRole user,Map<String,String> queryMap,int currentPage,int pageSize){
        String unit = queryMap.get("unit");
        String section = queryMap.get("section");
        List<ProjectResultView> summaryList = projectInfoMapper.getSummaryList(unit, section);
        return filterList(user,summaryList);
    }
    @DataScope
    private List<ProjectResultView> filterList(UserRole user,List<ProjectResultView> queryList){
        String dataScope = user.getParams().get(DATA_SCOPE);
        List<ProjectResultView> resultList;
        if (dataScope!=null && !DataScopeEnum.DATA_SCOPE_ALL.getDataScope().equals(dataScope)){
            String unitScope = user.getUnit();
            resultList=queryList.stream().filter(entity->unitScope.equals(entity.getUnit())).collect(Collectors.toList());
        }else {
            resultList=queryList;
        }
        resultList.forEach(entity->{
            BigDecimal auto=entity.getAutoResult()==null?BigDecimal.ZERO:entity.getAutoResult();
            BigDecimal unauto=entity.getNonAutoResult()==null?BigDecimal.ZERO:entity.getNonAutoResult();
            BigDecimal total=auto.add(unauto);
            entity.setTotal(total);
        });
        return resultList;
    }
}
