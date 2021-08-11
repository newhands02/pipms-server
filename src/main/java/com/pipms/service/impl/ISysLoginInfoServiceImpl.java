package com.pipms.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pipms.entity.SysLoginInfo;
import com.pipms.mapper.SysLoginInfoMapper;
import com.pipms.service.ISysLoginInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName SysLoginInfoServiceImpl
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2410:27
 * @Version 1.0
 **/
@Service
public class ISysLoginInfoServiceImpl extends ServiceImpl<SysLoginInfoMapper, SysLoginInfo> implements ISysLoginInfoService {

    @Override
    public boolean insertLoginInfo(SysLoginInfo logininfo) {
        return this.save(logininfo);
    }

    @Override
    public List<SysLoginInfo> selectLoginInfoList(SysLoginInfo logininfo) {
        return this.list();
    }

    @Override
    public boolean deleteLoginInfoByIds(List<Integer> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public void cleanLoginInfo() {
        this.getBaseMapper().clearLoginInfo();
    }
}
