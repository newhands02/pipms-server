package com.pipms.service.impl;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pipms.entity.ProjectInfo;
import com.pipms.entity.UserRole;
import com.pipms.service.IFileExportService;
import com.pipms.shiro.utils.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FileExportServiceImpl
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2814:47
 * @Version 1.0
 **/
@Service("FileExportService")
public class FileExportServiceImpl implements IFileExportService {
    private static final Logger file_export_logger = LoggerFactory.getLogger("file-export");
    @Autowired
    private ProjectInfoServiceImpl projectInfoService;
    @Override
    public void exportFileList(Map<String, String> queryMap, HttpServletResponse response) {
        UserRole sysUser = ShiroUtils.getSysUser();
        IPage<ProjectInfo> projectByQuery = projectInfoService.getProjectByQuery(sysUser,queryMap, 1, projectInfoService.count());
        List<ProjectInfo> data = projectByQuery.getRecords();
        try {
            OutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode("项目信息清单.xlsx","utf-8"));
            response.setHeader("FileName", URLEncoder.encode("项目信息清单.xlsx","utf-8"));
            response.setHeader("Access-Control-Expose-Headers","FileName");
            ExcelWriterBuilder builder=new ExcelWriterBuilder();
            ExcelWriter writer = builder.excelType(ExcelTypeEnum.XLSX).file(outputStream).build();
            ExcelWriterSheetBuilder sheetBuilder=new ExcelWriterSheetBuilder();
            WriteSheet sheet = sheetBuilder.sheetNo(1).sheetName("项目信息清单").build();
            sheet.setClazz(ProjectInfo.class);
            writer.write(data,sheet);
            writer.finish();
        } catch (IOException e) {
            file_export_logger.error("导出项目信息清单异常");
        }finally {
            try{
                response.getOutputStream().close();
            }catch (Exception e){
                file_export_logger.error("关闭项目信息清单响应输出流异常");
            }
        }
    }

    @Override
    public void exportSummaryList(Map<String, String> queryMap, HttpServletResponse response) {

    }
}
