package com.pipms.shiro.service;

import com.pipms.entity.SysUserOnline;
import com.pipms.service.ISysUserOnlineService;
import com.pipms.shiro.session.OnlineSession;
import com.pipms.shiro.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @ClassName ShiroService
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2411:34
 * @Version 1.0
 **/
@Service
public class SysShiroService {
    @Autowired
    private ISysUserOnlineService onlineService;
    /**
     * 删除会话
     *
     * @param onlineSession 会话信息
     */
    public void deleteSession(OnlineSession onlineSession)
    {
        onlineService.deleteOnlineById(String.valueOf(onlineSession.getId()));
    }

    /**
     * 获取会话信息
     *
     * @param sessionId
     * @return
     */
    public Session getSession(Serializable sessionId)
    {
        SysUserOnline userOnline = onlineService.selectOnlineById(String.valueOf(sessionId));
        return StringUtils.isNull(userOnline) ? null : createSession(userOnline);
    }

    public Session createSession(SysUserOnline userOnline)
    {
        OnlineSession onlineSession = new OnlineSession();
        if (StringUtils.isNotNull(userOnline))
        {
            onlineSession.setId(userOnline.getSessionId());
            onlineSession.setHost(userOnline.getIpaddr());
            onlineSession.setBrowser(userOnline.getBrowser());
            onlineSession.setOs(userOnline.getOs());
            onlineSession.setUnit(userOnline.getUnit());
            onlineSession.setLoginName(userOnline.getLoginName());
            onlineSession.setLoginAccount(userOnline.getLoginAccount());
            onlineSession.setStartTimestamp(userOnline.getStartTimestamp());
            onlineSession.setLastAccessTime(userOnline.getLastAccessTime());
            onlineSession.setTimeout(userOnline.getExpireTime());
        }
        return onlineSession;
    }
}
