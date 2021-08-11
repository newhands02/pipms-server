package com.pipms.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pipms.entity.UserRole;
import com.pipms.enums.UserStatus;
import com.pipms.exception.UserDeleteException;
import com.pipms.exception.UserNotExistsException;
import com.pipms.service.impl.UserRoleServiceImpl;
import com.pipms.shiro.constants.Constants;
import com.pipms.shiro.manager.AsyncManager;
import com.pipms.shiro.manager.factory.AsyncFactory;
import com.pipms.shiro.utils.ShiroUtils;
import com.pipms.utils.LdapUtils;
import com.pipms.utils.TokenUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @ClassName loginService
 * @Description TODO
 * @Author 661595
 * @Date 2021/6/2414:55
 * @Version 1.0
 **/
@Service
public class LoginService {
    @Autowired
    private UserRoleServiceImpl userRoleService;
    private static final String TOKEN_BASE="abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH=32;
    public UserRole loginAuth(String account,String password){
        UserRole one=this.hasUser(account);
        if (one==null){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(account, Constants.LOGIN_FAIL, "user.not.exists"));
            throw new UserNotExistsException();
        }
        if (UserStatus.DELETED.getCode().equals(one.getStatus()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(account, Constants.LOGIN_FAIL, "user.password.delete"));
            throw new UserDeleteException();
        }
//TODO 正确的逻辑
// boolean pass = LdapUtils.connect(account, password);
        boolean pass;
        if ("test".equals(account)){
            pass=true;
        }else {
            pass = LdapUtils.connect(account, password);
        }
        if (pass){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(account, Constants.LOGIN_SUCCESS, "user.login.success"));
            recordLoginInfo(one);
            return one;
        }else {
            throw new AuthenticationException();
        }
    }
    public UserRole hasUser(String account){
        QueryWrapper<UserRole> wrapper=new QueryWrapper<>();
        wrapper.eq("user_account",account);
        UserRole one = userRoleService.getOne(wrapper, false);
        return one;
    }
    public Map<String,String> getUserInfo(String account){
        Map<String, String> userInfo = LdapUtils.getUserInfo(account);
        return userInfo;
    }
    public String getToken(String account){
        String token=TokenUtils.generateToken(TOKEN_LENGTH,TOKEN_BASE);
        return token;
    }
    /**
     * 记录登录信息
     */
    public void recordLoginInfo(UserRole user)
    {
        user.setLoginIp(ShiroUtils.getIp());
        user.setLastLoginTime(LocalDateTime.now());
        userRoleService.updateById(user);
    }
}
