package com.pipms.controller;

import com.alibaba.fastjson.JSONObject;
import com.pipms.entity.view.UserManageView;
import com.pipms.mapper.UserManageMapper;
import com.pipms.service.IUserManageService;
import com.pipms.service.IUserRoleService;
import com.pipms.service.impl.SysUserOnlineServiceImpl;
import com.pipms.shiro.utils.StringUtils;
import com.pipms.utils.LdapUtils;
import com.pipms.utils.R;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserManagerController
 * @Description 用户管理控制层
 * @Author 661595
 * @Date 2021/7/2717:08
 * @Version 1.0
 **/
@RestController
@RequestMapping("userManage")
public class UserManagerController {

    @Autowired
    private IUserManageService userManageService;
    @Autowired
    private SysUserOnlineServiceImpl sysUserOnlineService;

    @RequiresRoles(value = {"super_admin","sys_admin"},logical = Logical.OR)
    @RequiresPermissions("user:find")
    @PostMapping("getViewList")
    public R getViewList(@RequestBody Map<String,String> queryArgs,int currentPage,int pageSize){
        List<UserManageView> userView = userManageService.getViewList(queryArgs,currentPage,pageSize);
        return R.ok().put("list",userView).put("total",userView.size());
    }
    @GetMapping("getUserInfo")
    public Map<String, String> getUnitNames(String account){
        return LdapUtils.getUserInfo(account);
    }
    @RequiresRoles(value = {"super_admin","sys_admin"},logical = Logical.OR)
    @RequiresPermissions("user:insert")
    @PostMapping("addUser")
    public R addUser(@RequestBody Map userInfo){
        boolean b = userManageService.addUser(userInfo);
        return b?R.ok("添加成功"):R.error("添加失败");
    }
    @RequiresRoles(value = {"super_admin","sys_admin"},logical = Logical.OR)
    @RequiresPermissions("user:update")
    @GetMapping("forceLogout")
    public R forceLogout(String userAccount){
        boolean b = sysUserOnlineService.forceLogout(userAccount);
        return b?R.ok("用户"+userAccount+"下线成功"):R.error("强制下线失败");
    }

    @RequiresRoles(value = {"super_admin","sys_admin"},logical = Logical.OR)
    @RequiresPermissions("user:update")
    @GetMapping("deactiveAccount")
    public R deactiveAccount(String userAccount){
        boolean b = userManageService.stopAccount(userAccount);
        return b?R.ok("停用"+userAccount+"成功"):R.error("停用失败");
    }

    @RequiresRoles(value = {"super_admin","sys_admin"},logical = Logical.OR)
    @RequiresPermissions("user:update")
    @PostMapping("allocateRole")
    public R allocateRole(@RequestBody JSONObject params){
        String userAccount = params.getString("userAccount");
        List<Integer> typeList =(List<Integer>) params.get("typeList");
        boolean b = userManageService.allocateRoles(userAccount, typeList);
        return b?R.ok("角色分配成功"):R.error("角色分配失败");
    }
}
