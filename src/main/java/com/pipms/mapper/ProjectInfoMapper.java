package com.pipms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pipms.entity.ProjectInfo;
import com.pipms.entity.view.ProjectResultView;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>
 * 项目信息表 Mapper 接口
 * </p>
 *
 * @author xyj
 * @since 2021-06-21
 */
@Repository
public interface ProjectInfoMapper extends BaseMapper<ProjectInfo> {
    @SelectProvider(type = ProjectInfoMapper.DynamicDataDaoProvider.class,method = "getSummaryView")
    List<ProjectResultView> getSummaryList(@Param("unit")String unit,
                                           @Param("section")String section);

    class DynamicDataDaoProvider{
        public String getSummaryView(@Param("unit")String unit,
                                     @Param("section")String section){
            StringBuffer selectSql=new StringBuffer();
            selectSql.append("SELECT unit,section,SUM(project_result) nonauto_result,any_value(t2.auto_result) auto_result "+
                    "FROM project_info AS t1 "+
                    "LEFT JOIN ( SELECT unit unit2,section section2,SUM(project_result) auto_result "+
                    "FROM project_info "+
                    "WHERE automatic_flag=1 "+
                    "GROUP BY unit,section "+
                    ") AS t2 ON t1.unit=t2.unit2 "+
                    "WHERE automatic_flag=0 ");
            if (!"".equals(unit) && unit!=null) selectSql.append("AND unit = '"+unit+"' ");
            if (!"".equals(section) && section!=null) selectSql.append("AND section = '"+section+"' ");
            selectSql.append("GROUP BY unit,section");
            return selectSql.toString();

        }
    }
}
