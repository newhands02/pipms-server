package com.pipms.service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *@Author xyj
 *@Description 文件导出业务层
 *@Date 14:45 2021/7/28
 *@Param
 *@return **/
public interface IFileExportService {
    void exportFileList(Map<String,String> queryMap, HttpServletResponse response);
    void exportSummaryList(Map<String,String> queryMap, HttpServletResponse response);
}
