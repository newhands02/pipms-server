package com.pipms.enums;

public enum RoleNameEnum {
    SUPER_ADMIN(1,"超级管理员","super_admin"),
    SYS_ADMIN(2,"系统管理员","sys_admin"),
    COMPANY_ADMIN(3,"公司级管理员","company_admin"),
    PRE_COLLABORATOR(4,"高级协作","pre_collaborator"),
    UNIT_AUDITOR(5,"单位审核员","unit_auditor"),
    UNIT_ADMIN(6,"单位管理员","unit_admin"),
    PROMOTER(7,"推进人","promoter"),
    SPONSOR(8,"提出人","sponsor");


    private final int roleType;
    private final String roleName;
    private final String roleNameEN;

    RoleNameEnum(int roleType, String roleName,String roleNameEN)
    {
        this.roleType=roleType;
        this.roleName=roleName;
        this.roleNameEN=roleNameEN;
    }

    public int getRoleType() {
        return roleType;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleNameEN() {
        return roleNameEN;
    }

    public static RoleNameEnum matchType(int roleType){
        RoleNameEnum result=SPONSOR;
        switch (roleType){
            case 1:result=SUPER_ADMIN;break;
            case 2:result=SYS_ADMIN;break;
            case 3:result=COMPANY_ADMIN;break;
            case 4:result=PRE_COLLABORATOR;break;
            case 5:result=UNIT_AUDITOR;break;
            case 6:result=UNIT_ADMIN;break;
            case 7:result=PROMOTER;break;
            case 8:result=SPONSOR;break;
            default:break;
        }
        return result;
    }
}
