package com.pipms.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pipms.entity.view.UserManageView;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserManageMapper extends BaseMapper<UserManageView> {
    @SelectProvider(type = DynamicDataDaoProvider.class,method = "getView")
    List<UserManageView> getUserView(@Param("unit")String unit,
                                     @Param("userName")String userName,
                                     @Param("userAccount")String userAccount,
                                     @Param("start")int start,
                                     @Param("end")int end);


    class DynamicDataDaoProvider{
        public String getView(@Param("unit")String unit,
                              @Param("userName")String userName,
                              @Param("userAccount")String userAccount,
                              @Param("start")int start,
                              @Param("end")int end){
            return new SQL(){
                {
                    SELECT("user_role.unit,user_name,user_account,role_names,last_login_time,user_role.status account_status,sys_user_online.status online_status");
                    FROM("user_role");
                    LEFT_OUTER_JOIN("sys_user_online ON user_role.user_account=sys_user_online.login_account");
                    if (!"".equals(unit)) WHERE("user_role.unit = #{unit}");
                    if (!"".equals(userName)) WHERE("user_name = #{userName}");
                    if (!"".equals(userAccount)) WHERE("user_account = #{userAccount}");
                    LIMIT("#{start},#{end}");
                }
            }.toString();
        }
    }
}
