package com.pipms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pipms.entity.SysUserOnline;
import com.pipms.enums.OnlineStatus;
import com.pipms.mapper.SysUserOnlineMapper;
import com.pipms.service.ISysUserOnlineService;
import com.pipms.shiro.OnlineWebSessionManager;
import com.pipms.shiro.service.SysShiroService;
import com.pipms.shiro.session.OnlineSessionDAO;
import com.pipms.shiro.utils.StringUtils;
import com.pipms.utils.DateUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SysUserOnlineServiceImpl
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2411:15
 * @Version 1.0
 **/
@Service
public class SysUserOnlineServiceImpl extends ServiceImpl<SysUserOnlineMapper, SysUserOnline>
        implements ISysUserOnlineService {
    @Autowired
    private SysShiroService sysShiroService;
    @Override
    public SysUserOnline selectOnlineById(String sessionId) {
        return this.getById(sessionId);
    }
    @Override
    public void saveOnline(SysUserOnline online) {
        QueryWrapper<SysUserOnline> wrapper=new QueryWrapper<>();
        wrapper.eq("login_account",online.getLoginAccount());
        this.saveOrUpdate(online,wrapper);
    }

    @Override
    public Page<SysUserOnline> selectUserOnlineList(SysUserOnline userOnline,int currentPage,int pageSize) {
        Page<SysUserOnline> page=new Page<>(currentPage,pageSize);
        return this.page(page);
    }

    @Override
    public boolean forceLogout(String userAccount) {
        boolean success=false;
        QueryWrapper<SysUserOnline> wrapper=new QueryWrapper();
        wrapper.eq("login_account",userAccount).eq("status","on_line");
        SysUserOnline one = this.getOne(wrapper, false);
        if (one!=null){
            Serializable sessionId = one.getSessionId();
            Session kickoutSession = sysShiroService.getSession(sessionId);
            if (null != kickoutSession)
            {
// 设置会话的kickout属性表示踢出了
                kickoutSession.setAttribute("kickout", true);
                success=true;
            }
            if (success){
                UpdateWrapper<SysUserOnline> updateWrapper=new UpdateWrapper<>();
                updateWrapper.eq("login_account",userAccount).set("status","off_line");
                this.update(updateWrapper);
            }
        }
        return success;
    }

    @Override
    public List<SysUserOnline> selectOnlineByExpired(Date expiredDate) {
        String lastAccessTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, expiredDate);
        QueryWrapper<SysUserOnline> wrapper=new QueryWrapper<>();
        wrapper.eq("last_access_time",lastAccessTime).orderByAsc("last_access_time");
        return this.list(wrapper);
    }


    @Override
    public boolean deleteOnlineById(String sessionId) {
        SysUserOnline byId = this.getById(sessionId);
        if (StringUtils.isNotNull(byId)){
            return this.removeById(sessionId);
        }
        return false;
    }

    @Override
    public boolean batchDeleteOnline(List<String> sessions) {
        if (!sessions.isEmpty()){
            return this.removeByIds(sessions);
        }
        return false;
    }
    @Override
    public boolean logout(String userAccount) {
        UpdateWrapper<SysUserOnline> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("login_account",userAccount).set("status","off_line");
        return this.update(updateWrapper);
    }
}
