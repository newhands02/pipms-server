package com.pipms.entity.view;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName UserManageView
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2816:57
 * @Version 1.0
 **/
public class UserManageView implements Serializable {
    private static final long serialVersionUID = 1L;
    private String unit;
    private String userName;
    private String userAccount;
    private String roleNames;
    private String accountStatus;
    private LocalDateTime lastLoginTime;
    private String onlineStatus;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
