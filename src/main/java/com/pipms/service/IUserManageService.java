package com.pipms.service;

import com.pipms.entity.view.UserManageView;

import java.util.List;
import java.util.Map;

public interface IUserManageService {

    List<UserManageView> getViewList(Map<String,String> queryArgs,int currentPage,int pageSize);
    boolean addUser(Map userInfo);
    boolean stopAccount(String account);
    boolean allocateRoles(String userAccount,List<Integer> typeList);
}
