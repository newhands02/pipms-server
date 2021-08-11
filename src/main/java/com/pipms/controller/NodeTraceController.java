package com.pipms.controller;

import com.pipms.entity.Node;
import com.pipms.entity.UserRole;
import com.pipms.service.impl.NodeServiceImpl;
import com.pipms.service.impl.ProjectInfoServiceImpl;
import com.pipms.shiro.utils.ShiroUtils;
import com.pipms.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ClassName NodeTraceController
 * @Description 节点信息跟踪控制层
 * @Author 661595
 * @Date 2021/7/111:24
 * @Version 1.0
 **/
@RestController
@RequestMapping("nodeTrace")
public class NodeTraceController {
    @Autowired
    ProjectInfoServiceImpl projectInfoService;
    @Autowired
    NodeServiceImpl nodeService;

    /**
     *@Description 获取起始项目编号
     * **/
    @RequiresPermissions("project:find")
    @GetMapping("getInitProjectNumber")
    public R getInitProjectNumber(){
        UserRole sysUser = ShiroUtils.getSysUser();
        return R.ok().put("projectNumber",projectInfoService.getFistProjectNumberByAccount(sysUser));
    }
    /**
     *@Description 获取时间轴
     * **/
    @RequiresPermissions("project:find")
    @GetMapping("getTraceData")
    public R getProjectTimeline(String projectNumber){
        List<Node> nodeList = nodeService.getNodeList(projectNumber);
        List<Map> nodeTimeLine = nodeService.getNodeTimeLine(nodeList);
        Collections.reverse(nodeTimeLine);
        return R.ok().put("nodeList",nodeList).put("nodeTimeLine",nodeTimeLine);
    }
    /**
     *@Description 获取操作节点信息
     * **/
    @RequiresPermissions("project:find")
    @GetMapping("getApproveNodes")
    public R getApproveNodes(String projectNumber,int currentState){
        List<Node> approveNodes = nodeService.getApproveNodes(projectNumber, currentState);
        return R.ok().put("list",approveNodes);
    }
}
