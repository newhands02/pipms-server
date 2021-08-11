package com.pipms.shiro.manager.factory;
import java.time.LocalDateTime;
import java.util.TimerTask;

import com.pipms.entity.SysLoginInfo;
import com.pipms.entity.SysUserOnline;
import com.pipms.service.impl.ISysLoginInfoServiceImpl;
import com.pipms.service.impl.SysUserOnlineServiceImpl;
import com.pipms.shiro.constants.Constants;
import com.pipms.shiro.session.OnlineSession;
import com.pipms.shiro.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 异步工厂（产生任务用）
 *
 * @author liuhulu
 *
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 同步session到数据库
     *
     * @param session 在线用户会话
     * @return 任务task
     */
    public static TimerTask syncSessionToDb(final OnlineSession session)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                SysUserOnline online = new SysUserOnline();
                online.setSessionId(String.valueOf(session.getId()));
                online.setUnit(session.getUnit());
                online.setLoginName(session.getLoginName());
                online.setLoginAccount(session.getLoginAccount());
                online.setStartTimestamp(session.getStartTimestamp());
                online.setLastAccessTime(session.getLastAccessTime());
                online.setExpireTime(session.getTimeout());
                online.setIpaddr(session.getHost());
                online.setLoginLocation(AddressUtils.getRealAddressByIP(session.getHost()));
                online.setBrowser(session.getBrowser());
                online.setOs(session.getOs());
                online.setStatus(session.getStatus());
                SpringUtils.getBean(SysUserOnlineServiceImpl.class).saveOnline(online);

            }
        };
    }
    /**
     * 记录登陆信息
     *
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message, final Object... args)
    {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = ShiroUtils.getIp();
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
// 打印信息到日志
                sys_user_logger.info(s.toString(), args);
// 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
// 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
// 封装对象
                SysLoginInfo logininfor = new SysLoginInfo();
                logininfor.setLoginName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                logininfor.setLoginTime(LocalDateTime.now());
// 日志状态
                if (Constants.LOGIN_SUCCESS.equals(status) || Constants.LOGOUT.equals(status))
                {
                    logininfor.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    logininfor.setStatus(Constants.FAIL);
                }
// 插入数据
                SpringUtils.getBean(ISysLoginInfoServiceImpl.class).insertLoginInfo(logininfor);
            }
        };
    }
}
