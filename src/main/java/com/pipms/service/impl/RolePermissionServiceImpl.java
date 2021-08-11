package com.pipms.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pipms.entity.RolePermission;
import com.pipms.entity.UserRole;
import com.pipms.enums.RoleNameEnum;
import com.pipms.mapper.RolePermissionMapper;
import com.pipms.service.IRolePermissionService;
import com.pipms.shiro.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName RolePermissionServiceImpl
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2514:52
 * @Version 1.0
 **/
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
        implements IRolePermissionService {
    @Autowired
    UserRoleServiceImpl userRoleService;
    @Override
    public Set<String> getPermissionByRole(String roleName) {
        String[] names = roleName.split(",");
        QueryWrapper permWrapper=new QueryWrapper<RolePermission>();
        permWrapper.in("role_name",names);
        List<RolePermission> list = this.list(permWrapper);
        Set<String> permissionSet = list.stream().map(RolePermission::getPermission).collect(Collectors.toSet());
        return permissionSet;
    }

    @Override
    public Set<String> getRolesByAccount(String account) {
        QueryWrapper userWrapper=new QueryWrapper<UserRole>();
        userWrapper.eq("user_account",account);
        UserRole one = userRoleService.getOne(userWrapper, false);
        if (one==null){
            return new HashSet<>();
        }
        String role=one.getRoleNames();
        String[] roles = role.split(",");
        Set<String> roleSet = Arrays.asList(roles).stream().collect(Collectors.toSet());
        return roleSet;
    }
    @Override
    public int[] getMenuList() {
        UserRole sysUser = ShiroUtils.getSysUser();
        String roleTypes = sysUser.getRoleTypes();
        String[] roles = roleTypes.split(",");
        QueryWrapper<RolePermission> wrapper=new QueryWrapper<>();
        int size = this.count(wrapper.select("DISTINCT(role_type)"));
        int[] menus=new int[size];
        for (String role : roles) {
            int index=Integer.valueOf(role);
            if (index>0&&index<=size){
                menus[index-1]=1;
            }
        }
        return menus;
    }

    @Override
    public List<Map<String, Object>> getRoleMap() {
        QueryWrapper<RolePermission> wrapper=new QueryWrapper<>();
        wrapper.select("DISTINCT(role_type)");
        List<RolePermission> list = this.list(wrapper);
        String roleTypes = ShiroUtils.getSysUser().getRoleTypes();
        List<Integer> roleTypeList = list.stream().map(RolePermission::getRoleType).collect(Collectors.toList());
        List<Map<String,Object>> roleMapList=new LinkedList<>();
        roleTypeList.forEach(roleType->{
            RoleNameEnum roleNameEnum = RoleNameEnum.matchType(roleType);
            Map<String,Object> item=new HashMap<>();
            item.put("label",roleNameEnum.getRoleName());
            item.put("value",roleNameEnum.getRoleType());
            roleMapList.add(item);
        });
        if (!roleTypes.contains("1")){
            return roleMapList.stream().filter(item-> (int)item.get("value")!=1).collect(Collectors.toList());
        }
        return roleMapList;
    }
}
