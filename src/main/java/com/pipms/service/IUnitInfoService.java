package com.pipms.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pipms.entity.UnitInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <p>
 * 单位信息表 服务类
 * </p>
 *
 * @author xyj
 * @since 2021-06-21
 */
public interface IUnitInfoService extends IService<UnitInfo> {
    Page<UnitInfo> queryUnitInfoPage(Map<String,String> queryArgs,int currentPage,int pageSize);
    List<String> getUnitNameList();
}
