package com.pipms.entity;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ParentEntity
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2314:45
 * @Version 1.0
 **/
public class ParentEntity {

    /** 请求参数 */
    @TableField(exist = false)
    @ExcelIgnore
    private Map<String, String> params;

    public Map<String, String> getParams() {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
