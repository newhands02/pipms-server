package com.pipms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.*;
import java.util.stream.Collectors;

public class LdapUtils {
    private static final String ldapFactory = "com.sun.jndi.ldap.LdapCtxFactory";
    private static final String server = "LDAP://10.1.1.1/";
    private static final String baseDN = "ou=格力电器,dc=it2004,dc=gree,dc=com,dc=cn";
    private static LdapContext ctx = null;
    private static Control[] connCtls = null;
    private static Logger logger= LoggerFactory.getLogger(LdapUtils.class);

    // 验证账号密码
    public static boolean connect(String account,String password) {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
        env.put(Context.PROVIDER_URL, server + baseDN);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        String username = "it2004\\"+account;
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        try {
            ctx = new InitialLdapContext(env, connCtls);
            ctx.close();

            return true;
        } catch (Exception e) {
            logger.error("验证失败");
            return false;
        }
    }
    //初始化连接
    private static void init(){
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
        env.put(Context.PROVIDER_URL, server + baseDN);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        String username = "it2004\\661595";
        String password = "Yujie*0909";
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        try{
            ctx = new InitialLdapContext(env, connCtls);
        }catch(Exception e){
            logger.error("初始化连接出错");
        }
    }
    //获取用户信息
    public static Map<String,String> getUserInfo(String userAccount){
        Map<String,String> resultMap=new HashMap<>();
        Attributes attributes = getAttributesByAccount(userAccount);
        if (attributes==null){
            return resultMap;
        }
        try {
            resultMap.put("name",attributes.get("displayName").get().toString());
            resultMap.put("mail",attributes.get("mail").get().toString());
// String memberOf= attributes.get("memberOf").get().toString();
            resultMap.put("company",attributes.get("company").get().toString());
// resultMap.put("department",attributes.get("department").get().toString());
            resultMap.put("unit",attributes.get("department").get().toString());
        } catch (NamingException e) {
            logger.error("获取属性名出错");
        }
        return resultMap;
    }
    //根据账号获取姓名
    public static String getUserNameByAccount(String userAccount){
        Attributes attributes = getAttributesByAccount(userAccount);
        String name="未获取到姓名";
        if (attributes==null){
            return name;
        }
        try{
            name=attributes.get("displayName").get().toString();
        }catch (NamingException e) {
            logger.error("获取姓名出错");
        }
        return name;
    }
    //获取组织成员的账号列表集合
    public static List<String> getMemberAccounts(String departmentAccount){
        List<String> list=new ArrayList<>();
        Attributes attributes = getAttributesByAccount(departmentAccount);
        if (attributes==null){
            return list;
        }
        try {
            String member = attributes.get("member").toString();
            String[] members = member.replace("member:", "").trim().split(",");
            List<String> memberList = Arrays.asList(members);
            list = memberList.stream()
                    .filter(s -> s.trim().startsWith("CN="))
                    .map(s -> s.replace("CN=", "").trim())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     *@Author xyj
     *@Description 通过用户的输入进行模糊匹配，返回displayName列表
     *@Date 9:23 2021/7/9
     *@Param [givenName]
     *@return java.util.List<java.lang.String> **/
    public static List<String> getDisplayNameByGivenName(String givenName){
        String searchFilter="givenName="+givenName+"*";
        return getDisplayNameList(searchFilter);
    }
    public static List<String> getDisplayNameByGivenNameInDepartment(String givenName,String department){
        String searchFiler="(&(givenName="+givenName+"*)(department="+department+"))";
        return getDisplayNameList(searchFiler);
    }
    private static List<String> getDisplayNameList(String searchFilter){
        List<Attributes> resultList = getAttributesByFilter(searchFilter);
        List<String> displayNameList=new ArrayList<>();
        resultList.forEach(attributes -> {
            try {
                displayNameList.add(attributes.get("displayName").get().toString());
            } catch (NamingException e) {
                e.printStackTrace();
            }
        });
        return displayNameList;
    }
    /**
     *@Author xyj
     *@Description 通过用户的输入进行校验，返回displayName
     * 用户输入的如果是givenName将会有多个值，返回第一个
     *@Date 9:22 2021/7/9
     *@Param [inputName]
     *@return java.lang.String **/
    public static Map<String, String> getDisplayNameByInput(String inputName){
        String searchFilter="(|(givenName="+inputName+")(displayName="+inputName+"))";
// String searchFilter="givenName="+inputName+" | displayName="+inputName;
        return getDisplayNameByFilter(searchFilter);
    }
    /**
     *@Author xyj
     *@Description 获取该单位名为inputName的用户displayName
     * 如果有多个，则返回第一个
     *@Date 9:47 2021/7/9
     *@Param [inputName, department]
     *@return java.lang.String **/
    public static Map<String, String> getDepartmentDisplayName(String inputName, String department){
        String searchFilter="(&(department="+department+")(|(givenName="+inputName+")(displayName="+inputName+")))";
// String searchFilter="(givenName="+inputName+" | displayName="+inputName+") & department="+department;
        return getDisplayNameByFilter(searchFilter);
    }
    //通过搜索条件来获取displayName
    private static Map<String, String> getDisplayNameByFilter(String searchFilter){
        String displayName="";
        String mail="";
        List<Attributes> names = getAttributesByFilter(searchFilter);
        if (!names.isEmpty()){
            try {
                Attributes attribute = names.get(0);
                displayName=attribute.get("displayName").get().toString();
                mail=attribute.get("mail").get().toString();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        Map<String,String> map=new HashMap<>();
        map.put("displayName",displayName);
        map.put("mail",mail);
        return map;
    }
    //通过账号来获取属性集合
    public static Attributes getAttributesByAccount(String userAccount){
        String searchFilter = "sAMAccountName="+userAccount;
        List<Attributes> resultList = getAttributesByFilter(searchFilter);
        return resultList.get(0);
    }

    //获取搜索条件来获取属性集合
    private static List<Attributes> getAttributesByFilter(String searchFilter){
        init();
        SearchControls constraint=new SearchControls();
        constraint.setSearchScope(SearchControls.SUBTREE_SCOPE);
// Attributes attributes=null;
        List<Attributes> attributesList=new ArrayList<>();
        try {
            NamingEnumeration<SearchResult> search = ctx.search("", searchFilter, constraint);
// if (search!=null && search.hasMoreElements()){
            while (search!=null && search.hasMoreElements()){
                Object obj=search.nextElement();
                if(obj instanceof SearchResult){
                    SearchResult sr=(SearchResult)obj;
// attributes = sr.getAttributes();
                    attributesList.add(sr.getAttributes());
                }
            }
        }catch (NamingException e){
            logger.error("该用户不存在");
        }finally {
            try {
                ctx.close();
            } catch (NamingException e) {
                logger.error("关闭初始化连接出错");
            }
        }
// return attributes;
        return attributesList;
    }
}

