package com.pipms.controller;

import com.pipms.utils.LdapUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName LdapController
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/1011:21
 * @Version 1.0
 **/
@RestController
public class LdapController {
    @GetMapping("getDisplayNames")
    public List getDisplayName(String queryName){
        List result= LdapUtils.getDisplayNameByGivenName(queryName).stream().map(oldStr -> {
            HashMap<String,String> map = new HashMap();
            map.put("value",oldStr);
            return map;
        }).collect(Collectors.toList());

        return result;
    }
    @GetMapping("getDisplayName")
    public Map<String, String> getNameByInput(String inputName){
        return LdapUtils.getDisplayNameByInput(inputName);
    }
    @GetMapping("getRealNameInDepartment")
    public Map<String, String> getNameInDepartment(String inputName, String department){
        return LdapUtils.getDepartmentDisplayName(inputName,department);
    }
    @GetMapping("getDisplayNamesInDepartment")
    public List<Map> getDisplayNames(String queryName, String department){
        return LdapUtils.getDisplayNameByGivenNameInDepartment(queryName,department).stream()
                .map(oldStr->{
                    HashMap<String,String> map = new HashMap();
                    map.put("value",oldStr);
                    return map;
                }).collect(Collectors.toList());
    }
}
