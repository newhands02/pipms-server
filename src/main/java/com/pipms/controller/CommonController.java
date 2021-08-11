package com.pipms.controller;

import com.pipms.service.IFileExportService;
import com.pipms.service.impl.RolePermissionServiceImpl;
import com.pipms.shiro.utils.ShiroUtils;
import com.pipms.utils.LdapUtils;
import com.pipms.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @ClassName CommonController
 * @Description TODO
 * @Author 661595
 * @Date 2021/6/2111:04
 * @Version 1.0
 **/
@RestController
public class CommonController {
    @Autowired
    private RolePermissionServiceImpl rolePermissionService;
    @Autowired
    @Qualifier("FileExportService")
    IFileExportService fileExportService;


    @GetMapping("projectTypes")
    public List<String> getProjectType(){
        String[] types={"工艺优化","设计改善","流程改善","管理创新","包装定容改善","布局调整",
                "信息化","设备改造","设备引进","工序前移","工序外移"};
        return Arrays.asList(types);
    }
    @GetMapping("getRoleType")
    public R getRoleType(){
        int[] menuList = rolePermissionService.getMenuList();
        return R.ok().put("menuList",menuList);
    }
    @RequiresPermissions("project:find")
    @PostMapping("exportProjectList")
    public void exportProjectList(@RequestBody Map<String,String> queryMap,HttpServletResponse response){
        fileExportService.exportFileList(queryMap,response);
    }
    @GetMapping("getUserInfo")
    public Map<String,String> getUserInfo(){
        String loginAccount = ShiroUtils.getLoginAccount();
        return LdapUtils.getUserInfo(loginAccount);
    }
    @GetMapping("getRoleMap")
    public List getRoleMap(){
        return rolePermissionService.getRoleMap();
    }
}

