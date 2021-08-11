package com.pipms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pipms.entity.UserRole;
import com.pipms.entity.view.UserManageView;
import com.pipms.enums.RoleNameEnum;
import com.pipms.mapper.UserManageMapper;
import com.pipms.service.IUserManageService;
import com.pipms.service.IUserRoleService;
import com.pipms.shiro.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @ClassName UserManageServiceImpl
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2913:41
 * @Version 1.0
 **/
@Service
public class UserManageServiceImpl implements IUserManageService {
    @Autowired
    private UserManageMapper userManageMapper;
    @Autowired
    private IUserRoleService userRoleService;
    @Override
    public List<UserManageView> getViewList(Map<String, String> queryArgs, int currentPage, int pageSize) {
        String unit = queryArgs.get("unit");
        String userName = queryArgs.get("userName");
        String account = queryArgs.get("userAccount");
        int start=(currentPage-1)*pageSize;
        int end=currentPage*pageSize;
        return userManageMapper.getUserView(unit,userName,account,start,end);
    }
    /**
     *@Author xyj
     *@Description 添加用户
     *@Param [userInfo]
     *@return boolean **/
    @Override
    public boolean addUser(Map userInfo) {
        String account=(String) userInfo.get("account");
        if (userAlreadyExits(account)){
            return false;
        }
        String userName=(String) userInfo.get("name");
        String unit=(String) userInfo.get("unit");
        List<Integer> roleList=(List<Integer>) userInfo.get("role");
        StringBuilder roleTypes=new StringBuilder();
        StringBuilder roleName=new StringBuilder();
        if (roleList.isEmpty()){
            roleTypes.append("8");
            roleName.append("sponsor");
        }else if (roleList.size()==1){
            Integer type = roleList.get(0);
            roleTypes.append(type.intValue());
            roleName.append(RoleNameEnum.matchType(type).getRoleNameEN());
        }else {
            roleList.forEach(roleType->{
                roleTypes.append(roleType+",");
                roleName.append(RoleNameEnum.matchType(roleType).getRoleNameEN())
                        .append(",");
            });
            int index = roleTypes.lastIndexOf(",");
            int lastSpiltOfRoleName = roleName.lastIndexOf(",");
            roleTypes.replace(index,index+1,"");
            roleName.replace(lastSpiltOfRoleName,lastSpiltOfRoleName+1,"");
        }
        UserRole user=new UserRole();
        user.setUserName(userName);
        user.setUserAccount(account);
        user.setUnit(unit);
        user.setRoleTypes(roleTypes.toString());
        user.setRoleNames(roleName.toString());
        user.setUpdateBy(ShiroUtils.getLoginAccount());
        user.setUpdateTime(LocalDateTime.now());
        return userRoleService.save(user);
    }
    /**
     *@Author xyj
     *@Description 检查该用户是否已经存在于数据库当中
     *@return boolean**/
    private boolean userAlreadyExits(String account) {
        QueryWrapper<UserRole> wrapper=new QueryWrapper<>();
        wrapper.eq("user_account",account);
        UserRole one = userRoleService.getOne(wrapper,false);
        if (one==null){
            return false;
        }else {
            return true;
        }
    }
    /**
     *@Author xyj
     *@Description 将账号状态设为停用
     *@Param [account]
     *@return boolean **/
    @Override
    public boolean stopAccount(String account) {
        UpdateWrapper<UserRole> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_account",account).set("status",'1');
        return userRoleService.update(updateWrapper);
    }

    /**
     *@Author xyj
     *@Description 为账号分配权限
     *@Param [roles]
     *@return boolean **/
    @Override
    public boolean allocateRoles(String userAccount,List<Integer> typeList) {
        StringBuffer roleType=new StringBuffer(),roleName=new StringBuffer();
        typeList.forEach(type->{
            roleType.append(type).append(",");
            roleName.append(RoleNameEnum.matchType(type).getRoleNameEN()).append(",");
        });
        int index = roleType.lastIndexOf(",");
        int lastSpiltOfRoleName = roleName.lastIndexOf(",");
        roleType.replace(index,index+1,"");
        roleName.replace(lastSpiltOfRoleName,lastSpiltOfRoleName+1,"");
        UpdateWrapper<UserRole> wrapper=new UpdateWrapper<>();
        wrapper.eq("user_account",userAccount)
                .set("role_types",roleType.toString())
                .set("role_names",roleName.toString());
        return userRoleService.update(wrapper);
    }
}
