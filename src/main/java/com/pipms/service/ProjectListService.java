package com.pipms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pipms.annotation.DataScope;
import com.pipms.entity.UserRole;
import com.pipms.entity.view.ProjectList;
import com.pipms.enums.DataScopeEnum;
import com.pipms.mapper.ProjectListMapper;
import org.springframework.stereotype.Service;


/**
 * @ClassName ProjectListService
 * @Description TODO
 * @Author 661595
 * @Date 2021/6/2516:30
 * @Version 1.0
 **/
@Service
public class ProjectListService extends ServiceImpl<ProjectListMapper, ProjectList> {
    @DataScope
    public IPage<ProjectList> listByAccount(UserRole user, int currentPage, int pageSize){
        IPage<ProjectList> page=new Page<>(currentPage,pageSize);
        QueryWrapper<ProjectList> wrapper=new QueryWrapper<>();
        String dataScope = user.getParams().get("dataScope");
        if (DataScopeEnum.DATA_SCOPE_UNIT.getDataScope().equals(dataScope)){
            wrapper.eq("unit",user.getUnit());
        }else if (DataScopeEnum.DATA_SCOPE_ACCOUNT.getDataScope().equals(dataScope)){
            wrapper.eq("create_account",user.getUserAccount());
        }
        return this.page(page,wrapper);
    }
}
