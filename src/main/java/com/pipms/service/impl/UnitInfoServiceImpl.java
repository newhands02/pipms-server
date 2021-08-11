package com.pipms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pipms.entity.UnitInfo;
import com.pipms.mapper.UnitInfoMapper;
import com.pipms.service.IUnitInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 单位信息表 服务实现类
 * </p>
 *
 * @author xyj
 * @since 2021-06-21
 */
@Service
public class UnitInfoServiceImpl extends ServiceImpl<UnitInfoMapper, UnitInfo> implements IUnitInfoService {

    @Override
    public Page<UnitInfo> queryUnitInfoPage(Map<String, String> queryArgs, int currentPage, int pageSize) {
        Page<UnitInfo> page=new Page<>(currentPage,pageSize);
        QueryWrapper<UnitInfo> queryWrapper=new QueryWrapper<>();
        String unitSection = queryArgs.get("unitSection");
        if (unitSection!=null && !"".equals(unitSection)){
            queryWrapper.eq("unit_section",unitSection);
        }
        String unitSysName = queryArgs.get("unitSysName");
        if (unitSysName!=null && !"".equals(unitSysName)){
            queryWrapper.eq("unit_sys_name",unitSysName);
        }
        String unitCode = queryArgs.get("unitCode");
        if (unitCode!=null && !"".equals(unitCode)){
            queryWrapper.eq("unit_code",unitCode);
        }
        return this.page(page,queryWrapper);
    }

    @Override
    public List<String> getUnitNameList() {
        QueryWrapper<UnitInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("unit_sys_name");
        List<UnitInfo> unitInfoList = this.list(queryWrapper);
        return unitInfoList.stream().map(UnitInfo::getUnitSysName).collect(Collectors.toList());
    }
}
