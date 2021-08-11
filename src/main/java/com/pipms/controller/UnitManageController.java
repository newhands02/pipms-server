package com.pipms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pipms.entity.UnitInfo;
import com.pipms.service.IUnitInfoService;
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
 * @ClassName UnitManageController
 * @Description 单位信息管理控制层
 * @Author 661595
 * @Date 2021/7/2915:06
 * @Version 1.0
 **/
@RestController
@RequestMapping("unitManage")
public class UnitManageController {

    @Autowired
    private IUnitInfoService unitInfoService;

    @RequiresRoles(value = {"super_admin","sys_admin"},logical = Logical.OR)
    @RequiresPermissions("unit:find")
    @PostMapping("getInfoList")
    public R getUnitInfoList(@RequestBody Map<String,String> queryArgs, int currentPage, int pageSize){
        Page<UnitInfo> page = unitInfoService.queryUnitInfoPage(queryArgs, currentPage, pageSize);
        return R.ok().put("list",page.getRecords()).put("total",page.getTotal());
    }

}
