package com.pipms.exception;

import com.pipms.utils.R;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName NoPermissionExceptionHandler
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2616:29
 * @Version 1.0
 **/
@ControllerAdvice
public class NoPermissionExceptionHandler {
    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public R handleShiroAuthorizationException(Exception ex){
        return R.error(403,"授权失败");
    }
}
