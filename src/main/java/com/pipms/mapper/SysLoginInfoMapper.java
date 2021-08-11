package com.pipms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pipms.entity.SysLoginInfo;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @ClassName SysLoginInfoMapper
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2410:28
 * @Version 1.0
 **/
@Repository
public interface SysLoginInfoMapper extends BaseMapper<SysLoginInfo> {
    @Update("truncate table sys_login_info")
    void clearLoginInfo();
}
