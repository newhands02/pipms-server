package com.pipms.entity.view;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName ProjectList
 * @Description TODO
 * @Author 661595
 * @Date 2021/6/2515:39
 * @Version 1.0
 **/
@Data
@TableName("project_list")
public class ProjectList implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 所属版块
     */
    private String section;

    /**
     * 落地单位
     */
    private String unit;
    /**
     * 单位代号+年月+4位数字
     */
    private String projectNumber;
    /**
     * 项目提出人
     */
    private String projectSponsor;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 结题时间
     */

    private String finishTime;

    /**
     * 当前状态
     */
    private String currentState;
}
