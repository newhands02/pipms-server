package com.pipms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pipms.entity.RolePermission;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRolePermissionService extends IService<RolePermission> {
    Set<String> getPermissionByRole(String roleName);
    Set<String> getRolesByAccount(String account);
    int[] getMenuList();
    List<Map<String, Object>> getRoleMap();
}
