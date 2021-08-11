package com.pipms.shiro.realm;
import com.pipms.entity.UserRole;
import com.pipms.exception.UserDeleteException;
import com.pipms.exception.UserNotExistsException;
import com.pipms.service.IRolePermissionService;
import com.pipms.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName UserRealm
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2215:39
 * @Version 1.0
 **/
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private LoginService loginService;
    @Autowired
    IRolePermissionService rolePermissionService;
    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserRole userRole=(UserRole)principals.getPrimaryPrincipal();//获取的是用户名——账号account
        Set<String> roleSet = rolePermissionService.getRolesByAccount(userRole.getUserAccount());
        Set<String> permSet=new HashSet<>();
        roleSet.forEach(role->permSet.addAll(rolePermissionService.getPermissionByRole(role)));
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roleSet);
        info.addStringPermissions(permSet);
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken uptoken=(UsernamePasswordToken) token;
        String userAccount=uptoken.getUsername();
        String password=String.valueOf(uptoken.getPassword());
        UserRole userRole=null;
        try{
            userRole = loginService.loginAuth(userAccount, password);
        }catch (UserNotExistsException e){
            throw new UnknownAccountException("您不是系统用户", e);
        }catch (UserDeleteException e){
            throw new AccountException("该账号已被停用", e);
        }catch (Exception e)
        {
            log.info("对用户[" + userAccount + "]进行登录验证..验证未通过{}", e.getMessage());
            throw new AuthenticationException("用户名或者密码错误", e);
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userRole,password,this.getName());
        return info;
    }

    /**
     *
     *  清理缓存权限
     *
     */
    public void clearCachedAuthorizationInfo()
    {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
