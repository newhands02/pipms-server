package com.pipms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pipms.entity.SysUserOnline;

import java.util.Date;
import java.util.List;

public interface ISysUserOnlineService extends IService<SysUserOnline> {
    /**
     * 通过会话序号查询信息
     *
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    public SysUserOnline selectOnlineById(String sessionId);

    /**
     * 通过会话序号删除信息
     *
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    public boolean deleteOnlineById(String sessionId);

    /**
     * 通过会话序号删除信息
     *
     * @param sessions 会话ID集合
     * @return 在线用户信息
     */
    public boolean batchDeleteOnline(List<String> sessions);

    /**
     * 保存会话信息
     *
     * @param online 会话信息
     */
    public void saveOnline(SysUserOnline online);

    /**
     * 查询会话集合
     *
     * @param userOnline 分页参数
     * @return 会话集合
     */
    public Page<SysUserOnline> selectUserOnlineList(SysUserOnline userOnline,int currentPage,int pageSize);

    /**
     * 强退用户
     *
     * @param sessionId 会话ID
     */
    public boolean forceLogout(String sessionId);

    /**
     * 查询会话集合
     *
     * @param expiredDate 有效期
     * @return 会话集合
     */
    public List<SysUserOnline> selectOnlineByExpired(Date expiredDate);

    /**
     * 用户登出
     *
     * @param userAccount 登录账号
     */
    public boolean logout(String userAccount);
}
