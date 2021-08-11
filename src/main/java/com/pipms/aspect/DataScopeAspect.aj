package com.pipms.aspect;

import com.pipms.annotation.DataScope;
import com.pipms.entity.UserRole;
import com.pipms.enums.DataScopeEnum;
import com.pipms.shiro.utils.ShiroUtils;
import com.pipms.shiro.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @ClassName DataScopeAspect
 * @Description 数据范围过滤切面
 * @Author 661595
 * @Date 2021/7/269:40
 * @Version 1.0
 **/
@Aspect
@Component
public class DataScopeAspect {
    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    // 配置织入点
    @Pointcut("@annotation(com.pipms.annotation.DataScope)")
    public void dataScopePointCut()
    {
    }
    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable
    {
        handleDataScope(point);
    }

    private void handleDataScope(final JoinPoint joinPoint) {
// 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null)
        {
            return;
        }
// 获取当前的用户
        UserRole currentUser = ShiroUtils.getSysUser();
        if (currentUser != null)
        {
// 如果是超级管理员，则不过滤数据
            if (!currentUser.getRoleNames().contains("super_admin"))
            {
                dataScopeFilter(joinPoint, currentUser);
            }
        }
    }
    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user 用户
     */
    public static void dataScopeFilter(JoinPoint joinPoint, UserRole user)
    {
        String scope="";
        String[] roleTypes = user.getRoleTypes().split(",");
        int maxRole = StringUtils.findMin(roleTypes);
        if (maxRole<5){
            scope= DataScopeEnum.DATA_SCOPE_ALL.getDataScope();
        }else if (maxRole<7){
            scope= DataScopeEnum.DATA_SCOPE_UNIT.getDataScope();
        }else {
            scope= DataScopeEnum.DATA_SCOPE_ACCOUNT.getDataScope();
        }
        UserRole baseEntity = (UserRole) joinPoint.getArgs()[0];
        baseEntity.getParams().put(DATA_SCOPE,scope);
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
