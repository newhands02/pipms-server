package com.pipms.controller;

import com.pipms.service.ISysUserOnlineService;
import com.pipms.shiro.utils.ShiroUtils;
import com.pipms.utils.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName LoginController
 * @Description TODO
 * @Author 661595
 * @Date 2021/6/2414:50
 * @Version 1.0
 **/
@RestController
public class LoginController {
    @Autowired
    private ISysUserOnlineService userOnlineService;

    @GetMapping("login")
    public R login(String account, String password,boolean rememberMe){
        if (account==null || password==null){
            return R.error(401,"账号密码不能为空");
        }
        UsernamePasswordToken token=new UsernamePasswordToken(account,password,rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(token);
            return R.ok("登录成功");
        }catch (AuthenticationException e){
            return R.error(e.getMessage());
        }
    }
    @GetMapping("/unauth")
    public R unauth(){
        return R.error(403,"授权失败");
    }
    @GetMapping("/logout")
    public R logOut(){
        userOnlineService.logout(ShiroUtils.getLoginAccount());
        ShiroUtils.logout();
        return R.ok();
    }
}
