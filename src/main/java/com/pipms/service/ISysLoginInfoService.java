package com.pipms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pipms.entity.SysLoginInfo;

import java.util.List;

public interface ISysLoginInfoService extends IService<SysLoginInfo> {
    /**
     * 新增系统登录日志
     *
     * @param logininfo 访问日志对象
     */
    public boolean insertLoginInfo(SysLoginInfo logininfo);

    /**
     * 查询系统登录日志集合
     *
     * @param logininfo 访问日志对象
     * @return 登录记录集合
     */
    public List<SysLoginInfo> selectLoginInfoList(SysLoginInfo logininfo);

    /**
     * 批量删除系统登录日志
     *
     * @param ids 需要删除的数据
     * @return
     */
    public boolean deleteLoginInfoByIds(List<Integer> ids);
    /**
     * 清空系统登录日志
     */
    public void cleanLoginInfo();

}
