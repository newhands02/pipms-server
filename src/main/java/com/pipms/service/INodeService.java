package com.pipms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pipms.entity.Node;
import java.util.List;
import java.util.Map;

public interface INodeService extends IService<Node> {
    boolean insertNode(String projectNumber,int currentState,String operation,String account,String comment);
    List<Map> getNodeTimeLine(List<Node> nodeList);
    List<Node> getNodeList(String projectNumber);
}
