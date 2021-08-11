package com.pipms.enums;

/**
 * @ClassName DataScope
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2610:29
 * @Version 1.0
 **/
public enum DataScopeEnum {
    DATA_SCOPE_ALL("all"),DATA_SCOPE_UNIT ("unit"),DATA_SCOPE_ACCOUNT ("account");

    private final String dataScope;
    DataScopeEnum(String dataScope){
        this.dataScope=dataScope;
    }

    public String getDataScope() {
        return dataScope;
    }
}
