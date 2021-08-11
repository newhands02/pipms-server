package com.pipms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pipms.entity.Node;
import com.pipms.mapper.NodeMapper;
import com.pipms.service.INodeService;
import com.pipms.utils.LdapUtils;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName NodeServiceImpl
 * @Description TODO
 * @Author 661595
 * @Date 2021/6/2816:22
 * @Version 1.0
 **/
@Service
public class NodeServiceImpl extends ServiceImpl<NodeMapper, Node> implements INodeService {
    @Override
    public boolean insertNode(String projectNumber, int currentState, String operation, String account,String comment) {
        Node node=new Node();
        node.setProjectNumber(projectNumber);
        node.setCurrentState(currentState);
        node.setOperation(operation);
        node.setAccount(account);
        if ("test".equals(account)){
            node.setOperator("test");
        }else {
            node.setOperator(LdapUtils.getUserNameByAccount(account));
        }

        node.setComment(comment);
        node.setOperateTime(LocalDateTime.now());
        boolean save = this.save(node);
        return save;
    }

    @Override
    public List<Node> getNodeList(String projectNumber) {
        QueryWrapper<Node> wrapper=new QueryWrapper<>();
        wrapper.eq("project_number",projectNumber).orderByAsc("operate_time");
        List<Node> list = this.list(wrapper);
        return list;
    }
    @Override
    public List<Map> getNodeTimeLine(List<Node> nodeList) {
        List<Map> timelineList=new LinkedList<>();
        nodeList.forEach(node -> {
            Map<String,String> nodeMap=getNodeMap(node);
            timelineList.add(nodeMap);
        });
        return timelineList;
    }
    public List<Node> getApproveNodes(String projectNumber,int currentState){
        QueryWrapper<Node> wrapper=new QueryWrapper<>();
        wrapper.eq("project_number",projectNumber);
// wrapper.eq("project_number",projectNumber).eq("current_state",currentState);
        List<Node> list = this.list(wrapper);
        return list;
    }

    private Map<String, String> getNodeMap(Node node) {
        Map<String,String> map=new HashMap<>();
        map.put("content",node.getOperation());
        map.put("timestamp",node.getOperateTime().toString());
        String color="";
        switch (node.getCurrentState()){
            case 1:color="blue";break;
            case 2:color="green";break;
            case 3:color="#0bbd87";break;
            case 4:color="yellow";break;
            case 5:color="orange";break;
            default:break;
        }
        if (!"".equals(color)){
            map.put("color",color);
        }
        return map;
    }
}
