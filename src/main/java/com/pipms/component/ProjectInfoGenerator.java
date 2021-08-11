package com.pipms.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pipms.entity.ProjectInfo;
import com.pipms.entity.UnitInfo;
import com.pipms.service.impl.ProjectInfoServiceImpl;
import com.pipms.service.impl.UnitInfoServiceImpl;
import com.pipms.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ProjectInfoGenerator {
    @Autowired
    ProjectInfoServiceImpl projectInfoService;
    @Autowired
    UnitInfoServiceImpl unitInfoService;
    /**
     *@Author xyj
     *@Description 获取项目编号
     *@Date 9:49 2021/6/25
     *@Param [unitName]
     *@return java.lang.String **/
    public String getFlowCode(String unitName){
        String prefix=getUniCodeAndDateCode(unitName);
        String suffix=getSuffixNum();
        String code=generateCode(prefix,suffix);
        while (hasCode(code)){
            suffix=getSuffixNum();
            code=generateCode(prefix,suffix);
        }
        return code;
    }
    //判断生成的编号是否已经存在
    private boolean hasCode(String code){
        QueryWrapper<ProjectInfo> projectWrapper=new QueryWrapper<>();
        projectWrapper.eq("project_number",code);
        ProjectInfo project = projectInfoService.getOne(projectWrapper, false);
        return project==null?false:true;
    }
    //生成单位代码+年月+4位随机数
    private String generateCode(String prefix,String suffix){
        return prefix+suffix;
    }
    //单位代码+年月
    private String getUniCodeAndDateCode(String unitName){
        QueryWrapper<UnitInfo> wrapper=new QueryWrapper();
        wrapper.eq("unit_sys_name",unitName);
        UnitInfo one = unitInfoService.getOne(wrapper, false);
        String unitCode = one.getUnitCode();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
        String format = sdf.format(new Date());
        return unitCode+format;
    }
    //生成4位随机数
    private String getSuffixNum(){
        return TokenUtils.generateToken(4,"0123456789");
    }
    //根据单位名获取版块名称，如果为空则说明不存在
    public UnitInfo getUnitInfo(String unitName){
        QueryWrapper<UnitInfo> wrapper=new QueryWrapper<>();
        wrapper.eq("unit_sys_name",unitName);
        return unitInfoService.getOne(wrapper, false);

    }
}
